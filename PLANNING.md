##### High-Level Functionality:

An Android app that runs in the background, reads and processes phone notifications, identifies ones that come from UPI payments, and announces identified transactions.

##### Specific Design Requirements:

* runs in the background, even when the phone is turned off
* reads the phone's notifications and identifies ones that mention receiving money
* processes the text of the notifications to determine how much money is being received
* use text-to-speech to read out that the user "received 'x' rupees" using the phone's speakers
* should be compatible with PhonePe, GPay, Paytm, Navi, super.money, BHIM, Fampay, and CRED.

##### Other Notes:

* the README.md file should state the purpose of the app and explain how the app can be installed on a phone
* the UI should include a toggle that allows the user to enable/disable the "received" announcements
* the UI should include a text box that the user can edit and type a message in. The message, if any is entered, should be read out after the standard "received ..." message is read. The contents of the text box should persist through device restarts and repeated opening/closing of the app. There should be a "save" button below the text box that the user must click to make the text persist.
* if notification-sending permissions are not given, the UI should include a button that states that notification-sending permissions must be given for the app to work, and clicking on the button should take the user to the page in settings that allows them to give the permission
* if notification-reading permissions are not given, the UI should include a button that states that notification-reading permissions must be given for the app to work, and clicking on the button should take the user to the page in settings that allows them to give the permission