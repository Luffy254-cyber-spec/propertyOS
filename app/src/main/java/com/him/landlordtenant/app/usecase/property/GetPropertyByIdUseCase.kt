package com.him.landlordtenant.app.usecase.property

import com.him.landlordtenant.app.interfaces.PropertyDetailsData
import com.him.landlordtenant.app.interfaces.PropertyRepository

class GetPropertyByIdUseCase(
    private val propertyRepository: PropertyRepository
) {
    suspend operator fun invoke(propertyId: String): Result<PropertyDetailsData> {
        return try {
            propertyRepository.getProperty(propertyId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
