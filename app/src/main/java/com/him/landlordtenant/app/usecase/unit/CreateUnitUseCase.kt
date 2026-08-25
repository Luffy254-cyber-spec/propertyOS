package com.him.landlordtenant.app.usecase.unit

import com.him.landlordtenant.app.interfaces.PropertyRepository
import com.him.landlordtenant.app.interfaces.UnitCreateData

class CreateUnitUseCase(
    private val propertyRepository: PropertyRepository
) {
    suspend operator fun invoke(
        ownerId: String,
        propertyId: String,
        unit: UnitCreateData
    ): Result<String> {
        return try {
            propertyRepository.createUnit(ownerId, propertyId, unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
