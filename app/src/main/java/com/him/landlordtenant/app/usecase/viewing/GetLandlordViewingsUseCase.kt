package com.him.landlordtenant.app.usecase.viewing

import com.him.landlordtenant.app.interfaces.ViewingSummaryData
import com.him.landlordtenant.app.interfaces.ViewingRepository

class GetLandlordViewingsUseCase(
    private val viewingRepository: ViewingRepository
) {
    suspend operator fun invoke(landlordId: String): Result<List<ViewingSummaryData>> {
        return try {
            viewingRepository.getLandlordViewings(landlordId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
