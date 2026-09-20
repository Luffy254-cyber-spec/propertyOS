package com.him.landlordtenant.app.ui.screens.landlord.floors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.viewmodel.landlord.FloorsViewModel
import com.him.landlordtenant.app.interfaces.PropertyFloorData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloorsScreen(
    apartmentId: String,
    onBack: () -> Unit,
    onAddHouseToFloor: (String, String) -> Unit,
    onShowHouses: (String, String) -> Unit,
    onAddFloor: () -> Unit,
    viewModel: FloorsViewModel = hiltViewModel()
) {
    val floors by viewModel.floors.collectAsState()
    val apartmentName by viewModel.apartmentName.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Refresh every time screen becomes active
    LaunchedEffect(Unit) {
        viewModel.loadFloors(apartmentId)
    }

    FloorsContent(
        floors = floors,
        apartmentName = apartmentName,
        isLoading = isLoading,
        onBack = onBack,
        onAddHouseToFloor = { floorId -> onAddHouseToFloor(apartmentId, floorId) },
        onShowHouses = { floorId -> onShowHouses(apartmentId, floorId) },
        onAddFloor = onAddFloor
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloorsContent(
    floors: List<PropertyFloorData>,
    apartmentName: String,
    isLoading: Boolean,
    onBack: () -> Unit,
    onAddHouseToFloor: (String) -> Unit,
    onShowHouses: (String) -> Unit,
    onAddFloor: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "Floors & Units", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = apartmentName, fontSize = 11.sp, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp).padding(end = 16.dp), strokeWidth = 2.dp)
                    }
                    IconButton(onClick = onAddFloor) {
                        Icon(Icons.Default.Add, "Add")
                    }
                }
            )
        }
    ) { padding ->
        if (floors.isEmpty() && !isLoading) {
            EmptyFloors(onAddFloor, Modifier.padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(floors) { floor ->
                    FloorItem(
                        floor = floor, 
                        onAddHouse = { onAddHouseToFloor(floor.id) },
                        onShowHouses = { onShowHouses(floor.id) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FloorsScreenPreview() {
    PropertyOSTheme {
        FloorsContent(
            floors = listOf(
                PropertyFloorData(id = "1", number = 1, name = "Ground Floor", unitCount = 5)
            ),
            apartmentName = "Green Valley Apartments",
            isLoading = false,
            onBack = {},
            onAddHouseToFloor = {},
            onShowHouses = {},
            onAddFloor = {}
        )
    }
}

@Composable
private fun FloorItem(floor: PropertyFloorData, onAddHouse: () -> Unit, onShowHouses: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onShowHouses),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(48.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Layers, null, tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = floor.name.ifEmpty { "Floor ${floor.number}" }, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = "${floor.unitCount} Units Added", fontSize = 12.sp, color = Color.Gray)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onAddHouse, 
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp), 
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add House", fontSize = 12.sp)
                }
                
                OutlinedButton(
                    onClick = onShowHouses, 
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Visibility, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Show Houses", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun EmptyFloors(onAdd: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("No floors added yet", color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onAdd) { Text("Add First Floor") }
        }
    }
}
