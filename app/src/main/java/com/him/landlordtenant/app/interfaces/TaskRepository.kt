package com.him.landlordtenant.app.interfaces

import com.him.landlordtenant.app.data.model.TaskPriority
import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * TASK REPOSITORY
 * =============================================================
 *
 * Central workflow engine for property management operations.
 *
 * Handles:
 *
 * - Maintenance tasks
 * - Inspection tasks
 * - Viewing tasks
 * - Rent follow-ups
 * - Arrears follow-ups
 * - Document verification
 * - Agreement signing
 * - Property onboarding
 * - Tenant onboarding
 * - Move-in tasks
 * - Move-out tasks
 * - Utility meter reading tasks
 * - Property advertising tasks
 * - Staff tasks
 * - Contractor tasks
 * - Recurring tasks
 * - Deadlines
 * - Reminders
 * - Escalations
 * - Task dependencies
 * - Automation rules
 * - Task history
 *
 * =============================================================
 */

interface TaskRepository {

    /*
     * ---------------------------------------------------------
     * CREATE TASK
     * ---------------------------------------------------------
     */

    suspend fun createTask(
        createdBy: String,
        task: CreateTaskData
    ): Result<String>

    suspend fun getTask(
        taskId: String
    ): Result<TaskData>

    fun observeTask(
        taskId: String
    ): Flow<Result<TaskData>>

    suspend fun updateTask(
        userId: String,
        taskId: String,
        update: UpdateTaskData
    ): Result<Unit>

    suspend fun deleteTask(
        userId: String,
        taskId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * TASK LISTS
     * ---------------------------------------------------------
     */

    suspend fun getTasksForUser(
        userId: String
    ): Result<List<TaskData>>

    suspend fun getTasksForProperty(
        propertyId: String
    ): Result<List<TaskData>>

    suspend fun getTasksForUnit(
        unitId: String
    ): Result<List<TaskData>>

    suspend fun getTasksForTenant(
        tenantId: String
    ): Result<List<TaskData>>

    suspend fun getTasksForStaff(
        staffId: String
    ): Result<List<TaskData>>

    suspend fun getTasksForContractor(
        contractorId: String
    ): Result<List<TaskData>>

    suspend fun getTasksByStatus(
        userId: String,
        status: TaskStatus
    ): Result<List<TaskData>>

    suspend fun getTasksByPriority(
        userId: String,
        priority: TaskPriority
    ): Result<List<TaskData>>

    suspend fun getOverdueTasks(
        userId: String
    ): Result<List<TaskData>>


    /*
     * ---------------------------------------------------------
     * ASSIGNMENT
     * ---------------------------------------------------------
     */

    suspend fun assignTaskToStaff(
        userId: String,
        taskId: String,
        staffId: String
    ): Result<Unit>

    suspend fun assignTaskToTenant(
        userId: String,
        taskId: String,
        tenantId: String
    ): Result<Unit>

    suspend fun assignTaskToContractor(
        userId: String,
        taskId: String,
        contractorId: String
    ): Result<Unit>

    suspend fun unassignTask(
        userId: String,
        taskId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * STATUS
     * ---------------------------------------------------------
     */

    suspend fun startTask(
        userId: String,
        taskId: String
    ): Result<Unit>

    suspend fun pauseTask(
        userId: String,
        taskId: String,
        reason: String?
    ): Result<Unit>

    suspend fun completeTask(
        userId: String,
        taskId: String,
        completionNote: String?
    ): Result<Unit>

    suspend fun cancelTask(
        userId: String,
        taskId: String,
        reason: String?
    ): Result<Unit>

    suspend fun reopenTask(
        userId: String,
        taskId: String,
        reason: String?
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * DEADLINES
     * ---------------------------------------------------------
     */

    suspend fun setDeadline(
        userId: String,
        taskId: String,
        deadline: String
    ): Result<Unit>

    suspend fun extendDeadline(
        userId: String,
        taskId: String,
        newDeadline: String,
        reason: String
    ): Result<Unit>

    suspend fun getTasksDueToday(
        userId: String
    ): Result<List<TaskData>>

    suspend fun getTasksDueTomorrow(
        userId: String
    ): Result<List<TaskData>>

    suspend fun getTasksDueThisWeek(
        userId: String
    ): Result<List<TaskData>>


    /*
     * ---------------------------------------------------------
     * PRIORITY
     * ---------------------------------------------------------
     */

    suspend fun changePriority(
        userId: String,
        taskId: String,
        priority: TaskPriority
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * COMMENTS
     * ---------------------------------------------------------
     */

    suspend fun addComment(
        userId: String,
        taskId: String,
        comment: String
    ): Result<String>

    suspend fun getComments(
        taskId: String
    ): Result<List<TaskCommentData>>

    suspend fun deleteComment(
        userId: String,
        commentId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * ATTACHMENTS
     * ---------------------------------------------------------
     */

    suspend fun attachDocument(
        userId: String,
        taskId: String,
        documentId: String
    ): Result<Unit>

    suspend fun getAttachments(
        taskId: String
    ): Result<List<String>>

    suspend fun removeAttachment(
        userId: String,
        taskId: String,
        documentId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * CHECKLISTS
     * ---------------------------------------------------------
     */

    suspend fun createChecklistItem(
        userId: String,
        taskId: String,
        item: CreateChecklistItemData
    ): Result<String>

    suspend fun updateChecklistItem(
        userId: String,
        itemId: String,
        update: UpdateChecklistItemData
    ): Result<Unit>

    suspend fun deleteChecklistItem(
        userId: String,
        itemId: String
    ): Result<Unit>

    suspend fun getChecklist(
        taskId: String
    ): Result<List<TaskChecklistItemData>>


    /*
     * ---------------------------------------------------------
     * TASK DEPENDENCIES
     * ---------------------------------------------------------
     */

    suspend fun addDependency(
        userId: String,
        taskId: String,
        dependsOnTaskId: String
    ): Result<Unit>

    suspend fun removeDependency(
        userId: String,
        taskId: String,
        dependsOnTaskId: String
    ): Result<Unit>

    suspend fun getDependencies(
        taskId: String
    ): Result<List<TaskDependencyData>>


    /*
     * ---------------------------------------------------------
     * TASK HISTORY
     * ---------------------------------------------------------
     */

    suspend fun getTaskHistory(
        taskId: String
    ): Result<List<TaskHistoryData>>


    /*
     * ---------------------------------------------------------
     * RECURRING TASKS
     * ---------------------------------------------------------
     */

    suspend fun createRecurringTask(
        userId: String,
        task: CreateRecurringTaskData
    ): Result<String>

    suspend fun updateRecurringTask(
        userId: String,
        recurringTaskId: String,
        update: UpdateRecurringTaskData
    ): Result<Unit>

    suspend fun pauseRecurringTask(
        userId: String,
        recurringTaskId: String
    ): Result<Unit>

    suspend fun resumeRecurringTask(
        userId: String,
        recurringTaskId: String
    ): Result<Unit>

    suspend fun cancelRecurringTask(
        userId: String,
        recurringTaskId: String
    ): Result<Unit>

    suspend fun getRecurringTasks(
        propertyId: String
    ): Result<List<RecurringTaskData>>


    /*
     * ---------------------------------------------------------
     * AUTOMATION RULES
     * ---------------------------------------------------------
     */

    suspend fun createAutomationRule(
        userId: String,
        rule: CreateTaskAutomationRuleData
    ): Result<String>

    suspend fun updateAutomationRule(
        userId: String,
        ruleId: String,
        update: UpdateTaskAutomationRuleData
    ): Result<Unit>

    suspend fun deleteAutomationRule(
        userId: String,
        ruleId: String
    ): Result<Unit>

    suspend fun getAutomationRules(
        organizationId: String
    ): Result<List<TaskAutomationRuleData>>

    suspend fun enableAutomationRule(
        userId: String,
        ruleId: String
    ): Result<Unit>

    suspend fun disableAutomationRule(
        userId: String,
        ruleId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * AUTOMATIC TASK GENERATION
     * ---------------------------------------------------------
     */

    suspend fun generateRentFollowUpTask(
        tenantId: String,
        billId: String
    ): Result<String>

    suspend fun generateArrearsTask(
        tenantId: String,
        amount: Double
    ): Result<String>

    suspend fun generateInspectionTask(
        propertyId: String,
        unitId: String?,
        inspectionDate: String
    ): Result<String>

    suspend fun generateViewingTask(
        propertyId: String,
        viewingId: String
    ): Result<String>

    suspend fun generateMaintenanceTask(
        propertyId: String,
        maintenanceRequestId: String
    ): Result<String>

    suspend fun generateDocumentVerificationTask(
        userId: String,
        documentId: String
    ): Result<String>

    suspend fun generateAgreementSigningTask(
        agreementId: String
    ): Result<String>

    suspend fun generateMeterReadingTask(
        meterId: String,
        dueDate: String
    ): Result<String>

    suspend fun generateMoveInTask(
        tenantId: String,
        unitId: String
    ): Result<String>

    suspend fun generateMoveOutTask(
        tenantId: String,
        unitId: String
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * REMINDERS
     * ---------------------------------------------------------
     */

    suspend fun createReminder(
        userId: String,
        reminder: CreateTaskReminderData
    ): Result<String>

    suspend fun cancelReminder(
        userId: String,
        reminderId: String
    ): Result<Unit>

    suspend fun getTaskReminders(
        taskId: String
    ): Result<List<TaskReminderData>>


    /*
     * ---------------------------------------------------------
     * ESCALATION
     * ---------------------------------------------------------
     */

    suspend fun escalateTask(
        userId: String,
        taskId: String,
        reason: String
    ): Result<Unit>

    suspend fun getEscalatedTasks(
        organizationId: String
    ): Result<List<TaskData>>


    /*
     * ---------------------------------------------------------
     * AUTOMATED PROCESSING
     * ---------------------------------------------------------
     */

    suspend fun processDueTasks(
        organizationId: String
    ): Result<Int>

    suspend fun processOverdueTasks(
        organizationId: String
    ): Result<Int>

    suspend fun processAutomationRules(
        organizationId: String
    ): Result<Int>


    /*
     * ---------------------------------------------------------
     * ANALYTICS
     * ---------------------------------------------------------
     */

    suspend fun getTaskAnalytics(
        organizationId: String,
        startDate: String,
        endDate: String
    ): Result<TaskAnalyticsData>

    suspend fun getPropertyTaskAnalytics(
        propertyId: String,
        startDate: String,
        endDate: String
    ): Result<PropertyTaskAnalyticsData>
}


/*
 * =============================================================
 * DATA CONTRACTS
 * =============================================================
 */

data class CreateTaskData(
    val organizationId: String,
    val propertyId: String?,
    val unitId: String?,
    val tenantId: String?,
    val title: String,
    val description: String?,
    val type: TaskType,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val assignedStaffId: String? = null,
    val assignedContractorId: String? = null,
    val deadline: String? = null
)

data class UpdateTaskData(
    val title: String?,
    val description: String?,
    val priority: TaskPriority?,
    val deadline: String?,
    val status: TaskStatus?
)

data class TaskData(
    val id: String,
    val organizationId: String,
    val propertyId: String?,
    val unitId: String?,
    val tenantId: String?,
    val title: String,
    val description: String?,
    val type: TaskType,
    val priority: TaskPriority,
    val status: TaskStatus,
    val assignedStaffId: String?,
    val assignedContractorId: String?,
    val deadline: String?,
    val createdBy: String,
    val createdAt: String,
    val completedAt: String?
)

data class TaskCommentData(
    val id: String,
    val taskId: String,
    val userId: String,
    val comment: String,
    val createdAt: String
)

data class CreateChecklistItemData(
    val title: String,
    val description: String?
)

data class UpdateChecklistItemData(
    val title: String?,
    val description: String?,
    val completed: Boolean?
)

data class TaskChecklistItemData(
    val id: String,
    val taskId: String,
    val title: String,
    val description: String?,
    val completed: Boolean,
    val completedBy: String?,
    val completedAt: String?
)

data class TaskDependencyData(
    val taskId: String,
    val dependsOnTaskId: String,
    val dependencyType: TaskDependencyType
)

data class TaskHistoryData(
    val id: String,
    val taskId: String,
    val userId: String,
    val action: TaskHistoryAction,
    val description: String?,
    val timestamp: String
)

data class CreateRecurringTaskData(
    val organizationId: String,
    val propertyId: String?,
    val title: String,
    val description: String?,
    val type: TaskType,
    val priority: TaskPriority,
    val frequency: TaskFrequency,
    val nextRunDate: String,
    val assignedStaffId: String?
)

data class UpdateRecurringTaskData(
    val title: String?,
    val description: String?,
    val priority: TaskPriority?,
    val frequency: TaskFrequency?,
    val nextRunDate: String?,
    val assignedStaffId: String?
)



data class CreateTaskAutomationRuleData(
    val organizationId: String,
    val name: String,
    val description: String?,
    val trigger: TaskAutomationTrigger,
    val action: TaskAutomationAction,
    val taskType: TaskType?,
    val priority: TaskPriority?,
    val active: Boolean = true
)

data class UpdateTaskAutomationRuleData(
    val name: String?,
    val description: String?,
    val trigger: TaskAutomationTrigger?,
    val action: TaskAutomationAction?,
    val taskType: TaskType?,
    val priority: TaskPriority?,
    val active: Boolean?
)

data class TaskAutomationRuleData(
    val id: String,
    val organizationId: String,
    val name: String,
    val description: String?,
    val trigger: TaskAutomationTrigger,
    val action: TaskAutomationAction,
    val taskType: TaskType?,
    val priority: TaskPriority?,
    val active: Boolean
)

data class CreateTaskReminderData(
    val taskId: String,
    val remindAt: String,
    val channel: TaskReminderChannel,
    val message: String?
)

data class TaskReminderData(
    val id: String,
    val taskId: String,
    val remindAt: String,
    val channel: TaskReminderChannel,
    val message: String?,
    val sent: Boolean
)

data class TaskAnalyticsData(
    val totalTasks: Int,
    val completedTasks: Int,
    val pendingTasks: Int,
    val inProgressTasks: Int,
    val overdueTasks: Int,
    val cancelledTasks: Int,
    val averageCompletionTimeHours: Double,
    val completionRate: Double,
    val escalationCount: Int
)

data class PropertyTaskAnalyticsData(
    val propertyId: String,
    val totalTasks: Int,
    val completedTasks: Int,
    val pendingTasks: Int,
    val overdueTasks: Int,
    val maintenanceTasks: Int,
    val inspectionTasks: Int,
    val viewingTasks: Int,
    val completionRate: Double
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class TaskType {

    GENERAL,

    MAINTENANCE,

    INSPECTION,

    VIEWING,

    RENT_FOLLOW_UP,

    ARREARS_FOLLOW_UP,

    DOCUMENT_VERIFICATION,

    AGREEMENT_SIGNING,

    MOVE_IN,

    MOVE_OUT,

    METER_READING,

    PROPERTY_ONBOARDING,

    TENANT_ONBOARDING,

    ADVERTISING,

    PAYMENT_RECONCILIATION,

    UTILITY_BILLING,

    EXPENSE_APPROVAL,

    STAFF_TASK,

    CONTRACTOR_TASK,

    OTHER
}

enum class TaskStatus {

    PENDING,

    IN_PROGRESS,

    PAUSED,

    COMPLETED,

    CANCELLED,

    OVERDUE,

    ESCALATED
}

enum class TaskPriority {

    LOW,

    MEDIUM,

    HIGH,

    URGENT
}

enum class TaskDependencyType {

    BLOCKED_BY,

    RELATED_TO,

    REQUIRED_BEFORE
}

enum class TaskHistoryAction {

    CREATED,

    ASSIGNED,

    UNASSIGNED,

    STARTED,

    PAUSED,

    COMPLETED,

    CANCELLED,

    REOPENED,

    PRIORITY_CHANGED,

    DEADLINE_CHANGED,

    COMMENT_ADDED,

    ATTACHMENT_ADDED,

    ESCALATED
}

enum class TaskFrequency {

    DAILY,

    WEEKLY,

    BI_WEEKLY,

    MONTHLY,

    QUARTERLY,

    SEMI_ANNUALLY,

    ANNUALLY
}

enum class TaskAutomationTrigger {

    RENT_OVERDUE,

    BILL_OVERDUE,

    PAYMENT_RECEIVED,

    MAINTENANCE_CREATED,

    INSPECTION_DUE,

    VIEWING_BOOKED,

    DOCUMENT_UPLOADED,

    AGREEMENT_CREATED,

    AGREEMENT_SIGNED,

    TENANT_MOVING_IN,

    TENANT_MOVING_OUT,

    METER_READING_DUE,

    EXPENSE_CREATED,

    TASK_OVERDUE,

    PROPERTY_CREATED,

    UNIT_VACANT,

    CUSTOM
}

enum class TaskAutomationAction {

    CREATE_TASK,

    SEND_NOTIFICATION,

    SEND_MESSAGE,

    ASSIGN_STAFF,

    ESCALATE_TASK,

    CREATE_REMINDER
}

enum class TaskReminderChannel {

    IN_APP,

    PUSH,

    SMS,

    EMAIL
}