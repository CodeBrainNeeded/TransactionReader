# TransactionReader

TransactionReader is an Android app that listens for UPI payment notifications and announces incoming amounts with text-to-speech.

## Current Scope

- Runs a notification listener in the background.
- Filters notifications from supported UPI apps (BHIM, Google Pay, PhonePe, Paytm, CRED).
- Parses incoming-payment notifications and extracts rupee amount.
- Speaks a confirmation in the format: "Received X rupees".
- Provides a simple setup screen for notification access, test TTS, and per-app allowlist.

## Install on Phone (APK Sideload)

1. Open this project in Android Studio.
2. Let Gradle sync finish.
3. Build an APK from Build > Build Bundle(s) / APK(s) > Build APK(s).
4. Copy the generated APK to your phone.
5. On your phone, enable installation from unknown sources for your file manager/browser.
6. Install the APK.

## First-Time Setup on Device

1. Open TransactionReader.
2. Tap "Open notification access settings".
3. Enable TransactionReader in notification listener access.
4. Return to app and use "Test TTS".
5. Enable the UPI apps you use in the allowlist.
6. Optionally enable reliability mode if your device aggressively kills background apps.

## Notes and Limitations

- Android does not allow auto-granting notification listener access; this is always manual.
- Reliability differs by OEM battery policy and Android version.
- Processing is local on-device in the current implementation.
