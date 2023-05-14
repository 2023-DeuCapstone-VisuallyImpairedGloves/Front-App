package com.example.deucapstone2023.ui.screen.setting

import androidx.lifecycle.ViewModel
import com.example.deucapstone2023.ui.screen.setting.state.ButtonStatus
import com.example.deucapstone2023.ui.screen.setting.state.getButtonStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class SettingUiState(
    val controlStatus: ButtonStatus
) {
    companion object {
        fun getInitValues() = SettingUiState(
            controlStatus = ButtonStatus.OFF
        )
    }
}
@HiltViewModel
class SettingViewModel @Inject constructor() : ViewModel() {
    private val _settingUiState = MutableStateFlow(SettingUiState.getInitValues())
    val settingUiState get() = _settingUiState.asStateFlow()

    fun setControlStatus(status: ButtonStatus) = _settingUiState.update { state ->
        state.copy(controlStatus = status)
    }
}