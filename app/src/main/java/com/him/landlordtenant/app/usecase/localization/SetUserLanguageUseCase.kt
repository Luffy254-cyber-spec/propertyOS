package com.him.landlordtenant.app.usecase.localization

import com.him.landlordtenant.app.interfaces.LocalizationRepository

class SetUserLanguageUseCase(
    private val localizationRepository: LocalizationRepository
) {
    suspend operator fun invoke(userId: String, languageCode: String): Result<Unit> {
        return try {
            localizationRepository.setUserLanguage(userId, languageCode)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
