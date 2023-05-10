package com.example.deucapstone2023.domain.model

data class RouteModel(
    val name: String,
    val description: String,
    val turnType: Int,
    val pointType: String,
    val facilityType: Int,
    val startLatitude: Double,
    val startLongitude: Double,
    val destinationLatitude: Double,
    val destinationLongitude: Double,
    val lineInfo: List<LineModel>,
    val totalDistance: Int,
    val totalTime: Int,
) {
    companion object {
        fun getInitValues() = RouteModel(
            name = "",
            description = "",
            turnType = 0,
            pointType = "",
            facilityType = 0,
            startLatitude = 0.0,
            startLongitude = 0.0,
            destinationLatitude = 0.0,
            destinationLongitude = 0.0,
            lineInfo = emptyList(),
            totalDistance = 0,
            totalTime = 0
        )
    }
}

data class LineModel(val latitude: Double, val longitude: Double)
