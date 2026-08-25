package com.him.landlordtenant.app.usecase.maintenance

import com.him.landlordtenant.app.interfaces.MaintenanceRepository

class AssignTechnicianUseCase(
    private val maintenanceRepository: MaintenanceRepository
) {
    suspend operator fun invoke(
        landlordId: String,
        requestId: String,
        technicianId: String
    ): Result<Unit> {
        return try {
            maintenanceRepository.assignProfessional(landlordId, requestId, technicianId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
