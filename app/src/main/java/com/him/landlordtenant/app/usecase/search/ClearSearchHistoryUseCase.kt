package com.him.landlordtenant.app.usecase.search

import com.him.landlordtenant.app.interfaces.SearchRepository

class ClearSearchHistoryUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(userId: String): Result<Unit> {
        return try {
            searchRepository.clearSearchHistory(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
