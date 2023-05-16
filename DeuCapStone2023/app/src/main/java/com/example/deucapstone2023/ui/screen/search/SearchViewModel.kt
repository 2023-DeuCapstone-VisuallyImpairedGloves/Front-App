package com.example.deucapstone2023.ui.screen.search

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deucapstone2023.R
import com.example.deucapstone2023.domain.model.POIModel
import com.example.deucapstone2023.domain.usecase.POIUsecase
import com.example.deucapstone2023.domain.usecase.RouteUsecase
import com.example.deucapstone2023.ui.base.PointType
import com.example.deucapstone2023.ui.screen.search.state.Location
import com.example.deucapstone2023.ui.screen.search.state.POIState
import com.example.deucapstone2023.ui.screen.search.state.RouteState
import com.example.deucapstone2023.ui.screen.search.state.toPOIListState
import com.example.deucapstone2023.ui.screen.search.state.toRouteListState
import com.example.deucapstone2023.utils.catchFetching
import com.skt.tmap.MapUtils
import com.skt.tmap.TMapPoint
import com.skt.tmap.overlay.TMapMarkerItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@Stable
sealed class SearchEventFlow {
    object Loading : SearchEventFlow()
    data class POIList(val poiList: List<POIState>) : SearchEventFlow()
    data class RouteList(val routeList: List<RouteState>) : SearchEventFlow()
}

@Stable
data class SearchUiState(
    val location: Location,
    val routeIndex: Int,
    val recentDistance : Int,
    val destinationInfo : POIState,
    val routeList: List<RouteState>
    ) {
        companion object {
            fun getInitValues(lat: Double = 35.15130665819491, lon: Double = 129.02657807928898) = SearchUiState(
                location = Location(latitude = lat, longitude = lon),
                routeIndex = 0,
                recentDistance = 0,
                destinationInfo = POIState.getInitValues(),
                routeList = emptyList()
            )
        }
    }

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val poiUsecase: POIUsecase,
    private val routeUsecase: RouteUsecase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _searchEventFlow = MutableSharedFlow<SearchEventFlow>()
    val searchEventFlow get() = _searchEventFlow.asSharedFlow()

    private val _searchUiState = MutableStateFlow(SearchUiState.getInitValues())
    val searchUiState get() = _searchUiState.asStateFlow()

    fun setUserLocation(lat: Double, lon: Double) = _searchUiState.update { state ->
        state.copy(location = Location(latitude = lat, longitude = lon))
    }

    fun setDestinationInfo(poi: POIState) = _searchUiState.update { state ->
        state.copy(destinationInfo = poi)
    }

    fun navigateRouteOnMap(voiceOutput: (String) -> Unit) {
        val route = searchUiState.value.routeList[searchUiState.value.routeIndex]

        if(searchUiState.value.recentDistance + 7 >= getDistanceFromHere(lat = route.destinationLatitude, lon = route.destinationLongitude)) {
            // 정상경로 -> LineString 정보를 활용 해서 현 위치 에서의 description 을 안내 해야함
            _searchUiState.update { state -> state.copy(recentDistance = getDistanceFromHere(lat = route.destinationLatitude, lon = route.destinationLongitude)) }

            // point 없이 linestring이 이어서 결합된 경우 -> 지정 description 안내 후 남은 거리 만큼 이동 추가 안내
            if(route.totalDistance != route.description.filter { it.isDigit() }.toInt()) {
                if(route.description.filter { it.isDigit() }.toInt() > (route.totalDistance - searchUiState.value.recentDistance))
                    voiceOutput("${route.description}해 주세요")
                else {
                    //남은거리 안내
                    if(searchUiState.value.recentDistance > 30) //18미터 초과라면 남은거리 안내
                        voiceOutput("보행자 경로를 따라 ${searchUiState.value.recentDistance}m 직진해 주세요")
                    else { // 남은 거리가 18미터 이하라면 다음경로 안내
                        guideRemainDistance(voiceOutput = voiceOutput)
                    }
                }
            } else { // point 와 linestring 이 1:1 매칭 인 경우
                if(route.totalDistance - searchUiState.value.recentDistance <= 10) // 이동한 거리가 12미터 이하 일 때
                    voiceOutput("${route.description}해 주세요")
                else {
                    if(searchUiState.value.recentDistance > 30) { //이동한 거리가 12미터 초과 이고, 남은 거리가 18미터 초과일 때
                        voiceOutput("${searchUiState.value.recentDistance}m 직진해 주세요")
                    } else { //이동한 거리가 2미터 초과 이고, 남은 거리가 18미터 이하 일 때
                        guideRemainDistance(voiceOutput = voiceOutput)
                    }
                }
            }
        } else {
            //경로 재요청
            requestPedestrianRoute(voiceOutput = voiceOutput)
            Toast.makeText(context, "total : ${searchUiState.value.recentDistance}, 거리: ${getDistanceFromHere(lat = route.destinationLatitude, lon = route.destinationLongitude)}",Toast.LENGTH_SHORT).show()
        }
    }

    private fun guideRemainDistance(voiceOutput : (String) -> Unit) {
        when(searchUiState.value.routeList[searchUiState.value.routeIndex + 1].pointType) {
            PointType.EP -> { // 목적지 라면, 목적지 안내
                if(searchUiState.value.recentDistance <= 15) {
                    voiceOutput("목적지에 부근에 도착했습니다. 경로안내를 종료합니다.")
                    _searchUiState.update { state -> state.copy(routeList = emptyList()) }
                } else {
                    voiceOutput("${searchUiState.value.recentDistance}m 직진해 주세요. 이어서 목적지가 있습니다.")
                }
            }
            else -> { // 목적지가 아니면, 현재 남은 거리 안내후 다음경로 안내
                voiceOutput("${searchUiState.value.recentDistance}m 직진해 주세요. 이어서 ${searchUiState.value.routeList[searchUiState.value.routeIndex+1].description}해 주세요")
                if(searchUiState.value.recentDistance <= 15)  // 이동한 거리가 12미터 초과 이고, 남은 거리가 12미터 미만 일 때
                    _searchUiState.update { state ->
                        state.copy(
                            routeIndex = state.routeIndex + 1,
                            recentDistance = searchUiState.value.routeList[searchUiState.value.routeIndex + 1].totalDistance + searchUiState.value.recentDistance
                        )
                    }
            }
        }
    }

    private fun getDistanceFromHere(lat: Double, lon: Double) =
        MapUtils.getDistance(
            TMapPoint(searchUiState.value.location.latitude,searchUiState.value.location.longitude),
            TMapPoint(lat,lon)
        ).toInt()

    private fun requestPedestrianRoute(voiceOutput: (String) -> Unit) {
        getRoutePedestrian(appKey = context.getString(R.string.T_Map_key))
        voiceOutput("경로를 이탈 했습니다. 경로를 재 요청 합니다.")
    }

    fun searchPlaceOnTyping(appKey: String, place: String) =
        poiUsecase.getPOISearch(
            poiModel = POIModel(
                name = place,
                centerLat = searchUiState.value.location.latitude,
                centerLon = searchUiState.value.location.longitude,
                radius = "0",
                searchtypCd = "R",
                bizName = "",
                address = "",
                id = 0L
            ),
            appKey = appKey
        ).onEach { poiList ->
            val poi = poiList.toPOIListState().first()
            setDestinationInfo(poi)
            getRoutePedestrian(context.getString(R.string.T_Map_key))
        }.catchFetching(
            onFailedHttpException = {},
            onFailedElseException = {}
        ).launchIn(viewModelScope)

    fun searchPlace(appKey: String, place: String) =
        poiUsecase.getPOISearch(
            poiModel = POIModel(
                name = place,
                centerLat = searchUiState.value.location.latitude,
                centerLon = searchUiState.value.location.longitude,
                radius = "0",
                searchtypCd = "R",
                bizName = "",
                address = "",
                id = 0L
            ),
            appKey = appKey
        ).onEach { poiList ->
            setDestinationInfo(poiList.toPOIListState().first())
            emitEventFlow(SearchEventFlow.POIList(poiList.toPOIListState()))
        }.catchFetching(
            onFailedHttpException = {},
            onFailedElseException = {}
        ).launchIn(viewModelScope)

    fun getPoiMarkers(poiList: List<POIState>): List<TMapMarkerItem> {
        val markers = mutableListOf<TMapMarkerItem>()
        poiList.onEach { poi ->
            markers.add(makeMarker(poi))
        }
        return markers
    }

    fun getPoiMarker(index: Int, poiList: List<POIState>): TMapMarkerItem =
        poiList[index].run {
            makeMarker(this)
        }


    fun makeMarker(poi: POIState) =
        TMapMarkerItem().apply {
            icon =
                BitmapFactory.decodeResource(context.resources, R.drawable.ic_pin_blue_a_midium)
            tMapPoint = TMapPoint(poi.latitude, poi.longitude)
            name = poi.name
            id = poi.name
        }

    fun getRoutePedestrian(appKey: String) =
        routeUsecase.getRoutePedestrian(
            appKey = appKey,
            startLatitude = searchUiState.value.location.latitude,
            startLongitude = searchUiState.value.location.longitude,
            destinationPoiId = searchUiState.value.destinationInfo.id.toString(),
            destinationLatitude = searchUiState.value.destinationInfo.latitude,
            destinationLongitude = searchUiState.value.destinationInfo.longitude
        ).onEach { routeModels ->
            val route = routeModels.toRouteListState()
            _searchUiState.update { state -> state.copy(routeList = route, recentDistance = route.first().totalDistance, routeIndex = 0) }
            emitEventFlow(
                SearchEventFlow.RouteList(
                    route.filter { it.lineInfo.isNotEmpty() })
            )
        }.catchFetching(
            onFailedHttpException = {e ->
                Log.d("tests","error : ${e.message}, ${e.printStackTrace()}}")
            },
            onFailedElseException = {e ->
                Log.d("tests","error : ${e.message} , ${e.printStackTrace()}}")
            }
        ).launchIn(viewModelScope)

    private fun emitEventFlow(state: SearchEventFlow) {
        viewModelScope.launch {
            _searchEventFlow.emit(state)
        }
    }
}

