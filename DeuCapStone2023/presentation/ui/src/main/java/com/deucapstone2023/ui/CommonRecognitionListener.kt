package com.deucapstone2023.ui

import android.os.Bundle
import android.speech.RecognitionListener

class CommonRecognitionListener(val doOnResult:(Bundle?) -> Unit, val doOnError:(Int) -> Unit = {}) : RecognitionListener {
    override fun onReadyForSpeech(params: Bundle?) {}

    override fun onBeginningOfSpeech() {}

    override fun onRmsChanged(rmsdB: Float) {}

    override fun onBufferReceived(buffer: ByteArray?) {}

    override fun onEndOfSpeech() {}

    override fun onError(error: Int) { doOnError(error) }

    override fun onResults(results: Bundle?) { doOnResult(results) }

    override fun onPartialResults(partialResults: Bundle?) {}

    override fun onEvent(eventType: Int, params: Bundle?) {}
}