package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * REPORT REPOSITORY
 * =============================================================
 *
 * Handles:
 *
 * - Rent reports
 * - Payment reports
 * - Arrears reports
 * - Income reports
 * - Expense reports
 * - Property reports
 * - Tenant reports
 * - Maintenance reports
 * - Lease reports
 * - Agreement reports
 * - Staff reports
 * - Broker reports
 * - Technician reports
 * - Portfolio reports
 * - Organization reports
 * - Tax summaries
 * - Audit reports
 * - Occupancy reports
 * - Financial reports
 * - Custom reports
 * - Scheduled reports
 * - Exporting
 * - Report history
 *
 * =============================================================
 */

interface ReportRepository {

    /*
     * ---------------------------------------------------------
     * REPORT GENERATION
     * ---------------------------------------------------------
     */

    suspend fun generateReport(
        actorId: String,
        request: ReportRequest
    ): Result<GeneratedReportData>

    suspend fun generateReportPreview(
        actorId: String,
        request: ReportRequest
    ): Result<ReportPreviewData>


    /*
     * ---------------------------------------------------------
     * REPORT HISTORY
     * ---------------------------------------------------------
     */

    suspend fun getReport(
        reportId: String
    ): Result<GeneratedReportData>

    suspend fun getUserReports(
        userId: String,
        limit: Int = 50
    ): Result<List<GeneratedReportData>>

    fun observeUserReports(
        userId: String
    ): Flow<Result<List<GeneratedReportData>>>

    suspend fun deleteReport(
        actorId: String,
        reportId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * RENT REPORTS
     * ---------------------------------------------------------
     */

    suspend fun generateRentCollectionReport(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>

    suspend fun generateRentArrearsReport(
        actorId: String,
        scope: ReportScope
    ): Result<GeneratedReportData>

    suspend fun generateRentDueReport(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>

    suspend fun generateRentLedgerReport(
        actorId: String,
        tenantId: String,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>


    /*
     * ---------------------------------------------------------
     * PAYMENT REPORTS
     * ---------------------------------------------------------
     */

    suspend fun generatePaymentReport(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>

    suspend fun generateFailedPaymentReport(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>

    suspend fun generateRefundReport(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>


    /*
     * ---------------------------------------------------------
     * FINANCIAL REPORTS
     * ---------------------------------------------------------
     */

    suspend fun generateIncomeReport(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>

    suspend fun generateExpenseReport(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>

    suspend fun generateProfitLossReport(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>

    suspend fun generateCashFlowReport(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>

    suspend fun generateFinancialSummary(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<FinancialSummaryData>


    /*
     * ---------------------------------------------------------
     * TAX REPORTS
     * ---------------------------------------------------------
     */

    suspend fun generateTaxSummary(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<TaxSummaryData>

    suspend fun generateTaxTransactionReport(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>


    /*
     * ---------------------------------------------------------
     * PROPERTY REPORTS
     * ---------------------------------------------------------
     */

    suspend fun generatePropertyReport(
        actorId: String,
        propertyId: String
    ): Result<GeneratedReportData>

    suspend fun generatePropertyPerformanceReport(
        actorId: String,
        propertyId: String,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>

    suspend fun generatePropertyOccupancyReport(
        actorId: String,
        scope: ReportScope
    ): Result<GeneratedReportData>

    suspend fun generateVacancyReport(
        actorId: String,
        scope: ReportScope
    ): Result<GeneratedReportData>


    /*
     * ---------------------------------------------------------
     * TENANT REPORTS
     * ---------------------------------------------------------
     */

    suspend fun generateTenantReport(
        actorId: String,
        tenantId: String
    ): Result<GeneratedReportData>

    suspend fun generateTenantPaymentHistory(
        actorId: String,
        tenantId: String,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>

    suspend fun generateTenantArrearsReport(
        actorId: String,
        tenantId: String
    ): Result<GeneratedReportData>

    suspend fun generateTenantActivityReport(
        actorId: String,
        tenantId: String,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>


    /*
     * ---------------------------------------------------------
     * MAINTENANCE REPORTS
     * ---------------------------------------------------------
     */

    suspend fun generateMaintenanceReport(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>

    suspend fun generateMaintenanceCostReport(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>

    suspend fun generateTechnicianPerformanceReport(
        actorId: String,
        technicianId: String,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>


    /*
     * ---------------------------------------------------------
     * LEASE REPORTS
     * ---------------------------------------------------------
     */

    suspend fun generateLeaseReport(
        actorId: String,
        scope: ReportScope
    ): Result<GeneratedReportData>

    suspend fun generateExpiringLeaseReport(
        actorId: String,
        scope: ReportScope,
        daysAhead: Int
    ): Result<GeneratedReportData>

    suspend fun generateLeaseRenewalReport(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>


    /*
     * ---------------------------------------------------------
     * AGREEMENT REPORTS
     * ---------------------------------------------------------
     */

    suspend fun generateAgreementReport(
        actorId: String,
        scope: ReportScope
    ): Result<GeneratedReportData>

    suspend fun generateUnsignedAgreementReport(
        actorId: String,
        scope: ReportScope
    ): Result<GeneratedReportData>


    /*
     * ---------------------------------------------------------
     * APPLICATION REPORTS
     * ---------------------------------------------------------
     */

    suspend fun generateApplicationReport(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>

    suspend fun generateApplicationConversionReport(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>


    /*
     * ---------------------------------------------------------
     * VIEWING REPORTS
     * ---------------------------------------------------------
     */

    suspend fun generateViewingReport(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>

    suspend fun generateViewingConversionReport(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>


    /*
     * ---------------------------------------------------------
     * LISTING REPORTS
     * ---------------------------------------------------------
     */

    suspend fun generateListingPerformanceReport(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>


    /*
     * ---------------------------------------------------------
     * STAFF REPORTS
     * ---------------------------------------------------------
     */

    suspend fun generateStaffActivityReport(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>

    suspend fun generateStaffPerformanceReport(
        actorId: String,
        staffId: String,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>

    suspend fun generateTaskPerformanceReport(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>


    /*
     * ---------------------------------------------------------
     * PORTFOLIO REPORTS
     * ---------------------------------------------------------
     */

    suspend fun generatePortfolioPerformanceReport(
        actorId: String,
        portfolioId: String,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>

    suspend fun generatePortfolioIncomeReport(
        actorId: String,
        portfolioId: String,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>

    suspend fun generatePortfolioExpenseReport(
        actorId: String,
        portfolioId: String,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>


    /*
     * ---------------------------------------------------------
     * ORGANIZATION REPORTS
     * ---------------------------------------------------------
     */

    suspend fun generateOrganizationReport(
        actorId: String,
        organizationId: String,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>

    suspend fun generateOrganizationFinancialReport(
        actorId: String,
        organizationId: String,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>

    suspend fun generateOrganizationStaffReport(
        actorId: String,
        organizationId: String,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>


    /*
     * ---------------------------------------------------------
     * AUDIT REPORTS
     * ---------------------------------------------------------
     */

    suspend fun generateAuditReport(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>

    suspend fun generateSecurityReport(
        actorId: String,
        scope: ReportScope,
        startDate: String,
        endDate: String
    ): Result<GeneratedReportData>


    /*
     * ---------------------------------------------------------
     * CUSTOM REPORTS
     * ---------------------------------------------------------
     */

    suspend fun createCustomReport(
        actorId: String,
        configuration: CustomReportConfiguration
    ): Result<String>

    suspend fun getCustomReports(
        userId: String
    ): Result<List<CustomReportData>>

    suspend fun updateCustomReport(
        actorId: String,
        reportId: String,
        configuration: CustomReportConfiguration
    ): Result<Unit>

    suspend fun deleteCustomReport(
        actorId: String,
        reportId: String
    ): Result<Unit>

    suspend fun runCustomReport(
        actorId: String,
        reportId: String
    ): Result<GeneratedReportData>


    /*
     * ---------------------------------------------------------
     * SCHEDULED REPORTS
     * ---------------------------------------------------------
     */

    suspend fun scheduleReport(
        actorId: String,
        schedule: ReportScheduleData
    ): Result<String>

    suspend fun getScheduledReports(
        userId: String
    ): Result<List<ReportScheduleData>>

    suspend fun updateReportSchedule(
        actorId: String,
        scheduleId: String,
        schedule: ReportScheduleData
    ): Result<Unit>

    suspend fun deleteReportSchedule(
        actorId: String,
        scheduleId: String
    ): Result<Unit>

    suspend fun runScheduledReport(
        actorId: String,
        scheduleId: String
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * EXPORT
     * ---------------------------------------------------------
     */

    suspend fun exportReport(
        actorId: String,
        reportId: String,
        format: ReportFormat
    ): Result<String>

    suspend fun exportReportData(
        actorId: String,
        request: ReportRequest,
        format: ReportFormat
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * SHARING
     * ---------------------------------------------------------
     */

    suspend fun shareReport(
        actorId: String,
        reportId: String,
        recipients: List<String>,
        message: String?
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * REPORT TEMPLATES
     * ---------------------------------------------------------
     */

    suspend fun getReportTemplates(
        userId: String
    ): Result<List<ReportTemplateData>>

    suspend fun saveReportTemplate(
        actorId: String,
        template: ReportTemplateData
    ): Result<String>

    suspend fun deleteReportTemplate(
        actorId: String,
        templateId: String
    ): Result<Unit>
}


/*
 * =============================================================
 * REQUEST
 * =============================================================
 */

data class ReportRequest(
    val type: ReportType,
    val scope: ReportScope,
    val startDate: String?,
    val endDate: String?,
    val filters: Map<String, String> = emptyMap(),
    val groupBy: ReportGroupBy? = null,
    val sortBy: String? = null,
    val ascending: Boolean = false,
    val includeCharts: Boolean = true,
    val includeSummary: Boolean = true
)


/*
 * =============================================================
 * SCOPE
 * =============================================================
 */

data class ReportScope(
    val userId: String? = null,
    val organizationId: String? = null,
    val portfolioId: String? = null,
    val propertyId: String? = null,
    val unitId: String? = null,
    val tenantId: String? = null,
    val landlordId: String? = null,
    val staffId: String? = null,
    val branchId: String? = null,
    val teamId: String? = null
)


/*
 * =============================================================
 * GENERATED REPORT
 * =============================================================
 */

data class GeneratedReportData(
    val id: String,
    val type: ReportType,
    val title: String,
    val description: String?,
    val generatedBy: String,
    val scope: ReportScope,
    val startDate: String?,
    val endDate: String?,
    val format: ReportFormat,
    val fileUrl: String?,
    val fileSizeBytes: Long?,
    val rowCount: Int,
    val generatedAt: String,
    val expiresAt: String?
)


/*
 * =============================================================
 * PREVIEW
 * =============================================================
 */

data class ReportPreviewData(
    val title: String,
    val columns: List<ReportColumnData>,
    val rows: List<List<String>>,
    val totalRows: Int,
    val summary: Map<String, String>
)

data class ReportColumnData(
    val key: String,
    val title: String,
    val type: ReportColumnType
)


/*
 * =============================================================
 * FINANCIAL SUMMARY
 * =============================================================
 */

data class FinancialSummaryData(
    val totalIncome: Double,
    val totalExpenses: Double,
    val netIncome: Double,
    val rentIncome: Double,
    val otherIncome: Double,
    val maintenanceExpenses: Double,
    val utilityExpenses: Double,
    val taxExpenses: Double,
    val insuranceExpenses: Double,
    val managementExpenses: Double,
    val outstandingRent: Double,
    val collectionRate: Double
)


/*
 * =============================================================
 * TAX SUMMARY
 * =============================================================
 */

data class TaxSummaryData(
    val grossIncome: Double,
    val taxableIncome: Double,
    val deductibleExpenses: Double,
    val taxAmount: Double,
    val taxPaid: Double,
    val taxOutstanding: Double,
    val currency: String
)


/*
 * =============================================================
 * CUSTOM REPORT
 * =============================================================
 */

data class CustomReportConfiguration(
    val name: String,
    val description: String?,
    val type: ReportType,
    val scope: ReportScope,
    val columns: List<String>,
    val filters: Map<String, String>,
    val groupBy: ReportGroupBy?,
    val sortBy: String?,
    val ascending: Boolean
)

data class CustomReportData(
    val id: String,
    val ownerId: String,
    val configuration: CustomReportConfiguration,
    val createdAt: String,
    val updatedAt: String
)


/*
 * =============================================================
 * SCHEDULE
 * =============================================================
 */

data class ReportScheduleData(
    val id: String = "",
    val ownerId: String,
    val reportType: ReportType,
    val scope: ReportScope,
    val frequency: ReportFrequency,
    val dayOfWeek: Int?,
    val dayOfMonth: Int?,
    val time: String,
    val format: ReportFormat,
    val recipients: List<String>,
    val enabled: Boolean = true,
    val nextRunAt: String? = null,
    val createdAt: String = ""
)


/*
 * =============================================================
 * TEMPLATE
 * =============================================================
 */

data class ReportTemplateData(
    val id: String = "",
    val ownerId: String,
    val name: String,
    val description: String?,
    val configuration: CustomReportConfiguration,
    val createdAt: String = "",
    val updatedAt: String = ""
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class ReportType {

    RENT_COLLECTION,

    RENT_ARREARS,

    RENT_DUE,

    RENT_LEDGER,

    PAYMENT,

    FAILED_PAYMENT,

    REFUND,

    INCOME,

    EXPENSE,

    PROFIT_LOSS,

    CASH_FLOW,

    FINANCIAL_SUMMARY,

    TAX_SUMMARY,

    TAX_TRANSACTION,

    PROPERTY,

    PROPERTY_PERFORMANCE,

    OCCUPANCY,

    VACANCY,

    TENANT,

    TENANT_PAYMENT_HISTORY,

    TENANT_ARREARS,

    TENANT_ACTIVITY,

    MAINTENANCE,

    MAINTENANCE_COST,

    TECHNICIAN_PERFORMANCE,

    LEASE,

    EXPIRING_LEASE,

    LEASE_RENEWAL,

    AGREEMENT,

    UNSIGNED_AGREEMENT,

    APPLICATION,

    APPLICATION_CONVERSION,

    VIEWING,

    VIEWING_CONVERSION,

    LISTING_PERFORMANCE,

    STAFF_ACTIVITY,

    STAFF_PERFORMANCE,

    TASK_PERFORMANCE,

    PORTFOLIO_PERFORMANCE,

    PORTFOLIO_INCOME,

    PORTFOLIO_EXPENSE,

    ORGANIZATION,

    ORGANIZATION_FINANCIAL,

    ORGANIZATION_STAFF,

    AUDIT,

    SECURITY,

    CUSTOM
}


enum class ReportFormat {

    PDF,

    CSV,

    EXCEL,

    JSON
}


enum class ReportGroupBy {

    DAY,

    WEEK,

    MONTH,

    QUARTER,

    YEAR,

    PROPERTY,

    UNIT,

    TENANT,

    STAFF,

    BRANCH,

    DEPARTMENT,

    TEAM,

    PAYMENT_METHOD,

    EXPENSE_CATEGORY,

    MAINTENANCE_CATEGORY
}


enum class ReportColumnType {

    TEXT,

    INTEGER,

    DECIMAL,

    CURRENCY,

    DATE,

    DATETIME,

    BOOLEAN,

    PERCENTAGE
}


enum class ReportFrequency {

    DAILY,

    WEEKLY,

    MONTHLY,

    QUARTERLY,

    YEARLY
}