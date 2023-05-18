package com.example.deucapstone2023.ui.screen.setting.component

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

@Composable
fun BluetoothControlSetting(
    status: ButtonStatus,
    setStatus: (ButtonStatus) -> Unit,
    showSnackBar: (String) -> Unit
) {
    when (status) {
        ButtonStatus.ON -> {
            showSnackBar("자동 연결이 활성화 되었습니다.")
        }

        ButtonStatus.OFF -> {
            showSnackBar("자동 연결이 비활성화 되었습니다.")
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
            text = "블루투스 자동 연결",
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
    DeuCapStone2023Theme() {
        BluetoothControlSetting(
            status = ButtonStatus.ON,
            setStatus = {},
            showSnackBar = {}
        )
    }
}
