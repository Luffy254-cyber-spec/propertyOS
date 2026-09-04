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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.viewmodel.landlord.FloorsViewModel
import com.him.landlordtenant.app.ui.screens.tenant.HouseStatus
import com.him.landlordtenant.app.ui.screens.tenant.HouseType
import com.him.landlordtenant.app.ui.screens.tenant.TenantHouseUIModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloorUnitsScreen(
    apartmentId: String,
    floorId: String,
    onBack: () -> Unit,
    onHouseClick: (String) -> Unit = {},
    viewModel: FloorsViewModel = hiltViewModel()
) {
    val units by viewModel.units.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    var showAddDialog by remember { mutableStateOf(false) }
    var unitNumber by remember { mutableStateOf("") }
    var unitType by remember { mutableStateOf("ONE_BEDROOM") }
    var unitRent by remember { mutableStateOf("") }

    LaunchedEffect(apartmentId, floorId) {
        viewModel.loadUnits(apartmentId, floorId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Floor $floorId Units") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp).padding(end = 16.dp), strokeWidth = 2.dp)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Default.Add, "Add Unit", tint = Color.White)
            }
        }
    ) { padding ->
        if (units.isEmpty() && !isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.MapsHomeWork, null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No units on this floor", color = Color.Gray)
                    TextButton(onClick = { showAddDialog = true }) {
                        Text("Create First Unit")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(units) { unit ->
                    UnitItem(unit) { onHouseClick(unit.houseId) }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add New Unit") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(value = unitNumber, onValueChange = { unitNumber = it }, label = { Text("Unit Number (e.g. A1)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = unitRent, onValueChange = { unitRent = it }, label = { Text("Monthly Rent (KES)") }, modifier = Modifier.fillMaxWidth())
                    
                    Text("Unit Type", fontWeight = FontWeight.Bold)
                    Column {
                        listOf("BEDSITTER", "ONE_BEDROOM", "TWO_BEDROOM", "SHOP", "OFFICE").forEach { type ->
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { unitType = type }.padding(vertical = 4.dp)) {
                                RadioButton(selected = unitType == type, onClick = { unitType = type })
                                Text(type.replace("_", " "))
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { 
                        viewModel.addUnit(apartmentId, floorId, unitNumber, unitType, unitRent.toDoubleOrNull() ?: 0.0) {
                            showAddDialog = false
                            unitNumber = ""
                            unitRent = ""
                            viewModel.loadUnits(apartmentId, floorId)
                        }
                    },
                    enabled = unitNumber.isNotBlank() && unitRent.isNotBlank()
                ) { Text("Create") }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun UnitItem(unit: TenantHouseUIModel, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Home, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Unit ${unit.houseNumber}", fontWeight = FontWeight.Bold)
                Text(unit.houseType.name.replace("_", " "), fontSize = 12.sp, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("KES ${unit.monthlyRent}", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                Surface(
                    color = if (unit.status == HouseStatus.VACANT) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        unit.status.name, 
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (unit.status == HouseStatus.VACANT) Color(0xFF2E7D32) else Color(0xFFC62828)
                    )
                }
            }
        }
    }
}
