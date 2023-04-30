package com.example.deucapstone2023.data.dto.response.poi
import com.google.gson.annotations.SerializedName

data class EvChargers(
    @SerializedName("evCharger")
    val evCharger: List<EvCharger?>?
)