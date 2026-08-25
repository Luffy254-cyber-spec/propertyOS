package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * AUTOMATION REPOSITORY
 * =============================================================
 *
 * Central automation/workflow layer.
 *
 * Automates:
 *
 * - Rent reminders
 * - Arrears reminders
 * - Payment receipts
 * - Bill reminders
 * - Lease expiry reminders
 * - Agreement reminders
 * - Maintenance follow-ups
 * - Viewing reminders
 * - Application follow-ups
 * - Vacancy advertising
 * - Scheduled messages
 * - Recurring tasks
 * - Staff assignments
 * - Escalations
 * - Notifications
 * - Report generation
 * - Payment follow-ups
 * - Tenant onboarding
 * - Property onboarding
 * - Compliance reminders
 *
 * =============================================================
 */

interface AutomationRepository {

    /*
     * ---------------------------------------------------------
     * AUTOMATION RULES
     * ---------------------------------------------------------
     */

    suspend fun createRule(
        actorId: String,
        rule: CreateAutomationRuleData
    ): Result<String>

    suspend fun getRule(
        ruleId: String
    ): Result<AutomationRuleData>

    suspend fun getRules(
        ownerId: String
    ): Result<List<AutomationRuleData>>

    fun observeRules(
        ownerId: String
    ): Flow<Result<List<AutomationRuleData>>>

    suspend fun updateRule(
        actorId: String,
        ruleId: String,
        update: UpdateAutomationRuleData
    ): Result<Unit>

    suspend fun deleteRule(
        actorId: String,
        ruleId: String
    ): Result<Unit>

    suspend fun enableRule(
        actorId: String,
        ruleId: String
    ): Result<Unit>

    suspend fun disableRule(
        actorId: String,
        ruleId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * RULE EXECUTION
     * ---------------------------------------------------------
     */

    suspend fun executeRule(
        actorId: String,
        ruleId: String
    ): Result<AutomationExecutionData>

    suspend fun testRule(
        actorId: String,
        ruleId: String
    ): Result<AutomationTestResultData>

    suspend fun getExecution(
        executionId: String
    ): Result<AutomationExecutionData>

    suspend fun getExecutionHistory(
        ruleId: String,
        limit: Int = 50
    ): Result<List<AutomationExecutionData>>

    fun observeExecutionHistory(
        ruleId: String
    ): Flow<Result<List<AutomationExecutionData>>>


    /*
     * ---------------------------------------------------------
     * RENT AUTOMATION
     * ---------------------------------------------------------
     */

    suspend fun createRentReminderRule(
        actorId: String,
        configuration: RentReminderConfiguration
    ): Result<String>

    suspend fun createArrearsReminderRule(
        actorId: String,
        configuration: ArrearsReminderConfiguration
    ): Result<String>

    suspend fun createPaymentReceiptRule(
        actorId: String,
        configuration: PaymentReceiptConfiguration
    ): Result<String>

    suspend fun createPaymentFailureRule(
        actorId: String,
        configuration: PaymentFailureConfiguration
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * BILL AUTOMATION
     * ---------------------------------------------------------
     */

    suspend fun createBillReminderRule(
        actorId: String,
        configuration: BillReminderConfiguration
    ): Result<String>

    suspend fun createBillOverdueRule(
        actorId: String,
        configuration: BillOverdueConfiguration
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * LEASE AUTOMATION
     * ---------------------------------------------------------
     */

    suspend fun createLeaseExpiryRule(
        actorId: String,
        configuration: LeaseExpiryConfiguration
    ): Result<String>

    suspend fun createLeaseRenewalRule(
        actorId: String,
        configuration: LeaseRenewalConfiguration
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * AGREEMENT AUTOMATION
     * ---------------------------------------------------------
     */

    suspend fun createAgreementReminderRule(
        actorId: String,
        configuration: AgreementReminderConfiguration
    ): Result<String>

    suspend fun createAgreementExpiryRule(
        actorId: String,
        configuration: AgreementExpiryConfiguration
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * MAINTENANCE AUTOMATION
     * ---------------------------------------------------------
     */

    suspend fun createMaintenanceAssignmentRule(
        actorId: String,
        configuration: MaintenanceAssignmentConfiguration
    ): Result<String>

    suspend fun createMaintenanceFollowUpRule(
        actorId: String,
        configuration: MaintenanceFollowUpConfiguration
    ): Result<String>

    suspend fun createMaintenanceEscalationRule(
        actorId: String,
        configuration: MaintenanceEscalationConfiguration
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * PROPERTY AUTOMATION
     * ---------------------------------------------------------
     */

    suspend fun createVacancyAdvertisingRule(
        actorId: String,
        configuration: VacancyAdvertisingConfiguration
    ): Result<String>

    suspend fun createPropertyOnboardingRule(
        actorId: String,
        configuration: PropertyOnboardingConfiguration
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * TENANT AUTOMATION
     * ---------------------------------------------------------
     */

    suspend fun createTenantOnboardingRule(
        actorId: String,
        configuration: TenantOnboardingConfiguration
    ): Result<String>

    suspend fun createTenantWelcomeRule(
        actorId: String,
        configuration: TenantWelcomeConfiguration
    ): Result<String>

    suspend fun createTenantFollowUpRule(
        actorId: String,
        configuration: TenantFollowUpConfiguration
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * VIEWING AUTOMATION
     * ---------------------------------------------------------
     */

    suspend fun createViewingReminderRule(
        actorId: String,
        configuration: ViewingReminderConfiguration
    ): Result<String>

    suspend fun createViewingFollowUpRule(
        actorId: String,
        configuration: ViewingFollowUpConfiguration
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * APPLICATION AUTOMATION
     * ---------------------------------------------------------
     */

    suspend fun createApplicationFollowUpRule(
        actorId: String,
        configuration: ApplicationFollowUpConfiguration
    ): Result<String>

    suspend fun createApplicationStatusRule(
        actorId: String,
        configuration: ApplicationStatusConfiguration
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * MESSAGING AUTOMATION
     * ---------------------------------------------------------
     */

    suspend fun createScheduledMessage(
        actorId: String,
        message: ScheduledMessageData
    ): Result<String>

    suspend fun getScheduledMessages(
        ownerId: String
    ): Result<List<ScheduledMessageData>>

    suspend fun cancelScheduledMessage(
        actorId: String,
        messageId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * TASK AUTOMATION
     * ---------------------------------------------------------
     */

    suspend fun createRecurringTask(
        actorId: String,
        task: RecurringTaskData
    ): Result<String>

    suspend fun getRecurringTasks(
        ownerId: String
    ): Result<List<RecurringTaskData>>

    suspend fun updateRecurringTask(
        actorId: String,
        taskId: String,
        task: RecurringTaskData
    ): Result<Unit>

    suspend fun deleteRecurringTask(
        actorId: String,
        taskId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * STAFF AUTOMATION
     * ---------------------------------------------------------
     */

    suspend fun createStaffAssignmentRule(
        actorId: String,
        configuration: StaffAssignmentConfiguration
    ): Result<String>

    suspend fun createStaffEscalationRule(
        actorId: String,
        configuration: StaffEscalationConfiguration
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * REPORT AUTOMATION
     * ---------------------------------------------------------
     */

    suspend fun createScheduledReportRule(
        actorId: String,
        configuration: ScheduledReportAutomationConfiguration
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * COMPLIANCE AUTOMATION
     * ---------------------------------------------------------
     */

    suspend fun createComplianceReminderRule(
        actorId: String,
        configuration: ComplianceReminderConfiguration
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * BULK AUTOMATION
     * ---------------------------------------------------------
     */

    suspend fun executeForTenants(
        actorId: String,
        ruleId: String,
        tenantIds: List<String>
    ): Result<BulkAutomationResultData>

    suspend fun executeForProperties(
        actorId: String,
        ruleId: String,
        propertyIds: List<String>
    ): Result<BulkAutomationResultData>


    /*
     * ---------------------------------------------------------
     * AUTOMATION STATISTICS
     * ---------------------------------------------------------
     */

    suspend fun getAutomationStatistics(
        ownerId: String
    ): Result<AutomationStatisticsData>

    suspend fun getAutomationLogs(
        ownerId: String,
        limit: Int = 100
    ): Result<List<AutomationLogData>>
}


/*
 * =============================================================
 * RULE
 * =============================================================
 */

data class CreateAutomationRuleData(
    val ownerId: String,
    val name: String,
    val description: String?,
    val trigger: AutomationTrigger,
    val conditions: List<AutomationCondition>,
    val actions: List<AutomationAction>,
    val priority: Int = 0,
    val enabled: Boolean = true
)

data class UpdateAutomationRuleData(
    val name: String?,
    val description: String?,
    val trigger: AutomationTrigger?,
    val conditions: List<AutomationCondition>?,
    val actions: List<AutomationAction>?,
    val priority: Int?,
    val enabled: Boolean?
)

data class AutomationRuleData(
    val id: String,
    val ownerId: String,
    val name: String,
    val description: String?,
    val trigger: AutomationTrigger,
    val conditions: List<AutomationCondition>,
    val actions: List<AutomationAction>,
    val priority: Int,
    val enabled: Boolean,
    val executionCount: Int,
    val lastExecutedAt: String?,
    val nextExecutionAt: String?,
    val createdAt: String,
    val updatedAt: String
)


/*
 * =============================================================
 * TRIGGER
 * =============================================================
 */

sealed class AutomationTrigger {

    data class Schedule(
        val frequency: AutomationFrequency,
        val time: String,
        val dayOfWeek: Int? = null,
        val dayOfMonth: Int? = null
    ) : AutomationTrigger()

    data class Event(
        val event: AutomationEvent
    ) : AutomationTrigger()

    data class BeforeDate(
        val event: AutomationEvent,
        val daysBefore: Int
    ) : AutomationTrigger()

    data class AfterDate(
        val event: AutomationEvent,
        val daysAfter: Int
    ) : AutomationTrigger()

    data class Threshold(
        val metric: AutomationMetric,
        val operator: AutomationOperator,
        val value: Double
    ) : AutomationTrigger()

    data class Manual(
        val name: String = "Manual"
    ) : AutomationTrigger()
}


/*
 * =============================================================
 * CONDITIONS
 * =============================================================
 */

data class AutomationCondition(
    val field: String,
    val operator: AutomationOperator,
    val value: String
)


/*
 * =============================================================
 * ACTION
 * =============================================================
 */

sealed class AutomationAction {

    data class SendNotification(
        val recipientType: RecipientType,
        val title: String,
        val message: String
    ) : AutomationAction()

    data class SendMessage(
        val recipientType: RecipientType,
        val message: String,
        val channel: MessageChannel
    ) : AutomationAction()

    data class SendEmail(
        val recipientType: RecipientType,
        val subject: String,
        val body: String
    ) : AutomationAction()

    data class SendSms(
        val recipientType: RecipientType,
        val message: String
    ) : AutomationAction()

    data class CreateTask(
        val title: String,
        val description: String?,
        val assignTo: String?,
        val priority: String
    ) : AutomationAction()

    data class AssignStaff(
        val staffRole: String,
        val propertyId: String?,
        val taskType: String
    ) : AutomationAction()

    data class GenerateReport(
        val reportType: String,
        val format: String
    ) : AutomationAction()

    data class PublishListing(
        val propertyId: String
    ) : AutomationAction()

    data class UnpublishListing(
        val propertyId: String
    ) : AutomationAction()

    data class CreateInvoice(
        val recipientType: RecipientType
    ) : AutomationAction()

    data class CreatePaymentReminder(
        val recipientType: RecipientType
    ) : AutomationAction()

    data class Escalate(
        val escalationLevel: Int,
        val recipientType: RecipientType
    ) : AutomationAction()

    data class AddTag(
        val tag: String
    ) : AutomationAction()

    data class Webhook(
        val url: String
    ) : AutomationAction()
}


/*
 * =============================================================
 * EXECUTION
 * =============================================================
 */

data class AutomationExecutionData(
    val id: String,
    val ruleId: String,
    val status: AutomationExecutionStatus,
    val triggeredAt: String,
    val completedAt: String?,
    val actionsExecuted: Int,
    val actionsFailed: Int,
    val errorMessage: String?,
    val results: List<AutomationActionResultData>
)

data class AutomationActionResultData(
    val actionType: String,
    val success: Boolean,
    val message: String?,
    val executedAt: String
)

data class AutomationTestResultData(
    val wouldTrigger: Boolean,
    val conditionsMatched: Boolean,
    val actions: List<String>,
    val warnings: List<String>
)

data class BulkAutomationResultData(
    val total: Int,
    val successful: Int,
    val failed: Int,
    val skipped: Int,
    val errors: List<String>
)


/*
 * =============================================================
 * RENT
 * =============================================================
 */

data class RentReminderConfiguration(
    val ownerId: String,
    val daysBeforeDue: Int,
    val reminderMessage: String,
    val channels: List<MessageChannel>,
    val repeatIfUnpaid: Boolean,
    val repeatAfterDays: Int?
)

data class ArrearsReminderConfiguration(
    val ownerId: String,
    val minimumAmount: Double,
    val daysOverdue: Int,
    val message: String,
    val channels: List<MessageChannel>,
    val escalationLevel: Int
)

data class PaymentReceiptConfiguration(
    val ownerId: String,
    val channels: List<MessageChannel>,
    val includeReceipt: Boolean
)

data class PaymentFailureConfiguration(
    val ownerId: String,
    val message: String,
    val channels: List<MessageChannel>
)


/*
 * =============================================================
 * BILL
 * =============================================================
 */

data class BillReminderConfiguration(
    val ownerId: String,
    val daysBeforeDue: Int,
    val message: String,
    val channels: List<MessageChannel>
)

data class BillOverdueConfiguration(
    val ownerId: String,
    val daysOverdue: Int,
    val message: String,
    val channels: List<MessageChannel>
)


/*
 * =============================================================
 * LEASE
 * =============================================================
 */

data class LeaseExpiryConfiguration(
    val ownerId: String,
    val daysBeforeExpiry: List<Int>,
    val message: String,
    val channels: List<MessageChannel>
)

data class LeaseRenewalConfiguration(
    val ownerId: String,
    val daysBeforeExpiry: Int,
    val message: String,
    val channels: List<MessageChannel>
)


/*
 * =============================================================
 * AGREEMENTS
 * =============================================================
 */

data class AgreementReminderConfiguration(
    val ownerId: String,
    val daysAfterCreation: Int,
    val message: String,
    val channels: List<MessageChannel>
)

data class AgreementExpiryConfiguration(
    val ownerId: String,
    val daysBeforeExpiry: Int,
    val message: String,
    val channels: List<MessageChannel>
)


/*
 * =============================================================
 * MAINTENANCE
 * =============================================================
 */

data class MaintenanceAssignmentConfiguration(
    val ownerId: String,
    val jobTypes: List<String>,
    val preferredTechnicianIds: List<String>,
    val autoAssign: Boolean
)

data class MaintenanceFollowUpConfiguration(
    val ownerId: String,
    val daysAfterCompletion: Int,
    val message: String,
    val channels: List<MessageChannel>
)

data class MaintenanceEscalationConfiguration(
    val ownerId: String,
    val hoursWithoutProgress: Int,
    val escalationLevel: Int,
    val notifyRoles: List<String>
)


/*
 * =============================================================
 * PROPERTY
 * =============================================================
 */

data class VacancyAdvertisingConfiguration(
    val ownerId: String,
    val daysAfterVacancy: Int,
    val channels: List<String>,
    val autoPublish: Boolean,
    val listingDurationDays: Int
)

data class PropertyOnboardingConfiguration(
    val ownerId: String,
    val createTasks: Boolean,
    val notifyStaff: Boolean,
    val requiredDocuments: List<String>
)


/*
 * =============================================================
 * TENANT
 * =============================================================
 */

data class TenantOnboardingConfiguration(
    val ownerId: String,
    val sendWelcomeMessage: Boolean,
    val createTenantTasks: Boolean,
    val sendAgreement: Boolean,
    val sendHouseRules: Boolean,
    val sendPaymentInstructions: Boolean
)

data class TenantWelcomeConfiguration(
    val ownerId: String,
    val message: String,
    val channels: List<MessageChannel>
)

data class TenantFollowUpConfiguration(
    val ownerId: String,
    val daysAfterMoveIn: Int,
    val message: String,
    val channels: List<MessageChannel>
)


/*
 * =============================================================
 * VIEWINGS
 * =============================================================
 */

data class ViewingReminderConfiguration(
    val ownerId: String,
    val minutesBeforeViewing: Int,
    val channels: List<MessageChannel>
)

data class ViewingFollowUpConfiguration(
    val ownerId: String,
    val hoursAfterViewing: Int,
    val message: String,
    val channels: List<MessageChannel>
)


/*
 * =============================================================
 * APPLICATIONS
 * =============================================================
 */

data class ApplicationFollowUpConfiguration(
    val ownerId: String,
    val hoursAfterApplication: Int,
    val message: String,
    val channels: List<MessageChannel>
)

data class ApplicationStatusConfiguration(
    val ownerId: String,
    val status: String,
    val message: String,
    val channels: List<MessageChannel>
)


/*
 * =============================================================
 * SCHEDULED MESSAGE
 * =============================================================
 */



/*
 * =============================================================
 * STAFF
 * =============================================================
 */

data class StaffAssignmentConfiguration(
    val ownerId: String,
    val taskType: String,
    val staffRole: String,
    val assignmentStrategy: AssignmentStrategy
)

data class StaffEscalationConfiguration(
    val ownerId: String,
    val taskType: String,
    val hoursBeforeEscalation: Int,
    val escalationRole: String
)


/*
 * =============================================================
 * REPORT
 * =============================================================
 */

data class ScheduledReportAutomationConfiguration(
    val ownerId: String,
    val reportType: String,
    val frequency: AutomationFrequency,
    val format: String,
    val recipients: List<String>
)


/*
 * =============================================================
 * COMPLIANCE
 * =============================================================
 */

data class ComplianceReminderConfiguration(
    val ownerId: String,
    val complianceType: String,
    val daysBeforeDue: Int,
    val message: String,
    val channels: List<MessageChannel>
)


/*
 * =============================================================
 * STATISTICS
 * =============================================================
 */

data class AutomationStatisticsData(
    val totalRules: Int,
    val activeRules: Int,
    val executionsToday: Int,
    val successfulExecutions: Int,
    val failedExecutions: Int,
    val notificationsSent: Int,
    val messagesSent: Int,
    val emailsSent: Int,
    val tasksCreated: Int,
    val reportsGenerated: Int,
    val estimatedHoursSaved: Double
)


/*
 * =============================================================
 * LOG
 * =============================================================
 */

data class AutomationLogData(
    val id: String,
    val ruleId: String?,
    val ruleName: String?,
    val event: String,
    val status: AutomationExecutionStatus,
    val message: String?,
    val timestamp: String
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class AutomationEvent {

    RENT_DUE,

    RENT_PAID,

    RENT_OVERDUE,

    PAYMENT_FAILED,

    BILL_DUE,

    BILL_OVERDUE,

    LEASE_CREATED,

    LEASE_EXPIRING,

    LEASE_EXPIRED,

    AGREEMENT_CREATED,

    AGREEMENT_UNSIGNED,

    AGREEMENT_EXPIRING,

    TENANT_CREATED,

    TENANT_MOVED_IN,

    PROPERTY_CREATED,

    PROPERTY_VACANT,

    MAINTENANCE_CREATED,

    MAINTENANCE_ASSIGNED,

    MAINTENANCE_COMPLETED,

    MAINTENANCE_OVERDUE,

    APPLICATION_CREATED,

    APPLICATION_UPDATED,

    VIEWING_CREATED,

    VIEWING_COMPLETED,

    TASK_CREATED,

    TASK_OVERDUE,

    DOCUMENT_EXPIRING,

    INSURANCE_EXPIRING,

    TAX_DUE,

    COMPLIANCE_DUE,

    MANUAL
}


enum class AutomationMetric {

    RENT_ARREARS,

    OCCUPANCY_RATE,

    COLLECTION_RATE,

    MAINTENANCE_COST,

    VACANT_UNITS,

    OPEN_MAINTENANCE,

    PENDING_APPLICATIONS,

    UNREAD_MESSAGES,

    TASK_BACKLOG,

    PORTFOLIO_VALUE,

    MONTHLY_INCOME,

    MONTHLY_EXPENSES
}


enum class AutomationOperator {

    EQUALS,

    NOT_EQUALS,

    GREATER_THAN,

    GREATER_THAN_OR_EQUAL,

    LESS_THAN,

    LESS_THAN_OR_EQUAL,

    CONTAINS,

    NOT_CONTAINS
}


enum class AutomationFrequency {

    HOURLY,

    DAILY,

    WEEKLY,

    MONTHLY,

    QUARTERLY,

    YEARLY
}


enum class AutomationExecutionStatus {

    RUNNING,

    SUCCESS,

    PARTIAL,

    FAILED,

    SKIPPED
}


enum class RecipientType {

    TENANT,

    LANDLORD,

    PROPERTY_MANAGER,

    CARETAKER,

    BROKER,

    TECHNICIAN,

    ACCOUNTANT,

    STAFF,

    OWNER,

    APPLICANT,

    VIEWER,

    ORGANIZATION_ADMIN,

    CUSTOM
}


enum class MessageChannel {

    IN_APP,

    PUSH,

    EMAIL,

    SMS,

    WHATSAPP
}


enum class AssignmentStrategy {

    ROUND_ROBIN,

    LEAST_BUSY,

    NEAREST,

    SPECIALIZATION,

    MANUAL,

    HIGHEST_RATING
}