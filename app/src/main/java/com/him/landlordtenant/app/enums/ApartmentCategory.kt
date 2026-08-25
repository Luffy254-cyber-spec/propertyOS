package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * APARTMENT CATEGORY
 * =============================================================
 */
enum class ApartmentCategory(
    val displayName: String
) {
    RESIDENTIAL("Residential"),
    COMMERCIAL("Commercial"),
    STUDENT("Student Housing"),
    FAMILY("Family Housing"),
    LUXURY("Luxury"),
    AFFORDABLE("Affordable Housing"),
    MIXED("Mixed");

    companion object {
        fun fromValue(value: String?): ApartmentCategory? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
