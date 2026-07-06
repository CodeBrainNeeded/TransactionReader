package com.varun.transactionreader.data

import android.content.Context
import android.content.SharedPreferences

class SettingsRepository(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isPackageEnabled(packageName: String): Boolean {
        return SupportedUpiApps.idFromPackageName(packageName) != null
    }

    fun isReceivedAnnouncementsEnabled(): Boolean {
        return preferences.getBoolean(KEY_RECEIVED_ANNOUNCEMENTS_ENABLED, true)
    }

    fun setReceivedAnnouncementsEnabled(enabled: Boolean) {
        preferences.edit().putBoolean(KEY_RECEIVED_ANNOUNCEMENTS_ENABLED, enabled).apply()
    }

    fun getCustomAnnouncementMessage(): String {
        return preferences.getString(KEY_CUSTOM_ANNOUNCEMENT_MESSAGE, "Thank you!") ?: "Thank you!"
    }

    fun setCustomAnnouncementMessage(message: String) {
        preferences.edit().putString(KEY_CUSTOM_ANNOUNCEMENT_MESSAGE, message).apply()
    }

    fun hasRequestedPostNotificationsPermission(): Boolean {
        return preferences.getBoolean(KEY_POST_NOTIFICATIONS_REQUESTED, false)
    }

    fun setPostNotificationsPermissionRequested(requested: Boolean) {
        preferences.edit().putBoolean(KEY_POST_NOTIFICATIONS_REQUESTED, requested).apply()
    }

    companion object {
        private const val PREFS_NAME = "transaction_reader_prefs"
        private const val KEY_RECEIVED_ANNOUNCEMENTS_ENABLED = "received_announcements_enabled"
        private const val KEY_CUSTOM_ANNOUNCEMENT_MESSAGE = "custom_announcement_message"
        private const val KEY_POST_NOTIFICATIONS_REQUESTED = "post_notifications_requested"
    }
}
