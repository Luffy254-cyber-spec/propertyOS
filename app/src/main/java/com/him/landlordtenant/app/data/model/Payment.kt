package com.him.landlordtenant.app.data.model

import com.him.landlordtenant.app.enums.PaymentMethod

/**
 * =============================================================
 * PAYMENT MODEL
 * =============================================================
 *
 * Represents a financial transaction made by a tenant or other
 * authorized payer.
 *
 * PAYMENT FLOW
 *
 * Tenant
 *   ↓
 * Select Bill
 *   ↓
 * Pay
 *   ↓
 * Select Payment Method
 *   ↓
 * Enter / Confirm Phone Number
 *   ↓
 * Payment Request
 *   ↓
 * Provider Processing
 *   ↓
 * SUCCESS / FAILED / CANCELLED
 *   ↓
 * Update Bill
 *   ↓
 * Generate Receipt
 *   ↓
 * Notify Tenant + Landlord
 *
 * =============================================================
 */

data class Payment(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val paymentReference: String = "",

    val transactionReference: String? = null,

    val providerTransactionId: String? = null,

    /*
     * ---------------------------------------------------------
     * PEOPLE
     * ---------------------------------------------------------
     */

    val tenantId: String? = null,

    val landlordId: String = "",

    val apartmentId: String = "",

    val houseId: String = "",

    /*
     * ---------------------------------------------------------
     * BILL
     * ---------------------------------------------------------
     */

    val billId: String? = null,

    val billNumber: String? = null,

    /*
     * ---------------------------------------------------------
     * PAYMENT DETAILS
     * ---------------------------------------------------------
     */

    val amount: Double = 0.0,

    val currency: String = "KES",

    val paymentType:
    PaymentType = PaymentType.RENT,

    val paymentMethod:
    PaymentMethod = PaymentMethod.MPESA,

    /*
     * ---------------------------------------------------------
     * PAYMENT PROVIDER
     * ---------------------------------------------------------
     */

    val provider:
    PaymentProvider = PaymentProvider.MPESA,

    /*
     * ---------------------------------------------------------
     * PAYER INFORMATION
     * ---------------------------------------------------------
 */

    val payerName: String? = null,

    val payerPhoneNumber: String? = null,

    val payerEmail: String? = null,

    /*
     * ---------------------------------------------------------
     * DESTINATION
     * ---------------------------------------------------------
 */

    val destination:
    PaymentDestination? = null,

    /*
     * ---------------------------------------------------------
     * PAYMENT STATUS
     * ---------------------------------------------------------
 */

    val status:
    PaymentStatus = PaymentStatus.INITIATED,

    /*
     * ---------------------------------------------------------
     * PROVIDER RESPONSE
     * ---------------------------------------------------------
 */

    val providerResponseCode: String? = null,

    val providerResponseMessage: String? = null,

    val providerReceiptNumber: String? = null,

    /*
     * ---------------------------------------------------------
     * PAYMENT REQUEST
     * ---------------------------------------------------------
 */

    val initiatedAt: String? = null,

    val processingAt: String? = null,

    val completedAt: String? = null,

    val failedAt: String? = null,

    val cancelledAt: String? = null,

    /*
     * ---------------------------------------------------------
     * FAILURE INFORMATION
     * ---------------------------------------------------------
 */

    val failureReason: String? = null,

    val failureCode: String? = null,

    /*
     * ---------------------------------------------------------
     * SECURITY / VERIFICATION
     * ---------------------------------------------------------
 */

    val verificationStatus:
    PaymentVerificationStatus =
        PaymentVerificationStatus.PENDING,

    val verifiedAt: String? = null,

    /*
     * ---------------------------------------------------------
     * RECEIPT
     * ---------------------------------------------------------
 */

    val receiptId: String? = null,

    /*
     * ---------------------------------------------------------
     * REFUND
     * ---------------------------------------------------------
 */

    val refund:
    RefundInformation? = null,

    /*
     * ---------------------------------------------------------
     * NOTES
     * ---------------------------------------------------------
 */

    val description: String? = null,

    val notes: String? = null,

    /*
     * ---------------------------------------------------------
     * AUDIT
     * ---------------------------------------------------------
 */

    val auditEvents:
    List<PaymentAuditEvent> = emptyList(),

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

    val createdAt: String? = null,

    val updatedAt: String? = null,

) {

    /*
     * =========================================================
     * COMPUTED PROPERTIES
     * =========================================================
     */

    /**
     * Whether payment succeeded.
     */

    val isSuccessful: Boolean
        get() =
            status == PaymentStatus.SUCCESS


    /**
     * Whether payment failed.
     */

    val isFailed: Boolean
        get() =
            status == PaymentStatus.FAILED


    /**
     * Whether payment is still processing.
     */

    val isProcessing: Boolean
        get() =
            status == PaymentStatus.INITIATED ||
                    status == PaymentStatus.PROCESSING


    /**
     * Whether payment can be retried.
     */

    val canRetry: Boolean
        get() =
            status == PaymentStatus.FAILED ||
                    status == PaymentStatus.CANCELLED


    /**
     * Whether payment has been verified.
     */

    val isVerified: Boolean
        get() =
            verificationStatus ==
                    PaymentVerificationStatus.VERIFIED


    /**
     * Whether a receipt should be generated.
     */

    val shouldGenerateReceipt: Boolean
        get() =
            isSuccessful &&
                    isVerified &&
                    receiptId == null
}


/*
 * =============================================================
 * PAYMENT TYPE
 * =============================================================
 */

enum class PaymentType(

    val displayName: String

) {

    RENT(
        "Rent"
    ),

    SECURITY_DEPOSIT(
        "Security Deposit"
    ),

    WATER(
        "Water Bill"
    ),

    ELECTRICITY(
        "Electricity Bill"
    ),

    GARBAGE(
        "Garbage Bill"
    ),

    INTERNET(
        "Internet Bill"
    ),

    SERVICE_CHARGE(
        "Service Charge"
    ),

    MAINTENANCE(
        "Maintenance"
    ),

    PENALTY(
        "Penalty"
    ),

    OTHER_BILL(
        "Other Bill"
    ),

    MULTIPLE_BILLS(
        "Multiple Bills"
    )
}


/*
 * =============================================================
 * PAYMENT STATUS
 * =============================================================
 */

enum class PaymentStatus(

    val displayName: String

) {

    INITIATED(
        "Initiated"
    ),

    PROCESSING(
        "Processing"
    ),

    SUCCESS(
        "Successful"
    ),

    FAILED(
        "Failed"
    ),

    CANCELLED(
        "Cancelled"
    ),

    EXPIRED(
        "Expired"
    ),

    REVERSED(
        "Reversed"
    ),

    REFUND_PENDING(
        "Refund Pending"
    ),

    REFUNDED(
        "Refunded"
    ),

    DISPUTED(
        "Disputed"
    )
}




/*
 * =============================================================
 * PAYMENT VERIFICATION
 * =============================================================
 *
 * Important for protecting against fake payment confirmations.
 *
 * The client should NOT simply assume:
 *
 * "Payment button clicked = payment successful."
 *
 * The backend/payment provider must verify the transaction.
 *
 * =============================================================
 */

enum class PaymentVerificationStatus(

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

    MANUAL_REVIEW(
        "Manual Review"
    )
}


/*
 * =============================================================
 * REFUND INFORMATION
 * =============================================================
 */

data class RefundInformation(

    val refundId: String = "",

    val amount: Double = 0.0,

    val reason: String = "",

    val status:
    RefundStatus = RefundStatus.PENDING,

    val requestedAt: String? = null,

    val processedAt: String? = null,

    val providerReference: String? = null
)




/*
 * =============================================================
 * PAYMENT AUDIT EVENT
 * =============================================================
 */

data class PaymentAuditEvent(

    val id: String = "",

    val paymentId: String = "",

    val event:
    PaymentAuditEventType =
        PaymentAuditEventType.CREATED,

    val description: String = "",

    val actorUserId: String? = null,

    val actorName: String? = null,

    val timestamp: String? = null,

    val providerReference: String? = null
)


/*
 * =============================================================
 * PAYMENT AUDIT EVENT TYPE
 * =============================================================
 */

enum class PaymentAuditEventType(

    val displayName: String

) {

    CREATED(
        "Payment Created"
    ),

    INITIATED(
        "Payment Initiated"
    ),

    PROCESSING(
        "Payment Processing"
    ),

    SUCCESS(
        "Payment Successful"
    ),

    FAILED(
        "Payment Failed"
    ),

    CANCELLED(
        "Payment Cancelled"
    ),

    EXPIRED(
        "Payment Expired"
    ),

    VERIFIED(
        "Payment Verified"
    ),

    RECEIPT_GENERATED(
        "Receipt Generated"
    ),

    REVERSED(
        "Payment Reversed"
    ),

    REFUND_REQUESTED(
        "Refund Requested"
    ),

    REFUND_COMPLETED(
        "Refund Completed"
    ),

    DISPUTED(
        "Payment Disputed"
    )
}
