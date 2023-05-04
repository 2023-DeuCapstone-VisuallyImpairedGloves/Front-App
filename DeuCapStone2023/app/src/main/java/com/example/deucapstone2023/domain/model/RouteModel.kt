package com.example.deucapstone2023.domain.model

data class RouteModel(
    val name: String,
    val description: String,
    val turnType: Int,
    val pointType: Int,
    val facilityType: Int,
    val startLatitude: Double,
    val startLongitude: Double,
    val destinationLatitude: Double,
    val destinationLongitude: Double,
    val totalDistance: Int,
    val totalTime: Int,
)
