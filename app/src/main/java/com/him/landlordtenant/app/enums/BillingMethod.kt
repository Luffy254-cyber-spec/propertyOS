package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * BILLING METHOD
 * =============================================================
 */
enum class BillingMethod(
    val displayName: String
) {
    FIXED_RATE("Fixed Rate"),
    METERED("Metered"),
    PRO_RATED("Pro-rated"),
    PERCENTAGE("Percentage"),
    CONSUMPTION_BASED("Consumption Based"),
    FLAT_FEE("Flat Fee");

    companion object {
        fun fromValue(value: String?): BillingMethod? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
