package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * TENANT ID TYPE
 * =============================================================
 */
enum class TenantIdType(
    val displayName: String
) {
    NATIONAL_ID("National ID"),
    PASSPORT("Passport"),
    DRIVING_LICENSE("Driving License"),
    ALIEN_ID("Alien Card / Refugee ID"),
    MILITARY_ID("Military ID"),
    OTHER("Other Official ID");

    companion object {
        fun fromValue(value: String?): TenantIdType? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
