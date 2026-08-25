package com.him.landlordtenant.app.usecase.search

import com.him.landlordtenant.app.interfaces.PropertySearchFilters
import com.him.landlordtenant.app.interfaces.SearchPropertyData
import com.him.landlordtenant.app.interfaces.SearchRepository

class SearchPropertiesWithFiltersUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(
        query: String,
        filters: PropertySearchFilters = PropertySearchFilters()
    ): Result<List<SearchPropertyData>> {
        return try {
            searchRepository.searchProperties(query, filters)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
