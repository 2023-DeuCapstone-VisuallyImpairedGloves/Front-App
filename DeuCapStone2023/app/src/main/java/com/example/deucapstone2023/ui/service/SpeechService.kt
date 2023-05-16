package com.example.deucapstone2023.ui.service

import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.deucapstone2023.ui.base.CommonRecognitionListener
import com.example.deucapstone2023.ui.navigation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class SpeechService : LifecycleService() {

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var textToSpeech: TextToSpeech
    private var enabled = false

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

    private val speechRecognizerListener = CommonRecognitionListener(
        doOnResult = { results ->
            lifecycleScope.launch {
                val userSpeech =
                    results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                if (userSpeech?.get(0) == null) {

                    voiceOutput("다시 말씀해 주실래요?")
                    checkIsSpeaking()
                } else if (!enabled) {
                    if (userSpeech[0] == HOT_WORDS) {

                        voiceOutput("네 부르셨나요?")
                        checkIsSpeaking()
                        enabled = true
                    }
                } else {
                    when {
                        userSpeech[0].contains("안내") -> {
                            startActivity(Intent(this@SpeechService, MainActivity::class.java).apply {
                                flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                putExtra("userMessage", userSpeech[0].split(Regex("로|으로"))[0])
                            })
                            stopSelf()
                        }

                        else -> {
                            enabled = false
                        }
                    }
                }
                startListening()
            }
        },
        doOnError = {
            startListening()
        }
    )

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
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                putExtra(
                    RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
                    1000
                )
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
            }
        )
    }

    companion object {
        const val HOT_WORDS = "비서"
    }
}