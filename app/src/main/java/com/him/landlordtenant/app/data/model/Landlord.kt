package com.him.landlordtenant.app.data.model

import com.him.landlordtenant.app.enums.PaymentMethod

/**
 * =============================================================
 * LANDLORD MODEL
 * =============================================================
 *
 * Stores landlord-specific information.
 *
 * Authentication:
 *     User.kt
 *
 * Landlord/property information:
 *     Landlord.kt
 *
 * Main responsibilities:
 *
 * - Own/manage apartments
 * - Manage floors and houses
 * - Manage tenants
 * - Configure rent
 * - Configure bills
 * - Configure payment methods
 * - Create tenancy agreements
 * - Manage property staff
 * - Receive tenant notifications
 * - Manage maintenance
 * - Manage property listings
 * - View financial reports
 *
 * =============================================================
 */

data class Landlord(

    /*
     * ---------------------------------------------------------
     * IDENTITY
     * ---------------------------------------------------------
     */

    val id: String = "",

    val userId: String = "",

    /*
     * ---------------------------------------------------------
     * LANDLORD PROFILE
     * ---------------------------------------------------------
     */

    val businessName: String? = null,

    val businessRegistrationNumber: String? = null,

    val landlordType:
    LandlordType = LandlordType.INDIVIDUAL,

    val bio: String? = null,

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val idType:
    LandlordIdType = LandlordIdType.NATIONAL_ID,

    val idNumber: String? = null,

    val idDocumentUrl: String? = null,

    /*
     * ---------------------------------------------------------
     * VERIFICATION
     * ---------------------------------------------------------
     */

    val verificationStatus:
    IdentityVerificationStatus =
        IdentityVerificationStatus.NOT_VERIFIED,

    val verificationSubmittedAt: String? = null,

    val verificationCompletedAt: String? = null,

    val verificationNotes: String? = null,

    /*
     * ---------------------------------------------------------
     * PROPERTY OWNERSHIP
     * ---------------------------------------------------------
     */

    val ownedApartmentIds:
    List<String> = emptyList(),

    val managedApartmentIds:
    List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * STAFF
     * ---------------------------------------------------------
 */

    val staffIds:
    List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * TENANTS
     * ---------------------------------------------------------
     */

    val tenantIds:
    List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * PROPERTY STATISTICS
     * ---------------------------------------------------------
     */

    val totalApartments: Int = 0,

    val totalFloors: Int = 0,

    val totalHouses: Int = 0,

    val occupiedHouses: Int = 0,

    val vacantHouses: Int = 0,

    val housesUnderMaintenance: Int = 0,

    /*
     * ---------------------------------------------------------
     * FINANCIAL INFORMATION
     * ---------------------------------------------------------
 */

    val totalExpectedMonthlyRent: Double = 0.0,

    val totalCollectedMonthlyRent: Double = 0.0,

    val totalOutstandingRent: Double = 0.0,

    val totalOutstandingBills: Double = 0.0,

    /*
     * ---------------------------------------------------------
     * PAYMENT CONFIGURATION
     * ---------------------------------------------------------
 */

    val paymentConfiguration:
    LandlordPaymentConfiguration =
        LandlordPaymentConfiguration(),

    /*
     * ---------------------------------------------------------
     * BILLING CONFIGURATION
     * ---------------------------------------------------------
 */

    val billingConfiguration:
    LandlordBillingConfiguration =
        LandlordBillingConfiguration(),

    /*
     * ---------------------------------------------------------
     * AGREEMENT
     * ---------------------------------------------------------
 */

    val defaultAgreementId: String? = null,

    /*
     * ---------------------------------------------------------
     * COMMUNICATION
     * ---------------------------------------------------------
 */

    val preferredContactMethod:
    ContactMethod = ContactMethod.PHONE,

    val supportPhone: String? = null,

    val supportEmail: String? = null,

    val whatsappNumber: String? = null,

    /*
     * ---------------------------------------------------------
     * PROFESSIONAL SERVICES
     * ---------------------------------------------------------
 */

    val preferredProfessionalIds:
    List<String> = emptyList(),

    /*
     * ---------------------------------------------------------
     * EMERGENCY INFORMATION
     * ---------------------------------------------------------
 */

    val emergencyContacts:
    List<LandlordEmergencyContact> = emptyList(),

    /*
     * ---------------------------------------------------------
     * ACCOUNT
     * ---------------------------------------------------------
 */

    val accountStatus:
    LandlordAccountStatus =
        LandlordAccountStatus.ACTIVE,

    /*
     * ---------------------------------------------------------
     * CREATED / UPDATED
     * ---------------------------------------------------------
 */

    val createdAt: String? = null,

    val updatedAt: String? = null

) {

    /*
     * =========================================================
     * COMPUTED PROPERTIES
     * =========================================================
 */

    /**
     * Whether the landlord has verified their identity.
     */

    val isVerified: Boolean
        get() =
            verificationStatus ==
                    IdentityVerificationStatus.VERIFIED


    /**
     * Whether the landlord has properties.
     */

    val hasProperties: Boolean
        get() =
            ownedApartmentIds.isNotEmpty() ||
                    managedApartmentIds.isNotEmpty()


    /**
     * Total outstanding money.
     */

    val totalOutstanding: Double
        get() =
            totalOutstandingRent +
                    totalOutstandingBills


    /**
     * Collection rate.
     *
     * Example:
     *
     * Expected: KES 100,000
     * Collected: KES 90,000
     *
     * Collection rate = 90%
     */

    val collectionRate: Double
        get() {

            if (totalExpectedMonthlyRent <= 0.0) {
                return 0.0
            }

            return (
                    totalCollectedMonthlyRent /
                            totalExpectedMonthlyRent
                    ) * 100.0
        }


    /**
     * Occupancy rate.
     */

    val occupancyRate: Double
        get() {

            if (totalHouses <= 0) {
                return 0.0
            }

            return (
                    occupiedHouses.toDouble() /
                            totalHouses.toDouble()
                    ) * 100.0
        }


    /**
     * Whether payment configuration is complete enough
     * to receive digital payments.
     */

    val hasPaymentConfiguration: Boolean
        get() =
            paymentConfiguration.methods.isNotEmpty()
}


/*
 * =============================================================
 * LANDLORD TYPE
 * =============================================================
 */

enum class LandlordType(

    val displayName: String

) {

    INDIVIDUAL(
        "Individual Landlord"
    ),

    COMPANY(
        "Company"
    ),

    PROPERTY_COMPANY(
        "Property Management Company"
    ),

    TRUST(
        "Trust"
    ),

    ORGANIZATION(
        "Organization"
    )
}


/*
 * =============================================================
 * LANDLORD ID TYPE
 * =============================================================
 */

enum class LandlordIdType(

    val displayName: String

) {

    NATIONAL_ID(
        "National ID"
    ),

    PASSPORT(
        "Passport"
    ),

    ALIEN_ID(
        "Alien ID"
    ),

    BUSINESS_REGISTRATION(
        "Business Registration"
    ),

    OTHER(
        "Other"
    )
}


/*
 * =============================================================
 * LANDLORD ACCOUNT STATUS
 * =============================================================
 */

enum class LandlordAccountStatus(

    val displayName: String

) {

    ACTIVE(
        "Active"
    ),

    PENDING_VERIFICATION(
        "Pending Verification"
    ),

    SUSPENDED(
        "Suspended"
    ),

    RESTRICTED(
        "Restricted"
    ),

    DISABLED(
        "Disabled"
    )
}


/*
 * =============================================================
 * PAYMENT CONFIGURATION
 * =============================================================
 *
 * Supports multiple payment channels.
 *
 * Example:
 *
 * M-Pesa
 * Airtel Money
 * Pesapal
 * Mastercard
 * Bank
 *
 * =============================================================
 */

data class LandlordPaymentConfiguration(

    val methods:
    List<PaymentMethod> = emptyList(),

    /*
     * ---------------------------------------------------------
     * M-PESA
     * ---------------------------------------------------------
     */

    val mpesa:
    MpesaConfiguration? = null,

    /*
     * ---------------------------------------------------------
     * AIRTEL MONEY
     * ---------------------------------------------------------
 */

    val airtelMoney:
    AirtelMoneyConfiguration? = null,

    /*
     * ---------------------------------------------------------
     * PESAPAL
     * ---------------------------------------------------------
 */

    val pesapal:
    PesapalConfiguration? = null,

    /*
     * ---------------------------------------------------------
     * BANK
     * ---------------------------------------------------------
 */

    val bank:
    BankAccountConfiguration? = null,

    /*
     * ---------------------------------------------------------
     * AUTOMATIC RENT COLLECTION
     * ---------------------------------------------------------
 */

    val automaticRentCollection:
    Boolean = false,

    /*
     * ---------------------------------------------------------
     * AUTOMATIC RECEIPTS
     * ---------------------------------------------------------
 */

    val automaticReceipts:
    Boolean = true
)


/*
 * =============================================================
 * PAYMENT METHODS
 * =============================================================
 */


/*
 * =============================================================
 * M-PESA CONFIGURATION
 * =============================================================
 */

data class MpesaConfiguration(

    val paybillNumber: String? = null,

    val tillNumber: String? = null,

    val accountNumber: String? = null,

    val businessName: String? = null,

    val isVerified: Boolean = false
)


/*
 * =============================================================
 * AIRTEL MONEY CONFIGURATION
 * =============================================================
 */

data class AirtelMoneyConfiguration(

    val phoneNumber: String? = null,

    val merchantNumber: String? = null,

    val accountNumber: String? = null,

    val businessName: String? = null,

    val isVerified: Boolean = false
)


/*
 * =============================================================
 * PESAPAL CONFIGURATION
 * =============================================================
 */

data class PesapalConfiguration(

    val merchantId: String? = null,

    val accountName: String? = null,

    val isVerified: Boolean = false
)


/*
 * =============================================================
 * BANK ACCOUNT CONFIGURATION
 * =============================================================
 */

data class BankAccountConfiguration(

    val bankName: String = "",

    val branchName: String? = null,

    val accountName: String = "",

    val accountNumber: String = "",

    val swiftCode: String? = null,

    val isVerified: Boolean = false
)


/*
 * =============================================================
 * BILLING CONFIGURATION
 * =============================================================
 */

data class LandlordBillingConfiguration(

    /*
     * Rent
     */

    val rentEnabled: Boolean = true,

    /*
     * Water
     */

    val waterBillingEnabled: Boolean = true,

    /*
     * Electricity
     */

    val electricityBillingEnabled: Boolean = true,

    /*
     * Garbage
     */

    val garbageBillingEnabled: Boolean = true,

    /*
     * Internet
     */

    val internetBillingEnabled: Boolean = false,

    /*
     * Service charge
     */

    val serviceChargeEnabled: Boolean = false,

    /*
     * Other bills
     */

    val otherBillsEnabled: Boolean = true,

    /*
     * Automatic reminders
     */

    val automaticBillReminders: Boolean = true,

    /*
     * Days before due date
     */

    val reminderDaysBeforeDue: Int = 3
)


/*
 * =============================================================
 * CONTACT METHOD
 * =============================================================
 */

enum class ContactMethod(

    val displayName: String

) {

    PHONE(
        "Phone"
    ),

    SMS(
        "SMS"
    ),

    WHATSAPP(
        "WhatsApp"
    ),

    EMAIL(
        "Email"
    ),

    IN_APP_CHAT(
        "In-App Chat"
    )
}


/*
 * =============================================================
 * LANDLORD EMERGENCY CONTACT
 * =============================================================
 */

data class LandlordEmergencyContact(

    val id: String = "",

    val name: String = "",

    val role: String = "",

    val phoneNumber: String = "",

    val alternativePhoneNumber: String? = null,

    val email: String? = null,

    val isPrimary: Boolean = false
)