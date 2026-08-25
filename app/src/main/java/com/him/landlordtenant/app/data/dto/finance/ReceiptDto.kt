package com.him.landlordtenant.app.data.dto.finance

import kotlinx.serialization.Serializable

/**
 * =============================================================
 * RECEIPT DTO
 * =============================================================
 *
 * Official proof that a payment was received.
 *
 * Supports:
 * - Rent receipts
 * - Deposit receipts
 * - Utility receipts
 * - Service-charge receipts
 * - Maintenance receipts
 * - Partial-payment receipts
 * - Downloadable/printable receipts
 *
 * =============================================================
 */

@Serializable
data class ReceiptDto(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val receiptNumber: String = "",

    val paymentId: String = "",

    val transactionId: String? = null,

    val billId: String? = null,

    /*
     * ---------------------------------------------------------
     * PARTIES
     * ---------------------------------------------------------
     */

    val tenantId: String = "",

    val landlordId: String? = null,

    /*
     * ---------------------------------------------------------
     * PROPERTY
     * ---------------------------------------------------------
     */

    val propertyId: String? = null,

    val apartmentId: String? = null,

    val houseId: String? = null,

    val agreementId: String? = null,

    /*
     * ---------------------------------------------------------
     * PAYMENT INFORMATION
     * ---------------------------------------------------------
     */

    val paymentType: String = "RENT",

    val paymentMethod: String = "MPESA",

    val provider: String? = null,

    val providerReference: String? = null,

    /*
     * ---------------------------------------------------------
     * AMOUNT
     * ---------------------------------------------------------
     */

    val amount: Double = 0.0,

    val currency: String = "KES",

    val amountInWords: String? = null,

    /*
     * ---------------------------------------------------------
     * ALLOCATION
     * ---------------------------------------------------------
     */

    val allocatedTo: String? = null,

    val allocationDescription: String? = null,

    val billBalanceBeforePayment: Double = 0.0,

    val billBalanceAfterPayment: Double = 0.0,

    /*
     * ---------------------------------------------------------
     * RECEIPT CONTENT
     * ---------------------------------------------------------
 */

    val title: String = "Payment Receipt",

    val description: String? = null,

    val notes: String? = null,

    /*
     * ---------------------------------------------------------
     * TENANT DETAILS
     * ---------------------------------------------------------
 */

    val tenantName: String? = null,

    val tenantPhoneMasked: String? = null,

    val tenantEmail: String? = null,

    /*
     * ---------------------------------------------------------
     * LANDLORD DETAILS
     * ---------------------------------------------------------
 */

    val landlordName: String? = null,

    val landlordBusinessName: String? = null,

    val landlordPhoneMasked: String? = null,

    /*
     * ---------------------------------------------------------
     * PROPERTY DETAILS
     * ---------------------------------------------------------
 */

    val propertyName: String? = null,

    val houseNumber: String? = null,

    val propertyAddress: String? = null,

    /*
     * ---------------------------------------------------------
     * DOCUMENT
     * ---------------------------------------------------------
 */

    val pdfUrl: String? = null,

    val documentStoragePath: String? = null,

    val documentHash: String? = null,

    val qrCodeData: String? = null,

    /*
     * ---------------------------------------------------------
     * VERIFICATION
     * ---------------------------------------------------------
 */

    val verificationCode: String? = null,

    val verified: Boolean = false,

    val verifiedAt: String? = null,

    /*
     * ---------------------------------------------------------
     * DELIVERY
     * ---------------------------------------------------------
 */

    val emailed: Boolean = false,

    val emailedAt: String? = null,

    val smsSent: Boolean = false,

    val smsSentAt: String? = null,

    val notificationSent: Boolean = false,

    val notificationSentAt: String? = null,

    /*
     * ---------------------------------------------------------
     * RECEIPT STATUS
     * ---------------------------------------------------------
 */

    val status: String = "ISSUED",

    val voided: Boolean = false,

    val voidReason: String? = null,

    val voidedAt: String? = null,

    val voidedBy: String? = null,

    /*
     * ---------------------------------------------------------
     * TIMESTAMPS
     * ---------------------------------------------------------
 */

    val paymentDate: String? = null,

    val issuedAt: String? = null,

    val createdAt: String? = null,

    val updatedAt: String? = null
) {

    /*
     * ---------------------------------------------------------
     * COMPUTED VALUES
     * ---------------------------------------------------------
 */

    val isValid: Boolean
        get() =
            status.uppercase() == "ISSUED" &&
                    !voided

    val hasPdf: Boolean
        get() =
            !pdfUrl.isNullOrBlank()

    val hasQrCode: Boolean
        get() =
            !qrCodeData.isNullOrBlank()

    val hasVerificationCode: Boolean
        get() =
            !verificationCode.isNullOrBlank()

    val balanceRemaining: Double
        get() =
            billBalanceAfterPayment
                .coerceAtLeast(0.0)
}