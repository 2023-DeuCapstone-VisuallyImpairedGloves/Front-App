package com.deucapstone2023.app.ui.screen.list.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deucapstone2023.app.ui.screen.list.state.SensorInfo
import com.deucapstone2023.app.ui.theme.DeuCapStone2023Theme
import com.deucapstone2023.app.ui.utils.tu

@Composable
fun SensorInfoHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "날짜",
            modifier = Modifier
                .weight(1f)
                .wrapContentSize()
        )
        Text(
            text = "상태",
            modifier = Modifier
                .weight(1f)
                .wrapContentSize()
        )
        Text(
            text = "설명",
            modifier = Modifier
                .weight(1f)
                .wrapContentSize()
        )
    }
}

@Composable
fun SensorInfoItems(
    item: SensorInfo
) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = item.date,
            maxLines = 1,
            fontSize = 12.tu,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .weight(1f)
                .wrapContentSize()
        )

        Text(
            text = item.status,
            maxLines = 1,
            fontSize = 12.tu,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .weight(1f)
                .wrapContentSize()
        )

        Text(
            text = item.desc,
            maxLines = 1,
            fontSize = 12.tu,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .weight(1f)
                .wrapContentSize()
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SensorInfoHeaderPreview() =
    DeuCapStone2023Theme {
        SensorInfoHeader()
    }


@Composable
@Preview(showBackground = true)
private fun SensorInfoItemsPreview() =
    DeuCapStone2023Theme {
        SensorInfoItems(
            SensorInfo(
                id = 0,
                status = "ON",
                desc = "현재 상태는 이렇습니다.",
                date = "2023/05/24"
            )
        )
    }