package com.deucapstone2023.ui.screen.search

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deucapstone2023.design.R
import com.deucapstone2023.domain.domain.model.getAzimuthFromValue
import com.deucapstone2023.ui.screen.search.state.Location
import com.deucapstone2023.ui.screen.search.state.POIState
import com.deucapstone2023.ui.screen.search.state.RouteState
import com.deucapstone2023.ui.screen.search.state.toLineModel
import com.deucapstone2023.ui.screen.search.state.toPOIListState
import com.deucapstone2023.ui.screen.search.state.toRouteListModel
import com.deucapstone2023.ui.screen.search.state.toRouteListState
import com.deucapstone2023.ui.utils.catchFetching
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
}

@Stable
data class SearchUiState(
    val location: Location,
    val routeList: List<RouteState>,
    val destinationInfo: POIState
) {
    companion object {
        fun getInitValues(lat: Double = 35.15130665819491, lon: Double = 129.02657807928898) =
            SearchUiState(
                location = Location(latitude = lat, longitude = lon),
                routeList = emptyList(),
                destinationInfo = POIState.getInitValues()
            )
    }
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val poiUsecase: com.deucapstone2023.domain.domain.usecase.POIUsecase,
    private val routeUsecase: com.deucapstone2023.domain.domain.usecase.RouteUsecase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _searchEventFlow = MutableSharedFlow<SearchEventFlow>()
    val searchEventFlow get() = _searchEventFlow.asSharedFlow()

    private val _searchUiState = MutableStateFlow(SearchUiState.getInitValues())
    val searchUiState get() = _searchUiState.asStateFlow()

    fun setUserLocation(lat: Double, lon: Double) = _searchUiState.update { state ->
        state.copy(location = Location(latitude = lat, longitude = lon))
    }

    fun setDestinationInfo(destinationInfo: POIState) = _searchUiState.update { state ->
        state.copy(destinationInfo = destinationInfo)
    }

    fun navigateRouteOnMap(
        azimuth: Double,
        quitNavigation: () -> Unit,
        voiceOutput: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                routeUsecase.navigateRouteOnMap(
                    routeList = searchUiState.value.routeList.toRouteListModel(),
                    source = searchUiState.value.location.toLineModel(),
                    azimuth = getAzimuthFromValue(azimuth),
                    voiceOutput = voiceOutput,
                    quitNavigation = {
                        _searchUiState.update { state -> state.copy(routeList = emptyList()) }
                        quitNavigation()
                    },
                    requestPedestrianRoute = { requestPedestrianRoute(voiceOutput) }
                )
            } catch (e: Exception) {
                when (e) {
                    is IndexOutOfBoundsException -> {
                        Toast.makeText(
                            context,
                            "index: ",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    else -> {
                        Toast.makeText(context, "${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun requestPedestrianRoute(
        voiceOutput: (String) -> Unit
    ) {
        getRoutePedestrian(
            appKey = context.getString(R.string.T_Map_key),
            guideVoiceMessage = {
                voiceOutput("경로를 이탈 했습니다. 경로를 재 요청 합니다.")
            }
        )
    }

    fun searchPlaceOnTyping(appKey: String, place: String, voiceOutput: (String) -> Unit) =
        poiUsecase.getPOISearch(
            poiModel = com.deucapstone2023.domain.domain.model.POIModel(
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
            getRoutePedestrian(
                appKey = context.getString(R.string.T_Map_key),
                guideVoiceMessage = {
                    voiceOutput("경로 안내를 시작합니다.")
                }
            )
        }.catchFetching(
            onFailedHttpException = {},
            onFailedElseException = {}
        ).launchIn(viewModelScope)

    fun searchPlace(appKey: String, place: String) =
        poiUsecase.getPOISearch(
            poiModel = com.deucapstone2023.domain.domain.model.POIModel(
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
        guideVoiceMessage: () -> Unit
    ) =
        routeUsecase.getRoutePedestrian(
            appKey = appKey,
            startLatitude = searchUiState.value.location.latitude,
            startLongitude = searchUiState.value.location.longitude,
            destinationPoiId = searchUiState.value.destinationInfo.id.toString(),
            destinationLatitude = searchUiState.value.destinationInfo.latitude,
            destinationLongitude = searchUiState.value.destinationInfo.longitude
        ).onEach { routeModels ->
            val route = routeModels.toRouteListState()
            _searchUiState.update { state ->
                state.copy(
                    routeList = route,
                )
            }
            routeUsecase.setInitNavigation(route.first().totalDistance)
            guideVoiceMessage()

        }.catchFetching(
            onFailedHttpException = { e ->
                Log.d("tests", "error : ${e.message}, ${e.printStackTrace()}}")
            },
            onFailedElseException = { e ->
                Log.d("tests", "error : ${e.message} , ${e.printStackTrace()}}")
            }
        ).launchIn(viewModelScope)

    private fun emitEventFlow(state: SearchEventFlow) {
        viewModelScope.launch {
            _searchEventFlow.emit(state)
        }
    }
}

