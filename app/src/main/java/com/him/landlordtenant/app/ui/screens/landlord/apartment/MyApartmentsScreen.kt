package com.him.landlordtenant.app.ui.screens.landlord.apartment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.TenantApartmentUIModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApartmentsScreen(
    apartments: List<TenantApartmentUIModel> = emptyList(),
    onBack: () -> Unit,
    onApartmentClick: (String) -> Unit,
    onApartmentPreview: (String) -> Unit = {},
    onAddApartment: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Properties") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onAddApartment) {
                        Icon(Icons.Default.Add, "Add")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddApartment) {
                Icon(Icons.Default.Add, "Add Property")
            }
        }
    ) { padding ->
        if (apartments.isEmpty()) {
            EmptyApartmentsState(onAddApartment)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(apartments) { apartment ->
                    ApartmentItem(
                        apartment = apartment,
                        onClick = { onApartmentClick(apartment.id) },
                        onPreview = { onApartmentPreview(apartment.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ApartmentItem(apartment: TenantApartmentUIModel, onClick: () -> Unit, onPreview: () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(140.dp).background(Color.LightGray), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Image, null, modifier = Modifier.size(48.dp), tint = Color.Gray)
                
                Box(modifier = Modifier.fillMaxSize().padding(12.dp), contentAlignment = Alignment.TopEnd) {
                    Box(modifier = Modifier.background(Color.White.copy(alpha = 0.8f), CircleShape)) {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, null)
                        }
                        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                            DropdownMenuItem(text = { Text("Edit") }, onClick = { showMenu = false; onClick() })
                            DropdownMenuItem(text = { Text("Preview as Tenant") }, onClick = { showMenu = false; onPreview() })
                            DropdownMenuItem(text = { Text("Delete") }, onClick = { showMenu = false }, colors = MenuDefaults.itemColors(textColor = Color.Red))
                        }
                    }
                }
            }
            
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(apartment.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    if (apartment.verified) {
                        Icon(Icons.Default.Verified, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    }
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(12.dp), tint = Color.Gray)
                    Text(apartment.location, fontSize = 12.sp, color = Color.Gray)
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${apartment.totalUnits} Units", fontSize = 12.sp)
                    Text(apartment.totalRevenue.ifEmpty { "KSh 0" }, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
private fun EmptyApartmentsState(onAdd: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Apartment, null, modifier = Modifier.size(80.dp), tint = Color.LightGray)
        Spacer(modifier = Modifier.height(16.dp))
        Text("No properties listed yet", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text("Start by adding your first apartment or building.", textAlign = TextAlign.Center, color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onAdd) {
            Text("Add My First Property")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyApartmentsScreenPreview() {
    PropertyOSTheme {
        MyApartmentsScreen(
            apartments = listOf(
                TenantApartmentUIModel(id = "1", name = "Green Valley", location = "Kilimani", totalUnits = 24, totalRevenue = "KSh 840k", verified = true, county = "Nairobi", description = "", availableUnits = 5, startingRent = 15000.0, highestRent = 25000.0, rating = 4.5, distanceKm = 1.0, houseTypes = emptyList())
            ),
            onBack = {},
            onApartmentClick = {},
            onAddApartment = {}
        )
    }
}
