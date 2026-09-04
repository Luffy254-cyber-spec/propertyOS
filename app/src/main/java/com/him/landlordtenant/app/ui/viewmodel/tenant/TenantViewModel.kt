package com.him.landlordtenant.app.ui.viewmodel.tenant

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.interfaces.*
import com.him.landlordtenant.app.ui.screens.tenant.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Receipt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TenantViewModel @Inject constructor(
    private val tenantRepository: TenantRepository,
    private val authRepository: AuthRepository,
    private val documentRepository: DocumentRepository,
    private val propertyListingRepository: PropertyListingRepository,
    private val billingRepository: PropertyBillingRepository,
    private val activityRepository: ActivityRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _dashboardState = MutableStateFlow<TenantDashboardUIState?>(null)
    val dashboardState: StateFlow<TenantDashboardUIState?> = _dashboardState.asStateFlow()

    private val _bills = MutableStateFlow<List<TenantBillUIModel>>(emptyList())
    val bills: StateFlow<List<TenantBillUIModel>> = _bills.asStateFlow()

    private val _maintenanceRequests = MutableStateFlow<List<MaintenanceRequestUIModel>>(emptyList())
    val maintenanceRequests: StateFlow<List<MaintenanceRequestUIModel>> = _maintenanceRequests.asStateFlow()

    private val _featuredApartments = MutableStateFlow<List<TenantApartmentUIModel>>(emptyList())
    val featuredApartments: StateFlow<List<TenantApartmentUIModel>> = _featuredApartments.asStateFlow()

    private val _unreadMessages = MutableStateFlow(0)
    val unreadMessages: StateFlow<Int> = _unreadMessages.asStateFlow()

    init {
        loadDashboardData()
        loadBills()
        loadMaintenanceRequests()
        loadFeaturedApartments()
        observeUnreadMessages()
    }

    private fun observeUnreadMessages() {
        val userId = authRepository.getCurrentUserId() ?: return
        chatRepository.getTotalUnreadCount(userId)
            .onEach { _unreadMessages.value = it }
            .launchIn(viewModelScope)
    }

    private fun loadFeaturedApartments() {
        viewModelScope.launch {
            propertyListingRepository.getFeaturedListings(limit = 10).onSuccess { listings ->
                val activeListings = listings.filter { it.status == ListingStatus.PUBLISHED }
                _featuredApartments.value = activeListings.map { listing ->
                    TenantApartmentUIModel(
                        id = listing.id,
                        name = listing.title,
                        county = listing.location.county,
                        location = listing.location.town,
                        description = listing.description,
                        availableUnits = listing.availableUnits,
                        totalUnits = listing.totalUnits,
                        startingRent = listing.monthlyRent ?: 0.0,
                        highestRent = listing.monthlyRent ?: 0.0,
                        rating = 4.8, // Initial rating
                        verified = listing.verified,
                        distanceKm = 0.0,
                        houseTypes = emptyList(),
                        images = listing.media.map { it.fileUrl }
                    )
                }
                
                // Update dashboard state with featured apartments
                val current = _dashboardState.value
                if (current != null) {
                    _dashboardState.value = current.copy(featuredApartments = _featuredApartments.value)
                }
            }
        }
    }

    fun loadDashboardData() {
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
                    rentStatus = data.rentBalance?.let { if (it.outstandingAmount <= 0) TenantRentStatus.PAID else TenantRentStatus.DUE_SOON } ?: TenantRentStatus.PAID,
                    occupancyStatus = if (data.tenancy != null) TenantOccupancyStatus.ACTIVE else TenantOccupancyStatus.INACTIVE,
                    unreadNotifications = data.notifications.size,
                    unreadMessages = data.unreadMessages,
                    maintenanceRequests = data.maintenanceRequests.size,
                    loyaltyPoints = 0, 
                    featuredApartments = _featuredApartments.value,
                    moveInChecklist = listOf(
                        ChecklistItem("1", "Pick a vacant house", data.tenancy?.unitName != "N/A" && data.tenancy?.unitName != null, "PHYSICAL"),
                        ChecklistItem("2", "Sign digital agreement", data.tenancy != null, "DOCUMENTS"),
                        ChecklistItem("3", "Upload National ID", data.tenancy != null, "DOCUMENTS"),
                        ChecklistItem("4", "Pay security deposit", (data.rentBalance?.amountPaid ?: 0.0) > 0, "UTILITIES"),
                        ChecklistItem("5", "Set up utility accounts", false, "UTILITIES")
                    )
                )
            }.onFailure {
                _dashboardState.value = null
            }
        }
    }

    private fun loadBills() {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId() ?: return@launch
            billingRepository.getActiveInvoices(userId).onSuccess { invoices ->
                val sdf = java.text.SimpleDateFormat("dd MMM, yyyy", java.util.Locale.getDefault())
                _bills.value = invoices.map { invoice ->
                    TenantBillUIModel(
                        id = invoice.id,
                        title = "Invoice #${invoice.invoiceNumber}",
                        description = "For ${invoice.month}/${invoice.year}",
                        amount = invoice.outstandingAmount,
                        dueDate = sdf.format(java.util.Date(invoice.dueDate)),
                        status = try { TenantBillStatus.valueOf(invoice.status.name) } catch(e: Exception) { TenantBillStatus.PENDING },
                        type = TenantBillType.RENT,
                        icon = Icons.Default.Receipt
                    )
                }
            }
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
                activityRepository.logActivity(
                    userId = userId,
                    activity = CreateActivityData(
                        title = "Maintenance Reported",
                        subtitle = request.description,
                        type = "MAINTENANCE",
                        status = "SUCCESS"
                    )
                )
                loadMaintenanceRequests()
            }
        }
    }

    fun pickHouse(house: TenantHouseUIModel) {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId() ?: return@launch
            val apartmentId = _dashboardState.value?.apartmentId ?: return@launch
            
            tenantRepository.pickHouse(userId, apartmentId, house.houseId).onSuccess {
                activityRepository.logActivity(
                    userId = userId,
                    activity = CreateActivityData(
                        title = "House Picked",
                        subtitle = "Unit: ${house.houseNumber}",
                        type = "TENANT",
                        status = "SUCCESS"
                    )
                )
                loadDashboardData()
            }
        }
    }
}
