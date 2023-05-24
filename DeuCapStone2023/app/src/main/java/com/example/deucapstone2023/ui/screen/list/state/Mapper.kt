package com.example.deucapstone2023.ui.screen.list.state

import com.example.deucapstone2023.data.datasource.local.database.entity.AzimuthSensor
import com.example.deucapstone2023.data.datasource.local.database.entity.DistanceSensor
import com.example.deucapstone2023.data.datasource.local.database.entity.ObstacleSensor

fun AzimuthSensor.toSensorInfo() = SensorInfo(
    id = this.id,
    status = this.status,
    desc = this.desc,
    date = this.date
)

fun DistanceSensor.toSensorInfo() = SensorInfo(
    id = this.id,
    status = this.status,
    desc = this.desc,
    date = this.date
)

fun ObstacleSensor.toSensorInfo() = SensorInfo(
    id = this.id,
    status = this.status,
    desc = this.desc,
    date = this.date
)

fun SensorInfo.toAzimuthSensor() = AzimuthSensor(
    id = this.id,
    status = this.status,
    desc = this.desc,
    date = this.date
)

fun SensorInfo.toDistanceSensor() = DistanceSensor(
    id = this.id,
    status = this.status,
    desc = this.desc,
    date = this.date
)

fun SensorInfo.toObstacleSensor() = ObstacleSensor(
    id = this.id,
    status = this.status,
    desc = this.desc,
    date = this.date
)