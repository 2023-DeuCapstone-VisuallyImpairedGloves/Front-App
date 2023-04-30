package com.example.deucapstone2023.ui.screen.home.component

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.deucapstone2023.R
import com.example.deucapstone2023.ui.component.DefaultAppBar
import com.example.deucapstone2023.ui.theme.DeuCapStone2023Theme
import com.example.deucapstone2023.ui.theme.lightGray
import com.example.deucapstone2023.utils.tu

@Composable
fun HomeAppBar(
    title: String = "",
    hintMessage: String? = null,
    modifier: Modifier = Modifier,
    onTitleChanged: (String) -> Unit,
    onNavigateToNaviScreen: () -> Unit
) {

    DefaultAppBar(
        headerIcon = R.drawable.ic_hamburger,
        onHeaderIconClick = { /*TODO*/ },
        modifier = modifier,
        content = {
            BasicTextField(
                value = title,
                onValueChange = { value -> onTitleChanged(value) },
                maxLines = 1,
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
            IconButton(onClick = onNavigateToNaviScreen) {
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
        HomeAppBar(
            title = "",
            hintMessage = "장소, 버스, 지하철, 주소 검색",
            onTitleChanged = {},
            onNavigateToNaviScreen = {}
        )
    }