package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * STAFF REPOSITORY
 * =============================================================
 *
 * Handles employees and operational staff working for:
 *
 * - Landlords
 * - Property managers
 * - Property management companies
 * - Apartment complexes
 * - Estates
 *
 * Supports:
 * - Staff profiles
 * - Roles
 * - Permissions
 * - Property assignments
 * - Unit assignments
 * - Attendance
 * - Shifts
 * - Leave
 * - Tasks
 * - Performance
 * - Activity logs
 * - Staff documents
 * - Staff notifications
 * - Payroll references
 * - Emergency contacts
 *
 * =============================================================
 */

interface StaffRepository {

    /*
     * ---------------------------------------------------------
     * STAFF CREATION
     * ---------------------------------------------------------
     */

    suspend fun createStaff(
        createdBy: String,
        staff: CreateStaffData
    ): Result<String>

    suspend fun getStaff(
        staffId: String
    ): Result<StaffData>

    fun observeStaff(
        staffId: String
    ): Flow<Result<StaffData>>

    suspend fun updateStaff(
        userId: String,
        staffId: String,
        staff: UpdateStaffData
    ): Result<Unit>

    suspend fun deactivateStaff(
        userId: String,
        staffId: String,
        reason: String?
    ): Result<Unit>

    suspend fun reactivateStaff(
        userId: String,
        staffId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * STAFF LISTING
     * ---------------------------------------------------------
     */

    suspend fun getStaffByOrganization(
        organizationId: String
    ): Result<List<StaffData>>

    suspend fun getStaffByProperty(
        propertyId: String
    ): Result<List<StaffData>>

    suspend fun getStaffByRole(
        organizationId: String,
        role: StaffRole
    ): Result<List<StaffData>>

    suspend fun searchStaff(
        organizationId: String,
        query: String
    ): Result<List<StaffData>>


    /*
     * ---------------------------------------------------------
     * ROLES
     * ---------------------------------------------------------
     */

    suspend fun createRole(
        userId: String,
        role: CreateStaffRoleData
    ): Result<String>

    suspend fun updateRole(
        userId: String,
        roleId: String,
        role: UpdateStaffRoleData
    ): Result<Unit>

    suspend fun deleteRole(
        userId: String,
        roleId: String
    ): Result<Unit>

    suspend fun getRoles(
        organizationId: String
    ): Result<List<StaffRoleData>>

    suspend fun assignRole(
        userId: String,
        staffId: String,
        roleId: String
    ): Result<Unit>

    suspend fun removeRole(
        userId: String,
        staffId: String,
        roleId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * PERMISSIONS
     * ---------------------------------------------------------
     */

    suspend fun getPermissions(
        staffId: String
    ): Result<List<StaffPermission>>

    suspend fun grantPermission(
        userId: String,
        staffId: String,
        permission: StaffPermission
    ): Result<Unit>

    suspend fun revokePermission(
        userId: String,
        staffId: String,
        permission: StaffPermission
    ): Result<Unit>

    suspend fun hasPermission(
        staffId: String,
        permission: StaffPermission
    ): Result<Boolean>


    /*
     * ---------------------------------------------------------
     * PROPERTY ASSIGNMENTS
     * ---------------------------------------------------------
     */

    suspend fun assignProperty(
        userId: String,
        staffId: String,
        propertyId: String
    ): Result<Unit>

    suspend fun unassignProperty(
        userId: String,
        staffId: String,
        propertyId: String
    ): Result<Unit>

    suspend fun getAssignedProperties(
        staffId: String
    ): Result<List<String>>

    suspend fun getPropertyStaff(
        propertyId: String
    ): Result<List<StaffData>>


    /*
     * ---------------------------------------------------------
     * UNIT ASSIGNMENTS
     * ---------------------------------------------------------
     */

    suspend fun assignUnit(
        userId: String,
        staffId: String,
        unitId: String
    ): Result<Unit>

    suspend fun unassignUnit(
        userId: String,
        staffId: String,
        unitId: String
    ): Result<Unit>

    suspend fun getAssignedUnits(
        staffId: String
    ): Result<List<String>>


    /*
     * ---------------------------------------------------------
     * ATTENDANCE
     * ---------------------------------------------------------
     */

    suspend fun clockIn(
        staffId: String,
        attendance: ClockInData
    ): Result<String>

    suspend fun clockOut(
        staffId: String,
        attendanceId: String
    ): Result<Unit>

    suspend fun getAttendance(
        staffId: String,
        startDate: String,
        endDate: String
    ): Result<List<AttendanceData>>

    suspend fun getPropertyAttendance(
        propertyId: String,
        date: String
    ): Result<List<AttendanceData>>

    fun observeCurrentAttendance(
        propertyId: String
    ): Flow<Result<List<AttendanceData>>>


    /*
     * ---------------------------------------------------------
     * SHIFTS
     * ---------------------------------------------------------
     */

    suspend fun createShift(
        userId: String,
        shift: CreateShiftData
    ): Result<String>

    suspend fun updateShift(
        userId: String,
        shiftId: String,
        shift: UpdateShiftData
    ): Result<Unit>

    suspend fun assignShift(
        userId: String,
        shiftId: String,
        staffId: String
    ): Result<Unit>

    suspend fun getStaffShifts(
        staffId: String,
        startDate: String,
        endDate: String
    ): Result<List<ShiftData>>


    /*
     * ---------------------------------------------------------
     * LEAVE
     * ---------------------------------------------------------
     */

    suspend fun requestLeave(
        staffId: String,
        leave: CreateLeaveRequestData
    ): Result<String>

    suspend fun approveLeave(
        approverId: String,
        leaveId: String
    ): Result<Unit>

    suspend fun rejectLeave(
        approverId: String,
        leaveId: String,
        reason: String
    ): Result<Unit>

    suspend fun cancelLeave(
        staffId: String,
        leaveId: String
    ): Result<Unit>

    suspend fun getStaffLeave(
        staffId: String
    ): Result<List<LeaveRequestData>>


    /*
     * ---------------------------------------------------------
     * STAFF TASKS
     * ---------------------------------------------------------
     */

    suspend fun createTask(
        createdBy: String,
        task: CreateStaffTaskData
    ): Result<String>

    suspend fun assignTask(
        userId: String,
        taskId: String,
        staffId: String
    ): Result<Unit>

    suspend fun updateTask(
        staffId: String,
        taskId: String,
        update: UpdateStaffTaskData
    ): Result<Unit>

    suspend fun completeTask(
        staffId: String,
        taskId: String,
        completionNote: String?
    ): Result<Unit>

    suspend fun getStaffTasks(
        staffId: String
    ): Result<List<StaffTaskData>>

    suspend fun getPropertyTasks(
        propertyId: String
    ): Result<List<StaffTaskData>>


    /*
     * ---------------------------------------------------------
     * STAFF DOCUMENTS
     * ---------------------------------------------------------
     */

    suspend fun attachDocument(
        userId: String,
        staffId: String,
        documentId: String
    ): Result<Unit>

    suspend fun getStaffDocuments(
        staffId: String
    ): Result<List<String>>

    suspend fun removeDocument(
        userId: String,
        staffId: String,
        documentId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * EMERGENCY CONTACTS
     * ---------------------------------------------------------
     */

    suspend fun addEmergencyContact(
        userId: String,
        staffId: String,
        contact: EmergencyContactData
    ): Result<String>

    suspend fun updateEmergencyContact(
        userId: String,
        contactId: String,
        contact: EmergencyContactData
    ): Result<Unit>

    suspend fun deleteEmergencyContact(
        userId: String,
        contactId: String
    ): Result<Unit>

    suspend fun getEmergencyContacts(
        staffId: String
    ): Result<List<EmergencyContactData>>


    /*
     * ---------------------------------------------------------
     * PERFORMANCE
     * ---------------------------------------------------------
     */

    suspend fun createPerformanceReview(
        reviewerId: String,
        review: CreatePerformanceReviewData
    ): Result<String>

    suspend fun getPerformanceReviews(
        staffId: String
    ): Result<List<PerformanceReviewData>>

    suspend fun updatePerformanceReview(
        reviewerId: String,
        reviewId: String,
        update: UpdatePerformanceReviewData
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * STAFF ACTIVITY
     * ---------------------------------------------------------
     */

    suspend fun getActivityLogs(
        staffId: String,
        startDate: String? = null,
        endDate: String? = null
    ): Result<List<StaffActivityLogData>>

    suspend fun recordActivity(
        staffId: String,
        activity: StaffActivityLogData
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * STAFF ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getStaffAnalytics(
        organizationId: String
    ): Result<StaffAnalyticsData>

    suspend fun getPropertyStaffAnalytics(
        propertyId: String
    ): Result<PropertyStaffAnalyticsData>
}


/*
 * =============================================================
 * DATA CONTRACTS
 * =============================================================
 */

data class CreateStaffData(
    val organizationId: String,
    val userId: String?,
    val fullName: String,
    val email: String?,
    val phoneNumber: String?,
    val nationalId: String?,
    val role: StaffRole,
    val employmentType: EmploymentType,
    val startDate: String,
    val propertyIds: List<String> = emptyList()
)

data class UpdateStaffData(
    val fullName: String?,
    val email: String?,
    val phoneNumber: String?,
    val employmentType: EmploymentType?,
    val status: StaffStatus?
)

data class StaffData(
    val id: String,
    val organizationId: String,
    val userId: String?,
    val fullName: String,
    val email: String?,
    val phoneNumber: String?,
    val role: StaffRole,
    val employmentType: EmploymentType,
    val status: StaffStatus,
    val startDate: String,
    val assignedPropertyCount: Int,
    val assignedUnitCount: Int,
    val pendingTaskCount: Int
)

data class CreateStaffRoleData(
    val organizationId: String,
    val name: String,
    val description: String?,
    val permissions: List<StaffPermission>
)

data class UpdateStaffRoleData(
    val name: String?,
    val description: String?,
    val permissions: List<StaffPermission>?
)

data class StaffRoleData(
    val id: String,
    val organizationId: String,
    val name: String,
    val description: String?,
    val permissions: List<StaffPermission>
)

data class ClockInData(
    val propertyId: String?,
    val latitude: Double?,
    val longitude: Double?,
    val timestamp: String,
    val notes: String?
)

data class AttendanceData(
    val id: String,
    val staffId: String,
    val propertyId: String?,
    val clockIn: String,
    val clockOut: String?,
    val latitude: Double?,
    val longitude: Double?,
    val status: AttendanceStatus,
    val notes: String?
)

data class CreateShiftData(
    val propertyId: String?,
    val name: String,
    val startTime: String,
    val endTime: String,
    val date: String
)

data class UpdateShiftData(
    val name: String?,
    val startTime: String?,
    val endTime: String?,
    val date: String?
)

data class ShiftData(
    val id: String,
    val propertyId: String?,
    val name: String,
    val startTime: String,
    val endTime: String,
    val date: String,
    val assignedStaffId: String?
)

data class CreateLeaveRequestData(
    val leaveType: LeaveType,
    val startDate: String,
    val endDate: String,
    val reason: String?
)

data class LeaveRequestData(
    val id: String,
    val staffId: String,
    val leaveType: LeaveType,
    val startDate: String,
    val endDate: String,
    val reason: String?,
    val status: LeaveStatus,
    val approvedBy: String?
)

data class CreateStaffTaskData(
    val propertyId: String?,
    val unitId: String?,
    val title: String,
    val description: String?,
    val priority: TaskPriority,
    val dueDate: String?
)

data class UpdateStaffTaskData(
    val status: StaffTaskStatus?,
    val priority: TaskPriority?,
    val dueDate: String?,
    val notes: String?
)

data class StaffTaskData(
    val id: String,
    val propertyId: String?,
    val unitId: String?,
    val title: String,
    val description: String?,
    val priority: TaskPriority,
    val status: StaffTaskStatus,
    val dueDate: String?,
    val assignedStaffId: String?,
    val createdAt: String
)

data class EmergencyContactData(
    val id: String = "",
    val name: String,
    val relationship: String,
    val phoneNumber: String,
    val alternativePhoneNumber: String?,
    val address: String?
)

data class CreatePerformanceReviewData(
    val staffId: String,
    val reviewPeriodStart: String,
    val reviewPeriodEnd: String,
    val rating: Double,
    val strengths: String?,
    val weaknesses: String?,
    val recommendations: String?
)

data class UpdatePerformanceReviewData(
    val rating: Double?,
    val strengths: String?,
    val weaknesses: String?,
    val recommendations: String?
)

data class PerformanceReviewData(
    val id: String,
    val staffId: String,
    val reviewerId: String,
    val reviewPeriodStart: String,
    val reviewPeriodEnd: String,
    val rating: Double,
    val strengths: String?,
    val weaknesses: String?,
    val recommendations: String?,
    val createdAt: String
)

data class StaffActivityLogData(
    val id: String = "",
    val staffId: String,
    val action: StaffActivityAction,
    val propertyId: String?,
    val description: String?,
    val timestamp: String
)


data class PropertyStaffAnalyticsData(
    val propertyId: String,
    val totalStaff: Int,
    val activeStaff: Int,
    val pendingTasks: Int,
    val completedTasks: Int,
    val attendanceRate: Double
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class StaffRole {

    PROPERTY_MANAGER,

    CARETAKER,

    ACCOUNTANT,

    RECEPTIONIST,

    SECURITY,

    CLEANER,

    MAINTENANCE_COORDINATOR,

    INSPECTOR,

    GROUNDSKEEPER,

    ADMINISTRATOR,

    OTHER
}


enum class StaffStatus {

    ACTIVE,

    INACTIVE,

    SUSPENDED,

    ON_LEAVE,

    TERMINATED
}

enum class StaffPermission {

    VIEW_PROPERTIES,

    EDIT_PROPERTIES,

    VIEW_TENANTS,

    EDIT_TENANTS,

    VIEW_RENT,

    RECORD_RENT,

    VIEW_PAYMENTS,

    MANAGE_PAYMENTS,

    VIEW_EXPENSES,

    CREATE_EXPENSES,

    APPROVE_EXPENSES,

    VIEW_MAINTENANCE,

    CREATE_MAINTENANCE,

    ASSIGN_MAINTENANCE,

    VIEW_INSPECTIONS,

    CREATE_INSPECTIONS,

    VIEW_DOCUMENTS,

    UPLOAD_DOCUMENTS,

    VERIFY_DOCUMENTS,

    SEND_MESSAGES,

    SEND_ANNOUNCEMENTS,

    VIEW_REPORTS,

    EXPORT_REPORTS,

    MANAGE_STAFF,

    MANAGE_SETTINGS
}

enum class AttendanceStatus {

    PRESENT,

    LATE,

    ABSENT,

    HALF_DAY,

    OFF_DAY
}

enum class LeaveType {

    ANNUAL,

    SICK,

    MATERNITY,

    PATERNITY,

    EMERGENCY,

    UNPAID,

    OTHER
}

enum class LeaveStatus {

    PENDING,

    APPROVED,

    REJECTED,

    CANCELLED
}


enum class StaffTaskStatus {

    PENDING,

    IN_PROGRESS,

    COMPLETED,

    CANCELLED,

    OVERDUE
}

enum class StaffActivityAction {

    LOGIN,

    LOGOUT,

    VIEWED_PROPERTY,

    UPDATED_PROPERTY,

    VIEWED_TENANT,

    UPDATED_TENANT,

    COLLECTED_PAYMENT,

    CREATED_EXPENSE,

    APPROVED_EXPENSE,

    CREATED_MAINTENANCE_REQUEST,

    UPDATED_MAINTENANCE_REQUEST,

    COMPLETED_MAINTENANCE,

    CREATED_INSPECTION,

    COMPLETED_INSPECTION,

    SENT_MESSAGE,

    SENT_ANNOUNCEMENT,

    UPLOADED_DOCUMENT,

    VERIFIED_DOCUMENT,

    OTHER
}