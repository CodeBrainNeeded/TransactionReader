package com.speakupi.receiver

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import androidx.core.content.ContextCompat
import com.speakupi.service.ListenerForegroundService
import com.speakupi.service.UpiNotificationListenerService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action ?: return
        if (action != Intent.ACTION_BOOT_COMPLETED && action != Intent.ACTION_LOCKED_BOOT_COMPLETED) {
            return
        }

        val foregroundIntent = Intent(context, ListenerForegroundService::class.java)
        ContextCompat.startForegroundService(context, foregroundIntent)

        val component = ComponentName(context, UpiNotificationListenerService::class.java)
        NotificationListenerService.requestRebind(component)
    }
}
