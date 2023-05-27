package com.example.deucapstone2023.data.repositoryimpl

import com.example.deucapstone2023.data.datasource.local.LocalLogDatasource
import com.example.deucapstone2023.data.datasource.local.database.entity.AzimuthSensor
import com.example.deucapstone2023.data.datasource.local.database.entity.DistanceSensor
import com.example.deucapstone2023.data.datasource.local.database.entity.ObstacleSensor
import com.example.deucapstone2023.data.datasource.local.database.entity.UserLocation
import com.example.deucapstone2023.domain.repository.LogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogRepositoryImpl @Inject constructor(
    private val localInfoDatasource: LocalLogDatasource
) : LogRepository {

    override suspend fun setUserLocation(userLocation: UserLocation) {
        localInfoDatasource.setUserLocation(userLocation)
    }

    override suspend fun setAzimuthSensor(azimuthSensor: AzimuthSensor) {
        localInfoDatasource.setAzimuthSensor(azimuthSensor)
    }

    override suspend fun setDistanceSensor(distanceSensor: DistanceSensor) {
        localInfoDatasource.setDistanceSensor(distanceSensor)
    }

    override suspend fun setObstacleSensor(obstacleSensor: ObstacleSensor) {
        localInfoDatasource.setObstacleSensor(obstacleSensor)
    }

    override fun getUserLocation(): Flow<List<UserLocation>> =
        localInfoDatasource.getUserLocation()

    override fun getAzimuthSensor(): Flow<List<AzimuthSensor>> =
        localInfoDatasource.getAzimuthSensor()

    override fun getDistanceSensor(): Flow<List<DistanceSensor>> =
        localInfoDatasource.getDistanceSensor()

    override fun getObstacleSensor(): Flow<List<ObstacleSensor>> =
        localInfoDatasource.getObstacleSensor()

}