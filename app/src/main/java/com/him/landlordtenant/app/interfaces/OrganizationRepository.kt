package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * ORGANIZATION REPOSITORY
 * =============================================================
 *
 * Supports:
 *
 * - Property management companies
 * - Landlord organizations
 * - Real-estate agencies
 * - Property portfolios
 * - Branches
 * - Departments
 * - Teams
 * - Organization members
 * - Staff invitations
 * - Organization roles
 * - Ownership
 * - Property assignment
 * - Tenant assignment
 * - Organization settings
 * - Organization verification
 * - Subscription/plan information
 *
 * =============================================================
 */

interface OrganizationRepository {

    /*
     * ---------------------------------------------------------
     * ORGANIZATION CRUD
     * ---------------------------------------------------------
     */

    suspend fun createOrganization(
        actorId: String,
        organization: CreateOrganizationData
    ): Result<String>

    suspend fun getOrganization(
        organizationId: String
    ): Result<OrganizationData>

    fun observeOrganization(
        organizationId: String
    ): Flow<Result<OrganizationData>>

    suspend fun updateOrganization(
        actorId: String,
        organizationId: String,
        update: UpdateOrganizationData
    ): Result<Unit>

    suspend fun deactivateOrganization(
        actorId: String,
        organizationId: String,
        reason: String
    ): Result<Unit>

    suspend fun reactivateOrganization(
        actorId: String,
        organizationId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * USER ORGANIZATIONS
     * ---------------------------------------------------------
     */

    suspend fun getUserOrganizations(
        userId: String
    ): Result<List<OrganizationData>>

    fun observeUserOrganizations(
        userId: String
    ): Flow<Result<List<OrganizationData>>>

    suspend fun getDefaultOrganization(
        userId: String
    ): Result<OrganizationData?>

    suspend fun setDefaultOrganization(
        userId: String,
        organizationId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * MEMBERS
     * ---------------------------------------------------------
     */

    suspend fun addMember(
        actorId: String,
        organizationId: String,
        member: AddOrganizationMemberData
    ): Result<String>

    suspend fun removeMember(
        actorId: String,
        organizationId: String,
        userId: String,
        reason: String?
    ): Result<Unit>

    suspend fun getMembers(
        organizationId: String
    ): Result<List<OrganizationMemberData>>

    fun observeMembers(
        organizationId: String
    ): Flow<Result<List<OrganizationMemberData>>>

    suspend fun getMember(
        organizationId: String,
        userId: String
    ): Result<OrganizationMemberData>


    /*
     * ---------------------------------------------------------
     * MEMBER ROLES
     * ---------------------------------------------------------
     */

    suspend fun assignMemberRole(
        actorId: String,
        organizationId: String,
        userId: String,
        role: OrganizationRole
    ): Result<Unit>

    suspend fun removeMemberRole(
        actorId: String,
        organizationId: String,
        userId: String,
        role: OrganizationRole
    ): Result<Unit>

    suspend fun getMemberRoles(
        organizationId: String,
        userId: String
    ): Result<List<OrganizationRole>>

    suspend fun updateMemberStatus(
        actorId: String,
        organizationId: String,
        userId: String,
        status: OrganizationMemberStatus
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * INVITATIONS
     * ---------------------------------------------------------
     */

    suspend fun inviteMember(
        actorId: String,
        organizationId: String,
        invitation: OrganizationInvitationData
    ): Result<String>

    suspend fun getInvitation(
        invitationId: String
    ): Result<OrganizationInvitationResult>

    suspend fun getPendingInvitations(
        organizationId: String
    ): Result<List<OrganizationInvitationResult>>

    suspend fun acceptInvitation(
        userId: String,
        invitationId: String
    ): Result<Unit>

    suspend fun rejectInvitation(
        userId: String,
        invitationId: String
    ): Result<Unit>

    suspend fun cancelInvitation(
        actorId: String,
        invitationId: String
    ): Result<Unit>

    suspend fun resendInvitation(
        actorId: String,
        invitationId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * BRANCHES
     * ---------------------------------------------------------
     */

    suspend fun createBranch(
        actorId: String,
        organizationId: String,
        branch: CreateBranchData
    ): Result<String>

    suspend fun getBranch(
        branchId: String
    ): Result<BranchData>

    suspend fun getOrganizationBranches(
        organizationId: String
    ): Result<List<BranchData>>

    suspend fun updateBranch(
        actorId: String,
        branchId: String,
        update: UpdateBranchData
    ): Result<Unit>

    suspend fun deactivateBranch(
        actorId: String,
        branchId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * DEPARTMENTS
     * ---------------------------------------------------------
     */

    suspend fun createDepartment(
        actorId: String,
        organizationId: String,
        department: CreateDepartmentData
    ): Result<String>

    suspend fun getDepartments(
        organizationId: String
    ): Result<List<DepartmentData>>

    suspend fun updateDepartment(
        actorId: String,
        departmentId: String,
        name: String,
        description: String?
    ): Result<Unit>

    suspend fun deleteDepartment(
        actorId: String,
        departmentId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * TEAMS
     * ---------------------------------------------------------
     */

    suspend fun createTeam(
        actorId: String,
        organizationId: String,
        team: CreateTeamData
    ): Result<String>

    suspend fun getTeams(
        organizationId: String
    ): Result<List<TeamData>>

    suspend fun getTeam(
        teamId: String
    ): Result<TeamData>

    suspend fun updateTeam(
        actorId: String,
        teamId: String,
        name: String,
        description: String?
    ): Result<Unit>

    suspend fun deleteTeam(
        actorId: String,
        teamId: String
    ): Result<Unit>

    suspend fun addTeamMember(
        actorId: String,
        teamId: String,
        userId: String
    ): Result<Unit>

    suspend fun removeTeamMember(
        actorId: String,
        teamId: String,
        userId: String
    ): Result<Unit>

    suspend fun getTeamMembers(
        teamId: String
    ): Result<List<String>>


    /*
     * ---------------------------------------------------------
     * PROPERTY ASSIGNMENT
     * ---------------------------------------------------------
     */

    suspend fun assignProperty(
        actorId: String,
        organizationId: String,
        propertyId: String,
        assignment: PropertyAssignmentData
    ): Result<String>

    suspend fun unassignProperty(
        actorId: String,
        organizationId: String,
        propertyId: String
    ): Result<Unit>

    suspend fun getOrganizationProperties(
        organizationId: String
    ): Result<List<String>>

    suspend fun getPropertyAssignment(
        organizationId: String,
        propertyId: String
    ): Result<PropertyAssignmentData>


    /*
     * ---------------------------------------------------------
     * STAFF PROPERTY ASSIGNMENT
     * ---------------------------------------------------------
     */

    suspend fun assignStaffToProperty(
        actorId: String,
        organizationId: String,
        userId: String,
        propertyId: String
    ): Result<String>

    suspend fun removeStaffFromProperty(
        actorId: String,
        assignmentId: String
    ): Result<Unit>

    suspend fun getStaffProperties(
        organizationId: String,
        userId: String
    ): Result<List<String>>

    suspend fun getPropertyStaff(
        organizationId: String,
        propertyId: String
    ): Result<List<String>>


    /*
     * ---------------------------------------------------------
     * OWNER MANAGEMENT
     * ---------------------------------------------------------
     */

    suspend fun addPropertyOwner(
        actorId: String,
        propertyId: String,
        ownerId: String,
        ownershipPercentage: Double
    ): Result<String>

    suspend fun removePropertyOwner(
        actorId: String,
        ownershipId: String
    ): Result<Unit>

    suspend fun getPropertyOwners(
        propertyId: String
    ): Result<List<PropertyOwnerData>>

    suspend fun updateOwnershipPercentage(
        actorId: String,
        ownershipId: String,
        percentage: Double
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * ORGANIZATION VERIFICATION
     * ---------------------------------------------------------
     */

    suspend fun submitVerification(
        actorId: String,
        organizationId: String,
        verification: OrganizationVerificationData
    ): Result<String>

    suspend fun getVerification(
        organizationId: String
    ): Result<OrganizationVerificationResult>

    suspend fun approveVerification(
        reviewerId: String,
        organizationId: String
    ): Result<Unit>

    suspend fun rejectVerification(
        reviewerId: String,
        organizationId: String,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * SETTINGS
     * ---------------------------------------------------------
     */

    suspend fun getSettings(
        organizationId: String
    ): Result<OrganizationSettingsData>

    suspend fun updateSettings(
        actorId: String,
        organizationId: String,
        settings: OrganizationSettingsData
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * BILLING / PLAN
     * ---------------------------------------------------------
     */

    suspend fun getSubscription(
        organizationId: String
    ): Result<OrganizationSubscriptionData>

    suspend fun updateSubscription(
        actorId: String,
        organizationId: String,
        plan: OrganizationPlan
    ): Result<Unit>

    suspend fun getUsage(
        organizationId: String
    ): Result<OrganizationUsageData>


    /*
     * ---------------------------------------------------------
     * ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getOrganizationAnalytics(
        organizationId: String
    ): Result<OrganizationAnalyticsData>
}


/*
 * =============================================================
 * ORGANIZATION
 * =============================================================
 */

data class CreateOrganizationData(
    val name: String,
    val legalName: String?,
    val type: OrganizationType,
    val description: String?,
    val email: String?,
    val phone: String?,
    val address: String?,
    val country: String,
    val ownerId: String
)

data class UpdateOrganizationData(
    val name: String?,
    val legalName: String?,
    val description: String?,
    val email: String?,
    val phone: String?,
    val address: String?,
    val logoUrl: String?
)

data class OrganizationData(
    val id: String,
    val name: String,
    val legalName: String?,
    val type: OrganizationType,
    val description: String?,
    val email: String?,
    val phone: String?,
    val address: String?,
    val country: String,
    val ownerId: String,
    val logoUrl: String?,
    val verified: Boolean,
    val active: Boolean,
    val createdAt: String
)


/*
 * =============================================================
 * MEMBERS
 * =============================================================
 */

data class AddOrganizationMemberData(
    val userId: String,
    val roles: List<OrganizationRole>,
    val branchId: String?,
    val departmentId: String?,
    val jobTitle: String?
)

data class OrganizationMemberData(
    val id: String,
    val organizationId: String,
    val userId: String,
    val userName: String?,
    val email: String?,
    val roles: List<OrganizationRole>,
    val branchId: String?,
    val departmentId: String?,
    val jobTitle: String?,
    val status: OrganizationMemberStatus,
    val joinedAt: String,
    val lastActiveAt: String?
)


/*
 * =============================================================
 * INVITATIONS
 * =============================================================
 */

data class OrganizationInvitationData(
    val email: String,
    val userId: String?,
    val roles: List<OrganizationRole>,
    val branchId: String?,
    val departmentId: String?,
    val jobTitle: String?,
    val message: String?,
    val expiresAt: String
)

data class OrganizationInvitationResult(
    val id: String,
    val organizationId: String,
    val organizationName: String,
    val email: String,
    val invitedBy: String,
    val roles: List<OrganizationRole>,
    val status: InvitationStatus,
    val expiresAt: String,
    val createdAt: String
)


/*
 * =============================================================
 * BRANCH
 * =============================================================
 */

data class CreateBranchData(
    val name: String,
    val code: String,
    val address: String?,
    val phone: String?,
    val managerId: String?
)

data class UpdateBranchData(
    val name: String?,
    val address: String?,
    val phone: String?,
    val managerId: String?,
    val active: Boolean?
)

data class BranchData(
    val id: String,
    val organizationId: String,
    val name: String,
    val code: String,
    val address: String?,
    val phone: String?,
    val managerId: String?,
    val active: Boolean,
    val createdAt: String
)


/*
 * =============================================================
 * DEPARTMENT
 * =============================================================
 */

data class CreateDepartmentData(
    val name: String,
    val description: String?,
    val managerId: String?
)

data class DepartmentData(
    val id: String,
    val organizationId: String,
    val name: String,
    val description: String?,
    val managerId: String?,
    val memberCount: Int,
    val active: Boolean
)


/*
 * =============================================================
 * TEAM
 * =============================================================
 */

data class CreateTeamData(
    val name: String,
    val description: String?,
    val departmentId: String?,
    val leaderId: String?
)

data class TeamData(
    val id: String,
    val organizationId: String,
    val name: String,
    val description: String?,
    val departmentId: String?,
    val leaderId: String?,
    val memberCount: Int,
    val active: Boolean
)


/*
 * =============================================================
 * PROPERTY ASSIGNMENT
 * =============================================================
 */

data class PropertyAssignmentData(
    val id: String = "",
    val organizationId: String,
    val propertyId: String,
    val branchId: String?,
    val managerId: String?,
    val assignedAt: String = "",
    val active: Boolean = true
)


/*
 * =============================================================
 * PROPERTY OWNERSHIP
 * =============================================================
 */

data class PropertyOwnerData(
    val id: String,
    val propertyId: String,
    val ownerId: String,
    val ownerName: String?,
    val ownershipPercentage: Double,
    val verified: Boolean,
    val createdAt: String
)


/*
 * =============================================================
 * VERIFICATION
 * =============================================================
 */

data class OrganizationVerificationData(
    val registrationNumber: String?,
    val taxNumber: String?,
    val businessLicenseUrl: String?,
    val certificateUrl: String?,
    val proofOfAddressUrl: String?,
    val representativeIdUrl: String?
)

data class OrganizationVerificationResult(
    val organizationId: String,
    val status: OrganizationVerificationStatus,
    val registrationNumber: String?,
    val taxNumber: String?,
    val reviewedBy: String?,
    val reviewedAt: String?,
    val rejectionReason: String?
)


/*
 * =============================================================
 * SETTINGS
 * =============================================================
 */

data class OrganizationSettingsData(
    val timezone: String,
    val currency: String,
    val defaultRentDueDay: Int,
    val gracePeriodDays: Int,
    val defaultLateFeeEnabled: Boolean,
    val defaultLateFeePercentage: Double,
    val autoSendPaymentReminders: Boolean,
    val autoSendLeaseReminders: Boolean,
    val autoCreateReceipts: Boolean,
    val requireTwoFactorForStaff: Boolean,
    val requireApprovalForRefunds: Boolean,
    val requireApprovalForRentChanges: Boolean
)


/*
 * =============================================================
 * SUBSCRIPTION
 * =============================================================
 */

data class OrganizationSubscriptionData(
    val organizationId: String,
    val plan: OrganizationPlan,
    val status: SubscriptionStatus,
    val propertyLimit: Int,
    val memberLimit: Int,
    val storageLimitMb: Long,
    val currentProperties: Int,
    val currentMembers: Int,
    val renewalDate: String?
)


/*
 * =============================================================
 * USAGE
 * =============================================================
 */

data class OrganizationUsageData(
    val properties: Int,
    val units: Int,
    val tenants: Int,
    val members: Int,
    val documents: Int,
    val storageMb: Long,
    val activeListings: Int,
    val monthlyPayments: Int
)


/*
 * =============================================================
 * ANALYTICS
 * =============================================================
 */

data class OrganizationAnalyticsData(
    val organizationId: String,
    val totalProperties: Int,
    val totalUnits: Int,
    val occupiedUnits: Int,
    val vacantUnits: Int,
    val totalTenants: Int,
    val totalStaff: Int,
    val totalBranches: Int,
    val totalTeams: Int,
    val monthlyRentExpected: Double,
    val monthlyRentCollected: Double,
    val outstandingRent: Double,
    val maintenanceOpen: Int,
    val activeListings: Int,
    val occupancyRate: Double
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class OrganizationType {

    INDIVIDUAL_LANDLORD,

    PROPERTY_MANAGER,

    PROPERTY_MANAGEMENT_COMPANY,

    REAL_ESTATE_AGENCY,

    BROKERAGE,

    HOUSING_COOPERATIVE,

    CORPORATE_LANDLORD,

    INVESTMENT_GROUP,

    NGO,

    GOVERNMENT,

    OTHER
}


enum class OrganizationRole {

    OWNER,

    CO_OWNER,

    ADMIN,

    PROPERTY_MANAGER,

    BRANCH_MANAGER,

    CARETAKER,

    BROKER,

    ACCOUNTANT,

    MAINTENANCE_MANAGER,

    SUPPORT_AGENT,

    SECURITY_MANAGER,

    INSPECTOR,

    LEASING_AGENT,

    FINANCE_MANAGER,

    STAFF
}


enum class OrganizationMemberStatus {

    INVITED,

    ACTIVE,

    SUSPENDED,

    INACTIVE,

    REMOVED
}


enum class InvitationStatus {

    PENDING,

    ACCEPTED,

    REJECTED,

    CANCELLED,

    EXPIRED
}


enum class OrganizationVerificationStatus {

    NOT_SUBMITTED,

    PENDING,

    VERIFIED,

    REJECTED,

    EXPIRED
}


enum class OrganizationPlan {

    FREE,

    STARTER,

    PROFESSIONAL,

    BUSINESS,

    ENTERPRISE
}


enum class SubscriptionStatus {

    ACTIVE,

    TRIAL,

    PAST_DUE,

    CANCELLED,

    EXPIRED
}