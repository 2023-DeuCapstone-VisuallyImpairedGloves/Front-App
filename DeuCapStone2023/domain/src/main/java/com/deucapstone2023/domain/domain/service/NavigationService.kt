package com.deucapstone2023.domain.domain.service

import com.deucapstone2023.domain.domain.model.AzimuthType
import com.deucapstone2023.domain.domain.model.LineModel
import com.deucapstone2023.domain.domain.model.RouteModel

interface NavigationService {
    suspend fun navigateRouteOnMap(
        routeList: List<RouteModel>,
        source: LineModel,
        azimuth: AzimuthType,
        voiceOutput: (String) -> Unit,
        quitNavigation: () -> Unit,
        requestPedestrianRoute: ((String) -> Unit) -> Unit
    )

    fun getDistanceFromSourceToDest(source: LineModel, dest: LineModel): Int

    fun calculateAzimuth(source: LineModel, dest: LineModel): Double

    fun checkAzimuthDeviceWithLineInfo(azimuth: AzimuthType, route: RouteModel): Boolean

    fun setInitValues(totalDistance: Int)
}