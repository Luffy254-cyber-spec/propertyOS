package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * ACCOUNT STATUS
 * =============================================================
 */
enum class AccountStatus(
    val displayName: String
) {
    ACTIVE("Active"),
    PENDING_VERIFICATION("Pending Verification"),
    SUSPENDED("Suspended"),
    DEACTIVATED("Deactivated"),
    BLOCKED("Blocked"),
    ARCHIVED("Archived");

    companion object {
        fun fromValue(value: String?): AccountStatus? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
