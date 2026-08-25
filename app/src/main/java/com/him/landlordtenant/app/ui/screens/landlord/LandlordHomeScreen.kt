package com.him.landlordtenant.app.ui.screens.landlord

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
fun LandlordHomeScreen(
    onNavigateToDashboard: () -> Unit
) {
    LaunchedEffect(Unit) {
        onNavigateToDashboard()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun LandlordHomeScreenPreview() {
    PropertyOSTheme {
        LandlordHomeScreen({})
    }
}
