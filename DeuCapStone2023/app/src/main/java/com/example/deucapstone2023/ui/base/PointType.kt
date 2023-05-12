package com.example.deucapstone2023.ui.base

import androidx.compose.runtime.Stable

@Stable
enum class PointType(val desc: String, val display: String) {
    INIT("","초기값"),
    SP("SP","출발지"),
    EP("EP","도착지"),
    GP("GP","일반 안내점");

    companion object {
        fun getType(s: String) = PointType.values().first { it.desc == s }
    }
}