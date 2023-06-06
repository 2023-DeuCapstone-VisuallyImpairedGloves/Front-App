package com.deucapstone2023.data.data.datasource.remote.dto.response.poi
import com.google.gson.annotations.SerializedName

data class POIResponse(
    @SerializedName("searchPoiInfo")
    val searchPoiInfo: SearchPoiInfo
)

data class SearchPoiInfo(
    @SerializedName("count")
    val count: String,
    @SerializedName("page")
    val page: String,
    @SerializedName("pois")
    val pois: Pois,
    @SerializedName("totalCount")
    val totalCount: String
)

data class Pois(
    @SerializedName("poi")
    val poi: List<Poi?>
)