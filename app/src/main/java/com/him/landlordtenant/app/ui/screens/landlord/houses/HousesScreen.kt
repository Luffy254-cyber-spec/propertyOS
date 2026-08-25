package com.him.landlordtenant.app.ui.screens.landlord.houses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun HousesScreen(
    houses: List<TenantHouseUIModel> = emptyList(),
    onBack: () -> Unit,
    onHouseClick: (String) -> Unit,
    onAddHouse: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Managed Houses") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onAddHouse) {
                        Icon(Icons.Default.Add, "Add")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                placeholder = { Text("Search by house number...") },
                leadingIcon = { Icon(Icons.Default.Search, null) }
            )

            if (houses.isEmpty()) {
                EmptyHouses(onAddHouse)
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(houses.filter { it.houseNumber.contains(searchQuery, true) }) { house ->
                        LandlordHouseItem(house) { onHouseClick(house.houseId) }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HousesScreenPreview() {
    PropertyOSTheme {
        HousesScreen(onBack = {}, onHouseClick = {}, onAddHouse = {})
    }
}

@Composable
private fun LandlordHouseItem(house: TenantHouseUIModel, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Home, null, tint = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "House ${house.houseNumber}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "${house.houseType} • Floor ${house.floorNumber}", fontSize = 12.sp, color = Color.Gray)
            }
            Text(text = house.status.name, color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun EmptyHouses(onAdd: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("No houses added", color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onAdd) { Text("Add New House") }
        }
    }
}
