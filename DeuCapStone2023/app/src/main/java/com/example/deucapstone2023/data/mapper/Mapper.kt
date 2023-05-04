package com.example.deucapstone2023.data.mapper

import com.example.deucapstone2023.data.dto.response.poi.POIResponse
import com.example.deucapstone2023.data.dto.response.poi.Poi
import com.example.deucapstone2023.data.dto.response.poi.RouteResponse
import com.example.deucapstone2023.domain.model.POIModel
import com.example.deucapstone2023.domain.model.RouteModel

fun POIModel.toRequestDTO(appKey: String) = mutableMapOf<String, String>().apply {
    put("version", "1")
    put("page", "1")
    put("count", "20")
    put("appKey", appKey)
    put("searchKeyword", name)
    if(radius.isNotBlank())
        put("radius", radius)
    if(centerLon != .0)
        put("centerLon", centerLon.toString())
    if(centerLat != .0)
        put("centerLat", centerLat.toString())
    if(searchtypCd.isNotBlank())
        put("searchtypCd", searchtypCd)
}

fun POIResponse.toPOIModel() = this.searchPoiInfo.pois.poi.map { it?.toPOIModel() ?: POIModel.getInitValues() }

fun Poi.toPOIModel() = POIModel(
    id = id?.toLong() ?: 0L,
    name = name ?: "",
    address = newAddressList.newAddress.map { it.fullAddressRoad }.first() ?: "",
    radius = radius ?: "",
    bizName = bizName ?: upperBizName ?: middleBizName ?: lowerBizName ?: detailBizName ?: "",
    centerLat = noorLat?.toDouble() ?: .0,
    centerLon = noorLon?.toDouble() ?: .0,
    searchtypCd = ""
)

fun RouteResponse.toRouteModel() = this.features?.chunked(2) { features ->
    val location = features[1]?.geometry?.coordinates?.split(",")?.map { it.toDouble() }
    RouteModel(
        startLatitude = location?.get(0) ?: .0,
        startLongitude = location?.get(1) ?: .0,
        destinationLatitude = location?.get(3) ?: .0,
        destinationLongitude = location?.get(4) ?: .0,
        name = features[1]?.properties?.name ?: "",
        description = features[0]?.properties?.description ?: "",
        turnType = features[0]?.properties?.turnType ?: 0,
        pointType = features[0]?.properties?.pointType?.toInt() ?: 0,
        facilityType = features[1]?.properties?.facilityType?.toInt() ?: 0,
        totalDistance = features[1]?.properties?.distance ?: 0,
        totalTime = features[1]?.properties?.time ?: 0
    )
} ?: emptyList()
