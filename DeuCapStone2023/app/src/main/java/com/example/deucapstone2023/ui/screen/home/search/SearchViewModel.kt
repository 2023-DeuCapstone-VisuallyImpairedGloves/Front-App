package com.example.deucapstone2023.ui.screen.home.search

import android.content.Context
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deucapstone2023.R
import com.example.deucapstone2023.domain.usecase.POIUsecase
import com.example.deucapstone2023.domain.usecase.RouteUsecase
import com.example.deucapstone2023.ui.screen.home.search.state.POIState
import com.example.deucapstone2023.ui.screen.home.search.state.RouteState
import com.example.deucapstone2023.ui.screen.home.search.state.toPOIListState
import com.example.deucapstone2023.ui.screen.home.search.state.toPOIModel
import com.example.deucapstone2023.ui.screen.home.search.state.toRouteListState
import com.example.deucapstone2023.utils.catchFetching
import com.skt.tmap.TMapPoint
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.overlay.TMapMarkerItem2
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject



data class SearchUiState(
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
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val poiUsecase: POIUsecase,
    private val routeUsecase: RouteUsecase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _searchUiState = MutableStateFlow(SearchUiState.getInitValues())
    val searchUiState get() = _searchUiState.asStateFlow()

    fun setUserLocation(lat: Double, lon: Double) = _searchUiState.update { state ->
        state.copy(latitude = lat, longitude = lon)
    }

    fun searchPlace(appKey: String, place: String) =
        poiUsecase.getPOISearch(
            poiModel = searchUiState.value.toPOIModel(place),
            appKey = appKey
        ).onEach { poiList ->
            _searchUiState.update { state ->
                state.copy(poiList = poiList.toPOIListState())
            }
        }.catchFetching(
            onFailedHttpException = {},
            onFailedElseException = {}
        ).launchIn(viewModelScope)

    fun getPoiMarkers(): List<TMapMarkerItem> {
        val markers = mutableListOf<TMapMarkerItem>()
        searchUiState.value.poiList.onEach { poi ->
            markers.add(makeMarker(poi))
        }
        return markers
    }

    fun getPoiMarker(index: Int): TMapMarkerItem =
        searchUiState.value.poiList[index].run {
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
            startLatitude = searchUiState.value.latitude,
            startLongitude = searchUiState.value.longitude,
            destinationLatitude = destinationLatitude,
            destinationLongitude = destinationLongitude,
            destinationPoiId = destinationPoiId
        ).onEach { routeModels ->  
            _searchUiState.update { state ->
                state.copy(routeList = routeModels.toRouteListState())
            }
        }.catchFetching(
            onFailedHttpException = {},
            onFailedElseException = {}
        ).launchIn(viewModelScope)

}

