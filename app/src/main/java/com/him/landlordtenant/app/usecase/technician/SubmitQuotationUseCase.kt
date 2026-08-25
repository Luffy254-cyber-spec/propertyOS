package com.him.landlordtenant.app.usecase.technician

import com.him.landlordtenant.app.interfaces.MaintenanceQuotationData
import com.him.landlordtenant.app.interfaces.MaintenanceRepository

class SubmitQuotationUseCase(
    private val maintenanceRepository: MaintenanceRepository
) {
    suspend operator fun invoke(
        technicianId: String,
        requestId: String,
        quotation: MaintenanceQuotationData
    ): Result<String> {
        return try {
            maintenanceRepository.submitQuotation(technicianId, requestId, quotation)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
