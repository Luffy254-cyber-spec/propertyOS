package com.him.landlordtenant.app.ui.viewmodel.landlord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.StaffData
import com.him.landlordtenant.app.interfaces.StaffRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandlordStaffViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val staffRepository: StaffRepository
) : ViewModel() {

    private val _staffList = MutableStateFlow<List<StaffData>>(emptyList())
    val staffList: StateFlow<List<StaffData>> = _staffList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadStaff()
    }

    fun loadStaff() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = authRepository.getCurrentUserId() ?: return@launch
            staffRepository.getStaffByOrganization(userId).onSuccess {
                _staffList.value = it
            }
            _isLoading.value = false
        }
    }
}
