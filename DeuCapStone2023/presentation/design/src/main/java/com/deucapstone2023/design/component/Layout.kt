package com.deucapstone2023.design.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deucapstone2023.design.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultLayout(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    androidx.compose.material3.Scaffold(
        topBar = { topBar() },
        bottomBar = { bottomBar() },
        modifier = modifier,
        containerColor = white
    ) {
        content(it)
    }

}

@Composable
fun SnackBarLayout(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    scaffoldState: ScaffoldState,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = { topBar() },
        scaffoldState = scaffoldState,
        snackbarHost = { snackBarHostState ->
            Column() {
                SnackBarHostCustom(headerMessage = snackBarHostState.currentSnackbarData?.message
                    ?: "",
                    contentMessage = snackBarHostState.currentSnackbarData?.actionLabel ?: "",
                    snackBarHostState = scaffoldState.snackbarHostState,
                    disMissSnackBar = { scaffoldState.snackbarHostState.currentSnackbarData?.dismiss() })
                VerticalSpacer(height = 90.dp)
            }
        },
        backgroundColor = white,
        modifier = modifier.fillMaxSize()
    ) {
        content(it)
    }
}