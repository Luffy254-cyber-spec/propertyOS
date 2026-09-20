package com.him.landlordtenant.app.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.him.landlordtenant.app.ui.screens.landlord.LandlordDashboardScreen
import com.him.landlordtenant.app.ui.screens.landlord.agreements.AgreementPreviewScreen
import com.him.landlordtenant.app.ui.screens.landlord.agreements.AgreementsScreen
import com.him.landlordtenant.app.ui.screens.landlord.agreements.CreateAgreementScreen
import com.him.landlordtenant.app.ui.screens.landlord.analytics.PropertyAnalyticsScreen
import com.him.landlordtenant.app.ui.screens.landlord.apartment.ApartmentLocationScreen
import com.him.landlordtenant.app.ui.screens.landlord.apartment.ApartmentManagementScreen
import com.him.landlordtenant.app.ui.screens.landlord.apartment.ApartmentMediaScreen
import com.him.landlordtenant.app.ui.screens.landlord.apartment.ApartmentPreviewScreen
import com.him.landlordtenant.app.ui.screens.landlord.apartment.CreateApartmentScreen
import com.him.landlordtenant.app.ui.screens.landlord.apartment.EditApartmentScreen
import com.him.landlordtenant.app.ui.screens.landlord.apartment.MyApartmentsScreen
import com.him.landlordtenant.app.ui.screens.landlord.billing.BillingDashboardScreen
import com.him.landlordtenant.app.ui.screens.landlord.billing.CreateInvoiceScreen
import com.him.landlordtenant.app.ui.screens.landlord.billing.FinancialSettingsScreen
import com.him.landlordtenant.app.ui.screens.landlord.billing.MeterReadingScreen
import com.him.landlordtenant.app.ui.screens.landlord.billing.PaymentHistoryScreen
import com.him.landlordtenant.app.ui.screens.landlord.billing.ReceiptsScreen
import com.him.landlordtenant.app.ui.screens.landlord.communication.LandlordChatScreen
import com.him.landlordtenant.app.ui.screens.landlord.expenses.ExpenseTrackerScreen
import com.him.landlordtenant.app.ui.screens.landlord.floors.AddFloorScreen
import com.him.landlordtenant.app.ui.screens.landlord.floors.FloorUnitsScreen
import com.him.landlordtenant.app.ui.screens.landlord.floors.FloorsScreen
import com.him.landlordtenant.app.ui.screens.landlord.houses.CreateHouseScreen
import com.him.landlordtenant.app.ui.screens.landlord.houses.EditHouseScreen
import com.him.landlordtenant.app.ui.screens.landlord.houses.HouseManagementScreen
import com.him.landlordtenant.app.ui.screens.landlord.houses.HouseMediaScreen
import com.him.landlordtenant.app.ui.screens.landlord.houses.HouseStatusScreen
import com.him.landlordtenant.app.ui.screens.landlord.maintenance.MaintenanceDashboardScreen
import com.him.landlordtenant.app.ui.screens.landlord.maintenance.MaintenanceDetailsScreen
import com.him.landlordtenant.app.ui.screens.landlord.maintenance.MaintenanceRequestsScreen
import com.him.landlordtenant.app.ui.screens.landlord.marketing.MarketingToolsScreen
import com.him.landlordtenant.app.ui.screens.landlord.profile.LandlordProfileScreen
import com.him.landlordtenant.app.ui.screens.landlord.reports.ExpensesReportScreen
import com.him.landlordtenant.app.ui.screens.landlord.reports.OccupancyReportScreen
import com.him.landlordtenant.app.ui.screens.landlord.reports.PaymentReportScreen
import com.him.landlordtenant.app.ui.screens.landlord.reports.ReportsDashboardScreen
import com.him.landlordtenant.app.ui.screens.landlord.reports.RevenueReportScreen
import com.him.landlordtenant.app.ui.screens.landlord.staff.LandlordStaffScreen
import com.him.landlordtenant.app.ui.screens.landlord.tenants.FormerTenantsScreen
import com.him.landlordtenant.app.ui.screens.landlord.tenants.PendingTenantsScreen
import com.him.landlordtenant.app.ui.screens.landlord.tenants.TenantDetailsScreen
import com.him.landlordtenant.app.ui.screens.landlord.tenants.TenantsScreen
import com.him.landlordtenant.app.ui.screens.landlord.vault.LandlordDocumentsScreen
import com.him.landlordtenant.app.ui.screens.landlord.viewings.ViewingRequestsScreen
import com.him.landlordtenant.app.ui.screens.common.EditProfileScreen
import com.him.landlordtenant.app.ui.screens.common.SettingsScreen
import com.him.landlordtenant.app.ui.screens.communication.ChatDetailScreen
import com.him.landlordtenant.app.ui.screens.communication.ChatListScreen
import com.him.landlordtenant.app.ui.screens.communication.NewChatScreen
import com.him.landlordtenant.app.ui.screens.communication.PropertyCommunityScreen
import com.him.landlordtenant.app.ui.screens.notifications.NotificationSettingsScreen
import com.him.landlordtenant.app.ui.viewmodel.landlord.AgreementsViewModel
import com.him.landlordtenant.app.ui.viewmodel.landlord.HouseManagementViewModel
import com.him.landlordtenant.app.ui.viewmodel.landlord.LandlordDashboardViewModel
import com.him.landlordtenant.app.ui.viewmodel.landlord.LandlordMaintenanceViewModel
import com.him.landlordtenant.app.ui.viewmodel.landlord.LandlordTenantsViewModel
import com.him.landlordtenant.app.ui.viewmodel.landlord.MaintenanceDetailsViewModel
import com.him.landlordtenant.app.ui.viewmodel.landlord.MyApartmentsViewModel
import com.him.landlordtenant.app.ui.viewmodel.landlord.TenantDetailsViewModel
import com.him.landlordtenant.app.ui.viewmodel.landlord.UnitTenantManagementViewModel
import com.him.landlordtenant.app.ui.screens.landlord.houses.UnitTenantManagementScreen

fun NavGraphBuilder.landlordNavGraph(
    navController: NavHostController,
    onLogout: () -> Unit,
    onSwitchRole: () -> Unit
) {
    navigation(
        startDestination = Route.LandlordDashboard.route,
        route = "landlord_graph"
    ) {
        composable(Route.LandlordHome.route) {
            navController.navigate(Route.LandlordDashboard.route) {
                popUpTo(Route.LandlordHome.route) { inclusive = true }
            }
        }
        
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
            NotificationSettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.LandlordDashboard.route) {
            val viewModel: LandlordDashboardViewModel = hiltViewModel()
            val dashboardData by viewModel.uiState.collectAsState()
            val unreadMessages by viewModel.unreadMessages.collectAsState()

            LandlordDashboardScreen(
                dashboardData = dashboardData,
                unreadMessages = unreadMessages,
                onNotifications = { navController.navigate(Route.Notifications.route) },
                onProfile = { navController.navigate(Route.Profile.route) },
                onAddProperty = { navController.navigate(Route.CreateApartment.route) },
                onManageProperties = { navController.navigate(Route.MyApartments.route) },
                onMarketplace = { navController.navigate(Route.TenantSearch.route) },
                onEditProperty = { id -> navController.navigate(Route.ApartmentManagement.createRoute(id)) },
                onTenants = { navController.navigate(Route.LandlordTenants.route) },
                onPayments = { navController.navigate(Route.LandlordBilling.route) },
                onMaintenance = { navController.navigate(Route.MaintenanceDashboard.route) },
                onReports = { navController.navigate(Route.LandlordReports.route) },
                onStaff = { navController.navigate(Route.LandlordStaff.route) },
                onExpenses = { navController.navigate(Route.LandlordExpenses.route) },
                onMarketing = { navController.navigate(Route.MarketingTools.route) },
                onDocuments = { navController.navigate(Route.LandlordDocuments.route) },
                onViewings = { navController.navigate(Route.ViewingRequests.route) },
                onSettings = { navController.navigate(Route.Settings.route) },
                onMessages = { navController.navigate(Route.Messages.route) },
                onChat = { navController.navigate(Route.Messages.route) },
                onAgreements = { navController.navigate(Route.LandlordAgreements.route) },
                onLogout = onLogout,
                onSwitchRole = onSwitchRole,
                onRefresh = { viewModel.loadDashboardData() }
            )
        }

        composable(Route.LandlordStaff.route) {
            LandlordStaffScreen(onBack = { navController.popBackStack() })
        }

        composable(Route.LandlordExpenses.route) {
            ExpenseTrackerScreen(onBack = { navController.popBackStack() })
        }

        composable(Route.MarketingTools.route) {
            MarketingToolsScreen(onBack = { navController.popBackStack() })
        }

        composable(Route.LandlordDocuments.route) {
            LandlordDocumentsScreen(onBack = { navController.popBackStack() })
        }

        composable(Route.ViewingRequests.route) {
            ViewingRequestsScreen(onBack = { navController.popBackStack() })
        }

        composable(
            Route.PropertyAnalytics.route,
            arguments = listOf(navArgument("propertyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: ""
            PropertyAnalyticsScreen(propertyId = propertyId, onBack = { navController.popBackStack() })
        }

        composable(Route.Messages.route) {
            ChatListScreen(
                onConversationClick = { id -> 
                    navController.navigate(Route.ChatDetail.createRoute(id)) 
                },
                onNavigateToSettings = { navController.navigate(Route.Settings.route) },
                onNewChat = { navController.navigate(Route.NewChat.route) }
            )
        }

        composable(Route.NewChat.route) {
            NewChatScreen(
                onBack = { navController.popBackStack() },
                onUserSelected = { userId ->
                    navController.navigate(Route.LandlordChat.createRoute(userId)) {
                        popUpTo(Route.NewChat.route) { inclusive = true }
                    }
                },
                onPropertyCommunity = { propertyId, isAnnouncementsOnly ->
                    navController.navigate(Route.PropertyCommunity.createRoute(propertyId, isAnnouncementsOnly))
                }
            )
        }

        composable(
            Route.PropertyCommunity.route,
            arguments = listOf(
                navArgument("propertyId") { type = NavType.StringType },
                navArgument("isAnnouncementsOnly") { type = NavType.BoolType; defaultValue = false }
            )
        ) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: ""
            val isAnnouncementsOnly = backStackEntry.arguments?.getBoolean("isAnnouncementsOnly") ?: false
            PropertyCommunityScreen(
                propertyId = propertyId,
                onBack = { navController.popBackStack() },
                isAnnouncementsOnly = isAnnouncementsOnly,
                onNavigateToChat = { chatId -> navController.navigate(Route.ChatDetail.createRoute(chatId)) }
            )
        }

        composable(
            Route.ChatDetail.route,
            arguments = listOf(navArgument("conversationId") { type = NavType.StringType })
        ) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getString("conversationId") ?: ""
            ChatDetailScreen(
                conversationId = conversationId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            Route.LandlordChat.route,
            arguments = listOf(navArgument("partnerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val partnerId = backStackEntry.arguments?.getString("partnerId") ?: ""
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
                onAddApartment = { navController.navigate(Route.CreateApartment.route) },
                onRefresh = { viewModel.refresh() }
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
                onEditDetails = { id -> navController.navigate(Route.EditApartment.createRoute(id)) },
                onFinancialSettings = { id -> navController.navigate(Route.FinancialSettings.createRoute(id)) },
                onViewAnalytics = { id -> navController.navigate(Route.PropertyAnalytics.createRoute(id)) }
            )
        }
        
        composable(
            Route.FinancialSettings.route,
            arguments = listOf(navArgument("propertyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: ""
            FinancialSettingsScreen(
                propertyId = propertyId,
                onBack = { navController.popBackStack() }
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
                    name = "Property Preview",
                    county = "Location",
                    location = "Area",
                    description = "Property description",
                    availableUnits = 0,
                    totalUnits = 0,
                    startingRent = 0.0,
                    highestRent = 0.0,
                    rating = 0.0,
                    verified = true,
                    distanceKm = 0.0,
                    houseTypes = emptyList()
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
                apartmentId = apartmentId,
                onBack = { navController.popBackStack() },
                onAddFloor = { navController.navigate(Route.AddFloor.createRoute(apartmentId)) },
                onAddHouseToFloor = { aid, fid -> navController.navigate(Route.CreateHouse.createRoute(aid, fid)) },
                onShowHouses = { aid, fid -> navController.navigate(Route.FloorUnits.createRoute(aid, fid)) }
            )
        }

        composable(
            Route.CreateHouse.route,
            arguments = listOf(
                navArgument("apartmentId") { type = NavType.StringType },
                navArgument("floorId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val apartmentId = backStackEntry.arguments?.getString("apartmentId") ?: ""
            val floorId = backStackEntry.arguments?.getString("floorId") ?: ""
            CreateHouseScreen(
                apartmentId = apartmentId,
                floorId = floorId,
                onBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        composable(
            Route.FloorUnits.route,
            arguments = listOf(
                navArgument("apartmentId") { type = NavType.StringType },
                navArgument("floorId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val apartmentId = backStackEntry.arguments?.getString("apartmentId") ?: ""
            val floorId = backStackEntry.arguments?.getString("floorId") ?: ""
            FloorUnitsScreen(
                apartmentId = apartmentId,
                floorId = floorId,
                onBack = { navController.popBackStack() },
                onAddHouse = { aid, fid -> navController.navigate(Route.CreateHouse.createRoute(aid, fid)) },
                onHouseClick = { aid, fid, hid -> navController.navigate(Route.HouseManagement.createRoute(aid, fid, hid)) }
            )
        }

        composable(
            Route.HouseManagement.route,
            arguments = listOf(
                navArgument("apartmentId") { type = NavType.StringType },
                navArgument("floorId") { type = NavType.StringType },
                navArgument("houseId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val apartmentId = backStackEntry.arguments?.getString("apartmentId") ?: ""
            val floorId = backStackEntry.arguments?.getString("floorId") ?: ""
            val houseId = backStackEntry.arguments?.getString("houseId") ?: ""
            
            HouseManagementScreen(
                apartmentId = apartmentId,
                floorId = floorId,
                houseId = houseId,
                onBack = { navController.popBackStack() },
                onEditDetails = { aid, fid, hid -> navController.navigate(Route.EditHouse.createRoute(aid, fid, hid)) },
                onManageMedia = { aid, fid, hid -> navController.navigate(Route.HouseMedia.createRoute(aid, fid, hid)) },
                onUpdateStatus = { aid, fid, hid -> 
                    navController.navigate(Route.HouseStatusUpdate.createRoute(aid, fid, hid)) 
                },
                onManageTenant = { hid -> 
                    navController.navigate(Route.UnitTenantManagement.createRoute(apartmentId, floorId, hid)) 
                },
                onMeterReading = { id -> 
                    navController.navigate(Route.MeterReading.createRoute("House $id", "Current Tenant", "Water", 0.0, 150.0))
                },
                onCreateInvoice = { aid, fid, hid ->
                    navController.navigate(Route.CreateInvoice.createRoute(aid, fid, hid))
                }
            )
        }

        composable(
            Route.EditHouse.route,
            arguments = listOf(
                navArgument("apartmentId") { type = NavType.StringType },
                navArgument("floorId") { type = NavType.StringType },
                navArgument("houseId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val apartmentId = backStackEntry.arguments?.getString("apartmentId") ?: ""
            val floorId = backStackEntry.arguments?.getString("floorId") ?: ""
            val houseId = backStackEntry.arguments?.getString("houseId") ?: ""
            EditHouseScreen(
                apartmentId = apartmentId,
                floorId = floorId,
                houseId = houseId,
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() }
            )
        }

        composable(
            Route.HouseMedia.route,
            arguments = listOf(
                navArgument("apartmentId") { type = NavType.StringType },
                navArgument("floorId") { type = NavType.StringType },
                navArgument("houseId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            HouseMediaScreen(
                onBack = { navController.popBackStack() },
                onAddMedia = { /* TODO */ }
            )
        }

        composable(
            Route.HouseStatusUpdate.route,
            arguments = listOf(
                navArgument("apartmentId") { type = NavType.StringType },
                navArgument("floorId") { type = NavType.StringType },
                navArgument("houseId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val apartmentId = backStackEntry.arguments?.getString("apartmentId") ?: ""
            val floorId = backStackEntry.arguments?.getString("floorId") ?: ""
            val houseId = backStackEntry.arguments?.getString("houseId") ?: ""
            
            HouseStatusScreen(
                apartmentId = apartmentId,
                floorId = floorId,
                houseId = houseId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            Route.CreateInvoice.route,
            arguments = listOf(
                navArgument("apartmentId") { type = NavType.StringType },
                navArgument("floorId") { type = NavType.StringType },
                navArgument("houseId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val apartmentId = backStackEntry.arguments?.getString("apartmentId") ?: ""
            val floorId = backStackEntry.arguments?.getString("floorId") ?: ""
            val houseId = backStackEntry.arguments?.getString("houseId") ?: ""
            
            CreateInvoiceScreen(
                apartmentId = apartmentId,
                floorId = floorId,
                houseId = houseId,
                onBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(
            Route.UnitTenantManagement.route,
            arguments = listOf(
                navArgument("apartmentId") { type = NavType.StringType },
                navArgument("floorId") { type = NavType.StringType },
                navArgument("houseId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val apartmentId = backStackEntry.arguments?.getString("apartmentId") ?: ""
            val floorId = backStackEntry.arguments?.getString("floorId") ?: ""
            val houseId = backStackEntry.arguments?.getString("houseId") ?: ""
            
            UnitTenantManagementScreen(
                apartmentId = apartmentId,
                floorId = floorId,
                houseId = houseId,
                onBack = { navController.popBackStack() },
                onViewTenantDetails = { tenantId -> 
                    navController.navigate(Route.TenantDetails.createRoute(tenantId))
                }
            )
        }

        composable(
            Route.AddFloor.route,
            arguments = listOf(navArgument("apartmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val apartmentId = backStackEntry.arguments?.getString("apartmentId") ?: ""
            AddFloorScreen(
                apartmentId = apartmentId,
                onBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        composable(
            Route.ApartmentMedia.route,
            arguments = listOf(navArgument("apartmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val apartmentId = backStackEntry.arguments?.getString("apartmentId") ?: ""
            ApartmentMediaScreen(
                apartmentId = apartmentId,
                onBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
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
                onSaveSuccess = { navController.popBackStack() }
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
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        composable(Route.LandlordTenants.route) {
            TenantsScreen(
                onBack = { navController.popBackStack() },
                onTenantClick = { id -> navController.navigate(Route.TenantDetails.createRoute(id)) },
                onChatClick = { id -> navController.navigate(Route.LandlordChat.createRoute(id)) },
                onInviteTenant = { /* TODO: Invite tenant logic */ },
                onPendingTenants = { navController.navigate(Route.LandlordPendingTenants.route) },
                onFormerTenants = { navController.navigate(Route.LandlordFormerTenants.route) }
            )
        }

        composable(Route.LandlordPendingTenants.route) {
            PendingTenantsScreen(onBack = { navController.popBackStack() })
        }

        composable(Route.LandlordFormerTenants.route) {
            FormerTenantsScreen(onBack = { navController.popBackStack() })
        }

        composable(
            Route.TenantDetails.route,
            arguments = listOf(navArgument("tenantId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tenantId = backStackEntry.arguments?.getString("tenantId") ?: ""
            val viewModel: TenantDetailsViewModel = hiltViewModel()
            val tenant by viewModel.tenant.collectAsState()

            LaunchedEffect(tenantId) {
                viewModel.loadTenant(tenantId)
            }

            val context = LocalContext.current
            tenant?.let {
                TenantDetailsScreen(
                    tenant = it,
                    onBack = { navController.popBackStack() },
                    onChat = { id -> navController.navigate(Route.LandlordChat.createRoute(id)) },
                    onCall = { phone ->
                        val intent = android.content.Intent(android.content.Intent.ACTION_DIAL, android.net.Uri.parse("tel:$phone"))
                        context.startActivity(intent)
                    },
                    onRemoveTenant = { /* TODO */ }
                )
            }
        }

        composable(Route.LandlordBilling.route) {
            BillingDashboardScreen(
                onBack = { navController.popBackStack() },
                onPaymentHistory = { navController.navigate(Route.PaymentHistory.route) },
                onReceipts = { navController.navigate(Route.Receipts.route) },
                onFinancialSettings = { 
                    navController.navigate(Route.MyApartments.route)
                }
            )
        }

        composable(Route.PaymentHistory.route) {
            PaymentHistoryScreen(onBack = { navController.popBackStack() })
        }

        composable(Route.Receipts.route) {
            ReceiptsScreen(onBack = { navController.popBackStack() })
        }

        composable(
            Route.MeterReading.route,
            arguments = listOf(
                navArgument("unitName") { type = NavType.StringType },
                navArgument("tenantName") { type = NavType.StringType },
                navArgument("meterType") { type = NavType.StringType },
                navArgument("previousReading") { type = NavType.StringType },
                navArgument("ratePerUnit") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val unitName = backStackEntry.arguments?.getString("unitName") ?: ""
            val tenantName = backStackEntry.arguments?.getString("tenantName") ?: ""
            val meterType = backStackEntry.arguments?.getString("meterType") ?: ""
            val previousReading = backStackEntry.arguments?.getString("previousReading")?.toDoubleOrNull() ?: 0.0
            val ratePerUnit = backStackEntry.arguments?.getString("ratePerUnit")?.toDoubleOrNull() ?: 0.0

            MeterReadingScreen(
                unitName = unitName,
                tenantName = tenantName,
                meterType = meterType,
                previousReading = previousReading,
                ratePerUnit = ratePerUnit,
                onBack = { navController.popBackStack() },
                onSave = { _, _, _ -> navController.popBackStack() }
            )
        }

        composable(Route.MaintenanceDashboard.route) {
            MaintenanceDashboardScreen(
                onBack = { navController.popBackStack() },
                onActiveRequests = { navController.navigate(Route.LandlordMaintenance.route) },
                onMaintenanceHistory = { navController.navigate(Route.MaintenanceHistory.route) },
                onManageTechnicians = { navController.navigate(Route.LandlordStaff.route) }
            )
        }

        composable(Route.LandlordMaintenance.route) {
            val viewModel: LandlordMaintenanceViewModel = hiltViewModel()
            val requests by viewModel.requests.collectAsState()
            
            MaintenanceRequestsScreen(
                requests = requests.map { 
                    com.him.landlordtenant.app.ui.screens.tenant.MaintenanceRequestUIModel(
                        id = it.id,
                        category = try { com.him.landlordtenant.app.ui.screens.tenant.MaintenanceCategory.valueOf(it.priority) } catch(e: Exception) { com.him.landlordtenant.app.ui.screens.tenant.MaintenanceCategory.OTHER },
                        priority = try { com.him.landlordtenant.app.ui.screens.tenant.MaintenancePriority.valueOf(it.priority) } catch(e: Exception) { com.him.landlordtenant.app.ui.screens.tenant.MaintenancePriority.MEDIUM },
                        location = it.unitName,
                        description = it.title,
                        status = try { com.him.landlordtenant.app.ui.screens.tenant.MaintenanceStatus.valueOf(it.status) } catch(e: Exception) { com.him.landlordtenant.app.ui.screens.tenant.MaintenanceStatus.SUBMITTED },
                        createdAt = "N/A",
                        assignedTo = it.assignedProfessionalId,
                        assignedPhone = null
                    )
                },
                onBack = { navController.popBackStack() },
                onRequestClick = { id -> navController.navigate(Route.MaintenanceDetails.createRoute(id)) }
            )
        }

        composable(
            Route.MaintenanceDetails.route,
            arguments = listOf(navArgument("requestId") { type = NavType.StringType })
        ) { backStackEntry ->
            val requestId = backStackEntry.arguments?.getString("requestId") ?: ""
            val viewModel: MaintenanceDetailsViewModel = hiltViewModel()
            val request by viewModel.request.collectAsState()

            LaunchedEffect(requestId) {
                viewModel.loadRequest(requestId)
            }

            request?.let {
                MaintenanceDetailsScreen(
                    request = it,
                    onBack = { navController.popBackStack() },
                    onAssign = { /* TODO */ },
                    onUpdateStatus = { /* TODO */ }
                )
            }
        }

        composable(Route.LandlordReports.route) {
            ReportsDashboardScreen(
                onBack = { navController.popBackStack() },
                onRevenueReport = { navController.navigate(Route.RevenueReport.route) },
                onPaymentReport = { navController.navigate(Route.PaymentReport.route) },
                onOccupancyReport = { navController.navigate(Route.OccupancyReport.route) },
                onExpensesReport = { navController.navigate(Route.ExpensesReport.route) }
            )
        }

        composable(Route.RevenueReport.route) {
            RevenueReportScreen(onBack = { navController.popBackStack() })
        }

        composable(Route.PaymentReport.route) {
            PaymentReportScreen(onBack = { navController.popBackStack() })
        }

        composable(Route.OccupancyReport.route) {
            OccupancyReportScreen(onBack = { navController.popBackStack() })
        }

        composable(Route.ExpensesReport.route) {
            ExpensesReportScreen(onBack = { navController.popBackStack() })
        }

        composable(Route.PropertyManagement.route) {
            val viewModel: MyApartmentsViewModel = hiltViewModel()
            val apartments by viewModel.apartments.collectAsState()

            MyApartmentsScreen(
                apartments = apartments,
                onBack = { navController.popBackStack() },
                onApartmentClick = { id -> navController.navigate(Route.ApartmentManagement.createRoute(id)) },
                onApartmentPreview = { id -> navController.navigate(Route.ApartmentPreview.createRoute(id)) },
                onAddApartment = { navController.navigate(Route.CreateApartment.route) },
                onRefresh = { viewModel.refresh() }
            )
        }

        composable(Route.LandlordAgreements.route) {
            val viewModel: AgreementsViewModel = hiltViewModel()
            val agreements by viewModel.agreements.collectAsState()
            
            LaunchedEffect(Unit) {
                viewModel.loadAgreements()
            }

            AgreementsScreen(
                agreements = agreements,
                onBack = { navController.popBackStack() },
                onAgreementClick = { id -> navController.navigate(Route.AgreementPreview.createRoute(id)) },
                onCreateAgreement = { navController.navigate(Route.CreateAgreement.route) }
            )
        }

        composable(Route.CreateAgreement.route) {
            CreateAgreementScreen(
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() }
            )
        }

        composable(
            Route.AgreementPreview.route,
            arguments = listOf(navArgument("agreementId") { type = NavType.StringType })
        ) { backStackEntry ->
            val agreementId = backStackEntry.arguments?.getString("agreementId") ?: ""
            val viewModel: AgreementsViewModel = hiltViewModel()
            val agreement by viewModel.currentAgreement.collectAsState()

            LaunchedEffect(agreementId) {
                viewModel.loadAgreement(agreementId)
            }

            agreement?.let {
                AgreementPreviewScreen(
                    agreement = it,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
