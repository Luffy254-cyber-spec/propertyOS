package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.data.model.House
import com.him.landlordtenant.app.data.remote.FirebaseDataSource
import com.him.landlordtenant.app.interfaces.ActivityRepository
import com.him.landlordtenant.app.interfaces.CreateActivityData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.util.Log
import javax.inject.Inject

@HiltViewModel
class EditHouseViewModel @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource,
    private val activityRepository: ActivityRepository
) : ViewModel() {

    private val _house = MutableStateFlow<House?>(null)
    val house: StateFlow<House?> = _house.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()

    fun loadHouse(apartmentId: String, floorId: String, houseId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val ref = firebaseDataSource.getReference("landlord_units/$apartmentId/$floorId/$houseId")
                val snapshot = ref.get().await()
                _house.value = snapshot.getValue(House::class.java)
            } catch (e: Exception) {
                Log.e("EditHouseVM", "Error loading house", e)
            }
            _isLoading.value = false
        }
    }

    fun updateHouse(
        apartmentId: String,
        floorId: String,
        houseId: String,
        houseNumber: String,
        rent: Double,
        deposit: Double,
        initialWaterReading: Double,
        status: com.him.landlordtenant.app.data.model.HouseStatus,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val current = _house.value ?: return@launch
                val updated = current.copy(
                    houseNumber = houseNumber,
                    monthlyRent = rent,
                    securityDeposit = deposit,
                    initialWaterReading = initialWaterReading,
                    status = status
                )

                // 1. Update in Reliable Independent Path
                firebaseDataSource.writeData("landlord_units/$apartmentId/$floorId/$houseId", updated)
                
                // 2. Update in public structure
                firebaseDataSource.writeData("properties/$apartmentId/units/$houseId", updated)

                activityRepository.logActivity(
                    userId = "landlord", // Should be real ID
                    activity = CreateActivityData(
                        title = "House Details Updated",
                        subtitle = "Unit $houseNumber updated",
                        type = "PROPERTY",
                        status = "SUCCESS"
                    )
                )
                _saveSuccess.value = true
                onSuccess()
            } catch (e: Exception) {
                Log.e("EditHouseVM", "Error updating house", e)
            }
            _isLoading.value = false
        }
    }
}
