package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * VISIT TYPE
 * =============================================================
 */
enum class VisitType(
    val displayName: String
) {
    VIEWING("Property Viewing"),
    MAINTENANCE("Maintenance Visit"),
    INSPECTION("Inspection Visit"),
    MANAGEMENT("Management Visit"),
    DELIVERY("Delivery"),
    GUEST("Guest Visit");

    companion object {
        fun fromValue(value: String?): VisitType? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
