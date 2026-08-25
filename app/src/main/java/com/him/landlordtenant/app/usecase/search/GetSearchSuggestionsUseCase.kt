package com.him.landlordtenant.app.usecase.search

import com.him.landlordtenant.app.interfaces.SearchRepository
import com.him.landlordtenant.app.interfaces.SearchSuggestionData
import com.him.landlordtenant.app.interfaces.SearchSuggestionType

class GetSearchSuggestionsUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(
        query: String,
        type: SearchSuggestionType? = null,
        limit: Int = 10
    ): Result<List<SearchSuggestionData>> {
        if (query.length < 2) return Result.success(emptyList())
        return try {
            searchRepository.getSearchSuggestions(query, type, limit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
