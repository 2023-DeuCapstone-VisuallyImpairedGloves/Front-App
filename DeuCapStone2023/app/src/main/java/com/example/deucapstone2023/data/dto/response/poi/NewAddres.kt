package com.example.deucapstone2023.data.dto.response.poi
import com.google.gson.annotations.SerializedName

data class NewAddres(
    @SerializedName("bldNo1")
    val bldNo1: String?,
    @SerializedName("bldNo2")
    val bldNo2: String?,
    @SerializedName("centerLat")
    val centerLat: String?,
    @SerializedName("centerLon")
    val centerLon: String?,
    @SerializedName("frontLat")
    val frontLat: String?,
    @SerializedName("frontLon")
    val frontLon: String?,
    @SerializedName("fullAddressRoad")
    val fullAddressRoad: String?,
    @SerializedName("roadId")
    val roadId: String?,
    @SerializedName("roadName")
    val roadName: String?
)