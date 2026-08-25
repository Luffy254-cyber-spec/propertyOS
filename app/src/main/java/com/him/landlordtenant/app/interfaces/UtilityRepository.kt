package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * UTILITY REPOSITORY
 * =============================================================
 *
 * Handles utilities associated with properties and units.
 *
 * Supports:
 * - Electricity
 * - Water
 * - Garbage
 * - Internet
 * - Gas
 * - Solar
 * - Security services
 * - Shared/common-area utilities
 * - Meter readings
 * - Utility consumption
 * - Utility bills
 * - Tenant allocations
 * - Utility arrears
 * - Utility payments
 * - Utility providers
 * - Recurring utility charges
 * - Estimated readings
 * - Bill generation
 * - Usage analytics
 *
 * =============================================================
 */

interface UtilityRepository {

    /*
     * ---------------------------------------------------------
     * UTILITY CONFIGURATION
     * ---------------------------------------------------------
     */

    suspend fun createUtility(
        userId: String,
        utility: CreateUtilityData
    ): Result<String>

    suspend fun getUtility(
        utilityId: String
    ): Result<UtilityData>

    suspend fun updateUtility(
        userId: String,
        utilityId: String,
        utility: UpdateUtilityData
    ): Result<Unit>

    suspend fun deactivateUtility(
        userId: String,
        utilityId: String
    ): Result<Unit>

    suspend fun getPropertyUtilities(
        propertyId: String
    ): Result<List<UtilityData>>

    suspend fun getUnitUtilities(
        unitId: String
    ): Result<List<UtilityData>>


    /*
     * ---------------------------------------------------------
     * UTILITY PROVIDERS
     * ---------------------------------------------------------
     */

    suspend fun createProvider(
        userId: String,
        provider: CreateUtilityProviderData
    ): Result<String>

    suspend fun getProvider(
        providerId: String
    ): Result<UtilityProviderData>

    suspend fun getProviders(
        utilityType: UtilityType
    ): Result<List<UtilityProviderData>>

    suspend fun updateProvider(
        userId: String,
        providerId: String,
        provider: UpdateUtilityProviderData
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * METERS
     * ---------------------------------------------------------
     */

    suspend fun addMeter(
        userId: String,
        meter: CreateMeterData
    ): Result<String>

    suspend fun getMeter(
        meterId: String
    ): Result<MeterData>

    suspend fun updateMeter(
        userId: String,
        meterId: String,
        meter: UpdateMeterData
    ): Result<Unit>

    suspend fun removeMeter(
        userId: String,
        meterId: String
    ): Result<Unit>

    suspend fun getPropertyMeters(
        propertyId: String
    ): Result<List<MeterData>>

    suspend fun getUnitMeters(
        unitId: String
    ): Result<List<MeterData>>


    /*
     * ---------------------------------------------------------
     * METER READINGS
     * ---------------------------------------------------------
     */

    suspend fun submitMeterReading(
        userId: String,
        reading: SubmitMeterReadingData
    ): Result<String>

    suspend fun updateMeterReading(
        userId: String,
        readingId: String,
        reading: UpdateMeterReadingData
    ): Result<Unit>

    suspend fun getMeterReadings(
        meterId: String,
        startDate: String? = null,
        endDate: String? = null
    ): Result<List<MeterReadingData>>

    fun observeLatestMeterReading(
        meterId: String
    ): Flow<Result<MeterReadingData?>>

    suspend fun getLatestMeterReadings(
        propertyId: String
    ): Result<List<MeterReadingData>>


    /*
     * ---------------------------------------------------------
     * CONSUMPTION
     * ---------------------------------------------------------
     */

    suspend fun calculateConsumption(
        meterId: String,
        startReadingId: String,
        endReadingId: String
    ): Result<UtilityConsumptionData>

    suspend fun getConsumptionHistory(
        meterId: String,
        startDate: String,
        endDate: String
    ): Result<List<UtilityConsumptionData>>

    suspend fun getUnitConsumption(
        unitId: String,
        utilityType: UtilityType,
        startDate: String,
        endDate: String
    ): Result<UtilityConsumptionData>

    suspend fun getPropertyConsumption(
        propertyId: String,
        utilityType: UtilityType,
        startDate: String,
        endDate: String
    ): Result<UtilityConsumptionData>


    /*
     * ---------------------------------------------------------
     * UTILITY BILLING
     * ---------------------------------------------------------
     */

    suspend fun createUtilityBill(
        userId: String,
        bill: CreateUtilityBillData
    ): Result<String>

    suspend fun generateMeterBill(
        userId: String,
        meterId: String,
        billingPeriod: BillingPeriodData
    ): Result<String>

    suspend fun generatePropertyUtilityBills(
        userId: String,
        propertyId: String,
        billingPeriod: BillingPeriodData
    ): Result<List<String>>

    suspend fun getUtilityBill(
        billId: String
    ): Result<UtilityBillData>

    suspend fun updateUtilityBill(
        userId: String,
        billId: String,
        bill: UpdateUtilityBillData
    ): Result<Unit>

    suspend fun cancelUtilityBill(
        userId: String,
        billId: String,
        reason: String?
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * TENANT UTILITY BILLS
     * ---------------------------------------------------------
     */

    suspend fun getTenantUtilityBills(
        tenantId: String
    ): Result<List<UtilityBillData>>

    fun observeTenantUtilityBills(
        tenantId: String
    ): Flow<Result<List<UtilityBillData>>>

    suspend fun getOutstandingUtilityBills(
        tenantId: String
    ): Result<List<UtilityBillData>>

    suspend fun getOverdueUtilityBills(
        tenantId: String
    ): Result<List<UtilityBillData>>


    /*
     * ---------------------------------------------------------
     * PROPERTY UTILITY BILLS
     * ---------------------------------------------------------
     */

    suspend fun getPropertyUtilityBills(
        propertyId: String,
        startDate: String? = null,
        endDate: String? = null
    ): Result<List<UtilityBillData>>

    suspend fun getUnitUtilityBills(
        unitId: String
    ): Result<List<UtilityBillData>>


    /*
     * ---------------------------------------------------------
     * SHARED UTILITIES
     * ---------------------------------------------------------
     */

    suspend fun createSharedUtilityRule(
        userId: String,
        rule: CreateSharedUtilityRuleData
    ): Result<String>

    suspend fun updateSharedUtilityRule(
        userId: String,
        ruleId: String,
        rule: UpdateSharedUtilityRuleData
    ): Result<Unit>

    suspend fun getSharedUtilityRules(
        propertyId: String
    ): Result<List<SharedUtilityRuleData>>

    suspend fun calculateSharedUtilityAllocation(
        propertyId: String,
        utilityType: UtilityType,
        totalAmount: Double
    ): Result<List<UtilityAllocationData>>


    /*
     * ---------------------------------------------------------
     * UTILITY PAYMENTS
     * ---------------------------------------------------------
     */

    suspend fun recordUtilityPayment(
        userId: String,
        payment: UtilityPaymentData
    ): Result<String>

    suspend fun getUtilityPayments(
        billId: String
    ): Result<List<UtilityPaymentData>>

    suspend fun getTenantUtilityPayments(
        tenantId: String
    ): Result<List<UtilityPaymentData>>

    suspend fun allocatePayment(
        userId: String,
        paymentId: String,
        billId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * ARREARS
     * ---------------------------------------------------------
     */

    suspend fun calculateUtilityArrears(
        tenantId: String
    ): Result<UtilityArrearsData>

    suspend fun getPropertyUtilityArrears(
        propertyId: String
    ): Result<List<UtilityArrearsData>>

    suspend fun waiveUtilityArrears(
        userId: String,
        billId: String,
        amount: Double,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * ESTIMATED READINGS
     * ---------------------------------------------------------
     */

    suspend fun createEstimatedReading(
        userId: String,
        reading: EstimatedReadingData
    ): Result<String>

    suspend fun approveEstimatedReading(
        userId: String,
        readingId: String
    ): Result<Unit>

    suspend fun correctEstimatedReading(
        userId: String,
        readingId: String,
        actualReading: Double
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * AUTOMATION
     * ---------------------------------------------------------
     */

    suspend fun processRecurringUtilityBills(
        propertyId: String,
        billingDate: String
    ): Result<Int>

    suspend fun sendUtilityDueReminders(
        propertyId: String
    ): Result<Int>

    suspend fun markOverdueBills(
        propertyId: String
    ): Result<Int>


    /*
     * ---------------------------------------------------------
     * ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getUtilityAnalytics(
        propertyId: String,
        startDate: String,
        endDate: String
    ): Result<UtilityAnalyticsData>

    suspend fun getTenantUtilityAnalytics(
        tenantId: String,
        startDate: String,
        endDate: String
    ): Result<TenantUtilityAnalyticsData>


    /*
     * ---------------------------------------------------------
     * EXPORT
     * ---------------------------------------------------------
     */

    suspend fun exportUtilityReport(
        userId: String,
        propertyId: String,
        startDate: String,
        endDate: String,
        format: UtilityExportFormat
    ): Result<String>
}


/*
 * =============================================================
 * DATA CONTRACTS
 * =============================================================
 */

data class CreateUtilityData(
    val propertyId: String,
    val unitId: String?,
    val type: UtilityType,
    val name: String,
    val providerId: String?,
    val billingMethod: UtilityBillingMethod,
    val active: Boolean = true
)

data class UpdateUtilityData(
    val name: String?,
    val providerId: String?,
    val billingMethod: UtilityBillingMethod?,
    val active: Boolean?
)

data class UtilityData(
    val id: String,
    val propertyId: String,
    val unitId: String?,
    val type: UtilityType,
    val name: String,
    val providerId: String?,
    val billingMethod: UtilityBillingMethod,
    val active: Boolean
)

data class CreateUtilityProviderData(
    val name: String,
    val utilityTypes: List<UtilityType>,
    val phoneNumber: String?,
    val email: String?,
    val accountReference: String?
)

data class UpdateUtilityProviderData(
    val name: String?,
    val phoneNumber: String?,
    val email: String?,
    val accountReference: String?
)

data class UtilityProviderData(
    val id: String,
    val name: String,
    val utilityTypes: List<UtilityType>,
    val phoneNumber: String?,
    val email: String?,
    val accountReference: String?
)

data class CreateMeterData(
    val propertyId: String,
    val unitId: String?,
    val utilityType: UtilityType,
    val meterNumber: String,
    val unitOfMeasure: String,
    val installationDate: String?
)

data class UpdateMeterData(
    val meterNumber: String?,
    val unitOfMeasure: String?,
    val active: Boolean?
)

data class MeterData(
    val id: String,
    val propertyId: String,
    val unitId: String?,
    val utilityType: UtilityType,
    val meterNumber: String,
    val unitOfMeasure: String,
    val installationDate: String?,
    val active: Boolean
)

data class SubmitMeterReadingData(
    val meterId: String,
    val reading: Double,
    val readingDate: String,
    val photoPath: String?,
    val submittedBy: String
)

data class UpdateMeterReadingData(
    val reading: Double?,
    val readingDate: String?,
    val photoPath: String?
)


data class UtilityConsumptionData(
    val meterId: String?,
    val utilityType: UtilityType,
    val startReading: Double,
    val endReading: Double,
    val consumption: Double,
    val unitOfMeasure: String,
    val startDate: String,
    val endDate: String
)

data class BillingPeriodData(
    val startDate: String,
    val endDate: String,
    val dueDate: String
)

data class CreateUtilityBillData(
    val propertyId: String,
    val unitId: String?,
    val tenantId: String?,
    val utilityType: UtilityType,
    val description: String,
    val billingPeriod: BillingPeriodData,
    val amount: Double,
    val currency: String = "KES",
    val meterReadingId: String?,
    val consumption: Double?,
    val status: UtilityBillStatus = UtilityBillStatus.UNPAID
)

data class UpdateUtilityBillData(
    val description: String?,
    val amount: Double?,
    val dueDate: String?,
    val status: UtilityBillStatus?
)

data class UtilityBillData(
    val id: String,
    val propertyId: String,
    val unitId: String?,
    val tenantId: String?,
    val utilityType: UtilityType,
    val description: String,
    val billingPeriod: BillingPeriodData,
    val amount: Double,
    val paidAmount: Double,
    val balance: Double,
    val currency: String,
    val meterReadingId: String?,
    val consumption: Double?,
    val status: UtilityBillStatus,
    val createdAt: String
)

data class CreateSharedUtilityRuleData(
    val propertyId: String,
    val utilityType: UtilityType,
    val allocationMethod: UtilityAllocationMethod,
    val fixedAmount: Double?,
    val percentage: Double?,
    val active: Boolean = true
)

data class UpdateSharedUtilityRuleData(
    val allocationMethod: UtilityAllocationMethod?,
    val fixedAmount: Double?,
    val percentage: Double?,
    val active: Boolean?
)

data class SharedUtilityRuleData(
    val id: String,
    val propertyId: String,
    val utilityType: UtilityType,
    val allocationMethod: UtilityAllocationMethod,
    val fixedAmount: Double?,
    val percentage: Double?,
    val active: Boolean
)

data class UtilityAllocationData(
    val tenantId: String,
    val unitId: String,
    val allocatedAmount: Double,
    val percentage: Double
)

data class UtilityPaymentData(
    val id: String = "",
    val billId: String,
    val tenantId: String,
    val amount: Double,
    val paymentMethod: UtilityPaymentMethod,
    val transactionReference: String?,
    val paymentDate: String
)

data class UtilityArrearsData(
    val tenantId: String,
    val totalBilled: Double,
    val totalPaid: Double,
    val totalOutstanding: Double,
    val overdueAmount: Double,
    val overdueBills: Int
)

data class EstimatedReadingData(
    val meterId: String,
    val estimatedReading: Double,
    val reason: String,
    val billingPeriod: BillingPeriodData
)

data class UtilityAnalyticsData(
    val totalUtilityCost: Double,
    val electricityCost: Double,
    val waterCost: Double,
    val garbageCost: Double,
    val internetCost: Double,
    val gasCost: Double,
    val otherCost: Double,
    val totalConsumption: Double,
    val averageMonthlyCost: Double,
    val outstandingAmount: Double,
    val overdueAmount: Double
)

data class TenantUtilityAnalyticsData(
    val tenantId: String,
    val totalBilled: Double,
    val totalPaid: Double,
    val outstanding: Double,
    val averageMonthlyCost: Double,
    val consumptionByUtility: Map<UtilityType, Double>
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class UtilityType {

    ELECTRICITY,

    WATER,

    GARBAGE,

    INTERNET,

    GAS,

    SOLAR,

    SECURITY,

    CLEANING,

    SEWER,

    OTHER
}

enum class UtilityBillingMethod {

    METERED,

    FIXED,

    SHARED,

    LANDLORD_PAID,

    TENANT_PAID_DIRECTLY
}

enum class MeterReadingSource {

    TENANT,

    LANDLORD,

    CARETAKER,

    PROPERTY_MANAGER,

    AUTOMATIC,

    IMPORTED,

    ESTIMATED
}

enum class UtilityBillStatus {

    DRAFT,

    UNPAID,

    PARTIALLY_PAID,

    PAID,

    OVERDUE,

    DISPUTED,

    WAIVED,

    CANCELLED
}

enum class UtilityAllocationMethod {

    EQUAL_SPLIT,

    BY_UNIT_SIZE,

    BY_OCCUPANTS,

    BY_CONSUMPTION,

    PERCENTAGE,

    FIXED_AMOUNT
}

enum class UtilityPaymentMethod {

    CASH,

    MPESA,

    BANK_TRANSFER,

    CARD,

    CHEQUE,

    OTHER
}

enum class UtilityExportFormat {

    PDF,

    CSV,

    EXCEL
}