package com.him.landlordtenant.app.usecase.landlord

import com.him.landlordtenant.app.interfaces.LandlordRepository

class InviteTenantUseCase(
    private val landlordRepository: LandlordRepository
) {
    suspend operator fun invoke(
        landlordId: String,
        tenantId: String,
        propertyId: String,
        unitId: String
    ): Result<String> {
        return try {
            landlordRepository.inviteTenant(landlordId, tenantId, propertyId, unitId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
