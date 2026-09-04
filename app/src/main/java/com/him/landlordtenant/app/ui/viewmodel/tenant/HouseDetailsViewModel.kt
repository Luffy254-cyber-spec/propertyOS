package com.him.landlordtenant.app.ui.viewmodel.tenant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.data.remote.FirebaseDataSource
import com.him.landlordtenant.app.ui.screens.tenant.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HouseDetailsViewModel @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : ViewModel() {

    private val _house = MutableStateFlow<TenantHouseUIModel?>(null)
    val house: StateFlow<TenantHouseUIModel?> = _house.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadHouse(houseId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // This is a complex fetch across the properties/floors tree in RTDB
                // For now, search all apartments or use a indexed node if it exists
                val apartmentsSnapshot = firebaseDataSource.getReference("properties").get().await()
                var found: TenantHouseUIModel? = null
                
                for (aptSnapshot in apartmentsSnapshot.children) {
                    val floorsSnapshot = aptSnapshot.child("floors")
                    for (floorSnapshot in floorsSnapshot.children) {
                        val unitsSnapshot = floorSnapshot.child("units")
                        val unitSnapshot = unitsSnapshot.child(houseId)
                        if (unitSnapshot.exists()) {
                            found = TenantHouseUIModel(
                                houseId = houseId,
                                houseNumber = unitSnapshot.child("number").getValue(String::class.java) ?: "",
                                floorNumber = floorSnapshot.child("number").getValue(Int::class.java) ?: 0,
                                houseType = HouseType.valueOf(unitSnapshot.child("type").getValue(String::class.java) ?: "ONE_BEDROOM"),
                                status = HouseStatus.valueOf(unitSnapshot.child("status").getValue(String::class.java) ?: "VACANT"),
                                condition = HouseCondition.GOOD,
                                monthlyRent = unitSnapshot.child("rent").getValue(Double::class.java) ?: 0.0,
                                deposit = unitSnapshot.child("rent").getValue(Double::class.java) ?: 0.0,
                                apartmentName = aptSnapshot.child("name").getValue(String::class.java) ?: "Apartment",
                                apartmentAddress = aptSnapshot.child("address").getValue(String::class.java) ?: ""
                            )
                            break
                        }
                    }
                    if (found != null) break
                }
                _house.value = found
            } catch (e: Exception) {
                _house.value = null
            }
            _isLoading.value = false
        }
    }

    fun joinHouse(houseId: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            // Mock join logic
            kotlinx.coroutines.delay(1000)
            onComplete()
            _isLoading.value = false
        }
    }
}
