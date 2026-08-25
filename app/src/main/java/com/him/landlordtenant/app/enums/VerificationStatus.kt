package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * VERIFICATION STATUS
 * =============================================================
 */
enum class VerificationStatus(
    val displayName: String
) {
    NOT_SUBMITTED("Not Submitted"),
    PENDING("Pending Verification"),
    UNDER_REVIEW("Under Review"),
    VERIFIED("Verified"),
    REJECTED("Rejected"),
    SUSPENDED("Suspended"),
    EXPIRED("Verification Expired");

    companion object {
        fun fromValue(value: String?): VerificationStatus? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
