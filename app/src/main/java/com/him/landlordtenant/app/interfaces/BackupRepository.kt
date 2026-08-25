package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * BACKUP REPOSITORY
 * =============================================================
 *
 * Handles application data protection and disaster recovery.
 *
 * Supports:
 *
 * - Manual backups
 * - Scheduled backups
 * - Backup history
 * - Backup verification
 * - Restore points
 * - Data restoration
 * - Backup retention
 * - Cloud/local backup metadata
 * - Export/import
 * - Disaster recovery
 * - Backup health monitoring
 *
 * IMPORTANT:
 *
 * This repository manages backup metadata and recovery operations.
 * Actual encryption, storage credentials and infrastructure secrets
 * must remain outside the domain layer.
 *
 * =============================================================
 */

interface BackupRepository {

    /*
     * ---------------------------------------------------------
     * BACKUP DASHBOARD
     * ---------------------------------------------------------
     */

    suspend fun getBackupDashboard(
        scope: BackupScope
    ): Result<BackupDashboardData>

    fun observeBackupDashboard(
        scope: BackupScope
    ): Flow<Result<BackupDashboardData>>


    /*
     * ---------------------------------------------------------
     * CREATE BACKUP
     * ---------------------------------------------------------
     */

    suspend fun createBackup(
        actorId: String,
        request: CreateBackupRequest
    ): Result<String>

    suspend fun createFullBackup(
        actorId: String,
        scope: BackupScope
    ): Result<String>

    suspend fun createIncrementalBackup(
        actorId: String,
        scope: BackupScope
    ): Result<String>

    suspend fun createDifferentialBackup(
        actorId: String,
        scope: BackupScope
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * BACKUP HISTORY
     * ---------------------------------------------------------
     */

    suspend fun getBackups(
        scope: BackupScope,
        limit: Int = 50
    ): Result<List<BackupData>>

    fun observeBackups(
        scope: BackupScope
    ): Flow<Result<List<BackupData>>>

    suspend fun getBackup(
        backupId: String
    ): Result<BackupData>

    suspend fun deleteBackup(
        actorId: String,
        backupId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * BACKUP STATUS
     * ---------------------------------------------------------
     */

    suspend fun getBackupStatus(
        backupId: String
    ): Result<BackupStatusData>

    suspend fun retryBackup(
        actorId: String,
        backupId: String
    ): Result<Unit>

    suspend fun cancelBackup(
        actorId: String,
        backupId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * VERIFICATION
     * ---------------------------------------------------------
     */

    suspend fun verifyBackup(
        actorId: String,
        backupId: String
    ): Result<BackupVerificationData>

    suspend fun verifyLatestBackup(
        actorId: String,
        scope: BackupScope
    ): Result<BackupVerificationData>

    suspend fun getVerificationHistory(
        scope: BackupScope,
        limit: Int = 50
    ): Result<List<BackupVerificationData>>


    /*
     * ---------------------------------------------------------
     * RESTORE POINTS
     * ---------------------------------------------------------
     */

    suspend fun createRestorePoint(
        actorId: String,
        request: CreateRestorePointRequest
    ): Result<String>

    suspend fun getRestorePoints(
        scope: BackupScope,
        limit: Int = 50
    ): Result<List<RestorePointData>>

    suspend fun getRestorePoint(
        restorePointId: String
    ): Result<RestorePointData>

    suspend fun deleteRestorePoint(
        actorId: String,
        restorePointId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * RESTORATION
     * ---------------------------------------------------------
     */

    suspend fun previewRestore(
        actorId: String,
        restorePointId: String
    ): Result<RestorePreviewData>

    suspend fun restore(
        actorId: String,
        request: RestoreRequest
    ): Result<String>

    suspend fun cancelRestore(
        actorId: String,
        restoreJobId: String
    ): Result<Unit>

    suspend fun getRestoreStatus(
        restoreJobId: String
    ): Result<RestoreStatusData>

    suspend fun getRestoreHistory(
        scope: BackupScope,
        limit: Int = 50
    ): Result<List<RestoreHistoryData>>


    /*
     * ---------------------------------------------------------
     * SCHEDULES
     * ---------------------------------------------------------
     */

    suspend fun createBackupSchedule(
        actorId: String,
        schedule: CreateBackupScheduleData
    ): Result<String>

    suspend fun getBackupSchedules(
        scope: BackupScope
    ): Result<List<BackupScheduleData>>

    suspend fun getBackupSchedule(
        scheduleId: String
    ): Result<BackupScheduleData>

    suspend fun updateBackupSchedule(
        actorId: String,
        scheduleId: String,
        update: UpdateBackupScheduleData
    ): Result<Unit>

    suspend fun deleteBackupSchedule(
        actorId: String,
        scheduleId: String
    ): Result<Unit>

    suspend fun enableBackupSchedule(
        actorId: String,
        scheduleId: String
    ): Result<Unit>

    suspend fun disableBackupSchedule(
        actorId: String,
        scheduleId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * RETENTION
     * ---------------------------------------------------------
     */

    suspend fun getRetentionPolicy(
        scope: BackupScope
    ): Result<BackupRetentionPolicyData>

    suspend fun updateRetentionPolicy(
        actorId: String,
        scope: BackupScope,
        policy: UpdateBackupRetentionPolicyData
    ): Result<Unit>

    suspend fun cleanupExpiredBackups(
        actorId: String,
        scope: BackupScope
    ): Result<BackupCleanupResultData>


    /*
     * ---------------------------------------------------------
     * STORAGE
     * ---------------------------------------------------------
     */

    suspend fun getStorageStatus(
        scope: BackupScope
    ): Result<BackupStorageStatusData>

    suspend fun getStorageProviders(
        scope: BackupScope
    ): Result<List<BackupStorageProviderData>>

    suspend fun setPrimaryStorageProvider(
        actorId: String,
        scope: BackupScope,
        providerId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * EXPORT
     * ---------------------------------------------------------
     */

    suspend fun exportData(
        actorId: String,
        request: DataExportRequest
    ): Result<String>

    suspend fun getExports(
        scope: BackupScope,
        limit: Int = 50
    ): Result<List<DataExportData>>

    suspend fun getExportStatus(
        exportId: String
    ): Result<DataExportStatusData>

    suspend fun cancelExport(
        actorId: String,
        exportId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * IMPORT
     * ---------------------------------------------------------
     */

    suspend fun validateImport(
        actorId: String,
        request: DataImportRequest
    ): Result<ImportValidationData>

    suspend fun importData(
        actorId: String,
        request: DataImportRequest
    ): Result<String>

    suspend fun getImportStatus(
        importId: String
    ): Result<DataImportStatusData>

    suspend fun cancelImport(
        actorId: String,
        importId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * DISASTER RECOVERY
     * ---------------------------------------------------------
     */

    suspend fun getDisasterRecoveryStatus(
        scope: BackupScope
    ): Result<DisasterRecoveryStatusData>

    suspend fun runRecoveryTest(
        actorId: String,
        scope: BackupScope
    ): Result<RecoveryTestResultData>

    suspend fun getRecoveryTests(
        scope: BackupScope,
        limit: Int = 20
    ): Result<List<RecoveryTestResultData>>


    /*
     * ---------------------------------------------------------
     * HEALTH
     * ---------------------------------------------------------
     */

    suspend fun getBackupHealth(
        scope: BackupScope
    ): Result<BackupHealthData>

    suspend fun refreshBackupHealth(
        actorId: String,
        scope: BackupScope
    ): Result<Unit>
}


/*
 * =============================================================
 * SCOPE
 * =============================================================
 */

data class BackupDashboardData(
    val summary: BackupHealthData,
    val recentBackups: List<BackupData>,
    val schedules: List<BackupScheduleData>,
    val storage: BackupStorageStatusData
)

data class BackupScope(
    val organizationId: String? = null,
    val userId: String? = null,
    val portfolioId: String? = null,
    val propertyId: String? = null
)


/*
 * =============================================================
 * CREATE BACKUP
 * =============================================================
 */

data class CreateBackupRequest(
    val scope: BackupScope,
    val type: BackupType = BackupType.FULL,
    val storageProviderId: String? = null,
    val encrypt: Boolean = true,
    val description: String? = null
)


/*
 * =============================================================
 * BACKUP
 * =============================================================
 */

data class BackupData(
    val id: String,
    val scope: BackupScope,
    val type: BackupType,
    val status: BackupStatus,
    val sizeBytes: Long,
    val recordCount: Long,
    val storageProviderId: String?,
    val storageLocation: String?,
    val checksum: String?,
    val encrypted: Boolean,
    val createdBy: String?,
    val createdAt: String,
    val completedAt: String?,
    val expiresAt: String?,
    val verified: Boolean
)


/*
 * =============================================================
 * STATUS
 * =============================================================
 */

data class BackupStatusData(
    val backupId: String,
    val status: BackupStatus,
    val progressPercentage: Double,
    val processedRecords: Long,
    val totalRecords: Long,
    val sizeBytes: Long,
    val errorMessage: String?,
    val startedAt: String?,
    val completedAt: String?
)


/*
 * =============================================================
 * VERIFICATION
 * =============================================================
 */

data class BackupVerificationData(
    val id: String,
    val backupId: String,
    val status: BackupVerificationStatus,
    val checksumValid: Boolean,
    val dataReadable: Boolean,
    val recordsVerified: Long,
    val recordsFailed: Long,
    val testedAt: String,
    val notes: String?
)


/*
 * =============================================================
 * RESTORE POINT
 * =============================================================
 */

data class CreateRestorePointRequest(
    val scope: BackupScope,
    val backupId: String?,
    val name: String,
    val description: String?,
    val expiresAt: String?
)

data class RestorePointData(
    val id: String,
    val scope: BackupScope,
    val backupId: String?,
    val name: String,
    val description: String?,
    val createdAt: String,
    val expiresAt: String?,
    val recordCount: Long
)


/*
 * =============================================================
 * RESTORE
 * =============================================================
 */

data class RestoreRequest(
    val restorePointId: String,
    val targetScope: BackupScope,
    val mode: RestoreMode,
    val createSafetyBackup: Boolean = true,
    val selectedCollections: List<String> = emptyList(),
    val dryRun: Boolean = false
)

data class RestorePreviewData(
    val restorePointId: String,
    val affectedCollections: List<String>,
    val recordsToCreate: Long,
    val recordsToUpdate: Long,
    val recordsToDelete: Long,
    val conflicts: List<RestoreConflictData>,
    val warnings: List<String>
)

data class RestoreConflictData(
    val collection: String,
    val recordId: String,
    val reason: String,
    val resolution: String?
)

data class RestoreStatusData(
    val restoreJobId: String,
    val status: RestoreStatus,
    val progressPercentage: Double,
    val processedRecords: Long,
    val totalRecords: Long,
    val errorMessage: String?,
    val startedAt: String?,
    val completedAt: String?
)

data class RestoreHistoryData(
    val id: String,
    val restorePointId: String,
    val initiatedBy: String,
    val mode: RestoreMode,
    val status: RestoreStatus,
    val recordsAffected: Long,
    val createdAt: String,
    val completedAt: String?
)


/*
 * =============================================================
 * SCHEDULE
 * =============================================================
 */

data class CreateBackupScheduleData(
    val scope: BackupScope,
    val name: String,
    val frequency: BackupFrequency,
    val backupType: BackupType,
    val timeOfDay: String,
    val dayOfWeek: Int?,
    val dayOfMonth: Int?,
    val retentionDays: Int,
    val storageProviderId: String?,
    val enabled: Boolean = true
)

data class UpdateBackupScheduleData(
    val name: String?,
    val frequency: BackupFrequency?,
    val backupType: BackupType?,
    val timeOfDay: String?,
    val dayOfWeek: Int?,
    val dayOfMonth: Int?,
    val retentionDays: Int?,
    val storageProviderId: String?,
    val enabled: Boolean?
)

data class BackupScheduleData(
    val id: String,
    val scope: BackupScope,
    val name: String,
    val frequency: BackupFrequency,
    val backupType: BackupType,
    val timeOfDay: String,
    val dayOfWeek: Int?,
    val dayOfMonth: Int?,
    val retentionDays: Int,
    val storageProviderId: String?,
    val enabled: Boolean,
    val lastRunAt: String?,
    val nextRunAt: String?,
    val lastBackupStatus: BackupStatus?
)


/*
 * =============================================================
 * RETENTION
 * =============================================================
 */

data class BackupRetentionPolicyData(
    val dailyRetentionDays: Int,
    val weeklyRetentionWeeks: Int,
    val monthlyRetentionMonths: Int,
    val yearlyRetentionYears: Int,
    val minimumBackups: Int
)

data class UpdateBackupRetentionPolicyData(
    val dailyRetentionDays: Int?,
    val weeklyRetentionWeeks: Int?,
    val monthlyRetentionMonths: Int?,
    val yearlyRetentionYears: Int?,
    val minimumBackups: Int?
)

data class BackupCleanupResultData(
    val scanned: Int,
    val deleted: Int,
    val retained: Int,
    val failed: Int
)


/*
 * =============================================================
 * STORAGE
 * =============================================================
 */

data class BackupStorageStatusData(
    val totalCapacityBytes: Long?,
    val usedCapacityBytes: Long?,
    val availableCapacityBytes: Long?,
    val backupCount: Int,
    val lastSuccessfulBackupAt: String?,
    val storageHealthy: Boolean
)

data class BackupStorageProviderData(
    val id: String,
    val name: String,
    val type: BackupStorageType,
    val enabled: Boolean,
    val primary: Boolean,
    val healthy: Boolean,
    val availableSpaceBytes: Long?
)


/*
 * =============================================================
 * EXPORT
 * =============================================================
 */

data class DataExportRequest(
    val scope: BackupScope,
    val format: DataExportFormat,
    val collections: List<String> = emptyList(),
    val includeDocuments: Boolean = false,
    val encrypt: Boolean = true
)

data class DataExportData(
    val id: String,
    val scope: BackupScope,
    val format: DataExportFormat,
    val status: DataExportStatus,
    val sizeBytes: Long?,
    val recordCount: Long?,
    val createdAt: String,
    val completedAt: String?,
    val expiresAt: String?
)

data class DataExportStatusData(
    val exportId: String,
    val status: DataExportStatus,
    val progressPercentage: Double,
    val processedRecords: Long,
    val totalRecords: Long,
    val sizeBytes: Long?,
    val errorMessage: String?
)


/*
 * =============================================================
 * IMPORT
 * =============================================================
 */

data class DataImportRequest(
    val scope: BackupScope,
    val sourceId: String,
    val format: DataExportFormat,
    val mode: ImportMode,
    val selectedCollections: List<String> = emptyList(),
    val dryRun: Boolean = false
)

data class ImportValidationData(
    val valid: Boolean,
    val totalRecords: Long,
    val validRecords: Long,
    val invalidRecords: Long,
    val conflicts: List<ImportConflictData>,
    val warnings: List<String>,
    val errors: List<String>
)

data class ImportConflictData(
    val collection: String,
    val recordId: String,
    val reason: String,
    val resolution: String?
)

data class DataImportStatusData(
    val importId: String,
    val status: DataImportStatus,
    val progressPercentage: Double,
    val processedRecords: Long,
    val totalRecords: Long,
    val importedRecords: Long,
    val skippedRecords: Long,
    val failedRecords: Long,
    val errorMessage: String?
)


/*
 * =============================================================
 * DISASTER RECOVERY
 * =============================================================
 */

data class DisasterRecoveryStatusData(
    val scope: BackupScope,
    val recoveryReady: Boolean,
    val latestBackupAt: String?,
    val latestVerifiedBackupAt: String?,
    val latestRestorePointAt: String?,
    val recoveryPointObjectiveMinutes: Int,
    val recoveryTimeObjectiveMinutes: Int,
    val estimatedRecoveryTimeMinutes: Int?,
    val issues: List<String>
)

data class RecoveryTestResultData(
    val id: String,
    val scope: BackupScope,
    val successful: Boolean,
    val backupId: String?,
    val restorePointId: String?,
    val recordsTested: Long,
    val recordsRestored: Long,
    val durationSeconds: Long,
    val issues: List<String>,
    val testedAt: String
)


/*
 * =============================================================
 * HEALTH
 * =============================================================
 */

data class BackupHealthData(
    val healthy: Boolean,
    val score: Double,
    val lastSuccessfulBackupAt: String?,
    val lastFailedBackupAt: String?,
    val daysSinceSuccessfulBackup: Int?,
    val verifiedBackups: Int,
    val unverifiedBackups: Int,
    val failedBackups: Int,
    val storageHealthy: Boolean,
    val recoveryReady: Boolean,
    val issues: List<String>
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class BackupType {
    FULL,
    INCREMENTAL,
    DIFFERENTIAL
}

enum class BackupStatus {
    QUEUED,
    RUNNING,
    COMPLETED,
    FAILED,
    CANCELLED,
    EXPIRED
}

enum class BackupVerificationStatus {
    PENDING,
    VERIFIED,
    FAILED
}

enum class RestoreMode {
    FULL_RESTORE,
    PARTIAL_RESTORE,
    MERGE,
    REPLACE
}

enum class RestoreStatus {
    QUEUED,
    RUNNING,
    COMPLETED,
    FAILED,
    CANCELLED,
    ROLLED_BACK
}

enum class BackupFrequency {
    HOURLY,
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
}

enum class BackupStorageType {
    LOCAL,
    CLOUD,
    REMOTE,
    NETWORK
}

enum class DataExportFormat {
    JSON,
    CSV,
    XLSX,
    SQL,
    ZIP
}

enum class DataExportStatus {
    QUEUED,
    RUNNING,
    COMPLETED,
    FAILED,
    CANCELLED,
    EXPIRED
}

enum class ImportMode {
    CREATE_ONLY,
    UPDATE_ONLY,
    UPSERT,
    REPLACE
}

enum class DataImportStatus {
    QUEUED,
    VALIDATING,
    RUNNING,
    COMPLETED,
    FAILED,
    CANCELLED
}