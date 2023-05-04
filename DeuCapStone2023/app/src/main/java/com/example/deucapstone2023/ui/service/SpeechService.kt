package com.example.deucapstone2023.ui.service

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.deucapstone2023.R
import com.example.deucapstone2023.domain.usecase.POIUsecase
import com.example.deucapstone2023.ui.navigation.MainActivity
import com.example.deucapstone2023.ui.screen.home.search.SearchUiState
import com.example.deucapstone2023.ui.screen.home.search.state.toPOIModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class SpeechService : LifecycleService() {

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var textToSpeech: TextToSpeech
    private var enabled = MutableStateFlow(false)
    @Inject lateinit var poiUsecase: POIUsecase

    override fun onCreate() {
        super.onCreate()

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this).apply {
            setRecognitionListener(speechRecognizerListener)
        }

        textToSpeech = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech.apply {
                    language = Locale.KOREAN
                    setSpeechRate(1.0f)
                    setPitch(1.0f)
                }
            }
        }
        startListening()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (intent?.getBooleanExtra("status", false) == true)
            stopSelf()

        return START_STICKY
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

    private val speechRecognizerListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
        }

        override fun onBeginningOfSpeech() {

        }

        override fun onRmsChanged(rmsdB: Float) {

        }

        override fun onBufferReceived(buffer: ByteArray?) {

        }

        override fun onEndOfSpeech() {

        }

        override fun onError(error: Int) {
            startListening()
        }

        override fun onResults(results: Bundle?) {
            val userSpeech =
                results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

            if (userSpeech?.get(0) == null) {
                lifecycleScope.launch {
                    voiceOutput("다시 말씀해 주실래요?")
                    checkIsSpeaking()
                    startListening()
                }
            } else if (!enabled.value) {
                if (userSpeech[0] == HOT_WORDS) {
                    lifecycleScope.launch {
                        voiceOutput("네 부르셨나요?")
                        checkIsSpeaking()
                        enabled.update { true }
                        startListening()
                    }
                } else
                    startListening()
            } else if (enabled.value) {
                when {
                    userSpeech[0].contains("안내") -> {
                        startActivity(Intent(this@SpeechService,MainActivity::class.java).apply{
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            putExtra("userMessage",userSpeech[0].split("로")[0])
                        })
                        stopSelf()
                        // enabled.update { false }
                        // startListening()
                    }
                }
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {

        }

        override fun onEvent(eventType: Int, params: Bundle?) {

        }

    }

    private suspend fun checkIsSpeaking() {
        while (true) {
            if (!textToSpeech.isSpeaking)
                break
            delay(100)
        }
    }

    private fun voiceOutput(message: String) {
        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun startListening() {
        speechRecognizer.startListening(
            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_CALLING_PACKAGE,
                    packageName
                )
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1)
                putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 1000)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
            }
        )
    }

    companion object {
        const val HOT_WORDS = "비서"
    }
}