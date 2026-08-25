package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * IDENTITY VERIFICATION STATUS
 * =============================================================
 */
enum class IdentityVerificationStatus(
    val displayName: String
) {
    NOT_VERIFIED("Not Verified"),
    PENDING("Pending Review"),
    VERIFIED("Verified"),
    REJECTED("Rejected"),
    EXPIRED("Expired");

    companion object {
        fun fromValue(value: String?): IdentityVerificationStatus? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
