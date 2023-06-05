package com.deucapstone2023.data.data.datasource.remote.dto.response.poi
import com.google.gson.annotations.SerializedName

data class EvChargers(
    @SerializedName("evCharger")
    val evCharger: List<EvCharger?>?
)