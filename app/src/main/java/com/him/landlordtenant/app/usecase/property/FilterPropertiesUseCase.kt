package com.him.landlordtenant.app.usecase.property

import com.him.landlordtenant.app.interfaces.PropertyFilterData
import com.him.landlordtenant.app.interfaces.PropertyListingData
import com.him.landlordtenant.app.interfaces.PropertyRepository

/**
 * Filters properties using structured search criteria.
 */
class FilterPropertiesUseCase(
    private val propertyRepository: PropertyRepository
) {

    suspend operator fun invoke(
        filter: PropertyFilterData,
        page: Int = 1,
        pageSize: Int = 20
    ): Result<List<PropertyListingData>> {

        if (page < 1) {
            return Result.failure(
                IllegalArgumentException(
                    "Page must be greater than zero."
                )
            )
        }

        if (pageSize !in 1..100) {
            return Result.failure(
                IllegalArgumentException(
                    "Page size must be between 1 and 100."
                )
            )
        }

        return try {
            propertyRepository.filterProperties(
                filter = filter,
                page = page,
                pageSize = pageSize
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}