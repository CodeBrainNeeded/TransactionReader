package com.speakupi.data

data class SupportedUpiApp(val id: String, val displayName: String, val packageNames: Set<String>)

object SupportedUpiApps {
    const val BHIM = "bhim"
    const val GPAY = "gpay"
    const val PHONEPE = "phonepe"
    const val PAYTM = "paytm"
    const val NAVI = "navi"
    const val SUPER_MONEY = "super_money"
    const val FAMPAY = "fampay"
    const val CRED = "cred"

    val all: List<SupportedUpiApp> = listOf(
        SupportedUpiApp(
            id = BHIM,
            displayName = "BHIM",
            packageNames = setOf("in.org.npci.upiapp", "com.upi.bhim")
        ),
        SupportedUpiApp(
            id = GPAY,
            displayName = "Google Pay",
            packageNames = setOf("com.google.android.apps.nbu.paisa.user")
        ),
        SupportedUpiApp(
            id = PHONEPE,
            displayName = "PhonePe",
            packageNames = setOf("com.phonepe.app")
        ),
        SupportedUpiApp(
            id = PAYTM,
            displayName = "Paytm",
            packageNames = setOf("net.one97.paytm")
        ),
        SupportedUpiApp(
            id = NAVI,
            displayName = "Navi",
            packageNames = setOf("com.navifinserv.customer", "com.naviapp")
        ),
        SupportedUpiApp(
            id = SUPER_MONEY,
            displayName = "super.money",
            packageNames = setOf("com.supermoney.app", "in.supermoney.app")
        ),
        SupportedUpiApp(
            id = FAMPAY,
            displayName = "FamPay",
            packageNames = setOf("in.fampay.app", "com.fampay.app")
        ),
        SupportedUpiApp(
            id = CRED,
            displayName = "CRED UPI",
            packageNames = setOf("com.dreamplug.androidapp")
        )
    )

    private val packageToAppId: Map<String, String> = all
        .flatMap { app -> app.packageNames.map { pkg -> pkg to app.id } }
        .toMap()

    fun idFromPackageName(packageName: String): String? = packageToAppId[packageName]
}
