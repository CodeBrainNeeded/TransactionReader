package com.varun.transactionreader.util

import android.content.Context
import android.provider.Settings

object NotificationAccessUtils {
    fun isNotificationAccessEnabled(context: Context): Boolean {
        val packageName = context.packageName
        val flat = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
            ?: return false
        return flat.contains(packageName)
    }
}
