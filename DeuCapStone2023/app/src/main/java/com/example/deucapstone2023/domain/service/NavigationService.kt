package com.example.deucapstone2023.domain.service

import com.example.deucapstone2023.domain.model.LineModel
import com.example.deucapstone2023.domain.model.RouteModel
import com.example.deucapstone2023.ui.base.AzimuthType

interface NavigationService {
    suspend fun navigateRouteOnMap(
        routeList: List<RouteModel>,
        source: LineModel,
        azimuth: AzimuthType,
        voiceOutput: (String) -> Unit,
        quitNavigation: () -> Unit,
        requestPedestrianRoute: ((String) -> Unit) -> Unit
    )

    fun getDistanceFromSource(source: LineModel, dest: LineModel): Int

    fun calculateAzimuth(source: LineModel, dest: LineModel): Double

    fun checkAzimuthDeviceWithLineInfo(azimuth: AzimuthType, route: RouteModel): Boolean

    fun setInitValues(totalDistance: Int)
}