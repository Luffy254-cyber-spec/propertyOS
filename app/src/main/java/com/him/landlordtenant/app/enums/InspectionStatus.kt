package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * INSPECTION STATUS
 * =============================================================
 */
enum class InspectionStatus(
    val displayName: String
) {
    SCHEDULED("Scheduled"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    RESCHEDULED("Rescheduled"),
    DISPUTED("Disputed"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    companion object {
        fun fromValue(value: String?): InspectionStatus? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
