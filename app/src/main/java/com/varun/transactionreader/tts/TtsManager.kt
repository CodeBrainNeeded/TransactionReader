package com.varun.transactionreader.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.varun.transactionreader.R
import java.util.Locale
import java.util.UUID

object TtsManager {
    private var tts: TextToSpeech? = null
    @Volatile
    private var isReady: Boolean = false

    fun initialize(context: Context) {
        if (tts != null) {
            return
        }

        val appContext = context.applicationContext
        tts = TextToSpeech(appContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale("en", "IN"))
                isReady = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
            } else {
                isReady = false
            }
        }
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) = Unit
            override fun onDone(utteranceId: String?) = Unit
            override fun onError(utteranceId: String?) {
                Log.w(TAG, "TTS failed for utteranceId=$utteranceId")
            }
        })
    }

    fun speakReceivedAmount(context: Context, amountText: String, customMessage: String? = null) {
        initialize(context)
        if (!isReady) {
            Log.w(TAG, "TTS requested before engine was ready")
            return
        }

        val text = context.getString(R.string.speech_template, amountText)
        tts?.speak(text, TextToSpeech.QUEUE_ADD, null, UUID.randomUUID().toString())

        val extraMessage = customMessage?.trim().orEmpty()
        if (extraMessage.isNotEmpty()) {
            tts?.speak(extraMessage, TextToSpeech.QUEUE_ADD, null, UUID.randomUUID().toString())
        }
    }

    fun speakTestLine(context: Context) {
        initialize(context)
        if (!isReady) {
            return
        }
        val utteranceId = UUID.randomUUID().toString()
        tts?.speak("Transaction Reader is ready", TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isReady = false
    }

    private const val TAG = "TtsManager"
}
