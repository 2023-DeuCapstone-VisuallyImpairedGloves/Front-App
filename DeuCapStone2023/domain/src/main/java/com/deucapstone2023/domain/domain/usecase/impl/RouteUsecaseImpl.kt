package com.deucapstone2023.domain.domain.usecase.impl

import com.deucapstone2023.domain.domain.model.AzimuthType
import com.deucapstone2023.domain.domain.model.LineModel
import com.deucapstone2023.domain.domain.model.RouteModel
import com.deucapstone2023.domain.domain.repository.RouteRepository
import com.deucapstone2023.domain.domain.service.PedestrianNavigationService
import com.deucapstone2023.domain.domain.usecase.RouteUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RouteUsecaseImpl @Inject constructor(
    private val routeRepository: RouteRepository,
    private val navigationService: PedestrianNavigationService
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