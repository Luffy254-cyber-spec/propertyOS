package com.him.landlordtenant.app.usecase.localization

import com.him.landlordtenant.app.interfaces.LanguageData
import com.him.landlordtenant.app.interfaces.LocalizationRepository

class GetUserLanguageUseCase(
    private val localizationRepository: LocalizationRepository
) {
    suspend operator fun invoke(userId: String): Result<LanguageData> {
        return try {
            localizationRepository.getUserLanguage(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
