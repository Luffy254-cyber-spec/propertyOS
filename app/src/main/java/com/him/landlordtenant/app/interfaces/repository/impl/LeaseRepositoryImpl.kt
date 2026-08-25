package com.him.landlordtenant.app.interfaces.repository.impl

import com.him.landlordtenant.app.data.remote.FirestoreDataSource
import com.him.landlordtenant.app.interfaces.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LeaseRepositoryImpl @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource
) : LeaseRepository {

    override suspend fun getLease(leaseId: String): Result<LeaseDetailsData> {
        return firestoreDataSource.getData("leases", leaseId, LeaseDetailsData::class.java)
            .map { it ?: throw Exception("Lease not found") }
    }

    override fun observeLease(leaseId: String): Flow<Result<LeaseDetailsData>> = flow {
        emit(getLease(leaseId))
    }

    override suspend fun getTenantLeases(tenantId: String): Result<List<LeaseSummaryData>> = Result.failure(NotImplementedError())

    override suspend fun getActiveLease(tenantId: String): Result<LeaseDetailsData?> = Result.failure(NotImplementedError())

    override suspend fun createLease(actorId: String, request: CreateLeaseData): Result<String> {
        val id = firestoreDataSource.collection("leases").document().id
        return firestoreDataSource.saveData("leases", id, request).map { id }
    }

    override suspend fun signLease(actorId: String, leaseId: String, signatureUrl: String): Result<Unit> = Result.failure(NotImplementedError())

    override suspend fun terminateLease(actorId: String, data: LeaseTerminationData): Result<Unit> = Result.failure(NotImplementedError())

    override suspend fun getMoveOutRequest(leaseId: String): Result<MoveOutRequestData?> = Result.failure(NotImplementedError())

    override suspend fun requestMoveOut(actorId: String, leaseId: String, request: MoveOutRequestData): Result<String> = Result.failure(NotImplementedError())

    override suspend fun cancelMoveOut(actorId: String, leaseId: String): Result<Unit> = Result.failure(NotImplementedError())

    override suspend fun getInspections(leaseId: String): Result<List<InspectionData>> = Result.failure(NotImplementedError())

    override suspend fun scheduleInspection(actorId: String, leaseId: String, schedule: InspectionScheduleData): Result<String> = Result.failure(NotImplementedError())

    override suspend fun getLeaseHistory(propertyId: String): Result<List<LeaseSummaryData>> = Result.failure(NotImplementedError())

    override suspend fun getActiveTenantLease(tenantId: String): Result<LeaseDetailsData?> = Result.failure(NotImplementedError())

    override suspend fun getExpiringLeases(landlordId: String, days: Int): Result<List<LeaseSummaryData>> = Result.failure(NotImplementedError())
}
