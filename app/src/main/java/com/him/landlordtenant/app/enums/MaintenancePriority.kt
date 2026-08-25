package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * MAINTENANCE PRIORITY
 * =============================================================
 */
enum class MaintenancePriority(
    val displayName: String,
    val isUrgent: Boolean
) {
    LOW("Low", false),
    NORMAL("Normal", false),
    HIGH("High", true),
    URGENT("Urgent", true),
    EMERGENCY("Emergency", true);

    companion object {
        fun fromValue(value: String?): MaintenancePriority? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
