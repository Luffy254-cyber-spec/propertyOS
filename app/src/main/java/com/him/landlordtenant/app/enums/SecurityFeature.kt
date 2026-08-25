package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * SECURITY FEATURE
 * =============================================================
 */
enum class SecurityFeature(
    val displayName: String
) {
    CCTV("CCTV"),
    SECURITY_GUARD("Security Guard"),
    ELECTRIC_FENCE("Electric Fence"),
    GATED_ENTRY("Gated Entry"),
    ACCESS_CARD("Access Card"),
    BIOMETRIC_ACCESS("Biometric Access"),
    INTERCOM("Intercom"),
    SECURITY_LIGHTING("Security Lighting"),
    ALARM_SYSTEM("Alarm System"),
    CONTROLLED_PARKING("Controlled Parking");

    companion object {
        fun fromValue(value: String?): SecurityFeature? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
