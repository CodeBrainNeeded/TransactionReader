# TransactionReader

TransactionReader is an Android app that listens for UPI payment notifications and announces incoming amounts with text-to-speech.

## Current Scope

- Runs a notification listener in the background.
- Starts a foreground reliability service automatically and restarts it after boot.
- Filters notifications from supported UPI apps automatically: BHIM, Google Pay, PhonePe, Paytm, Navi, super.money, FamPay, and CRED.
- Parses incoming-payment notifications and extracts the rupee amount.
- Speaks a confirmation in the format: "Received X rupees".
- Optionally speaks a second custom message after the amount announcement.
- Provides a minimal setup screen with:
	- a toggle to enable or disable received-payment announcements
	- a text box for the optional post-announcement message

## Current UI Behavior

- The optional post-announcement message defaults to `Thank you!`.
- The custom message persists across app restarts and device restarts.
- Supported UPI apps are no longer selected in the UI; matching is automatic for all supported packages.
- Reliability mode is always on; there is no longer a separate toggle for it.

## Install on Phone (APK Sideload)

1. Open this project in Android Studio.
2. Let Gradle sync finish.
3. Build an APK from Build > Build Bundle(s) / APK(s) > Build APK(s).
4. Copy the generated APK to your phone.
5. On your phone, enable installation from unknown sources for your file manager/browser.
6. Install the APK.

## First-Time Setup on Device

1. Open TransactionReader.
2. On Android 13 and above, allow the app's notification permission if prompted.
3. Enable TransactionReader in Android's notification listener access screen when it opens.
4. Return to the app.
5. Leave `Announce received payments` enabled if you want announcements.
6. Edit the optional message field if you want a custom phrase spoken after each received-payment announcement.

## Notes and Limitations

- Android does not allow auto-granting notification listener access; this is always manual.
- Reliability still depends somewhat on OEM battery policy and Android version, even with the foreground service always running.
- On Android 13 and above, notification posting permission may also be required for the foreground-service notification.
- Processing is local on-device in the current implementation.
