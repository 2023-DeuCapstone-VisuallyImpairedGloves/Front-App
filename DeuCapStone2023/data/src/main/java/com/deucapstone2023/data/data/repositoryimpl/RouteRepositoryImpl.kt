package com.deucapstone2023.data.data.repositoryimpl

import com.deucapstone2023.data.data.datasource.remote.FetchRouteDatasource
import javax.inject.Inject
import com.deucapstone2023.domain.domain.repository.RouteRepository

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
    ): List<com.deucapstone2023.domain.domain.model.RouteModel> =
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