package com.example.deucapstone2023.ui.navigation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.FrameLayout
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.deucapstone2023.R
import com.example.deucapstone2023.ui.navigation.NavigationGraph
import com.example.deucapstone2023.ui.theme.DeuCapStone2023Theme
import com.skt.tmap.TMapView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var tMapView: TMapView
    private lateinit var locationManager: LocationManager
    private lateinit var myLocationListener: LocationListener
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var textToSpeech: TextToSpeech

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if(permissions.any { permission -> permission.value.not() }) {
            Toast.makeText(this,"권한 동의가 필요합니다.",Toast.LENGTH_LONG).show()
            finish()
        }
        else {
            permissions.onEach { permission ->
                when(permission.key) {
                    Manifest.permission.RECORD_AUDIO -> {
                        speechRecognizer.startListening(
                            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this@MainActivity.packageName)
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
                            }
                        )
                    }
                    Manifest.permission.ACCESS_FINE_LOCATION -> {
                        setLocationListener()
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
        getMyLocation()
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
                modifier = Modifier.padding(it)
            )
        }
    }

    private fun initState() {
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        myLocationListener = LocationListener { location ->
            tMapView.setCenterPoint(location.latitude, location.longitude)
            tMapView.zoomLevel = 15

            locationManager.removeUpdates(myLocationListener)
        }


        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this).apply {
            setRecognitionListener(object: RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    TODO("Not yet implemented")
                }

                override fun onBeginningOfSpeech() {
                    TODO("Not yet implemented")
                }

                override fun onRmsChanged(rmsdB: Float) {
                    TODO("Not yet implemented")
                }

                override fun onBufferReceived(buffer: ByteArray?) {
                    TODO("Not yet implemented")
                }

                override fun onEndOfSpeech() {
                    TODO("Not yet implemented")
                }

                override fun onError(error: Int) {
                    TODO("Not yet implemented")
                }

                override fun onResults(results: Bundle?) {
                    TODO("Not yet implemented")
                }

                override fun onPartialResults(partialResults: Bundle?) {
                    TODO("Not yet implemented")
                }

                override fun onEvent(eventType: Int, params: Bundle?) {
                    TODO("Not yet implemented")
                }

            })
        }

        textToSpeech = TextToSpeech(this) { status ->
            if(status != TextToSpeech.ERROR) {
                textToSpeech.apply {
                    language = Locale.KOREAN
                    setSpeechRate(1.0f)
                    setPitch(1.0f)
                }
            }
        }
    }

    private fun voiceOutput(message: String) {
        textToSpeech.speak(message,TextToSpeech.QUEUE_FLUSH,null,null)
    }

    private fun getMyLocation() {
        val isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (isGpsEnable) {
            locationPermissionRequest.launch(PERMISSIONS)
        }
    }

    @Suppress("MissingPermission")
    private fun setLocationListener() {
        val minTime: Long = 1500
        val minDistance = 100f

        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, myLocationListener
            )
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