package com.him.landlordtenant.app.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.him.landlordtenant.app.ui.screens.tenant.house.HouseDetailsScreen
import com.him.landlordtenant.app.ui.screens.tenant.lease.VacateRequestScreen
import com.him.landlordtenant.app.ui.screens.tenant.lease.CurrentLeaseScreen
import com.him.landlordtenant.app.ui.screens.tenant.payment.PaymentScreen
import com.him.landlordtenant.app.ui.screens.tenant.payment.ReceiptScreen
import com.him.landlordtenant.app.ui.screens.tenant.profile.TenantProfileScreen
import com.him.landlordtenant.app.ui.screens.common.SettingsScreen
import com.him.landlordtenant.app.ui.screens.common.EditProfileScreen
import com.him.landlordtenant.app.ui.screens.tenant.search.ApartmentSearchScreen
import com.him.landlordtenant.app.ui.screens.communication.ChatDetailScreen
import com.him.landlordtenant.app.ui.screens.communication.ChatListScreen
import com.him.landlordtenant.app.ui.screens.communication.NewChatScreen
import com.him.landlordtenant.app.ui.screens.communication.PropertyCommunityScreen
import com.him.landlordtenant.app.ui.screens.tenant.communication.TenantChatScreen
import com.him.landlordtenant.app.ui.screens.tenant.services.TenantServicesScreen
import com.him.landlordtenant.app.ui.screens.tenant.amenities.AmenityBookingScreen
import com.him.landlordtenant.app.ui.screens.tenant.maintenance.MaintenanceHistoryScreen
import com.him.landlordtenant.app.ui.screens.tenant.rewards.TenantRewardsScreen
import com.him.landlordtenant.app.ui.screens.tenant.utilities.UtilityUsageScreen
import com.him.landlordtenant.app.ui.screens.tenant.vault.DocumentVaultScreen
import com.him.landlordtenant.app.ui.screens.tenant.reviews.PropertyReviewsScreen
import com.him.landlordtenant.app.ui.viewmodel.tenant.*

fun NavGraphBuilder.tenantNavGraph(
    navController: NavHostController,
    onLogout: () -> Unit,
    onSwitchRole: () -> Unit
) {
    navigation(
        startDestination = Route.TenantHome.route,
        route = "tenant_graph"
    ) {
        composable(Route.TenantHome.route) {
            val tenantViewModel: TenantViewModel = hiltViewModel()
            val dashboardState by tenantViewModel.dashboardState.collectAsState()
            val bills by tenantViewModel.bills.collectAsState()
            val unreadMessages by tenantViewModel.unreadMessages.collectAsState()

            // Redirect to search if no apartment is assigned
            LaunchedEffect(dashboardState) {
                if (dashboardState != null && dashboardState!!.apartmentId.isEmpty()) {
                    navController.navigate(Route.TenantSearch.route) {
                        popUpTo(Route.TenantHome.route) { inclusive = true }
                    }
                }
            }

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
                    unreadMessages = unreadMessages,
                    onSearchApartments = { navController.navigate(Route.TenantSearch.route) },
                    onAvailableHouses = { navController.navigate(Route.HouseSelection.createRoute(data.apartmentId)) },
                    onPayRent = { navController.navigate(Route.TenantPayments.route) },
                    onPayBills = { navController.navigate(Route.TenantBills.route) },
                    onPaymentHistory = { navController.navigate(Route.TenantLease.route) },
                    onNotifications = { navController.navigate(Route.Notifications.route) },
                    onMessages = { navController.navigate(Route.Messages.route) },
                    onMaintenance = { navController.navigate(Route.TenantMaintenance.route) },
                    onAgreement = { navController.navigate(Route.TenantAgreement.route) },
                    onMyHouse = { navController.navigate(Route.MyHouse.route) },
                    onServices = { navController.navigate(Route.TenantServices.route) },
                    onRewards = { navController.navigate(Route.TenantRewards.route) },
                    onAmenityBooking = { navController.navigate(Route.AmenityBooking.createRoute(data.apartmentId)) },
                    onMaintenanceHistory = { navController.navigate(Route.MaintenanceHistory.route) },
                    onUtilityUsage = { navController.navigate(Route.UtilityUsage.route) },
                    onDocumentVault = { navController.navigate(Route.DocumentVault.route) },
                    onPropertyReviews = { id -> navController.navigate(Route.PropertyReviews.createRoute(id)) },
                    onContactLandlord = { navController.navigate(Route.TenantChat.createRoute(data.landlordId)) },
                    onProfile = { navController.navigate(Route.Profile.route) },
                    onSettings = { navController.navigate(Route.Settings.route) },
                    onLogout = onLogout,
                    onDashboard = { navController.navigate(Route.TenantDashboard.route) },
                    onSwitchRole = onSwitchRole,
                    onRefresh = { tenantViewModel.loadDashboardData() }
                )
            }
        }

        composable(Route.TenantDashboard.route) {
            val tenantViewModel: TenantViewModel = hiltViewModel()
            val dashboardState by tenantViewModel.dashboardState.collectAsState()
            
            dashboardState?.let { data ->
                TenantDashboardScreen(
                    dashboardData = data,
                    onBack = { navController.popBackStack() },
                    onNotifications = { navController.navigate(Route.Notifications.route) },
                    onProfile = { navController.navigate(Route.Profile.route) },
                    onPayRent = { navController.navigate(Route.TenantPayments.route) },
                    onBills = { navController.navigate(Route.TenantBills.route) },
                    onMaintenance = { navController.navigate(Route.TenantMaintenance.route) },
                    onEmergency = { navController.navigate(Route.TenantEmergency.route) },
                    onAgreement = { navController.navigate(Route.TenantAgreement.route) },
                    onLandlordChat = { navController.navigate(Route.TenantChat.createRoute(data.landlordId)) },
                    onVacate = { navController.navigate(Route.TenantVacateNotice.route) },
                    onHouseDetails = { navController.navigate(Route.MyHouse.route) }
                )
            }
        }

        composable(Route.MyHouse.route) {
            val tenantViewModel: TenantViewModel = hiltViewModel()
            val dashboardState by tenantViewModel.dashboardState.collectAsState()

            dashboardState?.let { data ->
                TenantMyHouseScreen(
                    house = TenantHouseUIModel(
                        houseId = "MOCK",
                        houseNumber = data.houseNumber,
                        floorNumber = data.floorNumber.toIntOrNull() ?: 0,
                        houseType = try { HouseType.valueOf(data.houseType) } catch(e: Exception) { HouseType.ONE_BEDROOM },
                        status = HouseStatus.OCCUPIED,
                        condition = HouseCondition.GOOD,
                        monthlyRent = data.monthlyRent,
                        deposit = data.monthlyRent,
                        apartmentName = data.apartmentName,
                        landlordName = data.landlordName,
                        landlordPhone = data.landlordPhone
                    ),
                    onBack = { navController.popBackStack() },
                    onMaintenance = { navController.navigate(Route.TenantMaintenance.route) },
                    onVacate = { navController.navigate(Route.TenantVacateNotice.route) },
                    onAgreement = { navController.navigate(Route.TenantAgreement.route) },
                    onPayments = { navController.navigate(Route.TenantBills.route) }
                )
            }
        }

        composable(Route.TenantSearch.route) {
            ApartmentSearchScreen(
                onBack = { 
                    if (!navController.popBackStack()) {
                        navController.navigate(Route.ChooseRole.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
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
            val context = LocalContext.current
            ApartmentDetailsScreen(
                apartmentId = apartmentId,
                onBack = { 
                    if (!navController.popBackStack()) {
                        navController.navigate(Route.TenantSearch.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                onViewHouses = { id -> 
                    navController.navigate(Route.HouseSelection.createRoute(id)) 
                },
                onJoinApartment = { id, name -> 
                    navController.navigate(Route.TenantJoinAgreement.createRoute(id, name))
                },
                onDirections = { lat, lng ->
                    val uri = "google.navigation:q=$lat,$lng"
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(uri))
                    intent.setPackage("com.google.android.apps.maps")
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // Fallback if maps not installed
                        context.startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(uri)))
                    }
                }
            )
        }

        composable(
            Route.HouseSelection.route,
            arguments = listOf(navArgument("apartmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tenantViewModel: TenantViewModel = hiltViewModel()
            val apartmentId = backStackEntry.arguments?.getString("apartmentId") ?: ""
            HouseSelectionScreen(
                apartmentId = apartmentId,
                apartmentName = "Select Your Unit",
                onBack = { navController.popBackStack() },
                onHouseDetails = { id -> navController.navigate(Route.HouseDetails.createRoute(id)) },
                onJoinHouse = { house ->
                    tenantViewModel.pickHouse(house)
                    navController.navigate(Route.TenantHome.route) {
                        popUpTo(Route.TenantHome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            Route.HouseDetails.route,
            arguments = listOf(navArgument("houseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val houseId = backStackEntry.arguments?.getString("houseId") ?: ""
            val viewModel: HouseDetailsViewModel = hiltViewModel()
            val house by viewModel.house.collectAsState()

            LaunchedEffect(houseId) {
                viewModel.loadHouse(houseId)
            }

            house?.let {
                HouseDetailsScreen(
                    house = it,
                    onBack = { navController.popBackStack() },
                    onJoinHouse = { id -> 
                        viewModel.joinHouse(id) {
                            navController.navigate(Route.TenantHome.route) {
                                popUpTo(Route.TenantHome.route) { inclusive = true }
                            }
                        }
                    }
                )
            }
        }

        composable(Route.TenantBills.route) {
            val tenantViewModel: TenantViewModel = hiltViewModel()
            val bills by tenantViewModel.bills.collectAsState()
            BillsScreen(
                apartmentName = "My Apartment",
                houseNumber = "G2",
                bills = bills,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.TenantRentAndBills.route) {
            val tenantViewModel: TenantViewModel = hiltViewModel()
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
                    onViewReceipt = { history -> navController.navigate(Route.TenantReceipt.createRoute(history.id)) }
                )
            }
        }

        composable(
            Route.TenantReceipt.route,
            arguments = listOf(navArgument("receiptId") { type = NavType.StringType })
        ) { backStackEntry ->
            val receiptId = backStackEntry.arguments?.getString("receiptId") ?: ""
            ReceiptScreen(
                receipt = TenantPaymentReceiptUIModel(
                    receiptNumber = receiptId,
                    transactionId = "TXN-MOCK",
                    amount = 0.0,
                    paymentMethod = "M-PESA",
                    phoneNumber = "0700000000",
                    date = "Today",
                    apartmentName = "Property",
                    houseNumber = "Unit",
                    tenantName = "Tenant"
                ),
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.TenantMaintenance.route) {
            val tenantViewModel: TenantViewModel = hiltViewModel()
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

        composable(
            Route.TenantJoinAgreement.route,
            arguments = listOf(
                navArgument("apartmentId") { type = NavType.StringType },
                navArgument("apartmentName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val apartmentId = backStackEntry.arguments?.getString("apartmentId") ?: ""
            val apartmentName = backStackEntry.arguments?.getString("apartmentName") ?: "Property"
            
            TenantJoiningScreen(
                apartmentId = apartmentId,
                apartmentName = apartmentName,
                onBack = { navController.popBackStack() },
                onJoinComplete = {
                    // After join, go to house selection to pick a vacant unit
                    navController.navigate(Route.HouseSelection.createRoute(apartmentId)) {
                        popUpTo(Route.TenantHome.route) { inclusive = false }
                    }
                }
            )
        }

        composable(Route.TenantAgreement.route) {
            val viewModel: TenantAgreementViewModel = hiltViewModel()
            val agreement by viewModel.agreement.collectAsState()
            val isLoading by viewModel.isLoading.collectAsState()
            val error by viewModel.error.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.loadAgreement()
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (agreement != null) {
                AgreementScreen(
                    agreement = agreement!!,
                    onBack = { navController.popBackStack() },
                    onAgreementAccepted = { viewModel.acceptAgreement(it) }
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = error ?: "No agreement found.")
                }
            }
        }

        composable(Route.TenantPayments.route) {
            val tenantViewModel: TenantViewModel = hiltViewModel()
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
            val tenantViewModel: TenantViewModel = hiltViewModel()
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
                    navController.navigate(Route.TenantChat.createRoute(userId)) {
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
            Route.TenantChat.route,
            arguments = listOf(navArgument("partnerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val partnerId = backStackEntry.arguments?.getString("partnerId") ?: ""
            val authViewModel: com.him.landlordtenant.app.ui.viewmodel.auth.AuthViewModel = hiltViewModel()
            val userId = authViewModel.currentUser.collectAsState().value?.id ?: ""
            
            TenantChatScreen(
                currentUserId = userId,
                partnerId = partnerId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.TenantLease.route) {
            val tenantViewModel: TenantViewModel = hiltViewModel()
            val dashboardState by tenantViewModel.dashboardState.collectAsState()

            dashboardState?.let { data ->
                CurrentLeaseScreen(
                    tenant = data,
                    onBack = { navController.popBackStack() },
                    onViewAgreement = { navController.navigate(Route.TenantAgreement.route) },
                    onVacateRequest = { navController.navigate(Route.TenantVacateNotice.route) }
                )
            }
        }

        composable(Route.TenantServices.route) {
            TenantServicesScreen(
                onBack = { navController.popBackStack() },
                onCategoryClick = { category ->
                    // For now just show a message or generic screen
                    navController.navigate(Route.MaintenanceHistory.route) 
                }
            )
        }

        composable(
            Route.AmenityBooking.route,
            arguments = listOf(navArgument("propertyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: ""
            AmenityBookingScreen(
                propertyId = propertyId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.MaintenanceHistory.route) {
            MaintenanceHistoryScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.TenantRewards.route) {
            val tenantViewModel: TenantViewModel = hiltViewModel()
            val dashboardState by tenantViewModel.dashboardState.collectAsState()
            TenantRewardsScreen(
                points = dashboardState?.loyaltyPoints ?: 0,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.UtilityUsage.route) {
            UtilityUsageScreen(onBack = { navController.popBackStack() })
        }

        composable(Route.DocumentVault.route) {
            DocumentVaultScreen(onBack = { navController.popBackStack() })
        }

        composable(
            Route.PropertyReviews.route,
            arguments = listOf(navArgument("propertyId") { type = NavType.StringType })
        ) { backStackEntry ->
            PropertyReviewsScreen(onBack = { navController.popBackStack() })
        }
    }
}
