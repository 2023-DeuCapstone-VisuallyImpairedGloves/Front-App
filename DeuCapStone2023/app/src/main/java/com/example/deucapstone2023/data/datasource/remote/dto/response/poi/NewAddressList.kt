package com.example.deucapstone2023.data.datasource.remote.dto.response.poi
import com.google.gson.annotations.SerializedName

data class NewAddressList(
    @SerializedName("newAddress")
    val newAddress: List<NewAddres>
)