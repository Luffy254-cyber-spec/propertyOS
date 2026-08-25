package com.him.landlordtenant.app.data.model

/**
 * =============================================================
 * PAYMENT TRANSACTION MODEL
 * =============================================================
 *
 * Represents an actual financial transaction made through the
 * application's payment system.
 *
 * Examples:
 *
 * - Monthly rent
 * - Security deposit
 * - Water bill
 * - Garbage collection
 * - Electricity
 * - Maintenance charges
 * - Other apartment charges
 *
 * =============================================================
 *
 * PAYMENT FLOW
 *
 * Tenant
 *   ↓
 * Select Bill
 *   ↓
 * Pay Now
 *   ↓
 * PaymentTransaction CREATED
 *   ↓
 * Payment Provider
 *   ↓
 * PENDING
 *   ↓
 * SUCCESS / FAILED / CANCELLED
 *   ↓
 * Receipt Generated
 *   ↓
 * Bill Updated
 *
 * =============================================================
 */

data class PaymentTransaction(

    /*
     * ---------------------------------------------------------
     * TRANSACTION IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val transactionReference: String = "",

    /*
     * Provider-generated reference.
     *
     * Examples:
     *
     * M-Pesa receipt number
     * Airtel transaction ID
     * Pesapal reference
     * Card transaction reference
     */

    val providerReference: String? = null,

    /*
     * ---------------------------------------------------------
     * USER INFORMATION
     * ---------------------------------------------------------
     */

    val tenantId: String = "",

    val tenantName: String? = null,

    val landlordId: String = "",

    val landlordName: String? = null,

    /*
     * ---------------------------------------------------------
     * PROPERTY INFORMATION
     * ---------------------------------------------------------
     */

    val apartmentId: String? = null,

    val apartmentName: String? = null,

    val floorId: String? = null,

    val floorNumber: Int? = null,

    val houseId: String? = null,

    val houseNumber: String? = null,

    /*
     * ---------------------------------------------------------
     * BILL INFORMATION
     * ---------------------------------------------------------
     */

    val billId: String? = null,

    val billType:
    TransactionBillType =
        TransactionBillType.OTHER,

    val billDescription: String? = null,

    /*
     * ---------------------------------------------------------
     * PAYMENT METHOD
     * ---------------------------------------------------------
 */

    val paymentMethodId: String? = null,

    val paymentMethod:
    PaymentMethodType =
        PaymentMethodType.MPESA,

    /*
     * ---------------------------------------------------------
     * PAYMENT PROVIDER
     * ---------------------------------------------------------
 */

    val provider:
    PaymentProvider =
        PaymentProvider.MPESA,

    /*
     * ---------------------------------------------------------
     * AMOUNT
     * ---------------------------------------------------------
 */

    val amount: Double = 0.0,

    val currency: String = "KES",

    /*
     * Amount paid by the tenant.
     */

    val paidAmount: Double = 0.0,

    /*
     * Processing fee.
     */

    val processingFee: Double = 0.0,

    /*
     * Total amount charged.
     */

    val totalAmount: Double = 0.0,

    /*
     * ---------------------------------------------------------
     * PAYMENT STATUS
     * ---------------------------------------------------------
 */

    val status:
    PaymentTransactionStatus =
        PaymentTransactionStatus.INITIATED,

    /*
     * ---------------------------------------------------------
     * PAYMENT CHANNEL
     * ---------------------------------------------------------
 */

    val channel:
    PaymentChannel =
        PaymentChannel.MOBILE_MONEY,

    /*
     * ---------------------------------------------------------
     * CUSTOMER PAYMENT DETAILS
     * ---------------------------------------------------------
 *
 * Only non-sensitive information should be stored.
 *
 * NEVER store:
 *
 * - M-Pesa PIN
 * - Card CVV
 * - Full card number
 * - Passwords
 *
 * ---------------------------------------------------------
 */

    val payerPhoneNumber: String? = null,

    val payerEmail: String? = null,

    /*
     * Masked card number if applicable.
     *
     * Example:
     *
     * **** **** **** 1234
     */

    val maskedCardNumber: String? = null,

    /*
     * ---------------------------------------------------------
     * DESTINATION INFORMATION
     * ---------------------------------------------------------
 */

    val destination:
    PaymentDestination? = null,

    /*
     * ---------------------------------------------------------
     * CALLBACK INFORMATION
     * ---------------------------------------------------------
 */

    val callback:
    PaymentCallback? = null,

    /*
     * ---------------------------------------------------------
     * FAILURE INFORMATION
     * ---------------------------------------------------------
 */

    val failure:
    PaymentFailure? = null,

    /*
     * ---------------------------------------------------------
     * RECONCILIATION
     * ---------------------------------------------------------
 */

    val reconciliation:
    PaymentReconciliation =
        PaymentReconciliation(),

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
    PaymentRefund? = null,

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

    val initiatedAt: String? = null,

    val processingAt: String? = null,

    val completedAt: String? = null,

    val failedAt: String? = null,

    val cancelledAt: String? = null,

    val updatedAt: String? = null,
) {

    /*
     * =========================================================
     * COMPUTED PROPERTIES
     * =========================================================
     */

    val isSuccessful: Boolean
        get() =
            status ==
                    PaymentTransactionStatus.SUCCESS

    val isPending: Boolean
        get() =
            status ==
                    PaymentTransactionStatus.PENDING ||
                    status ==
                    PaymentTransactionStatus.PROCESSING

    val isFailed: Boolean
        get() =
            status ==
                    PaymentTransactionStatus.FAILED

    val isCancelled: Boolean
        get() =
            status ==
                    PaymentTransactionStatus.CANCELLED

    val isRefunded: Boolean
        get() =
            status ==
                    PaymentTransactionStatus.REFUNDED

    val hasReceipt: Boolean
        get() =
            !receiptId.isNullOrBlank()

    val hasProviderReference: Boolean
        get() =
            !providerReference.isNullOrBlank()
}


/*
 * =============================================================
 * PAYMENT CHANNEL
 * =============================================================
 */

enum class PaymentChannel(

    val displayName: String

) {

    MOBILE_MONEY(
        "Mobile Money"
    ),

    CARD(
        "Card"
    ),

    BANK_TRANSFER(
        "Bank Transfer"
    ),

    PAYBILL(
        "Paybill"
    ),

    TILL(
        "Till Number"
    ),

    ONLINE_GATEWAY(
        "Online Gateway"
    )
}


/*
 * =============================================================
 * TRANSACTION BILL TYPE
 * =============================================================
 */

enum class TransactionBillType(

    val displayName: String

) {

    RENT(
        "Rent"
    ),

    SECURITY_DEPOSIT(
        "Security Deposit"
    ),

    WATER(
        "Water"
    ),

    ELECTRICITY(
        "Electricity"
    ),

    GARBAGE(
        "Garbage Collection"
    ),

    INTERNET(
        "Internet"
    ),

    MAINTENANCE(
        "Maintenance"
    ),

    SERVICE_CHARGE(
        "Service Charge"
    ),

    PENALTY(
        "Penalty"
    ),

    OTHER(
        "Other"
    )
}




/*
 * =============================================================
 * PAYMENT CALLBACK
 * =============================================================
 *
 * Backend/payment-provider callback information.
 * =============================================================
 */

data class PaymentCallback(

    val received: Boolean = false,

    val callbackReference: String? = null,

    val callbackStatus: String? = null,

    val callbackMessage: String? = null,

    val receivedAt: String? = null
)


/*
 * =============================================================
 * PAYMENT FAILURE
 * =============================================================
 */

data class PaymentFailure(

    val code: String? = null,

    val reason: String? = null,

    val message: String? = null,

    val retryAllowed: Boolean = false,

    val failedAt: String? = null
)


/*
 * =============================================================
 * PAYMENT RECONCILIATION
 * =============================================================
 *
 * Used by landlords/property managers to determine whether
 * the transaction has been successfully matched against the
 * expected bill.
 *
 * =============================================================
 */

data class PaymentReconciliation(

    val status:
    ReconciliationStatus =
        ReconciliationStatus.NOT_RECONCILED,

    val reconciledBy: String? = null,

    val reconciledAt: String? = null,

    val reconciliationReference: String? = null,

    val notes: String? = null
)


/*
 * =============================================================
 * RECONCILIATION STATUS
 * =============================================================
 */

enum class ReconciliationStatus(

    val displayName: String

) {

    NOT_RECONCILED(
        "Not Reconciled"
    ),

    AUTOMATICALLY_RECONCILED(
        "Automatically Reconciled"
    ),

    MANUALLY_RECONCILED(
        "Manually Reconciled"
    ),

    DISPUTED(
        "Disputed"
    ),

    REQUIRES_REVIEW(
        "Requires Review"
    )
}


/*
 * =============================================================
 * REFUND
 * =============================================================
 */

data class PaymentRefund(

    val requested: Boolean = false,

    val refundReference: String? = null,

    val amount: Double = 0.0,

    val reason: String? = null,

    val status:
    RefundStatus =
        RefundStatus.NOT_REQUESTED,

    val requestedAt: String? = null,

    val completedAt: String? = null
)


