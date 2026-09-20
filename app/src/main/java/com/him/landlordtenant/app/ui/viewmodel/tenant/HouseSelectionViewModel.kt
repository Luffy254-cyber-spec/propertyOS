package com.him.landlordtenant.app.ui.viewmodel.tenant

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.data.remote.FirebaseDataSource
import com.him.landlordtenant.app.ui.screens.tenant.HouseCondition
import com.him.landlordtenant.app.data.model.HouseStatus
import com.him.landlordtenant.app.ui.screens.tenant.HouseType
import com.him.landlordtenant.app.ui.screens.tenant.TenantHouseUIModel
import com.him.landlordtenant.app.interfaces.PropertyFloorData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HouseSelectionViewModel @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : ViewModel() {

    private val _floors = MutableStateFlow<List<PropertyFloorData>>(emptyList())
    val floors: StateFlow<List<PropertyFloorData>> = _floors.asStateFlow()

    private val _selectedFloorUnits = MutableStateFlow<List<TenantHouseUIModel>>(emptyList())
    val selectedFloorUnits: StateFlow<List<TenantHouseUIModel>> = _selectedFloorUnits.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadFloors(apartmentId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Fetch floors from the landlord_floors path which is the primary source for units/structure
                val ref = firebaseDataSource.getReference("landlord_floors/$apartmentId")
                val snapshot = ref.get().await()
                
                val floorList = snapshot.children.mapNotNull { child ->
                    try {
                        val floor = child.getValue(PropertyFloorData::class.java)
                        if (floor != null && floor.id.isEmpty()) floor.copy(id = child.key ?: "") else floor
                    } catch (e: Exception) {
                        null
                    }
                }.sortedBy { it.number }
                
                _floors.value = floorList
            } catch (e: Exception) {
                Log.e("HouseSelectionVM", "Error loading floors: ${e.message}")
                _floors.value = emptyList()
            }
            _isLoading.value = false
        }
    }

    fun loadUnitsForFloor(apartmentId: String, floorId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Fetch units from the primary management path: landlord_units/$apartmentId/$floorId
                val snapshot = firebaseDataSource.getReference("landlord_units/$apartmentId/$floorId").get().await()
                
                val unitList = snapshot.children.mapNotNull { unitSnapshot ->
                    try {
                        // The data is stored as a House object
                        val house = unitSnapshot.getValue(com.him.landlordtenant.app.data.model.House::class.java)
                        
                        house?.let { h ->
                            TenantHouseUIModel(
                                houseId = h.id,
                                apartmentId = apartmentId,
                                houseNumber = h.houseNumber,
                                floorNumber = h.floorId.replace("floor_", "").toIntOrNull() ?: 0,
                                houseType = try { HouseType.valueOf(h.houseType.name) } catch(e: Exception) { HouseType.ONE_BEDROOM },
                                status = h.status,
                                condition = HouseCondition.GOOD,
                                monthlyRent = h.monthlyRent,
                                deposit = h.securityDeposit,
                                description = h.description,
                                imageUrls = h.imageUrls
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("HouseSelectionVM", "Error mapping unit: ${e.message}")
                        null
                    }
                }
                _selectedFloorUnits.value = unitList
            } catch (e: Exception) {
                Log.e("HouseSelectionVM", "Error loading units: ${e.message}")
                _selectedFloorUnits.value = emptyList()
            }
            _isLoading.value = false
        }
    }
}
