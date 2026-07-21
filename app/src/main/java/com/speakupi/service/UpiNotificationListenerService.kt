package com.speakupi.service

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.content.ContextCompat
import com.speakupi.data.SettingsRepository
import com.speakupi.parser.NotificationParser
import com.speakupi.tts.TtsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

class UpiNotificationListenerService : NotificationListenerService() {
    private val workerScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private lateinit var settingsRepository: SettingsRepository
    private val recentlyAnnounced = ConcurrentHashMap<Int, Long>()

    override fun onCreate() {
        super.onCreate()
        settingsRepository = SettingsRepository(this)
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        TtsManager.initialize(this)
        if (!hasRequestedReliabilityServiceStart) {
            ContextCompat.startForegroundService(this, Intent(this, ListenerForegroundService::class.java))
            hasRequestedReliabilityServiceStart = true
        }
        Log.i(TAG, "Notification listener connected")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn == null) return

        workerScope.launch {
            handleNotification(sbn)
        }
    }

    private fun handleNotification(sbn: StatusBarNotification) {
        val packageName = sbn.packageName ?: return
        if (!settingsRepository.isPackageEnabled(packageName)) {
            return
        }
        if (!settingsRepository.isReceivedAnnouncementsEnabled()) {
            return
        }

        val extras = sbn.notification.extras
        val title = extras?.getCharSequence("android.title")?.toString()
        val body = extras?.getCharSequence("android.text")?.toString()
            ?: extras?.getCharSequence("android.bigText")?.toString()

        val parsed = NotificationParser.parse(packageName, title, body) ?: return
        if (isDuplicate(sbn, parsed.amount.toPlainString())) {
            return
        }

        TtsManager.speakReceivedAmount(
            this,
            parsed.amount.stripTrailingZeros().toPlainString(),
            settingsRepository.getCustomAnnouncementMessage()
        )
    }

    private fun isDuplicate(sbn: StatusBarNotification, amount: String): Boolean {
        val fingerprint = "${sbn.key}_$amount".hashCode()
        val now = System.currentTimeMillis()
        val seenAt = recentlyAnnounced[fingerprint]

        if (seenAt != null && now - seenAt < DEDUP_TTL_MS) {
            return true
        }

        recentlyAnnounced[fingerprint] = now
        if (recentlyAnnounced.size > MAX_DEDUP_ENTRIES) {
            cleanupDedupCache(now)
        }
        return false
    }

    private fun cleanupDedupCache(now: Long) {
        val iterator = recentlyAnnounced.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (now - entry.value > DEDUP_TTL_MS) {
                iterator.remove()
            }
        }
    }

    override fun onDestroy() {
        workerScope.cancel()
        super.onDestroy()
    }

    companion object {
        @Volatile
        private var hasRequestedReliabilityServiceStart: Boolean = false
        private const val TAG = "UpiNotifListener"
        private const val DEDUP_TTL_MS = 30_000L
        private const val MAX_DEDUP_ENTRIES = 256
    }
}
