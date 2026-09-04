package com.him.landlordtenant.app.ui.screens.tenant.house

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
    val houses by viewModel.houses.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var showJoinDialog by remember { mutableStateOf(false) }
    var selectedHouse by remember { mutableStateOf<TenantHouseUIModel?>(null) }

    LaunchedEffect(apartmentId) {
        viewModel.loadHouses(apartmentId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(apartmentName, fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp).padding(end = 16.dp), strokeWidth = 2.dp)
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Select your new home",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )
            }
            
            itemsIndexed(houses) { index, house ->
                var isVisible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(index * 100L)
                    isVisible = true
                }
                AnimatedVisibility(visible = isVisible, enter = slideInHorizontally { -50 } + fadeIn()) {
                    HouseItem(
                        house = house,
                        onClick = { onHouseDetails(house.houseId) },
                        onJoin = { 
                            selectedHouse = house
                            if (house.status == HouseStatus.VACANT) showJoinDialog = true 
                        }
                    )
                }
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
            apartmentId = "1",
            apartmentName = "Sample Apartment",
            onBack = {}
        )
    }
}
