package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * PAYMENT METHOD
 * =============================================================
 */
enum class PaymentMethod(
    val displayName: String
) {
    CASH("Cash"),
    MPESA("M-Pesa"),
    AIRTEL_MONEY("Airtel Money"),
    BANK_TRANSFER("Bank Transfer"),
    CHEQUE("Cheque"),
    CARD("Credit/Debit Card"),
    PESAPAL("Pesapal"),
    PAYPAL("PayPal"),
    GOOGLE_PAY("Google Pay"),
    APPLE_PAY("Apple Pay"),
    OTHER("Other");

    companion object {
        fun fromValue(value: String?): PaymentMethod? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
