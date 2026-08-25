package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * INSPECTION CONDITION
 * =============================================================
 */
enum class InspectionCondition(
    val displayName: String
) {
    EXCELLENT("Excellent"),
    GOOD("Good"),
    FAIR("Fair / Working"),
    POOR("Poor / Needs Attention"),
    DAMAGED("Damaged / Broken"),
    MISSING("Missing"),
    NOT_APPLICABLE("N/A");

    companion object {
        fun fromValue(value: String?): InspectionCondition? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
