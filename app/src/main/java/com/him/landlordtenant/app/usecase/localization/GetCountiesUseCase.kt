package com.him.landlordtenant.app.usecase.localization

import com.him.landlordtenant.app.interfaces.CountyData
import com.him.landlordtenant.app.interfaces.LocalizationRepository

class GetCountiesUseCase(
    private val localizationRepository: LocalizationRepository
) {
    suspend operator fun invoke(): Result<List<CountyData>> {
        return try {
            localizationRepository.getCounties()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
