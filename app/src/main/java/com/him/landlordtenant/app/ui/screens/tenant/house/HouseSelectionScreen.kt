package com.him.landlordtenant.app.ui.screens.tenant.house

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.TenantHouseUIModel
import com.him.landlordtenant.app.ui.screens.tenant.HouseStatus
import com.him.landlordtenant.app.ui.screens.tenant.HouseType
import com.him.landlordtenant.app.ui.screens.tenant.HouseCondition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseSelectionScreen(
    apartmentName: String,
    totalFloors: Int,
    houses: List<TenantHouseUIModel>,
    onBack: () -> Unit,
    onHouseSelected: (TenantHouseUIModel) -> Unit = {},
    onJoinHouse: (TenantHouseUIModel) -> Unit = {},
) {
    var showJoinDialog by remember { mutableStateOf(false) }
    var selectedHouse by remember { mutableStateOf<TenantHouseUIModel?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(apartmentName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Select a house to join",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            items(houses) { house ->
                HouseItem(
                    house = house,
                    onClick = { onHouseSelected(house) },
                    onJoin = { 
                        selectedHouse = house
                        if (house.status == HouseStatus.VACANT) showJoinDialog = true 
                    }
                )
            }
        }
    }

    if (showJoinDialog && selectedHouse != null) {
        AlertDialog(
            onDismissRequest = { showJoinDialog = false },
            title = { Text("Join House ${selectedHouse?.houseNumber}") },
            text = { Text("Are you sure you want to apply to join this house? Your application will be sent to the landlord.") },
            confirmButton = {
                Button(onClick = { 
                    showJoinDialog = false
                    selectedHouse?.let { onJoinHouse(it) }
                }) {
                    Text("Confirm")
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
private fun HouseItem(house: TenantHouseUIModel, onClick: () -> Unit, onJoin: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("House ${house.houseNumber}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                StatusBadge(status = house.status)
            }
            
            Text("Floor ${house.floorNumber} • ${house.houseType.name.replace("_", " ")}", fontSize = 14.sp, color = Color.Gray)
            
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("KES ${house.monthlyRent} / month", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                if (house.status == HouseStatus.VACANT) {
                    Button(onClick = onJoin, shape = RoundedCornerShape(8.dp), contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)) {
                        Text("Apply", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(status: HouseStatus) {
    val color = when (status) {
        HouseStatus.VACANT -> Color(0xFF2E7D32)
        HouseStatus.OCCUPIED -> Color(0xFFD32F2F)
        HouseStatus.NOT_READY -> Color(0xFFF57C00)
    }
    Surface(shape = RoundedCornerShape(50), color = color.copy(alpha = 0.1f)) {
        Text(text = status.name, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Preview(showBackground = true)
@Composable
fun HouseSelectionScreenPreview() {
    PropertyOSTheme {
        HouseSelectionScreen(
            apartmentName = "Green Valley",
            totalFloors = 5,
            houses = listOf(
                TenantHouseUIModel(houseNumber = "G1", floorNumber = 0, houseType = HouseType.TWO_BEDROOM, status = HouseStatus.VACANT, condition = HouseCondition.EXCELLENT, monthlyRent = 15000.0, deposit = 15000.0),
                TenantHouseUIModel(houseNumber = "G2", floorNumber = 0, houseType = HouseType.BEDSITTER, status = HouseStatus.OCCUPIED, condition = HouseCondition.GOOD, monthlyRent = 8000.0, deposit = 8000.0)
            ),
            onBack = {}
        )
    }
}
