package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

interface MaintenanceRepository {

    suspend fun createRequest(
        tenantId: String,
        request: CreateMaintenanceRequestData
    ): Result<String>

    suspend fun createEmergencyRequest(
        tenantId: String,
        request: EmergencyMaintenanceRequestData
    ): Result<String>

    suspend fun updateRequest(
        userId: String,
        requestId: String,
        request: CreateMaintenanceRequestData
    ): Result<Unit>

    suspend fun cancelRequest(
        userId: String,
        requestId: String,
        reason: String?
    ): Result<Unit>

    suspend fun getRequest(
        requestId: String
    ): Result<MaintenanceDetailsData>

    fun observeRequest(
        requestId: String
    ): Flow<Result<MaintenanceDetailsData>>

    suspend fun getTenantRequests(
        tenantId: String
    ): Result<List<MaintenanceSummaryData>>

    fun observeTenantRequests(
        tenantId: String
    ): Flow<Result<List<MaintenanceSummaryData>>>

    suspend fun getLandlordRequests(
        landlordId: String
    ): Result<List<MaintenanceSummaryData>>

    fun observeLandlordRequests(
        landlordId: String
    ): Flow<Result<List<MaintenanceSummaryData>>>

    suspend fun getPropertyRequests(
        propertyId: String
    ): Result<List<MaintenanceSummaryData>>

    suspend fun updateStatus(
        userId: String,
        requestId: String,
        status: String
    ): Result<Unit>

    suspend fun approveRequest(
        landlordId: String,
        requestId: String
    ): Result<Unit>

    suspend fun rejectRequest(
        landlordId: String,
        requestId: String,
        reason: String
    ): Result<Unit>

    suspend fun markAsInProgress(
        technicianId: String,
        requestId: String
    ): Result<Unit>

    suspend fun markAsCompleted(
        technicianId: String,
        requestId: String
    ): Result<Unit>

    suspend fun confirmCompletion(
        tenantId: String,
        requestId: String
    ): Result<Unit>

    suspend fun updatePriority(
        userId: String,
        requestId: String,
        priority: String
    ): Result<Unit>

    suspend fun uploadRequestImage(
        userId: String,
        requestId: String,
        filePath: String
    ): Result<String>

    suspend fun findProfessionals(
        requestId: String,
        category: String,
        latitude: Double?,
        longitude: Double?,
        radiusKm: Double = 20.0
    ): Result<List<ProfessionalSummaryData>>

    suspend fun assignProfessional(
        landlordId: String,
        requestId: String,
        professionalId: String
    ): Result<Unit>

    suspend fun submitQuotation(
        professionalId: String,
        requestId: String,
        quotation: MaintenanceQuotationData
    ): Result<String>

    suspend fun getQuotations(
        requestId: String
    ): Result<List<MaintenanceQuotationData>>

    suspend fun scheduleRepair(
        scheduledBy: String,
        requestId: String,
        schedule: MaintenanceScheduleData
    ): Result<Unit>

    suspend fun addProgressUpdate(
        professionalId: String,
        requestId: String,
        update: MaintenanceProgressData
    ): Result<String>

    suspend fun getProgressUpdates(
        requestId: String
    ): Result<List<MaintenanceProgressData>>

    suspend fun addCost(
        userId: String,
        requestId: String,
        cost: MaintenanceCostData
    ): Result<String>

    suspend fun getTotalCost(
        requestId: String
    ): Result<Double>

    suspend fun sendMaintenanceMessage(
        userId: String,
        requestId: String,
        message: String
    ): Result<String>

    fun observeMaintenanceMessages(
        requestId: String
    ): Flow<Result<List<MaintenanceMessageData>>>
}

data class CreateMaintenanceRequestData(
    val propertyId: String,
    val unitId: String?,
    val category: String,
    val title: String,
    val description: String,
    val priority: String,
    val preferredDate: String?,
    val preferredTime: String?
)

data class EmergencyMaintenanceRequestData(
    val propertyId: String,
    val unitId: String?,
    val category: String,
    val title: String,
    val description: String,
    val emergencyType: String,
    val latitude: Double?,
    val longitude: Double?
)

data class MaintenanceDetailsData(
    val id: String,
    val propertyId: String,
    val propertyName: String?,
    val unitId: String?,
    val unitName: String?,
    val tenantId: String?,
    val tenantName: String?,
    val landlordId: String?,
    val title: String,
    val description: String,
    val category: String,
    val priority: String,
    val status: String,
    val assignedProfessional: ProfessionalSummaryData?,
    val quotations: List<MaintenanceQuotationData>,
    val schedule: MaintenanceScheduleData?,
    val progress: List<MaintenanceProgressData>,
    val costs: List<MaintenanceCostData>,
    val createdAt: String,
    val updatedAt: String,
    val completedAt: String?
)

data class MaintenanceQuotationData(
    val id: String = "",
    val requestId: String,
    val professionalId: String,
    val professionalName: String?,
    val laborCost: Double,
    val materialCost: Double,
    val transportCost: Double,
    val totalCost: Double,
    val description: String,
    val estimatedDays: Int,
    val status: String,
    val createdAt: String
)

data class MaintenanceScheduleData(
    val date: String,
    val startTime: String,
    val endTime: String?,
    val locationNotes: String?
)

data class MaintenanceProgressData(
    val id: String = "",
    val requestId: String,
    val professionalId: String,
    val description: String,
    val percentage: Int,
    val mediaUrls: List<String> = emptyList(),
    val createdAt: String
)

data class MaintenanceCostData(
    val id: String = "",
    val requestId: String,
    val category: String,
    val description: String,
    val amount: Double,
    val receiptUrl: String?,
    val createdAt: String
)

data class MaintenanceMessageData(
    val id: String,
    val requestId: String,
    val senderId: String,
    val senderName: String?,
    val message: String,
    val createdAt: String,
    val read: Boolean
)
