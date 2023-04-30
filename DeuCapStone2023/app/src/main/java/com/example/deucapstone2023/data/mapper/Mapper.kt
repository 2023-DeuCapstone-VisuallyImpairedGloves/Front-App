package com.example.deucapstone2023.data.mapper

import com.example.deucapstone2023.data.dto.response.poi.POIResponse
import com.example.deucapstone2023.data.dto.response.poi.Poi
import com.example.deucapstone2023.domain.model.POIModel

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
}

fun POIResponse.toPOIModel() = this.searchPoiInfo.pois.poi.map { it?.toPOIModel() ?: POIModel.getInitValues() }

fun Poi.toPOIModel() = POIModel(
    name = name ?: "",
    address = newAddressList.newAddress.map { it.fullAddressRoad }.first() ?: "",
    radius = radius ?: "",
    bizName = bizName ?: upperBizName ?: middleBizName ?: lowerBizName ?: detailBizName ?: "",
    centerLat = noorLat?.toDouble() ?: .0,
    centerLon = noorLon?.toDouble() ?: .0,
    searchtypCd = ""
)