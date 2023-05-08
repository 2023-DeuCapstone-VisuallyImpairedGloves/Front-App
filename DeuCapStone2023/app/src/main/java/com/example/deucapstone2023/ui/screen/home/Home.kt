package com.example.deucapstone2023.ui.screen.home

import android.content.Intent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import com.example.deucapstone2023.R
import com.example.deucapstone2023.ui.component.DefaultLayout
import com.example.deucapstone2023.ui.screen.home.component.HomeAppBar
import com.example.deucapstone2023.ui.screen.home.search.CommonRecognitionListener
import com.example.deucapstone2023.ui.screen.home.search.SearchUiState
import com.example.deucapstone2023.ui.screen.home.search.UserLocation
import com.example.deucapstone2023.ui.screen.home.search.state.POIState
import com.example.deucapstone2023.ui.service.SpeechService
import com.example.deucapstone2023.ui.theme.DeuCapStone2023Theme
import com.example.deucapstone2023.utils.addFocusCleaner
import com.skt.tmap.TMapData
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.overlay.TMapPolyLine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun Home(
    uiState: SearchUiState,
    tMapView: TMapView,
    title: String,
    userLocation: UserLocation,
    onTitleChanged: (String) -> Unit,
    onNavigateToNaviScreen: () -> Unit,
    startListening: () -> Unit,
    checkIsSpeaking: suspend () -> Unit,
    voiceOutput: (String) -> Unit,
    makeMarker: (POIState) -> TMapMarkerItem,
    getRoutePedestrian: (String, String, Double, Double) -> Unit,
    setSpeechRecognizerListener: (CommonRecognitionListener) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = uiState) {
        when (uiState) {
            is SearchUiState.Loading -> {}
            is SearchUiState.RouteList -> {}
            is SearchUiState.POIList -> {

                if (uiState.poiList.isEmpty()) {
                    voiceOutput("해당 도착지의 정보를 조회할 수 없어요. 다시 말씀해 주시겠어요?")
                } else {
                    var repeatFlag = true
                    var callbackCompleteFlag = true

                    uiState.poiList.take(3).onEachIndexed poiList@{ index, poi ->
                        while (!callbackCompleteFlag) {
                            delay(50)
                        }
                        if (!repeatFlag)
                            return@poiList

                        voiceOutput("도착지 ${poi.name} 의 주소는 ${poi.address} 입니다. 해당 도착지가 맞나요?")
                        callbackCompleteFlag = false
                        checkIsSpeaking()

                        setSpeechRecognizerListener(CommonRecognitionListener(
                            doOnResult = { results ->
                                val userSpeech2 =
                                    results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                                if (userSpeech2 != null) {
                                    if (userSpeech2[0].contains(Regex("^(?=.*(?:네|예|맞아|응)).+\$"))) {
                                        // poi로 경로설정
                                        repeatFlag = false

                                        coroutineScope.launch {
                                            val pedestrianRoute =
                                                withContext(Dispatchers.IO) {
                                                    TMapData().findPathDataWithType(
                                                        TMapData.TMapPathType.PEDESTRIAN_PATH,
                                                        TMapPoint(
                                                            userLocation.latitude,
                                                            userLocation.longitude
                                                        ),
                                                        TMapPoint(
                                                            poi.latitude,
                                                            poi.longitude
                                                        )
                                                    )
                                                }
                                            tMapView.apply {
                                                addTMapMarkerItem(makeMarker(poi))
                                                removeAllTMapPolyLine()
                                                addTMapPolyLine(pedestrianRoute.apply {
                                                    setID("pedestrianRoute")
                                                })
                                            }
                                        }

                                        getRoutePedestrian(
                                            context.getString(R.string.T_Map_key),
                                            poi.id.toString(),
                                            poi.latitude,
                                            poi.longitude
                                        )
                                    } else if (userSpeech2[0].contains(Regex("^(?=.*(?:아니|틀렸)).+\$"))) {
                                        if (index == 3) {
                                            voiceOutput("제가 잘못 이해했어요. 다시 말씀해 주시겠어요?")
                                        }
                                    }
                                    callbackCompleteFlag = true
                                }
                            }
                        ))
                        startListening()
                    }
                }
                context.startService(
                    Intent(
                        context,
                        SpeechService::class.java
                    )
                )
            }
        }
    }

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
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
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
            uiState = SearchUiState.Loading,
            tMapView = TMapView(context),
            title = "",
            userLocation = UserLocation.getInitValues(),
            onTitleChanged = {},
            onNavigateToNaviScreen = {},
            startListening = {},
            checkIsSpeaking = {},
            voiceOutput = {},
            makeMarker = { TMapMarkerItem() },
            getRoutePedestrian = { _, _, _, _ -> },
            setSpeechRecognizerListener = {}
        )
    }
}