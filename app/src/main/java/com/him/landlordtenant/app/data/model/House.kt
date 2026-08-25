package com.him.landlordtenant.app.data.model

/**
 * =============================================================
 * HOUSE MODEL
 * =============================================================
 *
 * Represents one physical rental unit inside an apartment.
 *
 * Example:
 *
 * Apartment: Green View Apartments
 * Floor: 2
 * House: G2-04
 * Type: 2 Bedroom
 *
 * The same House object can be used by:
 *
 * Tenant:
 * - View house
 * - View rent
 * - View bills
 * - View status
 * - View facilities
 *
 * Landlord:
 * - Create house
 * - Edit house
 * - Change status
 * - Assign tenant
 * - View income
 * - Manage bills
 * - Manage maintenance
 *
 * Backend:
 * - Database record
 * - API response
 * - Firebase document
 *
 * =============================================================
 */

data class House(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val apartmentId: String = "",

    val floorId: String = "",

    val houseNumber: String = "",

    /*
     * ---------------------------------------------------------
     * BASIC INFORMATION
     * ---------------------------------------------------------
     */

    val houseType: HouseType = HouseType.BEDSITTER,

    val bedrooms: Int = 0,

    val bathrooms: Int = 1,

    val sizeSquareMeters: Double = 0.0,

    val description: String = "",

    /*
     * ---------------------------------------------------------
     * RENTAL INFORMATION
     * ---------------------------------------------------------
     */

    val monthlyRent: Double = 0.0,

    val securityDeposit: Double = 0.0,

    val serviceCharge: Double = 0.0,

    /*
     * ---------------------------------------------------------
     * BILLS
     * ---------------------------------------------------------
     *
     * These can later be changed from fixed values to
     * metered/dynamic billing.
     */

    val waterBill: Double = 0.0,

    val electricityBill: Double = 0.0,

    val garbageBill: Double = 0.0,

    val internetBill: Double = 0.0,

    val otherMonthlyCharges: Double = 0.0,

    /*
     * ---------------------------------------------------------
     * BILLING CONFIGURATION
     * ---------------------------------------------------------
     */

    val billingConfiguration:
    BillingConfiguration = BillingConfiguration(),

    /*
     * ---------------------------------------------------------
     * HOUSE STATUS
     * ---------------------------------------------------------
     */

    val status:
    HouseStatus = HouseStatus.VACANT,

    /*
     * ---------------------------------------------------------
     * TENANT
     * ---------------------------------------------------------
     *
     * Null means no tenant is currently assigned.
     */

    val tenantId: String? = null,

    val tenantName: String? = null,

    /*
     * ---------------------------------------------------------
     * HOUSE CONDITION
     * ---------------------------------------------------------
     */

    val condition:
    HouseCondition = HouseCondition.GOOD,

    val conditionDescription: String = "",

    /*
     * ---------------------------------------------------------
     * AVAILABILITY
     * ---------------------------------------------------------
     */

    val availableFrom: String? = null,

    val expectedReadyDate: String? = null,

    /*
     * ---------------------------------------------------------
     * MEDIA
     * ---------------------------------------------------------
     */

    val imageUrls: List<String> = emptyList(),

    val videoUrls: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * FACILITIES
     * ---------------------------------------------------------
     */

    val facilities: List<HouseFacility> = emptyList(),

    /*
     * ---------------------------------------------------------
     * LOCATION
     * ---------------------------------------------------------
     *
     * The apartment normally has the main GPS location.
     * These fields allow a house to optionally have its
     * own location metadata.
     */

    val latitude: Double? = null,

    val longitude: Double? = null,

    /*
     * ---------------------------------------------------------
     * TENANCY DATES
     * ---------------------------------------------------------
     */

    val moveInDate: String? = null,

    val leaseStartDate: String? = null,

    val leaseEndDate: String? = null,

    /*
     * ---------------------------------------------------------
     * RENT DUE CONFIGURATION
     * ---------------------------------------------------------
     */

    val rentDueDay: Int = 1,

    val gracePeriodDays: Int = 0,

    /*
     * ---------------------------------------------------------
     * HOUSE RULES
     * ---------------------------------------------------------
     */

    val houseRules: List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * AGREEMENT
     * ---------------------------------------------------------
     */

    val agreementId: String? = null,

    /*
     * ---------------------------------------------------------
     * MAINTENANCE
     * ---------------------------------------------------------
     */

    val activeMaintenanceRequestCount: Int = 0,

    /*
     * ---------------------------------------------------------
     * VERIFICATION
     * ---------------------------------------------------------
     */

    val isVerified: Boolean = false,

    /*
     * ---------------------------------------------------------
     * VISIBILITY
     * ---------------------------------------------------------
     *
     * A landlord may have a vacant house but temporarily hide
     * it from public listings.
     */

    val isListed: Boolean = true,

    /*
     * ---------------------------------------------------------
     * AUDIT INFORMATION
     * ---------------------------------------------------------
     */

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val createdBy: String? = null,

    val updatedBy: String? = null
) {

    /**
     * Total recurring monthly cost.
     *
     * This is an estimate. Actual bills may be calculated
     * differently depending on the landlord's billing model.
     */

    val estimatedMonthlyTotal: Double
        get() =
            monthlyRent +
                    serviceCharge +
                    waterBill +
                    electricityBill +
                    garbageBill +
                    internetBill +
                    otherMonthlyCharges


    /**
     * Amount required before moving in.
     *
     * Normally:
     *
     * Deposit + first month's rent
     *
     * Additional charges can be added separately.
     */

    val estimatedMoveInAmount: Double
        get() =
            securityDeposit +
                    monthlyRent +
                    serviceCharge


    /**
     * Whether this house currently has a tenant.
     */

    val isOccupied: Boolean
        get() =
            status == HouseStatus.OCCUPIED


    /**
     * Whether this house can currently be joined.
     */

    val canBeJoined: Boolean
        get() =
            status == HouseStatus.VACANT &&
                    isListed &&
                    isVerified
}


/*
 * =============================================================
 * BILLING CONFIGURATION
 * =============================================================
 *
 * Controls how different charges are calculated.
 *
 * Example:
 *
 * Water:
 *   Metered
 *
 * Garbage:
 *   Fixed
 *
 * Electricity:
 *   Tenant pays directly
 *
 * =============================================================
 */

data class BillingConfiguration(

    val water:
    BillingMethod = BillingMethod.FIXED,

    val electricity:
    BillingMethod = BillingMethod.METERED,

    val garbage:
    BillingMethod = BillingMethod.FIXED,

    val internet:
    BillingMethod = BillingMethod.FIXED,

    val serviceCharge:
    BillingMethod = BillingMethod.FIXED,

    val other:
    BillingMethod = BillingMethod.FIXED
)


/*
 * =============================================================
 * BILLING METHOD
 * =============================================================
 */

enum class BillingMethod(

    val displayName: String

) {

    FIXED(
        "Fixed Amount"
    ),

    METERED(
        "Metered"
    ),

    SHARED(
        "Shared"
    ),

    TENANT_DIRECT(
        "Paid Directly by Tenant"
    ),

    INCLUDED_IN_RENT(
        "Included in Rent"
    ),

    NOT_APPLICABLE(
        "Not Applicable"
    )
}


/*
 * =============================================================
 * HOUSE FACILITY
 * =============================================================
 */

enum class HouseFacility(

    val displayName: String

) {

    PARKING(
        "Parking"
    ),

    BALCONY(
        "Balcony"
    ),

    KITCHEN(
        "Kitchen"
    ),

    BOREHOLE(
        "Borehole"
    ),

    WATER_TANK(
        "Water Tank"
    ),

    HOT_SHOWER(
        "Hot Shower"
    ),

    SECURITY(
        "Security"
    ),

    CCTV(
        "CCTV"
    ),

    WIFI(
        "Wi-Fi"
    ),

    INTERNET(
        "Internet"
    ),

    LIFT(
        "Lift"
    ),

    GENERATOR(
        "Generator"
    ),

    BACKUP_POWER(
        "Backup Power"
    ),

    LAUNDRY(
        "Laundry"
    ),

    GYM(
        "Gym"
    ),

    SWIMMING_POOL(
        "Swimming Pool"
    ),

    GARDEN(
        "Garden"
    ),

    PLAYGROUND(
        "Playground"
    ),

    PET_FRIENDLY(
        "Pet Friendly"
    ),

    GATED_COMPOUND(
        "Gated Compound"
    ),

    ACCESS_CONTROL(
        "Access Control"
    ),

    OTHER(
        "Other"
    )
}