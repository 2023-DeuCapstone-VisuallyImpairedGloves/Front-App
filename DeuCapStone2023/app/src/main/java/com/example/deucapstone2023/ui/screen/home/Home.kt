package com.example.deucapstone2023.ui.screen.home

import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import com.example.deucapstone2023.ui.component.DefaultLayout
import com.example.deucapstone2023.ui.screen.home.component.HomeAppBar
import com.example.deucapstone2023.ui.theme.DeuCapStone2023Theme
import com.example.deucapstone2023.utils.addFocusCleaner
import com.skt.tmap.TMapView

@Composable
fun Home(
    tMapView: TMapView,
    title: String,
    onTitleChanged: (String) -> Unit,
    onNavigateToNaviScreen: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    DefaultLayout(
        modifier = Modifier.addFocusCleaner(focusManager)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { context ->
                    FrameLayout(context).apply {
                        addView(tMapView)
                    }

                },
                modifier = Modifier.align(Alignment.Center)
            )
            HomeAppBar(
                title = title,
                hintMessage = "장소, 버스, 지하철, 주소 검색",
                onTitleChanged = onTitleChanged,
                modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth().padding(16.dp),
                onNavigateToNaviScreen = onNavigateToNaviScreen
            )
        }
    }
}

@Composable
@Preview
private fun PreviewHomeScreen() {
    val context = LocalContext.current
    DeuCapStone2023Theme {
        Home(
            tMapView = TMapView(context),
            title = "",
            onTitleChanged = {},
            onNavigateToNaviScreen = {}
        )
    }
}