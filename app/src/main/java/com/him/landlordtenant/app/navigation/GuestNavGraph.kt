package com.him.landlordtenant.app.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.him.landlordtenant.app.ui.screens.guest.GuestHomeScreen
import com.him.landlordtenant.app.ui.screens.guest.GuestApartment

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
                apartments = sampleGuestApartments(),
                counties = listOf("All", "Nairobi", "Kiambu", "Mombasa", "Kisumu"),
                onLogin = onLogin,
                onRegister = { /* Navigate to register */ },
                onBack = { onLogin() },
                onApartmentSelected = { apartment ->
                    navController.navigate(Route.ApartmentDetails.createRoute(apartment.id))
                }
            )
        }
        
        // You can add more public routes here if needed
    }
}

private fun sampleGuestApartments() = listOf(
    GuestApartment(
        id = "APT001",
        name = "Green Valley Apartments",
        county = "Nairobi",
        location = "Kilimani, Nairobi",
        description = "Modern residential apartment with secure parking and water supply.",
        houseTypes = listOf("Bedsitter", "1 Bedroom", "2 Bedroom"),
        startingRent = 12000,
        availableHouses = 8,
        totalHouses = 48,
        floors = 6,
        landlordName = "Property Management",
        verified = true,
        distanceKm = 1.4
    ),
    GuestApartment(
        id = "APT002",
        name = "Sunrise Heights",
        county = "Kiambu",
        location = "Ruiru, Kiambu",
        description = "Family-friendly apartments with spacious houses.",
        houseTypes = listOf("1 Bedroom", "2 Bedroom", "3 Bedroom"),
        startingRent = 15000,
        availableHouses = 5,
        totalHouses = 36,
        floors = 4,
        landlordName = "Sunrise Properties",
        verified = true,
        distanceKm = 5.8
    )
)
