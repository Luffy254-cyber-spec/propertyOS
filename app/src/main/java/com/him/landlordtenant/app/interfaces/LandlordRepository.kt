package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * LANDLORD REPOSITORY
 * =============================================================
 *
 * Domain contract for landlord/property-owner operations.
 *
 * Covers:
 * - Property management
 * - Units
 * - Tenants
 * - Rent
 * - Bills
 * - Payments
 * - Agreements
 * - Maintenance
 * - Professionals
 * - Viewings
 * - Messaging
 * - Notifications
 * - Reports
 * - Property advertisements
 * - Dashboard statistics
 *
 * Implementations may use:
 * - Firebase
 * - REST API
 * - Room
 * - Supabase
 * - Django backend
 * - Local cache
 *
 * =============================================================
 */

interface LandlordRepository {

    /*
     * ---------------------------------------------------------
     * LANDLORD PROFILE
     * ---------------------------------------------------------
     */

    suspend fun getLandlordProfile(
        landlordId: String
    ): Result<LandlordProfileData>

    fun observeLandlordProfile(
        landlordId: String
    ): Flow<Result<LandlordProfileData>>

    suspend fun updateLandlordProfile(
        landlordId: String,
        profile: LandlordProfileData
    ): Result<Unit>

    suspend fun updateProfilePhoto(
        landlordId: String,
        filePath: String
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * PROPERTIES
     * ---------------------------------------------------------
     */

    suspend fun createProperty(
        landlordId: String,
        property: PropertyData
    ): Result<String>

    suspend fun updateProperty(
        landlordId: String,
        propertyId: String,
        property: PropertyData
    ): Result<Unit>

    suspend fun deleteProperty(
        landlordId: String,
        propertyId: String
    ): Result<Unit>

    suspend fun getProperty(
        landlordId: String,
        propertyId: String
    ): Result<PropertyData>

    suspend fun getProperties(
        landlordId: String
    ): Result<List<PropertyData>>

    fun observeProperties(
        landlordId: String
    ): Flow<Result<List<PropertyData>>>

    suspend fun publishProperty(
        landlordId: String,
        propertyId: String
    ): Result<Unit>

    suspend fun unpublishProperty(
        landlordId: String,
        propertyId: String
    ): Result<Unit>

    suspend fun markPropertyAsAvailable(
        landlordId: String,
        propertyId: String
    ): Result<Unit>

    suspend fun markPropertyAsOccupied(
        landlordId: String,
        propertyId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * PROPERTY MEDIA
     * ---------------------------------------------------------
     */

    suspend fun uploadPropertyImage(
        landlordId: String,
        propertyId: String,
        filePath: String
    ): Result<String>

    suspend fun uploadPropertyVideo(
        landlordId: String,
        propertyId: String,
        filePath: String
    ): Result<String>

    suspend fun deletePropertyMedia(
        landlordId: String,
        propertyId: String,
        mediaId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * PROPERTY UNITS
     * ---------------------------------------------------------
     */

    suspend fun createUnit(
        landlordId: String,
        propertyId: String,
        unit: UnitData
    ): Result<String>

    suspend fun updateUnit(
        landlordId: String,
        propertyId: String,
        unitId: String,
        unit: UnitData
    ): Result<Unit>

    suspend fun deleteUnit(
        landlordId: String,
        propertyId: String,
        unitId: String
    ): Result<Unit>

    suspend fun getUnits(
        landlordId: String,
        propertyId: String
    ): Result<List<UnitData>>

    fun observeUnits(
        propertyId: String
    ): Flow<Result<List<UnitData>>>


    /*
     * ---------------------------------------------------------
     * TENANTS
     * ---------------------------------------------------------
     */

    suspend fun getTenants(
        landlordId: String
    ): Result<List<TenantSummaryData>>

    fun observeTenants(
        landlordId: String
    ): Flow<Result<List<TenantSummaryData>>>

    suspend fun getTenant(
        landlordId: String,
        tenantId: String
    ): Result<TenantSummaryData>

    suspend fun inviteTenant(
        landlordId: String,
        tenantId: String,
        propertyId: String,
        unitId: String
    ): Result<String>

    suspend fun removeTenant(
        landlordId: String,
        tenantId: String,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * RENT
     * ---------------------------------------------------------
     */

    suspend fun getRentCollectionSummary(
        landlordId: String
    ): Result<RentCollectionData>

    suspend fun getTenantRentBalance(
        landlordId: String,
        tenantId: String
    ): Result<RentBalanceData>

    suspend fun getRentTransactions(
        landlordId: String
    ): Result<List<PaymentSummaryData>>

    suspend fun getOverdueRent(
        landlordId: String
    ): Result<List<RentArrearsData>>

    suspend fun sendRentReminder(
        landlordId: String,
        tenantId: String
    ): Result<Unit>

    suspend fun sendBulkRentReminders(
        landlordId: String,
        tenantIds: List<String>
    ): Result<Int>


    /*
     * ---------------------------------------------------------
     * BILLS
     * ---------------------------------------------------------
     */

    suspend fun createBill(
        landlordId: String,
        bill: LandlordBillData
    ): Result<String>

    suspend fun updateBill(
        landlordId: String,
        billId: String,
        bill: LandlordBillData
    ): Result<Unit>

    suspend fun cancelBill(
        landlordId: String,
        billId: String,
        reason: String
    ): Result<Unit>

    suspend fun getBills(
        landlordId: String
    ): Result<List<LandlordBillData>>

    fun observeBills(
        landlordId: String
    ): Flow<Result<List<LandlordBillData>>>

    suspend fun getOutstandingBills(
        landlordId: String
    ): Result<List<LandlordBillData>>

    suspend fun getOverdueBills(
        landlordId: String
    ): Result<List<LandlordBillData>>


    /*
     * ---------------------------------------------------------
     * PAYMENTS
     * ---------------------------------------------------------
     */

    suspend fun getPayments(
        landlordId: String
    ): Result<List<PaymentSummaryData>>

    fun observePayments(
        landlordId: String
    ): Flow<Result<List<PaymentSummaryData>>>

    suspend fun getPayment(
        landlordId: String,
        paymentId: String
    ): Result<PaymentSummaryData>

    suspend fun reconcilePayment(
        landlordId: String,
        paymentId: String
    ): Result<Unit>

    suspend fun issuePaymentReceipt(
        landlordId: String,
        paymentId: String
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * AGREEMENTS
     * ---------------------------------------------------------
     */

    suspend fun createAgreement(
        landlordId: String,
        agreement: LandlordAgreementData
    ): Result<String>

    suspend fun updateAgreement(
        landlordId: String,
        agreementId: String,
        agreement: LandlordAgreementData
    ): Result<Unit>

    suspend fun sendAgreementForSignature(
        landlordId: String,
        agreementId: String
    ): Result<Unit>

    suspend fun getAgreements(
        landlordId: String
    ): Result<List<LandlordAgreementData>>

    fun observeAgreements(
        landlordId: String
    ): Flow<Result<List<LandlordAgreementData>>>

    suspend fun terminateAgreement(
        landlordId: String,
        agreementId: String,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * MAINTENANCE
     * ---------------------------------------------------------
     */

    suspend fun getMaintenanceRequests(
        landlordId: String
    ): Result<List<LandlordMaintenanceData>>

    fun observeMaintenanceRequests(
        landlordId: String
    ): Flow<Result<List<LandlordMaintenanceData>>>

    suspend fun getMaintenanceRequest(
        landlordId: String,
        requestId: String
    ): Result<LandlordMaintenanceData>

    suspend fun assignMaintenance(
        landlordId: String,
        requestId: String,
        professionalId: String
    ): Result<Unit>

    suspend fun approveMaintenance(
        landlordId: String,
        requestId: String
    ): Result<Unit>

    suspend fun rejectMaintenance(
        landlordId: String,
        requestId: String,
        reason: String
    ): Result<Unit>

    suspend fun cancelMaintenance(
        landlordId: String,
        requestId: String,
        reason: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * PROFESSIONALS
     * ---------------------------------------------------------
     */

    suspend fun searchProfessionals(
        landlordId: String,
        category: String,
        location: String? = null
    ): Result<List<ProfessionalSummaryData>>

    suspend fun getProfessional(
        professionalId: String
    ): Result<ProfessionalSummaryData>

    suspend fun hireProfessional(
        landlordId: String,
        professionalId: String,
        request: ProfessionalRequestData
    ): Result<String>


    /*
     * ---------------------------------------------------------
     * VIEWINGS
     * ---------------------------------------------------------
     */

    suspend fun getViewingRequests(
        landlordId: String
    ): Result<List<LandlordViewingData>>

    fun observeViewingRequests(
        landlordId: String
    ): Flow<Result<List<LandlordViewingData>>>

    suspend fun approveViewing(
        landlordId: String,
        viewingId: String
    ): Result<Unit>

    suspend fun rejectViewing(
        landlordId: String,
        viewingId: String,
        reason: String
    ): Result<Unit>

    suspend fun rescheduleViewing(
        landlordId: String,
        viewingId: String,
        newDate: String,
        newTime: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * MESSAGING
     * ---------------------------------------------------------
     */

    suspend fun getConversations(
        landlordId: String
    ): Result<List<ConversationSummaryData>>

    fun observeConversations(
        landlordId: String
    ): Flow<Result<List<ConversationSummaryData>>>

    suspend fun sendMessage(
        landlordId: String,
        conversationId: String,
        message: String
    ): Result<String>

    suspend fun markConversationAsRead(
        landlordId: String,
        conversationId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * NOTIFICATIONS
     * ---------------------------------------------------------
     */

    suspend fun getNotifications(
        landlordId: String
    ): Result<List<NotificationSummaryData>>

    fun observeNotifications(
        landlordId: String
    ): Flow<Result<List<NotificationSummaryData>>>

    suspend fun markNotificationAsRead(
        landlordId: String,
        notificationId: String
    ): Result<Unit>

    suspend fun markAllNotificationsAsRead(
        landlordId: String
    ): Result<Unit>


    /*
     * ---------------------------------------------------------
     * REPORTS
     * ---------------------------------------------------------
     */

    suspend fun getOccupancyReport(
        landlordId: String
    ): Result<OccupancyReportData>

    // Staff Management
    suspend fun getStaff(landlordId: String): Result<List<StaffData>>
    suspend fun addStaff(landlordId: String, staff: CreateStaffData): Result<String>
    suspend fun removeStaff(landlordId: String, staffId: String): Result<Unit>

    suspend fun getIncomeReport(
        landlordId: String,
        startDate: String,
        endDate: String
    ): Result<IncomeReportData>

    suspend fun getArrearsReport(
        landlordId: String
    ): Result<ArrearsReportData>

    suspend fun getMaintenanceReport(
        landlordId: String
    ): Result<MaintenanceReportData>


    /*
     * ---------------------------------------------------------
     * DASHBOARD
     * ---------------------------------------------------------
     */

    suspend fun getDashboard(
        landlordId: String
    ): Result<LandlordDashboardData>

    fun observeDashboard(
        landlordId: String
    ): Flow<Result<LandlordDashboardData>>
}


/*
 * =============================================================
 * LANDLORD DOMAIN DATA CONTRACTS
 * =============================================================
 */

data class LandlordProfileData(
    val id: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String?,
    val profilePhotoUrl: String?,
    val verified: Boolean
)

data class PropertyData(
    val id: String = "",
    val name: String,
    val description: String?,
    val address: String,
    val county: String?,
    val town: String?,
    val latitude: Double?,
    val longitude: Double?,
    val propertyType: String,
    val totalUnits: Int,
    val occupiedUnits: Int,
    val monthlyStartingRent: Double,
    val status: String
)

data class UnitData(
    val id: String = "",
    val name: String,
    val floor: String?,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val monthlyRent: Double,
    val occupied: Boolean,
    val tenantId: String?
)

data class TenantSummaryData(
    val id: String,
    val name: String,
    val phoneNumber: String?,
    val propertyId: String,
    val propertyName: String,
    val unitId: String,
    val unitName: String,
    val rentBalance: Double,
    val tenancyStatus: String
)

data class RentCollectionData(
    val expectedAmount: Double,
    val collectedAmount: Double,
    val outstandingAmount: Double,
    val collectionRate: Double,
    val tenantCount: Int,
    val payingTenants: Int,
    val overdueTenants: Int
)

data class RentArrearsData(
    val tenantId: String,
    val tenantName: String,
    val propertyName: String,
    val unitName: String,
    val amount: Double,
    val daysOverdue: Int
)

data class LandlordBillData(
    val id: String = "",
    val tenantId: String?,
    val propertyId: String,
    val unitId: String?,
    val title: String,
    val description: String?,
    val amount: Double,
    val dueDate: String,
    val status: String
)

data class LandlordAgreementData(
    val id: String = "",
    val tenantId: String,
    val propertyId: String,
    val unitId: String,
    val startDate: String,
    val endDate: String?,
    val monthlyRent: Double,
    val deposit: Double,
    val status: String
)

data class LandlordMaintenanceData(
    val id: String,
    val tenantId: String,
    val tenantName: String,
    val propertyId: String,
    val propertyName: String,
    val unitName: String,
    val title: String,
    val description: String,
    val priority: String,
    val status: String,
    val assignedProfessionalId: String?
)

data class LandlordViewingData(
    val id: String,
    val propertyId: String,
    val propertyName: String,
    val requesterName: String,
    val requesterPhone: String?,
    val requestedDate: String,
    val requestedTime: String,
    val status: String
)

data class OccupancyReportData(
    val totalUnits: Int,
    val occupiedUnits: Int,
    val vacantUnits: Int,
    val occupancyRate: Double
)

data class IncomeReportData(
    val totalExpected: Double,
    val totalCollected: Double,
    val outstanding: Double,
    val transactionCount: Int
)

data class ArrearsReportData(
    val totalArrears: Double,
    val overdueTenantCount: Int,
    val averageDaysOverdue: Double
)

data class MaintenanceReportData(
    val totalRequests: Int,
    val completedRequests: Int,
    val pendingRequests: Int,
    val disputedRequests: Int,
    val averageCompletionDays: Double
)

data class LandlordDashboardData(
    val profile: LandlordProfileData,
    val properties: List<PropertyData>,
    val totalTenants: Int,
    val occupiedUnits: Int,
    val vacantUnits: Int,
    val expectedRent: Double,
    val collectedRent: Double,
    val arrears: Double,
    val pendingMaintenance: Int,
    val pendingViewings: Int,
    val unreadMessages: Int,
    val unreadNotifications: Int
)
