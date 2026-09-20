package com.him.landlordtenant.app.ui.viewmodel.landlord

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UnitTenantManagementUIState(
    val house: UnitData? = null,
    val currentTenant: UserProfileData? = null,
    val availableTenants: List<TenantSummaryData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val assignmentSuccess: Boolean = false
)

@HiltViewModel
class UnitTenantManagementViewModel @Inject constructor(
    private val landlordRepository: LandlordRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UnitTenantManagementUIState())
    val uiState: StateFlow<UnitTenantManagementUIState> = _uiState.asStateFlow()

    private var houseJob: kotlinx.coroutines.Job? = null

    fun startObserving(apartmentId: String, floorId: String, houseId: String) {
        houseJob?.cancel()
        _uiState.update { it.copy(isLoading = true) }
        
        houseJob = landlordRepository.observeHouse(apartmentId, floorId, houseId)
            .onEach { result ->
                result.onSuccess { house ->
                    _uiState.update { it.copy(house = house, isLoading = false) }
                    if (house.tenantId != null) {
                        loadCurrentTenant(house.tenantId)
                    } else {
                        _uiState.update { it.copy(currentTenant = null) }
                        loadAvailableTenants()
                    }
                }.onFailure { error ->
                    _uiState.update { it.copy(error = error.message, isLoading = false) }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadCurrentTenant(tenantId: String) {
        viewModelScope.launch {
            userRepository.getUserProfile(tenantId).onSuccess { profile ->
                _uiState.update { it.copy(currentTenant = profile) }
            }
        }
    }

    private fun loadAvailableTenants() {
        viewModelScope.launch {
            val landlordId = authRepository.getCurrentUserId() ?: return@launch
            landlordRepository.getTenants(landlordId).onSuccess { allTenants ->
                // Filter tenants who don't have a unit assigned yet
                val available = allTenants.filter { it.unitId.isEmpty() || it.unitId == "GENERAL" }
                _uiState.update { it.copy(availableTenants = available) }
            }.onFailure { e ->
                Log.e("UnitTenantVM", "Error loading tenants", e)
            }
        }
    }

    fun assignTenant(tenantId: String, apartmentId: String, floorId: String, houseId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val landlordId = authRepository.getCurrentUserId() ?: return@launch
            
            val result = landlordRepository.assignTenantToUnit(
                landlordId = landlordId,
                tenantId = tenantId,
                apartmentId = apartmentId,
                floorId = floorId,
                houseId = houseId
            )
            
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, assignmentSuccess = true) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = "Failed to assign tenant: ${e.message}") }
            }
        }
    }

    fun evictTenant(apartmentId: String, floorId: String, houseId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val landlordId = authRepository.getCurrentUserId() ?: return@launch
            
            landlordRepository.evictTenantFromUnit(landlordId, apartmentId, floorId, houseId)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, currentTenant = null) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = "Failed to remove tenant: ${e.message}") }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
    
    fun resetSuccess() {
        _uiState.update { it.copy(assignmentSuccess = false) }
    }
}
