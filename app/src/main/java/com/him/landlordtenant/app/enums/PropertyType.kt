package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * PROPERTY TYPE
 * =============================================================
 */
enum class PropertyType(
    val displayName: String
) {
    APARTMENT("Apartment"),
    FLATS("Flats"),
    BEDSITTER_COMPLEX("Bedsitter Complex"),
    HOSTEL("Hostel"),
    STUDENT_HOUSING("Student Housing"),
    TOWNHOUSE("Townhouse"),
    RESIDENTIAL_COMPLEX("Residential Complex"),
    COMMERCIAL("Commercial Property"),
    MIXED_USE("Mixed Use"),
    OTHER("Other");

    companion object {
        fun fromValue(value: String?): PropertyType? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
