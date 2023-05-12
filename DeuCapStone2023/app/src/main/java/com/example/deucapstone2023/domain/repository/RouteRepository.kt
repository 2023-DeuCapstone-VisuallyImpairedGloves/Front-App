package com.example.deucapstone2023.domain.repository

import com.example.deucapstone2023.domain.model.RouteModel

interface RouteRepository {
    suspend fun getRoutePedestrian(
        appKey: String,
        startLatitude: Double,
        startLongitude: Double,
        destinationPoiId: String,
        destinationLatitude: Double,
        destinationLongitude: Double,
        startName: String = "출발지",
        destinationName: String = "목적지"
    ) : List<RouteModel>
}