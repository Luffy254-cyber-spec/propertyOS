package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _floors = MutableStateFlow<List<String>>(emptyList())
    val floors: StateFlow<List<String>> = _floors.asStateFlow()

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
                // Fetch Name
                val snapshotName = firebaseDataSource.getReference("listings/$apartmentId/title").get().await()
                _apartmentName.value = snapshotName.getValue(String::class.java) ?: "My Apartment"

                // Try fetching floors from RTDB
                val rtdbSnapshot = firebaseDataSource.getReference("properties/$apartmentId/floors").get().await()
                val rtdbFloors = rtdbSnapshot.children.mapNotNull { it.key }
                
                // Try fetching from Firestore
                val firestoreSnapshot = firestoreDataSource.collection("properties/$apartmentId/floors").get().await()
                val firestoreFloors = firestoreSnapshot.documents.map { it.id }
                
                _floors.value = (rtdbFloors + firestoreFloors).distinct().sorted()
            } catch (e: Exception) {
                _floors.value = emptyList()
            }
            _isLoading.value = false
        }
    }

    fun addFloor(apartmentId: String, floorNumber: Int, floorName: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val floorId = floorNumber.toString()
                val floorData = mapOf(
                    "number" to floorNumber,
                    "name" to floorName,
                    "createdAt" to System.currentTimeMillis()
                )
                
                // Save to RTDB
                val rtdbResult = firebaseDataSource.writeData("properties/$apartmentId/floors/$floorId", floorData)
                
                // Also save to Firestore for redundancy
                val firestoreResult = firestoreDataSource.saveData("properties/$apartmentId/floors", floorId, floorData)
                
                if (rtdbResult.isSuccess || firestoreResult.isSuccess) {
                    onSuccess()
                }
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadUnits(apartmentId: String, floorId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = firebaseDataSource.getReference("properties/$apartmentId/floors/$floorId/units").get().await()
                val unitList = snapshot.children.mapNotNull { unitSnapshot ->
                    TenantHouseUIModel(
                        houseId = unitSnapshot.key ?: "",
                        houseNumber = unitSnapshot.child("number").getValue(String::class.java) ?: "",
                        floorNumber = floorId.toIntOrNull() ?: 0,
                        houseType = HouseType.valueOf(unitSnapshot.child("type").getValue(String::class.java) ?: "ONE_BEDROOM"),
                        status = HouseStatus.valueOf(unitSnapshot.child("status").getValue(String::class.java) ?: "VACANT"),
                        condition = HouseCondition.GOOD,
                        monthlyRent = unitSnapshot.child("rent").getValue(Double::class.java) ?: 0.0,
                        deposit = unitSnapshot.child("rent").getValue(Double::class.java) ?: 0.0
                    )
                }
                _units.value = unitList
            } catch (e: Exception) {
                _units.value = emptyList()
            }
            _isLoading.value = false
        }
    }

    fun addUnit(apartmentId: String, floorId: String, unitNumber: String, unitType: String, rent: Double, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val unitData = mapOf(
                    "id" to unitNumber,
                    "number" to unitNumber,
                    "type" to unitType,
                    "rent" to rent,
                    "status" to "VACANT",
                    "createdAt" to System.currentTimeMillis()
                )
                
                // RTDB: properties/$apartmentId/floors/$floorId/units/$unitNumber
                firebaseDataSource.writeData("properties/$apartmentId/floors/$floorId/units/$unitNumber", unitData)
                
                // Update Marketplace Listing Stats
                val listingRef = firebaseDataSource.getReference("listings/$apartmentId")
                val listingSnapshot = listingRef.get().await()
                val currentTotal = listingSnapshot.child("totalUnits").getValue(Int::class.java) ?: 0
                val currentAvail = listingSnapshot.child("availableUnits").getValue(Int::class.java) ?: 0
                
                listingRef.child("totalUnits").setValue(currentTotal + 1)
                listingRef.child("availableUnits").setValue(currentAvail + 1)

                onSuccess()
            } catch (e: Exception) {
                // Handle error
            }
            _isLoading.value = false
        }
    }
}
