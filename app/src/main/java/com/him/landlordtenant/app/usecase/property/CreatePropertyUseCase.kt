package com.him.landlordtenant.app.usecase.property

import com.him.landlordtenant.app.interfaces.PropertyCreateData
import com.him.landlordtenant.app.interfaces.PropertyRepository

class CreatePropertyUseCase(
    private val propertyRepository: PropertyRepository
) {
    suspend operator fun invoke(
        ownerId: String,
        propertyData: PropertyCreateData
    ): Result<String> {
        return try {
            propertyRepository.createProperty(ownerId, propertyData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
