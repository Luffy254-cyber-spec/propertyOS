package com.him.landlordtenant.app.usecase.backup

import com.him.landlordtenant.app.interfaces.BackupRepository
import com.him.landlordtenant.app.interfaces.BackupScope
import com.him.landlordtenant.app.interfaces.CreateBackupRequest

class CreateManualBackupUseCase(
    private val backupRepository: BackupRepository
) {
    suspend operator fun invoke(
        actorId: String,
        scope: BackupScope,
        description: String? = null
    ): Result<String> {
        val request = CreateBackupRequest(
            scope = scope,
            description = description
        )
        return try {
            backupRepository.createBackup(actorId, request)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
