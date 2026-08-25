package com.him.landlordtenant.app.usecase.localization

import com.him.landlordtenant.app.interfaces.LocalizationRepository
import com.him.landlordtenant.app.interfaces.SubCountyData

class GetSubCountiesUseCase(
    private val localizationRepository: LocalizationRepository
) {
    suspend operator fun invoke(countyId: String): Result<List<SubCountyData>> {
        return try {
            localizationRepository.getSubCounties(countyId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
