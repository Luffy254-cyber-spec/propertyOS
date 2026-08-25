package com.him.landlordtenant.app.usecase.property

import com.him.landlordtenant.app.interfaces.PropertyListingData
import com.him.landlordtenant.app.interfaces.PropertyRepository

class SearchPropertiesUseCase(
    private val propertyRepository: PropertyRepository
) {
    suspend operator fun invoke(
        query: String,
        page: Int = 1,
        pageSize: Int = 20
    ): Result<List<PropertyListingData>> {
        if (query.isBlank()) {
            return propertyRepository.getAvailableProperties(page, pageSize)
        }
        return try {
            propertyRepository.searchProperties(query, page, pageSize)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
