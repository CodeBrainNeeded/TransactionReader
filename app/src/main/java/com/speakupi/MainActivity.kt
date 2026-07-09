package com.speakupi

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.materialswitch.MaterialSwitch
import com.speakupi.data.SettingsRepository
import com.speakupi.service.ListenerForegroundService
import com.speakupi.util.NotificationAccessUtils

class MainActivity : AppCompatActivity() {
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var notificationSendPermissionButton: MaterialButton
    private lateinit var notificationReadPermissionExplanation: TextView
    private lateinit var notificationReadPermissionButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settingsRepository = SettingsRepository(this)
        ContextCompat.startForegroundService(this, Intent(this, ListenerForegroundService::class.java))

        notificationSendPermissionButton = findViewById(R.id.notificationSendPermissionButton)
    notificationReadPermissionExplanation = findViewById(R.id.notificationReadPermissionExplanation)
        notificationReadPermissionButton = findViewById(R.id.notificationReadPermissionButton)

        notificationSendPermissionButton.setOnClickListener {
            openAppNotificationSettings()
        }
        notificationReadPermissionButton.setOnClickListener {
            openNotificationAccessSettings()
        }

        val receivedAnnouncementsSwitch = findViewById<MaterialSwitch>(R.id.switchReceivedAnnouncements)
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
    }

    override fun onResume() {
        super.onResume()
        maybeRequestPostNotificationsPermission()
        updatePermissionButtons()
    }

    private fun maybeRequestPostNotificationsPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return
        }

        val permissionState = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.POST_NOTIFICATIONS
        )
        if (permissionState == PackageManager.PERMISSION_GRANTED) {
            return
        }

        if (settingsRepository.hasRequestedPostNotificationsPermission()) {
            return
        }

        settingsRepository.setPostNotificationsPermissionRequested(true)
        requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), REQUEST_POST_NOTIFICATIONS)
    }

    private fun openNotificationAccessSettings() {
        startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
    }

    private fun updatePermissionButtons() {
        notificationSendPermissionButton.visibility = if (needsNotificationSendPermission()) {
            View.VISIBLE
        } else {
            View.GONE
        }

        val notificationReadAccessMissing = !NotificationAccessUtils.isNotificationAccessEnabled(this)
        notificationReadPermissionExplanation.visibility = if (notificationReadAccessMissing) {
            View.VISIBLE
        } else {
            View.GONE
        }
        notificationReadPermissionButton.visibility = if (notificationReadAccessMissing) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun needsNotificationSendPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return false
        }

        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
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

    companion object {
        private const val REQUEST_POST_NOTIFICATIONS = 1001
    }
}
