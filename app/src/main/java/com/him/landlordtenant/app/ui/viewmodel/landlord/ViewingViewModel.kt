package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.ViewingRepository
import com.him.landlordtenant.app.interfaces.ViewingSummaryData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val viewingRepository: ViewingRepository,
    private val activityRepository: com.him.landlordtenant.app.interfaces.ActivityRepository
) : ViewModel() {

    private val _viewings = MutableStateFlow<List<ViewingSummaryData>>(emptyList())
    val viewings: StateFlow<List<ViewingSummaryData>> = _viewings.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadViewings()
    }

    fun loadViewings() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = authRepository.getCurrentUserId() ?: return@launch
            viewingRepository.getLandlordViewings(userId).onSuccess {
                _viewings.value = it.sortedByDescending { v -> v.createdAt }
            }
            _isLoading.value = false
        }
    }

    fun approveViewing(viewingId: String) {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId() ?: return@launch
            viewingRepository.approveViewing(userId, viewingId).onSuccess {
                activityRepository.logActivity(
                    userId = userId,
                    activity = com.him.landlordtenant.app.interfaces.CreateActivityData(
                        title = "Viewing Approved",
                        subtitle = "Appointment #$viewingId confirmed",
                        type = "VIEWING",
                        status = "SUCCESS"
                    )
                )
                loadViewings()
            }
        }
    }

    fun rejectViewing(viewingId: String, reason: String?) {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId() ?: return@launch
            viewingRepository.rejectViewing(userId, viewingId, reason).onSuccess {
                loadViewings()
            }
        }
    }
}
