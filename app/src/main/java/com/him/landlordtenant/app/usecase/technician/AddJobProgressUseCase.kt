package com.him.landlordtenant.app.usecase.technician

import com.him.landlordtenant.app.interfaces.MaintenanceProgressData
import com.him.landlordtenant.app.interfaces.MaintenanceRepository

class AddJobProgressUseCase(
    private val maintenanceRepository: MaintenanceRepository
) {
    suspend operator fun invoke(
        technicianId: String,
        requestId: String,
        progressUpdate: MaintenanceProgressData
    ): Result<String> {
        return try {
            maintenanceRepository.addProgressUpdate(technicianId, requestId, progressUpdate)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
