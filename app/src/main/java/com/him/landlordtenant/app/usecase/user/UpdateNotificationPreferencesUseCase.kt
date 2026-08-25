package com.him.landlordtenant.app.usecase.user

import com.him.landlordtenant.app.interfaces.UserRepository

/**
 * Controls which categories of notifications a user receives.
 *
 * Important:
 * Critical security, payment, legal, and emergency notifications
 * should not necessarily be disabled just because a user disables
 * ordinary notifications.
 */
class UpdateNotificationPreferencesUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        userId: String,
        pushEnabled: Boolean? = null,
        emailEnabled: Boolean? = null,
        smsEnabled: Boolean? = null,
        whatsappEnabled: Boolean? = null,

        rentReminders: Boolean? = null,
        paymentReceipts: Boolean? = null,
        arrearsAlerts: Boolean? = null,
        billReminders: Boolean? = null,

        maintenanceUpdates: Boolean? = null,
        agreementUpdates: Boolean? = null,
        propertyUpdates: Boolean? = null,
        viewingReminders: Boolean? = null,

        messageNotifications: Boolean? = null,
        vacancyAlerts: Boolean? = null,
        securityAlerts: Boolean? = null,
        marketingNotifications: Boolean? = null
    ): Result<Unit> {

        val cleanUserId = userId.trim()

        if (cleanUserId.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "User ID is required."
                )
            )
        }

        /*
         * Do not allow a request that attempts to disable every
         * delivery channel when important notifications are still
         * required by the application's security/business rules.
         *
         * The backend remains responsible for enforcing mandatory
         * notification policies.
         */

        return try {
            userRepository.updateNotificationPreferences(
                userId = cleanUserId,

                pushEnabled = pushEnabled,
                emailEnabled = emailEnabled,
                smsEnabled = smsEnabled,
                whatsappEnabled = whatsappEnabled,

                rentReminders = rentReminders,
                paymentReceipts = paymentReceipts,
                arrearsAlerts = arrearsAlerts,
                billReminders = billReminders,

                maintenanceUpdates = maintenanceUpdates,
                agreementUpdates = agreementUpdates,
                propertyUpdates = propertyUpdates,
                viewingReminders = viewingReminders,

                messageNotifications = messageNotifications,
                vacancyAlerts = vacancyAlerts,
                securityAlerts = securityAlerts,
                marketingNotifications = marketingNotifications
            )
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}