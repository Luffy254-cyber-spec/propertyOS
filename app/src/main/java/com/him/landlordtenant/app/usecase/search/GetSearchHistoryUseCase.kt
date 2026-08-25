package com.him.landlordtenant.app.usecase.search

import com.him.landlordtenant.app.interfaces.SearchHistoryData
import com.him.landlordtenant.app.interfaces.SearchRepository

class GetSearchHistoryUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(userId: String, limit: Int = 20): Result<List<SearchHistoryData>> {
        return try {
            searchRepository.getSearchHistory(userId, limit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
