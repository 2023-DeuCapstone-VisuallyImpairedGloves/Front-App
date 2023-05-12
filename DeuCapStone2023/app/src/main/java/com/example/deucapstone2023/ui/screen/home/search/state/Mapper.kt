package com.example.deucapstone2023.ui.screen.home.search.state

import com.example.deucapstone2023.domain.model.POIModel
import com.example.deucapstone2023.ui.screen.home.search.SearchUiState

fun SearchUiState.toPOIModel(place: String) = POIModel(
    name = place,
    centerLat = latitude,
    centerLon = longitude,
    radius = "",
    searchtypCd = searchType,
    bizName = "",
    address = ""
)

fun POIModel.toPOIState() = POIState(
    name = name,
    address = address,
    distance = radius,
    biz = bizName,
    latitude = centerLat,
    longitude = centerLon
)

fun List<POIModel>.toPOIListState() = this.map { it.toPOIState() }
