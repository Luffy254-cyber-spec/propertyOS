package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * BILL REPOSITORY
 * =============================================================
 *
 * Handles property bills and tenant charges.
 *
 * Supports:
 * - Utility bills
 * - Water
 * - Electricity
 * - Garbage
 * - Internet
 * - Security
 * - Service charges
 * - Recurring bills
 * - Individual tenant charges
 * - Shared property bills
 * - Partial payments
 * - Arrears
 * - Due dates
 * - Reminders
 * - Bill statements
 * - Billing analytics
 *
 * =============================================================
 */

interface BillRepository {

    /*
     * ---------------------------------------------------------
     * BILL CREATION
     * ---------------------------------------------------------
     */

    /**
     * Create a bill for a tenant.
     */
    suspend fun createBill(
        createdBy: String,
        bill: CreateBillData
    ): Result<String>

    /**
     * Create one bill for multiple tenants.
     */
    suspend fun createBulkBill(
        createdBy: String,
        bill: BulkBillData
    ): Result<List<String>>

    /**
     * Create a bill for an entire property.
     */
    suspend fun createPropertyBill(
        createdBy: String,
        bill: PropertyBillData
    ): Result<String>

    /**
     * Update an unpaid bill.
     */
    suspend fun updateBill(
        userId: String,
        billId: String,
        bill: CreateBillData
    ): Result<Unit>

    /**
     * Cancel a bill.
     */
    suspend fun cancelBill(
        userId: String,
        billId: String,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * BILL RETRIEVAL
     * ---------------------------------------------------------
     */

    /**
     * Get a single bill.
     */
    suspend fun getBill(
        billId: String
    ): Result<BillDetailsData>

    /**
     * Observe a bill.
     */
    fun observeBill(
        billId: String
    ): Flow<Result<BillDetailsData>>

    /**
     * Get tenant bills.
     */
    suspend fun getTenantBills(
        tenantId: String
    ): Result<List<BillDetailsData>>

    /**
     * Observe tenant bills.
     */
    fun observeTenantBills(
        tenantId: String
    ): Flow<Result<List<BillDetailsData>>>

    /**
     * Get landlord bills.
     */
    suspend fun getLandlordBills(
        landlordId: String
    ): Result<List<BillDetailsData>>

    /**
     * Get property bills.
     */
    suspend fun getPropertyBills(
        propertyId: String
    ): Result<List<BillDetailsData>>

    /**
     * Get unit bills.
     */
    suspend fun getUnitBills(
        unitId: String
    ): Result<List<BillDetailsData>>


    /*
     * ---------------------------------------------------------
     * BILL FILTERING
     * ---------------------------------------------------------
     */

    suspend fun getOutstandingBills(
        userId: String
    ): Result<List<BillDetailsData>>

    suspend fun getOverdueBills(
        userId: String
    ): Result<List<BillDetailsData>>

    suspend fun getPaidBills(
        userId: String
    ): Result<List<BillDetailsData>>

    suspend fun getBillsDueSoon(
        userId: String,
        days: Int = 7
    ): Result<List<BillDetailsData>>

    suspend fun getBillsByType(
        userId: String,
        type: BillType
    ): Result<List<BillDetailsData>>


    /*
     * ---------------------------------------------------------
     * BILL PAYMENT
     * ---------------------------------------------------------
     */

    /**
     * Record a payment against a bill.
     */
    suspend fun recordPayment(
        userId: String,
        billId: String,
        payment: BillPaymentData
    ): Result<String>

    /**
     * Apply an existing payment to a bill.
     */
    suspend fun allocatePayment(
        userId: String,
        billId: String,
        paymentId: String,
        amount: Double
    ): Result<Unit>

    /**
     * Get payments attached to a bill.
     */
    suspend fun getBillPayments(
        billId: String
    ): Result<List<BillPaymentData>>

    /**
     * Get remaining bill balance.
     */
    suspend fun getBillBalance(
        billId: String
    ): Result<BillBalanceData>


    /*
     * ---------------------------------------------------------
     * ARREARS
     * ---------------------------------------------------------
     */

    /**
     * Get total tenant arrears.
     */
    suspend fun getTenantArrears(
        tenantId: String
    ): Result<ArrearsSummaryData>

    /**
     * Get landlord-wide arrears.
     */
    suspend fun getLandlordArrears(
        landlordId: String
    ): Result<ArrearsSummaryData>

    /**
     * Get property arrears.
     */
    suspend fun getPropertyArrears(
        propertyId: String
    ): Result<ArrearsSummaryData>

    /**
     * Get individual overdue charges.
     */
    suspend fun getArrearItems(
        userId: String
    ): Result<List<ArrearItemData>>


    /*
     * ---------------------------------------------------------
     * REMINDERS
     * ---------------------------------------------------------
     */

    /**
     * Send a reminder for one bill.
     */
    suspend fun sendReminder(
        billId: String
    ): Result<Unit>

    /**
     * Send reminders to selected tenants.
     */
    suspend fun sendBulkReminders(
        billIds: List<String>
    ): Result<Int>

    /**
     * Automatically process upcoming bills.
     */
    suspend fun processDueReminders(
        daysBeforeDue: Int
    ): Result<Int>

    /**
     * Automatically process overdue reminders.
     */
    suspend fun processOverdueReminders(): Result<Int>


    /*
     * ---------------------------------------------------------
     * RECURRING BILLS
     * ---------------------------------------------------------
     */

    /**
     * Create a recurring billing rule.
     */
    suspend fun createRecurringBill(
        createdBy: String,
        recurringBill: RecurringBillData
    ): Result<String>

    /**
     * Update recurring billing rule.
     */
    suspend fun updateRecurringBill(
        userId: String,
        recurringBillId: String,
        recurringBill: RecurringBillData
    ): Result<Unit>

    /**
     * Disable recurring billing.
     */
    suspend fun disableRecurringBill(
        userId: String,
        recurringBillId: String
    ): Result<Unit>

    /**
     * Get recurring billing rules.
     */
    suspend fun getRecurringBills(
        userId: String
    ): Result<List<RecurringBillData>>

    /**
     * Generate bills from recurring rules.
     *
     * Usually executed by a backend scheduled job.
     */
    suspend fun generateRecurringBills(): Result<Int>


    /*
     * ---------------------------------------------------------
     * SHARED BILLS
     * ---------------------------------------------------------
     */

    /**
     * Create a bill shared between tenants.
     */
    suspend fun createSharedBill(
        createdBy: String,
        bill: SharedBillData
    ): Result<String>

    /**
     * Get how a shared bill was divided.
     */
    suspend fun getBillAllocation(
        billId: String
    ): Result<List<BillAllocationData>>

    /**
     * Recalculate shared bill allocation.
     */
    suspend fun recalculateAllocation(
        billId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * RECEIPTS / DOCUMENTS
     * ---------------------------------------------------------
     */

    /**
     * Generate a bill receipt.
     */
    suspend fun generateReceipt(
        billId: String
    ): Result<String>

    /**
     * Generate a statement.
     */
    suspend fun generateStatement(
        userId: String,
        startDate: String,
        endDate: String
    ): Result<String>

    /**
     * Get bill receipt URL.
     */
    suspend fun getReceiptUrl(
        billId: String
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * BILLING ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getBillingSummary(
        userId: String
    ): Result<BillingSummaryData>

    suspend fun getMonthlyBillingSummary(
        userId: String,
        year: Int,
        month: Int
    ): Result<MonthlyBillingData>

    suspend fun getBillingHistory(
        userId: String,
        startDate: String,
        endDate: String
    ): Result<List<BillDetailsData>>
}


/*
 * =============================================================
 * DATA CONTRACTS
 * =============================================================
 */

data class CreateBillData(
    val tenantId: String,
    val propertyId: String,
    val unitId: String?,
    val type: BillType,
    val title: String,
    val description: String?,
    val amount: Double,
    val dueDate: String,
    val billingPeriodStart: String?,
    val billingPeriodEnd: String?
)

data class BulkBillData(
    val tenantIds: List<String>,
    val propertyId: String,
    val unitId: String?,
    val type: BillType,
    val title: String,
    val description: String?,
    val amountPerTenant: Double,
    val dueDate: String
)

data class PropertyBillData(
    val propertyId: String,
    val type: BillType,
    val title: String,
    val description: String?,
    val totalAmount: Double,
    val allocationMethod: BillAllocationMethod,
    val dueDate: String
)

data class BillDetailsData(
    val id: String,
    val tenantId: String,
    val tenantName: String?,
    val landlordId: String,
    val propertyId: String,
    val propertyName: String?,
    val unitId: String?,
    val unitName: String?,
    val type: BillType,
    val title: String,
    val description: String?,
    val originalAmount: Double,
    val amountPaid: Double,
    val balance: Double,
    val dueDate: String,
    val status: BillStatus,
    val createdAt: String
)

data class BillPaymentData(
    val id: String = "",
    val billId: String,
    val tenantId: String,
    val amount: Double,
    val paymentMethod: String,
    val transactionReference: String?,
    val paidAt: String
)

data class BillBalanceData(
    val originalAmount: Double,
    val amountPaid: Double,
    val outstandingAmount: Double,
    val daysOverdue: Int
)

data class ArrearsSummaryData(
    val totalOutstanding: Double,
    val overdueAmount: Double,
    val currentAmount: Double,
    val overdueBillCount: Int
)

data class ArrearItemData(
    val billId: String,
    val tenantId: String,
    val tenantName: String?,
    val billTitle: String,
    val amount: Double,
    val dueDate: String,
    val daysOverdue: Int
)

data class RecurringBillData(
    val id: String = "",
    val propertyId: String,
    val tenantIds: List<String>,
    val type: BillType,
    val title: String,
    val description: String?,
    val amount: Double,
    val frequency: BillingFrequency,
    val nextBillingDate: String,
    val active: Boolean
)

data class SharedBillData(
    val propertyId: String,
    val tenantIds: List<String>,
    val type: BillType,
    val title: String,
    val totalAmount: Double,
    val allocationMethod: BillAllocationMethod,
    val dueDate: String
)

data class BillAllocationData(
    val tenantId: String,
    val tenantName: String?,
    val allocatedAmount: Double,
    val percentage: Double
)

data class BillingSummaryData(
    val totalBilled: Double,
    val totalCollected: Double,
    val totalOutstanding: Double,
    val totalOverdue: Double,
    val collectionRate: Double
)

data class MonthlyBillingData(
    val year: Int,
    val month: Int,
    val billed: Double,
    val collected: Double,
    val outstanding: Double,
    val overdue: Double
)


/*
 * =============================================================
 * BILL ENUMS
 * =============================================================
 */

enum class BillType {

    WATER,

    ELECTRICITY,

    GARBAGE,

    INTERNET,

    SECURITY,

    CLEANING,

    SERVICE_CHARGE,

    PARKING,

    MAINTENANCE,

    LANDSCAPING,

    GAS,

    OTHER
}

enum class BillStatus {

    DRAFT,

    ISSUED,

    DUE_SOON,

    PARTIALLY_PAID,

    PAID,

    OVERDUE,

    CANCELLED
}

enum class BillAllocationMethod {

    EQUAL_SPLIT,

    BY_UNIT,

    BY_OCCUPANCY,

    BY_METER_USAGE,

    CUSTOM
}

enum class BillingFrequency {

    DAILY,

    WEEKLY,

    MONTHLY,

    QUARTERLY,

    YEARLY
}