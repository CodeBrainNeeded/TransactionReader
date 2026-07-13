# Privacy Policy for SpeakUPI

Effective date: 2026-07-13

## 1. Overview
SpeakUPI is designed to detect supported UPI payment notifications on your device and speak payment-received announcements.

Your privacy is a core design goal. SpeakUPI processes notification content locally on your device and does not send notification data to external servers.

## 2. Data We Process
SpeakUPI may process the following data on-device:
- Notification metadata and content from apps you allow Android to expose through Notification Access (for example: app package name, notification title, notification text).
- Parsed payment amount and confidence signals used to decide whether to announce a received payment.

## 3. Temporary Processing and Deletion
SpeakUPI processes notification content in memory and does not persist raw notification text or transaction details to long-term storage.

After processing:
- Notification content is not written to a local database or remote server by SpeakUPI.
- A short-lived in-memory duplicate-check cache is kept to prevent repeated announcements. This cache stores computed fingerprints (not full notification text) and is automatically cleaned/expired.

## 4. Data Stored on Device
SpeakUPI stores only app settings in SharedPreferences, such as:
- Whether received-payment announcements are enabled.
- Your custom post-announcement message.
- A local flag for whether POST_NOTIFICATIONS permission was requested.

These settings are stored locally by Android.

## 5. Backup and Device Transfer
SpeakUPI includes Android backup/device-transfer rules that allow SharedPreferences settings to be included in cloud backup and device-to-device transfer, depending on your Android/Google backup configuration.

Important:
- This applies to app settings listed above.
- SpeakUPI still does not intentionally store or back up raw notification text or full transaction notifications.

## 6. Data Sharing
SpeakUPI does not sell, rent, or share your data with advertisers, data brokers, or third-party analytics providers.

SpeakUPI does not transmit notification content to a SpeakUPI backend service.

## 7. Permissions
SpeakUPI may request the following permissions/features:
- Notification Access (Android system setting): required to read notifications for payment detection.
- POST_NOTIFICATIONS: required to show app notifications where applicable.
- FOREGROUND_SERVICE / FOREGROUND_SERVICE_DATA_SYNC: used to keep reliability services active.
- RECEIVE_BOOT_COMPLETED: used to restart required services after reboot.

Permissions can be revoked in Android settings at any time.

## 8. Security
SpeakUPI is designed for local processing and minimal data retention. While no software can guarantee absolute security, reducing collection and storage reduces exposure risk.

## 9. Children
SpeakUPI is not directed to children under 13, and it does not intentionally collect personal data from children.

## 10. Changes to This Policy
This Privacy Policy may be updated as features change. Updates should be published with a revised effective date.

## 11. Contact
For privacy questions, use the project repository contact method (for example, Issues or the maintainer contact details provided with the project).
