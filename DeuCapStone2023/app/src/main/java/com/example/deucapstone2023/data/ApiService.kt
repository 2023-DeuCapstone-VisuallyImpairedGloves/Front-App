package com.example.deucapstone2023.data

import com.example.deucapstone2023.data.dto.response.poi.POIResponse
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {
    @GET("pois/")
    suspend fun getPOISearch(@QueryMap poiRequest: Map<String, String>) : POIResponse
}