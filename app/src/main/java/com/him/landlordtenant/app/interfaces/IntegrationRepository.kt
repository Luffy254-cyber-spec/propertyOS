package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * INTEGRATION REPOSITORY
 * =============================================================
 *
 * Central interface for external services.
 *
 * Supports:
 *
 * - Payment gateways
 * - Mobile money
 * - Banks
 * - SMS
 * - Email
 * - WhatsApp
 * - Push notifications
 * - Google Maps
 * - Geocoding
 * - Identity verification
 * - Cloud storage
 * - Calendar services
 * - Video/image services
 * - E-signatures
 * - Analytics
 * - AI services
 * - Webhooks
 * - Third-party APIs
 *
 * The domain layer should not depend directly on SDK-specific
 * implementations.
 *
 * =============================================================
 */

interface IntegrationRepository {

    /*
     * ---------------------------------------------------------
     * INTEGRATION DASHBOARD
     * ---------------------------------------------------------
     */

    suspend fun getIntegrationDashboard(
        organizationId: String
    ): Result<IntegrationDashboardData>

    fun observeIntegrationDashboard(
        organizationId: String
    ): Flow<Result<IntegrationDashboardData>>


    /*
     * ---------------------------------------------------------
     * INTEGRATIONS
     * ---------------------------------------------------------
     */

    suspend fun getIntegrations(
        organizationId: String
    ): Result<List<IntegrationData>>

    suspend fun getIntegration(
        integrationId: String
    ): Result<IntegrationData>

    suspend fun createIntegration(
        actorId: String,
        integration: CreateIntegrationData
    ): Result<String>

    suspend fun updateIntegration(
        actorId: String,
        integrationId: String,
        update: UpdateIntegrationData
    ): Result<Unit>

    suspend fun deleteIntegration(
        actorId: String,
        integrationId: String
    ): Result<Unit>

    suspend fun enableIntegration(
        actorId: String,
        integrationId: String
    ): Result<Unit>

    suspend fun disableIntegration(
        actorId: String,
        integrationId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * CONNECTION TESTING
     * ---------------------------------------------------------
     */

    suspend fun testIntegration(
        actorId: String,
        integrationId: String
    ): Result<IntegrationTestResultData>

    suspend fun refreshIntegrationStatus(
        integrationId: String
    ): Result<IntegrationHealthData>


    /*
     * ---------------------------------------------------------
     * PAYMENT PROVIDERS
     * ---------------------------------------------------------
     */

    suspend fun getPaymentProviders(
        organizationId: String
    ): Result<List<PaymentProviderData>>

    suspend fun initiatePayment(
        actorId: String,
        request: ExternalPaymentRequest
    ): Result<ExternalPaymentResultData>

    suspend fun verifyPayment(
        paymentReference: String,
        provider: String
    ): Result<ExternalPaymentVerificationData>

    suspend fun refundPayment(
        actorId: String,
        request: ExternalRefundRequest
    ): Result<ExternalRefundResultData>

    suspend fun getPaymentStatus(
        paymentReference: String
    ): Result<ExternalPaymentStatusData>


    /*
     * ---------------------------------------------------------
     * MOBILE MONEY
     * ---------------------------------------------------------
     */

    suspend fun initiateMobileMoneyPayment(
        actorId: String,
        request: MobileMoneyPaymentRequest
    ): Result<MobileMoneyPaymentResultData>

    suspend fun checkMobileMoneyPayment(
        transactionId: String
    ): Result<MobileMoneyPaymentStatusData>

    suspend fun reverseMobileMoneyPayment(
        actorId: String,
        transactionId: String,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * BANKING
     * ---------------------------------------------------------
     */

    suspend fun verifyBankAccount(
        actorId: String,
        request: BankAccountVerificationRequest
    ): Result<BankAccountVerificationData>

    suspend fun getBankTransferStatus(
        transferReference: String
    ): Result<BankTransferStatusData>


    /*
     * ---------------------------------------------------------
     * SMS
     * ---------------------------------------------------------
     */

    suspend fun sendSms(
        actorId: String?,
        request: SmsRequest
    ): Result<CommunicationDeliveryResultData>

    suspend fun getSmsStatus(
        messageId: String
    ): Result<CommunicationDeliveryStatusData>


    /*
     * ---------------------------------------------------------
     * EMAIL
     * ---------------------------------------------------------
     */

    suspend fun sendEmail(
        actorId: String?,
        request: EmailRequest
    ): Result<CommunicationDeliveryResultData>

    suspend fun getEmailStatus(
        messageId: String
    ): Result<CommunicationDeliveryStatusData>


    /*
     * ---------------------------------------------------------
     * WHATSAPP
     * ---------------------------------------------------------
     */

    suspend fun sendWhatsAppMessage(
        actorId: String?,
        request: WhatsAppRequest
    ): Result<CommunicationDeliveryResultData>

    suspend fun getWhatsAppStatus(
        messageId: String
    ): Result<CommunicationDeliveryStatusData>


    /*
     * ---------------------------------------------------------
     * PUSH NOTIFICATIONS
     * ---------------------------------------------------------
     */

    suspend fun sendPushNotification(
        actorId: String?,
        request: PushNotificationRequest
    ): Result<CommunicationDeliveryResultData>

    suspend fun registerPushToken(
        userId: String,
        token: String,
        platform: PushPlatform
    ): Result<Unit>

    suspend fun removePushToken(
        userId: String,
        token: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * GOOGLE MAPS / LOCATION
     * ---------------------------------------------------------
     */

    suspend fun geocodeAddress(
        request: GeocodeRequest
    ): Result<GeocodeResultData>

    suspend fun reverseGeocode(
        latitude: Double,
        longitude: Double
    ): Result<GeocodeResultData>

    suspend fun calculateDistance(
        origin: CoordinatesData,
        destination: CoordinatesData
    ): Result<DistanceResultData>

    suspend fun calculateRoute(
        request: RouteRequest
    ): Result<RouteResultData>

    suspend fun searchPlaces(
        request: PlaceSearchRequest
    ): Result<List<PlaceResultData>>


    /*
     * ---------------------------------------------------------
     * IDENTITY VERIFICATION
     * ---------------------------------------------------------
     */

    suspend fun startIdentityVerification(
        actorId: String,
        request: IdentityVerificationRequest
    ): Result<IdentityVerificationSessionData>

    suspend fun getIdentityVerificationStatus(
        verificationId: String
    ): Result<IdentityVerificationStatusData>

    suspend fun cancelIdentityVerification(
        actorId: String,
        verificationId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * CLOUD STORAGE
     * ---------------------------------------------------------
     */

    suspend fun uploadFile(
        actorId: String,
        request: ExternalFileUploadRequest
    ): Result<ExternalFileData>

    suspend fun deleteFile(
        actorId: String,
        fileId: String
    ): Result<Unit>

    suspend fun getFile(
        fileId: String
    ): Result<ExternalFileData>

    suspend fun generateDownloadUrl(
        fileId: String
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * CALENDAR
     * ---------------------------------------------------------
     */

    suspend fun createCalendarEvent(
        actorId: String,
        request: CalendarEventRequest
    ): Result<CalendarEventData>

    suspend fun updateCalendarEvent(
        actorId: String,
        eventId: String,
        request: CalendarEventRequest
    ): Result<Unit>

    suspend fun deleteCalendarEvent(
        actorId: String,
        eventId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * E-SIGNATURE
     * ---------------------------------------------------------
     */

    suspend fun createSignatureRequest(
        actorId: String,
        request: SignatureRequest
    ): Result<ExternalSignatureRequestData>

    suspend fun getSignatureStatus(
        requestId: String
    ): Result<SignatureStatusData>

    suspend fun cancelSignatureRequest(
        actorId: String,
        requestId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * AI SERVICES
     * ---------------------------------------------------------
     */

    suspend fun requestAiAnalysis(
        actorId: String,
        request: AiAnalysisRequest
    ): Result<AiAnalysisResultData>

    suspend fun generateAiMessage(
        actorId: String,
        request: AiMessageRequest
    ): Result<AiMessageResultData>


    /*
     * ---------------------------------------------------------
     * WEBHOOKS
     * ---------------------------------------------------------
     */

    suspend fun registerWebhook(
        actorId: String,
        webhook: CreateWebhookData
    ): Result<String>

    suspend fun getWebhooks(
        organizationId: String
    ): Result<List<WebhookData>>

    suspend fun updateWebhook(
        actorId: String,
        webhookId: String,
        update: UpdateWebhookData
    ): Result<Unit>

    suspend fun deleteWebhook(
        actorId: String,
        webhookId: String
    ): Result<Unit>

    suspend fun testWebhook(
        actorId: String,
        webhookId: String
    ): Result<WebhookTestResultData>


    /*
     * ---------------------------------------------------------
     * WEBHOOK EVENTS
     * ---------------------------------------------------------
     */

    suspend fun processWebhookEvent(
        event: WebhookEventData
    ): Result<Unit>

    suspend fun getWebhookEvents(
        organizationId: String,
        limit: Int = 100
    ): Result<List<WebhookEventData>>


    /*
     * ---------------------------------------------------------
     * API REQUEST LOGS
     * ---------------------------------------------------------
     */

    suspend fun getIntegrationLogs(
        organizationId: String,
        integrationId: String? = null,
        limit: Int = 100
    ): Result<List<IntegrationLogData>>


    /*
     * ---------------------------------------------------------
     * USAGE / BILLING
     * ---------------------------------------------------------
     */

    suspend fun getIntegrationUsage(
        organizationId: String,
        integrationId: String
    ): Result<IntegrationUsageData>

    suspend fun getIntegrationCosts(
        organizationId: String,
        integrationId: String,
        startDate: String,
        endDate: String
    ): Result<IntegrationCostData>


    /*
     * ---------------------------------------------------------
     * FALLBACK
     * ---------------------------------------------------------
     */

    suspend fun getFallbackProvider(
        organizationId: String,
        category: IntegrationCategory
    ): Result<IntegrationData?>

    suspend fun setFallbackProvider(
        actorId: String,
        organizationId: String,
        category: IntegrationCategory,
        integrationId: String
    ): Result<Unit>
}


/*
 * =============================================================
 * INTEGRATION
 * =============================================================
 */

data class CreateIntegrationData(
    val organizationId: String,
    val name: String,
    val category: IntegrationCategory,
    val provider: String,
    val description: String?,
    val enabled: Boolean = true
)

data class UpdateIntegrationData(
    val name: String?,
    val description: String?,
    val enabled: Boolean?
)

data class IntegrationData(
    val id: String,
    val organizationId: String,
    val name: String,
    val category: IntegrationCategory,
    val provider: String,
    val description: String?,
    val enabled: Boolean,
    val healthy: Boolean,
    val primary: Boolean,
    val lastCheckedAt: String?,
    val createdAt: String
)

data class IntegrationTestResultData(
    val integrationId: String,
    val successful: Boolean,
    val responseTimeMs: Long?,
    val message: String?,
    val testedAt: String
)

data class IntegrationHealthData(
    val integrationId: String,
    val healthy: Boolean,
    val status: IntegrationHealthStatus,
    val responseTimeMs: Long?,
    val errorCount: Int,
    val lastSuccessAt: String?,
    val lastFailureAt: String?,
    val checkedAt: String
)


/*
 * =============================================================
 * PAYMENTS
 * =============================================================
 */

data class PaymentProviderData(
    val id: String,
    val name: String,
    val provider: String,
    val supportedCurrencies: List<String>,
    val supportedMethods: List<String>,
    val enabled: Boolean,
    val primary: Boolean
)

data class ExternalPaymentRequest(
    val providerId: String,
    val amount: Double,
    val currency: String,
    val reference: String,
    val description: String?,
    val customerId: String?,
    val callbackUrl: String?
)

data class ExternalPaymentResultData(
    val transactionId: String,
    val reference: String,
    val status: ExternalPaymentStatus,
    val checkoutUrl: String?,
    val message: String?
)

data class ExternalPaymentVerificationData(
    val transactionId: String,
    val reference: String,
    val status: ExternalPaymentStatus,
    val amount: Double?,
    val currency: String?,
    val paidAt: String?,
    val verified: Boolean
)

data class ExternalRefundRequest(
    val transactionId: String,
    val amount: Double?,
    val reason: String
)

data class ExternalRefundResultData(
    val refundId: String,
    val transactionId: String,
    val status: ExternalRefundStatus,
    val amount: Double?,
    val message: String?
)

data class ExternalPaymentStatusData(
    val transactionId: String,
    val status: ExternalPaymentStatus,
    val updatedAt: String
)


/*
 * =============================================================
 * MOBILE MONEY
 * =============================================================
 */

data class MobileMoneyPaymentRequest(
    val providerId: String,
    val phoneNumber: String,
    val amount: Double,
    val currency: String,
    val accountReference: String,
    val description: String?
)

data class MobileMoneyPaymentResultData(
    val transactionId: String,
    val status: ExternalPaymentStatus,
    val message: String?
)

data class MobileMoneyPaymentStatusData(
    val transactionId: String,
    val status: ExternalPaymentStatus,
    val amount: Double?,
    val receiptNumber: String?,
    val completedAt: String?
)


/*
 * =============================================================
 * BANKING
 * =============================================================
 */

data class BankAccountVerificationRequest(
    val bankName: String,
    val accountName: String,
    val accountNumber: String,
    val country: String
)

data class BankAccountVerificationData(
    val verified: Boolean,
    val accountName: String?,
    val bankName: String?,
    val message: String?
)

data class BankTransferStatusData(
    val reference: String,
    val status: BankTransferStatus,
    val amount: Double?,
    val completedAt: String?
)


/*
 * =============================================================
 * SMS
 * =============================================================
 */

data class SmsRequest(
    val recipient: String,
    val message: String,
    val senderId: String? = null
)


/*
 * =============================================================
 * EMAIL
 * =============================================================
 */

data class EmailRequest(
    val recipient: String,
    val subject: String,
    val body: String,
    val html: Boolean = true,
    val attachments: List<String> = emptyList()
)


/*
 * =============================================================
 * WHATSAPP
 * =============================================================
 */

data class WhatsAppRequest(
    val recipient: String,
    val message: String,
    val templateName: String? = null,
    val parameters: Map<String, String> = emptyMap()
)


/*
 * =============================================================
 * PUSH
 * =============================================================
 */

data class PushNotificationRequest(
    val userId: String,
    val title: String,
    val body: String,
    val data: Map<String, String> = emptyMap()
)


/*
 * =============================================================
 * COMMUNICATION STATUS
 * =============================================================
 */

data class CommunicationDeliveryResultData(
    val messageId: String,
    val accepted: Boolean,
    val status: CommunicationDeliveryStatus,
    val message: String?
)

data class CommunicationDeliveryStatusData(
    val messageId: String,
    val status: CommunicationDeliveryStatus,
    val deliveredAt: String?,
    val readAt: String?,
    val failureReason: String?
)


/*
 * =============================================================
 * MAPS
 * =============================================================
 */

data class CoordinatesData(
    val latitude: Double,
    val longitude: Double
)

data class GeocodeRequest(
    val address: String
)

data class GeocodeResultData(
    val formattedAddress: String,
    val coordinates: CoordinatesData,
    val components: Map<String, String>
)

data class DistanceResultData(
    val distanceMeters: Double,
    val durationSeconds: Long
)

data class RouteRequest(
    val origin: CoordinatesData,
    val destination: CoordinatesData,
    val mode: TravelMode = TravelMode.DRIVING
)

data class RouteResultData(
    val distanceMeters: Double,
    val durationSeconds: Long,
    val encodedPolyline: String?,
    val steps: List<RouteStepData>
)

data class RouteStepData(
    val instruction: String,
    val distanceMeters: Double,
    val durationSeconds: Long
)

data class PlaceSearchRequest(
    val query: String,
    val location: CoordinatesData?,
    val radiusMeters: Int?
)

data class PlaceResultData(
    val placeId: String,
    val name: String,
    val address: String?,
    val coordinates: CoordinatesData,
    val category: String?
)


/*
 * =============================================================
 * IDENTITY
 * =============================================================
 */

data class IdentityVerificationRequest(
    val userId: String,
    val country: String,
    val documentType: ExternalIdentityDocumentType,
    val documentId: String
)

data class IdentityVerificationSessionData(
    val verificationId: String,
    val provider: String,
    val status: IdentityVerificationStatus
)

data class IdentityVerificationStatusData(
    val verificationId: String,
    val status: IdentityVerificationStatus,
    val verified: Boolean,
    val confidenceScore: Double?,
    val reason: String?
)


/*
 * =============================================================
 * FILE STORAGE
 * =============================================================
 */

data class ExternalFileUploadRequest(
    val filePath: String,
    val fileName: String,
    val mimeType: String,
    val folder: String?,
    val public: Boolean = false
)

data class ExternalFileData(
    val id: String,
    val fileName: String,
    val mimeType: String,
    val sizeBytes: Long,
    val url: String?,
    val createdAt: String
)


/*
 * =============================================================
 * CALENDAR
 * =============================================================
 */

data class CalendarEventRequest(
    val title: String,
    val description: String?,
    val startAt: String,
    val endAt: String,
    val location: String?,
    val attendees: List<String> = emptyList()
)

data class CalendarEventData(
    val eventId: String,
    val provider: String,
    val eventUrl: String?,
    val createdAt: String
)


/*
 * =============================================================
 * E-SIGNATURE
 * =============================================================
 */

data class SignatureRequest(
    val documentId: String,
    val signers: List<SignatureSignerData>,
    val message: String?
)

data class SignatureSignerData(
    val userId: String?,
    val name: String,
    val email: String,
    val phone: String?
)

data class ExternalSignatureRequestData(
    val requestId: String,
    val status: SignatureStatus,
    val signingUrl: String?,
    val createdAt: String
)

data class SignatureStatusData(
    val requestId: String,
    val status: SignatureStatus,
    val signedCount: Int,
    val totalSigners: Int,
    val completedAt: String?
)


/*
 * =============================================================
 * AI
 * =============================================================
 */

data class AiAnalysisRequest(
    val type: AiAnalysisType,
    val input: String,
    val context: Map<String, String> = emptyMap()
)

data class AiAnalysisResultData(
    val type: AiAnalysisType,
    val result: String,
    val confidence: Double?,
    val generatedAt: String
)

data class AiMessageRequest(
    val purpose: String,
    val recipientContext: String,
    val additionalContext: Map<String, String> = emptyMap(),
    val tone: String = "professional"
)

data class AiMessageResultData(
    val message: String,
    val generatedAt: String
)


/*
 * =============================================================
 * WEBHOOKS
 * =============================================================
 */

data class CreateWebhookData(
    val organizationId: String,
    val name: String,
    val url: String,
    val events: List<String>,
    val enabled: Boolean = true
)

data class UpdateWebhookData(
    val name: String?,
    val url: String?,
    val events: List<String>?,
    val enabled: Boolean?
)

data class WebhookData(
    val id: String,
    val organizationId: String,
    val name: String,
    val url: String,
    val events: List<String>,
    val enabled: Boolean,
    val healthy: Boolean,
    val lastTriggeredAt: String?
)

data class WebhookTestResultData(
    val webhookId: String,
    val successful: Boolean,
    val statusCode: Int?,
    val responseTimeMs: Long?,
    val message: String?
)

data class WebhookEventData(
    val id: String? = null,
    val organizationId: String?,
    val eventType: String,
    val payload: Map<String, Any?>,
    val source: String?,
    val receivedAt: String? = null,
    val processed: Boolean = false
)


/*
 * =============================================================
 * LOGGING / USAGE
 * =============================================================
 */

data class IntegrationLogData(
    val id: String,
    val integrationId: String,
    val operation: String,
    val successful: Boolean,
    val statusCode: Int?,
    val durationMs: Long?,
    val errorMessage: String?,
    val timestamp: String
)

data class IntegrationUsageData(
    val integrationId: String,
    val requests: Long,
    val successfulRequests: Long,
    val failedRequests: Long,
    val dataTransferredBytes: Long,
    val periodStart: String,
    val periodEnd: String
)

data class IntegrationCostData(
    val integrationId: String,
    val currency: String,
    val totalCost: Double,
    val requestCost: Double,
    val dataCost: Double,
    val otherCost: Double,
    val periodStart: String,
    val periodEnd: String
)


/*
 * =============================================================
 * DASHBOARD
 * =============================================================
 */

data class IntegrationDashboardData(
    val totalIntegrations: Int,
    val activeIntegrations: Int,
    val healthyIntegrations: Int,
    val unhealthyIntegrations: Int,
    val failedRequests24h: Long,
    val successfulRequests24h: Long,
    val estimatedCostThisMonth: Double,
    val currency: String,
    val recentFailures: List<IntegrationLogData>
)


/*
 * =============================================================
 * ENUMS
 * =============================================================
 */

enum class IntegrationCategory {
    PAYMENT,
    MOBILE_MONEY,
    BANKING,
    SMS,
    EMAIL,
    WHATSAPP,
    PUSH_NOTIFICATION,
    MAPS,
    GEOCODING,
    IDENTITY_VERIFICATION,
    STORAGE,
    CALENDAR,
    E_SIGNATURE,
    AI,
    ANALYTICS,
    WEBHOOK,
    OTHER
}

enum class IntegrationHealthStatus {
    HEALTHY,
    DEGRADED,
    UNAVAILABLE,
    UNKNOWN
}

enum class ExternalPaymentStatus {
    PENDING,
    PROCESSING,
    SUCCESS,
    FAILED,
    CANCELLED,
    REFUNDED
}

enum class ExternalRefundStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED,
    REJECTED
}

enum class BankTransferStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED,
    REVERSED
}

enum class CommunicationDeliveryStatus {
    QUEUED,
    SENT,
    DELIVERED,
    READ,
    FAILED
}

enum class PushPlatform {
    ANDROID,
    IOS,
    WEB
}

enum class TravelMode {
    DRIVING,
    WALKING,
    BICYCLING,
    TRANSIT
}

enum class ExternalIdentityDocumentType {
    NATIONAL_ID,
    PASSPORT,
    DRIVING_LICENSE,
    OTHER
}

enum class IdentityVerificationStatus {
    CREATED,
    PENDING,
    PROCESSING,
    VERIFIED,
    REJECTED,
    FAILED,
    CANCELLED
}

enum class SignatureStatus {
    DRAFT,
    SENT,
    PARTIALLY_SIGNED,
    COMPLETED,
    DECLINED,
    EXPIRED,
    CANCELLED
}

enum class AiAnalysisType {
    PROPERTY_DESCRIPTION,
    MAINTENANCE_ANALYSIS,
    FRAUD_ANALYSIS,
    DOCUMENT_ANALYSIS,
    RENT_ANALYSIS,
    TENANT_ANALYSIS,
    MARKET_ANALYSIS,
    GENERAL
}