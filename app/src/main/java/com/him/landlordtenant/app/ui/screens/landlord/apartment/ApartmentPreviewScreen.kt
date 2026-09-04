package com.him.landlordtenant.app.ui.screens.landlord.apartment

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.TenantApartmentUIModel
import com.him.landlordtenant.app.ui.screens.tenant.apartment.ApartmentDetailsContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApartmentPreviewScreen(
    apartment: TenantApartmentUIModel,
    onBack: () -> Unit
) {
    // Reuse the tenant-facing screen to show exactly what they see
    ApartmentDetailsContent(
        apartment = apartment,
        isLoading = false,
        onBack = onBack,
        onViewHouses = { _ -> },
        onJoinApartment = { _, _ -> },
        title = "Public Preview"
    )
}

@Preview(showBackground = true)
@Composable
fun ApartmentPreviewScreenPreview() {
    PropertyOSTheme {
        ApartmentPreviewScreen(
            apartment = TenantApartmentUIModel(
                id = "1",
                name = "Sample Apartment",
                county = "Nairobi",
                location = "Kilimani",
                description = "Sample description",
                availableUnits = 5,
                totalUnits = 10,
                startingRent = 15000.0,
                highestRent = 20000.0,
                rating = 4.5,
                verified = true,
                distanceKm = 1.0,
                houseTypes = listOf("1BR", "2BR")
            ),
            onBack = {}
        )
    }
}
