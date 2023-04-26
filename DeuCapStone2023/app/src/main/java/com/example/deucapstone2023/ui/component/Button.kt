package com.example.deucapstone2023.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deucapstone2023.ui.theme.DeuCapStone2023Theme
import com.example.deucapstone2023.ui.theme.black
import com.example.deucapstone2023.ui.theme.blue
import com.example.deucapstone2023.ui.theme.deepGray
import com.example.deucapstone2023.ui.theme.lightGray
import com.example.deucapstone2023.ui.theme.white
import com.example.deucapstone2023.utils.tu

@Composable
fun LargeButton(
    content: String,
    modifier: Modifier = Modifier,
    fontSize: Int = 18,
    enabled: Boolean = true,
    contentPaddingValues: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
) {
    val backgroundColor = when {
        enabled -> blue
        else -> lightGray
    }

    val textColor = when {
        enabled -> white
        else -> deepGray
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .background(backgroundColor)
            .padding(contentPaddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = content,
            fontSize = fontSize.tu,
            fontWeight = FontWeight.W700,
            color = textColor
        )
    }
}

@Preview
@Composable
private fun PreviewDefaultButton() =
    DeuCapStone2023Theme {
        LargeButton(content = "버튼 활성화")
    }

@Preview
@Composable
private fun PreviewDefaultButtonDisabled() =
    DeuCapStone2023Theme {
        LargeButton(
            content = "버튼 비활성화",
            enabled = false
        )
    }