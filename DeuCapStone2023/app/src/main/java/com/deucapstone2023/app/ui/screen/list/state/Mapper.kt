package com.deucapstone2023.app.ui.screen.list.state

import com.deucapstone2023.domain.domain.model.SensorModel

fun SensorModel.toSensorInfo() =
    SensorInfo(
        id = this.id,
        status = this.status,
        desc = this.desc,
        date = this.date
    )

fun com.deucapstone2023.domain.domain.model.UserLocation.toUserLocationState() = UserLocation(
    id = id,
    latitude = latitude,
    longitude = longitude,
    nearPoiName = nearPoiName,
    source = source,
    dest = dest,
    date = date
)