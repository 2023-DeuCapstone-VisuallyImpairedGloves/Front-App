package com.example.deucapstone2023.ui.screen.search.state

import com.example.deucapstone2023.domain.model.LineModel
import com.example.deucapstone2023.domain.model.POIModel
import com.example.deucapstone2023.domain.model.RouteModel
import com.example.deucapstone2023.ui.base.FacilityType
import com.example.deucapstone2023.ui.base.PointType
import com.example.deucapstone2023.ui.base.TurnType

fun POIModel.toPOIState() = POIState(
    id = id,
    name = name,
    address = address,
    distance = radius,
    biz = bizName,
    latitude = centerLat,
    longitude = centerLon
)

fun List<POIModel>.toPOIListState() = this.map { it.toPOIState() }

fun List<RouteModel>.toRouteListState() = this.map { it.toRouteState() }

fun RouteModel.toRouteState() = RouteState(
    name = name,
    description = description,
    turnType = TurnType.getType(turnType),
    pointType = PointType.getType(pointType),
    facilityType = FacilityType.getType(facilityType),
    startLatitude = startLatitude,
    startLongitude = startLongitude,
    destinationLatitude = destinationLatitude,
    destinationLongitude = destinationLongitude,
    totalDistance = totalDistance,
    totalTime = totalTime,
    lineInfo = lineInfo.toLineListState()
)

fun List<LineModel>.toLineListState() = this.map { it.toLineState() }
fun LineModel.toLineState() = Location(latitude = this.latitude, longitude = this.longitude)