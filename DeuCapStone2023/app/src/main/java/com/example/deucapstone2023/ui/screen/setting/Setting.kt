package com.example.deucapstone2023.ui.screen.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deucapstone2023.ui.component.SnackBarLayout
import com.example.deucapstone2023.ui.screen.setting.component.BluetoothControlSetting
import com.example.deucapstone2023.ui.screen.setting.state.ButtonStatus
import com.example.deucapstone2023.ui.theme.DeuCapStone2023Theme
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(
    settingUiState: SettingUiState,
    setControlStatus: (ButtonStatus) -> Unit,
    setUpBluetooth: (() -> Unit, () -> Unit) -> Unit,
    disableBluetooth: (() -> Unit, () -> Unit) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    SnackBarLayout(scaffoldState = scaffoldState) {
        Column(modifier = Modifier
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
                },
                setUpBluetooth = setUpBluetooth,
                disableBluetooth = disableBluetooth
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
            setControlStatus = {},
            setUpBluetooth = { _, _ -> },
            disableBluetooth = { _, _ -> }
        )
    }
