package com.example.deucapstone2023.domain.usecase

import com.example.deucapstone2023.domain.model.RouteModel
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

}