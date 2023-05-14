package com.example.deucapstone2023.ui.screen.setting.component

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deucapstone2023.ui.component.VerticalSpacer
import com.example.deucapstone2023.ui.screen.setting.state.ButtonStatus
import com.example.deucapstone2023.ui.theme.DeuCapStone2023Theme
import com.example.deucapstone2023.ui.theme.blue
import com.example.deucapstone2023.ui.theme.deepGray
import com.example.deucapstone2023.ui.theme.white
import com.example.deucapstone2023.utils.tu

val PERMISSIONS = arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN)

@Composable
fun BluetoothControlSetting(
    status: ButtonStatus,
    setStatus: (ButtonStatus) -> Unit,
    showSnackBar: (String) -> Unit,
    setUpBluetooth: (() -> Unit, () -> Unit) -> Unit,
    disableBluetooth: (() -> Unit, () -> Unit) -> Unit
) {
    val bluetoothSetUpLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                setUpBluetooth(
                    {
                        showSnackBar("장치에 연결되었습니다.")
                    },
                    {
                        showSnackBar("장치 연결에 실패했습니다.")
                        setStatus(ButtonStatus.OFF)
                    }
                )
            }

            Activity.RESULT_CANCELED -> {}
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.any { it.value.not() }) {
            showSnackBar("권한이 필요합니다.")
        } else {
            bluetoothSetUpLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        }
    }

    when (status) {
        ButtonStatus.ON -> {
            permissionLauncher.launch(PERMISSIONS)
        }

        ButtonStatus.OFF -> {
            disableBluetooth(
                {
                    showSnackBar("장치가 연결해제 되었습니다.")
                },
                {
                    showSnackBar("장치의 연결해제에 실패했습니다.")
                }
            )
        }
    }

    val indicatorBias by animateFloatAsState(
        when (status) {
            ButtonStatus.ON -> 1f
            ButtonStatus.OFF -> -1f
        }
    )

    Row(
        modifier = Modifier
            .height(36.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "현재 시간 항상 보기",
            fontSize = 16.tu,
            fontWeight = FontWeight.W700,
            color = deepGray,
            modifier = Modifier.weight(1f)
        )
        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .border(1.dp, blue, RoundedCornerShape(35.dp))
                .background(white, RoundedCornerShape(35.dp))
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(100.dp))
                        .clickable {
                            setStatus(ButtonStatus.OFF)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "OFF",
                        color = blue,
                        fontSize = 16.tu,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(100.dp))
                        .clickable {
                            setStatus(ButtonStatus.ON)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ON",
                        color = blue,
                        fontSize = 16.tu,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            VerticalSpacer(height = 6.dp)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(maxWidth / 2)
                    .padding(3.dp)
                    .background(blue, RoundedCornerShape(100.dp))
                    .align(BiasAlignment(indicatorBias, 0f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = status.displayName,
                    color = white,
                    fontSize = 16.tu,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Preview
@Composable
private fun PreviewTimeStatusSetting() {
    val context = LocalContext.current
    DeuCapStone2023Theme() {
        BluetoothControlSetting(
            status = ButtonStatus.ON,
            setStatus = {},
            showSnackBar = {},
            setUpBluetooth = { _, _ -> },
            disableBluetooth = { _, _ -> }
        )
    }
}
