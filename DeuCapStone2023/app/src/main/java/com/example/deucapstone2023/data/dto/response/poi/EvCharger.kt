package com.example.deucapstone2023.data.dto.response.poi
import com.google.gson.annotations.SerializedName

data class EvCharger(
    @SerializedName("chargerId")
    val chargerId: String?,
    @SerializedName("chargingDateTime")
    val chargingDateTime: String?,
    @SerializedName("isAvailable")
    val isAvailable: String?,
    @SerializedName("isFast")
    val isFast: String?,
    @SerializedName("operatorId")
    val operatorId: String?,
    @SerializedName("operatorName")
    val operatorName: String?,
    @SerializedName("powerType")
    val powerType: String?,
    @SerializedName("stationId")
    val stationId: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("updateDateTime")
    val updateDateTime: String?
)