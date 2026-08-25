package com.him.landlordtenant.app.usecase.unit

import com.him.landlordtenant.app.interfaces.PropertyRepository

class DeleteUnitUseCase(
    private val propertyRepository: PropertyRepository
) {
    suspend operator fun invoke(
        ownerId: String,
        propertyId: String,
        unitId: String
    ): Result<Unit> {
        return try {
            propertyRepository.deleteUnit(ownerId, propertyId, unitId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
