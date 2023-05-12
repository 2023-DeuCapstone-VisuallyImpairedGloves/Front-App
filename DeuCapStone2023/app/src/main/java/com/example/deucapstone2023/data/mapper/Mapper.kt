package com.example.deucapstone2023.data.mapper

import com.example.deucapstone2023.data.dto.response.poi.POIResponse
import com.example.deucapstone2023.data.dto.response.poi.Poi
import com.example.deucapstone2023.data.dto.response.poi.RouteResponse
import com.example.deucapstone2023.domain.model.LineModel
import com.example.deucapstone2023.domain.model.POIModel
import com.example.deucapstone2023.domain.model.RouteModel

fun POIModel.toRequestDTO(appKey: String) = mutableMapOf<String, String>().apply {
    put("version", "1")
    put("page", "1")
    put("count", "20")
    put("appKey", appKey)
    put("searchKeyword", name)
    if (radius.isNotBlank())
        put("radius", radius)
    if (centerLon != .0)
        put("centerLon", centerLon.toString())
    if (centerLat != .0)
        put("centerLat", centerLat.toString())
    if (searchtypCd.isNotBlank())
        put("searchtypCd", searchtypCd)
}

fun POIResponse.toPOIModel() =
    this.searchPoiInfo.pois.poi.map { it?.toPOIModel() ?: POIModel.getInitValues() }

fun Poi.toPOIModel() = POIModel(
    id = id?.toLong() ?: 0L,
    name = name ?: "",
    address = newAddressList.newAddress.map { it.fullAddressRoad }.first() ?: "",
    radius = radius ?: "",
    bizName = bizName ?: upperBizName ?: middleBizName ?: lowerBizName ?: detailBizName ?: "",
    centerLat = noorLat?.toDouble() ?: .0,
    centerLon = noorLon?.toDouble() ?: .0,
    searchtypCd = ""
)

fun RouteResponse.toRouteModel() = this.run {
    val list = mutableListOf<RouteModel>()

    var routeName = ""
    var routeDescription = ""
    var routeTurnType = 0
    var routePointType = ""
    var routeFacilityType = 0
    var startLatitude = 0.0
    var startLongitude = 0.0
    var destinationLatitude = 0.0
    var destinationLongitude = 0.0
    var totalDistance = 0
    var totalTime = 0
    val lineInfo = mutableListOf<LineModel>()

    this.features?.forEachIndexed { index, feature ->
        when (feature.geometry?.type) {
            "Point" -> {
                if (index != 0)
                    list.add(
                        RouteModel(
                            name = routeName,
                            description = routeDescription,
                            turnType = routeTurnType,
                            pointType = routePointType,
                            facilityType = routeFacilityType,
                            startLatitude = startLatitude,
                            startLongitude = startLongitude,
                            destinationLatitude = destinationLatitude,
                            destinationLongitude = destinationLongitude,
                            lineInfo = lineInfo.distinct(),
                            totalDistance = totalDistance,
                            totalTime = totalTime
                        )
                    )

                val location = feature.geometry.coordinates

                startLatitude = location?.get(0) as Double
                startLongitude = location.get(1) as Double
                routeDescription = feature.properties?.description ?: ""
                routeTurnType = feature.properties?.turnType ?: 1
                routePointType = feature.properties?.pointType ?: ""
                routeName = ""
                routeFacilityType = 0
                destinationLatitude = 0.0
                destinationLongitude = 0.0
                totalDistance = 0
                totalTime = 0
                lineInfo.clear()

                if(index == features.lastIndex)
                    list.add(
                        RouteModel(
                            name = routeName,
                            description = routeDescription,
                            turnType = routeTurnType,
                            pointType = routePointType,
                            facilityType = routeFacilityType,
                            startLatitude = startLatitude,
                            startLongitude = startLongitude,
                            destinationLatitude = destinationLatitude,
                            destinationLongitude = destinationLongitude,
                            lineInfo = lineInfo,
                            totalDistance = totalDistance,
                            totalTime = totalTime
                        )
                    )
            }

            "LineString" -> {
                val location = feature.geometry.coordinates
                destinationLatitude = (((location?.last() ?: .0) as List<*>?)?.get(0) ?: .0) as Double
                destinationLongitude = (((location?.last() ?: .0) as List<*>?)?.get(1) ?: .0) as Double
                routeName = feature.properties?.name ?: ""
                routeFacilityType = if (feature.properties?.facilityType?.isNotBlank() == true) feature.properties.facilityType.toInt() ?: 0 else 0
                totalDistance = feature.properties?.distance ?: 0
                totalTime = feature.properties?.time ?: 0
                lineInfo.addAll(location?.map {
                    val eachLocation = (it as List<*>)
                    LineModel(eachLocation[1] as Double, eachLocation[0] as Double)
                } ?: emptyList())
            }
        }
    }

    list.toList()
}


/*this.features?.forEachIndexed { index,feature ->
when (features.lastIndex == 0) {
    true -> {
        val location = features[0].geometry?.coordinates

        RouteModel(
            startLatitude = location?.get(0) as Double,
            startLongitude = location.get(1) as Double,
            destinationLatitude = .0,
            destinationLongitude = .0,
            name = "",
            description = features[0].properties?.description ?: "",
            turnType = features[0].properties?.turnType ?: 1,
            pointType = features[0].properties?.pointType ?: "",
            facilityType = 0,
            totalDistance = 0,
            totalTime = 0,
            lineInfo = emptyList()
        )
    }

    false -> {
        val location = features[1].geometry?.coordinates

        RouteModel(
            startLatitude = (((location?.first() ?: .0) as List<*>?)?.get(0) ?: .0) as Double,
            startLongitude = (((location?.first() ?: .0) as List<*>?)?.get(1) ?: .0) as Double,
            destinationLatitude = (((location?.last() ?: .0) as List<*>?)?.get(0) ?: .0) as Double,
            destinationLongitude = (((location?.last() ?: .0) as List<*>?)?.get(1) ?: .0) as Double,
            name = features[1].properties?.name ?: "",
            description = features[0].properties?.description ?: "",
            turnType = features[0].properties?.turnType ?: 0,
            pointType = features[0].properties?.pointType ?: "",
            facilityType = if (features[1].properties?.facilityType?.isNotBlank() == true) features[1].properties?.facilityType?.toInt() ?: 0 else 0,
            totalDistance = features[1].properties?.distance ?: 0,
            totalTime = features[1].properties?.time ?: 0,
            lineInfo = location?.map {
                val eachLocation = (it as List<*>)
                LineModel(eachLocation[1] as Double, eachLocation[0] as Double)
            } ?: emptyList()
        )
    }
}
} ?: emptyList()*/
