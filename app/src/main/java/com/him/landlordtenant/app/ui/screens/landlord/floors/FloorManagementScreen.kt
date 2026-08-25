package com.him.landlordtenant.app.ui.screens.landlord.floors

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.TenantHouseUIModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloorManagementScreen(
    floorNumber: Int,
    houses: List<TenantHouseUIModel> = emptyList(),
    onBack: () -> Unit,
    onAddHouse: () -> Unit,
    onHouseClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Floor $floorNumber Management") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onAddHouse) {
                        Icon(Icons.Default.Add, "Add House")
                    }
                }
            )
        }
    ) { padding ->
        if (houses.isEmpty()) {
            EmptyFloorState(onAddHouse, Modifier.padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(houses, key = { it.houseId }) { house ->
                    LandlordHouseRow(house) { onHouseClick(house.houseId) }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FloorManagementScreenPreview() {
    PropertyOSTheme {
        FloorManagementScreen(floorNumber = 1, onBack = {}, onAddHouse = {}, onHouseClick = {})
    }
}

@Composable
private fun LandlordHouseRow(house: TenantHouseUIModel, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Home, null, tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Unit ${house.houseNumber}", fontWeight = FontWeight.Bold)
                Text(text = house.tenantName.ifBlank { "Vacant" }, fontSize = 12.sp, color = if (house.tenantName.isBlank()) Color(0xFF2E7D32) else Color.Gray)
            }
            IconButton(onClick = { /* House Actions */ }) {
                Icon(Icons.Default.MoreVert, null)
            }
        }
    }
}

@Composable
private fun EmptyFloorState(onAdd: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No houses on this floor", color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onAdd) { Text("Add First House") }
    }
}
