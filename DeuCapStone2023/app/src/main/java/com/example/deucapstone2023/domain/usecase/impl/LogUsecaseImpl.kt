package com.example.deucapstone2023.domain.usecase.impl

import com.example.deucapstone2023.data.datasource.local.database.entity.AzimuthSensor
import com.example.deucapstone2023.data.datasource.local.database.entity.DistanceSensor
import com.example.deucapstone2023.data.datasource.local.database.entity.ObstacleSensor
import com.example.deucapstone2023.data.datasource.local.database.entity.UserLocation
import com.example.deucapstone2023.domain.repository.LogRepository
import com.example.deucapstone2023.domain.usecase.LogUsecase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogUsecaseImpl @Inject constructor(
    private val logRepository: LogRepository
) : LogUsecase {

    override suspend fun setUserLocation(userLocation: UserLocation) {
        logRepository.setUserLocation(userLocation)
    }

    override suspend fun setAzimuthSensor(azimuthSensor: AzimuthSensor) {
        logRepository.setAzimuthSensor(azimuthSensor)
    }

    override suspend fun setDistanceSensor(distanceSensor: DistanceSensor) {
        logRepository.setDistanceSensor(distanceSensor)
    }

    override suspend fun setObstacleSensor(obstacleSensor: ObstacleSensor) {
        logRepository.setObstacleSensor(obstacleSensor)
    }

    override fun getUserLocation(): Flow<List<UserLocation>> =
        logRepository.getUserLocation()

    override fun getAzimuthSensor(): Flow<List<AzimuthSensor>> =
        logRepository.getAzimuthSensor()

    override fun getDistanceSensor(): Flow<List<DistanceSensor>> =
        logRepository.getDistanceSensor()

    override fun getObstacleSensor(): Flow<List<ObstacleSensor>> =
        logRepository.getObstacleSensor()

}