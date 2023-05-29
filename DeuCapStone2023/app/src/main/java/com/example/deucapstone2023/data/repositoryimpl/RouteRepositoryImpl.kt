package com.example.deucapstone2023.data.repositoryimpl

import com.example.deucapstone2023.data.datasource.remote.FetchRouteDatasource
import com.example.deucapstone2023.domain.model.RouteModel
import com.example.deucapstone2023.domain.repository.RouteRepository
import javax.inject.Inject

class RouteRepositoryImpl @Inject constructor(
    private val routeDatasource: FetchRouteDatasource
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
        routeDatasource.getRoutePedestrian(
            appKey = appKey,
            startLatitude = startLatitude,
            startLongitude = startLongitude,
            destinationPoiId = destinationPoiId,
            destinationLatitude = destinationLatitude,
            destinationLongitude = destinationLongitude,
            startName = startName,
            destinationName = destinationName
        )

}