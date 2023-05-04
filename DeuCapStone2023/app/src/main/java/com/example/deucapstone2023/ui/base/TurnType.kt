package com.example.deucapstone2023.ui.base

enum class TurnType(val code: Int, val desc: String) {
    NO_GUIDANCE(code = 7, desc = "안내 없음"),
    GO_STRAIGHT(code = 11, desc = "직진"),
    TURN_LEFT(code = 12, desc = "좌회전"),
    TURN_RIGHT(code = 13, desc = "우회전"),
    U_TURN(code = 14, desc = "유턴"),
    TURN_LEFT_8(code = 16, desc = "8시 방향 좌회전"),
    TURN_LEFT_10(code = 17, desc = "10시 방향 좌회전"),
    TURN_RIGHT_2(code = 18, desc = "2시 방향 우회전"),
    TURN_RIGHT_4(code = 19, desc = "4시 방향 우회전"),
    OVERPASS(code = 125, desc = "육교"),
    UNDERPASS(code = 126, desc = "지하보도"),
    STAIR_ENTRY(code = 127, desc = "계단 진입"),
    RAMP_ENTRY(code = 128, desc = "경사로 진입"),
    STAIR_RAMP_ENTRY(code = 129, desc = "계단 경사로 진입"),
    SOURCE(code = 200, desc = "출발지"),
    DESTINATION(code = 201, desc = "목적지"),
    CROSSWALK(code = 211, desc = "횡단보도"),
    LEFT_CROSSWALK(code = 212, desc = "좌측 횡단보도"),
    RIGHT_CROSSWALK(code = 213, desc = "우측 횡단보도"),
    CROSSWALK_8(code = 214, desc = "8시 방향 횡단보도"),
    CROSSWALK_10(code = 215, desc = "10시 방향 횡단보도"),
    CROSSWALK_2(code = 216, desc = "2시 방향 횡단보도"),
    CROSSWALK_4(code = 217, desc = "4시 방향 횡단보도"),
    ELEVATOR(code = 218, desc = "엘리베이터"),
    GO_STRAIGHT_TEMP(code = 233, desc = "직진 임시")
}
