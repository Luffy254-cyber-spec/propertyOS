package com.him.landlordtenant.app.usecase.maintenance

import com.him.landlordtenant.app.interfaces.MaintenanceRepository

class UpdateMaintenanceStatusUseCase(
    private val maintenanceRepository: MaintenanceRepository
) {
    suspend operator fun invoke(
        userId: String,
        requestId: String,
        status: String
    ): Result<Unit> {
        return try {
            maintenanceRepository.updateStatus(userId, requestId, status)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
