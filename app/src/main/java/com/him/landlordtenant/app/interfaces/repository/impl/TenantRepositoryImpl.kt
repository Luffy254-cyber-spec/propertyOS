package com.him.landlordtenant.app.interfaces.repository.impl

import android.util.Log
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
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TenantRepositoryImpl @Inject constructor(
    private val tenantDao: TenantDao,
    private val userDao: UserDao,
    private val maintenanceDao: MaintenanceDao,
    private val firebaseDataSource: FirebaseDataSource,
    private val networkHelper: NetworkHelper,
    private val chatRepository: ChatRepository,
    private val propertyRepository: PropertyRepository
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
            val userResult = firebaseDataSource.readData("users/$tenantId", com.him.landlordtenant.app.data.model.User::class.java)
            val user = userResult.getOrNull()
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
            val userResult = firebaseDataSource.readData("users/$tenantId", com.him.landlordtenant.app.data.model.User::class.java)
            val user = userResult.getOrNull()
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
            
            // Fetch membership from RTDB
            val membershipResult = firebaseDataSource.readData("memberships/$tenantId", Map::class.java)
            val membership = membershipResult.getOrNull()
            
            val tenancy = if (membership != null) {
                val apartmentId = membership["apartmentId"] as? String ?: ""
                
                // Fetch listing from RTDB
                val apartmentResult = firebaseDataSource.readData("listings/$apartmentId", MarketplacePropertyListingData::class.java)
                val apartment = apartmentResult.getOrNull()
                
                val landlordId = apartment?.ownerId ?: ""
                val landlordUser = if (landlordId.isNotEmpty()) {
                    firebaseDataSource.readData("users/$landlordId", com.him.landlordtenant.app.data.model.User::class.java).getOrNull()
                } else null
                
                TenancyData(
                    id = tenantId, // Simplified for RTDB structure if memberships are keyed by tenantId
                    propertyId = apartmentId,
                    propertyName = apartment?.title ?: "Apartment",
                    unitName = membership["houseNumber"] as? String ?: "N/A",
                    landlordId = landlordId,
                    landlordName = landlordUser?.fullName ?: "Landlord",
                    landlordPhone = landlordUser?.phoneNumber ?: "N/A",
                    startDate = membership["createdAt"]?.toString() ?: "",
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
            firebaseDataSource.writeData("maintenance_requests/$id", data)
        } else {
            // TODO: Schedule WorkManager sync
        }
        
        Result.success(id)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getMaintenanceRequests(tenantId: String): Result<List<MaintenanceSummaryData>> = try {
        if (networkHelper.isNetworkAvailable()) {
            val result = firebaseDataSource.readData("maintenance_requests", Map::class.java)
            val allRequests = result.getOrNull() as? Map<String, Map<String, Any>>
            
            val remoteRequests = allRequests?.values?.filter { it["tenantId"] == tenantId }?.map { doc ->
                MaintenanceSummaryData(
                    id = doc["id"]?.toString() ?: "",
                    propertyId = "",
                    propertyName = null,
                    unitId = null,
                    unitName = null,
                    tenantId = tenantId,
                    tenantName = null,
                    title = doc["title"]?.toString() ?: "",
                    category = doc["category"]?.toString() ?: "",
                    priority = doc["priority"]?.toString() ?: "",
                    status = doc["status"]?.toString() ?: "PENDING",
                    assignedProfessionalId = null,
                    createdAt = doc["createdAt"]?.toString() ?: ""
                )
            } ?: emptyList()
            
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

    override suspend fun joinApartment(tenantId: String, apartmentId: String, signature: String?, nationalIdUrl: String?): Result<Unit> = try {
        val membershipId = "MEM_${tenantId}_${apartmentId}"
        val membership = mutableMapOf(
            "id" to membershipId,
            "tenantId" to tenantId,
            "apartmentId" to apartmentId,
            "status" to "JOINED", 
            "createdAt" to System.currentTimeMillis()
        )
        
        signature?.let { membership["signature"] = it }
        nationalIdUrl?.let { membership["nationalIdUrl"] = it }

        firebaseDataSource.writeData("memberships/$tenantId", membership)
        
        // Mark user as having an active membership in the profile
        firebaseDataSource.writeData("users/$tenantId/currentApartmentId", apartmentId)

        // Create activity for landlord
        try {
            propertyRepository.getProperty(apartmentId).onSuccess { prop ->
                val activityId = "act_${System.currentTimeMillis()}"
                val activity = mapOf(
                    "id" to activityId,
                    "title" to "New Tenant Application",
                    "subtitle" to "A tenant joined ${prop.name}",
                    "time" to "Just now",
                    "type" to "TENANT"
                )
                firebaseDataSource.writeData("activities/${prop.ownerId}/$activityId", activity)
            }
        } catch (e: Exception) {
            Log.e("TenantRepository", "Failed to create activity", e)
        }

        // Automatic addition to community groups
        try {
            propertyRepository.getProperty(apartmentId).onSuccess { property ->
                chatRepository.getOrCreateCommunityConversation(
                    apartmentId = apartmentId,
                    landlordId = property.ownerId,
                    apartmentName = property.name
                )
                chatRepository.getOrCreateTenantGroup(
                    apartmentId = apartmentId,
                    landlordId = property.ownerId,
                    apartmentName = property.name
                )
                chatRepository.addTenantToCommunityGroup(tenantId, apartmentId)
                
                // Creative System Welcome
                chatRepository.sendSystemMessage("tenant_$apartmentId", "🌟 A new neighbor, ${tenantId.take(4)}, has arrived! Let's make them feel at home.")
            }
        } catch (e: Exception) {
            Log.e("TenantRepository", "Failed to add tenant to community groups", e)
        }

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun pickHouse(tenantId: String, apartmentId: String, houseId: String): Result<Unit> = try {
        // Update membership with houseId
        firebaseDataSource.writeData("memberships/$tenantId/houseId", houseId)
        
        // Also update unit status to OCCUPIED in property management
        val snapshot = firebaseDataSource.getReference("properties/$apartmentId/floors").get().await()
        var houseNumber = houseId
        snapshot.children.forEach { floorSnapshot ->
            val unitRef = floorSnapshot.child("units/$houseId")
            if (unitRef.exists()) {
                houseNumber = unitRef.child("number").getValue(String::class.java) ?: houseId
                unitRef.child("status").ref.setValue("OCCUPIED")
            }
        }

        // Post system message in community chat
        val tenant = userDao.getById(tenantId)
        chatRepository.sendSystemMessage("tenant_$apartmentId", "🔑 ${tenant?.firstName ?: "A new tenant"} has just moved into Unit $houseNumber! Welcome home!")

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
