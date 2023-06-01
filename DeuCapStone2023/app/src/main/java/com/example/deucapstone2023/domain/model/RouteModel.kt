package com.example.deucapstone2023.domain.model

import com.example.deucapstone2023.ui.base.FacilityType
import com.example.deucapstone2023.ui.base.PointType
import com.example.deucapstone2023.ui.base.TurnType

data class RouteModel(
    val name: String,
    val description: String,
    val turnType: TurnType,
    val pointType: PointType,
    val facilityType: FacilityType,
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
            turnType = TurnType.NO_GUIDANCE1,
            pointType = PointType.INIT,
            facilityType = FacilityType.INIT,
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
