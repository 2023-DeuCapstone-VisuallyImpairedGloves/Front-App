package com.example.deucapstone2023.ui.screen.search

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deucapstone2023.R
import com.example.deucapstone2023.data.datasource.local.database.entity.AzimuthSensor
import com.example.deucapstone2023.data.datasource.local.database.entity.UserLocation
import com.example.deucapstone2023.domain.model.POIModel
import com.example.deucapstone2023.domain.usecase.LogUsecase
import com.example.deucapstone2023.domain.usecase.POIUsecase
import com.example.deucapstone2023.domain.usecase.RouteUsecase
import com.example.deucapstone2023.ui.base.getAzimuthFromValue
import com.example.deucapstone2023.ui.screen.list.state.SensorInfo
import com.example.deucapstone2023.ui.screen.list.state.toAzimuthSensor
import com.example.deucapstone2023.ui.screen.list.state.toDistanceSensor
import com.example.deucapstone2023.ui.screen.search.state.Location
import com.example.deucapstone2023.ui.screen.search.state.NavigationManager
import com.example.deucapstone2023.ui.screen.search.state.POIState
import com.example.deucapstone2023.ui.screen.search.state.RouteState
import com.example.deucapstone2023.ui.screen.search.state.toPOIListState
import com.example.deucapstone2023.ui.screen.search.state.toRouteListState
import com.example.deucapstone2023.utils.catchFetching
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
    val routeList: List<RouteState>
) {
    companion object {
        fun getInitValues(lat: Double = 35.15130665819491, lon: Double = 129.02657807928898) =
            SearchUiState(
                location = Location(latitude = lat, longitude = lon),
                routeList = emptyList()
            )
    }
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val poiUsecase: POIUsecase,
    private val routeUsecase: RouteUsecase,
    private val logUsecase: LogUsecase,
    val navigationManager: NavigationManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _searchEventFlow = MutableSharedFlow<SearchEventFlow>()
    val searchEventFlow get() = _searchEventFlow.asSharedFlow()

    private val _searchUiState = MutableStateFlow(SearchUiState.getInitValues())
    val searchUiState get() = _searchUiState.asStateFlow()

    fun setUserLocation(lat: Double, lon: Double) = _searchUiState.update { state ->
        state.copy(location = Location(latitude = lat, longitude = lon))
    }

    fun setDestinationInfo(poi: POIState) {
        navigationManager.destinationInfo = poi
    }

    fun navigateRouteOnMap(azimuth: Double,quitNavigation: () -> Unit, voiceOutput: (String) -> Unit) {
        try{ navigationManager.navigateRouteOnMap(
            routeList = searchUiState.value.routeList,
            source = searchUiState.value.location,
            azimuth = getAzimuthFromValue(azimuth),
            voiceOutput = voiceOutput,
            quitNavigation = {
                _searchUiState.update { state -> state.copy(routeList = emptyList()) }
                quitNavigation()
            },
            requestPedestrianRoute = { requestPedestrianRoute(voiceOutput) },
            context = context,
            setUserLocationOnDatabase = { userLocation -> setUserLocationOnDatabase(userLocation) },
            setAzimuthSensorOnDatabase = { sensorInfo -> setAzimuthSensorOnDatabase(sensorInfo)},
            setIndex = {index -> setIndex(index)}
        ) }
        catch (e:Exception) {
            when(e) {
                is IndexOutOfBoundsException -> {
                    Toast.makeText(context,"index: ${navigationManager.recentLineInfoIndex}",Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(context,"${e.message}",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

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
            destinationPoiId = navigationManager.destinationInfo.id.toString(),
            destinationLatitude = navigationManager.destinationInfo.latitude,
            destinationLongitude = navigationManager.destinationInfo.longitude
        ).onEach { routeModels ->
            val route = routeModels.toRouteListState()
            _searchUiState.update { state ->
                state.copy(
                    routeList = route,
                )
            }
            navigationManager.apply {
                recentDistance = route.first().totalDistance
                routeIndex = 0
                recentLineInfoIndex = 0
            }

        }.catchFetching(
            onFailedHttpException = { e ->
                Log.d("tests", "error : ${e.message}, ${e.printStackTrace()}}")
            },
            onFailedElseException = { e ->
                Log.d("tests", "error : ${e.message} , ${e.printStackTrace()}}")
            }
        ).launchIn(viewModelScope)

    private fun setUserLocationOnDatabase(
        userLocation: UserLocation
    ) {
        viewModelScope.launch {
            logUsecase.setUserLocation(userLocation)
        }
    }

    private fun setAzimuthSensorOnDatabase(
        azimuthSensor: SensorInfo
    ) {
        viewModelScope.launch {
            logUsecase.setAzimuthSensor(azimuthSensor.toAzimuthSensor())
        }
    }

    private fun setIndex(
        index: SensorInfo
    ) {
        viewModelScope.launch {
            logUsecase.setDistanceSensor(index.toDistanceSensor())
        }
    }

    private fun emitEventFlow(state: SearchEventFlow) {
        viewModelScope.launch {
            _searchEventFlow.emit(state)
        }
    }
}

