package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * MEMBERSHIP ROLE
 * =============================================================
 */
enum class MembershipRole(
    val displayName: String
) {
    PRIMARY_TENANT("Primary Tenant"),
    OCCUPANT("Occupant"),
    DEPENDENT("Dependent"),
    CO_TENANT("Co-Tenant"),
    GUEST("Long-term Guest");

    companion object {
        fun fromValue(value: String?): MembershipRole? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
