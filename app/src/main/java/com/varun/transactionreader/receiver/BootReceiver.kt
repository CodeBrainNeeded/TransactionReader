package com.varun.transactionreader.receiver

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import androidx.core.content.ContextCompat
import com.varun.transactionreader.data.SettingsRepository
import com.varun.transactionreader.service.ListenerForegroundService
import com.varun.transactionreader.service.UpiNotificationListenerService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action ?: return
        if (action != Intent.ACTION_BOOT_COMPLETED && action != Intent.ACTION_LOCKED_BOOT_COMPLETED) {
            return
        }

        val settingsRepository = SettingsRepository(context)
        if (settingsRepository.isForegroundModeEnabled()) {
            val foregroundIntent = Intent(context, ListenerForegroundService::class.java)
            ContextCompat.startForegroundService(context, foregroundIntent)
        }

        val component = ComponentName(context, UpiNotificationListenerService::class.java)
        NotificationListenerService.requestRebind(component)
    }
}
