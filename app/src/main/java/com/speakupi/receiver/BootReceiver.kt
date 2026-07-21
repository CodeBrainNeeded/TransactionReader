package com.speakupi.receiver

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.service.notification.NotificationListenerService
import com.speakupi.service.UpiNotificationListenerService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action ?: return
        if (action != Intent.ACTION_BOOT_COMPLETED && action != Intent.ACTION_LOCKED_BOOT_COMPLETED) {
            return
        }

        val component = ComponentName(context, UpiNotificationListenerService::class.java)
        NotificationListenerService.requestRebind(component)
    }
}
