package com.example.deucapstone2023.domain.model

data class POIModel(
    val id: Long,
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
            id = 0L,
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