package com.him.landlordtenant.app.usecase.maintenance

import com.him.landlordtenant.app.interfaces.MaintenanceRepository
import com.him.landlordtenant.app.interfaces.MaintenanceSummaryData

class GetTenantMaintenanceRequestsUseCase(
    private val maintenanceRepository: MaintenanceRepository
) {
    suspend operator fun invoke(tenantId: String): Result<List<MaintenanceSummaryData>> {
        return try {
            maintenanceRepository.getTenantRequests(tenantId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
