package com.speakupi.parser

import java.math.BigDecimal

private val positiveKeywords = listOf(
    "received",
    "credited",
    "credit",
    "received from",
    "paid",
    "sent",
    "payment received",
    "payment successful",
    "payment success",
    "paid to you",
    "paid you",
    "has paid",
    "sent you",
)
private val negativeKeywords = listOf(
    "failed",
    "declined",
    "pending",
    "error",
    "reversed",
    "debited",
    "you paid",
    "you sent",
    "payment sent"
)
private val amountPatterns = listOf(
    Regex("(?:received|credited)\\s+(?:₹|rs\\.?|inr)\\s*(\\d+(?:,\\d{3})*(?:\\.\\d{1,2})?)", RegexOption.IGNORE_CASE),
    Regex("(?:₹|rs\\.?|inr)\\s*(\\d+(?:,\\d{3})*(?:\\.\\d{1,2})?)", RegexOption.IGNORE_CASE)
)

data class ParsedTransaction(
    val amount: BigDecimal,
    val sourcePackage: String,
    val confidence: Int,
    val canonicalText: String
)

object NotificationParser {
    fun parse(sourcePackage: String, title: String?, body: String?): ParsedTransaction? {
        val combined = listOfNotNull(title, body)
            .joinToString(" ")
            .replace("\\n", " ")
            .trim()

        if (combined.isBlank()) {
            return null
        }

        val normalized = combined.lowercase()
        if (negativeKeywords.any { keyword -> normalized.contains(keyword) }) {
            return null
        }
        if (!containsIncomingKeyword(normalized)) {
            return null
        }

        if (isOutgoingPaymentMessage(normalized)) {
            return null
        }

        val amount = extractAmount(combined) ?: return null
        if (amount < BigDecimal.ONE || amount > BigDecimal("999999")) {
            return null
        }

        val confidence = buildConfidence(normalized, combined)
        if (confidence < 3) {
            return null
        }

        return ParsedTransaction(
            amount = amount,
            sourcePackage = sourcePackage,
            confidence = confidence,
            canonicalText = normalized
        )
    }

    private fun extractAmount(text: String): BigDecimal? {
        for (pattern in amountPatterns) {
            val match = pattern.find(text) ?: continue
            val candidate = match.groupValues.getOrNull(1) ?: continue
            val clean = candidate.replace(",", "")
            val amount = clean.toBigDecimalOrNull()
            if (amount != null) {
                return amount
            }
        }
        return null
    }

    private fun buildConfidence(normalizedText: String, rawText: String): Int {
        var score = 0
        if (containsIncomingKeyword(normalizedText)) score += 2
        if (normalizedText.contains("upi")) score += 1
        if (rawText.contains("₹") || normalizedText.contains("rs") || normalizedText.contains("inr")) score += 1
        return score
    }

    private fun containsIncomingKeyword(normalizedText: String): Boolean {
        return positiveKeywords.any { keyword -> normalizedText.contains(keyword) }
            || (normalizedText.contains("paid") && normalizedText.contains("to you"))
            || (normalizedText.contains("sent") && normalizedText.contains("to you"))
            || (normalizedText.contains("payment") &&
                (normalizedText.contains("received") || normalizedText.contains("to you")))
    }

    private fun isOutgoingPaymentMessage(normalizedText: String): Boolean {
        if (normalizedText.contains("you paid") || normalizedText.contains("you sent")) {
            return true
        }

        val paidToOther = normalizedText.contains("paid to ") && !normalizedText.contains("paid to you")
        val sentToOther = normalizedText.contains("sent to ") && !normalizedText.contains("sent to you")
        return paidToOther || sentToOther
    }
}
