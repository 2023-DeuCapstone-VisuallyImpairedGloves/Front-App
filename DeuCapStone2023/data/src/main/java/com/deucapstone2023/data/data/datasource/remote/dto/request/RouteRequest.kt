package com.deucapstone2023.data.data.datasource.remote.dto.request

import com.google.gson.annotations.SerializedName

data class RouteRequest(
    @SerializedName("startX") val startLongitude: Double,
    @SerializedName("startY") val startLatitude: Double,
    @SerializedName("endPoiId") val destinationPoiId: String,
    @SerializedName("endX") val destinationLongitude: Double,
    @SerializedName("endY") val destinationLatitude: Double,
    @SerializedName("startName") val startName: String,
    @SerializedName("endName") val destinationName: String
)
