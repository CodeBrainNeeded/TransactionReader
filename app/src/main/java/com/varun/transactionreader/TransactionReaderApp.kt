package com.varun.transactionreader

import android.app.Application
import com.varun.transactionreader.tts.TtsManager

class TransactionReaderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        TtsManager.initialize(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        TtsManager.shutdown()
    }
}
