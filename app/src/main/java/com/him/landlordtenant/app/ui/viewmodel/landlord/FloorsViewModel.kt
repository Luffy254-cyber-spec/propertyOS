package com.him.landlordtenant.app.ui.viewmodel.landlord

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.data.model.HouseStatus
import com.him.landlordtenant.app.data.remote.FirebaseDataSource
import com.him.landlordtenant.app.data.remote.FirestoreDataSource
import com.him.landlordtenant.app.interfaces.*
import com.him.landlordtenant.app.ui.screens.tenant.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class FloorsViewModel @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource,
    private val firestoreDataSource: FirestoreDataSource,
    private val authRepository: AuthRepository,
    private val documentRepository: DocumentRepository
) : ViewModel() {

    private val _floors = MutableStateFlow<List<PropertyFloorData>>(emptyList())
    val floors: StateFlow<List<PropertyFloorData>> = _floors.asStateFlow()

    private val _units = MutableStateFlow<List<TenantHouseUIModel>>(emptyList())
    val units: StateFlow<List<TenantHouseUIModel>> = _units.asStateFlow()

    private val _apartmentName = MutableStateFlow("Loading...")
    val apartmentName: StateFlow<String> = _apartmentName.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadFloors(apartmentId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Fetch Name reliably from landlord properties
                val userId = authRepository.getCurrentUserId() ?: return@launch
                val snapshotName = firebaseDataSource.getReference("landlord_properties/$userId/$apartmentId/name").get().await()
                _apartmentName.value = snapshotName.getValue(String::class.java) ?: "My Apartment"

                // Independent Fetch for Floors
                val ref = firebaseDataSource.getReference("landlord_floors/$apartmentId")
                val snapshot = ref.get().await()
                
                val floorList = snapshot.children.mapNotNull { child ->
                    try {
                        child.getValue(PropertyFloorData::class.java)?.let { floor ->
                            if (floor.id.isEmpty()) floor.copy(id = child.key ?: "") else floor
                        }
                    } catch (e: Exception) {
                        null
                    }
                }.sortedBy { it.number }
                
                _floors.value = floorList
            } catch (e: Exception) {
                Log.e("FloorsVM", "Error loading floors: ${e.message}")
                _floors.value = emptyList()
            }
            _isLoading.value = false
        }
    }

    fun addFloor(apartmentId: String, floorNumber: Int, floorName: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val floorId = "floor_$floorNumber"
                val floorData = PropertyFloorData(
                    id = floorId,
                    number = floorNumber,
                    name = floorName,
                    unitCount = 0,
                    createdAt = System.currentTimeMillis()
                )
                
                // 1. Save to Reliable Independent Path
                firebaseDataSource.writeData("landlord_floors/$apartmentId/$floorId", floorData)
                
                // 2. Sync to property structure
                firebaseDataSource.writeData("properties/$apartmentId/floors/$floorId", floorData)
                
                onSuccess()
            } catch (e: Exception) {
                Log.e("FloorsVM", "Error adding floor", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadUnits(apartmentId: String, floorId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = firebaseDataSource.getReference("landlord_units/$apartmentId/$floorId").get().await()
                val unitList = snapshot.children.mapNotNull { unitSnapshot ->
                    try {
                        val house = unitSnapshot.getValue(com.him.landlordtenant.app.data.model.House::class.java)
                        house?.let { h ->
                            TenantHouseUIModel(
                                houseId = h.id,
                                houseNumber = h.houseNumber,
                                floorNumber = h.floorId.replace("floor_", "").toIntOrNull() ?: 0,
                                houseType = HouseType.valueOf(h.houseType.name),
                                status = HouseStatus.valueOf(h.status.name),
                                condition = HouseCondition.GOOD,
                                monthlyRent = h.monthlyRent,
                                deposit = h.securityDeposit,
                                description = h.description,
                                imageUrls = h.imageUrls
                            )
                        }
                    } catch (e: Exception) {
                        null
                    }
                }
                _units.value = unitList
            } catch (e: Exception) {
                _units.value = emptyList()
            }
            _isLoading.value = false
        }
    }

    fun addUnitWithMedia(
        apartmentId: String,
        floorId: String,
        houseNumber: String,
        houseType: HouseType,
        rent: Double,
        deposit: Double,
        description: String,
        initialWaterReading: Double,
        mediaUris: List<String>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Upload Media
                val finalUrls = mutableListOf<String>()
                mediaUris.forEach { uri ->
                    documentRepository.uploadImage(uri).onSuccess { finalUrls.add(it) }
                }

                val houseId = "house_${houseNumber}_${System.currentTimeMillis()}"
                val house = com.him.landlordtenant.app.data.model.House(
                    id = houseId,
                    houseNumber = houseNumber,
                    apartmentId = apartmentId,
                    floorId = floorId,
                    monthlyRent = rent,
                    securityDeposit = deposit,
                    description = description,
                    initialWaterReading = initialWaterReading,
                    houseType = com.him.landlordtenant.app.data.model.HouseType.valueOf(houseType.name),
                    status = com.him.landlordtenant.app.data.model.HouseStatus.VACANT,
                    imageUrls = finalUrls
                )
                
                // 2. Save to Independent Path
                firebaseDataSource.writeData("landlord_units/$apartmentId/$floorId/$houseId", house)
                
                // 3. Update public units
                firebaseDataSource.writeData("properties/$apartmentId/units/$houseId", house)

                // 4. Update Floor Unit Count
                val floorRef = firebaseDataSource.getReference("landlord_floors/$apartmentId/$floorId")
                val floorSnapshot = floorRef.get().await()
                val currentFloorCount = floorSnapshot.child("unitCount").getValue(Int::class.java) ?: 0
                floorRef.child("unitCount").setValue(currentFloorCount + 1)

                // 5. Update Property-wide Unit Count (Auto-Tally)
                val userId = authRepository.getCurrentUserId() ?: return@launch
                val propRef = firebaseDataSource.getReference("landlord_properties/$userId/$apartmentId")
                val propSnapshot = propRef.get().await()
                
                val currentTotal = propSnapshot.child("totalUnits").getValue(Int::class.java) ?: 0
                val currentAvailable = propSnapshot.child("availableUnits").getValue(Int::class.java) ?: 0
                
                val newTotal = currentTotal + 1
                val newAvailable = currentAvailable + 1

                // Update in all synced locations
                val updates = mapOf(
                    "totalUnits" to newTotal,
                    "availableUnits" to newAvailable
                )
                
                propRef.updateChildren(updates)
                firebaseDataSource.getReference("properties/$apartmentId").updateChildren(updates)
                firebaseDataSource.getReference("listings/$apartmentId").updateChildren(updates)

                onSuccess()
            } catch (e: Exception) {
                Log.e("FloorsVM", "Error adding unit", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
