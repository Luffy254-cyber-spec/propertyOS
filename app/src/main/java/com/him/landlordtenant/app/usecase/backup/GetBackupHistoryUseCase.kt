package com.him.landlordtenant.app.usecase.backup

import com.him.landlordtenant.app.interfaces.BackupData
import com.him.landlordtenant.app.interfaces.BackupRepository
import com.him.landlordtenant.app.interfaces.BackupScope

class GetBackupHistoryUseCase(
    private val backupRepository: BackupRepository
) {
    suspend operator fun invoke(
        scope: BackupScope,
        limit: Int = 50
    ): Result<List<BackupData>> {
        return try {
            backupRepository.getBackups(scope, limit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
