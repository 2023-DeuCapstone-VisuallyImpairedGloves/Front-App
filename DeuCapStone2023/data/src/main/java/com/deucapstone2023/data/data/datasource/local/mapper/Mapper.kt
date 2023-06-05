package com.deucapstone2023.data.data.datasource.local.mapper

import com.deucapstone2023.data.data.datasource.local.database.entity.AzimuthSensor
import com.deucapstone2023.data.data.datasource.local.database.entity.DistanceSensor
import com.deucapstone2023.data.data.datasource.local.database.entity.ObstacleSensor

fun AzimuthSensor.toSensorModel() = com.deucapstone2023.domain.domain.model.SensorModel(
    id = this.id,
    status = this.status,
    desc = this.desc,
    date = this.date
)

fun DistanceSensor.toSensorModel() = com.deucapstone2023.domain.domain.model.SensorModel(
    id = this.id,
    status = this.status,
    desc = this.desc,
    date = this.date
)

fun ObstacleSensor.toSensorModel() = com.deucapstone2023.domain.domain.model.SensorModel(
    id = this.id,
    status = this.status,
    desc = this.desc,
    date = this.date
)

fun com.deucapstone2023.data.data.datasource.local.database.entity.UserLocation.toUserLocationModel() = com.deucapstone2023.domain.domain.model.UserLocation(
    id = id,
    latitude = latitude,
    longitude = longitude,
    nearPoiName = nearPoiName,
    source = source,
    dest = dest,
    date = date
)

fun com.deucapstone2023.domain.domain.model.SensorModel.toAzimuthSensor() =
    AzimuthSensor(
        id = this.id,
        status = this.status,
        desc = this.desc,
        date = this.date
    )

fun com.deucapstone2023.domain.domain.model.SensorModel.toDistanceSensor() =
    DistanceSensor(
        id = this.id,
        status = this.status,
        desc = this.desc,
        date = this.date
    )

fun com.deucapstone2023.domain.domain.model.SensorModel.toObstacleSensor() =
    ObstacleSensor(
        id = this.id,
        status = this.status,
        desc = this.desc,
        date = this.date
    )

fun com.deucapstone2023.domain.domain.model.UserLocation.toUserLocationEntity() = com.deucapstone2023.data.data.datasource.local.database.entity.UserLocation(
    id = id,
    latitude = latitude,
    longitude = longitude,
    nearPoiName = nearPoiName,
    source = source,
    dest = dest,
    date = date
)