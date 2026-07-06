package com.varun.transactionreader.data

import android.content.Context
import android.content.SharedPreferences

class SettingsRepository(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isAppEnabled(appId: String): Boolean {
        return preferences.getBoolean(keyForApp(appId), appId == SupportedUpiApps.BHIM)
    }

    fun setAppEnabled(appId: String, enabled: Boolean) {
        preferences.edit().putBoolean(keyForApp(appId), enabled).apply()
    }

    fun isPackageEnabled(packageName: String): Boolean {
        val appId = SupportedUpiApps.idFromPackageName(packageName) ?: return false
        return isAppEnabled(appId)
    }

    fun isForegroundModeEnabled(): Boolean {
        return preferences.getBoolean(KEY_FOREGROUND_MODE, false)
    }

    fun setForegroundModeEnabled(enabled: Boolean) {
        preferences.edit().putBoolean(KEY_FOREGROUND_MODE, enabled).apply()
    }

    fun isReceivedAnnouncementsEnabled(): Boolean {
        return preferences.getBoolean(KEY_RECEIVED_ANNOUNCEMENTS_ENABLED, true)
    }

    fun setReceivedAnnouncementsEnabled(enabled: Boolean) {
        preferences.edit().putBoolean(KEY_RECEIVED_ANNOUNCEMENTS_ENABLED, enabled).apply()
    }

    fun getCustomAnnouncementMessage(): String {
        return preferences.getString(KEY_CUSTOM_ANNOUNCEMENT_MESSAGE, "") ?: ""
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

    private fun keyForApp(appId: String): String = "upi_app_enabled_$appId"

    companion object {
        private const val PREFS_NAME = "transaction_reader_prefs"
        private const val KEY_FOREGROUND_MODE = "foreground_mode_enabled"
        private const val KEY_RECEIVED_ANNOUNCEMENTS_ENABLED = "received_announcements_enabled"
        private const val KEY_CUSTOM_ANNOUNCEMENT_MESSAGE = "custom_announcement_message"
        private const val KEY_POST_NOTIFICATIONS_REQUESTED = "post_notifications_requested"
    }
}
