package com.him.landlordtenant.app.usecase.property

import com.him.landlordtenant.app.interfaces.PropertyRepository

/**
 * Generates the information required to share a property.
 */
class SharePropertyUseCase(
    private val propertyRepository: PropertyRepository
) {

    suspend operator fun invoke(
        propertyId: String,
        userId: String? = null
    ): Result<String> {

        val cleanPropertyId = propertyId.trim()
        val cleanUserId = userId?.trim()?.takeIf { it.isNotBlank() }

        if (cleanPropertyId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Property ID is required."
                )
            )
        }

        return try {
            if (cleanUserId != null) {
                propertyRepository.recordShare(cleanPropertyId, cleanUserId)
            }
            propertyRepository.generateShareLink(
                propertyId = cleanPropertyId
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}