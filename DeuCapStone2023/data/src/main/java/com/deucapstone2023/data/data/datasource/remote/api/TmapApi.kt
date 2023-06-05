package com.deucapstone2023.data.data.datasource.remote.api

import com.deucapstone2023.data.data.datasource.remote.dto.request.RouteRequest
import com.deucapstone2023.data.data.datasource.remote.dto.response.poi.POIResponse
import com.deucapstone2023.data.data.datasource.remote.dto.response.route.RouteResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface TmapApi {
    @GET("pois/")
    suspend fun getPOISearch(@QueryMap poiRequest: Map<String, String>): POIResponse?

    @POST("routes/pedestrian")
    suspend fun getRoutePedestrian(
        @Header("appKey") appKey: String,
        @Body routeRequest: RouteRequest
    ): RouteResponse

}