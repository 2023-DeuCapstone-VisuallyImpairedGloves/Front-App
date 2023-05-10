package com.example.deucapstone2023.ui.base

import android.os.Bundle
import android.speech.RecognitionListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class CommonRecognitionListener(val doOnResult:(Bundle?) -> Unit) : RecognitionListener {
    private val coroutineContext = Dispatchers.IO
    val coroutineScope = CoroutineScope(coroutineContext)
    override fun onReadyForSpeech(params: Bundle?) {}

    override fun onBeginningOfSpeech() {}

    override fun onRmsChanged(rmsdB: Float) {}

    override fun onBufferReceived(buffer: ByteArray?) {}

    override fun onEndOfSpeech() {}

    override fun onError(error: Int) {}

    override fun onResults(results: Bundle?) { doOnResult(results) }

    override fun onPartialResults(partialResults: Bundle?) {}

    override fun onEvent(eventType: Int, params: Bundle?) {}
}