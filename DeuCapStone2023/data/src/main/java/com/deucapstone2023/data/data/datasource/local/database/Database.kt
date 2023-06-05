package com.deucapstone2023.data.data.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.deucapstone2023.data.data.datasource.local.database.dao.CacheLocal
import com.deucapstone2023.data.data.datasource.local.database.entity.AzimuthSensor
import com.deucapstone2023.data.data.datasource.local.database.entity.DistanceSensor
import com.deucapstone2023.data.data.datasource.local.database.entity.ObstacleSensor
import com.deucapstone2023.data.data.datasource.local.database.entity.UserLocation

@Database(entities = [UserLocation::class, ObstacleSensor::class, DistanceSensor::class, AzimuthSensor::class],
    version = 1,  exportSchema = true)
abstract class Database : RoomDatabase() {
    abstract fun localDao(): CacheLocal
}