package com.example.deucapstone2023.ui.navigation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable
import com.example.deucapstone2023.R

@Stable
sealed class NavigationItem(
    val title:String,
    val route:String,
    @DrawableRes val icon:Int? = null,
    @DrawableRes val iconClicked:Int? = null,
) {
    object HOME: NavigationItem(
        title = "홈",
        route = "home",
        icon = R.drawable.ic_home,
        iconClicked = R.drawable.ic_home_clicked
    )
    object SETTING: NavigationItem(
        title = "설정",
        route = "setting",
        icon = R.drawable.ic_setting,
        iconClicked = R.drawable.ic_setting_clicked
    )

    object SEARCH: NavigationItem(
        title = "설정",
        route = "setting",
        icon = R.drawable.ic_setting,
        iconClicked = R.drawable.ic_setting_clicked
    )
}