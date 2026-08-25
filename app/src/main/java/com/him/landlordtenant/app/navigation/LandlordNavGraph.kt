package com.him.landlordtenant.app.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.him.landlordtenant.app.ui.screens.landlord.*
import com.him.landlordtenant.app.ui.screens.landlord.apartment.*
import com.him.landlordtenant.app.ui.screens.landlord.tenants.TenantsScreen
import com.him.landlordtenant.app.ui.screens.landlord.billing.BillingDashboardScreen
import com.him.landlordtenant.app.ui.screens.landlord.maintenance.MaintenanceRequestsScreen
import com.him.landlordtenant.app.ui.screens.tenant.LandlordDashboardUIState
import com.him.landlordtenant.app.ui.screens.tenant.LandlordBillingUIState
import com.him.landlordtenant.app.ui.screens.landlord.floors.FloorsScreen
import com.him.landlordtenant.app.ui.screens.landlord.floors.AddFloorScreen
import com.him.landlordtenant.app.ui.screens.landlord.communication.LandlordChatScreen
import com.him.landlordtenant.app.ui.viewmodel.landlord.LandlordDashboardViewModel
import com.him.landlordtenant.app.ui.viewmodel.landlord.MyApartmentsViewModel

import com.him.landlordtenant.app.ui.screens.landlord.profile.*
import com.him.landlordtenant.app.ui.screens.common.SettingsScreen
import com.him.landlordtenant.app.ui.screens.common.EditProfileScreen
import com.him.landlordtenant.app.ui.screens.communication.ChatListScreen

fun NavGraphBuilder.landlordNavGraph(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    navigation(
        startDestination = Route.LandlordDashboard.route,
        route = "landlord_graph"
    ) {
        composable(Route.Profile.route) {
            LandlordProfileScreen(
                onBack = { navController.popBackStack() },
                onEditProfile = { navController.navigate(Route.EditProfile.route) },
                onSettings = { navController.navigate(Route.Settings.route) },
                onVerification = { navController.navigate(Route.IdentityVerification.route) },
                onLogout = onLogout
            )
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

        composable(Route.LandlordDashboard.route) {
            val viewModel: LandlordDashboardViewModel = hiltViewModel()
            val dashboardData by viewModel.uiState.collectAsState()

            LandlordDashboardScreen(
                dashboardData = dashboardData,
                onNotifications = { navController.navigate(Route.Notifications.route) },
                onProfile = { navController.navigate(Route.Profile.route) },
                onAddProperty = { navController.navigate(Route.CreateApartment.route) },
                onManageProperties = { navController.navigate(Route.MyApartments.route) },
                onTenants = { navController.navigate(Route.LandlordTenants.route) },
                onPayments = { navController.navigate(Route.LandlordBilling.route) },
                onMaintenance = { navController.navigate(Route.LandlordMaintenance.route) },
                onReports = { navController.navigate(Route.LandlordReports.route) },
                onSettings = { navController.navigate(Route.Settings.route) },
                onChat = { navController.navigate(Route.Messages.route) },
                onLogout = onLogout
            )
        }

        composable(Route.Messages.route) {
            ChatListScreen(
                onConversationClick = { id -> 
                    navController.navigate(Route.LandlordChat.createRoute(id)) 
                },
                onNavigateToSettings = { navController.navigate(Route.Settings.route) }
            )
        }

        composable(
            Route.LandlordChat.route,
            arguments = listOf(navArgument("partnerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val partnerId = backStackEntry.arguments?.getString("partnerId")
            LandlordChatScreen(
                partnerId = partnerId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.MyApartments.route) {
            val viewModel: MyApartmentsViewModel = hiltViewModel()
            val apartments by viewModel.apartments.collectAsState()

            MyApartmentsScreen(
                apartments = apartments,
                onBack = { navController.popBackStack() },
                onApartmentClick = { id -> navController.navigate(Route.ApartmentManagement.createRoute(id)) },
                onApartmentPreview = { id -> navController.navigate(Route.ApartmentPreview.createRoute(id)) },
                onAddApartment = { navController.navigate(Route.CreateApartment.route) }
            )
        }

        composable(
            Route.ApartmentManagement.route,
            arguments = listOf(navArgument("apartmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val apartmentId = backStackEntry.arguments?.getString("apartmentId") ?: ""
            ApartmentManagementScreen(
                apartmentId = apartmentId,
                onBack = { navController.popBackStack() },
                onManageFloors = { id -> navController.navigate(Route.ApartmentFloors.createRoute(id)) },
                onManageMedia = { id -> navController.navigate(Route.ApartmentMedia.createRoute(id)) },
                onUpdateLocation = { id -> navController.navigate(Route.ApartmentLocation.createRoute(id)) },
                onEditDetails = { id -> navController.navigate(Route.EditApartment.createRoute(id)) }
            )
        }

        composable(
            Route.ApartmentPreview.route,
            arguments = listOf(navArgument("apartmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val apartmentId = backStackEntry.arguments?.getString("apartmentId") ?: ""
            ApartmentPreviewScreen(
                apartment = com.him.landlordtenant.app.ui.screens.tenant.TenantApartmentUIModel(
                    id = apartmentId,
                    name = "Green Valley Apartments",
                    county = "Nairobi",
                    location = "Kilimani",
                    description = "Modern living",
                    availableUnits = 5,
                    totalUnits = 24,
                    startingRent = 15000.0,
                    highestRent = 25000.0,
                    rating = 4.8,
                    verified = true,
                    distanceKm = 1.2,
                    houseTypes = listOf("1BR", "2BR")
                ),
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.CreateApartment.route) {
            CreateApartmentScreen(
                onBack = { navController.popBackStack() },
                onCreated = { id ->
                    navController.navigate(Route.ApartmentManagement.createRoute(id)) {
                        popUpTo(Route.CreateApartment.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(
            Route.ApartmentFloors.route,
            arguments = listOf(navArgument("apartmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val apartmentId = backStackEntry.arguments?.getString("apartmentId") ?: ""
            FloorsScreen(
                apartmentName = "Apartment $apartmentId",
                onBack = { navController.popBackStack() },
                onAddFloor = { navController.navigate(Route.AddFloor.createRoute(apartmentId)) },
                onFloorClick = { /* TODO */ }
            )
        }

        composable(
            Route.AddFloor.route,
            arguments = listOf(navArgument("apartmentId") { type = NavType.StringType })
        ) {
            AddFloorScreen(
                onBack = { navController.popBackStack() },
                onSave = { _, _ -> navController.popBackStack() }
            )
        }

        composable(
            Route.ApartmentMedia.route,
            arguments = listOf(navArgument("apartmentId") { type = NavType.StringType })
        ) {
            ApartmentMediaScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            Route.ApartmentLocation.route,
            arguments = listOf(navArgument("apartmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val apartmentId = backStackEntry.arguments?.getString("apartmentId") ?: ""
            ApartmentLocationScreen(
                apartmentId = apartmentId,
                onBack = { navController.popBackStack() },
                onSave = { _, _, _ -> navController.popBackStack() }
            )
        }

        composable(
            Route.EditApartment.route,
            arguments = listOf(navArgument("apartmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val apartmentId = backStackEntry.arguments?.getString("apartmentId") ?: ""
            EditApartmentScreen(
                apartmentId = apartmentId,
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() }
            )
        }

        composable(Route.LandlordTenants.route) {
            TenantsScreen(
                tenants = emptyList(),
                onBack = { navController.popBackStack() },
                onTenantClick = { id -> /* TODO */ },
                onInviteTenant = { /* TODO */ }
            )
        }

        composable(Route.LandlordBilling.route) {
            BillingDashboardScreen(
                billingData = LandlordBillingUIState(
                    totalOutstanding = "KSh 0",
                    thisMonthCollection = "KSh 0",
                    arrears = "KSh 0",
                    collectedAmount = "KSh 0",
                    pendingAmount = "KSh 0",
                    overdueAmount = "KSh 0"
                ),
                onBack = { navController.popBackStack() },
                onPaymentHistory = { /* TODO */ },
                onReceipts = { /* TODO */ },
                onFinancialSettings = { /* TODO */ }
            )
        }

        composable(Route.LandlordMaintenance.route) {
            MaintenanceRequestsScreen(
                requests = emptyList(),
                onBack = { navController.popBackStack() },
                onRequestClick = { /* TODO */ }
            )
        }

        composable(Route.LandlordReports.route) {
            com.him.landlordtenant.app.ui.screens.landlord.reports.ReportsDashboardScreen(
                onBack = { navController.popBackStack() },
                onRevenueReport = { /* TODO */ },
                onPaymentReport = { /* TODO */ },
                onOccupancyReport = { /* TODO */ },
                onExpensesReport = { /* TODO */ }
            )
        }
    }
}
