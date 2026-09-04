package com.him.landlordtenant.app.interfaces.repository.impl

import com.him.landlordtenant.app.data.remote.FirestoreDataSource
import com.him.landlordtenant.app.data.remote.FirebaseDataSource
import com.him.landlordtenant.app.interfaces.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AgreementRepositoryImpl @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource,
    private val firebaseDataSource: FirebaseDataSource
) : AgreementRepository {

    override suspend fun createAgreement(createdBy: String, agreement: AgreementCreateData): Result<String> = try {
        val id = firestoreDataSource.collection("agreements").document().id
        
        val fullAgreement = mapOf(
            "id" to id,
            "landlordId" to agreement.landlordId,
            "tenantId" to agreement.tenantId,
            "propertyId" to agreement.propertyId,
            "unitId" to agreement.unitId,
            "startDate" to agreement.startDate,
            "monthlyRent" to agreement.monthlyRent,
            "securityDeposit" to agreement.securityDeposit,
            "rules" to agreement.rules,
            "status" to "DRAFT",
            "createdAt" to System.currentTimeMillis(),
            "agreementProofUrl" to agreement.agreementProofUrl
        )

        // Try Firestore first
        val firestoreResult = firestoreDataSource.saveData("agreements", id, fullAgreement)
        
        // Also try Realtime Database
        val rtdbResult = try {
            firebaseDataSource.writeData("agreements/$id", fullAgreement)
        } catch (e: Exception) {
            Result.failure(e)
        }
        
        if (firestoreResult.isSuccess || rtdbResult.isSuccess) {
            Result.success(id)
        } else {
            Result.failure(firestoreResult.exceptionOrNull() ?: Exception("Failed to save agreement"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getAgreement(agreementId: String): Result<AgreementDetailsData> {
        // Try Realtime Database first
        val snapshot = firebaseDataSource.getReference("agreements/$agreementId").get().await()
        val agreement = snapshot.getValue(AgreementDetailsData::class.java)
        
        if (agreement != null) {
            return Result.success(agreement)
        }
        
        return firestoreDataSource.getData("agreements", agreementId, AgreementDetailsData::class.java)
            .map { it ?: throw Exception("Agreement not found") }
    }

    override fun observeAgreement(agreementId: String): Flow<Result<AgreementDetailsData>> = flow {
        // Simple mock for now as FirestoreDataSource doesn't have observe method
        emit(getAgreement(agreementId))
    }

    override suspend fun updateAgreement(userId: String, agreementId: String, agreement: AgreementCreateData): Result<Unit> {
        return firestoreDataSource.saveData("agreements", agreementId, agreement)
    }

    override suspend fun deleteDraft(userId: String, agreementId: String): Result<Unit> {
        return firestoreDataSource.deleteData("agreements", agreementId)
    }

    override suspend fun getUserAgreements(userId: String): Result<List<ContractAgreementSummaryData>> {
        return Result.failure(NotImplementedError("Query not implemented in FirestoreDataSource"))
    }

    override fun observeUserAgreements(userId: String): Flow<Result<List<AgreementSummaryData>>> = flow {
        emit(Result.failure(NotImplementedError()))
    }

    override suspend fun getPropertyAgreements(propertyId: String): Result<List<AgreementSummaryData>> = try {
        val snapshot = firebaseDataSource.getReference("agreements")
            .orderByChild("propertyId")
            .equalTo(propertyId)
            .get()
            .await()
        
        val agreements = snapshot.children.mapNotNull { it.getValue(AgreementSummaryData::class.java) }
        Result.success(agreements)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getTenantAgreements(tenantId: String): Result<List<AgreementSummaryData>> = try {
        val snapshot = firebaseDataSource.getReference("agreements")
            .orderByChild("tenantId")
            .equalTo(tenantId)
            .get()
            .await()
        
        val agreements = snapshot.children.mapNotNull { it.getValue(AgreementSummaryData::class.java) }
        Result.success(agreements)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getLandlordAgreements(landlordId: String): Result<List<ContractAgreementSummaryData>> = try {
        val snapshot = firebaseDataSource.getReference("agreements")
            .orderByChild("landlordId")
            .equalTo(landlordId)
            .get()
            .await()
        
        val agreements = snapshot.children.mapNotNull { it.getValue(ContractAgreementSummaryData::class.java) }
        Result.success(agreements)
    } catch (e: Exception) {
        Result.failure(e)
    }
    override suspend fun getTemplates(): Result<List<AgreementTemplateData>> = try {
        val snapshot = firebaseDataSource.getReference("agreement_templates").get().await()
        val templates = snapshot.children.mapNotNull { it.getValue(AgreementTemplateData::class.java) }
        
        if (templates.isNotEmpty()) {
            Result.success(templates)
        } else {
            // Default template if nothing in DB
            Result.success(listOf(
                AgreementTemplateData(
                    id = "standard_lease",
                    name = "Standard Residential Lease",
                    description = "A standard residential tenancy agreement covering all legal bases.",
                    version = "1.0",
                    clauses = listOf(
                        AgreementClauseData("1", "RENT", "Rent is due on the 1st of every month."),
                        AgreementClauseData("2", "DEPOSIT", "A security deposit equal to one month's rent is required."),
                        AgreementClauseData("3", "MAINTENANCE", "Tenant is responsible for general cleanliness."),
                        AgreementClauseData("4", "TERMINATION", "One month's notice is required before vacating.")
                    )
                )
            ))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    override suspend fun getTemplate(templateId: String): Result<AgreementTemplateData> = try {
        getTemplates().map { list ->
            list.find { it.id == templateId } ?: throw Exception("Template not found")
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    override suspend fun createFromTemplate(createdBy: String, templateId: String, agreement: AgreementCreateData): Result<String> = try {
        val template = getTemplate(templateId).getOrThrow()
        val combinedRules = (template.clauses.map { it.content } + agreement.rules).distinct()
        createAgreement(createdBy, agreement.copy(rules = combinedRules))
    } catch (e: Exception) {
        Result.failure(e)
    }
    override suspend fun getClauses(agreementId: String): Result<List<AgreementClauseData>> = Result.failure(NotImplementedError())
    override suspend fun addClause(userId: String, agreementId: String, clause: AgreementClauseData): Result<String> = Result.failure(NotImplementedError())
    override suspend fun updateClause(userId: String, agreementId: String, clauseId: String, clause: AgreementClauseData): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun removeClause(userId: String, agreementId: String, clauseId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun inviteTenant(landlordId: String, agreementId: String, tenantId: String): Result<Unit> = try {
        val updates = mapOf(
            "tenantId" to tenantId,
            "status" to "PENDING_TENANT_SIGNATURE",
            "invitedAt" to System.currentTimeMillis()
        )
        firebaseDataSource.getReference("agreements/$agreementId").updateChildren(updates).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
    override suspend fun acceptInvitation(tenantId: String, agreementId: String): Result<Unit> = try {
        val updates = mapOf(
            "status" to "TENANT_ACCEPTED",
            "acceptedAt" to System.currentTimeMillis()
        )
        firebaseDataSource.getReference("agreements/$agreementId").updateChildren(updates).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun rejectInvitation(tenantId: String, agreementId: String, reason: String?): Result<Unit> = try {
        val updates = mapOf(
            "status" to "TENANT_REJECTED",
            "rejectionReason" to reason,
            "rejectedAt" to System.currentTimeMillis()
        )
        firebaseDataSource.getReference("agreements/$agreementId").updateChildren(updates).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
    override suspend fun signAsTenant(tenantId: String, agreementId: String, signature: ContractDigitalSignatureData): Result<Unit> = try {
        val ref = firebaseDataSource.getReference("agreements/$agreementId/signatures/tenant")
        ref.setValue(signature).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signAsLandlord(landlordId: String, agreementId: String, signature: ContractDigitalSignatureData): Result<Unit> = try {
        val ref = firebaseDataSource.getReference("agreements/$agreementId/signatures/landlord")
        ref.setValue(signature).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
    override suspend fun getSignatureStatus(agreementId: String): Result<AgreementSignatureStatusData> = Result.failure(NotImplementedError())
    override suspend fun withdrawSignatureRequest(userId: String, agreementId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun approveAgreement(landlordId: String, agreementId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun activateAgreement(agreementId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun canActivateAgreement(agreementId: String): Result<Boolean> = Result.failure(NotImplementedError())
    override suspend fun generatePdf(agreementId: String): Result<String> = Result.failure(NotImplementedError())
    override suspend fun getAgreementDocument(agreementId: String): Result<AgreementDocumentData?> = Result.failure(NotImplementedError())
    override suspend fun regenerateDocument(agreementId: String): Result<String> = Result.failure(NotImplementedError())
    override suspend fun getDocumentUrl(agreementId: String): Result<String> = Result.failure(NotImplementedError())
    override suspend fun requestRenewal(requesterId: String, agreementId: String): Result<String> = Result.failure(NotImplementedError())
    override suspend fun approveRenewal(landlordId: String, agreementId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun rejectRenewal(landlordId: String, agreementId: String, reason: String?): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun renewAgreement(userId: String, agreementId: String, renewal: AgreementRenewalData): Result<String> = Result.failure(NotImplementedError())
    override suspend fun getExpiringAgreements(userId: String, days: Int): Result<List<AgreementSummaryData>> = Result.failure(NotImplementedError())
    override suspend fun getExpiredAgreements(userId: String): Result<List<AgreementSummaryData>> = Result.failure(NotImplementedError())
    override suspend fun processExpiredAgreements(): Result<Int> = Result.failure(NotImplementedError())
    override suspend fun requestTermination(requesterId: String, agreementId: String, request: AgreementTerminationData): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun approveTermination(landlordId: String, agreementId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun rejectTermination(landlordId: String, agreementId: String, reason: String?): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun terminateAgreement(userId: String, agreementId: String, reason: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun getAuditTrail(agreementId: String): Result<List<AgreementAuditData>> = Result.failure(NotImplementedError())
    override fun observeAuditTrail(agreementId: String): Flow<Result<List<AgreementAuditData>>> = flow { emit(Result.failure(NotImplementedError())) }
    override suspend fun getAgreementsRequiringAction(userId: String): Result<List<AgreementSummaryData>> = Result.failure(NotImplementedError())
    override suspend fun sendExpiryReminders(daysBeforeExpiry: Int): Result<Int> = Result.failure(NotImplementedError())
    override suspend fun sendSignatureReminders(): Result<Int> = Result.failure(NotImplementedError())
}
