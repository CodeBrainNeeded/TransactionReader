package com.speakupi.parser

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class NotificationParserTest {
    @Test
    fun parse_shouldExtractAmount_forBhimStyleMessage() {
        val parsed = NotificationParser.parse(
            sourcePackage = "in.org.npci.upiapp",
            title = "Payment received",
            body = "You received Rs. 1,250.50 from Ravi via UPI"
        )

        assertNotNull(parsed)
        assertEquals("1250.50", parsed?.amount?.toPlainString())
    }

    @Test
    fun parse_shouldRejectFailedNotifications() {
        val parsed = NotificationParser.parse(
            sourcePackage = "com.phonepe.app",
            title = "Payment update",
            body = "Payment failed for Rs. 500"
        )

        assertNull(parsed)
    }

    @Test
    fun parse_shouldRejectMissingPositiveKeyword() {
        val parsed = NotificationParser.parse(
            sourcePackage = "net.one97.paytm",
            title = "Recharge",
            body = "Rs. 299 debited for mobile recharge"
        )

        assertNull(parsed)
    }

    @Test
    fun parse_shouldExtractAmount_forGPayPaidToYouStyleMessage() {
        val parsed = NotificationParser.parse(
            sourcePackage = "com.google.android.apps.nbu.paisa.user",
            title = "UPI payment",
            body = "Rahul paid ₹350 to you via UPI"
        )

        assertNotNull(parsed)
        assertEquals("350", parsed?.amount?.toPlainString())
    }

    @Test
    fun parse_shouldExtractAmount_forPhonePePaymentReceivedStyleMessage() {
        val parsed = NotificationParser.parse(
            sourcePackage = "com.phonepe.app",
            title = "Payment update",
            body = "Payment received: INR 850 from Priya"
        )

        assertNotNull(parsed)
        assertEquals("850", parsed?.amount?.toPlainString())
    }

    @Test
    fun parse_shouldRejectOutgoingPaidStyleMessage() {
        val parsed = NotificationParser.parse(
            sourcePackage = "com.google.android.apps.nbu.paisa.user",
            title = "UPI payment",
            body = "You paid ₹350 to Rahul"
        )

        assertNull(parsed)
    }

    @Test
    fun parse_shouldExtractAmount_forSentToYouStyleMessage() {
        val parsed = NotificationParser.parse(
            sourcePackage = "com.phonepe.app",
            title = "UPI update",
            body = "Amit sent INR 500 to you"
        )

        assertNotNull(parsed)
        assertEquals("500", parsed?.amount?.toPlainString())
    }

    @Test
    fun parse_shouldRejectOutgoingSentStyleMessage() {
        val parsed = NotificationParser.parse(
            sourcePackage = "com.phonepe.app",
            title = "UPI update",
            body = "You sent INR 500 to Amit"
        )

        assertNull(parsed)
    }
}
