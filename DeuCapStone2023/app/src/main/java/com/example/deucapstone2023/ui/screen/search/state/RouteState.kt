package com.example.deucapstone2023.ui.screen.search.state

import androidx.compose.runtime.Stable
import com.example.deucapstone2023.ui.base.FacilityType
import com.example.deucapstone2023.ui.base.PointType
import com.example.deucapstone2023.ui.base.TurnType

@Stable
data class RouteState(
    val name: String,
    val description: String,
    val turnType: TurnType,
    val pointType: PointType,
    val facilityType: FacilityType,
    val startLatitude: Double,
    val startLongitude: Double,
    val destinationLatitude: Double,
    val destinationLongitude: Double,
    val totalDistance: Int,
    val totalTime: Int,
    val lineInfo: List<Location>
)
