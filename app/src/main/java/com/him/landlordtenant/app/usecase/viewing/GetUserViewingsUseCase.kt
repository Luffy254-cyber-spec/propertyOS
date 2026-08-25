package com.him.landlordtenant.app.usecase.viewing

import com.him.landlordtenant.app.interfaces.ViewingSummaryData
import com.him.landlordtenant.app.interfaces.ViewingRepository

class GetUserViewingsUseCase(
    private val viewingRepository: ViewingRepository
) {
    suspend operator fun invoke(userId: String): Result<List<ViewingSummaryData>> {
        return try {
            viewingRepository.getUserViewings(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
