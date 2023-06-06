package com.deucapstone2023.data.data.datasource.remote.dto.request

data class POIRequest(
    val version: Int = 1,
    val page: Int = 1,
    val count: Int = 20,
    val searchKeyword: String,
    val areaLLCode: String? = null,
    val areaLMCode: String? = null,
    val resCoordType: String? = null,
    val searchType: String? = null,
    val multiPoint: String? = null,
    val searchTypCd: String? = null,
    val radius: String? = null,
    val reqCoordType: String? = null,
    val centerLon: Int? = null,
    val centerLat: Int? = null,
    val poiGroupYn: String? = null,
    val callback: String? = null,
    val appKey: String,
    val evPublicType: String? = null,
    val evOemType: String? = null,
    val removeFireplugYn: String? = null
)
