package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * MEMBERSHIP STATUS
 * =============================================================
 */
enum class MembershipStatus(
    val displayName: String
) {
    PENDING("Pending Approval"),
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    REVOKED("Revoked"),
    EXPIRED("Expired");

    companion object {
        fun fromValue(value: String?): MembershipStatus? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
