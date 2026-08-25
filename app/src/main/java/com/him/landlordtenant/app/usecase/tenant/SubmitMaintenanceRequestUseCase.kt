package com.him.landlordtenant.app.usecase.tenant

import com.him.landlordtenant.app.interfaces.MaintenanceRequestData
import com.him.landlordtenant.app.interfaces.TenantRepository

class SubmitMaintenanceRequestUseCase(
    private val tenantRepository: TenantRepository
) {
    suspend operator fun invoke(
        tenantId: String,
        request: MaintenanceRequestData
    ): Result<String> {
        return try {
            tenantRepository.createMaintenanceRequest(tenantId, request)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
