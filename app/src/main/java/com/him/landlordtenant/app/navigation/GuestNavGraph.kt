package com.him.landlordtenant.app.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.him.landlordtenant.app.ui.screens.guest.GuestHomeScreen

fun NavGraphBuilder.guestNavGraph(
    navController: NavHostController,
    onLogin: () -> Unit
) {
    navigation(
        startDestination = "guest_search",
        route = "guest_graph"
    ) {
        composable("guest_search") {
            GuestHomeScreen(
                onLogin = onLogin,
                onRegister = { /* Navigate to register */ },
                onBack = { onLogin() },
                onApartmentSelected = { apartmentId ->
                    navController.navigate(Route.ApartmentDetails.createRoute(apartmentId))
                }
            )
        }
        
        // You can add more public routes here if needed
    }
}
