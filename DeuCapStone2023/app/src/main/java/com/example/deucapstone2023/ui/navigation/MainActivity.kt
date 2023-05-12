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
import com.example.deucapstone2023.ui.base.CommonRecognitionListener
import com.example.deucapstone2023.ui.screen.search.SearchViewModel
import com.example.deucapstone2023.ui.service.SpeechService
import com.example.deucapstone2023.ui.theme.DeuCapStone2023Theme
import com.skt.tmap.TMapGpsManager
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.overlay.TMapMarkerItem2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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
                        speechRecognizer.startListening(
                            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                putExtra(
                                    RecognizerIntent.EXTRA_CALLING_PACKAGE,
                                    this@MainActivity.packageName
                                )
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
                            }
                        )
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
                }
            }
        }
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

    private fun requireLaunchingPermission() {
        locationPermissionRequest.launch(PERMISSIONS)
    }

    private fun initState() {
        tMapGpsManager = TMapGpsManager(this).apply {
            minTime = 8000
            minDistance = 4.5F
            provider = TMapGpsManager.PROVIDER_GPS
            setOnLocationChangeListener { location ->
                tMapView.apply {
                    setCenterPoint(location.latitude, location.longitude)
                    zoomLevel = 18
                    if (getMarkerItemFromId("UserPosition") != null)
                        removeTMapMarkerItem("UserPosition")
                    addTMapMarkerItem(TMapMarkerItem().apply {
                        tMapPoint = TMapPoint(location.latitude, location.longitude)
                        icon = BitmapFactory.decodeResource(
                            resources,
                            R.drawable.ic_pin_red_a_midium
                        )
                        id = "내 위치"
                        name = "내 위치"
                    })
                }
            }
        }

        tMapGpsManager.openGps()
        startService(Intent(this, SpeechService::class.java))

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

    private fun voiceOutput(message: String) {
        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
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