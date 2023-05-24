package com.example.deucapstone2023.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deucapstone2023.R
import com.example.deucapstone2023.ui.theme.DeuCapStone2023Theme
import com.example.deucapstone2023.ui.theme.black
import com.example.deucapstone2023.ui.theme.gray
import com.example.deucapstone2023.ui.theme.white
import com.example.deucapstone2023.utils.tu

@Composable
fun DropDownMenuCustom(
    @DrawableRes iconHeader: Int? = null,
    @DrawableRes iconTail: Int? = null,
    label: String = "",
    text: String,
    items: List<String>,
    setTextChanged: (String) -> Unit
) {
    val dropDownExpandedState = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .clickable {
                dropDownExpandedState.value = !dropDownExpandedState.value
            }
    ) {
        if(label.isNotBlank()) {
            Text(
                text = label,
                color = black,
                fontWeight = FontWeight.Normal,
                fontSize = 13.tu
            )
            VerticalSpacer(height = 1.dp)
        }
        Row(
            modifier = Modifier
                .border(1.dp, gray, RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 10.dp)
        ) {
            iconHeader?.let {
                Icon(
                    painter = painterResource(id = iconHeader),
                    contentDescription = "DropDownMenuIconHeader"
                )
                HorizontalSpacer(width = 12.dp)
            }

            Text(
                text = text,
                fontSize = 16.tu,
                fontWeight = FontWeight.W400,
                color = black,
                modifier = Modifier.weight(1f)
            )

            iconTail?.let {
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = iconTail),
                        contentDescription = "DropDownMenuIconTail",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
        DropdownMenu(
            expanded = dropDownExpandedState.value,
            onDismissRequest = { dropDownExpandedState.value = false },
            modifier = Modifier.background(white)
        ) {
            items.forEach {
                DropdownMenuItem(
                    onClick = {
                        setTextChanged(it)
                        dropDownExpandedState.value = false
                    }
                ) {
                    Text(text = it)
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewDropDownMenuCustom() {
    DeuCapStone2023Theme() {
        DropDownMenuCustom(
            iconHeader = null,
            iconTail = R.drawable.ic_arrow_down_small,
            label = "라벨텍스트",
            text = "컨텐트 텍스트",
            setTextChanged = {},
            items = emptyList()
        )
    }
}