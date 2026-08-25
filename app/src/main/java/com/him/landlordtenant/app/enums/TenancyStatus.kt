package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * TENANCY STATUS
 * =============================================================
 */
enum class TenancyStatus(
    val displayName: String
) {
    PENDING_APPROVAL("Pending Approval"),
    ACTIVE("Active Tenant"),
    NOTICE_GIVEN("Under Notice"),
    VACATED("Vacated"),
    EVICTED("Evicted"),
    REJECTED("Application Rejected");

    companion object {
        fun fromValue(value: String?): TenancyStatus? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
