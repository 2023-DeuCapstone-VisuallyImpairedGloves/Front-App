package com.deucapstone2023.domain.domain.utils

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

fun getDistanceFromSourceToDest(sourceLat: Double, sourceLon: Double, destLat:Double, destLon: Double): Int {
    val R = 6371.0
    val dLat = Math.toRadians(destLat - sourceLat)
    val dLon = Math.toRadians(destLon - sourceLon)
    val a = sin(dLat / 2.0) * sin(dLat / 2.0) + cos(Math.toRadians(sourceLat)) * cos(
        Math.toRadians(destLat)
    ) * sin(dLon / 2.0) * sin(dLon / 2.0)
    val c = 2.0 * atan2(sqrt(a), sqrt(1.0 - a))

    return (R * c * 1000.0).toInt()
}