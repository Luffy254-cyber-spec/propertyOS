package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * ANALYTICS REPOSITORY
 * =============================================================
 *
 * Advanced analytics and decision-support layer.
 *
 * Handles:
 *
 * - Revenue analytics
 * - Rent analytics
 * - Occupancy analytics
 * - Vacancy analytics
 * - Arrears analytics
 * - Tenant retention
 * - Property profitability
 * - Maintenance analytics
 * - Staff KPIs
 * - Broker analytics
 * - Technician analytics
 * - Listing analytics
 * - Application conversion
 * - Viewing conversion
 * - Payment analytics
 * - Portfolio analytics
 * - Trend analysis
 * - Forecasting
 * - Anomaly detection
 * - Benchmarking
 * - Management insights
 *
 * =============================================================
 */

interface AnalyticsRepository {

    /*
     * ---------------------------------------------------------
     * GENERAL ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getAnalytics(
        request: AnalyticsRequest
    ): Result<AnalyticsData>

    fun observeAnalytics(
        request: AnalyticsRequest
    ): Flow<Result<AnalyticsData>>

    suspend fun refreshAnalytics(
        scope: AnalyticsScope
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * REVENUE
     * ---------------------------------------------------------
     */

    suspend fun getRevenueAnalytics(
        scope: AnalyticsScope,
        startDate: String,
        endDate: String,
        groupBy: AnalyticsGroupBy = AnalyticsGroupBy.MONTH
    ): Result<RevenueAnalyticsData>

    suspend fun getRevenueTrend(
        scope: AnalyticsScope,
        months: Int = 12
    ): Result<List<RevenueDataPoint>>

    suspend fun getRevenueGrowth(
        scope: AnalyticsScope,
        startDate: String,
        endDate: String
    ): Result<GrowthMetricData>


    /*
     * ---------------------------------------------------------
     * RENT
     * ---------------------------------------------------------
     */

    suspend fun getRentAnalytics(
        scope: AnalyticsScope,
        startDate: String,
        endDate: String
    ): Result<RentAnalyticsData>

    suspend fun getRentCollectionTrend(
        scope: AnalyticsScope,
        months: Int = 12
    ): Result<List<RentCollectionDataPoint>>

    suspend fun getAverageRent(
        scope: AnalyticsScope
    ): Result<Double>

    suspend fun getRentGrowth(
        scope: AnalyticsScope,
        months: Int = 12
    ): Result<GrowthMetricData>


    /*
     * ---------------------------------------------------------
     * ARREARS
     * ---------------------------------------------------------
     */

    suspend fun getArrearsAnalytics(
        scope: AnalyticsScope
    ): Result<ArrearsAnalyticsData>

    suspend fun getArrearsTrend(
        scope: AnalyticsScope,
        months: Int = 12
    ): Result<List<ArrearsDataPoint>>

    suspend fun getHighestArrearsProperties(
        scope: AnalyticsScope,
        limit: Int = 10
    ): Result<List<GeneralPropertyAnalyticsData>>

    suspend fun getHighestArrearsTenants(
        scope: AnalyticsScope,
        limit: Int = 10
    ): Result<List<TenantAnalyticsData>>


    /*
     * ---------------------------------------------------------
     * OCCUPANCY
     * ---------------------------------------------------------
     */

    suspend fun getOccupancyAnalytics(
        scope: AnalyticsScope
    ): Result<OccupancyAnalyticsData>

    suspend fun getOccupancyTrend(
        scope: AnalyticsScope,
        months: Int = 12
    ): Result<List<OccupancyDataPoint>>

    suspend fun getVacancyTrend(
        scope: AnalyticsScope,
        months: Int = 12
    ): Result<List<VacancyDataPoint>>

    suspend fun getLongestVacantUnits(
        scope: AnalyticsScope,
        limit: Int = 10
    ): Result<List<VacantUnitAnalyticsData>>


    /*
     * ---------------------------------------------------------
     * TENANT ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getTenantAnalytics(
        scope: AnalyticsScope
    ): Result<TenantAnalyticsSummaryData>

    suspend fun getTenantRetentionRate(
        scope: AnalyticsScope,
        months: Int = 12
    ): Result<Double>

    suspend fun getTenantTurnoverRate(
        scope: AnalyticsScope,
        months: Int = 12
    ): Result<Double>

    suspend fun getAverageTenantStay(
        scope: AnalyticsScope
    ): Result<Double>

    suspend fun getMoveInTrend(
        scope: AnalyticsScope,
        months: Int = 12
    ): Result<List<TenantMovementDataPoint>>

    suspend fun getMoveOutTrend(
        scope: AnalyticsScope,
        months: Int = 12
    ): Result<List<TenantMovementDataPoint>>


    /*
     * ---------------------------------------------------------
     * PROPERTY ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getPropertyAnalytics(
        propertyId: String,
        startDate: String?,
        endDate: String?
    ): Result<GeneralPropertyAnalyticsData>

    suspend fun getPropertyRankings(
        scope: AnalyticsScope,
        metric: PropertyRankingMetric,
        limit: Int = 10
    ): Result<List<GeneralPropertyAnalyticsData>>

    suspend fun getUnderperformingProperties(
        scope: AnalyticsScope,
        limit: Int = 10
    ): Result<List<GeneralPropertyAnalyticsData>>

    suspend fun getMostProfitableProperties(
        scope: AnalyticsScope,
        limit: Int = 10
    ): Result<List<GeneralPropertyAnalyticsData>>


    /*
     * ---------------------------------------------------------
     * PROFITABILITY
     * ---------------------------------------------------------
     */

    suspend fun getProfitabilityAnalytics(
        scope: AnalyticsScope,
        startDate: String,
        endDate: String
    ): Result<ProfitabilityAnalyticsData>

    suspend fun getPropertyProfitability(
        propertyId: String,
        startDate: String,
        endDate: String
    ): Result<PropertyProfitabilityData>

    suspend fun getProfitabilityTrend(
        scope: AnalyticsScope,
        months: Int = 12
    ): Result<List<ProfitabilityDataPoint>>


    /*
     * ---------------------------------------------------------
     * MAINTENANCE ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getMaintenanceAnalytics(
        scope: AnalyticsScope,
        startDate: String,
        endDate: String
    ): Result<MaintenanceAnalyticsData>

    suspend fun getMaintenanceTrend(
        scope: AnalyticsScope,
        months: Int = 12
    ): Result<List<MaintenanceDataPoint>>

    suspend fun getMostProblematicProperties(
        scope: AnalyticsScope,
        limit: Int = 10
    ): Result<List<PropertyMaintenanceAnalyticsData>>

    suspend fun getMaintenanceCostTrend(
        scope: AnalyticsScope,
        months: Int = 12
    ): Result<List<CostDataPoint>>

    suspend fun getAverageMaintenanceResolutionTime(
        scope: AnalyticsScope
    ): Result<Double>


    /*
     * ---------------------------------------------------------
     * TECHNICIAN ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getTechnicianAnalytics(
        technicianId: String,
        startDate: String,
        endDate: String
    ): Result<TechnicianAnalyticsData>

    suspend fun rankTechnicians(
        scope: AnalyticsScope,
        limit: Int = 10
    ): Result<List<TechnicianAnalyticsData>>


    /*
     * ---------------------------------------------------------
     * STAFF ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getStaffAnalytics(
        staffId: String,
        startDate: String,
        endDate: String
    ): Result<StaffAnalyticsData>

    suspend fun getStaffKPIs(
        scope: AnalyticsScope,
        startDate: String,
        endDate: String
    ): Result<List<StaffKPIData>>

    suspend fun rankStaff(
        scope: AnalyticsScope,
        metric: StaffRankingMetric,
        limit: Int = 10
    ): Result<List<StaffKPIData>>


    /*
     * ---------------------------------------------------------
     * BROKER ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getBrokerAnalytics(
        brokerId: String,
        startDate: String,
        endDate: String
    ): Result<BrokerAnalyticsData>

    suspend fun rankBrokers(
        scope: AnalyticsScope,
        limit: Int = 10
    ): Result<List<BrokerAnalyticsData>>


    /*
     * ---------------------------------------------------------
     * LISTING ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getListingAnalytics(
        scope: AnalyticsScope,
        startDate: String,
        endDate: String
    ): Result<ListingAnalyticsData>

    suspend fun getTopListings(
        scope: AnalyticsScope,
        limit: Int = 10
    ): Result<List<ListingPerformanceData>>

    suspend fun getListingConversionRate(
        scope: AnalyticsScope
    ): Result<Double>


    /*
     * ---------------------------------------------------------
     * APPLICATION ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getApplicationAnalytics(
        scope: AnalyticsScope,
        startDate: String,
        endDate: String
    ): Result<ApplicationAnalyticsData>

    suspend fun getApplicationConversionRate(
        scope: AnalyticsScope
    ): Result<Double>


    /*
     * ---------------------------------------------------------
     * VIEWING ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getViewingAnalytics(
        scope: AnalyticsScope,
        startDate: String,
        endDate: String
    ): Result<GeneralViewingAnalyticsData>

    suspend fun getViewingConversionRate(
        scope: AnalyticsScope
    ): Result<Double>


    /*
     * ---------------------------------------------------------
     * PAYMENT ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getPaymentAnalytics(
        scope: AnalyticsScope,
        startDate: String,
        endDate: String
    ): Result<PaymentAnalyticsData>

    suspend fun getPaymentMethodBreakdown(
        scope: AnalyticsScope,
        startDate: String,
        endDate: String
    ): Result<List<PaymentMethodAnalyticsData>>

    suspend fun getPaymentFailureRate(
        scope: AnalyticsScope,
        startDate: String,
        endDate: String
    ): Result<Double>


    /*
     * ---------------------------------------------------------
     * PORTFOLIO
     * ---------------------------------------------------------
     */

    suspend fun getPortfolioAnalytics(
        portfolioId: String,
        startDate: String?,
        endDate: String?
    ): Result<PortfolioAnalyticsResultData>

    suspend fun comparePortfolios(
        portfolioIds: List<String>,
        metric: PortfolioRankingMetric
    ): Result<List<PortfolioComparisonData>>


    /*
     * ---------------------------------------------------------
     * FORECASTING
     * ---------------------------------------------------------
     */

    suspend fun forecastRevenue(
        scope: AnalyticsScope,
        monthsAhead: Int
    ): Result<ForecastData>

    suspend fun forecastOccupancy(
        scope: AnalyticsScope,
        monthsAhead: Int
    ): Result<ForecastData>

    suspend fun forecastRentCollection(
        scope: AnalyticsScope,
        monthsAhead: Int
    ): Result<ForecastData>

    suspend fun forecastExpenses(
        scope: AnalyticsScope,
        monthsAhead: Int
    ): Result<ForecastData>

    suspend fun forecastVacancy(
        scope: AnalyticsScope,
        monthsAhead: Int
    ): Result<ForecastData>


    /*
     * ---------------------------------------------------------
     * ANOMALY DETECTION
     * ---------------------------------------------------------
     */

    suspend fun detectAnomalies(
        scope: AnalyticsScope,
        startDate: String,
        endDate: String
    ): Result<List<AnalyticsAnomalyData>>

    suspend fun getActiveAnomalies(
        scope: AnalyticsScope
    ): Result<List<AnalyticsAnomalyData>>

    suspend fun dismissAnomaly(
        actorId: String,
        anomalyId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * BENCHMARKING
     * ---------------------------------------------------------
     */

    suspend fun benchmarkProperty(
        propertyId: String
    ): Result<PropertyBenchmarkData>

    suspend fun benchmarkPortfolio(
        portfolioId: String
    ): Result<PortfolioBenchmarkData>


    /*
     * ---------------------------------------------------------
     * INSIGHTS
     * ---------------------------------------------------------
     */

    suspend fun getManagementInsights(
        scope: AnalyticsScope
    ): Result<List<ManagementInsightData>>

    suspend fun getPropertyInsights(
        propertyId: String
    ): Result<List<ManagementInsightData>>

    suspend fun getTenantInsights(
        tenantId: String
    ): Result<List<ManagementInsightData>>


    /*
     * ---------------------------------------------------------
     * HEATMAPS
     * ---------------------------------------------------------
     */

    suspend fun getMaintenanceHeatmap(
        scope: AnalyticsScope
    ): Result<List<HeatmapData>>

    suspend fun getOccupancyHeatmap(
        scope: AnalyticsScope
    ): Result<List<HeatmapData>>

    suspend fun getRevenueHeatmap(
        scope: AnalyticsScope
    ): Result<List<HeatmapData>>


    /*
     * ---------------------------------------------------------
     * ANALYTICS SNAPSHOTS
     * ---------------------------------------------------------
     */

    suspend fun createSnapshot(
        actorId: String,
        scope: AnalyticsScope
    ): Result<String>

    suspend fun getSnapshots(
        scope: AnalyticsScope,
        limit: Int = 12
    ): Result<List<AnalyticsSnapshotData>>
}


/*
 * =============================================================
 * REQUEST
 * =============================================================
 */

data class AnalyticsRequest(
    val scope: AnalyticsScope,
    val metrics: List<AnalyticsMetric>,
    val startDate: String?,
    val endDate: String?,
    val groupBy: AnalyticsGroupBy = AnalyticsGroupBy.MONTH
)


/*
 * =============================================================
 * SCOPE
 * =============================================================
 */

data class AnalyticsScope(
    val userId: String? = null,
    val organizationId: String? = null,
    val portfolioId: String? = null,
    val propertyId: String? = null,
    val branchId: String? = null,
    val teamId: String? = null
)


/*
 * =============================================================
 * GENERAL DATA
 * =============================================================
 */

data class AnalyticsData(
    val scope: AnalyticsScope,
    val metrics: Map<String, Double>,
    val trends: List<AnalyticsDataPoint>,
    val generatedAt: String
)

data class AnalyticsDataPoint(
    val label: String,
    val value: Double
)


/*
 * =============================================================
 * REVENUE
 * =============================================================
 */

data class RevenueAnalyticsData(
    val totalRevenue: Double,
    val rentalRevenue: Double,
    val otherRevenue: Double,
    val averageMonthlyRevenue: Double,
    val growthRate: Double
)

data class RevenueDataPoint(
    val period: String,
    val revenue: Double,
    val rentalRevenue: Double,
    val otherRevenue: Double
)


/*
 * =============================================================
 * GROWTH
 * =============================================================
 */

data class GrowthMetricData(
    val currentValue: Double,
    val previousValue: Double,
    val growthPercentage: Double,
    val direction: TrendDirection
)


/*
 * =============================================================
 * RENT
 * =============================================================
 */

data class RentAnalyticsData(
    val expectedRent: Double,
    val collectedRent: Double,
    val outstandingRent: Double,
    val collectionRate: Double,
    val averageRent: Double,
    val growthRate: Double
)

data class RentCollectionDataPoint(
    val period: String,
    val expected: Double,
    val collected: Double,
    val outstanding: Double,
    val collectionRate: Double
)


/*
 * =============================================================
 * ARREARS
 * =============================================================
 */

data class ArrearsAnalyticsData(
    val totalArrears: Double,
    val tenantsWithArrears: Int,
    val averageArrears: Double,
    val largestArrears: Double,
    val averageDaysOverdue: Double
)

data class ArrearsDataPoint(
    val period: String,
    val amount: Double,
    val tenantCount: Int
)


/*
 * =============================================================
 * OCCUPANCY
 * =============================================================
 */

data class OccupancyAnalyticsData(
    val totalUnits: Int,
    val occupiedUnits: Int,
    val vacantUnits: Int,
    val reservedUnits: Int,
    val occupancyRate: Double,
    val vacancyRate: Double
)

data class OccupancyDataPoint(
    val period: String,
    val occupancyRate: Double,
    val occupiedUnits: Int,
    val vacantUnits: Int
)

data class VacancyDataPoint(
    val period: String,
    val vacancyRate: Double,
    val vacantUnits: Int
)

data class VacantUnitAnalyticsData(
    val unitId: String,
    val propertyId: String,
    val propertyName: String?,
    val vacantSince: String,
    val daysVacant: Int,
    val monthlyRent: Double
)


/*
 * =============================================================
 * TENANTS
 * =============================================================
 */

data class TenantAnalyticsSummaryData(
    val totalTenants: Int,
    val activeTenants: Int,
    val newTenants: Int,
    val movedOutTenants: Int,
    val retentionRate: Double,
    val turnoverRate: Double,
    val averageStayMonths: Double
)

data class TenantMovementDataPoint(
    val period: String,
    val count: Int
)

data class TenantAnalyticsData(
    val tenantId: String,
    val tenantName: String?,
    val propertyId: String?,
    val paymentReliability: Double,
    val totalPaid: Double,
    val outstanding: Double,
    val daysOverdue: Int,
    val maintenanceRequests: Int,
    val leaseMonthsRemaining: Int?
)


/*
 * =============================================================
 * PROPERTY
 * =============================================================
 */

data class GeneralPropertyAnalyticsData(
    val propertyId: String,
    val propertyName: String?,
    val totalUnits: Int,
    val occupiedUnits: Int,
    val vacantUnits: Int,
    val occupancyRate: Double,
    val monthlyIncome: Double,
    val monthlyExpenses: Double,
    val monthlyNOI: Double,
    val roi: Double,
    val rentalYield: Double,
    val totalArrears: Double,
    val maintenanceCost: Double,
    val performanceScore: Double
)


/*
 * =============================================================
 * PROFITABILITY
 * =============================================================
 */

data class ProfitabilityAnalyticsData(
    val totalIncome: Double,
    val totalExpenses: Double,
    val noi: Double,
    val roi: Double,
    val rentalYield: Double,
    val profitMargin: Double
)


data class ProfitabilityDataPoint(
    val period: String,
    val income: Double,
    val expenses: Double,
    val noi: Double
)


/*
 * =============================================================
 * MAINTENANCE
 * =============================================================
 */

data class MaintenanceAnalyticsData(
    val totalRequests: Int,
    val completedRequests: Int,
    val openRequests: Int,
    val overdueRequests: Int,
    val urgentRequests: Int,
    val totalCost: Double,
    val averageCost: Double,
    val averageResolutionHours: Double
)

data class MaintenanceDataPoint(
    val period: String,
    val requests: Int,
    val completed: Int,
    val cost: Double
)

data class PropertyMaintenanceAnalyticsData(
    val propertyId: String,
    val propertyName: String?,
    val requestCount: Int,
    val totalCost: Double,
    val averageResolutionHours: Double,
    val repeatIssueRate: Double
)

data class CostDataPoint(
    val period: String,
    val cost: Double
)


/*
 * =============================================================
 * TECHNICIAN
 * =============================================================
 */

data class TechnicianAnalyticsData(
    val technicianId: String,
    val technicianName: String?,
    val jobsAssigned: Int,
    val jobsCompleted: Int,
    val jobsCancelled: Int,
    val completionRate: Double,
    val averageResolutionHours: Double,
    val averageRating: Double,
    val earnings: Double
)


/*
 * =============================================================
 * STAFF
 * =============================================================
 */


data class StaffKPIData(
    val staffId: String,
    val staffName: String?,
    val role: String?,
    val tasksCompleted: Int,
    val completionRate: Double,
    val responseTimeMinutes: Double,
    val performanceScore: Double
)


/*
 * =============================================================
 * BROKER
 * =============================================================
 */

data class BrokerAnalyticsData(
    val brokerId: String,
    val brokerName: String?,
    val listings: Int,
    val leads: Int,
    val viewings: Int,
    val applications: Int,
    val placements: Int,
    val conversionRate: Double,
    val commissionEarned: Double,
    val averageRating: Double
)


/*
 * =============================================================
 * LISTINGS
 * =============================================================
 */

data class ListingAnalyticsData(
    val totalListings: Int,
    val activeListings: Int,
    val expiredListings: Int,
    val totalViews: Int,
    val uniqueViewers: Int,
    val leads: Int,
    val applications: Int,
    val conversionRate: Double
)

data class ListingPerformanceData(
    val listingId: String,
    val propertyId: String,
    val title: String?,
    val views: Int,
    val leads: Int,
    val applications: Int,
    val conversionRate: Double
)


/*
 * =============================================================
 * APPLICATIONS
 * =============================================================
 */



/*
 * =============================================================
 * VIEWINGS
 * =============================================================
 */

data class GeneralViewingAnalyticsData(
    val totalViewings: Int,
    val completedViewings: Int,
    val cancelledViewings: Int,
    val noShows: Int,
    val applicationsGenerated: Int,
    val conversionRate: Double,
    val noShowRate: Double
)


/*
 * =============================================================
 * PAYMENTS
 * =============================================================
 */

data class PaymentAnalyticsData(
    val totalTransactions: Int,
    val successfulTransactions: Int,
    val failedTransactions: Int,
    val totalAmount: Double,
    val averageTransaction: Double,
    val successRate: Double,
    val failureRate: Double
)

data class PaymentMethodAnalyticsData(
    val method: String,
    val transactionCount: Int,
    val amount: Double,
    val percentage: Double
)


/*
 * =============================================================
 * PORTFOLIO
 * =============================================================
 */

data class PortfolioAnalyticsResultData(
    val portfolioId: String,
    val value: Double,
    val income: Double,
    val expenses: Double,
    val noi: Double,
    val occupancyRate: Double,
    val collectionRate: Double,
    val roi: Double,
    val rentalYield: Double,
    val performanceScore: Double
)

data class PortfolioComparisonData(
    val portfolioId: String,
    val portfolioName: String?,
    val metric: PortfolioRankingMetric,
    val value: Double,
    val rank: Int
)


/*
 * =============================================================
 * FORECAST
 * =============================================================
 */

data class ForecastData(
    val metric: AnalyticsMetric,
    val historical: List<AnalyticsDataPoint>,
    val forecast: List<ForecastDataPoint>,
    val confidencePercentage: Double,
    val generatedAt: String
)

data class ForecastDataPoint(
    val period: String,
    val predictedValue: Double,
    val lowerBound: Double?,
    val upperBound: Double?
)


/*
 * =============================================================
 * ANOMALIES
 * =============================================================
 */

data class AnalyticsAnomalyData(
    val id: String,
    val type: AnalyticsAnomalyType,
    val title: String,
    val description: String,
    val severity: AnalyticsSeverity,
    val metric: AnalyticsMetric,
    val expectedValue: Double?,
    val actualValue: Double?,
    val detectedAt: String,
    val resourceId: String?,
    val dismissed: Boolean
)


/*
 * =============================================================
 * BENCHMARKS
 * =============================================================
 */

data class PropertyBenchmarkData(
    val propertyId: String,
    val occupancyRate: Double,
    val averageMarketOccupancy: Double,
    val rentalYield: Double,
    val averageMarketYield: Double,
    val roi: Double,
    val averageMarketROI: Double,
    val performanceScore: Double
)

data class PortfolioBenchmarkData(
    val portfolioId: String,
    val occupancyRate: Double,
    val marketOccupancyRate: Double,
    val rentalYield: Double,
    val marketRentalYield: Double,
    val roi: Double,
    val marketROI: Double,
    val performanceScore: Double
)


/*
 * =============================================================
 * INSIGHTS
 * =============================================================
 */

data class ManagementInsightData(
    val id: String,
    val category: InsightCategory,
    val title: String,
    val description: String,
    val priority: InsightPriority,
    val estimatedImpact: Double?,
    val recommendedAction: String?,
    val resourceId: String?,
    val createdAt: String
)


/*
 * =============================================================
 * HEATMAP
 * =============================================================
 */

data class HeatmapData(
    val label: String,
    val value: Double,
    val latitude: Double?,
    val longitude: Double?
)


/*
 * =============================================================
 * SNAPSHOT
 * =============================================================
 */

data class AnalyticsSnapshotData(
    val id: String,
    val scope: AnalyticsScope,
    val period: String,
    val metrics: Map<String, Double>,
    val createdAt: String
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class AnalyticsMetric {

    REVENUE,

    RENT,

    COLLECTION_RATE,

    ARREARS,

    OCCUPANCY,

    VACANCY,

    TENANT_RETENTION,

    TENANT_TURNOVER,

    PROPERTY_VALUE,

    NOI,

    ROI,

    RENTAL_YIELD,

    EXPENSES,

    MAINTENANCE_COST,

    MAINTENANCE_REQUESTS,

    STAFF_PERFORMANCE,

    TECHNICIAN_PERFORMANCE,

    BROKER_PERFORMANCE,

    LISTING_VIEWS,

    APPLICATIONS,

    VIEWINGS,

    PAYMENT_VOLUME,

    PAYMENT_FAILURES,

    PORTFOLIO_VALUE
}


enum class AnalyticsGroupBy {

    DAY,

    WEEK,

    MONTH,

    QUARTER,

    YEAR,

    PROPERTY,

    UNIT,

    TENANT,

    STAFF,

    TECHNICIAN,

    BROKER
}


enum class PropertyRankingMetric {

    REVENUE,

    PROFIT,

    NOI,

    ROI,

    RENTAL_YIELD,

    OCCUPANCY,

    COLLECTION_RATE,

    PROPERTY_VALUE
}


enum class StaffRankingMetric {

    TASKS_COMPLETED,

    COMPLETION_RATE,

    RESPONSE_TIME,

    PERFORMANCE_SCORE
}


enum class PortfolioRankingMetric {

    VALUE,

    REVENUE,

    NOI,

    ROI,

    RENTAL_YIELD,

    OCCUPANCY,

    COLLECTION_RATE
}


enum class AnalyticsAnomalyType {

    REVENUE_DROP,

    REVENUE_SPIKE,

    EXPENSE_SPIKE,

    ARREARS_SPIKE,

    OCCUPANCY_DROP,

    OCCUPANCY_SPIKE,

    MAINTENANCE_SPIKE,

    PAYMENT_FAILURE_SPIKE,

    UNUSUAL_PAYMENT,

    UNUSUAL_ACTIVITY,

    PROPERTY_UNDERPERFORMING
}


enum class AnalyticsSeverity {

    INFO,

    WARNING,

    HIGH,

    CRITICAL
}


enum class InsightCategory {

    REVENUE,

    EXPENSE,

    OCCUPANCY,

    TENANT,

    PROPERTY,

    MAINTENANCE,

    PAYMENT,

    STAFF,

    LISTING,

    PORTFOLIO,

    SECURITY,

    OPERATIONS
}


enum class InsightPriority {

    LOW,

    MEDIUM,

    HIGH,

    CRITICAL
}


enum class TrendDirection {

    UP,

    DOWN,

    STABLE
}