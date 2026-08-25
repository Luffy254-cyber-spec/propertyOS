package com.him.landlordtenant.app.usecase.maintenance

import com.him.landlordtenant.app.interfaces.MaintenanceRepository
import com.him.landlordtenant.app.interfaces.MaintenanceSummaryData

class GetLandlordMaintenanceRequestsUseCase(
    private val maintenanceRepository: MaintenanceRepository
) {
    suspend operator fun invoke(landlordId: String): Result<List<MaintenanceSummaryData>> {
        return try {
            maintenanceRepository.getLandlordRequests(landlordId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
