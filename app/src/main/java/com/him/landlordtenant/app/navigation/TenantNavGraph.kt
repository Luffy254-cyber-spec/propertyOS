package com.him.landlordtenant.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.him.landlordtenant.app.ui.screens.tenant.*
import com.him.landlordtenant.app.ui.screens.tenant.agreement.AgreementScreen
import com.him.landlordtenant.app.ui.screens.tenant.apartment.ApartmentDetailsScreen
import com.him.landlordtenant.app.ui.screens.tenant.bills.BillsScreen
import com.him.landlordtenant.app.ui.screens.tenant.emergency.EmergencyScreen
import com.him.landlordtenant.app.ui.screens.tenant.house.HouseSelectionScreen
import com.him.landlordtenant.app.ui.screens.tenant.lease.VacateRequestScreen
import com.him.landlordtenant.app.ui.screens.tenant.payment.PaymentScreen
import com.him.landlordtenant.app.ui.screens.tenant.profile.TenantProfileScreen
import com.him.landlordtenant.app.ui.screens.common.SettingsScreen
import com.him.landlordtenant.app.ui.screens.common.EditProfileScreen
import com.him.landlordtenant.app.ui.screens.tenant.search.ApartmentSearchScreen
import com.him.landlordtenant.app.ui.screens.communication.ChatListScreen
import com.him.landlordtenant.app.ui.screens.tenant.communication.TenantChatScreen
import com.him.landlordtenant.app.ui.viewmodel.tenant.TenantViewModel

fun NavGraphBuilder.tenantNavGraph(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    navigation(
        startDestination = Route.TenantHome.route,
        route = "tenant_graph"
    ) {
        composable(Route.TenantHome.route) {
            val tenantViewModel: TenantViewModel = viewModel()
            val dashboardState by tenantViewModel.dashboardState.collectAsState()
            val bills by tenantViewModel.bills.collectAsState()

            dashboardState?.let { data ->
                TenantHomeScreen(
                    tenant = data,
                    bills = bills,
                    quickActions = listOf(
                        TenantQuickActionUIModel(title = "Pay Rent", subtitle = "Pay your monthly rent", icon = Icons.Default.Payment),
                        TenantQuickActionUIModel(title = "Pay Bills", subtitle = "Water, power and others", icon = Icons.AutoMirrored.Filled.ReceiptLong),
                        TenantQuickActionUIModel(title = "Find House", subtitle = "Search vacant houses", icon = Icons.Default.Search),
                        TenantQuickActionUIModel(title = "Maintenance", subtitle = "Report a problem", icon = Icons.Default.Build)
                    ),
                    onSearchApartments = { navController.navigate(Route.TenantSearch.route) },
                    onAvailableHouses = { navController.navigate(Route.HouseSelection.createRoute(data.apartmentId)) },
                    onPayRent = { navController.navigate(Route.TenantPayments.route) },
                    onPayBills = { navController.navigate(Route.TenantBills.route) },
                    onPaymentHistory = { /* TODO */ },
                    onNotifications = { navController.navigate(Route.Notifications.route) },
                    onMessages = { navController.navigate(Route.Messages.route) },
                    onMaintenance = { navController.navigate(Route.TenantMaintenance.route) },
                    onAgreement = { navController.navigate(Route.TenantAgreement.route) },
                    onProfile = { navController.navigate(Route.Profile.route) },
                    onSettings = { navController.navigate(Route.Settings.route) },
                    onLogout = onLogout,
                    onDashboard = { navController.navigate(Route.TenantDashboard.route) }
                )
            }
        }

        composable(Route.TenantDashboard.route) {
            val tenantViewModel: TenantViewModel = viewModel()
            val dashboardState by tenantViewModel.dashboardState.collectAsState()
            
            dashboardState?.let { data ->
                TenantDashboardScreen(
                    dashboardData = data,
                    onNotifications = { navController.navigate(Route.Notifications.route) },
                    onProfile = { navController.navigate(Route.Profile.route) },
                    onPayRent = { navController.navigate(Route.TenantPayments.route) },
                    onBills = { navController.navigate(Route.TenantBills.route) },
                    onMaintenance = { navController.navigate(Route.TenantMaintenance.route) },
                    onEmergency = { navController.navigate(Route.TenantEmergency.route) },
                    onAgreement = { navController.navigate(Route.TenantAgreement.route) },
                    onVacate = { navController.navigate(Route.TenantVacateNotice.route) },
                    onSettings = { navController.navigate(Route.Settings.route) },
                    onLogout = onLogout
                )
            }
        }

        composable(Route.TenantSearch.route) {
            ApartmentSearchScreen(
                onBack = { navController.popBackStack() },
                onApartmentSelected = { id -> 
                    navController.navigate(Route.ApartmentDetails.createRoute(id)) 
                }
            )
        }

        composable(
            Route.ApartmentDetails.route,
            arguments = listOf(navArgument("apartmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val apartmentId = backStackEntry.arguments?.getString("apartmentId") ?: ""
            ApartmentDetailsScreen(
                apartmentId = apartmentId,
                onBack = { navController.popBackStack() },
                onViewHouses = { id -> 
                    navController.navigate(Route.HouseSelection.createRoute(id)) 
                },
                onJoinApartment = { id -> 
                    navController.navigate(Route.TenantJoinAgreement.route)
                }
            )
        }

        composable(
            Route.HouseSelection.route,
            arguments = listOf(navArgument("apartmentId") { type = NavType.StringType })
        ) {
            HouseSelectionScreen(
                apartmentName = "Apartment",
                totalFloors = 5,
                houses = emptyList(),
                onBack = { navController.popBackStack() },
                onJoinHouse = { house -> 
                    navController.navigate(Route.HouseDetails.createRoute(house.houseId)) 
                }
            )
        }

        composable(Route.TenantBills.route) {
            val tenantViewModel: TenantViewModel = viewModel()
            val bills by tenantViewModel.bills.collectAsState()
            BillsScreen(
                apartmentName = "My Apartment",
                houseNumber = "G2",
                bills = bills,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.TenantRentAndBills.route) {
            val tenantViewModel: TenantViewModel = viewModel()
            val dashboardState by tenantViewModel.dashboardState.collectAsState()
            val bills by tenantViewModel.bills.collectAsState()
            
            dashboardState?.let { data ->
                TenantRentAndBillsScreen(
                    rent = TenantRentSummaryUIModel(
                        houseName = data.apartmentName,
                        monthlyRent = data.monthlyRent,
                        dueDate = data.dueDate,
                        arrears = data.outstandingAmount
                    ),
                    bills = bills,
                    onBack = { navController.popBackStack() },
                    onPayNow = { /* navController.navigate(Route.TenantPayments.route) */ },
                    onViewReceipt = { /* TODO */ }
                )
            }
        }

        composable(Route.TenantMaintenance.route) {
            val tenantViewModel: TenantViewModel = viewModel()
            val requests by tenantViewModel.maintenanceRequests.collectAsState()
            com.him.landlordtenant.app.ui.screens.tenant.maintenance.MaintenanceScreen(
                requests = requests,
                onBack = { navController.popBackStack() },
                onCreateRequest = { tenantViewModel.reportMaintenance(it) }
            )
        }

        composable(Route.TenantEmergency.route) {
            EmergencyScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.TenantVacateNotice.route) {
            VacateRequestScreen(
                onBack = { navController.popBackStack() },
                onSubmitted = { navController.popBackStack() }
            )
        }

        composable(Route.TenantJoinAgreement.route) {
            TenantHouseJoinAgreementScreen(
                agreement = TenantAgreementUIModel(
                    agreementId = "JOIN-001",
                    agreementVersion = "1.0",
                    apartmentName = "Apartment",
                    houseNumber = "H1",
                    floorNumber = "0",
                    landlordName = "Landlord",
                    tenantName = "New Tenant",
                    createdDate = "Today",
                    effectiveDate = "1st of Month",
                    monthlyRent = 12000.0,
                    deposit = 12000.0,
                    noticePeriodDays = 30,
                    agreementContent = "Standard joining terms and conditions."
                ),
                onBack = { navController.popBackStack() },
                onAccepted = { navController.navigate(Route.TenantHome.route) }
            )
        }

        composable(Route.TenantAgreement.route) {
            AgreementScreen(
                agreement = TenantAgreementUIModel(
                    agreementId = "AGR-101",
                    agreementVersion = "1.0",
                    apartmentName = "Green Valley",
                    houseNumber = "G2",
                    floorNumber = "1",
                    landlordName = "John Landlord",
                    tenantName = "Jane Tenant",
                    createdDate = "17 Aug",
                    effectiveDate = "1 Sept",
                    monthlyRent = 15000.0,
                    deposit = 15000.0,
                    noticePeriodDays = 30,
                    agreementContent = "Tenancy agreement details..."
                ),
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.TenantPayments.route) {
            val tenantViewModel: TenantViewModel = viewModel()
            val dashboardState by tenantViewModel.dashboardState.collectAsState()
            dashboardState?.let { data ->
                PaymentScreen(
                    paymentData = TenantPaymentUIState(
                        transactionId = "TXN-${System.currentTimeMillis()}",
                        apartmentId = data.apartmentId,
                        houseId = "101",
                        houseNumber = data.houseNumber,
                        landlordName = data.landlordName,
                        rent = data.monthlyRent,
                        deposit = 0.0,
                        water = data.waterBill,
                        garbage = data.garbageFee,
                        serviceCharge = data.serviceCharge
                    ),
                    onBack = { navController.popBackStack() },
                    onPaymentSuccessful = { 
                        navController.navigate(Route.TenantHome.route) {
                            popUpTo(Route.TenantHome.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Route.Profile.route) {
            val tenantViewModel: TenantViewModel = viewModel()
            val dashboardState by tenantViewModel.dashboardState.collectAsState()
            dashboardState?.let { data ->
                TenantProfileScreen(
                    tenant = data,
                    onBack = { navController.popBackStack() },
                    onLogout = onLogout,
                    onEditProfile = { navController.navigate(Route.EditProfile.route) },
                    onSettings = { navController.navigate(Route.Settings.route) }
                )
            }
        }

        composable(Route.EditProfile.route) {
            EditProfileScreen(onBack = { navController.popBackStack() })
        }

        composable(Route.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onNavigateToAbout = { navController.navigate(Route.About.route) },
                onNavigateToPrivacy = { navController.navigate(Route.PrivacyPolicy.route) },
                onNavigateToTerms = { navController.navigate(Route.TermsOfService.route) },
                onNavigateToHelp = { navController.navigate(Route.HelpCenter.route) },
                onLogout = onLogout
            )
        }

        composable(Route.Notifications.route) {
            com.him.landlordtenant.app.ui.screens.notifications.NotificationSettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.Messages.route) {
            ChatListScreen(
                onConversationClick = { id -> 
                    navController.navigate(Route.TenantChat.createRoute(id)) 
                },
                onNavigateToSettings = { navController.navigate(Route.Settings.route) }
            )
        }

        composable(
            Route.TenantChat.route,
            arguments = listOf(navArgument("partnerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val partnerId = backStackEntry.arguments?.getString("partnerId")
            val authViewModel: com.him.landlordtenant.app.ui.viewmodel.auth.AuthViewModel = hiltViewModel()
            val userId = authViewModel.currentUser.collectAsState().value?.id ?: ""
            
            TenantChatScreen(
                currentUserId = userId,
                partnerId = partnerId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
