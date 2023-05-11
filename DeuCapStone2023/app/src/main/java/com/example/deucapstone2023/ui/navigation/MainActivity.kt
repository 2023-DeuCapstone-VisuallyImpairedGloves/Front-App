package com.example.deucapstone2023.ui.navigation

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.deucapstone2023.R
import com.example.deucapstone2023.ui.base.CommonRecognitionListener
import com.example.deucapstone2023.ui.screen.search.state.Location
import com.example.deucapstone2023.ui.screen.search.state.RouteState
import com.example.deucapstone2023.ui.screen.temp.SearchEventFlow
import com.example.deucapstone2023.ui.screen.temp.SearchViewModel
import com.example.deucapstone2023.ui.service.SpeechService
import com.example.deucapstone2023.ui.theme.DeuCapStone2023Theme
import com.skt.tmap.TMapGpsManager
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val searchViewModel: SearchViewModel by viewModels()

    private val tMapView: TMapView by lazy { TMapView(this).apply { setSKTMapApiKey(getString(R.string.T_Map_key)) } }
    lateinit var tMapGpsManager: TMapGpsManager
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var textToSpeech: TextToSpeech

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
        lifecycleScope.launch {
            requireLaunchingPermission()
            initState()
        }
        setContent {
            DeuCapStone2023Theme {
                this.Content()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        val place = intent?.getStringExtra("userMessage")
        Log.d("tests", "검색한 주소명: $place")
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
            delay(50)
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

    private fun setSpeechRecognizerListener(
        listener: CommonRecognitionListener
    ) {
        speechRecognizer.setRecognitionListener(listener)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Content(
        bottomNavigationViewModel: NavigationViewModel = hiltViewModel()
    ) {
        val bottomState by bottomNavigationViewModel.bottomBarState.collectAsStateWithLifecycle()
        val navController = rememberNavController()
        Scaffold(
            bottomBar = {
                if (bottomState)
                    BottomNavigationGraph(navController = navController)
            }
        ) {
            Box(modifier = Modifier.padding(it)) {
                NavigationGraph(
                    tMapView = tMapView,
                    searchViewModel = searchViewModel,
                    navController = navController,
                    startListening = { startListening() },
                    checkIsSpeaking = { checkIsSpeaking() },
                    voiceOutput = { message -> voiceOutput(message) },
                    setSpeechRecognizerListener = { listener -> setSpeechRecognizerListener(listener) }
                )
            }
        }
    }

    private suspend fun requireLaunchingPermission() {
        locationPermissionRequest.launch(PERMISSIONS)
    }

    private fun initState() {
        tMapGpsManager = TMapGpsManager(this).apply {
            minTime = 8000
            minDistance = 5F
            provider = TMapGpsManager.PROVIDER_GPS
            setOnLocationChangeListener { location ->
                searchViewModel::setUserLocation.invoke(location.latitude, location.longitude)
                tMapView.apply {
                    setCenterPoint(location.latitude, location.longitude)
                    zoomLevel = 15
                    if (getMarkerItemFromId("UserPosition") != null)
                        removeTMapMarkerItem("UserPosition")
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
            }
        }
        /*lifecycleScope.launch {
            var searchUiState = Location.getInitValues()
            var flag2 = 0
            var flag = false
            launch {
                searchViewModel.searchUiState.collect { state ->
                    searchUiState = state.location
                    flag2 = state.routeIndex
                    if(state.location.latitude != 0.0)
                        tMapView.apply {
                            if (getMarkerItemFromId("UserPosition") != null)
                                removeTMapMarkerItem("UserPosition")
                            addTMapMarkerItem(TMapMarkerItem().apply {
                                tMapPoint = TMapPoint(state.location.latitude, state.location.longitude)
                                icon = BitmapFactory.decodeResource(
                                    resources,
                                    R.drawable.ic_pin_red_a_midium
                                )
                                id = "UserPosition"
                                name = "UserPosition"
                            })
                        }
                    if(state.location.latitude != 0.0)
                        flag = true
                }
            }
            launch {
                while(true) {
                    while (flag) {
                        if(flag2 == 0)
                            searchViewModel::setUserLocation.invoke(searchUiState.latitude +0.00005000000000 , searchUiState.longitude - 0.00000500000000 )
                        else
                            searchViewModel::setUserLocation.invoke(searchUiState.latitude, searchUiState.longitude - 0.00003000000000 )
                        delay(8000)
                    }
                    delay(1000)
                }
            }

        }*/
        tMapGpsManager.openGps()
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
        speechRecognizer.apply {
            cancel()
            destroy()
        }
        textToSpeech.apply {
            stop()
            shutdown()
        }
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