package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * PORTFOLIO REPOSITORY
 * =============================================================
 *
 * Handles:
 *
 * - Property portfolios
 * - Portfolio groups
 * - Property allocation
 * - Portfolio performance
 * - Occupancy analysis
 * - Rental income
 * - Expenses
 * - NOI
 * - ROI
 * - Rental yield
 * - Property appreciation
 * - Portfolio comparisons
 * - Investment targets
 * - Portfolio reports
 * - Performance alerts
 *
 * =============================================================
 */

interface PortfolioRepository {

    /*
     * ---------------------------------------------------------
     * PORTFOLIO CRUD
     * ---------------------------------------------------------
     */

    suspend fun createPortfolio(
        actorId: String,
        portfolio: CreatePortfolioData
    ): Result<String>

    suspend fun getPortfolio(
        portfolioId: String
    ): Result<PortfolioData>

    fun observePortfolio(
        portfolioId: String
    ): Flow<Result<PortfolioData>>

    suspend fun updatePortfolio(
        actorId: String,
        portfolioId: String,
        update: UpdatePortfolioData
    ): Result<Unit>

    suspend fun deletePortfolio(
        actorId: String,
        portfolioId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * USER PORTFOLIOS
     * ---------------------------------------------------------
     */

    suspend fun getUserPortfolios(
        userId: String
    ): Result<List<PortfolioData>>

    fun observeUserPortfolios(
        userId: String
    ): Flow<Result<List<PortfolioData>>>


    /*
     * ---------------------------------------------------------
     * ORGANIZATION PORTFOLIOS
     * ---------------------------------------------------------
     */

    suspend fun getOrganizationPortfolios(
        organizationId: String
    ): Result<List<PortfolioData>>


    /*
     * ---------------------------------------------------------
     * PROPERTY ASSIGNMENT
     * ---------------------------------------------------------
     */

    suspend fun addPropertyToPortfolio(
        actorId: String,
        portfolioId: String,
        propertyId: String
    ): Result<Unit>

    suspend fun removePropertyFromPortfolio(
        actorId: String,
        portfolioId: String,
        propertyId: String
    ): Result<Unit>

    suspend fun moveProperty(
        actorId: String,
        propertyId: String,
        fromPortfolioId: String,
        toPortfolioId: String
    ): Result<Unit>

    suspend fun getPortfolioProperties(
        portfolioId: String
    ): Result<List<String>>

    suspend fun getPropertyPortfolios(
        propertyId: String
    ): Result<List<String>>


    /*
     * ---------------------------------------------------------
     * PORTFOLIO GROUPS
     * ---------------------------------------------------------
     */

    suspend fun createPortfolioGroup(
        actorId: String,
        portfolioId: String,
        group: CreatePortfolioGroupData
    ): Result<String>

    suspend fun getPortfolioGroups(
        portfolioId: String
    ): Result<List<PortfolioGroupData>>

    suspend fun updatePortfolioGroup(
        actorId: String,
        groupId: String,
        name: String,
        description: String?
    ): Result<Unit>

    suspend fun deletePortfolioGroup(
        actorId: String,
        groupId: String
    ): Result<Unit>

    suspend fun addPropertyToGroup(
        actorId: String,
        groupId: String,
        propertyId: String
    ): Result<Unit>

    suspend fun removePropertyFromGroup(
        actorId: String,
        groupId: String,
        propertyId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * PERFORMANCE
     * ---------------------------------------------------------
     */

    suspend fun getPortfolioPerformance(
        portfolioId: String,
        startDate: String,
        endDate: String
    ): Result<PortfolioPerformanceData>

    suspend fun getPropertyPerformance(
        propertyId: String,
        startDate: String,
        endDate: String
    ): Result<PropertyPerformanceData>

    suspend fun getPortfolioPerformanceHistory(
        portfolioId: String,
        limit: Int = 12
    ): Result<List<PortfolioPerformanceSnapshotData>>


    /*
     * ---------------------------------------------------------
     * INCOME
     * ---------------------------------------------------------
     */

    suspend fun getPortfolioIncome(
        portfolioId: String,
        startDate: String,
        endDate: String
    ): Result<PortfolioIncomeData>

    suspend fun getPropertyIncome(
        propertyId: String,
        startDate: String,
        endDate: String
    ): Result<PropertyIncomeData>


    /*
     * ---------------------------------------------------------
     * EXPENSES
     * ---------------------------------------------------------
     */

    suspend fun getPortfolioExpenses(
        portfolioId: String,
        startDate: String,
        endDate: String
    ): Result<PortfolioExpenseData>

    suspend fun getPropertyExpenses(
        propertyId: String,
        startDate: String,
        endDate: String
    ): Result<PropertyExpenseData>


    /*
     * ---------------------------------------------------------
     * PROFITABILITY
     * ---------------------------------------------------------
     */

    suspend fun calculatePortfolioNOI(
        portfolioId: String,
        startDate: String,
        endDate: String
    ): Result<Double>

    suspend fun calculatePropertyNOI(
        propertyId: String,
        startDate: String,
        endDate: String
    ): Result<Double>

    suspend fun calculatePortfolioROI(
        portfolioId: String,
        startDate: String,
        endDate: String
    ): Result<Double>

    suspend fun calculatePropertyROI(
        propertyId: String,
        startDate: String,
        endDate: String
    ): Result<Double>

    suspend fun calculateRentalYield(
        propertyId: String,
        annualRent: Double
    ): Result<Double>


    /*
     * ---------------------------------------------------------
     * OCCUPANCY
     * ---------------------------------------------------------
     */

    suspend fun getPortfolioOccupancy(
        portfolioId: String
    ): Result<OccupancyData>

    suspend fun getPropertyOccupancy(
        propertyId: String
    ): Result<OccupancyData>

    suspend fun getVacantProperties(
        portfolioId: String
    ): Result<List<String>>

    suspend fun getUnderperformingProperties(
        portfolioId: String,
        threshold: Double
    ): Result<List<PropertyPerformanceData>>


    /*
     * ---------------------------------------------------------
     * RENT PERFORMANCE
     * ---------------------------------------------------------
     */

    suspend fun getRentCollectionRate(
        portfolioId: String,
        startDate: String,
        endDate: String
    ): Result<Double>

    suspend fun getOutstandingRent(
        portfolioId: String
    ): Result<Double>

    suspend fun getRentArrearsByProperty(
        portfolioId: String
    ): Result<List<PropertyArrearsData>>


    /*
     * ---------------------------------------------------------
     * PROPERTY VALUE
     * ---------------------------------------------------------
     */

    suspend fun getPortfolioValue(
        portfolioId: String
    ): Result<PortfolioValueData>

    suspend fun getPropertyValue(
        propertyId: String
    ): Result<PropertyValueData>

    suspend fun recordPropertyValuation(
        actorId: String,
        propertyId: String,
        valuation: PropertyValuationData
    ): Result<String>

    suspend fun getValuationHistory(
        propertyId: String
    ): Result<List<PropertyValuationData>>


    /*
     * ---------------------------------------------------------
     * INVESTMENT TARGETS
     * ---------------------------------------------------------
     */

    suspend fun createInvestmentTarget(
        actorId: String,
        portfolioId: String,
        target: CreateInvestmentTargetData
    ): Result<String>

    suspend fun getInvestmentTargets(
        portfolioId: String
    ): Result<List<InvestmentTargetData>>

    suspend fun updateInvestmentTarget(
        actorId: String,
        targetId: String,
        update: UpdateInvestmentTargetData
    ): Result<Unit>

    suspend fun deleteInvestmentTarget(
        actorId: String,
        targetId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * ALERTS
     * ---------------------------------------------------------
     */

    suspend fun getPortfolioAlerts(
        portfolioId: String
    ): Result<List<PortfolioAlertData>>

    suspend fun dismissAlert(
        actorId: String,
        alertId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * COMPARISON
     * ---------------------------------------------------------
     */

    suspend fun compareProperties(
        propertyIds: List<String>,
        startDate: String,
        endDate: String
    ): Result<List<PropertyPerformanceData>>

    suspend fun comparePortfolios(
        portfolioIds: List<String>,
        startDate: String,
        endDate: String
    ): Result<List<PortfolioPerformanceData>>


    /*
     * ---------------------------------------------------------
     * REPORTS
     * ---------------------------------------------------------
     */

    suspend fun generatePortfolioReport(
        actorId: String,
        portfolioId: String,
        startDate: String,
        endDate: String
    ): Result<String>

    suspend fun exportPortfolioData(
        actorId: String,
        portfolioId: String,
        format: PortfolioExportFormat
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getPortfolioAnalytics(
        portfolioId: String
    ): Result<PortfolioAnalyticsData>
}


/*
 * =============================================================
 * PORTFOLIO
 * =============================================================
 */

data class CreatePortfolioData(
    val ownerId: String,
    val organizationId: String?,
    val name: String,
    val description: String?,
    val currency: String,
    val targetAnnualReturn: Double?
)

data class UpdatePortfolioData(
    val name: String?,
    val description: String?,
    val targetAnnualReturn: Double?,
    val active: Boolean?
)

data class PortfolioData(
    val id: String,
    val ownerId: String,
    val organizationId: String?,
    val name: String,
    val description: String?,
    val currency: String,
    val propertyCount: Int,
    val totalUnits: Int,
    val occupiedUnits: Int,
    val vacantUnits: Int,
    val totalValue: Double,
    val monthlyIncome: Double,
    val monthlyExpenses: Double,
    val monthlyNOI: Double,
    val occupancyRate: Double,
    val annualROI: Double,
    val active: Boolean,
    val createdAt: String
)


/*
 * =============================================================
 * GROUP
 * =============================================================
 */

data class CreatePortfolioGroupData(
    val name: String,
    val description: String?
)

data class PortfolioGroupData(
    val id: String,
    val portfolioId: String,
    val name: String,
    val description: String?,
    val propertyCount: Int,
    val totalValue: Double,
    val monthlyIncome: Double
)


/*
 * =============================================================
 * PERFORMANCE
 * =============================================================
 */

data class PortfolioPerformanceData(
    val portfolioId: String,
    val propertyCount: Int,
    val totalValue: Double,
    val rentalIncome: Double,
    val otherIncome: Double,
    val operatingExpenses: Double,
    val netOperatingIncome: Double,
    val occupancyRate: Double,
    val collectionRate: Double,
    val outstandingRent: Double,
    val roi: Double,
    val rentalYield: Double
)

data class PropertyPerformanceData(
    val propertyId: String,
    val propertyName: String?,
    val totalValue: Double,
    val rentalIncome: Double,
    val operatingExpenses: Double,
    val netOperatingIncome: Double,
    val occupancyRate: Double,
    val collectionRate: Double,
    val outstandingRent: Double,
    val roi: Double,
    val rentalYield: Double
)

data class PortfolioPerformanceSnapshotData(
    val portfolioId: String,
    val period: String,
    val totalValue: Double,
    val income: Double,
    val expenses: Double,
    val noi: Double,
    val occupancyRate: Double,
    val roi: Double
)


/*
 * =============================================================
 * INCOME
 * =============================================================
 */

data class PortfolioIncomeData(
    val rent: Double,
    val deposits: Double,
    val lateFees: Double,
    val serviceCharges: Double,
    val otherIncome: Double,
    val total: Double
)

data class PropertyIncomeData(
    val propertyId: String,
    val rent: Double,
    val deposits: Double,
    val lateFees: Double,
    val serviceCharges: Double,
    val otherIncome: Double,
    val total: Double
)


/*
 * =============================================================
 * EXPENSES
 * =============================================================
 */

data class PortfolioExpenseData(
    val maintenance: Double,
    val utilities: Double,
    val taxes: Double,
    val insurance: Double,
    val managementFees: Double,
    val repairs: Double,
    val otherExpenses: Double,
    val total: Double
)

data class PropertyExpenseData(
    val propertyId: String,
    val maintenance: Double,
    val utilities: Double,
    val taxes: Double,
    val insurance: Double,
    val managementFees: Double,
    val repairs: Double,
    val otherExpenses: Double,
    val total: Double
)


/*
 * =============================================================
 * OCCUPANCY
 * =============================================================
 */

data class OccupancyData(
    val totalUnits: Int,
    val occupiedUnits: Int,
    val vacantUnits: Int,
    val reservedUnits: Int,
    val occupancyRate: Double,
    val vacancyRate: Double
)


/*
 * =============================================================
 * ARREARS
 * =============================================================
 */

data class PropertyArrearsData(
    val propertyId: String,
    val propertyName: String?,
    val outstandingAmount: Double,
    val tenantCount: Int,
    val oldestDueDate: String?
)


/*
 * =============================================================
 * VALUE
 * =============================================================
 */

data class PortfolioValueData(
    val portfolioId: String,
    val currentValue: Double,
    val acquisitionValue: Double,
    val appreciation: Double,
    val appreciationPercentage: Double,
    val valuationDate: String
)

data class PropertyValueData(
    val propertyId: String,
    val currentValue: Double,
    val acquisitionValue: Double,
    val appreciation: Double,
    val appreciationPercentage: Double,
    val valuationDate: String
)

data class PropertyValuationData(
    val id: String = "",
    val propertyId: String,
    val estimatedValue: Double,
    val valuationMethod: ValuationMethod,
    val valuerName: String?,
    val notes: String?,
    val valuationDate: String
)


/*
 * =============================================================
 * INVESTMENT TARGETS
 * =============================================================
 */

data class CreateInvestmentTargetData(
    val name: String,
    val metric: InvestmentMetric,
    val targetValue: Double,
    val deadline: String?
)

data class UpdateInvestmentTargetData(
    val name: String?,
    val targetValue: Double?,
    val deadline: String?,
    val active: Boolean?
)

data class InvestmentTargetData(
    val id: String,
    val portfolioId: String,
    val name: String,
    val metric: InvestmentMetric,
    val targetValue: Double,
    val currentValue: Double,
    val progressPercentage: Double,
    val deadline: String?,
    val achieved: Boolean,
    val active: Boolean
)


/*
 * =============================================================
 * ALERTS
 * =============================================================
 */

data class PortfolioAlertData(
    val id: String,
    val portfolioId: String,
    val propertyId: String?,
    val type: PortfolioAlertType,
    val title: String,
    val message: String,
    val severity: PortfolioAlertSeverity,
    val createdAt: String,
    val dismissed: Boolean
)


/*
 * =============================================================
 * ANALYTICS
 * =============================================================
 */

data class PortfolioAnalyticsData(
    val totalProperties: Int,
    val totalUnits: Int,
    val occupiedUnits: Int,
    val vacantUnits: Int,
    val occupancyRate: Double,
    val totalPortfolioValue: Double,
    val totalAcquisitionValue: Double,
    val totalAppreciation: Double,
    val monthlyIncome: Double,
    val monthlyExpenses: Double,
    val monthlyNOI: Double,
    val annualROI: Double,
    val rentalYield: Double,
    val collectionRate: Double,
    val outstandingRent: Double,
    val topPerformingPropertyId: String?,
    val underperformingPropertyId: String?
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class ValuationMethod {

    MARKET_COMPARISON,

    INCOME_APPROACH,

    COST_APPROACH,

    PROFESSIONAL_VALUATION,

    OWNER_ESTIMATE,

    OTHER
}

enum class InvestmentMetric {

    PORTFOLIO_VALUE,

    RENTAL_INCOME,

    NOI,

    ROI,

    RENTAL_YIELD,

    OCCUPANCY,

    COLLECTION_RATE,

    PROPERTY_COUNT,

    UNIT_COUNT,

    OTHER
}

enum class PortfolioAlertType {

    HIGH_VACANCY,

    LOW_COLLECTION,

    HIGH_ARREARS,

    LOW_ROI,

    EXPENSE_SPIKE,

    PROPERTY_VALUE_DROP,

    MAINTENANCE_SPIKE,

    LEASE_EXPIRING,

    INSURANCE_EXPIRING,

    TAX_DUE,

    TARGET_MISSED,

    OTHER
}

enum class PortfolioAlertSeverity {

    INFO,

    WARNING,

    HIGH,

    CRITICAL
}

enum class PortfolioExportFormat {

    CSV,

    JSON,

    PDF,

    EXCEL
}