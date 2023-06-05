package com.deucapstone2023.app.ui.screen.search.state

import androidx.compose.runtime.Stable
import com.deucapstone2023.domain.domain.model.FacilityType
import com.deucapstone2023.domain.domain.model.PointType
import com.deucapstone2023.domain.domain.model.TurnType

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
