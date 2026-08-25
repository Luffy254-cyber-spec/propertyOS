package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * MAINTENANCE CATEGORY
 * =============================================================
 */
enum class MaintenanceCategory(
    val displayName: String
) {
    PLUMBING("Plumbing"),
    ELECTRICAL("Electrical"),
    CARPENTRY("Carpenter"),
    PAINTING("Painting"),
    CLEANING("Cleaning"),
    SECURITY("Security"),
    HVAC("HVAC / Air Conditioning"),
    APPLIANCE("Appliance Repair"),
    ROOFING("Roofing"),
    GARDENING("Gardening / Landscaping"),
    GENERAL("General Repairs"),
    OTHER("Other");

    companion object {
        fun fromValue(value: String?): MaintenanceCategory? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
