package com.example.deucapstone2023.ui.screen.search.state

import com.example.deucapstone2023.ui.base.AzimuthType
import com.example.deucapstone2023.ui.base.PointType
import com.example.deucapstone2023.ui.base.getAzimuthFromValue
import com.example.deucapstone2023.ui.base.toAzimuthType
import com.skt.tmap.MapUtils
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

data class NavigationManager(
    var routeIndex: Int,
    var recentDistance: Int,
    var destinationInfo: POIState,
    var azimuth: AzimuthType
) {
    constructor() : this(
        routeIndex = 0,
        recentDistance = 0,
        destinationInfo = POIState.getInitValues(),
        azimuth = AzimuthType.SOUTH
    )

    private fun getDistanceFromSource(source: Location, dest: Location) =
        MapUtils.getDistance(
            source.latitude,
            source.longitude,
            dest.latitude,
            dest.longitude
        ).toInt()

    fun setInitAzimuth(source: Location, dest: Location) {
        val lat1 = source.latitude * Math.PI / 180
        val lat2 = dest.latitude * Math.PI / 180
        val lon1 = source.longitude * Math.PI / 180
        val lon2 = dest.longitude * Math.PI / 180

        val y = sin(lon2 - lon1) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(lon2 - lon1)
        val seta = atan2(y, x)
        val bearing = (seta * 180 / Math.PI + 360) % 360
        azimuth = getAzimuthFromValue(bearing)
    }

    fun navigateRouteOnMap(
        routeList: List<RouteState>,
        source: Location,
        azimuth: AzimuthType,
        voiceOutput: (String) -> Unit,
        quitNavigation: () -> Unit,
        requestPedestrianRoute: ((String) -> Unit) -> Unit
    ) {
        val route = routeList[routeIndex]
        val halfPoint = route.totalDistance.div(2)

        //TODO 현재 방위에 따라 다음 경로를 가기 위한 방위 안내 필요

        if (recentDistance > getDistanceFromSource(
                source = source,
                dest = Location(route.destinationLatitude, route.destinationLongitude)
            ) && recentDistance <= route.totalDistance && this.azimuth == azimuth
        ) {
            // 정상경로 -> LineString 정보를 활용 해서 현 위치 에서의 description 을 안내 해야함
            recentDistance = getDistanceFromSource(
                source = source,
                dest = Location(route.destinationLatitude, route.destinationLongitude)
            )

            if (recentDistance >= halfPoint - 10 && recentDistance <= halfPoint + 10) {
                guideRemainDistance(routeList = routeList,azimuth, voiceOutput, quitNavigation)
            } else {
                // point 없이 linestring이 이어서 결합된 경우 -> 지정 description 안내 후 남은 거리 만큼 이동 추가 안내
                if (route.totalDistance != route.description.filter { it.isDigit() }.toInt()) {
                    if (route.description.filter { it.isDigit() }
                            .toInt() > (route.totalDistance - recentDistance))
                        guideStartDistance(route, voiceOutput)
                    else {
                        if (recentDistance <= 30) { // 남은 거리가 30미터 이하 이면, 안내
                            guideRemainDistance(routeList = routeList,azimuth, voiceOutput, quitNavigation)
                        }
                    }
                } else { // point 와 linestring 이 1:1 매칭 인 경우
                    if (route.totalDistance - recentDistance <= 10) {// 이동한 거리가 10미터 이하 일 때
                        guideStartDistance(route, voiceOutput)
                    } else {
                        if (recentDistance <= 30) { // 남은 거리가 30미터 이하 이면, 안내
                            guideRemainDistance(routeList = routeList,azimuth, voiceOutput, quitNavigation)
                        }
                    }
                }
            }
        } else {
            //경로 재요청
            this.azimuth = azimuth
            requestPedestrianRoute(voiceOutput)
        }

    }

    private fun guideStartDistance(route: RouteState, voiceOutput: (String) -> Unit) {
        when (route.pointType) {
            PointType.SP -> {
                voiceOutput("다음 안내시 까지 ${route.description.filter { it.isDigit() }}m 직진 해주세요.")
            }

            else -> voiceOutput("${route.name} 에서 ${route.turnType.desc} 후 다음 안내시 까지 직진 해주세요.")
        }
    }

    private fun guideRemainDistance(
        routeList: List<RouteState>,
        currentAzimuthType: AzimuthType,
        voiceOutput: (String) -> Unit,
        quitNavigation: () -> Unit
    ) {
        when (routeList[routeIndex + 1].pointType) {
            PointType.EP -> { // 목적지 라면, 목적지 안내
                if (recentDistance <= 15) {
                    voiceOutput("목적지에 부근에 도착했습니다. 경로안내를 종료합니다.")
                    quitNavigation()
                } else {
                    voiceOutput("${recentDistance}m 직진해 주세요. 이어서 목적지가 있습니다.")
                }
            }

            else -> { // 목적지가 아니면, 현재 남은 거리 안내후 다음경로 안내
                voiceOutput("${recentDistance}m 직진해 주세요. 이어서 ${routeList[routeIndex + 1].description}해 주세요")
                if (recentDistance <= 15) {// 남은 거리가 15미터 이하 이면, 다음 경로로 업데이트
                    recentDistance = routeList[++routeIndex].totalDistance
                    this.azimuth = routeList[routeIndex].turnType.toAzimuthType(currentAzimuthType)
                }
            }
        }
    }
}