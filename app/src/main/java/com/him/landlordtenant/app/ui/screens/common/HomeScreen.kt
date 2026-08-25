package com.him.landlordtenant.app.ui.screens.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@Composable
fun HomeScreen(
    isLoggedIn: Boolean,
    userRole: String?,
    onNavigateToTenantDashboard: () -> Unit,
    onNavigateToLandlordDashboard: () -> Unit,
    onNavigateToOnboarding: () -> Unit
) {
    LaunchedEffect(isLoggedIn, userRole) {
        if (!isLoggedIn) {
            onNavigateToOnboarding()
        } else {
            when (userRole) {
                "TENANT" -> onNavigateToTenantDashboard()
                "LANDLORD" -> onNavigateToLandlordDashboard()
                else -> onNavigateToOnboarding()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PropertyOSTheme {
        HomeScreen(
            isLoggedIn = true,
            userRole = "TENANT",
            onNavigateToTenantDashboard = {},
            onNavigateToLandlordDashboard = {},
            onNavigateToOnboarding = {}
        )
    }
}
