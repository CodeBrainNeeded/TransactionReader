package com.varun.transactionreader

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.varun.transactionreader.data.SettingsRepository
import com.varun.transactionreader.data.SupportedUpiApps
import com.varun.transactionreader.service.ListenerForegroundService
import com.varun.transactionreader.tts.TtsManager
import com.varun.transactionreader.util.NotificationAccessUtils

class MainActivity : AppCompatActivity() {
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var listenerStatusText: TextView
    private var skipNextPostNotificationsPrompt = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settingsRepository = SettingsRepository(this)
        listenerStatusText = findViewById(R.id.listenerStatusText)

        ContextCompat.startForegroundService(this, Intent(this, ListenerForegroundService::class.java))

        findViewById<com.google.android.material.button.MaterialButton>(R.id.testTtsButton).setOnClickListener {
            TtsManager.speakTestLine(this)
        }

        val receivedAnnouncementsSwitch = findViewById<Switch>(R.id.switchReceivedAnnouncements)
        receivedAnnouncementsSwitch.isChecked = settingsRepository.isReceivedAnnouncementsEnabled()
        receivedAnnouncementsSwitch.setOnCheckedChangeListener { _, isChecked ->
            settingsRepository.setReceivedAnnouncementsEnabled(isChecked)
        }

        val customMessageInput = findViewById<EditText>(R.id.customMessageInput)
        customMessageInput.setText(settingsRepository.getCustomAnnouncementMessage())
        customMessageInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                settingsRepository.setCustomAnnouncementMessage(s?.toString().orEmpty())
            }
        })

        bindAllowlistCheckboxes()
    }

    override fun onResume() {
        super.onResume()
        updateListenerStatus()
        if (maybeRequestPostNotificationsPermission()) {
            return
        }
        maybeOpenNotificationAccessSettings()
    }

    private fun updateListenerStatus() {
        val enabled = NotificationAccessUtils.isNotificationAccessEnabled(this)
        listenerStatusText.setText(if (enabled) R.string.status_enabled else R.string.status_disabled)
    }

    private fun maybeOpenNotificationAccessSettings() {
        if (NotificationAccessUtils.isNotificationAccessEnabled(this)) {
            return
        }

        startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
    }

    private fun maybeRequestPostNotificationsPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return false
        }

        val permissionState = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.POST_NOTIFICATIONS
        )
        if (permissionState == PackageManager.PERMISSION_GRANTED) {
            return false
        }

        val hasRequestedBefore = settingsRepository.hasRequestedPostNotificationsPermission()
        val canShowRuntimePrompt = !hasRequestedBefore ||
            shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)

        if (!canShowRuntimePrompt) {
            openAppNotificationSettings()
            return true
        }

        if (skipNextPostNotificationsPrompt) {
            skipNextPostNotificationsPrompt = false
            return true
        }

        skipNextPostNotificationsPrompt = true
        settingsRepository.setPostNotificationsPermissionRequested(true)
        requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), REQUEST_POST_NOTIFICATIONS)
        return true
    }

    private fun openAppNotificationSettings() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        }

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
            return
        }

        startActivity(
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
            }
        )
    }

    private fun bindAllowlistCheckboxes() {
        bindCheckbox(R.id.checkBhim, SupportedUpiApps.BHIM)
        bindCheckbox(R.id.checkGpay, SupportedUpiApps.GPAY)
        bindCheckbox(R.id.checkPhonePe, SupportedUpiApps.PHONEPE)
        bindCheckbox(R.id.checkPaytm, SupportedUpiApps.PAYTM)
        bindCheckbox(R.id.checkCred, SupportedUpiApps.CRED)
    }

    private fun bindCheckbox(viewId: Int, appId: String) {
        val checkbox = findViewById<CheckBox>(viewId)
        checkbox.isChecked = settingsRepository.isAppEnabled(appId)
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            settingsRepository.setAppEnabled(appId, isChecked)
        }
    }

    companion object {
        private const val REQUEST_POST_NOTIFICATIONS = 1001
    }
}
