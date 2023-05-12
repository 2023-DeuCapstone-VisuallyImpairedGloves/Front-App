package com.example.deucapstone2023.ui.base

import androidx.compose.runtime.Stable

@Stable
enum class FacilityType(val code: Int,val desc:String) {
    INIT(code = 0, desc = "초기값"),
    BRIDGE(code = 1, desc = "교량"),
    TUNNEL(code = 2, desc = "터널"),
    oVERPASS(code = 3, desc = "고가도로"),
    COMMON_PEDESTRIAN_ROAD(code = 11, desc = "일반 보행자 도로"),
    OVERPASS(code = 12, desc = "육교"),
    UNDERPASS(code = 14, desc = "지하보도"),
    CROSSWALK(code = 15, desc = "횡단보도"),
    LARGE_FACILITIES(code = 16, desc = "대형 시설물 이동통로"),
    STAIR(code = 17, desc = "계단");

    companion object {
        fun getType(code: Int) = values().first { it.code == code }
    }
}