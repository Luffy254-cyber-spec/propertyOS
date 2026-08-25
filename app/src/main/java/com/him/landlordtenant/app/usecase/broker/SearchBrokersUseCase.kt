package com.him.landlordtenant.app.usecase.broker

import com.him.landlordtenant.app.interfaces.ProfessionalSearchFilters
import com.him.landlordtenant.app.interfaces.SearchProfessionalData
import com.him.landlordtenant.app.interfaces.SearchRepository

class SearchBrokersUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(
        query: String,
        filters: ProfessionalSearchFilters = ProfessionalSearchFilters()
    ): Result<List<SearchProfessionalData>> {
        return try {
            searchRepository.searchBrokers(query, filters)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
