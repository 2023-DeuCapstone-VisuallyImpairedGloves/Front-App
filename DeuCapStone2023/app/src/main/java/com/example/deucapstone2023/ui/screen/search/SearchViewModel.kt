package com.example.deucapstone2023.ui.screen.temp

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deucapstone2023.R
import com.example.deucapstone2023.domain.model.POIModel
import com.example.deucapstone2023.domain.usecase.POIUsecase
import com.example.deucapstone2023.domain.usecase.RouteUsecase
import com.example.deucapstone2023.ui.screen.search.state.Location
import com.example.deucapstone2023.ui.screen.search.state.POIState
import com.example.deucapstone2023.ui.screen.search.state.RouteState
import com.example.deucapstone2023.ui.screen.search.state.toPOIListState
import com.example.deucapstone2023.ui.screen.search.state.toRouteListState
import com.example.deucapstone2023.utils.catchFetching
import com.skt.tmap.TMapPoint
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.poi.TMapPOIItem
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
    val recentDistance : Double,
    val destinationName : String
    ) {
        companion object {
            fun getInitValues(lat: Double = .0, lon: Double = .0) = SearchUiState(
                location = Location(latitude = lat, longitude = lon),
                routeIndex = 0,
                recentDistance = 0.0,
                destinationName = ""
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

    fun navigateRouteOnMap(routeList: List<RouteState>, voiceOutput: (String) -> Unit) {
        val route = routeList[searchUiState.value.routeIndex]
        val userPosition = TMapPOIItem().apply {
            noorLat = searchUiState.value.location.latitude.toString()
            noorLon = searchUiState.value.location.longitude.toString()
        }

        if(userPosition.getDistance(TMapPoint(route.startLatitude, route.startLongitude)) >= 0) {
            // 정상경로
            if(userPosition.getDistance(TMapPoint(route.destinationLatitude, route.destinationLongitude)) <= 0.0) {
                // 다음경로
                _searchUiState.update { state -> state.copy(routeIndex = searchUiState.value.routeIndex + 1) }
            }else {
                if(searchUiState.value.recentDistance >= userPosition.getDistance(TMapPoint(route.destinationLatitude, route.destinationLongitude))) {
                    // 정상경로 -> LineString 정보를 활용해서 현위치에서의 description 을 안내해야함
                    _searchUiState.update { state -> state.copy(recentDistance = userPosition.getDistance(TMapPoint(route.destinationLatitude, route.destinationLongitude))) }

                    // point 없이 linestring이 이어서 결합된 경우 -> 지정 description 안내 후 남은 거리만큼 이동 추가 안내
                    if(route.totalDistance - searchUiState.value.recentDistance > route.description.filter { it.isDigit() }.toInt()) {
                        if(route.description.filter { it.isDigit() }.toInt() > searchUiState.value.recentDistance)
                            voiceOutput("${route.description}해 주세요")
                        else
                            voiceOutput("보행자 경로를 따라 ${route.totalDistance - searchUiState.value.recentDistance} 미터 이동해 주세요")
                    } else { // point 와 linestring 이 1:1 매칭인경우
                        if(searchUiState.value.recentDistance <= 2)
                            voiceOutput("${route.description.split("후")[1]}해 주세요")
                        else {
                            when(route.description == "도착") {
                                true -> {
                                    voiceOutput("목적지에 도착하였습니다. 경로안내를 종료합니다.")
                                }
                                false -> {
                                    voiceOutput("${route.description}해 주세요")
                                }
                            }
                        }
                    }
                }
                else {
                    //경로 재요청
                    searchPlace(context.getString(R.string.T_Map_key),searchUiState.value.destinationName)
                    voiceOutput("경로를 이탈 했습니다. 경로를 재 요청 합니다.")
                }
            }
        } else {
            //경로 재요청
            searchPlace(context.getString(R.string.T_Map_key),searchUiState.value.destinationName)
            voiceOutput("경로를 이탈 했습니다. 경로를 재 요청 합니다.")
        }
    }

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
            _searchUiState.update { state -> state.copy(destinationName = place) }
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

    fun getRoutePedestrian(
        appKey: String,
        destinationPoiId: String,
        destinationLatitude: Double,
        destinationLongitude: Double
    ) =
        routeUsecase.getRoutePedestrian(
            appKey = appKey,
            startLatitude = searchUiState.value.location.latitude,
            startLongitude = searchUiState.value.location.longitude,
            destinationLatitude = destinationLatitude,
            destinationLongitude = destinationLongitude,
            destinationPoiId = destinationPoiId
        ).onEach { routeModels ->
            emitEventFlow(
                SearchEventFlow.RouteList(
                    routeModels.toRouteListState().filter { it.lineInfo.isNotEmpty() })
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

