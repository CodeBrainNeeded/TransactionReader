package com.varun.transactionreader.parser

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
}
