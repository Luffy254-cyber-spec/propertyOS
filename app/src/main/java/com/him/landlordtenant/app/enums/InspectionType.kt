package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * INSPECTION TYPE
 * =============================================================
 */
enum class InspectionType(
    val displayName: String
) {
    MOVE_IN("Move-in Inspection"),
    MOVE_OUT("Move-out Inspection"),
    ROUTINE("Routine Maintenance"),
    EMERGENCY("Emergency Inspection"),
    RENOVATION("Renovation Check"),
    VALUATION("Valuation Inspection");

    companion object {
        fun fromValue(value: String?): InspectionType? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
