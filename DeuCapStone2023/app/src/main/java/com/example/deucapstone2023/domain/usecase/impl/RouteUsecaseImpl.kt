package com.example.deucapstone2023.domain.usecase.impl

import com.example.deucapstone2023.domain.model.LineModel
import com.example.deucapstone2023.domain.model.RouteModel
import com.example.deucapstone2023.domain.repository.RouteRepository
import com.example.deucapstone2023.domain.service.NavigationService
import com.example.deucapstone2023.domain.usecase.RouteUsecase
import com.example.deucapstone2023.ui.base.AzimuthType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RouteUsecaseImpl @Inject constructor(
    private val routeRepository: RouteRepository,
    private val navigationService: NavigationService
) : RouteUsecase {
    override fun getRoutePedestrian(
        appKey: String,
        startLatitude: Double,
        startLongitude: Double,
        destinationPoiId: String,
        destinationLatitude: Double,
        destinationLongitude: Double,
        startName: String,
        destinationName: String
    ): Flow<List<RouteModel>> = flow {
        emit(
            routeRepository.getRoutePedestrian(
                appKey = appKey,
                startLatitude = startLatitude,
                startLongitude = startLongitude,
                destinationPoiId = destinationPoiId,
                destinationLatitude = destinationLatitude,
                destinationLongitude = destinationLongitude,
                startName = startName,
                destinationName = destinationName
            )
        )
    }

    override suspend fun navigateRouteOnMap(
        routeList: List<RouteModel>,
        source: LineModel,
        azimuth: AzimuthType,
        voiceOutput: (String) -> Unit,
        quitNavigation: () -> Unit,
        requestPedestrianRoute: ((String) -> Unit) -> Unit
    ) {
        navigationService.navigateRouteOnMap(
            routeList,
            source,
            azimuth,
            voiceOutput,
            quitNavigation,
            requestPedestrianRoute
        )
    }

    override fun setInitNavigation(totalDistance: Int) {
        navigationService.setInitValues(totalDistance)
    }
}