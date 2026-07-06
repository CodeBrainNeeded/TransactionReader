##### High-Level Functionality:

An Android app that runs in the background, reads and processes phone notifications, identifies ones that come from UPI payments, and announces identified transactions.

##### Specific Design Requirements:

* runs in the background, even when the phone is turned off
* reads the phone's notifications and identifies ones that mention receiving money
* processes the text of the notifications to determine how much money is being received
* use text-to-speech to read out that the user "received 'x' rupees" using the phone's speakers
* should at a minimum be compatible with notifications from the BHIM app, but preferably also from other UPI applications

##### Other Notes:

* the README.md file should state the purpose of the app and explain how the app can be installed on a phone
* the UI should include a toggle that allows the user to enable/disable the "received" announcements
* the UI should include a text box that the user can edit and type a message in. The message, if any is entered, should also be read out after the standard "received ..." message is read. The contents of the text box should persist through device restarts and repeated opening/closing of the app.