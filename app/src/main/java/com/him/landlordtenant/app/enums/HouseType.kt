package com.him.landlordtenant.app.enums

/**
 * =============================================================
 * HOUSE TYPE
 * =============================================================
 */
enum class HouseType(
    val displayName: String
) {
    BEDSITTER("Bedsitter"),
    STUDIO("Studio"),
    ONE_BEDROOM("1 Bedroom"),
    TWO_BEDROOM("2 Bedroom"),
    THREE_BEDROOM("3 Bedroom"),
    FOUR_BEDROOM("4 Bedroom"),
    FIVE_BEDROOM("5 Bedroom"),
    PENTHOUSE("Penthouse"),
    SHOP("Shop"),
    OFFICE("Office"),
    OTHER("Other");

    companion object {
        fun fromValue(value: String?): HouseType? {
            if (value.isNullOrBlank()) return null
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
