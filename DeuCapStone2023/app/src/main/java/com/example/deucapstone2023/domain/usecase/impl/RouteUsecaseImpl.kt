package com.example.deucapstone2023.domain.usecase.impl

import com.example.deucapstone2023.domain.model.RouteModel
import com.example.deucapstone2023.domain.repository.RouteRepository
import com.example.deucapstone2023.domain.usecase.RouteUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RouteUsecaseImpl @Inject constructor(
    private val routeRepository: RouteRepository
) : RouteUsecase{
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
        emit(routeRepository.getRoutePedestrian(
            appKey = appKey,
            startLatitude = startLatitude,
            startLongitude = startLongitude,
            destinationPoiId = destinationPoiId,
            destinationLatitude = destinationLatitude,
            destinationLongitude = destinationLongitude,
            startName = startName,
            destinationName = destinationName
        ))
    }
}