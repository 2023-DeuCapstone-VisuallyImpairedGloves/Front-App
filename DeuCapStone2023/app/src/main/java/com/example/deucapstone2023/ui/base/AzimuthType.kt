package com.example.deucapstone2023.ui.base

import kotlin.math.roundToInt

enum class AzimuthType {
    NORTH,
    SOUTH,
    WEST,
    EAST;
}

fun getAzimuthFromValue(azimuth: Double) =
    when(azimuth.roundToInt()) {
        in 46..135 -> AzimuthType.EAST
        in 136..225 -> AzimuthType.SOUTH
        in 226..315 -> AzimuthType.WEST
        else -> AzimuthType.NORTH
    }