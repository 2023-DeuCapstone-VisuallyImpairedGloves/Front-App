package com.deucapstone2023.ui.screen.search.state

fun com.deucapstone2023.domain.domain.model.POIModel.toPOIState() = POIState(
    id = id,
    name = name,
    address = address,
    distance = radius,
    biz = bizName,
    latitude = centerLat,
    longitude = centerLon
)

fun List<com.deucapstone2023.domain.domain.model.POIModel>.toPOIListState() = this.map { it.toPOIState() }

fun List<com.deucapstone2023.domain.domain.model.RouteModel>.toRouteListState() = this.map { it.toRouteState() }

fun com.deucapstone2023.domain.domain.model.RouteModel.toRouteState() = RouteState(
    name = name,
    description = description,
    turnType = turnType,
    pointType = pointType,
    facilityType = facilityType,
    startLatitude = startLatitude,
    startLongitude = startLongitude,
    destinationLatitude = destinationLatitude,
    destinationLongitude = destinationLongitude,
    totalDistance = totalDistance,
    totalTime = totalTime,
    lineInfo = lineInfo.toLineListState()
)

fun List<RouteState>.toRouteListModel() = this.map { it.toRouteModel() }

fun RouteState.toRouteModel() = com.deucapstone2023.domain.domain.model.RouteModel(
    name = name,
    description = description,
    turnType = turnType,
    pointType = pointType,
    facilityType = facilityType,
    startLatitude = startLatitude,
    startLongitude = startLongitude,
    destinationLatitude = destinationLatitude,
    destinationLongitude = destinationLongitude,
    totalDistance = totalDistance,
    totalTime = totalTime,
    lineInfo = lineInfo.toLineListModel()
)

fun List<Location>.toLineListModel() = this.map { it.toLineModel() }

fun Location.toLineModel() = com.deucapstone2023.domain.domain.model.LineModel(
    latitude = this.latitude,
    longitude = this.longitude
)

fun List<com.deucapstone2023.domain.domain.model.LineModel>.toLineListState() = this.map { it.toLineState() }

fun com.deucapstone2023.domain.domain.model.LineModel.toLineState() = Location(latitude = this.latitude, longitude = this.longitude)