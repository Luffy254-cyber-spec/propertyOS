package com.him.landlordtenant.app.usecase.technician

import com.him.landlordtenant.app.interfaces.ProfessionalJobData
import com.him.landlordtenant.app.interfaces.ProfessionalRepository

class GetTechnicianJobsUseCase(
    private val professionalRepository: ProfessionalRepository
) {
    suspend operator fun invoke(technicianId: String): Result<List<ProfessionalJobData>> {
        return try {
            professionalRepository.getProfessionalJobs(technicianId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
