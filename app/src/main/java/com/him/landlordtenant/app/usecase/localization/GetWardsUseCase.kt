package com.him.landlordtenant.app.usecase.localization

import com.him.landlordtenant.app.interfaces.LocalizationRepository
import com.him.landlordtenant.app.interfaces.WardData

class GetWardsUseCase(
    private val localizationRepository: LocalizationRepository
) {
    suspend operator fun invoke(subCountyId: String): Result<List<WardData>> {
        return try {
            localizationRepository.getWards(subCountyId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
