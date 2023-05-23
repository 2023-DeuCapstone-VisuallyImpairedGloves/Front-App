package com.example.deucapstone2023.ui.screen.search.state

import android.content.Context
import com.example.deucapstone2023.ui.base.AzimuthType

interface NavigationManager {
    var routeIndex: Int
    var recentDistance: Int
    var destinationInfo: POIState
    var recentLineInfoIndex: Int

    fun navigateRouteOnMap(
        routeList: List<RouteState>,
        source: Location,
        azimuth: AzimuthType,
        voiceOutput: (String) -> Unit,
        quitNavigation: () -> Unit,
        requestPedestrianRoute: ((String) -> Unit) -> Unit,
        context: Context
    )

    fun guideStartDistance(
        route: RouteState,
        voiceOutput: (String) -> Unit
    )

    fun guideRemainDistance(
        routeList: List<RouteState>,
        voiceOutput: (String) -> Unit,
        quitNavigation: () -> Unit
    )

    fun getDistanceFromSource(source: Location, dest: Location): Int

    fun calculateAzimuth(source: Location, dest: Location): Double

    fun checkAzimuthDeviceWithLineInfo(azimuth: AzimuthType, route: RouteState): Boolean

}