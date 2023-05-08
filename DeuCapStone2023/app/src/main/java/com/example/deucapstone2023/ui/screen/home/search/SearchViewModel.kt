package com.example.deucapstone2023.ui.screen.home.search

import android.content.Context
import android.graphics.BitmapFactory
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.deucapstone2023.R
import com.example.deucapstone2023.domain.model.POIModel
import com.example.deucapstone2023.domain.usecase.POIUsecase
import com.example.deucapstone2023.domain.usecase.RouteUsecase
import com.example.deucapstone2023.ui.screen.home.search.state.POIState
import com.example.deucapstone2023.ui.screen.home.search.state.RouteState
import com.example.deucapstone2023.ui.screen.home.search.state.toPOIListState
import com.example.deucapstone2023.ui.screen.home.search.state.toRouteListState
import com.example.deucapstone2023.utils.catchFetching
import com.skt.tmap.TMapData
import com.skt.tmap.TMapPoint
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.overlay.TMapPolyLine
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


sealed class SearchUiState {
    object Loading : SearchUiState()
    data class POIList(val poiList: List<POIState>) : SearchUiState()
    data class RouteList(val routeList: List<RouteState>) : SearchUiState()

    /*data class SearchUiState(
        val poiList: List<POIState>,
        val latitude: Double,
        val longitude: Double,
        val routeList: List<RouteState>
    ) {
        companion object {
            fun getInitValues(lat: Double = .0, lon: Double = .0) = SearchUiState(
                poiList = emptyList(),
                latitude = lat,
                longitude = lon,
                routeList = emptyList()
            )
        }
    }*/
}

data class UserLocation(
    val latitude: Double,
    val longitude: Double
) {
    companion object {
        fun getInitValues(lat: Double = .0, lon: Double = .0) = UserLocation(
            latitude = lat,
            longitude = lon
        )
    }
}


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val poiUsecase: POIUsecase,
    private val routeUsecase: RouteUsecase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _searchUiState = MutableSharedFlow<SearchUiState>()
    val searchUiState get() = _searchUiState.asSharedFlow()

    private val _userLocation = MutableStateFlow(UserLocation.getInitValues())
    val userLocation get() = _userLocation.asStateFlow()

    fun setUserLocation(lat: Double, lon: Double) = _userLocation.update { state ->
        state.copy(latitude = lat, longitude = lon)
    }

    fun searchPlace(appKey: String, place: String) =
        poiUsecase.getPOISearch(
            poiModel = POIModel(
                name = place,
                centerLat = userLocation.value.latitude,
                centerLon = userLocation.value.longitude,
                radius = "0",
                searchtypCd = "R",
                bizName = "",
                address = "",
                id = 0L
            ),
            appKey = appKey
        ).onEach { poiList ->
            _searchUiState.emit(SearchUiState.POIList(poiList.toPOIListState()))

            /* _searchUiState.update { state ->
                state.copy(poiList = poiList.toPOIListState())
            }*/
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
            startLatitude = userLocation.value.latitude,
            startLongitude = userLocation.value.longitude,
            destinationLatitude = destinationLatitude,
            destinationLongitude = destinationLongitude,
            destinationPoiId = destinationPoiId
        ).onEach { routeModels ->
            emitUiState(SearchUiState.RouteList(routeModels.toRouteListState()))
        }.catchFetching(
            onFailedHttpException = {},
            onFailedElseException = {}
        ).launchIn(viewModelScope)

    private fun emitUiState(state: SearchUiState) {
        viewModelScope.launch {
            _searchUiState.emit(state)
        }
    }
}

