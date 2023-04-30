package com.example.deucapstone2023.domain.model

data class POIModel(
    val name: String,
    val searchtypCd: String,
    val radius: String,
    val centerLon: Double,
    val centerLat: Double,
    val address: String,
    val bizName: String
) {
    companion object {
        fun getInitValues() = POIModel(
            name = "",
            address = "",
            radius = "",
            centerLat = .0,
            centerLon = .0,
            bizName = "",
            searchtypCd = ""
        )
    }
}

/*
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
 */