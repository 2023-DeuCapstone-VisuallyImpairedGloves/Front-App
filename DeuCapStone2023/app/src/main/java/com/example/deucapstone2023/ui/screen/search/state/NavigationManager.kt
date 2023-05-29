package com.example.deucapstone2023.ui.screen.search.state

import android.content.Context
import com.example.deucapstone2023.data.datasource.local.database.entity.UserLocation
import com.example.deucapstone2023.ui.base.AzimuthType
import com.example.deucapstone2023.ui.screen.list.state.SensorInfo

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
        context: Context,
        setUserLocationOnDatabase: (UserLocation) -> Unit,
        setAzimuthSensorOnDatabase: (SensorInfo) -> Unit,
        setIndex: (SensorInfo) -> Unit
    )

    fun getDistanceFromSource(source: Location, dest: Location): Int

    fun calculateAzimuth(source: Location, dest: Location): Double

    fun checkAzimuthDeviceWithLineInfo(azimuth: AzimuthType, route: RouteState): Boolean

}