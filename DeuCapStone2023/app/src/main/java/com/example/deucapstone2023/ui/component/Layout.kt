package com.example.deucapstone2023.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.deucapstone2023.ui.theme.white

@Composable
fun DefaultLayout(
    topBar: @Composable () -> Unit = {},
    scaffoldState: ScaffoldState,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = { topBar() },
        scaffoldState = scaffoldState,
        snackbarHost = { snackBarHostState ->
            Column() {
                SnackBarHostCustom(headerMessage = snackBarHostState.currentSnackbarData?.message ?: "",
                    contentMessage = snackBarHostState.currentSnackbarData?.actionLabel ?: "",
                    snackBarHostState = scaffoldState.snackbarHostState,
                    disMissSnackBar = { scaffoldState.snackbarHostState.currentSnackbarData?.dismiss() })
                VerticalSpacer(height = 90.dp)
            }
        },
        backgroundColor = white
    ) {
        content(it)
    }
}