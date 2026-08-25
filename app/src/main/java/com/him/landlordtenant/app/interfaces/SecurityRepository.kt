package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * SECURITY REPOSITORY
 * =============================================================
 *
 * Central security and account-protection layer.
 *
 * Handles:
 *
 * - Sessions
 * - Devices
 * - Login history
 * - Suspicious activity
 * - Security alerts
 * - Two-factor authentication state
 * - Account lockouts
 * - Password/security events
 * - Access attempts
 * - IP/device monitoring
 * - Trusted devices
 * - Security risk scoring
 * - Security incidents
 * - Security audit history
 *
 * IMPORTANT:
 *
 * Sensitive credentials, passwords and authentication secrets
 * should never be stored or exposed directly by this interface.
 *
 * =============================================================
 */

interface SecurityRepository {

    /*
     * ---------------------------------------------------------
     * SECURITY DASHBOARD
     * ---------------------------------------------------------
     */

    suspend fun getSecurityDashboard(
        userId: String
    ): Result<SecurityDashboardData>

    fun observeSecurityDashboard(
        userId: String
    ): Flow<Result<SecurityDashboardData>>


    /*
     * ---------------------------------------------------------
     * SESSIONS
     * ---------------------------------------------------------
     */

    suspend fun getActiveSessions(
        userId: String
    ): Result<List<SecuritySessionData>>

    fun observeActiveSessions(
        userId: String
    ): Flow<Result<List<SecuritySessionData>>>

    suspend fun revokeSession(
        actorId: String,
        sessionId: String
    ): Result<Unit>

    suspend fun revokeAllOtherSessions(
        userId: String,
        currentSessionId: String
    ): Result<Unit>

    suspend fun revokeAllSessions(
        actorId: String,
        userId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * DEVICES
     * ---------------------------------------------------------
     */

    suspend fun registerDevice(
        userId: String,
        device: RegisterDeviceData
    ): Result<String>

    suspend fun getDevices(
        userId: String
    ): Result<List<SecurityDeviceData>>

    fun observeDevices(
        userId: String
    ): Flow<Result<List<SecurityDeviceData>>>

    suspend fun trustDevice(
        userId: String,
        deviceId: String
    ): Result<Unit>

    suspend fun untrustDevice(
        userId: String,
        deviceId: String
    ): Result<Unit>

    suspend fun removeDevice(
        actorId: String,
        deviceId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * LOGIN HISTORY
     * ---------------------------------------------------------
     */

    suspend fun getLoginHistory(
        userId: String,
        limit: Int = 100
    ): Result<List<LoginAttemptData>>

    suspend fun getRecentLoginAttempts(
        userId: String,
        hours: Int = 24
    ): Result<List<LoginAttemptData>>

    suspend fun recordLoginAttempt(
        attempt: RecordLoginAttemptData
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * ACCESS ATTEMPTS
     * ---------------------------------------------------------
     */

    suspend fun recordAccessAttempt(
        attempt: AccessAttemptData
    ): Result<String>

    suspend fun getAccessAttempts(
        userId: String?,
        limit: Int = 100
    ): Result<List<AccessAttemptData>>

    suspend fun getFailedAccessAttempts(
        userId: String,
        hours: Int = 24
    ): Result<List<AccessAttemptData>>


    /*
     * ---------------------------------------------------------
     * SUSPICIOUS ACTIVITY
     * ---------------------------------------------------------
     */

    suspend fun detectSuspiciousActivity(
        userId: String
    ): Result<List<SuspiciousActivityData>>

    suspend fun getSuspiciousActivities(
        userId: String?,
        status: SuspiciousActivityStatus? = null
    ): Result<List<SuspiciousActivityData>>

    fun observeSuspiciousActivities(
        userId: String
    ): Flow<Result<List<SuspiciousActivityData>>>

    suspend fun acknowledgeSuspiciousActivity(
        actorId: String,
        activityId: String
    ): Result<Unit>

    suspend fun dismissSuspiciousActivity(
        actorId: String,
        activityId: String,
        reason: String?
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * SECURITY ALERTS
     * ---------------------------------------------------------
     */

    suspend fun createSecurityAlert(
        actorId: String?,
        alert: CreateSecurityAlertData
    ): Result<String>

    suspend fun getSecurityAlerts(
        userId: String,
        includeResolved: Boolean = false
    ): Result<List<SecurityAlertData>>

    fun observeSecurityAlerts(
        userId: String
    ): Flow<Result<List<SecurityAlertData>>>

    suspend fun acknowledgeSecurityAlert(
        userId: String,
        alertId: String
    ): Result<Unit>

    suspend fun resolveSecurityAlert(
        actorId: String,
        alertId: String,
        resolution: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * 2FA
     * ---------------------------------------------------------
     */

    suspend fun getTwoFactorStatus(
        userId: String
    ): Result<TwoFactorStatusData>

    suspend fun enableTwoFactor(
        userId: String,
        method: TwoFactorMethod
    ): Result<Unit>

    suspend fun disableTwoFactor(
        userId: String
    ): Result<Unit>

    suspend fun markTwoFactorVerified(
        userId: String
    ): Result<Unit>

    suspend fun getTwoFactorMethods(
        userId: String
    ): Result<List<TwoFactorMethodData>>


    /*
     * ---------------------------------------------------------
     * ACCOUNT PROTECTION
     * ---------------------------------------------------------
     */

    suspend fun getAccountProtectionStatus(
        userId: String
    ): Result<AccountProtectionData>

    suspend fun lockAccount(
        actorId: String,
        userId: String,
        reason: String
    ): Result<Unit>

    suspend fun unlockAccount(
        actorId: String,
        userId: String,
        reason: String
    ): Result<Unit>

    suspend fun temporarilyLockAccount(
        userId: String,
        durationMinutes: Int,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * PASSWORD SECURITY EVENTS
     * ---------------------------------------------------------
     */

    suspend fun recordPasswordChanged(
        userId: String
    ): Result<Unit>

    suspend fun recordPasswordResetRequested(
        userId: String?
    ): Result<Unit>

    suspend fun recordPasswordResetCompleted(
        userId: String
    ): Result<Unit>

    suspend fun getPasswordSecurityHistory(
        userId: String,
        limit: Int = 50
    ): Result<List<SecurityEventData>>


    /*
     * ---------------------------------------------------------
     * EMAIL / PHONE SECURITY
     * ---------------------------------------------------------
     */

    suspend fun recordEmailChanged(
        userId: String
    ): Result<Unit>

    suspend fun recordPhoneChanged(
        userId: String
    ): Result<Unit>

    suspend fun getContactSecurityHistory(
        userId: String,
        limit: Int = 50
    ): Result<List<SecurityEventData>>


    /*
     * ---------------------------------------------------------
     * SECURITY RISK
     * ---------------------------------------------------------
     */

    suspend fun calculateRiskScore(
        userId: String
    ): Result<SecurityRiskScoreData>

    suspend fun getRiskFactors(
        userId: String
    ): Result<List<SecurityRiskFactorData>>

    suspend fun refreshRiskScore(
        userId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * IP / LOCATION MONITORING
     * ---------------------------------------------------------
     */

    suspend fun getRecentLocations(
        userId: String,
        limit: Int = 20
    ): Result<List<SecurityLocationData>>

    suspend fun detectUnusualLocation(
        userId: String,
        location: SecurityLocationData
    ): Result<Boolean>


    /*
     * ---------------------------------------------------------
     * RATE LIMITING
     * ---------------------------------------------------------
     */

    suspend fun getRateLimitStatus(
        key: String
    ): Result<RateLimitStatusData>

    suspend fun recordRateLimitViolation(
        key: String,
        action: String
    ): Result<Unit>

    suspend fun clearRateLimit(
        actorId: String,
        key: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * SECURITY INCIDENTS
     * ---------------------------------------------------------
     */

    suspend fun createIncident(
        actorId: String?,
        incident: CreateSecurityIncidentData
    ): Result<String>

    suspend fun getIncident(
        incidentId: String
    ): Result<SecurityIncidentData>

    suspend fun getIncidents(
        status: SecurityIncidentStatus? = null
    ): Result<List<SecurityIncidentData>>

    suspend fun updateIncident(
        actorId: String,
        incidentId: String,
        update: UpdateSecurityIncidentData
    ): Result<Unit>

    suspend fun closeIncident(
        actorId: String,
        incidentId: String,
        resolution: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * SECURITY AUDIT
     * ---------------------------------------------------------
     */

    suspend fun getSecurityAuditLog(
        userId: String?,
        limit: Int = 100
    ): Result<List<SecurityAuditData>>

    suspend fun getSecurityEvents(
        userId: String,
        eventType: SecurityEventType? = null,
        limit: Int = 100
    ): Result<List<SecurityEventData>>


    /*
     * ---------------------------------------------------------
     * ADMIN SECURITY
     * ---------------------------------------------------------
     */

    suspend fun getOrganizationSecurityOverview(
        organizationId: String
    ): Result<OrganizationSecurityOverviewData>

    suspend fun getHighRiskUsers(
        organizationId: String,
        limit: Int = 50
    ): Result<List<UserSecuritySummaryData>>

    suspend fun forceSignOutUser(
        actorId: String,
        userId: String,
        reason: String
    ): Result<Unit>

    suspend fun disableUserAccess(
        actorId: String,
        userId: String,
        reason: String
    ): Result<Unit>

    suspend fun restoreUserAccess(
        actorId: String,
        userId: String,
        reason: String
    ): Result<Unit>
}


/*
 * =============================================================
 * SESSION
 * =============================================================
 */

data class SecuritySessionData(
    val id: String,
    val userId: String,
    val deviceId: String?,
    val deviceName: String?,
    val platform: String?,
    val ipAddress: String?,
    val location: String?,
    val createdAt: String,
    val lastActivityAt: String,
    val expiresAt: String?,
    val current: Boolean,
    val trustedDevice: Boolean
)


/*
 * =============================================================
 * DEVICE
 * =============================================================
 */

data class RegisterDeviceData(
    val deviceName: String?,
    val deviceModel: String?,
    val manufacturer: String?,
    val platform: String,
    val osVersion: String?,
    val appVersion: String?,
    val fingerprint: String?
)

data class SecurityDeviceData(
    val id: String,
    val userId: String,
    val deviceName: String?,
    val deviceModel: String?,
    val manufacturer: String?,
    val platform: String,
    val osVersion: String?,
    val appVersion: String?,
    val trusted: Boolean,
    val blocked: Boolean,
    val firstSeenAt: String,
    val lastSeenAt: String
)


/*
 * =============================================================
 * LOGIN
 * =============================================================
 */

data class RecordLoginAttemptData(
    val userId: String?,
    val identifier: String?,
    val deviceId: String?,
    val ipAddress: String?,
    val location: String?,
    val successful: Boolean,
    val method: LoginMethod,
    val failureReason: String?
)

data class LoginAttemptData(
    val id: String,
    val userId: String?,
    val deviceId: String?,
    val ipAddress: String?,
    val location: String?,
    val successful: Boolean,
    val method: LoginMethod,
    val failureReason: String?,
    val timestamp: String
)


/*
 * =============================================================
 * ACCESS ATTEMPT
 * =============================================================
 */

data class AccessAttemptData(
    val id: String? = null,
    val userId: String?,
    val resourceType: String,
    val resourceId: String?,
    val action: String,
    val allowed: Boolean,
    val denialReason: String?,
    val ipAddress: String?,
    val timestamp: String? = null
)


/*
 * =============================================================
 * SUSPICIOUS ACTIVITY
 * =============================================================
 */

data class SuspiciousActivityData(
    val id: String,
    val userId: String?,
    val type: SuspiciousActivityType,
    val severity: SecuritySeverity,
    val title: String,
    val description: String,
    val riskScore: Double,
    val deviceId: String?,
    val ipAddress: String?,
    val detectedAt: String,
    val status: SuspiciousActivityStatus
)


/*
 * =============================================================
 * SECURITY ALERT
 * =============================================================
 */

data class CreateSecurityAlertData(
    val userId: String?,
    val type: SecurityAlertType,
    val severity: SecuritySeverity,
    val title: String,
    val message: String,
    val source: String?,
    val metadata: Map<String, String> = emptyMap()
)

data class SecurityAlertData(
    val id: String,
    val userId: String?,
    val type: SecurityAlertType,
    val severity: SecuritySeverity,
    val title: String,
    val message: String,
    val source: String?,
    val acknowledged: Boolean,
    val resolved: Boolean,
    val createdAt: String,
    val resolvedAt: String?
)


/*
 * =============================================================
 * 2FA
 * =============================================================
 */

data class TwoFactorStatusData(
    val enabled: Boolean,
    val verified: Boolean,
    val primaryMethod: TwoFactorMethod?,
    val enabledAt: String?,
    val lastVerifiedAt: String?
)

data class TwoFactorMethodData(
    val method: TwoFactorMethod,
    val enabled: Boolean,
    val verified: Boolean,
    val createdAt: String?
)


/*
 * =============================================================
 * ACCOUNT PROTECTION
 * =============================================================
 */

data class AccountProtectionData(
    val userId: String,
    val locked: Boolean,
    val temporarilyLocked: Boolean,
    val lockedUntil: String?,
    val failedAttempts: Int,
    val lastFailedAttemptAt: String?,
    val accessDisabled: Boolean,
    val reason: String?
)


/*
 * =============================================================
 * SECURITY RISK
 * =============================================================
 */

data class SecurityRiskScoreData(
    val userId: String,
    val score: Double,
    val level: SecurityRiskLevel,
    val calculatedAt: String
)

data class SecurityRiskFactorData(
    val type: SecurityRiskFactorType,
    val weight: Double,
    val value: Double,
    val description: String
)


/*
 * =============================================================
 * LOCATION
 * =============================================================
 */

data class SecurityLocationData(
    val ipAddress: String?,
    val country: String?,
    val region: String?,
    val city: String?,
    val latitude: Double?,
    val longitude: Double?,
    val timestamp: String
)


/*
 * =============================================================
 * RATE LIMIT
 * =============================================================
 */

data class RateLimitStatusData(
    val key: String,
    val limit: Int,
    val used: Int,
    val remaining: Int,
    val resetAt: String,
    val blocked: Boolean
)


/*
 * =============================================================
 * INCIDENT
 * =============================================================
 */

data class CreateSecurityIncidentData(
    val type: SecurityIncidentType,
    val severity: SecuritySeverity,
    val title: String,
    val description: String,
    val userId: String?,
    val source: String?,
    val evidenceDocumentIds: List<String> = emptyList()
)

data class UpdateSecurityIncidentData(
    val status: SecurityIncidentStatus?,
    val severity: SecuritySeverity?,
    val description: String?,
    val assignedTo: String?,
    val notes: String?
)

data class SecurityIncidentData(
    val id: String,
    val type: SecurityIncidentType,
    val severity: SecuritySeverity,
    val title: String,
    val description: String,
    val userId: String?,
    val source: String?,
    val status: SecurityIncidentStatus,
    val assignedTo: String?,
    val evidenceDocumentIds: List<String>,
    val createdAt: String,
    val updatedAt: String,
    val closedAt: String?
)


/*
 * =============================================================
 * SECURITY AUDIT
 * =============================================================
 */

data class SecurityAuditData(
    val id: String,
    val actorId: String?,
    val targetUserId: String?,
    val eventType: SecurityEventType,
    val action: String,
    val result: SecurityActionResult,
    val ipAddress: String?,
    val deviceId: String?,
    val metadata: Map<String, String>,
    val timestamp: String
)


/*
 * =============================================================
 * SECURITY EVENT
 * =============================================================
 */

data class SecurityEventData(
    val id: String,
    val userId: String?,
    val eventType: SecurityEventType,
    val description: String,
    val severity: SecuritySeverity,
    val ipAddress: String?,
    val deviceId: String?,
    val timestamp: String
)


/*
 * =============================================================
 * DASHBOARD
 * =============================================================
 */

data class SecurityDashboardData(
    val securityScore: Double,
    val riskLevel: SecurityRiskLevel,
    val activeSessions: Int,
    val trustedDevices: Int,
    val suspiciousActivities: Int,
    val unresolvedAlerts: Int,
    val failedLoginAttempts24h: Int,
    val accountLocked: Boolean,
    val twoFactorEnabled: Boolean,
    val recentEvents: List<SecurityEventData>
)


/*
 * =============================================================
 * ORGANIZATION SECURITY
 * =============================================================
 */

data class OrganizationSecurityOverviewData(
    val organizationId: String,
    val securityScore: Double,
    val totalUsers: Int,
    val activeUsers: Int,
    val highRiskUsers: Int,
    val lockedUsers: Int,
    val disabledUsers: Int,
    val activeIncidents: Int,
    val unresolvedAlerts: Int,
    val suspiciousActivities: Int
)

data class UserSecuritySummaryData(
    val userId: String,
    val userName: String?,
    val role: String?,
    val riskScore: Double,
    val riskLevel: SecurityRiskLevel,
    val suspiciousActivities: Int,
    val failedLoginAttempts: Int,
    val accountLocked: Boolean,
    val twoFactorEnabled: Boolean
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class LoginMethod {
    PASSWORD,
    OTP,
    TWO_FACTOR,
    BIOMETRIC,
    GOOGLE,
    APPLE,
    MICROSOFT,
    OTHER
}

enum class SuspiciousActivityType {
    MULTIPLE_FAILED_LOGINS,
    UNUSUAL_LOCATION,
    NEW_DEVICE,
    IMPOSSIBLE_TRAVEL,
    UNUSUAL_ACCESS_TIME,
    EXCESSIVE_REQUESTS,
    UNAUTHORIZED_ACCESS,
    ACCOUNT_TAKEOVER_INDICATOR,
    SESSION_ANOMALY,
    SUSPICIOUS_API_ACTIVITY
}

enum class SuspiciousActivityStatus {
    NEW,
    INVESTIGATING,
    ACKNOWLEDGED,
    DISMISSED,
    RESOLVED
}

enum class SecurityAlertType {
    NEW_LOGIN,
    NEW_DEVICE,
    PASSWORD_CHANGED,
    EMAIL_CHANGED,
    PHONE_CHANGED,
    TWO_FACTOR_CHANGED,
    SUSPICIOUS_LOGIN,
    ACCOUNT_LOCKED,
    ACCOUNT_UNLOCKED,
    ACCESS_DENIED,
    SECURITY_INCIDENT,
    HIGH_RISK_ACTIVITY
}

enum class SecuritySeverity {
    INFO,
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

enum class TwoFactorMethod {
    AUTHENTICATOR,
    SMS,
    EMAIL,
    PASSKEY,
    BIOMETRIC
}

enum class SecurityRiskLevel {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

enum class SecurityRiskFactorType {
    FAILED_LOGINS,
    NEW_DEVICE,
    UNUSUAL_LOCATION,
    UNUSUAL_ACTIVITY,
    ACCOUNT_AGE,
    SESSION_COUNT,
    ACCESS_DENIALS,
    PASSWORD_AGE,
    TWO_FACTOR_STATUS
}

enum class SecurityIncidentType {
    ACCOUNT_TAKEOVER,
    UNAUTHORIZED_ACCESS,
    SUSPICIOUS_LOGIN,
    DATA_ACCESS,
    FRAUD_INDICATOR,
    DEVICE_COMPROMISE,
    CREDENTIAL_COMPROMISE,
    POLICY_VIOLATION,
    OTHER
}

enum class SecurityIncidentStatus {
    OPEN,
    INVESTIGATING,
    CONTAINED,
    RESOLVED,
    CLOSED
}

enum class SecurityActionResult {
    SUCCESS,
    FAILURE,
    DENIED,
    BLOCKED
}

enum class SecurityEventType {
    LOGIN,
    LOGOUT,
    LOGIN_FAILED,
    PASSWORD_CHANGED,
    PASSWORD_RESET,
    EMAIL_CHANGED,
    PHONE_CHANGED,
    TWO_FACTOR_ENABLED,
    TWO_FACTOR_DISABLED,
    DEVICE_REGISTERED,
    DEVICE_TRUSTED,
    DEVICE_REMOVED,
    SESSION_CREATED,
    SESSION_REVOKED,
    ACCESS_GRANTED,
    ACCESS_DENIED,
    ACCOUNT_LOCKED,
    ACCOUNT_UNLOCKED,
    SUSPICIOUS_ACTIVITY,
    SECURITY_ALERT,
    SECURITY_INCIDENT,
    ADMIN_ACTION
}