package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * APARTMENT AMENITY
 * =============================================================
 */
enum class ApartmentAmenity(
    val displayName: String
) {
    PARKING("Parking"),
    CCTV("CCTV"),
    SECURITY_GUARDS("Security Guards"),
    GATED_COMPOUND("Gated Compound"),
    ELECTRIC_FENCE("Electric Fence"),
    BOREHOLE("Borehole"),
    WATER_TANK("Water Tank"),
    BACKUP_GENERATOR("Backup Generator"),
    SOLAR_POWER("Solar Power"),
    ELEVATOR("Elevator"),
    GYM("Gym"),
    SWIMMING_POOL("Swimming Pool"),
    PLAYGROUND("Playground"),
    GARDEN("Garden"),
    LAUNDRY("Laundry"),
    WIFI("Wi-Fi"),
    INTERNET("Internet"),
    RESTAURANT("Restaurant"),
    SHOP("Shop"),
    COMMON_ROOM("Common Room"),
    ROOFTOP("Rooftop"),
    BALCONY("Balcony"),
    OTHER("Other");

    companion object {
        fun fromValue(value: String?): ApartmentAmenity? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
