package com.him.landlordtenant.app.ui.screens.landlord.floors

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Layers
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloorsScreen(
    apartmentName: String,
    floors: List<String> = emptyList(), // Just IDs or names for now
    onBack: () -> Unit,
    onFloorClick: (String) -> Unit,
    onAddFloor: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "Floors", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = apartmentName, fontSize = 11.sp, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onAddFloor) {
                        Icon(Icons.Default.Add, "Add")
                    }
                }
            )
        }
    ) { padding ->
        if (floors.isEmpty()) {
            EmptyFloors(onAddFloor, Modifier.padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(floors) { floor ->
                    FloorItem(floor) { onFloorClick(floor) }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FloorsScreenPreview() {
    PropertyOSTheme {
        FloorsScreen(apartmentName = "Green Valley", onBack = {}, onFloorClick = {}, onAddFloor = {})
    }
}

@Composable
private fun FloorItem(name: String, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Layers, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Floor $name", fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
