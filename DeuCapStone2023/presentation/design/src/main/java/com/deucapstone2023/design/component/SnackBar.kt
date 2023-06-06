package com.deucapstone2023.design.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deucapstone2023.design.R
import com.deucapstone2023.design.theme.DeuCapStone2023Theme
import com.deucapstone2023.design.theme.blue
import com.deucapstone2023.design.theme.white
import com.deucapstone2023.design.utils.tu

@Composable
fun SnackBarHostCustom(
    headerMessage: String,
    contentMessage: String,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    disMissSnackBar: () -> Unit
){
    SnackbarHost(
        hostState = snackBarHostState,
        modifier = modifier,
        snackbar = {
            SnackBarCustom(
                headerMessage,
                contentMessage,
                disMissSnackBar
            )
        }
    )
}

@Composable
private fun SnackBarCustom(
    headerMessage: String,
    contentMessage: String,
    disMissSnackBar: () -> Unit
){
    Snackbar(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(78.dp),
        backgroundColor = blue,
        shape = RoundedCornerShape(8.dp),
        action =  {
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                androidx.compose.material3.IconButton(
                    onClick = disMissSnackBar,
                    modifier = Modifier
                        .height(24.dp)
                        .width(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_x),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        tint = white
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = headerMessage,
                fontWeight = FontWeight.W700,
                fontSize = 16.tu,
                color = white
            )
            if(contentMessage.isNotBlank()){
                VerticalSpacer(height = 4.dp)
                Text(
                    text = contentMessage,
                    fontWeight = FontWeight.W400,
                    fontSize = 13.tu,
                    color = white
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSnackBarCustom1(){
    DeuCapStone2023Theme {
        SnackBarCustom(
            headerMessage = "헤더메세지는 이렇게 보입니다.",
            contentMessage = "컨텐트메세지는 이렇게 보입니다.",
            disMissSnackBar = {}
        )
    }
}

@Preview
@Composable
private fun PreviewSnackBarCustom2(){
    DeuCapStone2023Theme {
        SnackBarCustom(
            headerMessage = "컨텐트메세지가 없다면 이렇게 보입니다.",
            contentMessage = "",
            disMissSnackBar = {}
        )
    }
}