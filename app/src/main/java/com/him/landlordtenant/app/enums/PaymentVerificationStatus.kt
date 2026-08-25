package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * PAYMENT VERIFICATION STATUS
 * =============================================================
 */
enum class PaymentVerificationStatus(
    val displayName: String
) {
    PENDING("Pending Verification"),
    VERIFIED("Verified"),
    UNVERIFIED("Unverified"),
    FAILED("Verification Failed"),
    REJECTED("Rejected"),
    DISPUTED("Disputed"),
    RECONCILED("Reconciled");

    companion object {
        fun fromValue(value: String?): PaymentVerificationStatus? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
