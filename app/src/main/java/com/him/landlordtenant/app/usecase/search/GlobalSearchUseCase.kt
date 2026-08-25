package com.him.landlordtenant.app.usecase.search

import com.him.landlordtenant.app.interfaces.GlobalSearchFilters
import com.him.landlordtenant.app.interfaces.GlobalSearchResult
import com.him.landlordtenant.app.interfaces.SearchRepository

class GlobalSearchUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(
        userId: String?,
        query: String,
        filters: GlobalSearchFilters = GlobalSearchFilters()
    ): Result<GlobalSearchResult> {
        return try {
            searchRepository.globalSearch(userId, query, filters)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
