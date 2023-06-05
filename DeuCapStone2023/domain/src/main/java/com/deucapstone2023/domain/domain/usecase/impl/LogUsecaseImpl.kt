package com.deucapstone2023.domain.domain.usecase.impl

import com.deucapstone2023.domain.domain.model.SensorModel
import com.deucapstone2023.domain.domain.model.UserLocation
import com.deucapstone2023.domain.domain.repository.LogRepository
import com.deucapstone2023.domain.domain.usecase.LogUsecase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogUsecaseImpl @Inject constructor(
    private val logRepository: LogRepository
) : LogUsecase {

    override suspend fun setUserLocation(userLocation: UserLocation) {
        logRepository.setUserLocation(userLocation)
    }

    override suspend fun setAzimuthSensor(azimuthSensor: SensorModel) {
        logRepository.setAzimuthSensor(azimuthSensor)
    }

    override suspend fun setDistanceSensor(distanceSensor: SensorModel) {
        logRepository.setDistanceSensor(distanceSensor)
    }

    override suspend fun setObstacleSensor(obstacleSensor: SensorModel) {
        logRepository.setObstacleSensor(obstacleSensor)
    }

    override fun getUserLocation(): Flow<List<UserLocation>> =
        logRepository.getUserLocation()

    override fun getAzimuthSensor(): Flow<List<SensorModel>> =
        logRepository.getAzimuthSensor()

    override fun getDistanceSensor(): Flow<List<SensorModel>> =
        logRepository.getDistanceSensor()

    override fun getObstacleSensor(): Flow<List<SensorModel>> =
        logRepository.getObstacleSensor()

}