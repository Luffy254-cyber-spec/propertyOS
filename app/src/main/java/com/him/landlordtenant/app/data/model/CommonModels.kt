package com.him.landlordtenant.app.data.model

enum class HouseType(val displayName: String) {
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
    OTHER("Other")
}

enum class VerificationStatus(val displayName: String) {
    NOT_SUBMITTED("Not Submitted"),
    PENDING("Pending Verification"),
    UNDER_REVIEW("Under Review"),
    VERIFIED("Verified"),
    REJECTED("Rejected"),
    SUSPENDED("Suspended"),
    EXPIRED("Verification Expired")
}

enum class HouseCondition(val displayName: String) {
    NEW("New"),
    EXCELLENT("Excellent"),
    GOOD("Good"),
    FAIR("Fair"),
    NEEDS_REPAIRS("Needs Repairs"),
    NEEDS_PAINTING("Needs Painting"),
    NEEDS_MAJOR_REPAIRS("Needs Major Repairs"),
    UNINHABITABLE("Uninhabitable"),
    DAMAGED("Damaged"),
    NEEDS_CLEANING("Needs Cleaning")
}

enum class HouseStatus(val displayName: String) {
    VACANT("Vacant"),
    OCCUPIED("Occupied"),
    NOT_READY("Not Ready"),
    UNDER_MAINTENANCE("Under Maintenance"),
    RESERVED("Reserved"),
    PENDING_MOVE_IN("Pending Move-In"),
    BLOCKED("Blocked"),
    ARCHIVED("Archived")
}

enum class PaymentProvider(val displayName: String) {
    MPESA("M-Pesa"),
    AIRTEL_MONEY("Airtel Money"),
    PESAPAL("Pesapal"),
    MASTERCARD("Mastercard"),
    VISA("Visa"),
    BANK("Bank"),
    CASH("Cash"),
    OTHER("Other")
}

enum class RefundStatus(val displayName: String) {
    NOT_REQUESTED("Not Requested"),
    PENDING("Pending"),
    REQUESTED("Requested"),
    PROCESSING("Processing"),
    COMPLETED("Completed"),
    SUCCESS("Refunded"),
    FAILED("Failed"),
    CANCELLED("Cancelled")
}

enum class PaymentTransactionStatus(val displayName: String) {
    INITIATED("Initiated"),
    PENDING("Pending"),
    PROCESSING("Processing"),
    SUCCESS("Successful"),
    FAILED("Failed"),
    CANCELLED("Cancelled"),
    REVERSED("Reversed"),
    REFUND_PENDING("Refund Pending"),
    REFUNDED("Refunded")
}

enum class TaskPriority(val displayName: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    URGENT("Urgent"),
    EMERGENCY("Emergency")
}

/*
 * =============================================================
 * PAYMENT DESTINATION
 * =============================================================
 */

data class PaymentDestination(
    val type: PaymentDestinationType = PaymentDestinationType.PAYBILL,
    val name: String? = null,
    val businessName: String? = null,
    val phoneNumber: String? = null,
    val paybillNumber: String? = null,
    val tillNumber: String? = null,
    val businessNumber: String? = null,
    val accountNumber: String? = null,
    val bankName: String? = null,
    val bankAccountName: String? = null,
    val bankAccountNumber: String? = null,
    val branchName: String? = null
)

/*
 * =============================================================
 * DESTINATION TYPE
 * =============================================================
 */

enum class PaymentDestinationType(
    val displayName: String
) {
    PHONE_NUMBER("Phone Number"),
    PAYBILL("Paybill"),
    TILL("Buy Goods Till"),
    BUSINESS_NUMBER("Business Number"),
    BANK_ACCOUNT("Bank Account"),
    PESAPAL_ACCOUNT("Pesapal Account"),
    ONLINE_GATEWAY("Online Gateway")
}
