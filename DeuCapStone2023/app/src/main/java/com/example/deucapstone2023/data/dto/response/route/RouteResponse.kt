package com.example.deucapstone2023.data.dto.response.poi


import com.google.gson.annotations.SerializedName

data class RouteResponse(
    @SerializedName("features")
    val features: List<Feature>?,
    @SerializedName("type")
    val type: String
)

data class Feature(
    @SerializedName("geometry")
    val geometry: Geometry?,
    @SerializedName("properties")
    val properties: Properties?,
    @SerializedName("type")
    val type: String?
)

data class Geometry(
    @SerializedName("coordinates")
    val coordinates: List<Any>?,
    @SerializedName("type")
    val type: String?
)

data class Properties(
    @SerializedName("categoryRoadType")
    val categoryRoadType: Int?,
    @SerializedName("crossName")
    val crossName: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("direction")
    val direction: String?,
    @SerializedName("distance")
    val distance: Int?,
    @SerializedName("facilityName")
    val facilityName: String?,
    @SerializedName("facilityType")
    val facilityType: String?,
    @SerializedName("guidePointName")
    val guidePointName: String?,
    @SerializedName("index")
    val index: Int?,
    @SerializedName("intersectionName")
    val intersectionName: String?,
    @SerializedName("lineIndex")
    val lineIndex: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("nearPoiName")
    val nearPoiName: String?,
    @SerializedName("nearPoiX")
    val nearPoiX: String?,
    @SerializedName("nearPoiY")
    val nearPoiY: String?,
    @SerializedName("pointIndex")
    val pointIndex: Int?,
    @SerializedName("pointType")
    val pointType: String?,
    @SerializedName("roadName")
    val roadName: String?,
    @SerializedName("roadType")
    val roadType: Int?,
    @SerializedName("time")
    val time: Int?,
    @SerializedName("turnType")
    val turnType: Int?,
    @SerializedName("totalTime")
    val totalTime: Int?,
)