package com.deucapstone2023.ui.screen.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.deucapstone2023.design.component.SnackBarLayout
import com.deucapstone2023.design.theme.DeuCapStone2023Theme
import com.deucapstone2023.ui.screen.setting.component.BluetoothControlSetting
import com.deucapstone2023.ui.screen.setting.state.ButtonStatus
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(
    settingViewModel: SettingViewModel
) {
    val settingUiState by settingViewModel.settingUiState.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current,
        initialValue = SettingUiState.getInitValues()
    )

    SettingScreen(
        settingUiState = settingUiState,
        setControlStatus = settingViewModel::setControlStatus
    )
}

@Composable
private fun SettingScreen(
    settingUiState: SettingUiState,
    setControlStatus: (ButtonStatus) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    SnackBarLayout(scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(vertical = 16.dp, horizontal = 20.dp)
        ) {
            BluetoothControlSetting(
                status = settingUiState.controlStatus,
                setStatus = setControlStatus,
                showSnackBar = { message ->
                    coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = message,
                            duration = SnackbarDuration.Indefinite
                        )
                    }
                }
            )
        }
    }
}

@Composable
@Preview
private fun PreviewSettingScreen() =
    DeuCapStone2023Theme {
        SettingScreen(
            settingUiState = SettingUiState.getInitValues(),
            setControlStatus = {}
        )
    }
