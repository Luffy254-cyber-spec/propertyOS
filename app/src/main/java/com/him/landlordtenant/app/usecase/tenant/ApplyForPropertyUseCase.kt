package com.him.landlordtenant.app.usecase.tenant

import com.him.landlordtenant.app.interfaces.TenantRepository

class ApplyForPropertyUseCase(
    private val tenantRepository: TenantRepository
) {
    suspend operator fun invoke(
        tenantId: String,
        propertyId: String
    ): Result<String> {
        // Implementation might involve more than just a repo call, 
        // like verifying profile completeness first.
        return Result.success("Application submitted") 
    }
}
