package com.deucapstone2023.data.data.datasource.local

import com.deucapstone2023.data.data.datasource.local.database.entity.AzimuthSensor
import com.deucapstone2023.data.data.datasource.local.database.entity.DistanceSensor
import com.deucapstone2023.data.data.datasource.local.database.entity.ObstacleSensor
import com.deucapstone2023.data.data.datasource.local.database.entity.UserLocation
import kotlinx.coroutines.flow.Flow

interface CacheLogDatasource {

    suspend fun setUserLocation(userLocation: UserLocation)

    suspend fun setAzimuthSensor(azimuthSensor: AzimuthSensor)

    suspend fun setDistanceSensor(distanceSensor: DistanceSensor)

    suspend fun setObstacleSensor(obstacleSensor: ObstacleSensor)

    fun getUserLocation(): Flow<List<UserLocation>>

    fun getAzimuthSensor(): Flow<List<AzimuthSensor>>

    fun getDistanceSensor(): Flow<List<DistanceSensor>>

    fun getObstacleSensor(): Flow<List<ObstacleSensor>>
}