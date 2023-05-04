package com.example.deucapstone2023.ui.screen.home.search.state

import com.example.deucapstone2023.domain.model.POIModel
import com.example.deucapstone2023.domain.model.RouteModel
import com.example.deucapstone2023.ui.screen.home.search.SearchUiState

fun SearchUiState.toPOIModel(place: String) = POIModel(
    name = place,
    centerLat = latitude,
    centerLon = longitude,
    radius = "0",
    searchtypCd = "R",
    bizName = "",
    address = "",
    id = 0L
)

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
    turnType = turnType,
    pointType = pointType,
    facilityType = facilityType,
    startLatitude = startLatitude,
    startLongitude = startLongitude,
    destinationLatitude = destinationLatitude,
    destinationLongitude = destinationLongitude,
    totalDistance = totalDistance,
    totalTime = totalTime
)