package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * DASHBOARD REPOSITORY
 * =============================================================
 *
 * Central dashboard aggregation layer.
 *
 * Supports:
 *
 * - Tenant dashboard
 * - Landlord dashboard
 * - Property manager dashboard
 * - Caretaker dashboard
 * - Broker dashboard
 * - Technician dashboard
 * - Accountant dashboard
 * - Support dashboard
 * - Security dashboard
 * - Admin dashboard
 * - Organization dashboard
 *
 * The repository aggregates information from:
 *
 * - Properties
 * - Units
 * - Tenants
 * - Payments
 * - Bills
 * - Maintenance
 * - Agreements
 * - Messages
 * - Listings
 * - Viewings
 * - Applications
 * - Notifications
 * - Staff
 * - Tasks
 * - Portfolio
 * - Audit logs
 *
 * =============================================================
 */

interface DashboardRepository {

    /*
     * ---------------------------------------------------------
     * GENERIC DASHBOARD
     * ---------------------------------------------------------
     */

    suspend fun getDashboard(
        userId: String,
        role: DashboardRole
    ): Result<DashboardData>

    fun observeDashboard(
        userId: String,
        role: DashboardRole
    ): Flow<Result<DashboardData>>


    /*
     * ---------------------------------------------------------
     * TENANT
     * ---------------------------------------------------------
     */

    suspend fun getTenantDashboard(
        userId: String
    ): Result<GenericTenantDashboardData>

    fun observeTenantDashboard(
        userId: String
    ): Flow<Result<GenericTenantDashboardData>>


    /*
     * ---------------------------------------------------------
     * LANDLORD
     * ---------------------------------------------------------
     */

    suspend fun getLandlordDashboard(
        userId: String
    ): Result<GenericLandlordDashboardData>

    fun observeLandlordDashboard(
        userId: String
    ): Flow<Result<GenericLandlordDashboardData>>


    /*
     * ---------------------------------------------------------
     * PROPERTY MANAGER
     * ---------------------------------------------------------
     */

    suspend fun getPropertyManagerDashboard(
        userId: String,
        organizationId: String?
    ): Result<PropertyManagerDashboardData>

    fun observePropertyManagerDashboard(
        userId: String,
        organizationId: String?
    ): Flow<Result<PropertyManagerDashboardData>>


    /*
     * ---------------------------------------------------------
     * CARETAKER
     * ---------------------------------------------------------
     */

    suspend fun getCaretakerDashboard(
        userId: String
    ): Result<CaretakerDashboardData>

    fun observeCaretakerDashboard(
        userId: String
    ): Flow<Result<CaretakerDashboardData>>


    /*
     * ---------------------------------------------------------
     * BROKER
     * ---------------------------------------------------------
     */

    suspend fun getBrokerDashboard(
        userId: String
    ): Result<BrokerDashboardData>

    fun observeBrokerDashboard(
        userId: String
    ): Flow<Result<BrokerDashboardData>>


    /*
     * ---------------------------------------------------------
     * TECHNICIAN
     * ---------------------------------------------------------
     */

    suspend fun getTechnicianDashboard(
        userId: String
    ): Result<TechnicianDashboardData>

    fun observeTechnicianDashboard(
        userId: String
    ): Flow<Result<TechnicianDashboardData>>


    /*
     * ---------------------------------------------------------
     * ACCOUNTANT
     * ---------------------------------------------------------
     */

    suspend fun getAccountantDashboard(
        userId: String,
        organizationId: String?
    ): Result<AccountantDashboardData>

    fun observeAccountantDashboard(
        userId: String,
        organizationId: String?
    ): Flow<Result<AccountantDashboardData>>


    /*
     * ---------------------------------------------------------
     * SUPPORT
     * ---------------------------------------------------------
     */

    suspend fun getSupportDashboard(
        userId: String,
        organizationId: String?
    ): Result<SupportDashboardData>


    /*
     * ---------------------------------------------------------
     * SECURITY
     * ---------------------------------------------------------
     */

    suspend fun getSecurityDashboard(
        userId: String,
        organizationId: String?
    ): Result<GenericSecurityDashboardData>


    /*
     * ---------------------------------------------------------
     * ADMIN
     * ---------------------------------------------------------
     */

    suspend fun getAdminDashboard(
        userId: String
    ): Result<AdminDashboardData>

    fun observeAdminDashboard(
        userId: String
    ): Flow<Result<AdminDashboardData>>


    /*
     * ---------------------------------------------------------
     * ORGANIZATION
     * ---------------------------------------------------------
     */

    suspend fun getOrganizationDashboard(
        organizationId: String
    ): Result<OrganizationDashboardData>

    fun observeOrganizationDashboard(
        organizationId: String
    ): Flow<Result<OrganizationDashboardData>>


    /*
     * ---------------------------------------------------------
     * REFRESH
     * ---------------------------------------------------------
     */

    suspend fun refreshDashboard(
        userId: String,
        role: DashboardRole
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * QUICK STATS
     * ---------------------------------------------------------
     */

    suspend fun getQuickStats(
        userId: String,
        role: DashboardRole
    ): Result<DashboardQuickStats>


    /*
     * ---------------------------------------------------------
     * RECENT ACTIVITY
     * ---------------------------------------------------------
     */

    suspend fun getRecentActivity(
        userId: String,
        limit: Int = 20
    ): Result<List<DashboardActivityData>>


    /*
     * ---------------------------------------------------------
     * ALERTS
     * ---------------------------------------------------------
     */

    suspend fun getDashboardAlerts(
        userId: String,
        role: DashboardRole
    ): Result<List<DashboardAlertData>>

    suspend fun dismissDashboardAlert(
        userId: String,
        alertId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * WIDGETS
     * ---------------------------------------------------------
     */

    suspend fun getDashboardWidgets(
        userId: String,
        role: DashboardRole
    ): Result<List<DashboardWidgetData>>

    suspend fun updateWidgetConfiguration(
        userId: String,
        widgetId: String,
        configuration: Map<String, String>
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * SEARCH
     * ---------------------------------------------------------
     */

    suspend fun searchDashboard(
        userId: String,
        query: String
    ): Result<List<DashboardSearchResultData>>
}


/*
 * =============================================================
 * GENERIC DASHBOARD
 * =============================================================
 */

data class DashboardData(
    val userId: String,
    val role: DashboardRole,
    val greeting: String,
    val quickStats: DashboardQuickStats,
    val alerts: List<DashboardAlertData>,
    val activities: List<DashboardActivityData>,
    val widgets: List<DashboardWidgetData>,
    val generatedAt: String
)


/*
 * =============================================================
 * TENANT DASHBOARD
 * =============================================================
 */

data class GenericTenantDashboardData(
    val tenantId: String,
    val tenantName: String?,
    val propertyId: String?,
    val propertyName: String?,
    val unitId: String?,
    val unitName: String?,
    val monthlyRent: Double,
    val outstandingRent: Double,
    val upcomingPayments: List<UpcomingPaymentData>,
    val activeBills: List<DashboardBillData>,
    val maintenanceRequests: List<DashboardMaintenanceData>,
    val lease: DashboardLeaseData?,
    val agreement: DashboardAgreementData?,
    val notifications: Int,
    val unreadMessages: Int,
    val upcomingViewings: Int,
    val recommendations: List<PropertyRecommendationData>
)


/*
 * =============================================================
 * LANDLORD DASHBOARD
 * =============================================================
 */

data class GenericLandlordDashboardData(
    val landlordId: String,
    val landlordName: String?,
    val totalProperties: Int,
    val totalUnits: Int,
    val occupiedUnits: Int,
    val vacantUnits: Int,
    val occupancyRate: Double,
    val expectedRent: Double,
    val collectedRent: Double,
    val outstandingRent: Double,
    val collectionRate: Double,
    val totalExpenses: Double,
    val netIncome: Double,
    val openMaintenance: Int,
    val activeListings: Int,
    val pendingApplications: Int,
    val upcomingLeaseExpiries: Int,
    val unreadMessages: Int,
    val pendingTasks: Int,
    val portfolioValue: Double,
    val roi: Double,
    val alerts: List<DashboardAlertData>,
    val topProperties: List<PropertyPerformanceSummaryData>
)


/*
 * =============================================================
 * PROPERTY MANAGER
 * =============================================================
 */

data class PropertyManagerDashboardData(
    val managerId: String,
    val organizationId: String?,
    val propertiesManaged: Int,
    val totalUnits: Int,
    val occupiedUnits: Int,
    val vacantUnits: Int,
    val occupancyRate: Double,
    val tenants: Int,
    val rentExpected: Double,
    val rentCollected: Double,
    val rentOutstanding: Double,
    val collectionRate: Double,
    val maintenanceOpen: Int,
    val maintenanceOverdue: Int,
    val pendingApplications: Int,
    val pendingViewings: Int,
    val expiringLeases: Int,
    val staffCount: Int,
    val pendingApprovals: Int,
    val unresolvedComplaints: Int,
    val activeListings: Int,
    val tasksDueToday: Int,
    val alerts: List<DashboardAlertData>
)


/*
 * =============================================================
 * CARETAKER
 * =============================================================
 */

data class CaretakerDashboardData(
    val caretakerId: String,
    val propertiesAssigned: Int,
    val unitsManaged: Int,
    val occupiedUnits: Int,
    val vacantUnits: Int,
    val tenants: Int,
    val maintenanceOpen: Int,
    val maintenanceAssigned: Int,
    val maintenanceUrgent: Int,
    val inspectionsToday: Int,
    val inspectionsUpcoming: Int,
    val tasksToday: Int,
    val tasksOverdue: Int,
    val tenantComplaints: Int,
    val unreadMessages: Int,
    val visitorsExpected: Int,
    val alerts: List<DashboardAlertData>
)


/*
 * =============================================================
 * BROKER
 * =============================================================
 */

data class BrokerDashboardData(
    val brokerId: String,
    val activeListings: Int,
    val totalViews: Int,
    val newLeads: Int,
    val activeLeads: Int,
    val scheduledViewings: Int,
    val completedViewings: Int,
    val cancelledViewings: Int,
    val pendingApplications: Int,
    val successfulPlacements: Int,
    val commissionExpected: Double,
    val commissionEarned: Double,
    val commissionPending: Double,
    val unreadMessages: Int,
    val alerts: List<DashboardAlertData>
)


/*
 * =============================================================
 * TECHNICIAN
 * =============================================================
 */

data class TechnicianDashboardData(
    val technicianId: String,
    val activeJobs: Int,
    val assignedJobs: Int,
    val urgentJobs: Int,
    val jobsToday: Int,
    val completedJobs: Int,
    val cancelledJobs: Int,
    val pendingQuotes: Int,
    val approvedQuotes: Int,
    val earningsExpected: Double,
    val earningsReceived: Double,
    val ratingsAverage: Double,
    val unreadMessages: Int,
    val alerts: List<DashboardAlertData>
)


/*
 * =============================================================
 * ACCOUNTANT
 * =============================================================
 */

data class AccountantDashboardData(
    val accountantId: String,
    val organizationId: String?,
    val rentExpected: Double,
    val rentCollected: Double,
    val rentOutstanding: Double,
    val collectionRate: Double,
    val expenses: Double,
    val income: Double,
    val netIncome: Double,
    val pendingPayments: Int,
    val failedPayments: Int,
    val refundsPending: Int,
    val invoicesOutstanding: Int,
    val billsDue: Double,
    val taxesDue: Double,
    val reconciliationsPending: Int,
    val financialAlerts: List<DashboardAlertData>
)


/*
 * =============================================================
 * SUPPORT
 * =============================================================
 */

data class SupportDashboardData(
    val supportAgentId: String,
    val openTickets: Int,
    val urgentTickets: Int,
    val assignedTickets: Int,
    val unassignedTickets: Int,
    val ticketsToday: Int,
    val resolvedToday: Int,
    val averageResponseMinutes: Double,
    val averageResolutionHours: Double,
    val customerSatisfaction: Double,
    val unreadMessages: Int,
    val alerts: List<DashboardAlertData>
)


/*
 * =============================================================
 * SECURITY
 * =============================================================
 */

data class GenericSecurityDashboardData(
    val securityUserId: String,
    val propertiesAssigned: Int,
    val visitorsToday: Int,
    val expectedVisitors: Int,
    val activeVisitors: Int,
    val incidentsToday: Int,
    val unresolvedIncidents: Int,
    val accessRequests: Int,
    val suspiciousActivities: Int,
    val alerts: List<DashboardAlertData>
)


/*
 * =============================================================
 * ADMIN
 * =============================================================
 */

data class AdminDashboardData(
    val adminId: String,
    val totalUsers: Int,
    val activeUsers: Int,
    val newUsersToday: Int,
    val totalOrganizations: Int,
    val verifiedOrganizations: Int,
    val totalProperties: Int,
    val totalUnits: Int,
    val totalTenants: Int,
    val activeListings: Int,
    val activePayments: Int,
    val paymentVolumeToday: Double,
    val openTickets: Int,
    val pendingVerifications: Int,
    val pendingReports: Int,
    val securityAlerts: Int,
    val systemErrors: Int,
    val suspiciousActivities: Int,
    val revenueToday: Double,
    val monthlyRevenue: Double,
    val platformHealth: PlatformHealthData,
    val alerts: List<DashboardAlertData>
)


/*
 * =============================================================
 * ORGANIZATION DASHBOARD
 * =============================================================
 */

data class OrganizationDashboardData(
    val organizationId: String,
    val organizationName: String?,
    val properties: Int,
    val units: Int,
    val occupiedUnits: Int,
    val vacantUnits: Int,
    val occupancyRate: Double,
    val tenants: Int,
    val staff: Int,
    val branches: Int,
    val teams: Int,
    val rentExpected: Double,
    val rentCollected: Double,
    val outstandingRent: Double,
    val collectionRate: Double,
    val expenses: Double,
    val netIncome: Double,
    val openMaintenance: Int,
    val activeListings: Int,
    val pendingApplications: Int,
    val portfolioValue: Double,
    val roi: Double,
    val alerts: List<DashboardAlertData>
)


/*
 * =============================================================
 * QUICK STATS
 * =============================================================
 */

data class DashboardQuickStats(
    val primaryMetric: DashboardMetricData,
    val secondaryMetrics: List<DashboardMetricData>
)

data class DashboardMetricData(
    val title: String,
    val value: String,
    val numericValue: Double?,
    val unit: String?,
    val trendPercentage: Double?,
    val trendDirection: DashboardTrendDirection?,
    val icon: String?
)


/*
 * =============================================================
 * PAYMENTS
 * =============================================================
 */

data class UpcomingPaymentData(
    val id: String,
    val title: String,
    val amount: Double,
    val dueDate: String,
    val type: String,
    val overdue: Boolean
)


/*
 * =============================================================
 * BILLS
 * =============================================================
 */

data class DashboardBillData(
    val id: String,
    val name: String,
    val amount: Double,
    val dueDate: String,
    val status: String,
    val overdue: Boolean
)


/*
 * =============================================================
 * MAINTENANCE
 * =============================================================
 */

data class DashboardMaintenanceData(
    val id: String,
    val title: String,
    val propertyId: String?,
    val propertyName: String?,
    val priority: String,
    val status: String,
    val assignedTo: String?,
    val createdAt: String
)


/*
 * =============================================================
 * LEASE
 * =============================================================
 */

data class DashboardLeaseData(
    val leaseId: String,
    val startDate: String,
    val endDate: String,
    val monthlyRent: Double,
    val status: String,
    val daysRemaining: Int
)


/*
 * =============================================================
 * AGREEMENT
 * =============================================================
 */

data class DashboardAgreementData(
    val agreementId: String,
    val title: String,
    val status: String,
    val signed: Boolean,
    val createdAt: String
)


/*
 * =============================================================
 * PROPERTY RECOMMENDATIONS
 * =============================================================
 */

data class PropertyRecommendationData(
    val propertyId: String,
    val title: String,
    val location: String?,
    val rent: Double?,
    val bedrooms: Int?,
    val imageUrl: String?,
    val matchScore: Double
)


/*
 * =============================================================
 * PROPERTY PERFORMANCE
 * =============================================================
 */

data class PropertyPerformanceSummaryData(
    val propertyId: String,
    val propertyName: String?,
    val income: Double,
    val expenses: Double,
    val noi: Double,
    val occupancyRate: Double,
    val collectionRate: Double,
    val roi: Double
)


/*
 * =============================================================
 * ACTIVITY
 * =============================================================
 */

data class DashboardActivityData(
    val id: String,
    val type: DashboardActivityType,
    val title: String,
    val description: String?,
    val actorName: String?,
    val resourceId: String?,
    val resourceType: String?,
    val timestamp: String,
    val read: Boolean
)


/*
 * =============================================================
 * ALERTS
 * =============================================================
 */

data class DashboardAlertData(
    val id: String,
    val type: DashboardAlertType,
    val title: String,
    val message: String,
    val severity: DashboardAlertSeverity,
    val resourceId: String?,
    val resourceType: String?,
    val createdAt: String,
    val dismissed: Boolean
)


/*
 * =============================================================
 * WIDGETS
 * =============================================================
 */

data class DashboardWidgetData(
    val id: String,
    val type: DashboardWidgetType,
    val title: String,
    val position: Int,
    val visible: Boolean,
    val configuration: Map<String, String>
)


/*
 * =============================================================
 * SEARCH
 * =============================================================
 */

data class DashboardSearchResultData(
    val id: String,
    val title: String,
    val subtitle: String?,
    val type: DashboardSearchResultType,
    val resourceId: String,
    val imageUrl: String?
)


/*
 * =============================================================
 * PLATFORM HEALTH
 * =============================================================
 */

data class PlatformHealthData(
    val status: PlatformHealthStatus,
    val apiAvailable: Boolean,
    val databaseAvailable: Boolean,
    val paymentServicesAvailable: Boolean,
    val notificationServicesAvailable: Boolean,
    val storageAvailable: Boolean,
    val errorRate: Double,
    val averageResponseTimeMs: Double
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class DashboardRole {

    TENANT,

    LANDLORD,

    PROPERTY_MANAGER,

    CARETAKER,

    BROKER,

    TECHNICIAN,

    ACCOUNTANT,

    SUPPORT_AGENT,

    SECURITY,

    ADMIN
}


enum class DashboardTrendDirection {

    UP,

    DOWN,

    STABLE
}


enum class DashboardActivityType {

    PAYMENT,

    PROPERTY,

    TENANT,

    LEASE,

    AGREEMENT,

    MAINTENANCE,

    MESSAGE,

    APPLICATION,

    VIEWING,

    LISTING,

    DOCUMENT,

    STAFF,

    TASK,

    VERIFICATION,

    SECURITY,

    SYSTEM
}


enum class DashboardAlertType {

    PAYMENT_DUE,

    PAYMENT_OVERDUE,

    ARREARS,

    MAINTENANCE,

    LEASE_EXPIRING,

    AGREEMENT_PENDING,

    APPLICATION_PENDING,

    VIEWING_PENDING,

    VERIFICATION_PENDING,

    STAFF_REQUEST,

    SECURITY,

    SYSTEM,

    PERFORMANCE,

    VACANCY,

    OTHER
}


enum class DashboardAlertSeverity {

    INFO,

    WARNING,

    HIGH,

    CRITICAL
}


enum class DashboardWidgetType {

    SUMMARY,

    REVENUE,

    EXPENSES,

    OCCUPANCY,

    ARREARS,

    PAYMENTS,

    MAINTENANCE,

    TENANTS,

    PROPERTIES,

    LISTINGS,

    APPLICATIONS,

    VIEWINGS,

    MESSAGES,

    TASKS,

    ACTIVITY,

    PERFORMANCE,

    MAP,

    CALENDAR,

    ANALYTICS,

    ALERTS
}


enum class DashboardSearchResultType {

    PROPERTY,

    UNIT,

    TENANT,

    LANDLORD,

    STAFF,

    PAYMENT,

    AGREEMENT,

    LEASE,

    MAINTENANCE,

    LISTING,

    APPLICATION,

    VIEWING,

    DOCUMENT,

    ORGANIZATION,

    TASK
}


enum class PlatformHealthStatus {

    HEALTHY,

    DEGRADED,

    MAJOR_OUTAGE,

    UNKNOWN
}
