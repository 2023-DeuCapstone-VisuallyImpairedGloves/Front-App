package com.deucapstone2023.app.ui.screen.list

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deucapstone2023.app.ui.screen.list.state.MenuItem
import com.deucapstone2023.app.ui.screen.list.state.SensorInfo
import com.deucapstone2023.app.ui.screen.list.state.UserLocation
import com.deucapstone2023.app.ui.screen.list.state.toSensorInfo
import com.deucapstone2023.app.ui.screen.list.state.toUserLocationState
import com.deucapstone2023.domain.domain.usecase.LogUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@Stable
data class ListUiState(
    val userLocationList: List<UserLocation>,
    val azimuthSensorList: List<SensorInfo>,
    val distanceSensorList: List<SensorInfo>,
    val obstacleSensorList: List<SensorInfo>,
    val menu: MenuItem
) {
    companion object {
        fun getInitValues() = ListUiState(
            userLocationList = emptyList(),
            azimuthSensorList = emptyList(),
            distanceSensorList = emptyList(),
            obstacleSensorList = emptyList(),
            menu = MenuItem.USERLOCATION
        )
    }
}

@HiltViewModel
class ListViewModel @Inject constructor(
    private val logUsecase: LogUsecase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ListUiState.getInitValues())
    val uiState get() = _uiState.asStateFlow()

    init {
        getUserLocationInfo()
        getAzimuthSensorInfo()
        getObstacleSensorInfo()
        getDistanceSensorInfo()
    }

    fun setMenu(menu: MenuItem) = _uiState.update { state -> state.copy(menu = menu) }

    private fun getUserLocationInfo() = logUsecase.getUserLocation()
        .onEach { userLocationList ->
            _uiState.update { state -> state.copy(userLocationList = userLocationList.map { it.toUserLocationState() }) }
        }.launchIn(viewModelScope)

    private fun getAzimuthSensorInfo() = logUsecase.getAzimuthSensor()
        .onEach { azimuthSensorList ->
            _uiState.update { state -> state.copy(azimuthSensorList = azimuthSensorList.map { it.toSensorInfo() }) }
        }.launchIn(viewModelScope)

    private fun getDistanceSensorInfo() = logUsecase.getDistanceSensor()
        .onEach { distanceSensorList ->
            _uiState.update { state -> state.copy(distanceSensorList = distanceSensorList.map { it.toSensorInfo() }) }
        }.launchIn(viewModelScope)

    private fun getObstacleSensorInfo() = logUsecase.getObstacleSensor()
        .onEach { obstacleSensorList ->
            _uiState.update { state -> state.copy(obstacleSensorList = obstacleSensorList.map { it.toSensorInfo() }) }
        }.launchIn(viewModelScope)
}