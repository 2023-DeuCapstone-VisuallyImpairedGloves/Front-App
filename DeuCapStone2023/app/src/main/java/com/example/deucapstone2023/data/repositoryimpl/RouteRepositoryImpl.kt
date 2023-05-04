package com.example.deucapstone2023.data.repositoryimpl

import com.example.deucapstone2023.data.ApiService
import com.example.deucapstone2023.data.dto.request.RouteRequest
import com.example.deucapstone2023.data.mapper.toRouteModel
import com.example.deucapstone2023.domain.model.RouteModel
import com.example.deucapstone2023.domain.repository.RouteRepository
import javax.inject.Inject

class RouteRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : RouteRepository {
    override suspend fun getRoutePedestrian(
        appKey: String,
        startLatitude: Double,
        startLongitude: Double,
        destinationPoiId: String,
        destinationLatitude: Double,
        destinationLongitude: Double,
        startName: String,
        destinationName: String
    ): List<RouteModel> =
        apiService.getRoutePedestrian(
            appKey = appKey,
            RouteRequest(
                startLatitude = startLatitude,
                startLongitude = startLongitude,
                destinationPoiId = destinationPoiId,
                destinationLatitude = destinationLatitude,
                destinationLongitude = destinationLongitude,
                startName = startName,
                destinationName = destinationName
            )
        ).toRouteModel()
}