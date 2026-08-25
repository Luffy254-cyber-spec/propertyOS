package com.him.landlordtenant.app.usecase.property

import com.him.landlordtenant.app.interfaces.PropertyReportData
import com.him.landlordtenant.app.interfaces.PropertyRepository

/**
 * Reports a property listing for review.
 *
 * Examples:
 * - Fake listing
 * - Wrong location
 * - Scam
 * - Duplicate listing
 * - Inappropriate content
 * - Incorrect pricing
 */
class ReportPropertyUseCase(
    private val propertyRepository: PropertyRepository
) {

    suspend operator fun invoke(
        userId: String,
        propertyId: String,
        reason: String,
        description: String? = null
    ): Result<String> {

        val cleanUserId = userId.trim()
        val cleanPropertyId = propertyId.trim()
        val cleanReason = reason.trim()
        val cleanDescription = description?.trim()?.takeIf { it.isNotBlank() }

        if (cleanUserId.isBlank()) {
            return Result.failure(
                IllegalArgumentException("User ID is required.")
            )
        }

        if (cleanPropertyId.isBlank()) {
            return Result.failure(
                IllegalArgumentException("Property ID is required.")
            )
        }

        if (cleanReason.isBlank()) {
            return Result.failure(
                IllegalArgumentException("Report reason is required.")
            )
        }

        if (cleanDescription != null && cleanDescription.length > 1000) {
            return Result.failure(
                IllegalArgumentException("Description must not exceed 1000 characters.")
            )
        }

        return try {
            propertyRepository.reportProperty(
                userId = cleanUserId,
                propertyId = cleanPropertyId,
                report = PropertyReportData(
                    reason = cleanReason,
                    description = cleanDescription
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
