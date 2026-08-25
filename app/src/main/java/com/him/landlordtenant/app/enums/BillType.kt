package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * BILL TYPE
 * =============================================================
 */
enum class BillType(
    val displayName: String
) {
    RENT("Rent"),
    WATER("Water Bill"),
    ELECTRICITY("Electricity Bill"),
    SERVICE_CHARGE("Service Charge"),
    GARBAGE("Garbage Collection"),
    INTERNET("Internet"),
    PARKING("Parking Fee"),
    SECURITY("Security Fee"),
    MAINTENANCE("Maintenance Charge"),
    PENALTY("Late Payment Penalty"),
    DEPOSIT("Security Deposit"),
    OTHER("Other Charge");

    companion object {
        fun fromValue(value: String?): BillType? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
