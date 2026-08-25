package com.him.landlordtenant.app.ui.viewmodel.tenant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.MaintenanceRequestData
import com.him.landlordtenant.app.interfaces.TenantRepository
import com.him.landlordtenant.app.ui.screens.tenant.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TenantViewModel @Inject constructor(
    private val tenantRepository: TenantRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _dashboardState = MutableStateFlow<TenantDashboardUIState?>(null)
    val dashboardState: StateFlow<TenantDashboardUIState?> = _dashboardState.asStateFlow()

    private val _bills = MutableStateFlow<List<TenantBillUIModel>>(emptyList())
    val bills: StateFlow<List<TenantBillUIModel>> = _bills.asStateFlow()

    private val _maintenanceRequests = MutableStateFlow<List<MaintenanceRequestUIModel>>(emptyList())
    val maintenanceRequests: StateFlow<List<MaintenanceRequestUIModel>> = _maintenanceRequests.asStateFlow()

    init {
        loadDashboardData()
        loadBills()
        loadMaintenanceRequests()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId() ?: return@launch
            tenantRepository.getDashboard(userId).onSuccess { data ->
                _dashboardState.value = TenantDashboardUIState(
                    tenantName = data.profile.fullName,
                    apartmentName = data.tenancy?.propertyName ?: "No active tenancy",
                    apartmentId = data.tenancy?.propertyId ?: "",
                    houseNumber = data.tenancy?.unitName ?: "N/A",
                    floorNumber = "0",
                    houseType = "Apartment",
                    location = "Nairobi",
                    landlordId = data.tenancy?.landlordId ?: "",
                    landlordName = data.tenancy?.landlordName ?: "N/A",
                    landlordPhone = data.tenancy?.landlordPhone ?: "N/A",
                    monthlyRent = data.tenancy?.monthlyRent ?: 0.0,
                    waterBill = 0.0,
                    garbageFee = 0.0,
                    serviceCharge = 0.0,
                    previousArrears = data.rentBalance?.arrearsAmount ?: 0.0,
                    amountPaid = data.rentBalance?.amountPaid ?: 0.0,
                    outstandingAmount = data.rentBalance?.outstandingAmount ?: 0.0,
                    totalDue = (data.rentBalance?.outstandingAmount ?: 0.0) + (data.tenancy?.monthlyRent ?: 0.0),
                    dueDate = data.rentBalance?.dueDate ?: "1st",
                    nextPaymentDate = data.rentBalance?.dueDate ?: "1st",
                    rentStatus = TenantRentStatus.PAID,
                    occupancyStatus = TenantOccupancyStatus.ACTIVE,
                    unreadNotifications = data.notifications.size,
                    unreadMessages = data.unreadMessages,
                    maintenanceRequests = data.maintenanceRequests.size
                )
            }.onFailure {
                // Fallback to simulation for now
                _dashboardState.value = TenantDashboardUIState(
                    tenantName = "Tenant User",
                    apartmentName = "Searching for Home",
                    apartmentId = "",
                    houseNumber = "N/A",
                    floorNumber = "0",
                    houseType = "N/A",
                    location = "Nairobi",
                    landlordId = "",
                    landlordName = "Property Admin",
                    landlordPhone = "N/A",
                    monthlyRent = 0.0,
                    waterBill = 0.0,
                    garbageFee = 0.0,
                    serviceCharge = 0.0,
                    previousArrears = 0.0,
                    amountPaid = 0.0,
                    outstandingAmount = 0.0,
                    totalDue = 0.0,
                    dueDate = "-",
                    nextPaymentDate = "-",
                    rentStatus = TenantRentStatus.PAID,
                    occupancyStatus = TenantOccupancyStatus.PENDING_VERIFICATION,
                    unreadNotifications = 0,
                    unreadMessages = 0,
                    maintenanceRequests = 0
                )
            }
        }
    }

    private fun loadBills() {
        viewModelScope.launch {
            _bills.value = listOf(
                TenantBillUIModel("1", "Water Bill", "Aug 2026", 500.0, "1st Sept", TenantBillStatus.PENDING, TenantBillType.WATER, "Aug 2026", "15 Aug"),
                TenantBillUIModel("2", "Garbage", "Aug 2026", 200.0, "1st Sept", TenantBillStatus.PENDING, TenantBillType.GARBAGE, "Aug 2026", "15 Aug"),
                TenantBillUIModel("3", "Rent", "Aug 2026", 15000.0, "1st Sept", TenantBillStatus.PAID, TenantBillType.RENT, "Aug 2026", "1 Aug")
            )
        }
    }

    private fun loadMaintenanceRequests() {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId() ?: return@launch
            tenantRepository.getMaintenanceRequests(userId).onSuccess { data ->
                _maintenanceRequests.value = data.map { item ->
                    MaintenanceRequestUIModel(
                        id = item.id,
                        category = try { MaintenanceCategory.valueOf(item.category) } catch(e: Exception) { MaintenanceCategory.OTHER },
                        priority = try { MaintenancePriority.valueOf(item.priority) } catch(e: Exception) { MaintenancePriority.MEDIUM },
                        location = "Unit ${item.unitName ?: ""}",
                        description = item.title,
                        status = try { MaintenanceStatus.valueOf(item.status) } catch(e: Exception) { MaintenanceStatus.SUBMITTED },
                        createdAt = item.createdAt,
                        assignedTo = null,
                        assignedPhone = null
                    )
                }
            }
        }
    }

    fun payBill(bill: TenantBillUIModel) {
        // Implementation for payment
    }

    fun reportMaintenance(request: MaintenanceRequestUIModel) {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId() ?: return@launch
            val data = MaintenanceRequestData(
                title = request.description,
                description = request.description,
                category = request.category.name,
                priority = request.priority.name
            )
            tenantRepository.createMaintenanceRequest(userId, data).onSuccess {
                loadMaintenanceRequests()
            }
        }
    }
}
