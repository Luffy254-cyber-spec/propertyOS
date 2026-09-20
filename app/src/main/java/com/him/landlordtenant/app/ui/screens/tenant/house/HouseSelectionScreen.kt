package com.him.landlordtenant.app.ui.screens.tenant.house

import androidx.compose.animation.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.TenantHouseUIModel
import com.him.landlordtenant.app.data.model.HouseStatus
import com.him.landlordtenant.app.ui.screens.tenant.HouseType
import com.him.landlordtenant.app.ui.screens.tenant.HouseCondition
import com.him.landlordtenant.app.interfaces.PropertyFloorData

import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.viewmodel.tenant.HouseSelectionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseSelectionScreen(
    apartmentId: String,
    apartmentName: String,
    onBack: () -> Unit,
    onJoinHouse: (TenantHouseUIModel) -> Unit = {},
    onHouseDetails: (String) -> Unit = {},
    viewModel: HouseSelectionViewModel = hiltViewModel()
) {
    val floors by viewModel.floors.collectAsState()
    val selectedFloorUnits by viewModel.selectedFloorUnits.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    var selectedFloorId by remember { mutableStateOf<String?>(null) }
    var selectedFloorNumber by remember { mutableIntStateOf(0) }
    var showJoinDialog by remember { mutableStateOf(false) }
    var houseToJoin by remember { mutableStateOf<TenantHouseUIModel?>(null) }

    LaunchedEffect(apartmentId) {
        viewModel.loadFloors(apartmentId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(apartmentName, fontWeight = FontWeight.Black, fontSize = 18.sp)
                        if (selectedFloorId != null) {
                            Text("Floor $selectedFloorNumber", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { 
                        if (selectedFloorId != null) {
                            selectedFloorId = null
                        } else {
                            onBack()
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp).padding(end = 16.dp), 
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (selectedFloorId == null) {
                // View 1: Floor Selection
                FloorSelectionList(
                    floors = floors,
                    onFloorClick = { floor ->
                        selectedFloorId = floor.id
                        selectedFloorNumber = floor.number
                        viewModel.loadUnitsForFloor(apartmentId, floor.id)
                    }
                )
            } else {
                // View 2: Unit Selection for Floor
                UnitSelectionList(
                    units = selectedFloorUnits,
                    onHouseClick = { onHouseDetails(it) },
                    onApply = { 
                        houseToJoin = it
                        showJoinDialog = true
                    }
                )
            }
        }
    }

    if (showJoinDialog && houseToJoin != null) {
        AlertDialog(
            onDismissRequest = { showJoinDialog = false },
            title = { Text("Submit Join Request") },
            text = { 
                Column {
                    Text("Are you sure you want to apply for House ${houseToJoin?.houseNumber}?", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("This will notify the landlord of your interest. You will be able to sign the agreement and pay once they approve.", fontSize = 13.sp, color = Color.Gray)
                }
            },
            confirmButton = {
                Button(onClick = { 
                    showJoinDialog = false
                    houseToJoin?.let { onJoinHouse(it) }
                }) {
                    Text("Confirm Application")
                }
            },
            dismissButton = {
                TextButton(onClick = { showJoinDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun FloorSelectionList(
    floors: List<PropertyFloorData>,
    onFloorClick: (PropertyFloorData) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Where would you like to stay?", fontSize = 24.sp, fontWeight = FontWeight.Black)
            Text("Select a floor to view available houses", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        if (floors.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 100.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.LayersClear, null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("No floors found for this property", color = Color.Gray)
                    }
                }
            }
        }

        items(floors) { floor ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onFloorClick(floor) },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = floor.number.toString(),
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (floor.name.isNotBlank()) floor.name else "Floor ${floor.number}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text("${floor.unitCount} Units Total", fontSize = 13.sp, color = Color.Gray)
                    }
                    Icon(Icons.Default.ArrowForwardIos, null, tint = Color.LightGray, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
private fun UnitSelectionList(
    units: List<TenantHouseUIModel>,
    onHouseClick: (String) -> Unit,
    onApply: (TenantHouseUIModel) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Pick Your Unit", fontSize = 24.sp, fontWeight = FontWeight.Black)
            Text("Tap on a unit to see full details", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (units.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 100.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Info, null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("No units found on this floor", color = Color.Gray)
                    }
                }
            }
        }

        items(units) { unit ->
            UnitItem(
                unit = unit,
                onClick = { onHouseClick(unit.houseId) },
                onApply = { onApply(unit) }
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun UnitItem(
    unit: TenantHouseUIModel,
    onClick: () -> Unit,
    onApply: () -> Unit
) {
    // Exact requested color logic
    val (statusColor, statusLabel, canApply) = when (unit.status) {
        HouseStatus.OCCUPIED -> Triple(Color(0xFFD32F2F), "Occupied", false) // Red, Inactive
        HouseStatus.VACANT -> Triple(Color(0xFF2E7D32), "Vacant", true) // Green, Active
        HouseStatus.NOT_READY -> Triple(Color(0xFFF57C00), "Not Ready", true) // Orange, Active
        HouseStatus.UNDER_MAINTENANCE -> Triple(Color(0xFFFFB300), "Maintenance", true) // Yellow, Active
        HouseStatus.RESERVED -> Triple(Color(0xFF1976D2), "Reserved", false) 
        else -> Triple(Color.Gray, unit.status.displayName, true)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Unit ${unit.houseNumber}",
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp
                )
                
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = statusColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = statusLabel.uppercase(),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = statusColor
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.HomeWork, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = unit.houseType.name.replace("_", " "),
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }

            if (unit.description.isNotBlank()) {
                Text(
                    text = unit.description,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    maxLines = 3,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("MONTHLY RENT", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.Gray, letterSpacing = 1.sp)
                    Text(
                        text = "KES ${String.format("%,.0f", unit.monthlyRent)}",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Button(
                    onClick = onApply,
                    enabled = canApply,
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (unit.status == HouseStatus.VACANT) Color(0xFF2E7D32) else statusColor,
                        disabledContainerColor = Color.LightGray
                    )
                ) {
                    Text(if (canApply) "APPLY" else "FULL", fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HouseSelectionScreenPreview() {
    PropertyOSTheme {
        HouseSelectionScreen(
            apartmentId = "1",
            apartmentName = "Sample Apartment",
            onBack = {}
        )
    }
}
