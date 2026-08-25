package com.him.landlordtenant.app.data.model

import com.him.landlordtenant.app.enums.PaymentMethod

/**
 * =============================================================
 * BILL MODEL
 * =============================================================
 *
 * Represents one financial bill/charge assigned to a tenant,
 * house, or apartment.
 *
 * Examples:
 *
 * Rent
 * Water
 * Electricity
 * Garbage
 * Internet
 * Service Charge
 * Security Charge
 * Deposit
 * Penalty
 * Other
 *
 * =============================================================
 *
 * BILL LIFECYCLE
 *
 * LANDLORD
 *     ↓
 * Create Bill
 *     ↓
 * Assign to House/Tenant
 *     ↓
 * Set Amount + Due Date
 *     ↓
 * BILL GENERATED
 *     ↓
 * Tenant notified
 *     ↓
 * Tenant pays
 *     ↓
 * PAYMENT
 *     ↓
 * Bill becomes PAID
 *
 * OR
 *
 * Due date passes
 *     ↓
 * OVERDUE
 *     ↓
 * ARREARS / PENALTY
 *
 * =============================================================
 */

data class Bill(

    /*
     * ---------------------------------------------------------
     * IDENTIFICATION
     * ---------------------------------------------------------
     */

    val id: String = "",

    val apartmentId: String = "",

    val floorId: String? = null,

    val houseId: String = "",

    val tenantId: String? = null,

    val landlordId: String = "",

    /*
     * ---------------------------------------------------------
     * BILL INFORMATION
     * ---------------------------------------------------------
     */

    val billNumber: String = "",

    val title: String = "",

    val description: String = "",

    val billType:
    BillType = BillType.OTHER,

    /*
     * ---------------------------------------------------------
     * BILLING PERIOD
     * ---------------------------------------------------------
     */

    val billingPeriod:
    BillingPeriod? = null,

    /*
     * ---------------------------------------------------------
     * AMOUNTS
     * ---------------------------------------------------------
     */

    val baseAmount: Double = 0.0,

    val penaltyAmount: Double = 0.0,

    val discountAmount: Double = 0.0,

    val taxAmount: Double = 0.0,

    /*
     * ---------------------------------------------------------
     * TOTAL
     * ---------------------------------------------------------
     */

    val totalAmount: Double = 0.0,

    val amountPaid: Double = 0.0,

    val amountOutstanding: Double = 0.0,

    /*
     * ---------------------------------------------------------
     * DUE DATE
     * ---------------------------------------------------------
     */

    val issueDate: String? = null,

    val dueDate: String? = null,

    /*
     * ---------------------------------------------------------
     * BILL STATUS
     * ---------------------------------------------------------
 */

    val status:
    BillStatus = BillStatus.PENDING,

    /*
     * ---------------------------------------------------------
     * PAYMENT
     * ---------------------------------------------------------
 */

    val paymentId: String? = null,

    val lastPaymentDate: String? = null,

    /*
     * ---------------------------------------------------------
     * METER INFORMATION
     * ---------------------------------------------------------
     *
     * Useful for water/electricity.
     * ---------------------------------------------------------
 */

    val meter:
    MeterReading? = null,

    /*
     * ---------------------------------------------------------
     * RECURRING BILL
     * ---------------------------------------------------------
 */

    val recurring:
    RecurringBillConfiguration? = null,

    /*
     * ---------------------------------------------------------
     * REMINDERS
     * ---------------------------------------------------------
 */

    val reminderConfiguration:
    BillReminderConfiguration =
        BillReminderConfiguration(),

    /*
     * ---------------------------------------------------------
     * PAYMENT METHOD
     * ---------------------------------------------------------
 */

    val acceptedPaymentMethods:
    List<PaymentMethod> = emptyList(),

    /*
     * ---------------------------------------------------------
     * NOTES
     * ---------------------------------------------------------
 */

    val landlordNotes: String? = null,

    val tenantNotes: String? = null,

    /*
     * ---------------------------------------------------------
     * CREATION / UPDATE
     * ---------------------------------------------------------
 */

    val createdAt: String? = null,

    val updatedAt: String? = null,

    val createdBy: String? = null,

    val updatedBy: String? = null
) {

    /*
     * =========================================================
     * COMPUTED PROPERTIES
     * =========================================================
     */

    /**
     * Calculates the expected total from the bill components.
     */

    val calculatedTotal: Double
        get() =
            baseAmount +
                    penaltyAmount +
                    taxAmount -
                    discountAmount


    /**
     * Whether the bill is completely paid.
     */

    val isPaid: Boolean
        get() =
            amountPaid >= totalAmount &&
                    totalAmount > 0.0


    /**
     * Whether there is an outstanding balance.
     */

    val hasBalance: Boolean
        get() =
            amountOutstanding > 0.0


    /**
     * Remaining amount.
     */

    val remainingAmount: Double
        get() =
            maxOf(
                totalAmount - amountPaid,
                0.0
            )


    /**
     * Whether the bill is overdue.
     */

    val isOverdue: Boolean
        get() =
            status == BillStatus.OVERDUE


    /**
     * Whether this is rent.
     */

    val isRent: Boolean
        get() =
            billType == BillType.RENT


    /**
     * Whether this bill can be paid.
     */

    val canBePaid: Boolean
        get() =
            status == BillStatus.PENDING ||
                    status == BillStatus.PARTIALLY_PAID ||
                    status == BillStatus.OVERDUE


    /**
     * Whether this bill is recurring.
     */

    val isRecurring: Boolean
        get() =
            recurring != null &&
                    recurring.enabled
}


/*
 * =============================================================
 * BILL TYPE
 * =============================================================
 */

enum class BillType(

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

    SERVICE_CHARGE(
        "Service Charge"
    ),

    SECURITY_CHARGE(
        "Security Charge"
    ),

    PARKING(
        "Parking"
    ),

    MAINTENANCE(
        "Maintenance Charge"
    ),

    LATE_PAYMENT_PENALTY(
        "Late Payment Penalty"
    ),

    OTHER(
        "Other"
    )
}


/*
 * =============================================================
 * BILL STATUS
 * =============================================================
 */

enum class BillStatus(

    val displayName: String

) {

    DRAFT(
        "Draft"
    ),

    PENDING(
        "Pending Payment"
    ),

    PARTIALLY_PAID(
        "Partially Paid"
    ),

    PAID(
        "Paid"
    ),

    OVERDUE(
        "Overdue"
    ),

    DISPUTED(
        "Disputed"
    ),

    CANCELLED(
        "Cancelled"
    ),

    WAIVED(
        "Waived"
    ),

    REFUNDED(
        "Refunded"
    )
}


/*
 * =============================================================
 * BILLING PERIOD
 * =============================================================
 */

data class BillingPeriod(

    val month: Int = 1,

    val year: Int = 1970,

    val startDate: String? = null,

    val endDate: String? = null,

    val label: String = ""
)


/*
 * =============================================================
 * METER READING
 * =============================================================
 *
 * Used for:
 *
 * - Water
 * - Electricity
 * - Other metered utilities
 *
 * Example:
 *
 * Previous: 1200
 * Current: 1250
 * Usage: 50
 *
 * =============================================================
 */

data class MeterReading(

    val meterId: String = "",

    val meterNumber: String? = null,

    val previousReading: Double = 0.0,

    val currentReading: Double = 0.0,

    val unitPrice: Double = 0.0,

    val unit:
    MeterUnit = MeterUnit.UNITS,

    val readingDate: String? = null,

    val readingTakenBy: String? = null
) {

    val usage: Double
        get() =
            maxOf(
                currentReading - previousReading,
                0.0
            )

    val calculatedCharge: Double
        get() =
            usage * unitPrice
}


/*
 * =============================================================
 * METER UNIT
 * =============================================================
 */

enum class MeterUnit(

    val displayName: String

) {

    KWH(
        "kWh"
    ),

    CUBIC_METER(
        "m³"
    ),

    LITRE(
        "Litres"
    ),

    UNITS(
        "Units"
    )
}


/*
 * =============================================================
 * RECURRING BILL CONFIGURATION
 * =============================================================
 *
 * Used for automatic monthly generation.
 *
 * Example:
 *
 * Rent
 *     Every month
 *
 * Garbage
 *     Every month
 *
 * Internet
 *     Every month
 *
 * =============================================================
 */

data class RecurringBillConfiguration(

    val enabled: Boolean = false,

    val frequency:
    BillingFrequency =
        BillingFrequency.MONTHLY,

    val dayOfMonth: Int = 1,

    val startDate: String? = null,

    val endDate: String? = null,

    val automaticGeneration: Boolean = true
)


/*
 * =============================================================
 * BILLING FREQUENCY
 * =============================================================
 */

enum class BillingFrequency(

    val displayName: String

) {

    DAILY(
        "Daily"
    ),

    WEEKLY(
        "Weekly"
    ),

    MONTHLY(
        "Monthly"
    ),

    QUARTERLY(
        "Quarterly"
    ),

    YEARLY(
        "Yearly"
    ),

    ONE_TIME(
        "One Time"
    )
}


/*
 * =============================================================
 * BILL REMINDER CONFIGURATION
 * =============================================================
 */

data class BillReminderConfiguration(

    val enabled: Boolean = true,

    /*
     * Days before due date.
     *
     * Example:
     *
     * 3 = notify tenant 3 days before.
     */

    val daysBeforeDue: List<Int> =
        listOf(
            7,
            3,
            1
        ),

    /*
     * Notify when overdue.
     */

    val notifyWhenOverdue: Boolean = true,

    /*
     * Notify landlord when tenant pays.
     */

    val notifyLandlordOnPayment: Boolean = true,

    /*
     * Notify tenant after payment.
     */

    val notifyTenantOnPayment: Boolean = true
)