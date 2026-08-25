package com.him.landlordtenant.app.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * =============================================================
 * LEASE REPOSITORY
 * =============================================================
 */

interface LeaseRepository {

    suspend fun getLease(leaseId: String): Result<LeaseDetailsData>

    fun observeLease(leaseId: String): Flow<Result<LeaseDetailsData>>

    suspend fun getTenantLeases(tenantId: String): Result<List<LeaseSummaryData>>

    suspend fun getActiveLease(tenantId: String): Result<LeaseDetailsData?>

    suspend fun createLease(actorId: String, request: CreateLeaseData): Result<String>

    suspend fun signLease(actorId: String, leaseId: String, signatureUrl: String): Result<Unit>

    suspend fun terminateLease(actorId: String, data: LeaseTerminationData): Result<Unit>

    suspend fun getMoveOutRequest(leaseId: String): Result<MoveOutRequestData?>

    suspend fun requestMoveOut(actorId: String, leaseId: String, request: MoveOutRequestData): Result<String>

    suspend fun cancelMoveOut(actorId: String, leaseId: String): Result<Unit>

    suspend fun getInspections(leaseId: String): Result<List<InspectionData>>

    suspend fun scheduleInspection(actorId: String, leaseId: String, schedule: InspectionScheduleData): Result<String>

    suspend fun getLeaseHistory(propertyId: String): Result<List<LeaseSummaryData>>

    suspend fun getActiveTenantLease(tenantId: String): Result<LeaseDetailsData?>
    
    suspend fun getExpiringLeases(landlordId: String, days: Int): Result<List<LeaseSummaryData>>
}

data class LeaseSummaryData(
    val id: String,
    val apartmentName: String,
    val houseNumber: String,
    val startDate: String,
    val endDate: String?,
    val status: String
)

data class LeaseDetailsData(
    val id: String,
    val tenantId: String,
    val tenantName: String,
    val propertyId: String,
    val propertyName: String,
    val unitId: String,
    val unitName: String,
    val startDate: String,
    val endDate: String?,
    val monthlyRent: Double,
    val securityDeposit: Double,
    val status: String,
    val agreementUrl: String?,
    val createdAt: String
)

data class MoveOutRequestData(
    val intendedDate: String,
    val reason: String,
    val createdAt: String = "",
    val status: String = "REQUESTED"
)
