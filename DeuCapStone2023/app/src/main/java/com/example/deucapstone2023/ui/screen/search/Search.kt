package com.example.deucapstone2023.ui.screen.search

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.withCreated
import com.example.deucapstone2023.R
import com.example.deucapstone2023.ui.base.CommonRecognitionListener
import com.example.deucapstone2023.ui.component.DefaultLayout
import com.example.deucapstone2023.ui.component.SnackBarLayout
import com.example.deucapstone2023.ui.screen.search.component.HomeAppBar
import com.example.deucapstone2023.ui.screen.search.state.POIState
import com.example.deucapstone2023.ui.service.SpeechService
import com.example.deucapstone2023.ui.theme.DeuCapStone2023Theme
import com.example.deucapstone2023.ui.theme.blue
import com.example.deucapstone2023.utils.addFocusCleaner
import com.skt.tmap.TMapGpsManager
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.overlay.TMapPolyLine
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    searchViewModel: SearchViewModel,
    homeViewModel: HomeViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    context: Context = LocalContext.current,
    startListening: () -> Unit,
    checkIsSpeaking: suspend () -> Unit,
    voiceOutput: (String) -> Unit,
    setSpeechRecognizerListener: (CommonRecognitionListener) -> Unit
) {
    val permissionState = rememberSaveable {
        mutableStateOf(false)
    }

    val locationPermissionRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.any { permission -> !permission.value }) {
            Toast.makeText(context, "권한 동의가 필요합니다.", Toast.LENGTH_LONG).show()
            (context as ComponentActivity).finish()
        }
        else {
            permissionState.value = true
        }
    }

    val tMapView = remember {
        TMapView(context as ComponentActivity).apply {
            setSKTMapApiKey(context.getString(R.string.T_Map_key))
            setOnMapReadyListener {
                setUserPosition(tMapView = this, lat = 35.15130665819491, lon = 129.02657807928898)
            }
        }
    }

    val tMapGpsManager: TMapGpsManager = TMapGpsManager(context).apply {
        minTime = 7000
        minDistance = 4.5F
        provider = TMapGpsManager.PROVIDER_GPS
        setOnLocationChangeListener { location ->
            searchViewModel::setUserLocation.invoke(location.latitude, location.longitude)
            setUserPosition(
                tMapView = tMapView,
                lat = location.latitude,
                lon = location.longitude,
                zoomLevel = 18
            )
        }
    }

    if(permissionState.value) {
        tMapGpsManager.openGps()
    }

    val searchEventFlow by searchViewModel.searchEventFlow.collectAsStateWithLifecycle(
        initialValue = SearchEventFlow.Loading,
        lifecycleOwner = LocalLifecycleOwner.current,
        minActiveState = Lifecycle.State.STARTED
    )
    val searchUiState by searchViewModel.searchUiState.collectAsStateWithLifecycle()
    val homeUiState by homeViewModel.homeUiState.collectAsStateWithLifecycle()

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                locationPermissionRequest.launch(PERMISSIONS)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            tMapGpsManager.closeGps()
            tMapView.onDestroy()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    HomeScreen(
        searchEventFlow = searchEventFlow,
        searchUiState = searchUiState,
        tMapView = tMapView,
        startListening = startListening,
        checkIsSpeaking = checkIsSpeaking,
        voiceOutput = voiceOutput,
        makeMarker = searchViewModel::makeMarker,
        getRoutePedestrian = searchViewModel::getRoutePedestrian,
        setSpeechRecognizerListener = setSpeechRecognizerListener,
        setDestinationInfo = searchViewModel::setDestinationInfo,
        navigateRouteOnMap = searchViewModel::navigateRouteOnMap,
        title = homeUiState.title,
        onTitleChanged = homeViewModel::setTitle,
        onNavigateToNaviScreen = {
            searchViewModel::searchPlaceOnTyping.invoke(
                context.getString(R.string.T_Map_key),
                homeUiState.title
            )
        }
    )

}

@Composable
private fun HomeScreen(
    searchEventFlow: SearchEventFlow,
    searchUiState: SearchUiState,
    tMapView: TMapView,
    title: String,
    onTitleChanged: (String) -> Unit,
    onNavigateToNaviScreen: () -> Unit,
    startListening: () -> Unit,
    checkIsSpeaking: suspend () -> Unit,
    voiceOutput: (String) -> Unit,
    makeMarker: (POIState) -> TMapMarkerItem,
    getRoutePedestrian: (String) -> Unit,
    setSpeechRecognizerListener: (CommonRecognitionListener) -> Unit,
    setDestinationInfo: (POIState) -> Unit,
    navigateRouteOnMap: ((String) -> Unit) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(key1 = searchUiState.location) {
        if (searchUiState.routeList.isNotEmpty())
            navigateRouteOnMap { message ->
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
                    removeTMapPolyLine("pedestrianRoute")
                    addTMapPolyLine(polyLine)
                }
            }

            is SearchEventFlow.POIList -> {
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

                                        setDestinationInfo(poi)

                                        tMapView.apply {
                                            removeTMapMarkerItem(poi.name)
                                            addTMapMarkerItem(makeMarker(poi))
                                        }

                                        getRoutePedestrian(context.getString(R.string.T_Map_key))

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
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable { focusManager.clearFocus() }) {
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

private val PERMISSIONS = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

private fun setUserPosition(tMapView: TMapView, lat: Double, lon: Double, zoomLevel: Int = 15) {
    tMapView.apply {
        setCenterPoint(lat, lon)
        this.zoomLevel = zoomLevel

        if (getMarkerItemFromId("UserPosition") != null)
            removeTMapMarkerItem("UserPosition")
        addTMapMarkerItem(TMapMarkerItem().apply {
            tMapPoint = TMapPoint(lat, lon)
            icon = BitmapFactory.decodeResource(
                resources,
                R.drawable.ic_pin_red_a_midium
            )
            id = "UserPosition"
            name = "UserPosition"
        })
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
            getRoutePedestrian = {},
            setSpeechRecognizerListener = {},
            setDestinationInfo = {},
            navigateRouteOnMap = {},
            title = "",
            onTitleChanged = {},
            onNavigateToNaviScreen = {},
        )
    }
}