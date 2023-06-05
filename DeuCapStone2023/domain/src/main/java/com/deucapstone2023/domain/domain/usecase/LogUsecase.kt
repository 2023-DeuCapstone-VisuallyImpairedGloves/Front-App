package com.deucapstone2023.domain.domain.usecase

import com.deucapstone2023.domain.domain.model.SensorModel
import com.deucapstone2023.domain.domain.model.UserLocation
import kotlinx.coroutines.flow.Flow

interface LogUsecase {

    suspend fun setUserLocation(userLocation: UserLocation)

    suspend fun setAzimuthSensor(azimuthSensor: SensorModel)

    suspend fun setDistanceSensor(distanceSensor: SensorModel)

    suspend fun setObstacleSensor(obstacleSensor: SensorModel)

    fun getUserLocation(): Flow<List<UserLocation>>

    fun getAzimuthSensor(): Flow<List<SensorModel>>

    fun getDistanceSensor(): Flow<List<SensorModel>>

    fun getObstacleSensor(): Flow<List<SensorModel>>

}