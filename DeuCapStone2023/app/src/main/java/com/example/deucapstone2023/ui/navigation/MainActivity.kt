package com.example.deucapstone2023.ui.navigation

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.deucapstone2023.R
import com.example.deucapstone2023.ui.base.CommonRecognitionListener
import com.example.deucapstone2023.ui.screen.search.SearchEventFlow
import com.example.deucapstone2023.ui.screen.search.SearchViewModel
import com.example.deucapstone2023.ui.service.SpeechService
import com.example.deucapstone2023.ui.theme.DeuCapStone2023Theme
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale
import java.util.UUID

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var bluetoothManager: BluetoothManager
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothSocket: BluetoothSocket? = null

    override fun onNewIntent(intent: Intent?) {
        val place = intent?.getStringExtra("userMessage")
        Log.d("tests", "검색한 주소명: $place")
        searchViewModel.searchPlace(
            appKey = getString(R.string.T_Map_key),
            place ?: "당감댁"
        )

        super.onNewIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.any { permission -> permission.value.not() }) {
                Toast.makeText(this, "권한 동의가 필요합니다.", Toast.LENGTH_LONG).show()
                finish()
            } else {
                setUpBluetoothAdapter(
                    successOnSettingUp = {
                        voiceOutput("기기와 연결되었습니다.")
                    },
                    failOnSettingUp = {
                        voiceOutput("기기 연결에 실패했습니다.")
                    }
                )
                initState()
                setContent {
                    DeuCapStone2023Theme {
                        Content()
                    }
                }
            }
        }

        permissionLauncher.launch(PERMISSIONS)
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
                    searchViewModel = searchViewModel,
                    navController = navController,
                    setUpBluetooth = this@MainActivity::setUpBluetoothAdapter,
                    disableBluetooth = this@MainActivity::disableBluetooth,
                    startListening = { startListening() },
                    checkIsSpeaking = { checkIsSpeaking() },
                    voiceOutput = { message -> voiceOutput(message) },
                    setSpeechRecognizerListener = { listener -> setSpeechRecognizerListener(listener) }
                )
            }
        }
    }

    private fun initState() {
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

        bluetoothManager = this.getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter

        startService(Intent(this, SpeechService::class.java))
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

    @SuppressLint("MissingPermission")
    private fun setUpBluetoothAdapter(
        successOnSettingUp: () -> Unit,
        failOnSettingUp: () -> Unit
    ) {
        getPairedDevices(bluetoothAdapter, DEVICE_NAME)?.let { address ->
            if (bluetoothAdapter?.isDiscovering == true)
                bluetoothAdapter?.cancelDiscovery()
            val device = bluetoothAdapter?.getRemoteDevice(address)
            lifecycleScope.launch(Dispatchers.IO) {
                bluetoothSocket = device?.createRfcommSocketToServiceRecord(UUID)
                bluetoothSocket?.let {
                    try {
                        it.connect()
                        successOnSettingUp()
                    } catch (e: IOException) {
                        it.close()
                        failOnSettingUp()
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getPairedDevices(bluetoothAdapter: BluetoothAdapter?, deviceName: String): String? =
        bluetoothAdapter?.let {
            // 블루투스 활성화 상태라면
            if (it.isEnabled) {
                // 페어링된 기기 확인

                val pairedDevices: Set<BluetoothDevice> = it.bondedDevices
                // 페어링된 기기가 존재하는 경우
                if (pairedDevices.isNotEmpty()) {
                    pairedDevices.find { device -> device.name == deviceName }?.address
                } else {
                    null
                }
            } else {
                null
            }
        }

    @SuppressLint("MissingPermission")
    private fun disableBluetooth(
        successOnDisable: () -> Unit,
        failOnDisable: () -> Unit
    ) {
        bluetoothAdapter?.let {
            if (it.isEnabled) {
                bluetoothSocket?.close()
                successOnDisable()
            } else
                failOnDisable()
        } ?: failOnDisable()
    }

    override fun onDestroy() {
        if(::speechRecognizer.isInitialized)
            speechRecognizer.apply {
                cancel()
                destroy()
            }
        if(::textToSpeech.isInitialized)
            textToSpeech.apply {
                stop()
                shutdown()
            }
        super.onDestroy()
    }

    companion object {
        private val PERMISSIONS = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN
        )
        val UUID: UUID = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
        const val DEVICE_NAME = "ESP32CAM-CLASSIC-BT"
    }
}