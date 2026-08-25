package com.him.landlordtenant.app.usecase.backup

import com.him.landlordtenant.app.interfaces.BackupRepository
import com.him.landlordtenant.app.interfaces.RestoreRequest

class RestoreDataUseCase(
    private val backupRepository: BackupRepository
) {
    suspend operator fun invoke(
        actorId: String,
        request: RestoreRequest
    ): Result<String> {
        return try {
            backupRepository.restore(actorId, request)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
