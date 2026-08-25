package com.him.landlordtenant.app.ui.screens.landlord.emergency

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.emergency.EmergencyScreen

@Composable
fun EmergencyServicesScreen(
    onBack: () -> Unit
) {
    // Shared emergency contact UI
    EmergencyScreen(
        onBack = onBack
    )
}

@Preview(showBackground = true)
@Composable
fun EmergencyServicesScreenPreview() {
    PropertyOSTheme {
        EmergencyServicesScreen(onBack = {})
    }
}
