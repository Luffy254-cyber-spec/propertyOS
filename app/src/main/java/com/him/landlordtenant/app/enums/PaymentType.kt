package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * PAYMENT TYPE
 * =============================================================
 */
enum class PaymentType(
    val displayName: String
) {
    RENT("Rent Payment"),
    DEPOSIT("Security Deposit"),
    BILL("Bill Payment"),
    SERVICE_FEE("Service Fee"),
    REPAIR_COST("Repair Cost"),
    FINE("Fine / Penalty"),
    REFUND("Refund"),
    OTHER("Other Payment");

    companion object {
        fun fromValue(value: String?): PaymentType? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
