package com.him.landlordtenant.app.ui.screens.landlord.apartment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.viewmodel.landlord.ApartmentManagementViewModel
import com.him.landlordtenant.app.interfaces.MarketplacePropertyListingData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApartmentManagementScreen(
    apartmentId: String,
    onBack: () -> Unit,
    onManageFloors: (String) -> Unit,
    onManageMedia: (String) -> Unit,
    onUpdateLocation: (String) -> Unit,
    onEditDetails: (String) -> Unit,
    onFinancialSettings: (String) -> Unit = {},
    onViewAnalytics: (String) -> Unit = {},
    viewModel: ApartmentManagementViewModel = hiltViewModel()
) {
    val apartment by viewModel.apartment.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(apartmentId) {
        viewModel.loadApartment(apartmentId)
    }

    ApartmentManagementContent(
        apartment = apartment,
        isLoading = isLoading,
        onBack = onBack,
        onManageFloors = { onManageFloors(apartmentId) },
        onFinancialSettings = { onFinancialSettings(apartmentId) },
        onManageMedia = { onManageMedia(apartmentId) },
        onUpdateLocation = { onUpdateLocation(apartmentId) },
        onEditDetails = { onEditDetails(apartmentId) },
        onViewAnalytics = { onViewAnalytics(apartmentId) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApartmentManagementContent(
    apartment: MarketplacePropertyListingData?,
    isLoading: Boolean,
    onBack: () -> Unit,
    onManageFloors: () -> Unit,
    onFinancialSettings: () -> Unit,
    onManageMedia: () -> Unit,
    onUpdateLocation: () -> Unit,
    onEditDetails: () -> Unit,
    onViewAnalytics: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Apartment") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (apartment == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Failed to load property details")
            }
        } else {
            val data = apartment
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                ApartmentOverviewHeader(data)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                ManagementOption(Icons.Default.Layers, "Floors & Units", "Add or edit houses and floors.") { onManageFloors() }
                ManagementOption(Icons.Default.Payments, "Payment Settings", "Configure M-Pesa, Airtel and Cards.") { onFinancialSettings() }
                ManagementOption(Icons.Default.Image, "Photos & Videos", "Manage property media gallery.") { onManageMedia() }
                ManagementOption(Icons.Default.LocationOn, "Location", "Update GPS and map address.") { onUpdateLocation() }
                ManagementOption(Icons.Default.Edit, "Property Details", "Edit name, desc, and amenities.") { onEditDetails() }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                SectionHeader("Statistics")
                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SmallStatCard(Modifier.weight(1f), "Units", data.totalUnits.toString())
                    SmallStatCard(Modifier.weight(1f), "Available", data.availableUnits.toString())
                }
                
                Button(
                    onClick = { onViewAnalytics() },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Analytics, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View Detailed Analytics")
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Save, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save & Finish", fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
private fun ApartmentOverviewHeader(apartment: MarketplacePropertyListingData) {
    Card(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(60.dp), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Apartment, null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(apartment.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("${apartment.location.town}, ${apartment.location.county}", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun ManagementOption(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = onClick
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold)
                Text(subtitle, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(title, modifier = Modifier.padding(horizontal = 16.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp)
}

@Composable
private fun SmallStatCard(modifier: Modifier, label: String, value: String) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, fontSize = 10.sp, color = Color.Gray)
            Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ApartmentManagementScreenPreview() {
    PropertyOSTheme {
        ApartmentManagementContent(
            apartment = MarketplacePropertyListingData(
                id = "1",
                title = "Sample Apartment",
                location = com.him.landlordtenant.app.interfaces.ListingLocationData(
                    latitude = 0.0,
                    longitude = 0.0,
                    address = "123 Main St",
                    county = "Nairobi",
                    town = "Westlands"
                ),
                totalUnits = 10,
                availableUnits = 5
            ),
            isLoading = false,
            onBack = {},
            onManageFloors = {},
            onFinancialSettings = {},
            onManageMedia = {},
            onUpdateLocation = {},
            onEditDetails = {},
            onViewAnalytics = {}
        )
    }
}
