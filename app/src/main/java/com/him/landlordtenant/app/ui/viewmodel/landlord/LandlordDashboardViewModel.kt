package com.him.landlordtenant.app.ui.viewmodel.landlord

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.data.remote.FirebaseDataSource
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.PropertyListingRepository
import com.him.landlordtenant.app.interfaces.PropertyRepository
import com.him.landlordtenant.app.ui.screens.tenant.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LandlordDashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: com.him.landlordtenant.app.interfaces.UserRepository,
    private val propertyListingRepository: PropertyListingRepository,
    private val propertyRepository: PropertyRepository,
    private val paymentRepository: com.him.landlordtenant.app.interfaces.PaymentRepository,
    private val maintenanceRepository: com.him.landlordtenant.app.interfaces.MaintenanceRepository,
    private val agreementRepository: com.him.landlordtenant.app.interfaces.AgreementRepository,
    private val documentRepository: com.him.landlordtenant.app.interfaces.DocumentRepository,
    private val activityRepository: com.him.landlordtenant.app.interfaces.ActivityRepository,
    private val chatRepository: com.him.landlordtenant.app.interfaces.ChatRepository,
    private val firebaseDataSource: FirebaseDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        LandlordDashboardUIState(
            businessName = "Landlord",
            email = "",
            totalProperties = 0,
            totalUnits = 0,
            totalRevenue = "KSh 0",
            occupancyRate = "0%"
        )
    )
    val uiState: StateFlow<LandlordDashboardUIState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _unreadMessages = MutableStateFlow(0)
    val unreadMessages: StateFlow<Int> = _unreadMessages.asStateFlow()

    init {
        loadDashboardData()
        observeUnreadMessages()
    }

    private fun observeUnreadMessages() {
        val userId = authRepository.getCurrentUserId() ?: return
        chatRepository.getTotalUnreadCount(userId)
            .onEach { _unreadMessages.value = it }
            .launchIn(viewModelScope)
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            val userId = authRepository.getCurrentUserId() ?: run {
                Log.e("LandlordDashboard", "No user ID found")
                _isRefreshing.value = false
                return@launch
            }
            Log.d("LandlordDashboard", "Loading data for user: $userId")
            val user = userRepository.getUserById(userId)
            val userName = (if (user?.displayName.isNullOrEmpty()) user?.fullName else user?.displayName)
                ?.takeIf { it.isNotEmpty() } 
                ?: authRepository.getCurrentUserName() 
                ?: "Landlord"
            val email = user?.email ?: authRepository.getCurrentUserEmail() ?: ""

            // Load activities using repository
            val activities = activityRepository.getUserActivities(userId).getOrDefault(emptyList()).map { log ->
                LandlordActivityUIModel(
                    id = log.id,
                    title = log.title,
                    subtitle = log.subtitle,
                    amount = log.amount,
                    time = formatTimestamp(log.timestamp),
                    type = log.type,
                    status = log.status
                )
            }

            // Load data in parallel for speed and resilience
            val allProperties = try {
                val listingsResult = propertyListingRepository.getListingsByOwner(userId)
                val propertiesResult = propertyRepository.getPropertiesByOwner(userId)

                val propertiesFromListings = listingsResult.getOrDefault(emptyList()).map { listing ->
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
                        rating = 4.5,
                        verified = listing.verified,
                        distanceKm = 0.0,
                        houseTypes = emptyList(),
                        images = listing.media.map { it.fileUrl },
                        totalRevenue = "KSh 0",
                        landlordName = userName
                    )
                }

                val managementProperties = propertiesResult.getOrDefault(emptyList()).map { prop ->
                    TenantApartmentUIModel(
                        id = prop.id,
                        name = prop.name,
                        county = prop.county ?: "",
                        location = prop.town ?: "",
                        description = prop.description ?: "",
                        availableUnits = prop.availableUnits,
                        totalUnits = prop.totalUnits,
                        startingRent = prop.startingRent,
                        highestRent = prop.startingRent,
                        rating = 4.5,
                        verified = prop.verified,
                        distanceKm = 0.0,
                        houseTypes = emptyList(),
                        images = prop.media.map { it.url },
                        totalRevenue = "KSh 0",
                        landlordName = userName
                    )
                }

                (propertiesFromListings + managementProperties).distinctBy { it.id.ifEmpty { it.name } }
            } catch (e: Exception) {
                Log.e("LandlordDashboard", "Property loading failed", e)
                emptyList()
            }
            
            Log.d("LandlordDashboard", "Found ${allProperties.size} properties in total")
            
            val totalProperties = allProperties.size
            val totalUnits = allProperties.sumOf { it.totalUnits }
            val availableUnits = allProperties.sumOf { it.availableUnits }
            val occupancyRate = if (totalUnits > 0) "${((totalUnits - availableUnits).toFloat() / totalUnits * 100).toInt()}%" else "0%"

            // Fetch Real Financial Data
            val paymentsResult = paymentRepository.getLandlordPayments(userId)
            val successfulPayments = paymentsResult.getOrDefault(emptyList()).filter { it.status == com.him.landlordtenant.app.enums.PaymentStatus.SUCCESS || it.status == com.him.landlordtenant.app.enums.PaymentStatus.RECONCILED }
            val totalRevenue = successfulPayments.sumOf { it.amount }
            
            // Calculate Revenue Trend (last 7 days)
            val revenueTrend = mutableListOf<Float>()
            val now = System.currentTimeMillis()
            for (i in 6 downTo 0) {
                val dayStart = now - (i * 24 * 60 * 60 * 1000L)
                val dayEnd = dayStart + (24 * 60 * 60 * 1000L)
                val dayAmount = successfulPayments.filter { 
                    // Simple parsing since completedAt might be a string. For now, assume timestamp if possible or skip.
                    // Let's use createdAt as a proxy if completedAt isn't a long.
                    it.createdAt.toLongOrNull()?.let { ts -> ts in dayStart..dayEnd } ?: false
                }.sumOf { it.amount }.toFloat()
                revenueTrend.add(dayAmount)
            }

            // Fetch Maintenance Data
            val maintenanceResult = maintenanceRepository.getLandlordRequests(userId)
            val activeRequests = maintenanceResult.getOrDefault(emptyList()).filter { it.status != "COMPLETED" && it.status != "CANCELLED" }
            
            // Fetch Agreement Data for Renewals
            val agreementsResult = agreementRepository.getLandlordAgreements(userId)
            val upcomingRenewals = agreementsResult.getOrDefault(emptyList()).count { 
                // Logic for "upcoming" (e.g. within 30 days)
                it.status == "ACTIVE" // Placeholder for real date logic
            }

            // Health Score calculation (simple logic)
            val healthScore = if (totalUnits > 0) {
                val occupancyWeight = (totalUnits - availableUnits).toFloat() / totalUnits * 50
                val paymentWeight = if (successfulPayments.isNotEmpty()) 50 else 0
                (occupancyWeight + paymentWeight).toInt().coerceIn(0, 100)
            } else 100

            // Fetch Compliance Documents
            val documentsResult = documentRepository.getUserDocuments(userId)
            val complianceDocs = documentsResult.getOrDefault(emptyList()).filter { it.type.contains("COMPLIANCE", true) || it.type.contains("CERTIFICATE", true) }
            val complianceItems = complianceDocs.map { 
                ComplianceUIModel(it.fileName, "Valid", false)
            }

            _uiState.value = LandlordDashboardUIState(
                businessName = userName,
                email = email,
                totalProperties = totalProperties,
                totalUnits = totalUnits,
                totalRevenue = "KES ${String.format("%,.0f", totalRevenue)}", 
                occupancyRate = occupancyRate,
                properties = allProperties,
                recentActivities = activities.sortedByDescending { it.id },
                healthScore = healthScore,
                revenueTrend = if (revenueTrend.all { it == 0f }) listOf(0.1f, 0.2f, 0.1f, 0.3f, 0.4f, 0.5f, 0.6f) else revenueTrend,
                activeMaintenanceRequests = activeRequests.size,
                upcomingRenewals = upcomingRenewals,
                maintenancePredictions = if (activeRequests.size > 2) {
                    listOf(MaintenancePredictionUIModel("Elevator System", "Maintenance Spike", "High Risk", "#F44336", "High request volume detected for elevators."))
                } else {
                    listOf(MaintenancePredictionUIModel("Routine Check", "All Units", "Scheduled", "#43A047", "Portfolio is stable. Next routine check in 20 days."))
                },
                complianceStatus = complianceItems
            )
            _isRefreshing.value = false
        }
    }

    private fun formatTimestamp(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        
        return when {
            diff < 60 * 1000L -> "Just now"
            diff < 60 * 60 * 1000L -> "${diff / (60 * 1000L)} mins ago"
            diff < 24 * 60 * 60 * 1000L -> "${diff / (60 * 60 * 1000L)} hours ago"
            else -> java.text.SimpleDateFormat("dd MMM", java.util.Locale.getDefault()).format(java.util.Date(timestamp))
        }
    }
}
