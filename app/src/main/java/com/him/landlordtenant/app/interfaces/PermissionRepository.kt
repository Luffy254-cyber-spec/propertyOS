package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * PERMISSION REPOSITORY
 * =============================================================
 *
 * Handles:
 *
 * - Roles
 * - Permissions
 * - Role assignment
 * - Custom permissions
 * - Property-level permissions
 * - Temporary permissions
 * - Permission inheritance
 * - Permission requests
 * - Permission approval
 * - Permission revocation
 * - Access checks
 * - Staff access
 * - Audit integration
 *
 * =============================================================
 */

interface PermissionRepository {

    /*
     * ---------------------------------------------------------
     * ROLES
     * ---------------------------------------------------------
     */

    suspend fun createRole(
        actorId: String,
        role: CreateRoleData
    ): Result<String>

    suspend fun getRole(
        roleId: String
    ): Result<RoleData>

    suspend fun getRoleByName(
        name: String
    ): Result<RoleData?>

    suspend fun getAllRoles(): Result<List<RoleData>>

    suspend fun updateRole(
        actorId: String,
        roleId: String,
        update: UpdateRoleData
    ): Result<Unit>

    suspend fun deleteRole(
        actorId: String,
        roleId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * PERMISSIONS
     * ---------------------------------------------------------
     */

    suspend fun createPermission(
        actorId: String,
        permission: CreatePermissionData
    ): Result<String>

    suspend fun getPermission(
        permissionId: String
    ): Result<PermissionData>

    suspend fun getAllPermissions(): Result<List<PermissionData>>

    suspend fun getPermissionsForRole(
        roleId: String
    ): Result<List<PermissionData>>

    suspend fun updatePermission(
        actorId: String,
        permissionId: String,
        update: UpdatePermissionData
    ): Result<Unit>

    suspend fun deletePermission(
        actorId: String,
        permissionId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * ROLE → PERMISSION
     * ---------------------------------------------------------
     */

    suspend fun grantPermissionToRole(
        actorId: String,
        roleId: String,
        permissionId: String
    ): Result<Unit>

    suspend fun revokePermissionFromRole(
        actorId: String,
        roleId: String,
        permissionId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * USER → ROLE
     * ---------------------------------------------------------
     */

    suspend fun assignRole(
        actorId: String,
        userId: String,
        roleId: String,
        scope: PermissionScopeData? = null
    ): Result<String>

    suspend fun removeRole(
        actorId: String,
        userId: String,
        roleId: String,
        scope: PermissionScopeData? = null
    ): Result<Unit>

    suspend fun getUserRoles(
        userId: String
    ): Result<List<UserRoleData>>

    fun observeUserRoles(
        userId: String
    ): Flow<Result<List<UserRoleData>>>


    /*
     * ---------------------------------------------------------
     * DIRECT USER PERMISSIONS
     * ---------------------------------------------------------
     */

    suspend fun grantPermissionToUser(
        actorId: String,
        userId: String,
        permissionId: String,
        scope: PermissionScopeData? = null,
        expiresAt: String? = null
    ): Result<String>

    suspend fun revokePermissionFromUser(
        actorId: String,
        userId: String,
        permissionId: String
    ): Result<Unit>

    suspend fun getUserPermissions(
        userId: String
    ): Result<List<UserPermissionData>>


    /*
     * ---------------------------------------------------------
     * ACCESS CHECKING
     * ---------------------------------------------------------
     */

    suspend fun hasPermission(
        userId: String,
        permission: String
    ): Result<Boolean>

    suspend fun hasPermission(
        userId: String,
        permission: String,
        resourceType: String,
        resourceId: String
    ): Result<Boolean>

    suspend fun checkAccess(
        request: AccessRequestData
    ): Result<AccessDecisionData>


    /*
     * ---------------------------------------------------------
     * RESOURCE ACCESS
     * ---------------------------------------------------------
     */

    suspend fun getAccessibleProperties(
        userId: String
    ): Result<List<String>>

    suspend fun getAccessibleUnits(
        userId: String
    ): Result<List<String>>

    suspend fun getAccessibleTenants(
        userId: String
    ): Result<List<String>>

    suspend fun getAccessiblePayments(
        userId: String
    ): Result<List<String>>

    suspend fun getAccessibleDocuments(
        userId: String
    ): Result<List<String>>


    /*
     * ---------------------------------------------------------
     * PROPERTY-SCOPED ACCESS
     * ---------------------------------------------------------
     */

    suspend fun grantPropertyAccess(
        actorId: String,
        userId: String,
        propertyId: String,
        permissions: List<String>,
        expiresAt: String? = null
    ): Result<String>

    suspend fun revokePropertyAccess(
        actorId: String,
        userId: String,
        propertyId: String
    ): Result<Unit>

    suspend fun getPropertyAccess(
        userId: String,
        propertyId: String
    ): Result<PropertyAccessData>

    suspend fun getPropertyUsers(
        actorId: String,
        propertyId: String
    ): Result<List<PropertyUserAccessData>>


    /*
     * ---------------------------------------------------------
     * TEMPORARY ACCESS
     * ---------------------------------------------------------
     */

    suspend fun createTemporaryAccess(
        actorId: String,
        request: TemporaryAccessRequestData
    ): Result<String>

    suspend fun revokeTemporaryAccess(
        actorId: String,
        accessId: String
    ): Result<Unit>

    suspend fun getActiveTemporaryAccess(
        userId: String
    ): Result<List<TemporaryAccessData>>


    /*
     * ---------------------------------------------------------
     * PERMISSION REQUESTS
     * ---------------------------------------------------------
     */

    suspend fun requestPermission(
        userId: String,
        request: CreatePermissionRequestData
    ): Result<String>

    suspend fun getPermissionRequest(
        requestId: String
    ): Result<PermissionRequestData>

    suspend fun getUserPermissionRequests(
        userId: String
    ): Result<List<PermissionRequestData>>

    suspend fun getPendingPermissionRequests(
        reviewerId: String
    ): Result<List<PermissionRequestData>>

    suspend fun approvePermissionRequest(
        reviewerId: String,
        requestId: String,
        expiresAt: String? = null
    ): Result<Unit>

    suspend fun rejectPermissionRequest(
        reviewerId: String,
        requestId: String,
        reason: String
    ): Result<Unit>

    suspend fun cancelPermissionRequest(
        userId: String,
        requestId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * ACCESS REVIEWS
     * ---------------------------------------------------------
     */

    suspend fun getUsersWithPermission(
        permission: String
    ): Result<List<String>>

    suspend fun getUsersWithRole(
        roleId: String
    ): Result<List<String>>

    suspend fun getExpiredPermissions(): Result<List<UserPermissionData>>

    suspend fun cleanupExpiredPermissions(): Result<Int>


    /*
     * ---------------------------------------------------------
     * ROLE HIERARCHY
     * ---------------------------------------------------------
     */

    suspend fun setParentRole(
        actorId: String,
        roleId: String,
        parentRoleId: String?
    ): Result<Unit>

    suspend fun getInheritedPermissions(
        roleId: String
    ): Result<List<PermissionData>>


    /*
     * ---------------------------------------------------------
     * SECURITY
     * ---------------------------------------------------------
     */

    suspend fun disableUserAccess(
        actorId: String,
        userId: String,
        reason: String
    ): Result<Unit>

    suspend fun enableUserAccess(
        actorId: String,
        userId: String
    ): Result<Unit>

    suspend fun isUserAccessDisabled(
        userId: String
    ): Result<Boolean>


    /*
     * ---------------------------------------------------------
     * ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getPermissionAnalytics(): Result<PermissionAnalyticsData>
}


/*
 * =============================================================
 * ROLE DATA
 * =============================================================
 */

data class CreateRoleData(
    val name: String,
    val description: String?,
    val type: RoleType,
    val permissions: List<String> = emptyList()
)

data class UpdateRoleData(
    val name: String?,
    val description: String?,
    val active: Boolean?
)

data class RoleData(
    val id: String,
    val name: String,
    val description: String?,
    val type: RoleType,
    val parentRoleId: String?,
    val permissions: List<String>,
    val active: Boolean,
    val systemRole: Boolean,
    val createdAt: String
)


/*
 * =============================================================
 * PERMISSION DATA
 * =============================================================
 */

data class CreatePermissionData(
    val name: String,
    val description: String?,
    val resource: PermissionResource,
    val action: PermissionAction,
    val sensitivity: PermissionSensitivity = PermissionSensitivity.NORMAL
)

data class UpdatePermissionData(
    val description: String?,
    val sensitivity: PermissionSensitivity?
)

data class PermissionData(
    val id: String,
    val name: String,
    val description: String?,
    val resource: PermissionResource,
    val action: PermissionAction,
    val sensitivity: PermissionSensitivity,
    val systemPermission: Boolean
)


/*
 * =============================================================
 * USER ROLE
 * =============================================================
 */



/*
 * =============================================================
 * USER PERMISSION
 * =============================================================
 */

data class UserPermissionData(
    val id: String,
    val userId: String,
    val permissionId: String,
    val permissionName: String,
    val source: PermissionSource,
    val scope: PermissionScopeData?,
    val grantedBy: String?,
    val grantedAt: String,
    val expiresAt: String?,
    val active: Boolean
)


/*
 * =============================================================
 * PERMISSION SCOPE
 * =============================================================
 */

data class PermissionScopeData(
    val propertyIds: List<String> = emptyList(),
    val unitIds: List<String> = emptyList(),
    val tenantIds: List<String> = emptyList(),
    val regionIds: List<String> = emptyList(),
    val organizationIds: List<String> = emptyList()
)


/*
 * =============================================================
 * ACCESS REQUEST
 * =============================================================
 */

data class AccessRequestData(
    val userId: String,
    val permission: String,
    val resourceType: String?,
    val resourceId: String?,
    val propertyId: String?
)

data class AccessDecisionData(
    val allowed: Boolean,
    val permission: String,
    val reason: String,
    val matchedRole: String?,
    val matchedPermission: String?,
    val scopeMatched: Boolean
)


/*
 * =============================================================
 * PROPERTY ACCESS
 * =============================================================
 */

data class PropertyAccessData(
    val userId: String,
    val propertyId: String,
    val permissions: List<String>,
    val roleId: String?,
    val grantedAt: String,
    val expiresAt: String?,
    val active: Boolean
)

data class PropertyUserAccessData(
    val userId: String,
    val userName: String?,
    val roleName: String?,
    val permissions: List<String>,
    val expiresAt: String?,
    val active: Boolean
)


/*
 * =============================================================
 * TEMPORARY ACCESS
 * =============================================================
 */

data class TemporaryAccessRequestData(
    val userId: String,
    val permission: String,
    val resourceType: String?,
    val resourceId: String?,
    val propertyId: String?,
    val reason: String,
    val startsAt: String,
    val expiresAt: String
)

data class TemporaryAccessData(
    val id: String,
    val userId: String,
    val permission: String,
    val resourceType: String?,
    val resourceId: String?,
    val propertyId: String?,
    val reason: String,
    val startsAt: String,
    val expiresAt: String,
    val active: Boolean
)


/*
 * =============================================================
 * PERMISSION REQUEST
 * =============================================================
 */

data class CreatePermissionRequestData(
    val permission: String,
    val resourceType: String?,
    val resourceId: String?,
    val propertyId: String?,
    val reason: String,
    val requestedDurationDays: Int?
)

data class PermissionRequestData(
    val id: String,
    val requesterId: String,
    val permission: String,
    val resourceType: String?,
    val resourceId: String?,
    val propertyId: String?,
    val reason: String,
    val status: PermissionRequestStatus,
    val reviewerId: String?,
    val rejectionReason: String?,
    val createdAt: String,
    val reviewedAt: String?,
    val expiresAt: String?
)


/*
 * =============================================================
 * ANALYTICS
 * =============================================================
 */

data class PermissionAnalyticsData(
    val totalRoles: Int,
    val totalPermissions: Int,
    val totalAssignedRoles: Int,
    val totalDirectPermissions: Int,
    val temporaryPermissions: Int,
    val pendingRequests: Int,
    val expiredPermissions: Int,
    val disabledUsers: Int,
    val usersByRole: Map<String, Int>,
    val permissionsByResource: Map<PermissionResource, Int>
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class RoleType {

    SYSTEM_ADMIN,

    ADMIN,

    PROPERTY_MANAGER,

    LANDLORD,

    CARETAKER,

    BROKER,

    TENANT,

    TECHNICIAN,

    CONTRACTOR,

    ACCOUNTANT,

    SUPPORT_AGENT,

    SECURITY,

    CUSTOM
}


enum class PermissionResource {

    USERS,

    PROPERTIES,

    UNITS,

    TENANTS,

    LANDLORDS,

    LEASES,

    AGREEMENTS,

    PAYMENTS,

    BILLS,

    EXPENSES,

    MAINTENANCE,

    INSPECTIONS,

    DOCUMENTS,

    REVIEWS,

    LISTINGS,

    VIEWINGS,

    MESSAGES,

    SUPPORT,

    DISPUTES,

    STAFF,

    REPORTS,

    ANALYTICS,

    SETTINGS,

    ROLES,

    PERMISSIONS,

    AUDIT_LOGS
}


enum class PermissionAction {

    CREATE,

    READ,

    UPDATE,

    DELETE,

    APPROVE,

    REJECT,

    VERIFY,

    ASSIGN,

    EXPORT,

    PUBLISH,

    UNPUBLISH,

    INVITE,

    REMOVE,

    MANAGE,

    VIEW_SENSITIVE
}


enum class PermissionSensitivity {

    LOW,

    NORMAL,

    HIGH,

    CRITICAL
}


enum class PermissionSource {

    ROLE,

    DIRECT,

    TEMPORARY,

    SYSTEM,

    INHERITED
}


enum class PermissionRequestStatus {

    PENDING,

    APPROVED,

    REJECTED,

    CANCELLED,

    EXPIRED
}