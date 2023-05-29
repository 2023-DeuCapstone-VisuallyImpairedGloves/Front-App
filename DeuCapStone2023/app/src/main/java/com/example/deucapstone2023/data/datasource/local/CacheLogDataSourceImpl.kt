package com.example.deucapstone2023.data.datasource.local

import com.example.deucapstone2023.data.datasource.local.database.dao.CacheLocal
import com.example.deucapstone2023.data.datasource.local.database.entity.AzimuthSensor
import com.example.deucapstone2023.data.datasource.local.database.entity.DistanceSensor
import com.example.deucapstone2023.data.datasource.local.database.entity.ObstacleSensor
import com.example.deucapstone2023.data.datasource.local.database.entity.UserLocation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CacheLogDataSourceImpl @Inject constructor(
    private val localService: CacheLocal
): CacheLogDatasource {

    override suspend fun setUserLocation(userLocation: UserLocation) {
        localService.setUserLocation(userLocation)
    }

    override suspend fun setAzimuthSensor(azimuthSensor: AzimuthSensor) {
        localService.setAzimuthSensor(azimuthSensor)
    }

    override suspend fun setDistanceSensor(distanceSensor: DistanceSensor) {
        localService.setDistanceSensor(distanceSensor)
    }

    override suspend fun setObstacleSensor(obstacleSensor: ObstacleSensor) {
        localService.setObstacleSensor(obstacleSensor)
    }

    override fun getUserLocation(): Flow<List<UserLocation>> =
        localService.getUserLocation()

    override fun getAzimuthSensor(): Flow<List<AzimuthSensor>> =
        localService.getAzimuthSensor()

    override fun getDistanceSensor(): Flow<List<DistanceSensor>> =
        localService.getDistanceSensor()

    override fun getObstacleSensor(): Flow<List<ObstacleSensor>> =
        localService.getObstacleSensor()
}