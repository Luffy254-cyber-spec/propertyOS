package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * INSPECTION REPOSITORY
 * =============================================================
 */

interface InspectionRepository {

    suspend fun createInspection(
        actorId: String,
        inspection: CreateInspectionData
    ): Result<String>

    suspend fun updateInspection(
        actorId: String,
        inspectionId: String,
        update: UpdateInspectionData
    ): Result<Unit>

    suspend fun getInspection(
        inspectionId: String
    ): Result<InspectionDetailsData>

    fun observeInspection(
        inspectionId: String
    ): Flow<Result<InspectionDetailsData>>

    suspend fun getPropertyInspections(
        propertyId: String
    ): Result<List<InspectionSummaryData>>

    suspend fun getUnitInspections(
        unitId: String
    ): Result<List<InspectionSummaryData>>

    suspend fun startInspection(
        actorId: String,
        inspectionId: String
    ): Result<Unit>

    suspend fun completeInspection(
        actorId: String,
        inspectionId: String,
        signatureUrl: String?
    ): Result<Unit>

    suspend fun cancelInspection(
        actorId: String,
        inspectionId: String,
        reason: String
    ): Result<Unit>

    suspend fun saveInspectionItem(
        actorId: String,
        inspectionId: String,
        item: InspectionItemData
    ): Result<Unit>

    suspend fun deleteInspectionItem(
        actorId: String,
        inspectionId: String,
        itemId: String
    ): Result<Unit>

    suspend fun getInspectionReport(
        inspectionId: String
    ): Result<InspectionReportData>

    suspend fun generateInspectionPdf(
        inspectionId: String
    ): Result<String>

    suspend fun scheduleInspection(
        actorId: String,
        schedule: InspectionScheduleData
    ): Result<String>
}

data class UpdateInspectionData(
    val scheduledDate: String?,
    val notes: String?,
    val status: InspectionStatus?
)

data class InspectionSummaryData(
    val id: String,
    val propertyId: String,
    val unitId: String,
    val leaseId: String?,
    val type: InspectionType,
    val scheduledDate: String,
    val status: InspectionStatus,
    val overallCondition: PropertyCondition?,
    val createdAt: String
)

data class InspectionDetailsData(
    val id: String,
    val propertyId: String,
    val unitId: String,
    val leaseId: String?,
    val type: InspectionType,
    val scheduledDate: String,
    val completedDate: String?,
    val status: InspectionStatus,
    val items: List<InspectionItemData>,
    val report: InspectionReportData?,
    val photos: List<InspectionMediaData>
)

data class InspectionMediaData(
    val id: String,
    val inspectionId: String,
    val areaId: String?,
    val itemId: String?,
    val type: String,
    val url: String,
    val thumbnailUrl: String?,
    val caption: String?,
    val createdAt: String
)
