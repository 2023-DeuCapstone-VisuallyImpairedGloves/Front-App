package com.example.deucapstone2023.domain.usecase

import android.content.Context
import com.example.deucapstone2023.data.datasource.local.database.entity.UserLocation
import com.example.deucapstone2023.domain.model.LineModel
import com.example.deucapstone2023.domain.model.RouteModel
import com.example.deucapstone2023.ui.base.AzimuthType
import com.example.deucapstone2023.ui.screen.list.state.SensorInfo
import com.example.deucapstone2023.ui.screen.search.state.Location
import com.example.deucapstone2023.ui.screen.search.state.RouteState
import kotlinx.coroutines.flow.Flow

interface RouteUsecase {

    fun getRoutePedestrian(
        appKey: String,
        startLatitude: Double,
        startLongitude: Double,
        destinationPoiId: String,
        destinationLatitude: Double,
        destinationLongitude: Double,
        startName: String = "출발지",
        destinationName: String = "목적지"
    ) : Flow<List<RouteModel>>

    suspend fun navigateRouteOnMap(
        routeList: List<RouteModel>,
        source: LineModel,
        azimuth: AzimuthType,
        voiceOutput: (String) -> Unit,
        quitNavigation: () -> Unit,
        requestPedestrianRoute: ((String) -> Unit) -> Unit,
        context: Context
    )

    fun setInitNavigation(
        totalDistance: Int
    )

}