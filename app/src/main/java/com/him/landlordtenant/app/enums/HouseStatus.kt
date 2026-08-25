package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * HOUSE STATUS
 * =============================================================
 */
enum class HouseStatus(
    val displayName: String
) {
    VACANT("Vacant"),
    OCCUPIED("Occupied"),
    NOT_READY("Not Ready"),
    UNDER_MAINTENANCE("Under Maintenance"),
    RESERVED("Reserved"),
    PENDING_MOVE_IN("Pending Move-In"),
    BLOCKED("Blocked"),
    ARCHIVED("Archived");

    companion object {
        fun fromValue(value: String?): HouseStatus? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
