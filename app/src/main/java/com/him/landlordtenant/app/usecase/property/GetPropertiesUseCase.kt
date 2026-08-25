package com.him.landlordtenant.app.usecase.property

import com.him.landlordtenant.app.interfaces.PropertyListingData
import com.him.landlordtenant.app.interfaces.PropertyRepository

/**
 * Retrieves a paginated list of properties.
 */
class GetPropertiesUseCase(
    private val propertyRepository: PropertyRepository
) {

    suspend operator fun invoke(
        page: Int = 1,
        pageSize: Int = 20
    ): Result<List<PropertyListingData>> {

        if (page < 1) {
            return Result.failure(IllegalArgumentException("Page must be greater than zero."))
        }

        return try {
            propertyRepository.getAvailableProperties(
                page = page,
                pageSize = pageSize
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}
