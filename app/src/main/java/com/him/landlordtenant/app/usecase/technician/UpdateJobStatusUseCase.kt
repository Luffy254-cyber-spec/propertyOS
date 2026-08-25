package com.him.landlordtenant.app.usecase.technician

import com.him.landlordtenant.app.interfaces.ProfessionalRepository

class UpdateJobStatusUseCase(
    private val professionalRepository: ProfessionalRepository
) {
    suspend fun acceptJob(technicianId: String, jobId: String): Result<Unit> {
        return try {
            professionalRepository.acceptJob(technicianId, jobId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun startJob(technicianId: String, jobId: String): Result<Unit> {
        return try {
            professionalRepository.startJob(technicianId, jobId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun completeJob(technicianId: String, jobId: String): Result<Unit> {
        return try {
            professionalRepository.completeJob(technicianId, jobId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
