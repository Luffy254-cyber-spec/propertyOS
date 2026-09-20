package com.him.landlordtenant.app.interfaces

import com.him.landlordtenant.app.data.model.Tenant
import kotlinx.coroutines.flow.Flow

interface TenantRepository {

    fun getTenants(): Flow<List<Tenant>>
    suspend fun getTenantById(id: String): Tenant?
    suspend fun saveTenant(tenant: Tenant): Result<Unit>
    suspend fun deleteTenant(tenant: Tenant): Result<Unit>

    fun observeTenantProfile(tenantId: String): Flow<Result<TenantProfileData>>

    suspend fun getTenantProfile(tenantId: String): Result<TenantProfileData>

    suspend fun updateTenantProfile(tenantId: String, profile: TenantProfileData): Result<Unit>

    suspend fun updateProfilePhoto(tenantId: String, filePath: String): Result<String>

    suspend fun removeProfilePhoto(tenantId: String): Result<Unit>

    suspend fun getCurrentTenancy(tenantId: String): Result<TenancyData?>

    suspend fun requestMoveOut(tenantId: String, tenancyId: String, reason: String): Result<Unit>

    suspend fun getRentBalance(tenantId: String): Result<RentBalanceData>

    suspend fun getRentPaymentHistory(tenantId: String): Result<List<PaymentSummaryData>>

    suspend fun getBills(tenantId: String): Result<List<BillSummaryData>>

    suspend fun getActiveAgreement(tenantId: String): Result<AgreementSummaryData?>

    suspend fun signAgreement(tenantId: String, agreementId: String): Result<Unit>

    suspend fun createMaintenanceRequest(tenantId: String, request: MaintenanceRequestData): Result<String>

    suspend fun getMaintenanceRequests(tenantId: String): Result<List<MaintenanceSummaryData>>

    suspend fun searchProperties(query: String, county: String? = null, town: String? = null): Result<List<PropertySearchData>>

    suspend fun saveProperty(tenantId: String, propertyId: String): Result<Unit>

    suspend fun requestViewing(tenantId: String, propertyId: String, viewing: ViewingRequestData): Result<String>

    suspend fun searchProfessionals(category: String, location: String? = null): Result<List<ProfessionalSummaryData>>

    suspend fun getProfessional(professionalId: String): Result<ProfessionalSummaryData>

    suspend fun requestProfessional(tenantId: String, professionalId: String, request: ProfessionalRequestData): Result<String>

    suspend fun getConversations(tenantId: String): Result<List<ConversationSummaryData>>

    suspend fun getNotifications(tenantId: String): Result<List<NotificationSummaryData>>

    suspend fun uploadDocument(tenantId: String, filePath: String, documentType: String): Result<String>

    suspend fun submitEmergencyReport(tenantId: String, emergency: EmergencyReportData): Result<String>

    suspend fun getDashboard(tenantId: String): Result<TenantDashboardData>

    suspend fun applyToApartment(tenantId: String, apartmentId: String, signature: String, nationalIdUrl: String): Result<Unit>
    suspend fun getTenantApplications(tenantId: String): Result<List<ApartmentApplicationData>>
    suspend fun cancelApplication(tenantId: String, applicationId: String): Result<Unit>

    suspend fun joinApartment(tenantId: String, apartmentId: String, signature: String? = null, nationalIdUrl: String? = null): Result<Unit>
    suspend fun pickHouse(tenantId: String, apartmentId: String, houseId: String): Result<Unit>
}

data class ApartmentApplicationData(
    val id: String = "",
    val tenantId: String = "",
    val tenantName: String = "",
    val tenantEmail: String = "",
    val tenantPhone: String = "",
    val apartmentId: String = "",
    val apartmentName: String = "",
    val houseId: String = "",
    val houseNumber: String = "",
    val landlordId: String = "",
    val status: String = "PENDING", // PENDING, APPROVED, DECLINED, CANCELLED
    val signature: String = "",
    val nationalIdUrl: String = "",
    val appliedAt: Long = 0L,
    val processedAt: Long? = null,
    val rejectionReason: String? = null
)

data class TenantProfileData(
    val id: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String?,
    val profilePhotoUrl: String?,
    val nationalIdVerified: Boolean
)

data class TenancyData(
    val id: String,
    val propertyId: String,
    val propertyName: String,
    val unitName: String,
    val landlordId: String,
    val landlordName: String,
    val landlordPhone: String,
    val startDate: String,
    val endDate: String?,
    val monthlyRent: Double
)

data class BillSummaryData(
    val id: String,
    val title: String,
    val amount: Double,
    val amountPaid: Double,
    val balance: Double,
    val dueDate: String,
    val status: String
)

data class AgreementSummaryData(
    val id: String,
    val propertyId: String,
    val propertyName: String,
    val startDate: String,
    val endDate: String?,
    val status: String
)

data class MaintenanceRequestData(
    val title: String,
    val description: String,
    val category: String,
    val priority: String,
    val imageUrls: List<String> = emptyList()
)

data class PropertySearchData(
    val id: String,
    val name: String,
    val location: String,
    val county: String?,
    val monthlyRent: Double,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val imageUrl: String?,
    val isVerified: Boolean
)

data class ViewingRequestData(
    val preferredDate: String,
    val preferredTime: String,
    val message: String?
)

data class EmergencyReportData(
    val title: String,
    val description: String,
    val location: String?,
    val latitude: Double?,
    val longitude: Double?
)

data class TenantDashboardData(
    val profile: TenantProfileData,
    val tenancy: TenancyData?,
    val rentBalance: RentBalanceData?,
    val outstandingBills: List<BillSummaryData>,
    val recentPayments: List<PaymentSummaryData>,
    val maintenanceRequests: List<MaintenanceSummaryData>,
    val notifications: List<NotificationSummaryData>,
    val unreadMessages: Int
)
