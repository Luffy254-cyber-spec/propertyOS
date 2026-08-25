package com.him.landlordtenant.app.usecase.tenant

import com.him.landlordtenant.app.interfaces.TenantProfileData
import com.him.landlordtenant.app.interfaces.TenantRepository

class GetTenantProfileUseCase(
    private val tenantRepository: TenantRepository
) {
    suspend operator fun invoke(tenantId: String): Result<TenantProfileData> {
        return try {
            tenantRepository.getTenantProfile(tenantId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
