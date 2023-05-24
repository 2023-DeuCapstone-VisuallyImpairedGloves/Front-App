package com.example.deucapstone2023.domain.repository

import com.example.deucapstone2023.data.datasource.local.database.entity.AzimuthSensor
import com.example.deucapstone2023.data.datasource.local.database.entity.DistanceSensor
import com.example.deucapstone2023.data.datasource.local.database.entity.ObstacleSensor
import com.example.deucapstone2023.data.datasource.local.database.entity.UserLocation
import kotlinx.coroutines.flow.Flow

interface LogRepository {

    suspend fun setUserLocation(userLocation: UserLocation)

    suspend fun setAzimuthSensor(azimuthSensor: AzimuthSensor)

    suspend fun setDistanceSensor(distanceSensor: DistanceSensor)

    suspend fun setObstacleSensor(obstacleSensor: ObstacleSensor)

    fun getUserLocation(): Flow<List<UserLocation>>

    fun getAzimuthSensor(): Flow<List<AzimuthSensor>>

    fun getDistanceSensor(): Flow<List<DistanceSensor>>

    fun getObstacleSensor(): Flow<List<ObstacleSensor>>
}