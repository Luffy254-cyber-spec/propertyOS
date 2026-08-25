package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * AUDIT LOG REPOSITORY
 * =============================================================
 *
 * Central security and accountability history.
 *
 * Records important actions performed throughout the platform.
 *
 * Examples:
 *
 * - User login/logout
 * - Profile changes
 * - Property creation/update/deletion
 * - Rent changes
 * - Agreement creation/signing
 * - Lease changes
 * - Payment creation/refund
 * - Tenant onboarding/removal
 * - Staff actions
 * - Document verification
 * - Maintenance actions
 * - Support actions
 * - Permission changes
 * - Administrative actions
 * - Suspicious activity
 *
 * =============================================================
 */

interface AuditLogRepository {

    /*
     * ---------------------------------------------------------
     * CREATE LOG
     * ---------------------------------------------------------
     */

    suspend fun createLog(
        log: CreateAuditLogData
    ): Result<String>

    suspend fun createSystemLog(
        log: CreateAuditLogData
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * GET LOG
     * ---------------------------------------------------------
     */

    suspend fun getLog(
        logId: String
    ): Result<AuditLogData>

    suspend fun getLogs(
        filters: AuditLogFilters = AuditLogFilters()
    ): Result<List<AuditLogData>>


    /*
     * ---------------------------------------------------------
     * USER ACTIVITY
     * ---------------------------------------------------------
     */

    suspend fun getUserActivity(
        userId: String,
        limit: Int = 100
    ): Result<List<AuditLogData>>

    fun observeUserActivity(
        userId: String
    ): Flow<Result<List<AuditLogData>>>


    /*
     * ---------------------------------------------------------
     * ENTITY HISTORY
     * ---------------------------------------------------------
     */

    suspend fun getEntityHistory(
        entityType: AuditEntityType,
        entityId: String,
        limit: Int = 100
    ): Result<List<AuditLogData>>

    fun observeEntityHistory(
        entityType: AuditEntityType,
        entityId: String
    ): Flow<Result<List<AuditLogData>>>


    /*
     * ---------------------------------------------------------
     * PROPERTY HISTORY
     * ---------------------------------------------------------
     */

    suspend fun getPropertyHistory(
        propertyId: String
    ): Result<List<AuditLogData>>


    /*
     * ---------------------------------------------------------
     * TENANCY HISTORY
     * ---------------------------------------------------------
     */

    suspend fun getLeaseHistory(
        leaseId: String
    ): Result<List<AuditLogData>>

    suspend fun getTenantHistory(
        tenantId: String
    ): Result<List<AuditLogData>>


    /*
     * ---------------------------------------------------------
     * PAYMENT HISTORY
     * ---------------------------------------------------------
     */

    suspend fun getPaymentHistory(
        paymentId: String
    ): Result<List<AuditLogData>>

    suspend fun getFinancialAuditTrail(
        startDate: String,
        endDate: String
    ): Result<List<AuditLogData>>


    /*
     * ---------------------------------------------------------
     * AGREEMENT HISTORY
     * ---------------------------------------------------------
     */

    suspend fun getAgreementHistory(
        agreementId: String
    ): Result<List<AuditLogData>>


    /*
     * ---------------------------------------------------------
     * ADMIN ACTIVITY
     * ---------------------------------------------------------
     */

    suspend fun getAdminActivity(
        adminId: String,
        limit: Int = 100
    ): Result<List<AuditLogData>>

    suspend fun getStaffActivity(
        staffId: String,
        limit: Int = 100
    ): Result<List<AuditLogData>>


    /*
     * ---------------------------------------------------------
     * SECURITY
     * ---------------------------------------------------------
     */

    suspend fun getSecurityEvents(
        filters: SecurityEventFilters = SecurityEventFilters()
    ): Result<List<AuditLogData>>

    suspend fun getSuspiciousActivity(
        userId: String? = null
    ): Result<List<AuditLogData>>

    suspend fun markAsReviewed(
        reviewerId: String,
        logId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * LOGIN ACTIVITY
     * ---------------------------------------------------------
     */

    suspend fun recordLogin(
        userId: String,
        login: LoginAuditData
    ): Result<String>

    suspend fun recordLogout(
        userId: String,
        deviceId: String?
    ): Result<String>

    suspend fun recordFailedLogin(
        identifier: String,
        deviceId: String?,
        ipAddress: String?
    ): Result<String>

    suspend fun getLoginHistory(
        userId: String,
        limit: Int = 50
    ): Result<List<LoginAuditData>>


    /*
     * ---------------------------------------------------------
     * DATA CHANGES
     * ---------------------------------------------------------
     */

    suspend fun recordCreate(
        actorId: String,
        entityType: AuditEntityType,
        entityId: String,
        after: Map<String, Any?>
    ): Result<String>

    suspend fun recordUpdate(
        actorId: String,
        entityType: AuditEntityType,
        entityId: String,
        before: Map<String, Any?>,
        after: Map<String, Any?>
    ): Result<String>

    suspend fun recordDelete(
        actorId: String,
        entityType: AuditEntityType,
        entityId: String,
        before: Map<String, Any?>
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * APPROVALS
     * ---------------------------------------------------------
     */

    suspend fun recordApproval(
        actorId: String,
        entityType: AuditEntityType,
        entityId: String,
        reason: String?
    ): Result<String>

    suspend fun recordRejection(
        actorId: String,
        entityType: AuditEntityType,
        entityId: String,
        reason: String
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * EXPORT
     * ---------------------------------------------------------
     */

    suspend fun exportAuditLogs(
        filters: AuditLogFilters = AuditLogFilters()
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * RETENTION
     * ---------------------------------------------------------
     */

    suspend fun archiveOldLogs(
        olderThan: String
    ): Result<Int>

    suspend fun getRetentionStatistics(): Result<AuditRetentionData>


    /*
     * ---------------------------------------------------------
     * ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getAuditAnalytics(
        startDate: String,
        endDate: String
    ): Result<AuditAnalyticsData>
}


/*
 * =============================================================
 * CREATE DATA
 * =============================================================
 */

data class CreateAuditLogData(
    val actorId: String?,
    val actorType: AuditActorType,
    val action: AuditAction,
    val entityType: AuditEntityType?,
    val entityId: String?,
    val description: String,
    val before: Map<String, Any?>? = null,
    val after: Map<String, Any?>? = null,
    val metadata: Map<String, String> = emptyMap(),
    val ipAddress: String? = null,
    val deviceId: String? = null,
    val userAgent: String? = null,
    val severity: AuditSeverity = AuditSeverity.INFO
)


/*
 * =============================================================
 * LOG DATA
 * =============================================================
 */

data class AuditLogData(
    val id: String,
    val actorId: String?,
    val actorName: String?,
    val actorType: AuditActorType,
    val action: AuditAction,
    val entityType: AuditEntityType?,
    val entityId: String?,
    val description: String,
    val before: Map<String, Any?>?,
    val after: Map<String, Any?>?,
    val metadata: Map<String, String>,
    val ipAddress: String?,
    val deviceId: String?,
    val userAgent: String?,
    val severity: AuditSeverity,
    val securityEvent: Boolean,
    val reviewed: Boolean,
    val createdAt: String
)


/*
 * =============================================================
 * FILTERS
 * =============================================================
 */

data class AuditLogFilters(
    val actorId: String? = null,
    val actorType: AuditActorType? = null,
    val action: AuditAction? = null,
    val entityType: AuditEntityType? = null,
    val entityId: String? = null,
    val severity: AuditSeverity? = null,
    val securityEventOnly: Boolean = false,
    val reviewed: Boolean? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val page: Int = 1,
    val pageSize: Int = 100
)

data class SecurityEventFilters(
    val userId: String? = null,
    val eventType: AuditSecurityEventType? = null,
    val severity: AuditSeverity? = null,
    val reviewed: Boolean? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val page: Int = 1,
    val pageSize: Int = 100
)


/*
 * =============================================================
 * LOGIN DATA
 * =============================================================
 */

data class LoginAuditData(
    val id: String = "",
    val userId: String? = null,
    val identifier: String?,
    val successful: Boolean,
    val deviceId: String?,
    val deviceName: String?,
    val ipAddress: String?,
    val location: String?,
    val failureReason: String?,
    val timestamp: String
)


/*
 * =============================================================
 * RETENTION
 * =============================================================
 */

data class AuditRetentionData(
    val totalLogs: Long,
    val activeLogs: Long,
    val archivedLogs: Long,
    val oldestActiveLog: String?,
    val oldestArchivedLog: String?
)


/*
 * =============================================================
 * ANALYTICS
 * =============================================================
 */

data class AuditAnalyticsData(
    val totalEvents: Long,
    val securityEvents: Long,
    val failedLogins: Long,
    val successfulLogins: Long,
    val creates: Long,
    val updates: Long,
    val deletes: Long,
    val approvals: Long,
    val rejections: Long,
    val suspiciousEvents: Long,
    val eventsByActor: Map<AuditActorType, Long>,
    val eventsByAction: Map<AuditAction, Long>,
    val eventsBySeverity: Map<AuditSeverity, Long>
)


/*
 * =============================================================
 * ACTOR TYPES
 * =============================================================
 */

enum class AuditActorType {

    TENANT,

    LANDLORD,

    PROPERTY_MANAGER,

    CARETAKER,

    BROKER,

    TECHNICIAN,

    CONTRACTOR,

    STAFF,

    ADMIN,

    SYSTEM,

    AUTOMATION,

    API
}


/*
 * =============================================================
 * ACTIONS
 * =============================================================
 */

enum class AuditAction {

    CREATE,

    READ,

    UPDATE,

    DELETE,

    LOGIN,

    LOGOUT,

    LOGIN_FAILED,

    PASSWORD_CHANGED,

    EMAIL_CHANGED,

    PHONE_CHANGED,

    ROLE_CHANGED,

    PERMISSION_GRANTED,

    PERMISSION_REVOKED,

    VERIFY,

    UNVERIFY,

    APPROVE,

    REJECT,

    SUSPEND,

    ACTIVATE,

    DEACTIVATE,

    ASSIGN,

    UNASSIGN,

    TRANSFER,

    ESCALATE,

    DEESCALATE,

    PAYMENT_CREATED,

    PAYMENT_CONFIRMED,

    PAYMENT_FAILED,

    PAYMENT_REFUNDED,

    RENT_CHANGED,

    AGREEMENT_SIGNED,

    AGREEMENT_CANCELLED,

    LEASE_STARTED,

    LEASE_TERMINATED,

    TENANT_ADDED,

    TENANT_REMOVED,

    PROPERTY_PUBLISHED,

    PROPERTY_UNPUBLISHED,

    LISTING_CREATED,

    LISTING_UPDATED,

    LISTING_DELETED,

    DOCUMENT_UPLOADED,

    DOCUMENT_VERIFIED,

    DOCUMENT_REJECTED,

    SUPPORT_CREATED,

    SUPPORT_RESOLVED,

    REVIEW_CREATED,

    REVIEW_MODERATED,

    DATA_EXPORTED,

    SECURITY_ALERT,

    OTHER
}


/*
 * =============================================================
 * ENTITY TYPES
 * =============================================================
 */

enum class AuditEntityType {

    USER,

    PROPERTY,

    UNIT,

    LISTING,

    TENANCY,

    LEASE,

    AGREEMENT,

    PAYMENT,

    BILL,

    EXPENSE,

    MAINTENANCE,

    INSPECTION,

    DOCUMENT,

    REVIEW,

    SUPPORT_TICKET,

    COMPLAINT,

    DISPUTE,

    EMERGENCY_REQUEST,

    MESSAGE,

    NOTIFICATION,

    STAFF,

    ROLE,

    PERMISSION,

    VERIFICATION,

    PROFESSIONAL,

    BROKER,

    OTHER
}


/*
 * =============================================================
 * SECURITY
 * =============================================================
 */

enum class AuditSecurityEventType {

    FAILED_LOGIN,

    MULTIPLE_FAILED_LOGINS,

    NEW_DEVICE,

    NEW_LOCATION,

    PASSWORD_CHANGE,

    EMAIL_CHANGE,

    PHONE_CHANGE,

    ROLE_CHANGE,

    PERMISSION_CHANGE,

    SUSPICIOUS_PAYMENT,

    SUSPICIOUS_ACTIVITY,

    UNAUTHORIZED_ACCESS,

    DATA_EXPORT,

    ACCOUNT_LOCKED,

    OTHER
}


/*
 * =============================================================
 * SEVERITY
 * =============================================================
 */

enum class AuditSeverity {

    DEBUG,

    INFO,

    WARNING,

    ERROR,

    CRITICAL
}