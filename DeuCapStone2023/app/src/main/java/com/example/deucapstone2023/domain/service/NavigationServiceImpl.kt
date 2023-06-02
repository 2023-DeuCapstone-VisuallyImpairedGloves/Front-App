package com.example.deucapstone2023.domain.service

import android.util.Log
import com.example.deucapstone2023.data.datasource.local.database.entity.UserLocation
import com.example.deucapstone2023.domain.model.LineModel
import com.example.deucapstone2023.domain.model.RouteModel
import com.example.deucapstone2023.domain.repository.LogRepository
import com.example.deucapstone2023.ui.base.AzimuthType
import com.example.deucapstone2023.ui.base.PointType
import com.example.deucapstone2023.ui.base.getAzimuthFromValue
import com.example.deucapstone2023.ui.screen.list.state.SensorInfo
import com.example.deucapstone2023.ui.screen.list.state.toAzimuthSensor
import com.example.deucapstone2023.ui.screen.list.state.toDistanceSensor
import com.example.deucapstone2023.utils.getCurrentTime
import com.skt.tmap.MapUtils
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class NavigationServiceImpl @Inject constructor(
    private val logRepository: LogRepository
) : NavigationService {

    var routeIndex: Int = 0
    var recentDistance: Int = 0
    var recentLineInfoIndex: Int = 0

    override fun getDistanceFromSource(source: LineModel, dest: LineModel): Int =
        MapUtils.getDistance(
            source.latitude,
            source.longitude,
            dest.latitude,
            dest.longitude
        ).toInt()

    override fun calculateAzimuth(source: LineModel, dest: LineModel): Double {
        val lat1 = source.latitude * Math.PI / 180
        val lat2 = dest.latitude * Math.PI / 180
        val lon1 = source.longitude * Math.PI / 180
        val lon2 = dest.longitude * Math.PI / 180

        val y = sin(lon2 - lon1) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(lon2 - lon1)
        val seta = atan2(y, x)
        return (seta * 180 / Math.PI + 360) % 360
    }

    override fun checkAzimuthDeviceWithLineInfo(azimuth: AzimuthType, route: RouteModel): Boolean =
        azimuth == getAzimuthFromValue(
            calculateAzimuth(
                source = route.lineInfo[recentLineInfoIndex],
                dest = route.lineInfo[recentLineInfoIndex + 1]
            )
        )

    override suspend fun navigateRouteOnMap(
        routeList: List<RouteModel>,
        source: LineModel,
        azimuth: AzimuthType,
        voiceOutput: (String) -> Unit,
        quitNavigation: () -> Unit,
        requestPedestrianRoute: ((String) -> Unit) -> Unit
    ) {
        val route = routeList[routeIndex]

        if (recentDistance - getDistanceFromSource(
                source = source,
                dest = LineModel(route.destinationLatitude, route.destinationLongitude)
            ) < 10
            && !checkAzimuthDeviceWithLineInfo(azimuth, route)
        ) {
            //경로 재요청
            requestPedestrianRoute(voiceOutput)
            Log.d(
                "test", "a - device: $azimuth route: ${
                    getAzimuthFromValue(
                        calculateAzimuth(
                            source = route.lineInfo[recentLineInfoIndex],
                            dest = route.lineInfo[recentLineInfoIndex + 1]
                        )
                    )
                }, index: $recentLineInfoIndex"
            )

            logRepository.setAzimuthSensor(
                SensorInfo(
                    id = 0,
                    status = "false",
                    desc = azimuth.name,
                    date = getCurrentTime()
                ).toAzimuthSensor()
            )

        } else {
            if (recentDistance > getDistanceFromSource(
                    source = source,
                    dest = LineModel(route.destinationLatitude, route.destinationLongitude)
                ) && recentDistance <= route.totalDistance
            ) {
                // 정상경로 -> LineString 정보를 활용 해서 현 위치 에서의 description 을 안내 해야함
                recentDistance = getDistanceFromSource(
                    source = source,
                    dest = LineModel(route.destinationLatitude, route.destinationLongitude)
                )
                logRepository.setUserLocation(
                    UserLocation(
                        id = 0,
                        date = getCurrentTime(),
                        latitude = source.latitude,
                        longitude = source.longitude,
                        nearPoiName = route.name,
                        source = routeList.first().name,
                        dest = routeList.last().name
                    )
                )
                logRepository.setAzimuthSensor(
                    SensorInfo(
                        id = 0,
                        status = "true",
                        desc = azimuth.name,
                        date = getCurrentTime()
                    ).toAzimuthSensor()
                )

                logRepository.setDistanceSensor(
                    SensorInfo(
                        id = 0,
                        status = recentLineInfoIndex.toString(),
                        desc = "${source.latitude} ${source.longitude}",
                        date = getCurrentTime()
                    ).toDistanceSensor()
                )

                val halfPoint = route.totalDistance.div(2)
                if (recentDistance >= halfPoint - 10 && recentDistance <= halfPoint + 10) {
                    guideRemainDistance(routeList = routeList, voiceOutput, quitNavigation)
                } else {
                    // point 없이 linestring이 이어서 결합된 경우 -> 지정 description 안내 후 남은 거리 만큼 이동 추가 안내
                    if (route.totalDistance != route.description.filter { it.isDigit() }.toInt()) {
                        if (route.description.filter { it.isDigit() }
                                .toInt() > route.totalDistance - recentDistance
                            && route.totalDistance - recentDistance in 5..15
                        )
                            guideStartDistance(route, voiceOutput)
                        else {
                            if (recentDistance <= 30) { // 남은 거리가 30미터 이하 이면, 안내
                                guideRemainDistance(
                                    routeList = routeList,
                                    voiceOutput,
                                    quitNavigation
                                )
                            }
                        }
                    } else { // point 와 linestring 이 1:1 매칭 인 경우
                        if (route.totalDistance - recentDistance in 5..15) {// 이동한 거리가 10미터 이하 일 때
                            guideStartDistance(route, voiceOutput)
                        } else {
                            if (recentDistance <= 30) { // 남은 거리가 30미터 이하 이면, 안내
                                guideRemainDistance(
                                    routeList = routeList,
                                    voiceOutput,
                                    quitNavigation
                                )
                            }
                        }
                    }
                    //정상경로 -> 10미터 단위로 gps가 바뀌기 때문에, lineString의 다음 좌표로 변경시킴
                    if (getDistanceFromSource(
                            source = source,
                            dest = route.lineInfo[recentLineInfoIndex + 1]
                        ) < 10
                    )
                        recentLineInfoIndex++
                }
            } else {
                //경로 재요청
                requestPedestrianRoute(voiceOutput)
                Log.d(
                    "test", "total: ${route.totalDistance}, remain: $recentDistance, actual: ${
                        getDistanceFromSource(
                            source = source,
                            dest = LineModel(route.destinationLatitude, route.destinationLongitude)
                        )
                    }"
                )
            }
        }

    }

    private fun guideStartDistance(
        route: RouteModel,
        voiceOutput: (String) -> Unit
    ) {
        when (route.pointType) {
            PointType.SP -> {
                voiceOutput("다음 안내시 까지 ${recentDistance}m 직진 해주세요.")
            }

            else -> voiceOutput("${route.name} 에서 ${route.turnType.desc} 후 다음 안내시 까지 직진 해주세요.")
        }
    }

    private fun guideRemainDistance(
        routeList: List<RouteModel>,
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
                    recentLineInfoIndex = 0
                }
            }
        }
    }

    override fun setInitValues(totalDistance: Int) {
        recentDistance = totalDistance
        routeIndex = 0
        recentLineInfoIndex = 0
    }
}