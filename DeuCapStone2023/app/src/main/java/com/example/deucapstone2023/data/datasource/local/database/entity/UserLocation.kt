package com.example.deucapstone2023.data.datasource.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserLocation(
    val latitude: Double,
    val longitude: Double,
    val nearPoiName: String,
    val source: String,
    val dest: String,
    val date: String,
    @PrimaryKey val id: Int
)
