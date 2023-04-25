package com.example.deucapstone2023.ui.navigation

import androidx.compose.runtime.Stable

@Stable
sealed class NavigationItem(
    val title:String,
    val route:String
) {
    object LOGIN: NavigationItem(
        title = "로그인",
        route = "login"
    )
    object HOME: NavigationItem(
        title = "홈",
        route = "home"
    )
    object SETTING: NavigationItem(
        title = "설정",
        route = "setting"
    )
}