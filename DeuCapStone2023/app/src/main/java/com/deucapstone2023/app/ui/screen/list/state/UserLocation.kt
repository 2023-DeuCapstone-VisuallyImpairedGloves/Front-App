package com.deucapstone2023.app.ui.screen.list.state

data class UserLocation(
    val latitude: Double,
    val longitude: Double,
    val nearPoiName: String,
    val source: String,
    val dest: String,
    val date: String,
    val id: Int
)