package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * HOUSE CONDITION
 * =============================================================
 */
enum class HouseCondition(
    val displayName: String
) {
    NEW("New"),
    EXCELLENT("Excellent"),
    GOOD("Good"),
    FAIR("Fair"),
    NEEDS_REPAIRS("Needs Repairs"),
    NEEDS_PAINTING("Needs Painting"),
    NEEDS_MAJOR_REPAIRS("Needs Major Repairs"),
    UNINHABITABLE("Uninhabitable"),
    DAMAGED("Damaged"),
    NEEDS_CLEANING("Needs Cleaning");

    companion object {
        fun fromValue(value: String?): HouseCondition? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
