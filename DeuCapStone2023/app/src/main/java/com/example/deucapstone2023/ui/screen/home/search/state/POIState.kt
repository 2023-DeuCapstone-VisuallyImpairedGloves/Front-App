package com.example.deucapstone2023.ui.screen.home.search.state

data class POIState(
    val name: String,
    val address: String,
    val distance: String,
    val biz: String,
    val latitude: Double,
    val longitude: Double
) {
    companion object {
        fun getInitValues() = POIState(
            name = "",
            address = "",
            distance = "",
            biz = "",
            latitude = .0,
            longitude = .0
        )
    }
}
