package com.him.landlordtenant.app.data.model

/**
 * =============================================================
 * PAYMENT METHOD MODEL
 * =============================================================
 *
 * Represents a payment destination/configuration used by a
 * landlord, property, apartment, or payment provider.
 *
 * Supported methods:
 *
 * - M-Pesa
 * - Airtel Money
 * - Pesapal
 * - Mastercard / Visa
 * - Bank Transfer
 * - Paybill
 * - Till Number
 * - Business Number
 * - Bank Account
 *
 * =============================================================
 */

data class PaymentMethodInfo(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val ownerId: String = "",

    val apartmentId: String? = null,

    val houseId: String? = null,

    /*
     * ---------------------------------------------------------
     * PAYMENT TYPE
     * ---------------------------------------------------------
 */

    val type:
    PaymentMethodType =
        PaymentMethodType.MPESA,

    /*
     * ---------------------------------------------------------
     * DISPLAY INFORMATION
     * ---------------------------------------------------------
 */

    val name: String = "",

    val description: String? = null,

    val enabled: Boolean = true,

    val primary: Boolean = false,

    /*
     * ---------------------------------------------------------
     * M-PESA / MOBILE MONEY
     * ---------------------------------------------------------
 */

    val mobileMoney:
    MobileMoneyDetails? = null,

    /*
     * ---------------------------------------------------------
     * PAYBILL / TILL
     * ---------------------------------------------------------
 */

    val businessPayment:
    BusinessPaymentDetails? = null,

    /*
     * ---------------------------------------------------------
     * BANK
     * ---------------------------------------------------------
 */

    val bankAccount:
    BankAccountDetails? = null,

    /*
     * ---------------------------------------------------------
     * CARD / PAYMENT GATEWAY
     * ---------------------------------------------------------
 */

    val gateway:
    PaymentGatewayDetails? = null,

    /*
     * ---------------------------------------------------------
     * CURRENCY
     * ---------------------------------------------------------
 */

    val currency: String = "KES",

    /*
     * ---------------------------------------------------------
     * VERIFICATION
     * ---------------------------------------------------------
 */

    val verification:
    PaymentMethodVerification =
        PaymentMethodVerification(),

    /*
     * ---------------------------------------------------------
     * FEES
     * ---------------------------------------------------------
 */

    val fees:
    PaymentFeeConfiguration =
        PaymentFeeConfiguration(),

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

    val createdAt: String? = null,

    val updatedAt: String? = null
)


/*
 * =============================================================
 * PAYMENT METHOD TYPE
 * =============================================================
 */

enum class PaymentMethodType(

    val displayName: String

) {

    MPESA(
        "M-Pesa"
    ),

    AIRTEL_MONEY(
        "Airtel Money"
    ),

    PESAPAL(
        "Pesapal"
    ),

    MASTERCARD(
        "Mastercard"
    ),

    VISA(
        "Visa"
    ),

    BANK_TRANSFER(
        "Bank Transfer"
    ),

    PAYBILL(
        "Paybill"
    ),

    TILL_NUMBER(
        "Till Number"
    ),

    BUSINESS_NUMBER(
        "Business Number"
    ),

    BANK_ACCOUNT(
        "Bank Account"
    )
}


/*
 * =============================================================
 * MOBILE MONEY DETAILS
 * =============================================================
 */

data class MobileMoneyDetails(

    /*
     * Example:
     *
     * 0712345678
     */

    val phoneNumber: String = "",

    val accountName: String? = null,

    val network:
    MobileMoneyNetwork =
        MobileMoneyNetwork.MPESA
)


/*
 * =============================================================
 * MOBILE MONEY NETWORK
 * =============================================================
 */

enum class MobileMoneyNetwork(

    val displayName: String

) {

    MPESA(
        "M-Pesa"
    ),

    AIRTEL(
        "Airtel Money"
    )
}


/*
 * =============================================================
 * BUSINESS PAYMENT DETAILS
 * =============================================================
 *
 * Used for:
 *
 * - Paybill
 * - Till Number
 * - Business Number
 *
 * =============================================================
 */

data class BusinessPaymentDetails(

    val businessNumber: String? = null,

    val paybillNumber: String? = null,

    val tillNumber: String? = null,

    val accountNumberRequired:
    Boolean = false,

    val accountNumberLabel:
    String = "Account Number",

    val accountNumberExample:
    String? = null,

    val businessName:
    String? = null
)


/*
 * =============================================================
 * BANK ACCOUNT DETAILS
 * =============================================================
 */

data class BankAccountDetails(

    val bankName: String = "",

    val branchName: String? = null,

    val branchCode: String? = null,

    val accountName: String = "",

    val accountNumber: String = "",

    val swiftCode: String? = null,

    val iban: String? = null
)


/*
 * =============================================================
 * PAYMENT GATEWAY DETAILS
 * =============================================================
 *
 * Used for gateways such as Pesapal and card processing.
 *
 * Do NOT store secret API keys inside this model or inside the
 * Android application.
 *
 * Secrets belong on the backend.
 *
 * =============================================================
 */

data class PaymentGatewayDetails(

    val provider:
    PaymentGatewayProvider =
        PaymentGatewayProvider.PESAPAL,

    val merchantReference:
    String? = null,

    val checkoutUrl:
    String? = null,

    val supportedCards:
    List<CardType> =
        emptyList()
)


/*
 * =============================================================
 * PAYMENT GATEWAY PROVIDER
 * =============================================================
 */

enum class PaymentGatewayProvider(

    val displayName: String

) {

    PESAPAL(
        "Pesapal"
    ),

    MASTERCARD(
        "Mastercard"
    ),

    VISA(
        "Visa"
    )
}


/*
 * =============================================================
 * CARD TYPE
 * =============================================================
 */

enum class CardType(

    val displayName: String

) {

    MASTERCARD(
        "Mastercard"
    ),

    VISA(
        "Visa"
    )
}


/*
 * =============================================================
 * PAYMENT METHOD VERIFICATION
 * =============================================================
 */

data class PaymentMethodVerification(

    val status:
    PaymentMethodVerificationStatus =
        PaymentMethodVerificationStatus.PENDING,

    val verified: Boolean = false,

    val verifiedBy: String? = null,

    val verifiedAt: String? = null,

    val verificationNote: String? = null
)


/*
 * =============================================================
 * VERIFICATION STATUS
 * =============================================================
 */

enum class PaymentMethodVerificationStatus(

    val displayName: String

) {

    PENDING(
        "Pending"
    ),

    VERIFIED(
        "Verified"
    ),

    REJECTED(
        "Rejected"
    ),

    SUSPENDED(
        "Suspended"
    )
}


/*
 * =============================================================
 * PAYMENT FEE CONFIGURATION
 * =============================================================
 */

data class PaymentFeeConfiguration(

    /*
     * Whether the tenant or landlord pays the processing fee.
     */

    val feePaidBy:
    PaymentFeePayer =
        PaymentFeePayer.TENANT,

    /*
     * Fixed fee in KES.
     */

    val fixedFee:
    Double = 0.0,

    /*
     * Percentage fee.
     *
     * Example:
     *
     * 1.5 = 1.5%
     */

    val percentageFee:
    Double = 0.0,

    /*
     * Whether the fee is displayed before confirmation.
     */

    val showBeforePayment:
    Boolean = true
)


/*
 * =============================================================
 * PAYMENT FEE PAYER
 * =============================================================
 */

enum class PaymentFeePayer(

    val displayName: String

) {

    TENANT(
        "Tenant"
    ),

    LANDLORD(
        "Landlord"
    ),

    SHARED(
        "Shared"
    )
}
