package com.deucapstone2023.ui.screen.temp.component

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.deucapstone2023.design.R
import com.deucapstone2023.design.component.DefaultAppBar
import com.deucapstone2023.design.theme.DeuCapStone2023Theme
import com.deucapstone2023.design.theme.deepGray
import com.deucapstone2023.design.theme.lightGray
import com.deucapstone2023.design.utils.tu

@Composable
fun SearchAppBar(
    title: String = "",
    hintMessage: String? = null,
    modifier: Modifier = Modifier,
    onTitleChanged: (String) -> Unit,
    onNavigateToHome: () -> Unit
) {
    val focusRequester by remember {
        mutableStateOf(FocusRequester())
    }

    DefaultAppBar(
        modifier = modifier,
        content = {
            BasicTextField(
                value = title,
                onValueChange = { value -> onTitleChanged(value) },
                maxLines = 1,
                cursorBrush = SolidColor(deepGray),
                modifier = Modifier.focusRequester(focusRequester),
                decorationBox = { innerTextFiled ->
                    if (title.isBlank() && !hintMessage.isNullOrBlank())
                        Text(
                            text = hintMessage,
                            color = lightGray,
                            fontSize = 12.tu,
                            fontWeight = FontWeight.Bold
                        )
                    else
                        innerTextFiled()
                }
            )
        },
        tailIcon = {
            IconButton(onClick = onNavigateToHome) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_navi),
                    contentDescription = "IconNavi"
                )
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
private fun PreviewHomeAppBar() =
    DeuCapStone2023Theme {
        SearchAppBar(
            title = "",
            hintMessage = "장소, 버스, 지하철, 주소 검색",
            onTitleChanged = {},
            onNavigateToHome = {}
        )
    }