package com.him.landlordtenant.app.usecase.tenant

import com.him.landlordtenant.app.interfaces.TenantProfileData
import com.him.landlordtenant.app.interfaces.TenantRepository

/**
 * Creates a tenant profile.
 */
class CreateTenantProfileUseCase(
    private val tenantRepository: TenantRepository
) {

    suspend operator fun invoke(
        userId: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        email: String
    ): Result<Unit> {

        val cleanUserId = userId.trim()
        val cleanFirstName = firstName.trim()
        val cleanLastName = lastName.trim()
        val cleanPhoneNumber = phoneNumber.trim()
        val cleanEmail = email.trim().lowercase()

        if (cleanUserId.isBlank()) {
            return Result.failure(IllegalArgumentException("User ID is required."))
        }

        if (cleanFirstName.isBlank()) {
            return Result.failure(IllegalArgumentException("First name is required."))
        }

        if (cleanLastName.isBlank()) {
            return Result.failure(IllegalArgumentException("Last name is required."))
        }

        return try {
            tenantRepository.updateTenantProfile(
                tenantId = cleanUserId,
                profile = TenantProfileData(
                    id = cleanUserId,
                    fullName = "$cleanFirstName $cleanLastName",
                    email = cleanEmail,
                    phoneNumber = cleanPhoneNumber,
                    profilePhotoUrl = null,
                    nationalIdVerified = false
                )
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}