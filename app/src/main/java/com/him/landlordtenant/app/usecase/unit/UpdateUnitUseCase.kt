package com.him.landlordtenant.app.usecase.unit

import com.him.landlordtenant.app.interfaces.PropertyRepository
import com.him.landlordtenant.app.interfaces.UnitCreateData

class UpdateUnitUseCase(
    private val propertyRepository: PropertyRepository
) {
    suspend operator fun invoke(
        ownerId: String,
        propertyId: String,
        unitId: String,
        unit: UnitCreateData
    ): Result<Unit> {
        return try {
            propertyRepository.updateUnit(ownerId, propertyId, unitId, unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
