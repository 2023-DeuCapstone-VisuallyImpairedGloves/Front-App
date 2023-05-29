package com.example.deucapstone2023.data.datasource.remote

import android.util.Log
import com.example.deucapstone2023.data.datasource.remote.api.TmapApi
import com.example.deucapstone2023.data.datasource.remote.dto.request.RouteRequest
import com.example.deucapstone2023.data.mapper.toRouteModel
import com.example.deucapstone2023.domain.model.RouteModel
import java.lang.Exception
import javax.inject.Inject

class FetchRouteDatasourceImpl @Inject constructor(
    private val tmapApi: TmapApi
) : FetchRouteDatasource {

    override suspend fun getRoutePedestrian(
        appKey: String,
        startLatitude: Double,
        startLongitude: Double,
        destinationPoiId: String,
        destinationLatitude: Double,
        destinationLongitude: Double,
        startName: String,
        destinationName: String
    ): List<RouteModel> =
        try {
            tmapApi.getRoutePedestrian(
                appKey = appKey,
                RouteRequest(
                    startLatitude = startLatitude,
                    startLongitude = startLongitude,
                    destinationPoiId = destinationPoiId,
                    destinationLatitude = destinationLatitude,
                    destinationLongitude = destinationLongitude,
                    startName = startName,
                    destinationName = destinationName
                )
            ).toRouteModel()
        } catch (e: Exception) {
            Log.d("test", "레파지토리 에러 : ${e.message} , ${e.printStackTrace()}")
            emptyList()
        }
}