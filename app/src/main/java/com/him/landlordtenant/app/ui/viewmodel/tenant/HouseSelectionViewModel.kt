package com.him.landlordtenant.app.ui.viewmodel.tenant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.data.remote.FirebaseDataSource
import com.him.landlordtenant.app.ui.screens.tenant.HouseCondition
import com.him.landlordtenant.app.ui.screens.tenant.HouseStatus
import com.him.landlordtenant.app.ui.screens.tenant.HouseType
import com.him.landlordtenant.app.ui.screens.tenant.TenantHouseUIModel
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

    private val _houses = MutableStateFlow<List<TenantHouseUIModel>>(emptyList())
    val houses: StateFlow<List<TenantHouseUIModel>> = _houses.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadHouses(apartmentId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Fetch units from RTDB
                val snapshot = firebaseDataSource.getReference("properties/$apartmentId/floors").get().await()
                val houseList = mutableListOf<TenantHouseUIModel>()
                
                snapshot.children.forEach { floorSnapshot ->
                    val floorNum = floorSnapshot.child("number").getValue(Int::class.java) ?: 0
                    floorSnapshot.child("units").children.forEach { unitSnapshot ->
                        houseList.add(
                            TenantHouseUIModel(
                                houseId = unitSnapshot.key ?: "",
                                houseNumber = unitSnapshot.child("number").getValue(String::class.java) ?: "",
                                floorNumber = floorNum,
                                houseType = HouseType.valueOf(unitSnapshot.child("type").getValue(String::class.java) ?: "ONE_BEDROOM"),
                                status = HouseStatus.valueOf(unitSnapshot.child("status").getValue(String::class.java) ?: "VACANT"),
                                condition = HouseCondition.GOOD,
                                monthlyRent = unitSnapshot.child("rent").getValue(Double::class.java) ?: 0.0,
                                deposit = unitSnapshot.child("rent").getValue(Double::class.java) ?: 0.0
                            )
                        )
                    }
                }
                _houses.value = houseList
            } catch (e: Exception) {
                _houses.value = emptyList()
            }
            _isLoading.value = false
        }
    }
}
