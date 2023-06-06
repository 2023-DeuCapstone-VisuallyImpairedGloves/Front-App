package com.deucapstone2023.domain.domain.model

data class UserLocation(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val nearPoiName: String,
    val source: String,
    val dest: String,
    val date: String
)
