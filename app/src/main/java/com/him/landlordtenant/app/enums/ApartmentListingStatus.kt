package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * APARTMENT LISTING STATUS
 * =============================================================
 */
enum class ApartmentListingStatus(
    val displayName: String
) {
    DRAFT("Draft"),
    PENDING_REVIEW("Pending Review"),
    PUBLISHED("Published"),
    PAUSED("Paused"),
    SUSPENDED("Suspended"),
    ARCHIVED("Archived");

    companion object {
        fun fromValue(value: String?): ApartmentListingStatus? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
