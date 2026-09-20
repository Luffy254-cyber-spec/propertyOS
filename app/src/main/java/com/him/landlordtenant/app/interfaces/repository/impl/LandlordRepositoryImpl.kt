package com.him.landlordtenant.app.interfaces.repository.impl

import com.him.landlordtenant.app.data.remote.FirebaseDataSource
import com.him.landlordtenant.app.data.remote.FirestoreDataSource
import com.him.landlordtenant.app.interfaces.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.toObjects

class LandlordRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource,
    private val firestoreDataSource: FirestoreDataSource
) : LandlordRepository {

    override suspend fun getLandlordProfile(landlordId: String): Result<LandlordProfileData> {
        return firebaseDataSource.readData("landlords/$landlordId", LandlordProfileData::class.java)
            .map { it ?: throw Exception("Landlord not found") }
    }

    override fun observeLandlordProfile(landlordId: String): Flow<Result<LandlordProfileData>> = flow {
        emit(getLandlordProfile(landlordId))
    }

    override suspend fun updateLandlordProfile(landlordId: String, profile: LandlordProfileData): Result<Unit> {
        return firebaseDataSource.writeData("landlords/$landlordId", profile)
    }

    override suspend fun getProperties(landlordId: String): Result<List<PropertyData>> = try {
        val snapshot = firebaseDataSource.getReference("properties")
            .orderByChild("ownerId")
            .equalTo(landlordId)
            .get()
            .await()
        val list = snapshot.children.mapNotNull { it.getValue(PropertyData::class.java) }
        Result.success(list)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun observeProperties(landlordId: String): Flow<Result<List<PropertyData>>> = flow {
        emit(getProperties(landlordId))
    }

    // Stub remaining methods
    override suspend fun updateProfilePhoto(landlordId: String, filePath: String): Result<String> = Result.failure(NotImplementedError())
    override suspend fun createProperty(landlordId: String, property: PropertyData): Result<String> = Result.failure(NotImplementedError())
    override suspend fun updateProperty(landlordId: String, propertyId: String, property: PropertyData): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun deleteProperty(landlordId: String, propertyId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun getProperty(landlordId: String, propertyId: String): Result<PropertyData> = Result.failure(NotImplementedError())
    override suspend fun publishProperty(landlordId: String, propertyId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun unpublishProperty(landlordId: String, propertyId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun markPropertyAsAvailable(landlordId: String, propertyId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun markPropertyAsOccupied(landlordId: String, propertyId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun uploadPropertyImage(landlordId: String, propertyId: String, filePath: String): Result<String> = Result.failure(NotImplementedError())
    override suspend fun uploadPropertyVideo(landlordId: String, propertyId: String, filePath: String): Result<String> = Result.failure(NotImplementedError())
    override suspend fun deletePropertyMedia(landlordId: String, propertyId: String, mediaId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun createUnit(landlordId: String, propertyId: String, unit: UnitData): Result<String> = Result.failure(NotImplementedError())
    override suspend fun updateUnit(landlordId: String, propertyId: String, unitId: String, unit: UnitData): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun deleteUnit(landlordId: String, propertyId: String, unitId: String): Result<Unit> = Result.failure(NotImplementedError())
    
    override suspend fun getUnits(landlordId: String, propertyId: String): Result<List<UnitData>> = try {
        val snapshot = firebaseDataSource.getReference("properties/$propertyId/units").get().await()
        val list = snapshot.children.mapNotNull { child ->
            child.getValue(PropertyUnitData::class.java)?.let { pu ->
                UnitData(
                    id = pu.id,
                    name = pu.name,
                    floor = pu.floor,
                    bedrooms = pu.bedrooms,
                    bathrooms = pu.bathrooms,
                    monthlyRent = pu.monthlyRent,
                    occupied = !pu.available,
                    tenantId = pu.tenantId
                )
            }
        }
        Result.success(list)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun observeUnits(propertyId: String): Flow<Result<List<UnitData>>> = callbackFlow {
        val ref = firebaseDataSource.getReference("properties/$propertyId/units")
        val listener = object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { child ->
                    child.getValue(PropertyUnitData::class.java)?.let { pu ->
                        UnitData(
                            id = pu.id,
                            name = pu.name,
                            floor = pu.floor,
                            bedrooms = pu.bedrooms,
                            bathrooms = pu.bathrooms,
                            monthlyRent = pu.monthlyRent,
                            occupied = !pu.available,
                            tenantId = pu.tenantId
                        )
                    }
                }
                trySend(Result.success(list))
            }
            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(error.toException()))
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    override fun observeHouse(apartmentId: String, floorId: String, houseId: String): Flow<Result<UnitData>> = callbackFlow {
        val ref = firebaseDataSource.getReference("landlord_units/$apartmentId/$floorId/$houseId")
        val listener = object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val house = snapshot.getValue(com.him.landlordtenant.app.data.model.House::class.java)
                if (house != null) {
                    trySend(Result.success(UnitData(
                        id = house.id,
                        name = house.houseNumber,
                        floor = house.floorId,
                        bedrooms = house.bedrooms,
                        bathrooms = house.bathrooms,
                        monthlyRent = house.monthlyRent,
                        occupied = house.status == com.him.landlordtenant.app.data.model.HouseStatus.OCCUPIED,
                        tenantId = house.tenantId
                    )))
                } else {
                    trySend(Result.failure(Exception("House not found")))
                }
            }
            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(error.toException()))
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    override suspend fun assignTenantToUnit(landlordId: String, tenantId: String, apartmentId: String, floorId: String, houseId: String): Result<Unit> = try {
        // 1. Fetch user and house info
        val userRef = firebaseDataSource.getReference("users/$tenantId").get().await()
        val userName = "${userRef.child("firstName").getValue(String::class.java)} ${userRef.child("lastName").getValue(String::class.java)}"
        
        val houseRef = firebaseDataSource.getReference("landlord_units/$apartmentId/$floorId/$houseId")
        val houseSnapshot = houseRef.get().await()
        val houseNumber = houseSnapshot.child("houseNumber").getValue(String::class.java) ?: houseId
        
        val now = System.currentTimeMillis()
        val dateStr = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())

        // 2. Prepare Updates
        val updates = mutableMapOf<String, Any?>()
        
        // A. Update unit status
        val unitUpdate = mapOf(
            "status" to "OCCUPIED",
            "tenantId" to tenantId,
            "tenantName" to userName,
            "moveInDate" to dateStr
        )
        
        updates["landlord_units/$apartmentId/$floorId/$houseId/status"] = "OCCUPIED"
        updates["landlord_units/$apartmentId/$floorId/$houseId/tenantId"] = tenantId
        updates["landlord_units/$apartmentId/$floorId/$houseId/tenantName"] = userName
        updates["landlord_units/$apartmentId/$floorId/$houseId/moveInDate"] = dateStr
        
        // B. Update public property unit
        updates["properties/$apartmentId/units/$houseId/available"] = false
        updates["properties/$apartmentId/units/$houseId/tenantId"] = tenantId
        
        // C. Update Tenant Summary in Landlord Directory
        updates["tenants_by_landlord/$landlordId/$tenantId/unitId"] = houseId
        updates["tenants_by_landlord/$landlordId/$tenantId/unitName"] = houseNumber
        updates["tenants_by_landlord/$landlordId/$tenantId/tenancyStatus"] = "Active"
        
        // D. Update Tenant Membership
        updates["memberships/$tenantId/houseId"] = houseId
        updates["memberships/$tenantId/houseNumber"] = houseNumber
        updates["memberships/$tenantId/status"] = "JOINED"

        // E. Update Unit Counts (Auto-Tally)
        val propRef = firebaseDataSource.getReference("properties/$apartmentId")
        val propSnapshot = propRef.get().await()
        val available = propSnapshot.child("availableUnits").getValue(Int::class.java) ?: 0
        val occupied = propSnapshot.child("occupiedUnits").getValue(Int::class.java) ?: 0
        
        updates["properties/$apartmentId/availableUnits"] = (available - 1).coerceAtLeast(0)
        updates["properties/$apartmentId/occupiedUnits"] = occupied + 1
        updates["listings/$apartmentId/availableUnits"] = (available - 1).coerceAtLeast(0)
        updates["listings/$apartmentId/occupiedUnits"] = occupied + 1

        firebaseDataSource.getReference("/").updateChildren(updates).await()
        
        // F. Log Activity
        val actId = "act_$now"
        val activity = mapOf(
            "id" to actId,
            "title" to "Tenant Assigned",
            "subtitle" to "$userName assigned to Unit $houseNumber",
            "type" to "TENANT",
            "status" to "SUCCESS",
            "timestamp" to now
        )
        firebaseDataSource.writeData("activities/$landlordId/$actId", activity)
        
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun evictTenantFromUnit(landlordId: String, apartmentId: String, floorId: String, houseId: String): Result<Unit> = try {
        val houseRef = firebaseDataSource.getReference("landlord_units/$apartmentId/$floorId/$houseId")
        val snapshot = houseRef.get().await()
        val tenantId = snapshot.child("tenantId").getValue(String::class.java)
        
        val updates = mutableMapOf<String, Any?>()
        updates["landlord_units/$apartmentId/$floorId/$houseId/status"] = "VACANT"
        updates["landlord_units/$apartmentId/$floorId/$houseId/tenantId"] = null
        updates["landlord_units/$apartmentId/$floorId/$houseId/tenantName"] = null
        updates["landlord_units/$apartmentId/$floorId/$houseId/moveInDate"] = null
        
        updates["properties/$apartmentId/units/$houseId/available"] = true
        updates["properties/$apartmentId/units/$houseId/tenantId"] = null
        
        if (!tenantId.isNullOrEmpty()) {
            updates["tenants_by_landlord/$landlordId/$tenantId/unitId"] = "GENERAL"
            updates["tenants_by_landlord/$landlordId/$tenantId/unitName"] = "N/A"
            
            updates["memberships/$tenantId/houseId"] = "GENERAL"
            updates["memberships/$tenantId/houseNumber"] = "N/A"
        }
        
        // Update Unit Counts (Auto-Tally)
        val propRef = firebaseDataSource.getReference("properties/$apartmentId")
        val propSnapshot = propRef.get().await()
        val available = propSnapshot.child("availableUnits").getValue(Int::class.java) ?: 0
        val occupied = propSnapshot.child("occupiedUnits").getValue(Int::class.java) ?: 0
        
        updates["properties/$apartmentId/availableUnits"] = available + 1
        updates["properties/$apartmentId/occupiedUnits"] = (occupied - 1).coerceAtLeast(0)
        updates["listings/$apartmentId/availableUnits"] = available + 1
        updates["listings/$apartmentId/occupiedUnits"] = (occupied - 1).coerceAtLeast(0)

        firebaseDataSource.getReference("/").updateChildren(updates).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
    override suspend fun getTenants(landlordId: String): Result<List<TenantSummaryData>> = try {
        // Source of truth for active tenants managed by this landlord
        val ref = firebaseDataSource.getReference("tenants_by_landlord/$landlordId")
        val snapshot = ref.get().await()
        
        val tenants = snapshot.children.mapNotNull { child ->
            try {
                child.getValue(TenantSummaryData::class.java)?.let { tenant ->
                    // Ensure ID is present
                    val finalId = if (tenant.id.isEmpty()) child.key ?: "" else tenant.id
                    tenant.copy(id = finalId)
                }
            } catch (e: Exception) {
                null
            }
        }
        
        Result.success(tenants)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun observeTenants(landlordId: String): Flow<Result<List<TenantSummaryData>>> = flow {
        emit(getTenants(landlordId))
    }
    override suspend fun getTenant(landlordId: String, tenantId: String): Result<TenantSummaryData> = try {
        val ref = firebaseDataSource.getReference("tenants_by_landlord/$landlordId/$tenantId")
        val snapshot = ref.get().await()
        val tenant = snapshot.getValue(TenantSummaryData::class.java)
        if (tenant != null) {
            Result.success(tenant.copy(id = if (tenant.id.isEmpty()) tenantId else tenant.id))
        } else {
            Result.failure(Exception("Tenant not found"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    override suspend fun inviteTenant(landlordId: String, tenantId: String, propertyId: String, unitId: String): Result<String> = Result.failure(NotImplementedError())
    override suspend fun removeTenant(landlordId: String, tenantId: String, reason: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun getRentCollectionSummary(landlordId: String): Result<RentCollectionData> = Result.failure(NotImplementedError())
    override suspend fun getTenantRentBalance(landlordId: String, tenantId: String): Result<RentBalanceData> = Result.failure(NotImplementedError())
    override suspend fun getRentTransactions(landlordId: String): Result<List<PaymentSummaryData>> = Result.failure(NotImplementedError())
    override suspend fun getOverdueRent(landlordId: String): Result<List<RentArrearsData>> = Result.failure(NotImplementedError())
    override suspend fun sendRentReminder(landlordId: String, tenantId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun sendBulkRentReminders(landlordId: String, tenantIds: List<String>): Result<Int> = Result.failure(NotImplementedError())
    override suspend fun createBill(landlordId: String, bill: LandlordBillData): Result<String> = Result.failure(NotImplementedError())
    override suspend fun updateBill(landlordId: String, billId: String, bill: LandlordBillData): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun cancelBill(landlordId: String, billId: String, reason: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun getBills(landlordId: String): Result<List<LandlordBillData>> = Result.failure(NotImplementedError())
    override fun observeBills(landlordId: String): Flow<Result<List<LandlordBillData>>> = flow { emit(Result.failure(NotImplementedError())) }
    override suspend fun getOutstandingBills(landlordId: String): Result<List<LandlordBillData>> = Result.failure(NotImplementedError())
    override suspend fun getOverdueBills(landlordId: String): Result<List<LandlordBillData>> = Result.failure(NotImplementedError())
    override suspend fun getPayments(landlordId: String): Result<List<PaymentSummaryData>> = Result.failure(NotImplementedError())
    override fun observePayments(landlordId: String): Flow<Result<List<PaymentSummaryData>>> = flow { emit(Result.failure(NotImplementedError())) }
    override suspend fun getPayment(landlordId: String, paymentId: String): Result<PaymentSummaryData> = Result.failure(NotImplementedError())
    override suspend fun reconcilePayment(landlordId: String, paymentId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun issuePaymentReceipt(landlordId: String, paymentId: String): Result<String> = Result.failure(NotImplementedError())
    override suspend fun createAgreement(landlordId: String, agreement: LandlordAgreementData): Result<String> = Result.failure(NotImplementedError())
    override suspend fun updateAgreement(landlordId: String, agreementId: String, agreement: LandlordAgreementData): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun sendAgreementForSignature(landlordId: String, agreementId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun getAgreements(landlordId: String): Result<List<LandlordAgreementData>> = Result.failure(NotImplementedError())
    override fun observeAgreements(landlordId: String): Flow<Result<List<LandlordAgreementData>>> = flow { emit(Result.failure(NotImplementedError())) }
    override suspend fun terminateAgreement(landlordId: String, agreementId: String, reason: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun getMaintenanceRequests(landlordId: String): Result<List<LandlordMaintenanceData>> = Result.failure(NotImplementedError())
    override fun observeMaintenanceRequests(landlordId: String): Flow<Result<List<LandlordMaintenanceData>>> = flow { emit(Result.failure(NotImplementedError())) }
    override suspend fun getMaintenanceRequest(landlordId: String, requestId: String): Result<LandlordMaintenanceData> = Result.failure(NotImplementedError())
    override suspend fun assignMaintenance(landlordId: String, requestId: String, professionalId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun approveMaintenance(landlordId: String, requestId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun rejectMaintenance(landlordId: String, requestId: String, reason: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun cancelMaintenance(landlordId: String, requestId: String, reason: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun searchProfessionals(landlordId: String, category: String, location: String?): Result<List<ProfessionalSummaryData>> = Result.failure(NotImplementedError())
    override suspend fun getProfessional(professionalId: String): Result<ProfessionalSummaryData> = Result.failure(NotImplementedError())
    override suspend fun hireProfessional(landlordId: String, professionalId: String, request: ProfessionalRequestData): Result<String> = Result.failure(NotImplementedError())
    override suspend fun getViewingRequests(landlordId: String): Result<List<LandlordViewingData>> = Result.failure(NotImplementedError())
    override fun observeViewingRequests(landlordId: String): Flow<Result<List<LandlordViewingData>>> = flow { emit(Result.failure(NotImplementedError())) }
    override suspend fun approveViewing(landlordId: String, viewingId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun rejectViewing(landlordId: String, viewingId: String, reason: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun rescheduleViewing(landlordId: String, viewingId: String, newDate: String, newTime: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun getConversations(landlordId: String): Result<List<ConversationSummaryData>> = Result.failure(NotImplementedError())
    override fun observeConversations(landlordId: String): Flow<Result<List<ConversationSummaryData>>> = flow { emit(Result.failure(NotImplementedError())) }
    override suspend fun sendMessage(landlordId: String, conversationId: String, message: String): Result<String> = Result.failure(NotImplementedError())
    override suspend fun markConversationAsRead(landlordId: String, conversationId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun getNotifications(landlordId: String): Result<List<NotificationSummaryData>> = Result.failure(NotImplementedError())
    override fun observeNotifications(landlordId: String): Flow<Result<List<NotificationSummaryData>>> = flow { emit(Result.failure(NotImplementedError())) }
    override suspend fun markNotificationAsRead(landlordId: String, notificationId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun markAllNotificationsAsRead(landlordId: String): Result<Unit> = Result.failure(NotImplementedError())
    override suspend fun getOccupancyReport(landlordId: String): Result<OccupancyReportData> = Result.failure(NotImplementedError())

    // Staff Implementation
    override suspend fun getStaff(landlordId: String): Result<List<StaffData>> = try {
        val snapshot = firestoreDataSource.collection("staff")
            .whereEqualTo("organizationId", landlordId)
            .get()
            .await()
        Result.success(snapshot.toObjects(StaffData::class.java))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun addStaff(landlordId: String, staff: CreateStaffData): Result<String> = try {
        val id = firestoreDataSource.collection("staff").document().id
        val data = StaffData(
            id = id,
            organizationId = landlordId,
            userId = staff.userId,
            fullName = staff.fullName,
            email = staff.email,
            phoneNumber = staff.phoneNumber,
            role = staff.role,
            employmentType = staff.employmentType,
            status = StaffStatus.ACTIVE,
            startDate = staff.startDate,
            assignedPropertyCount = staff.propertyIds.size,
            assignedUnitCount = 0,
            pendingTaskCount = 0
        )
        firestoreDataSource.saveData("staff", id, data).map { id }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun removeStaff(landlordId: String, staffId: String): Result<Unit> = try {
        firestoreDataSource.deleteData("staff", staffId)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getIncomeReport(landlordId: String, startDate: String, endDate: String): Result<IncomeReportData> = Result.failure(NotImplementedError())
    override suspend fun getArrearsReport(landlordId: String): Result<ArrearsReportData> = Result.failure(NotImplementedError())
    override suspend fun getMaintenanceReport(landlordId: String): Result<MaintenanceReportData> = Result.failure(NotImplementedError())
    override suspend fun getDashboard(landlordId: String): Result<LandlordDashboardData> = Result.failure(NotImplementedError())
    override fun observeDashboard(landlordId: String): Flow<Result<LandlordDashboardData>> = flow { emit(Result.failure(NotImplementedError())) }

    override suspend fun getPendingApplications(landlordId: String): Result<List<ApartmentApplicationData>> = try {
        val ref = firebaseDataSource.getReference("applications/$landlordId")
        val snapshot = ref.get().await()
        
        val list = snapshot.children.mapNotNull { child ->
            try {
                child.getValue(ApartmentApplicationData::class.java)?.let { app ->
                    if (app.id.isEmpty()) app.copy(id = child.key ?: "") else app
                }
            } catch (e: Exception) {
                android.util.Log.e("LandlordRepo", "Error mapping application ${child.key}", e)
                null
            }
        }
        
        Result.success(list.filter { it.status == "PENDING" }.sortedByDescending { it.appliedAt })
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun processApplication(landlordId: String, applicationId: String, status: String, reason: String?): Result<Unit> = try {
        val appRef = firebaseDataSource.getReference("applications/$landlordId/$applicationId")
        val appSnapshot = appRef.get().await()
        
        if (!appSnapshot.exists()) {
            throw Exception("Application not found at path: applications/$landlordId/$applicationId")
        }

        val app = appSnapshot.getValue(ApartmentApplicationData::class.java)?.let { 
            if (it.id.isEmpty()) it.copy(id = applicationId) else it
        } ?: throw Exception("Failed to deserialize application")
        
        val now = System.currentTimeMillis()
        val updates = mutableMapOf<String, Any?>()
        updates["status"] = status
        updates["processedAt"] = now
        if (reason != null) updates["rejectionReason"] = reason

        // Update in both places
        appRef.updateChildren(updates).await()
        firebaseDataSource.getReference("tenant_applications/${app.tenantId}/$applicationId").updateChildren(updates).await()

        if (status == "APPROVED") {
            // 1. Create Membership record
            val membershipId = "MEM_${app.tenantId}_${app.apartmentId}"
            val membership = mutableMapOf(
                "id" to membershipId,
                "tenantId" to app.tenantId,
                "apartmentId" to app.apartmentId,
                "houseId" to app.houseId,
                "houseNumber" to app.houseNumber,
                "status" to "JOINED",
                "signature" to app.signature,
                "nationalIdUrl" to app.nationalIdUrl,
                "createdAt" to now
            )
            firebaseDataSource.writeData("memberships/${app.tenantId}", membership).getOrThrow()
            firebaseDataSource.writeData("users/${app.tenantId}/currentApartmentId", app.apartmentId).getOrThrow()
            
            // 2. Add to tenants_by_landlord for the My Tenants list
            val tenantSummary = TenantSummaryData(
                id = app.tenantId,
                name = app.tenantName,
                phoneNumber = app.tenantPhone.takeIf { it.isNotEmpty() },
                propertyId = app.apartmentId,
                propertyName = app.apartmentName,
                unitId = app.houseId,
                unitName = app.houseNumber,
                rentBalance = 0.0,
                tenancyStatus = "Active"
            )
            firebaseDataSource.writeData("tenants_by_landlord/$landlordId/${app.tenantId}", tenantSummary).getOrThrow()

            // 3. Log activity for tenant
            val activityId = "act_${now}"
            val activity = mapOf(
                "id" to activityId,
                "title" to "Application Approved!",
                "subtitle" to "You have been accepted into ${app.apartmentName} ${if (app.houseNumber.isNotEmpty()) "Unit ${app.houseNumber}" else ""}",
                "time" to "Just now",
                "type" to "TENANT",
                "status" to "SUCCESS",
                "timestamp" to now
            )
            firebaseDataSource.writeData("activities/${app.tenantId}/$activityId", activity).getOrThrow()

            // 4. Update unit status if houseId was specified
            if (app.houseId.isNotEmpty() && app.houseId != "GENERAL") {
                try {
                    // Update unit in multiple synced locations
                    val unitUpdate = mapOf(
                        "status" to "OCCUPIED", 
                        "tenantId" to app.tenantId, 
                        "tenantName" to app.tenantName,
                        "moveInDate" to java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
                    )
                    
                    // A. Update in public property path
                    firebaseDataSource.updateData("properties/${app.apartmentId}/units/${app.houseId}", unitUpdate).getOrThrow()
                    
                    // B. Find and update in landlord_units (requires floorId)
                    val unitSnapshot = firebaseDataSource.getReference("properties/${app.apartmentId}/units/${app.houseId}").get().await()
                    val floorId = unitSnapshot.child("floorId").getValue(String::class.java)
                    if (!floorId.isNullOrEmpty()) {
                        firebaseDataSource.updateData("landlord_units/${app.apartmentId}/$floorId/${app.houseId}", unitUpdate).getOrThrow()
                    }

                    // 5. Update Apartment Availability (Auto-Tally)
                    val propSnapshot = firebaseDataSource.getReference("properties/${app.apartmentId}").get().await()
                    val available = propSnapshot.child("availableUnits").getValue(Int::class.java) ?: 0
                    val occupied = propSnapshot.child("occupiedUnits").getValue(Int::class.java) ?: 0
                    
                    val countsUpdate = mapOf(
                        "availableUnits" to (available - 1).coerceAtLeast(0),
                        "occupiedUnits" to (occupied + 1)
                    )
                    
                    firebaseDataSource.updateData("properties/${app.apartmentId}", countsUpdate).getOrThrow()
                    firebaseDataSource.updateData("listings/${app.apartmentId}", countsUpdate).getOrThrow()
                    firebaseDataSource.updateData("landlord_properties/$landlordId/${app.apartmentId}", countsUpdate).getOrThrow()
                    
                } catch (e: Exception) {
                    android.util.Log.e("LandlordRepo", "Failed to update unit/property status", e)
                }
            }
        } else if (status == "DECLINED") {
            // Log rejection activity for tenant
            val activityId = "act_${now}"
            val activity = mapOf(
                "id" to activityId,
                "title" to "Application Declined",
                "subtitle" to "Request to join ${app.apartmentName} was declined. ${reason ?: ""}",
                "time" to "Just now",
                "type" to "TENANT",
                "status" to "FAILURE",
                "timestamp" to now
            )
            firebaseDataSource.writeData("activities/${app.tenantId}/$activityId", activity).getOrThrow()
        }

        Result.success(Unit)
    } catch (e: Exception) {
        android.util.Log.e("LandlordRepo", "Error processing application", e)
        Result.failure(e)
    }

}
