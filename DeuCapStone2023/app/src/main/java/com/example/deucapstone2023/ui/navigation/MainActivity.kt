package com.example.deucapstone2023.ui.navigation

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.deucapstone2023.R
import com.example.deucapstone2023.ui.screen.home.search.SearchViewModel
import com.example.deucapstone2023.ui.service.SpeechService
import com.example.deucapstone2023.ui.theme.DeuCapStone2023Theme
import com.skt.tmap.TMapData
import com.skt.tmap.TMapGpsManager
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.overlay.TMapMarkerItem2
import com.skt.tmap.overlay.TMapPolyLine
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val searchViewModel: SearchViewModel by viewModels()

    @Inject
    lateinit var tMapView: TMapView
    lateinit var tMapGpsManager: TMapGpsManager
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var pedestrianRoute: TMapPolyLine

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.any { permission -> permission.value.not() }) {
            Toast.makeText(this, "권한 동의가 필요합니다.", Toast.LENGTH_LONG).show()
            finish()
        } else {
            permissions.onEach { permission ->
                when (permission.key) {
                    Manifest.permission.RECORD_AUDIO -> {

                    }

                    Manifest.permission.ACCESS_FINE_LOCATION -> {

                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DeuCapStone2023Theme {
                this.Content()
            }
        }
        initState()
        locationPermissionRequest.launch(PERMISSIONS)
        tMapGpsManager.openGps()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.searchUiState.collect { uiState ->
                    uiState.poiList.forEach { poi ->
                        Log.d(
                            "test",
                            "이름: ${poi.name}, 주소: ${poi.address}, 거리: ${poi.distance}, 업종명: ${poi.biz}"
                        )
                        searchViewModel.getPoiMarkers().forEach { marker ->
                            tMapView.addTMapMarkerItem(marker)
                        }
                    }

                    uiState.poiList.take(1).onEach { poi ->
                        voiceOutput("도착지 ${poi.name} 의 주소는 ${poi.address} 입니다. 해당 도착지가 맞나요?")
                        tMapView.addTMapMarkerItem(searchViewModel.makeMarker(poi))
                        checkIsSpeaking()
                        speechRecognizer.setRecognitionListener(object : RecognitionListener {
                            override fun onReadyForSpeech(params: Bundle?) {}
                            override fun onBeginningOfSpeech() {}
                            override fun onRmsChanged(rmsdB: Float) {}
                            override fun onBufferReceived(buffer: ByteArray?) {}
                            override fun onEndOfSpeech() {}
                            override fun onError(error: Int) {}

                            override fun onResults(results: Bundle?) {
                                val userSpeech2 =
                                    results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                                if (userSpeech2 != null) {
                                    if (userSpeech2[0].contains(Regex("^(?=.*(?:네|예|맞아|응)).+\$"))) {
                                        // poi로 경로설정
                                        try {
                                            val pedestrianRoute = TMapData().findPathDataWithType(
                                                TMapData.TMapPathType.PEDESTRIAN_PATH,
                                                TMapPoint(uiState.latitude, uiState.longitude),
                                                TMapPoint(poi.latitude, poi.longitude)
                                            ).apply {
                                                setID("pedestrianRoute")
                                            }

                                            tMapView.addTMapPolyLine(pedestrianRoute)
                                        }
                                        catch (e: Exception) {
                                            Log.d("tests","error : ${e.message} occurred in ${e.printStackTrace()}")
                                            Log.d("tests","slat: ${uiState.latitude} slon: ${uiState.longitude} dlat: ${poi.latitude} dlon: ${poi.longitude}")
                                        }

                                        searchViewModel.getRoutePedestrian(
                                            appKey = getString(R.string.T_Map_key),
                                            destinationPoiId = poi.id.toString(),
                                            destinationLatitude = poi.latitude,
                                            destinationLongitude = poi.longitude
                                        )
                                        startService(
                                            Intent(
                                                this@MainActivity,
                                                SpeechService::class.java
                                            )
                                        )
                                    } else {
                                        voiceOutput("요청하신 경로는 없는 경로에요. 다시 말씀해주시겠어요?")
                                        tMapView.removeTMapMarkerItem(poi.name)
                                        startService(
                                            Intent(
                                                this@MainActivity,
                                                SpeechService::class.java
                                            )
                                        )
                                    }
                                }
                            }

                            override fun onPartialResults(partialResults: Bundle?) {}
                            override fun onEvent(eventType: Int, params: Bundle?) {}
                        })
                        startListening()
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        val place = intent?.getStringExtra("userMessage")

        searchViewModel.searchPlace(
            appKey = getString(R.string.T_Map_key),
            place ?: "당감댁"
        )

        super.onNewIntent(intent)
    }

    private fun voiceOutput(message: String) {
        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
        Log.d("tests", "msg: $message")
    }

    private suspend fun checkIsSpeaking() {
        while (true) {
            if (!textToSpeech.isSpeaking)
                break
            delay(100)
        }
    }

    private fun startListening() {
        speechRecognizer.startListening(
            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_CALLING_PACKAGE,
                    packageName
                )
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                putExtra(
                    RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
                    1000
                )
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
            }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Content(
        bottomNavigationViewModel: NavigationViewModel = hiltViewModel()
    ) {
        val bottomState by bottomNavigationViewModel.bottomBarState.collectAsStateWithLifecycle()
        Scaffold(
            bottomBar = {
                if (bottomState)
                    BottomNavigationGraph(navController = rememberNavController())
            }
        ) {
            NavigationGraph(
                navController = rememberNavController(),
                tMapView = tMapView,
                modifier = Modifier.padding(it),
                searchViewModel = searchViewModel
            )
        }
    }

    private fun initState() {
        tMapGpsManager = TMapGpsManager(this).apply {
            minTime = 1000
            minDistance = 5F
            provider = TMapGpsManager.PROVIDER_NETWORK
            setOnLocationChangeListener { location ->
                searchViewModel::setUserLocation.invoke(location.latitude, location.longitude)
                tMapView.apply {
                    setCenterPoint(location.latitude, location.longitude)
                    zoomLevel = 15
                    if (getMarkerItem2FromID("UserPosition") != null)
                        removeTMapMarkerItem2("UserPosition")
                    addTMapMarkerItem(TMapMarkerItem().apply {
                        tMapPoint = TMapPoint(location.latitude, location.longitude)
                        icon = BitmapFactory.decodeResource(
                            resources,
                            R.drawable.ic_pin_red_a_midium
                        )
                        id = "UserPosition"
                        name = "UserPosition"
                    })
                }
                if (tMapView.getPolyLineFromId("pedestrianRoute") != null) {


                }
            }
        }
        startService(Intent(this, SpeechService::class.java))

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        textToSpeech = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech.apply {
                    language = Locale.KOREAN
                    setSpeechRate(1.0f)
                    setPitch(1.0f)
                }
            }
        }
    }

    override fun onDestroy() {
        tMapView.onDestroy()
        tMapGpsManager.closeGps()
        super.onDestroy()
    }

    companion object {
        val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO
        )
    }
}