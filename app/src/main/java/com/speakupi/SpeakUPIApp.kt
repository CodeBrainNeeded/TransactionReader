package com.speakupi

import android.app.Application
import com.speakupi.tts.TtsManager

class SpeakUPIApp : Application() {
    override fun onCreate() {
        super.onCreate()
        TtsManager.initialize(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        TtsManager.shutdown()
    }
}