package com.example.deucapstone2023.ui.screen.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deucapstone2023.domain.usecase.SettingUsecase
import com.example.deucapstone2023.ui.screen.setting.state.ButtonStatus
import com.example.deucapstone2023.ui.screen.setting.state.getButtonStatus
import com.example.deucapstone2023.ui.screen.setting.state.toBoolean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
class SettingViewModel @Inject constructor(
    private val settingUsecase: SettingUsecase
) : ViewModel() {
    private val _settingUiState = MutableStateFlow(SettingUiState.getInitValues())
    val settingUiState get() = _settingUiState.asStateFlow()

    init {
        getControlStatus()
    }

    fun setControlStatus(status: ButtonStatus) {
        viewModelScope.launch {
            settingUsecase.setConnectionStatus(status.toBoolean())
        }
    }

    private fun getControlStatus() = settingUsecase.getConnectionStatus()
        .onEach { status ->
            _settingUiState.update { state ->
                state.copy(controlStatus = getButtonStatus(status))
            }
        }.catch { e ->
            Log.d("test", "error: ${e.message}")
        }.launchIn(viewModelScope)
}