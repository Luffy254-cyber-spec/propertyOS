package com.him.landlordtenant.app.usecase.user

import com.him.landlordtenant.app.interfaces.UserDataExportData
import com.him.landlordtenant.app.interfaces.UserRepository

/**
 * Requests an export of the user's personal/application data.
 *
 * The repository/backend is responsible for collecting the
 * appropriate data and generating the export.
 */
class ExportUserDataUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        userId: String
    ): Result<UserDataExportData> {

        val cleanUserId = userId.trim()

        if (cleanUserId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "User ID is required."
                )
            )
        }

        return try {
            userRepository.exportUserData(
                userId = cleanUserId
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}