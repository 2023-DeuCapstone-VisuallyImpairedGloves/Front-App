package com.example.deucapstone2023.ui.screen.search

import android.content.Intent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.allViews
import com.example.deucapstone2023.R
import com.example.deucapstone2023.ui.component.DefaultLayout
import com.example.deucapstone2023.ui.base.CommonRecognitionListener
import com.example.deucapstone2023.ui.screen.temp.SearchEventFlow
import com.example.deucapstone2023.ui.screen.temp.SearchUiState
import com.example.deucapstone2023.ui.screen.search.state.POIState
import com.example.deucapstone2023.ui.screen.search.state.RouteState
import com.example.deucapstone2023.ui.service.SpeechService
import com.example.deucapstone2023.ui.theme.DeuCapStone2023Theme
import com.example.deucapstone2023.ui.theme.blue
import com.example.deucapstone2023.utils.addFocusCleaner
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.overlay.TMapPolyLine
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    searchEventFlow: SearchEventFlow,
    searchUiState: SearchUiState,
    tMapView: TMapView,
    startListening: () -> Unit,
    checkIsSpeaking: suspend () -> Unit,
    voiceOutput: (String) -> Unit,
    makeMarker: (POIState) -> TMapMarkerItem,
    getRoutePedestrian: (String, String, Double, Double) -> Unit,
    setSpeechRecognizerListener: (CommonRecognitionListener) -> Unit,
    navigateRouteOnMap: (List<RouteState>, (String) -> Unit) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(key1 = searchUiState.location) {
        if (searchEventFlow is SearchEventFlow.RouteList)
            navigateRouteOnMap(
                searchEventFlow.routeList,
            ) { message ->
                voiceOutput(message)
            }
    }

    LaunchedEffect(key1 = searchEventFlow) {
        when (searchEventFlow) {
            is SearchEventFlow.Loading -> {}
            is SearchEventFlow.RouteList -> {

                val polyLine = TMapPolyLine("pedestrianRoute", null).apply {
                    outLineColor = blue.toArgb()
                    lineColor = blue.toArgb()
                    outLineWidth = 2f
                    lineWidth = 2f
                }

                searchEventFlow.routeList.onEach { route ->
                    route.lineInfo.onEach { line ->
                        polyLine.addLinePoint(TMapPoint(line.latitude, line.longitude))
                    }
                }

                tMapView.apply {
                    addTMapPolyLine(polyLine)
                }
            }

            is SearchEventFlow.POIList -> {
                /*
                val poi = searchEventFlow.poiList.first()
                getRoutePedestrian(
                    context.getString(R.string.T_Map_key),
                    poi.id.toString(),
                    poi.latitude,
                    poi.longitude
                )
                tMapView.removeTMapPolyLine("pedestrianRoute")*/


                if (searchEventFlow.poiList.isEmpty()) {
                    voiceOutput("해당 도착지의 정보를 조회할 수 없어요. 다시 말씀해 주시겠어요?")
                } else {
                    var repeatFlag = true
                    var callbackCompleteFlag = true

                    searchEventFlow.poiList.take(3).onEachIndexed poiList@{ index, poi ->
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

                                        tMapView.apply {
                                            removeTMapMarkerItem(poi.name)
                                            addTMapMarkerItem(makeMarker(poi))
                                            removeTMapPolyLine("pedestrianRoute")
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
                        val parentView = tMapView.parent as? ViewGroup
                        parentView?.removeView(tMapView)
                        addView(tMapView)
                    }
                },
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
@Preview
private fun PreviewHomeScreen() {
    val context = LocalContext.current
    DeuCapStone2023Theme {
        HomeScreen(
            searchEventFlow = SearchEventFlow.Loading,
            searchUiState = SearchUiState.getInitValues(),
            tMapView = TMapView(context),
            startListening = {},
            checkIsSpeaking = {},
            voiceOutput = {},
            makeMarker = { TMapMarkerItem() },
            getRoutePedestrian = { _, _, _, _ -> },
            setSpeechRecognizerListener = {},
            navigateRouteOnMap = { _, _ -> }
        )
    }
}