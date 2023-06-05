package com.deucapstone2023.data.data.repositoryimpl

import com.deucapstone2023.data.data.datasource.local.CacheLogDatasource
import com.deucapstone2023.data.data.datasource.local.mapper.toAzimuthSensor
import com.deucapstone2023.data.data.datasource.local.mapper.toDistanceSensor
import com.deucapstone2023.data.data.datasource.local.mapper.toObstacleSensor
import com.deucapstone2023.data.data.datasource.local.mapper.toSensorModel
import com.deucapstone2023.data.data.datasource.local.mapper.toUserLocationEntity
import com.deucapstone2023.data.data.datasource.local.mapper.toUserLocationModel
import com.deucapstone2023.domain.domain.model.SensorModel
import com.deucapstone2023.domain.domain.model.UserLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.deucapstone2023.domain.domain.repository.LogRepository

class LogRepositoryImpl @Inject constructor(
    private val localInfoDatasource: CacheLogDatasource
) : LogRepository {

    override suspend fun setUserLocation(userLocation: UserLocation) {
        localInfoDatasource.setUserLocation(userLocation.toUserLocationEntity())
    }

    override suspend fun setAzimuthSensor(azimuthSensor: SensorModel) {
        localInfoDatasource.setAzimuthSensor(azimuthSensor.toAzimuthSensor())
    }

    override suspend fun setDistanceSensor(distanceSensor: SensorModel) {
        localInfoDatasource.setDistanceSensor(distanceSensor.toDistanceSensor())
    }

    override suspend fun setObstacleSensor(obstacleSensor: SensorModel) {
        localInfoDatasource.setObstacleSensor(obstacleSensor.toObstacleSensor())
    }

    override fun getUserLocation(): Flow<List<UserLocation>> =
        localInfoDatasource.getUserLocation().map { userLocationList ->
            userLocationList.map { userLocation ->
                userLocation.toUserLocationModel()
            }
        }

    override fun getAzimuthSensor(): Flow<List<SensorModel>> =
        localInfoDatasource.getAzimuthSensor().map { sensorList ->
            sensorList.map { sensor ->
                sensor.toSensorModel()
            }
        }

    override fun getDistanceSensor(): Flow<List<SensorModel>> =
        localInfoDatasource.getDistanceSensor().map { sensorList ->
            sensorList.map { sensor ->
                sensor.toSensorModel()
            }
        }

    override fun getObstacleSensor(): Flow<List<SensorModel>> =
        localInfoDatasource.getObstacleSensor().map { sensorList ->
            sensorList.map { sensor ->
                sensor.toSensorModel()
            }
        }

}