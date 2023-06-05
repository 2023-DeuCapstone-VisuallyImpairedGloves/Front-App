package com.deucapstone2023.domain.domain.usecase

import com.deucapstone2023.domain.domain.model.AzimuthType
import com.deucapstone2023.domain.domain.model.LineModel
import com.deucapstone2023.domain.domain.model.RouteModel
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
        requestPedestrianRoute: ((String) -> Unit) -> Unit
    )

    fun setInitNavigation(
        totalDistance: Int
    )

}