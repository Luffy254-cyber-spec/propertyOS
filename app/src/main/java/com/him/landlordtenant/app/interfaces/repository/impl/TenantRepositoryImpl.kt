package com.him.landlordtenant.app.interfaces.repository.impl

import com.him.landlordtenant.app.data.dao.MaintenanceDao
import com.him.landlordtenant.app.data.dao.TenantDao
import com.him.landlordtenant.app.data.dao.UserDao
import com.him.landlordtenant.app.data.entities.toDomain
import com.him.landlordtenant.app.data.entities.toEntity
import com.him.landlordtenant.app.data.model.*
import com.him.landlordtenant.app.data.remote.FirebaseDataSource
import com.him.landlordtenant.app.data.remote.FirestoreDataSource
import com.him.landlordtenant.app.interfaces.*
import com.him.landlordtenant.app.util.NetworkHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TenantRepositoryImpl @Inject constructor(
    private val tenantDao: TenantDao,
    private val userDao: UserDao,
    private val maintenanceDao: MaintenanceDao,
    private val firestoreDataSource: FirestoreDataSource,
    private val firebaseDataSource: FirebaseDataSource,
    private val networkHelper: NetworkHelper
) : TenantRepository {

    override fun getTenants(): Flow<List<Tenant>> {
        return tenantDao.getAll().map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getTenantById(id: String): Tenant? {
        return tenantDao.getById(id)?.toDomain()
    }

    override suspend fun saveTenant(tenant: Tenant): Result<Unit> {
        return try {
            tenantDao.insert(tenant.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTenant(tenant: Tenant): Result<Unit> {
        return try {
            tenantDao.delete(tenant.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeTenantProfile(tenantId: String): Flow<Result<TenantProfileData>> = flow {
        try {
            val snapshot = firestoreDataSource.collection("users").document(tenantId).get().await()
            val user = snapshot.toObject(com.him.landlordtenant.app.data.model.User::class.java)
            if (user != null) {
                emit(Result.success(TenantProfileData(
                    id = user.id,
                    fullName = user.fullName,
                    email = user.email ?: "",
                    phoneNumber = user.phoneNumber,
                    profilePhotoUrl = user.profileImageUrl,
                    nationalIdVerified = false
                )))
            } else {
                emit(Result.failure(Exception("User not found")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun getTenantProfile(tenantId: String): Result<TenantProfileData> = try {
        if (!networkHelper.isNetworkAvailable()) {
            val user = userDao.getById(tenantId)
            if (user != null) {
                Result.success(TenantProfileData(
                    id = user.id,
                    fullName = "${user.firstName} ${user.lastName}",
                    email = user.email ?: "",
                    phoneNumber = user.phoneNumber,
                    profilePhotoUrl = user.profileImageUrl,
                    nationalIdVerified = false
                ))
            } else {
                Result.failure(Exception("Offline: Profile not in cache"))
            }
        } else {
            val snapshot = firestoreDataSource.collection("users").document(tenantId).get().await()
            val user = snapshot.toObject(com.him.landlordtenant.app.data.model.User::class.java)
            if (user != null) {
                // Update cache
                userDao.insert(user.toEntity())
                Result.success(TenantProfileData(
                    id = user.id,
                    fullName = user.fullName,
                    email = user.email ?: "",
                    phoneNumber = user.phoneNumber,
                    profilePhotoUrl = user.profileImageUrl,
                    nationalIdVerified = false
                ))
            } else {
                Result.failure(Exception("User not found"))
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getDashboard(tenantId: String): Result<TenantDashboardData> = try {
        if (!networkHelper.isNetworkAvailable()) {
            // Offline: Construct dashboard from local cache
            val profile = getTenantProfile(tenantId).getOrThrow()
            val maintenance = getMaintenanceRequests(tenantId).getOrNull() ?: emptyList()
            
            // For TenancyData and RentBalance, we could check TenantEntity
            val tenantEntity = tenantDao.getById(tenantId)
            val tenancy = if (tenantEntity != null && tenantEntity.currentApartmentId != null) {
                TenancyData(
                    id = tenantEntity.id,
                    propertyId = tenantEntity.currentApartmentId ?: "",
                    propertyName = "My Apartment (Cached)",
                    unitName = tenantEntity.currentHouseId ?: "N/A",
                    landlordId = tenantEntity.currentLandlordId ?: "",
                    landlordName = "Landlord",
                    landlordPhone = "N/A",
                    startDate = tenantEntity.leaseStartDate ?: "",
                    endDate = tenantEntity.leaseEndDate,
                    monthlyRent = tenantEntity.monthlyRent
                )
            } else null

            Result.success(TenantDashboardData(
                profile = profile,
                tenancy = tenancy,
                rentBalance = RentBalanceData(
                    monthlyRent = tenancy?.monthlyRent ?: 0.0,
                    amountPaid = tenantEntity?.totalRentPaid ?: 0.0,
                    outstandingAmount = tenantEntity?.outstandingRent ?: 0.0,
                    arrearsAmount = 0.0,
                    dueDate = "N/A"
                ),
                outstandingBills = emptyList(),
                recentPayments = emptyList(),
                maintenanceRequests = maintenance,
                notifications = emptyList(),
                unreadMessages = 0
            ))
        } else {
            val profile = getTenantProfile(tenantId).getOrThrow()
            
            val membershipSnapshot = firestoreDataSource.collection("memberships")
                .whereEqualTo("tenantId", tenantId)
                .whereEqualTo("status", "ACTIVE")
                .limit(1)
                .get()
                .await()
            
            val membership = membershipSnapshot.documents.firstOrNull()
            
            val tenancy = if (membership != null) {
                val apartmentId = membership.getString("apartmentId") ?: ""
                val apartmentSnapshot = firestoreDataSource.collection("listings").document(apartmentId).get().await()
                val apartment = apartmentSnapshot.toObject(MarketplacePropertyListingData::class.java)
                
                val landlordId = apartment?.ownerId ?: ""
                val landlordUser = if (landlordId.isNotEmpty()) {
                    // Try RTDB first (Landlords)
                    val rtUser = firebaseDataSource.readData("users/$landlordId", com.him.landlordtenant.app.data.model.User::class.java).getOrNull()
                    if (rtUser != null) rtUser else {
                        // Fallback to Firestore
                        val fsDoc = firestoreDataSource.collection("users").document(landlordId).get().await()
                        fsDoc.toObject(com.him.landlordtenant.app.data.model.User::class.java)
                    }
                } else null
                
                TenancyData(
                    id = membership.id,
                    propertyId = apartmentId,
                    propertyName = apartment?.title ?: "Apartment",
                    unitName = membership.getString("houseNumber") ?: "N/A",
                    landlordId = landlordId,
                    landlordName = landlordUser?.fullName ?: "Landlord",
                    landlordPhone = landlordUser?.phoneNumber ?: "N/A",
                    startDate = membership.getString("createdAt") ?: "",
                    endDate = null,
                    monthlyRent = apartment?.monthlyRent ?: 0.0
                )
            } else null

            Result.success(TenantDashboardData(
                profile = profile,
                tenancy = tenancy,
                rentBalance = RentBalanceData(
                    monthlyRent = tenancy?.monthlyRent ?: 0.0,
                    amountPaid = 0.0,
                    outstandingAmount = 0.0,
                    arrearsAmount = 0.0,
                    dueDate = "1st"
                ),
                outstandingBills = emptyList(),
                recentPayments = emptyList(),
                maintenanceRequests = emptyList(),
                notifications = emptyList(),
                unreadMessages = 0
            ))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateTenantProfile(tenantId: String, profile: TenantProfileData): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun updateProfilePhoto(tenantId: String, filePath: String): Result<String> = Result.failure(NotImplementedError())
    override suspend fun removeProfilePhoto(tenantId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun getCurrentTenancy(tenantId: String): Result<TenancyData?> = Result.failure(NotImplementedError())
    override suspend fun requestMoveOut(tenantId: String, tenancyId: String, reason: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun getRentBalance(tenantId: String): Result<RentBalanceData> = Result.failure(NotImplementedError())
    override suspend fun getRentPaymentHistory(tenantId: String): Result<List<PaymentSummaryData>> = Result.failure(NotImplementedError())
    override suspend fun getBills(tenantId: String): Result<List<BillSummaryData>> = Result.failure(NotImplementedError())
    override suspend fun getActiveAgreement(tenantId: String): Result<AgreementSummaryData?> = Result.failure(NotImplementedError())
    override suspend fun signAgreement(tenantId: String, agreementId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun createMaintenanceRequest(tenantId: String, request: MaintenanceRequestData): Result<String> = try {
        val id = "MNT_${System.currentTimeMillis()}"
        
        val newRequest = MaintenanceRequest(
            id = id,
            tenantId = tenantId,
            title = request.title,
            description = request.description,
            category = MaintenanceCategory.valueOf(request.category.uppercase()),
            priority = MaintenancePriority.valueOf(request.priority.uppercase()),
            status = MaintenanceStatus.SUBMITTED,
            createdAt = System.currentTimeMillis().toString(),
            syncStatus = if (networkHelper.isNetworkAvailable()) SyncStatus.SYNCED else SyncStatus.PENDING
        )

        // Save locally first
        maintenanceDao.insert(newRequest.toEntity())

        if (networkHelper.isNetworkAvailable()) {
            val data = mapOf(
                "id" to id,
                "tenantId" to tenantId,
                "title" to request.title,
                "description" to request.description,
                "category" to request.category,
                "priority" to request.priority,
                "status" to "SUBMITTED",
                "createdAt" to System.currentTimeMillis()
            )
            firestoreDataSource.saveData("maintenance_requests", id, data)
        } else {
            // TODO: Schedule WorkManager sync
        }
        
        Result.success(id)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getMaintenanceRequests(tenantId: String): Result<List<MaintenanceSummaryData>> = try {
        if (networkHelper.isNetworkAvailable()) {
            val snapshot = firestoreDataSource.collection("maintenance_requests")
                .whereEqualTo("tenantId", tenantId)
                .get()
                .await()
            
            val remoteRequests = snapshot.documents.map { doc ->
                MaintenanceSummaryData(
                    id = doc.id,
                    propertyId = "",
                    propertyName = null,
                    unitId = null,
                    unitName = null,
                    tenantId = tenantId,
                    tenantName = null,
                    title = doc.getString("title") ?: "",
                    category = doc.getString("category") ?: "",
                    priority = doc.getString("priority") ?: "",
                    status = doc.getString("status") ?: "PENDING",
                    assignedProfessionalId = null,
                    createdAt = doc.getLong("createdAt")?.toString() ?: ""
                )
            }
            
            // Update local cache? (Optional but good for offline viewing later)
            // For now, let's just return remote
            Result.success(remoteRequests)
        } else {
            // Offline: Return from DAO
            val localRequests = maintenanceDao.getByTenantId(tenantId).map { entity ->
                MaintenanceSummaryData(
                    id = entity.id,
                    propertyId = entity.apartmentId,
                    propertyName = null,
                    unitId = entity.houseId,
                    unitName = entity.houseNumber,
                    tenantId = tenantId,
                    tenantName = entity.tenantName,
                    title = entity.title,
                    category = entity.category.displayName,
                    priority = entity.priority.displayName,
                    status = entity.status.displayName,
                    assignedProfessionalId = entity.assignedProfessionalId,
                    createdAt = entity.createdAt ?: ""
                )
            }
            Result.success(localRequests)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    override suspend fun searchProperties(query: String, county: String?, town: String?): Result<List<PropertySearchData>> = Result.failure(NotImplementedError())
    override suspend fun saveProperty(tenantId: String, propertyId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun requestViewing(tenantId: String, propertyId: String, viewing: ViewingRequestData): Result<String> = Result.failure(NotImplementedError())
    override suspend fun searchProfessionals(category: String, location: String?): Result<List<ProfessionalSummaryData>> = Result.failure(NotImplementedError())
    override suspend fun getProfessional(professionalId: String): Result<ProfessionalSummaryData> = Result.failure(NotImplementedError())
    override suspend fun requestProfessional(tenantId: String, professionalId: String, request: ProfessionalRequestData): Result<String> = Result.failure(NotImplementedError())
    override suspend fun getConversations(tenantId: String): Result<List<ConversationSummaryData>> = Result.failure(NotImplementedError())
    override suspend fun getNotifications(tenantId: String): Result<List<NotificationSummaryData>> = Result.failure(NotImplementedError())
    override suspend fun uploadDocument(tenantId: String, filePath: String, documentType: String): Result<String> = Result.failure(NotImplementedError())
    override suspend fun submitEmergencyReport(tenantId: String, emergency: EmergencyReportData): Result<String> = Result.failure(NotImplementedError())

    override suspend fun joinApartment(tenantId: String, apartmentId: String): Result<Unit> = try {
        val membershipId = "MEM_${tenantId}_${apartmentId}"
        val membership = mapOf(
            "id" to membershipId,
            "tenantId" to tenantId,
            "apartmentId" to apartmentId,
            "status" to "JOIN_REQUESTED",
            "createdAt" to System.currentTimeMillis()
        )
        firestoreDataSource.saveData("memberships", membershipId, membership)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
