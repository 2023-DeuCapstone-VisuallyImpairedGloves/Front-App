package com.deucapstone2023.app.ui.screen.list.state

enum class MenuItem(val desc: String) {
    USERLOCATION("사용자 위치 정보 조회"),
    AZIMUTH("방위 감지 센서 정보 조회"),
    DISTANCE("거리 감지 센서 정보 조회"),
    OBSTACLE("장애물 감지 센서 정보 조회");
}

fun String.findMenuFromString() = MenuItem.values().find { it.desc == this }