package com.him.landlordtenant.app.usecase.unit

import com.him.landlordtenant.app.interfaces.PropertyRepository
import com.him.landlordtenant.app.interfaces.PropertyUnitData

class GetUnitsUseCase(
    private val propertyRepository: PropertyRepository
) {
    suspend operator fun invoke(propertyId: String): Result<List<PropertyUnitData>> {
        return try {
            propertyRepository.getUnits(propertyId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
