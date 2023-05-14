package com.example.deucapstone2023.ui.screen.setting.state

import androidx.compose.runtime.Stable

@Stable
enum class ButtonStatus(val displayName: String) {
    ON(displayName = "ON"),
    OFF(displayName = "OFF");
}

fun getButtonStatus(status: Boolean) = when(status) {
    true -> ButtonStatus.ON
    else -> ButtonStatus.OFF
}
