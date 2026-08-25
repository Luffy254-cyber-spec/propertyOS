package com.him.landlordtenant.app.usecase.auth

import com.him.landlordtenant.app.interfaces.AuthRepository

/**
 * Verifies a user's phone number using a verification code.
 *
 * Typical flow:
 *
 * Registration
 *      ↓
 * Phone number submitted
 *      ↓
 * OTP sent by backend
 *      ↓
 * User enters OTP
 *      ↓
 * VerifyPhoneUseCase
 *      ↓
 * AuthRepository
 *      ↓
 * Phone verified
 */
class VerifyPhoneUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(
        userId: String,
        verificationCode: String
    ): Result<Unit> {

        val cleanUserId = userId.trim()
        val cleanCode = verificationCode.trim()

        if (cleanUserId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "User ID is required."
                )
            )
        }

        if (cleanCode.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "Verification code is required."
                )
            )
        }

        /*
         * Most OTP systems use 4–8 digits.
         * We don't enforce exactly 6 digits here because
         * the backend may use a different OTP configuration.
         */
        if (
            cleanCode.length < 4 ||
            cleanCode.length > 8
        ) {
            return Result.failure(
                IllegalArgumentException(
                    "Invalid verification code."
                )
            )
        }

        return try {
            authRepository.verifyPhone(
                userId = cleanUserId,
                verificationCode = cleanCode
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}