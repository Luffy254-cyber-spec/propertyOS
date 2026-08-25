package com.him.landlordtenant.app.data.model

import com.him.landlordtenant.app.enums.PaymentMethod

/**
 * =============================================================
 * RECEIPT MODEL
 * =============================================================
 *
 * Represents an official payment receipt generated after a
 * successful and verified payment.
 *
 * A receipt should be generated from a VERIFIED payment.
 *
 * =============================================================
 *
 * PAYMENT
 *    ↓
 * SUCCESS
 *    ↓
 * VERIFIED
 *    ↓
 * RECEIPT GENERATED
 *    ↓
 * Tenant receives receipt
 *    ↓
 * Landlord receives receipt record
 *
 * =============================================================
 */

data class Receipt(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val receiptNumber: String = "",

    val paymentId: String = "",

    val paymentReference: String = "",

    val transactionReference: String? = null,

    /*
     * ---------------------------------------------------------
     * PROPERTY
     * ---------------------------------------------------------
     */

    val apartmentId: String = "",

    val apartmentName: String = "",

    val floorId: String? = null,

    val floorNumber: Int? = null,

    val houseId: String = "",

    val houseNumber: String = "",

    /*
     * ---------------------------------------------------------
     * TENANT
     * ---------------------------------------------------------
     */

    val tenantId: String = "",

    val tenantName: String = "",

    val tenantPhoneNumber: String? = null,

    val tenantEmail: String? = null,

    /*
     * ---------------------------------------------------------
     * LANDLORD
     * ---------------------------------------------------------
 */

    val landlordId: String = "",

    val landlordName: String = "",

    val landlordPhoneNumber: String? = null,

    val landlordEmail: String? = null,

    /*
     * ---------------------------------------------------------
     * PAYMENT
     * ---------------------------------------------------------
 */

    val paymentMethod:
    PaymentMethod = PaymentMethod.MPESA,

    val paymentProvider:
    PaymentProvider = PaymentProvider.MPESA,

    val paymentProviderReceiptNumber:
    String? = null,

    /*
     * ---------------------------------------------------------
     * AMOUNT
     * ---------------------------------------------------------
 */

    val amountPaid: Double = 0.0,

    val currency: String = "KES",

    /*
     * ---------------------------------------------------------
     * BILL BREAKDOWN
     * ---------------------------------------------------------
 */

    val items:
    List<ReceiptItem> = emptyList(),

    /*
     * ---------------------------------------------------------
     * TOTALS
     * ---------------------------------------------------------
 */

    val subtotal: Double = 0.0,

    val discounts: Double = 0.0,

    val penalties: Double = 0.0,

    val taxes: Double = 0.0,

    val totalAmount: Double = 0.0,

    /*
     * ---------------------------------------------------------
     * RECEIPT STATUS
     * ---------------------------------------------------------
 */

    val status:
    ReceiptStatus = ReceiptStatus.GENERATED,

    /*
     * ---------------------------------------------------------
     * VERIFICATION
     * ---------------------------------------------------------
 */

    val verification:
    ReceiptVerification =
        ReceiptVerification(),

    /*
     * ---------------------------------------------------------
     * DOCUMENT
     * ---------------------------------------------------------
 */

    val pdfUrl: String? = null,

    val documentHash: String? = null,

    /*
     * ---------------------------------------------------------
     * DELIVERY
     * ---------------------------------------------------------
 */

    val delivery:
    ReceiptDelivery =
        ReceiptDelivery(),

    /*
     * ---------------------------------------------------------
     * NOTES
     * ---------------------------------------------------------
 */

    val notes: String? = null,

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

    val generatedAt: String? = null,

    val issuedAt: String? = null,

    val createdAt: String? = null,

    val updatedAt: String? = null
) {

    /*
     * =========================================================
     * COMPUTED PROPERTIES
     * =========================================================
     */

    /**
     * Whether this receipt has been verified.
     */

    val isVerified: Boolean
        get() =
            verification.status ==
                    ReceiptVerificationStatus.VERIFIED


    /**
     * Whether the receipt can be downloaded.
     */

    val canDownload: Boolean
        get() =
            !pdfUrl.isNullOrBlank()


    /**
     * Number of individual charges.
     */

    val itemCount: Int
        get() =
            items.size


    /**
     * Whether the receipt has been delivered.
     */

    val hasBeenDelivered: Boolean
        get() =
            delivery.tenantDelivered ||
                    delivery.landlordDelivered
}


/*
 * =============================================================
 * RECEIPT ITEM
 * =============================================================
 *
 * Represents individual charges included in a receipt.
 *
 * Example:
 *
 * Rent       KES 15,000
 * Water      KES    850
 * Garbage    KES    500
 *
 * Total      KES 16,350
 *
 * =============================================================
 */

data class ReceiptItem(

    val id: String = "",

    val billId: String? = null,

    val billNumber: String? = null,

    val description: String = "",

    val type:
    BillType = BillType.OTHER,

    val quantity: Double = 1.0,

    val unitPrice: Double = 0.0,

    val amount: Double = 0.0,

    val billingPeriod: String? = null
)


/*
 * =============================================================
 * RECEIPT STATUS
 * =============================================================
 */

enum class ReceiptStatus(

    val displayName: String

) {

    GENERATED(
        "Generated"
    ),

    VERIFIED(
        "Verified"
    ),

    SENT(
        "Sent"
    ),

    DOWNLOADED(
        "Downloaded"
    ),

    CANCELLED(
        "Cancelled"
    ),

    REPLACED(
        "Replaced"
    )
}


/*
 * =============================================================
 * RECEIPT VERIFICATION
 * =============================================================
 *
 * Allows a receipt to be verified independently.
 *
 * Example:
 *
 * Receipt number:
 *     RCP-2026-000123
 *
 * Verification code:
 *     ABC123XYZ
 *
 * =============================================================
 */

data class ReceiptVerification(

    val status:
    ReceiptVerificationStatus =
        ReceiptVerificationStatus.PENDING,

    val verificationCode: String? = null,

    val verificationUrl: String? = null,

    val verifiedAt: String? = null,

    val verifiedBy: String? = null
)


/*
 * =============================================================
 * RECEIPT VERIFICATION STATUS
 * =============================================================
 */

enum class ReceiptVerificationStatus(

    val displayName: String

) {

    PENDING(
        "Verification Pending"
    ),

    VERIFIED(
        "Verified"
    ),

    FAILED(
        "Verification Failed"
    ),

    REVOKED(
        "Revoked"
    )
}


/*
 * =============================================================
 * RECEIPT DELIVERY
 * =============================================================
 *
 * Keeps track of where the receipt has been delivered.
 * =============================================================
 */

data class ReceiptDelivery(

    /*
     * Tenant
     */

    val tenantDelivered: Boolean = false,

    val tenantDeliveredAt: String? = null,

    /*
     * Landlord
     */

    val landlordDelivered: Boolean = false,

    val landlordDeliveredAt: String? = null,

    /*
     * Email
     */

    val emailDelivered: Boolean = false,

    val emailDeliveredAt: String? = null,

    /*
     * SMS
     */

    val smsDelivered: Boolean = false,

    val smsDeliveredAt: String? = null,

    /*
     * In-app notification
     */

    val notificationDelivered: Boolean = false,

    val notificationDeliveredAt: String? = null
)