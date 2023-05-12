package com.example.deucapstone2023.ui.screen.search.state

import androidx.compose.runtime.Stable

@Stable
data class Location(
    val latitude: Double,
    val longitude: Double
) {
    companion object {
        fun getInitValues(lat: Double = .0, lon: Double = .0) = Location(
            latitude = lat,
            longitude = lon
        )
    }
}