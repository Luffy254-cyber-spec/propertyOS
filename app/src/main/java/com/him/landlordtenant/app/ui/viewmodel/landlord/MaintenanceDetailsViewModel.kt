package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.LandlordRepository
import com.him.landlordtenant.app.ui.screens.tenant.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MaintenanceDetailsViewModel @Inject constructor(
    private val landlordRepository: LandlordRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _request = MutableStateFlow<MaintenanceRequestUIModel?>(null)
    val request: StateFlow<MaintenanceRequestUIModel?> = _request.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadRequest(requestId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = authRepository.getCurrentUserId() ?: return@launch
            landlordRepository.getMaintenanceRequest(userId, requestId).onSuccess { data ->
                _request.value = MaintenanceRequestUIModel(
                    id = data.id,
                    category = try { MaintenanceCategory.valueOf(data.priority) } catch(e: Exception) { MaintenanceCategory.OTHER },
                    priority = try { MaintenancePriority.valueOf(data.priority) } catch(e: Exception) { MaintenancePriority.MEDIUM },
                    location = data.unitName,
                    description = data.title + "\n" + data.description,
                    status = try { MaintenanceStatus.valueOf(data.status) } catch(e: Exception) { MaintenanceStatus.SUBMITTED },
                    createdAt = "N/A",
                    assignedTo = data.assignedProfessionalId,
                    assignedPhone = null
                )
            }
            _isLoading.value = false
        }
    }
}
