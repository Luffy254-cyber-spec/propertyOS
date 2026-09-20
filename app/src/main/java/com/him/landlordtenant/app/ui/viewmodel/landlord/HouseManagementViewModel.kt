package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.*
import com.him.landlordtenant.app.data.model.House
import com.him.landlordtenant.app.data.model.HouseStatus
import com.him.landlordtenant.app.data.remote.FirebaseDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.util.Log
import javax.inject.Inject

@HiltViewModel
class HouseManagementViewModel @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val landlordRepository: LandlordRepository,
    private val firebaseDataSource: FirebaseDataSource,
    private val activityRepository: ActivityRepository
) : ViewModel() {

    private val _house = MutableStateFlow<House?>(null)
    val house: StateFlow<House?> = _house.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var houseJob: kotlinx.coroutines.Job? = null

    fun loadHouse(apartmentId: String, floorId: String, houseId: String) {
        houseJob?.cancel()
        _isLoading.value = true
        houseJob = landlordRepository.observeHouse(apartmentId, floorId, houseId)
            .onEach { result ->
                result.onSuccess { unitData ->
                    viewModelScope.launch {
                        try {
                            val ref = firebaseDataSource.getReference("landlord_units/$apartmentId/$floorId/$houseId")
                            val snapshot = ref.get().await()
                            _house.value = snapshot.getValue(House::class.java)
                        } catch (e: Exception) {
                            _error.value = "Failed to sync house details"
                        }
                        _isLoading.value = false
                    }
                }.onFailure {
                    _error.value = "Failed to load house"
                    _isLoading.value = false
                }
            }.launchIn(viewModelScope)
    }

    fun updateStatus(apartmentId: String, floorId: String, houseId: String, status: HouseStatus) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = propertyRepository.updateHouseStatus(apartmentId, floorId, houseId, status)
            result.onSuccess {
                loadHouse(apartmentId, floorId, houseId)
                activityRepository.logActivity(
                    userId = "landlord", // Should be real ID
                    activity = CreateActivityData(
                        title = "House Status Updated",
                        subtitle = "Unit $houseId marked as ${status.name}",
                        type = "PROPERTY",
                        status = "SUCCESS"
                    )
                )
            }.onFailure {
                _error.value = "Failed to update status: ${it.message}"
            }
            _isLoading.value = false
        }
    }
}
