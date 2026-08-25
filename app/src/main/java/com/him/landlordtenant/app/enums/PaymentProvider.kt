package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * PAYMENT PROVIDER
 * =============================================================
 */
enum class PaymentProvider(
    val displayName: String
) {
    MPESA("M-Pesa"),
    AIRTEL_MONEY("Airtel Money"),
    PESAPAL("Pesapal"),
    MASTERCARD("Mastercard"),
    VISA("Visa"),
    BANK("Bank"),
    CASH("Cash"),
    OTHER("Other");

    companion object {
        fun fromValue(value: String?): PaymentProvider? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
