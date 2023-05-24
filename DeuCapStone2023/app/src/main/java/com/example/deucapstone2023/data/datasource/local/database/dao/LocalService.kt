package com.example.deucapstone2023.data.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.deucapstone2023.data.datasource.local.database.entity.AzimuthSensor
import com.example.deucapstone2023.data.datasource.local.database.entity.DistanceSensor
import com.example.deucapstone2023.data.datasource.local.database.entity.ObstacleSensor
import com.example.deucapstone2023.data.datasource.local.database.entity.UserLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalService {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun setUserLocation(userLocation: UserLocation)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun setAzimuthSensor(azimuthSensor: AzimuthSensor)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun setDistanceSensor(distanceSensor: DistanceSensor)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun setObstacleSensor(obstacleSensor: ObstacleSensor)

    @Query("select * from UserLocation")
    fun getUserLocation(): Flow<List<UserLocation>>

    @Query("select * from AzimuthSensor")
    fun getAzimuthSensor(): Flow<List<AzimuthSensor>>

    @Query("select * from DistanceSensor")
    fun getDistanceSensor(): Flow<List<DistanceSensor>>

    @Query("select * from ObstacleSensor")
    fun getObstacleSensor(): Flow<List<ObstacleSensor>>
}