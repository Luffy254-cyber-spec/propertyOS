package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * VISIT STATUS
 * =============================================================
 */
enum class VisitStatus(
    val displayName: String
) {
    PENDING("Pending Approval"),
    CONFIRMED("Confirmed"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    NO_SHOW("No Show"),
    RESCHEDULED("Rescheduled");

    companion object {
        fun fromValue(value: String?): VisitStatus? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
