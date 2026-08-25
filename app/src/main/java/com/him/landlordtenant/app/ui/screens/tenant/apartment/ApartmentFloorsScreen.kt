package com.him.landlordtenant.app.ui.screens.tenant.apartment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.him.landlordtenant.app.ui.screens.tenant.ApartmentFloorUIModel
import com.him.landlordtenant.app.ui.screens.tenant.TenantHouseUIModel
import com.him.landlordtenant.app.ui.screens.tenant.HouseStatus
import com.him.landlordtenant.app.ui.screens.tenant.HouseType
import com.him.landlordtenant.app.ui.screens.tenant.HouseCondition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApartmentFloorsScreen(
    apartmentName: String,
    floors: List<ApartmentFloorUIModel>,
    onBack: () -> Unit,
    onHouseClicked: (String) -> Unit = {},
) {
    var selectedFloorIndex by remember { mutableIntStateOf(0) }
    var selectedStatus by remember { mutableStateOf<HouseStatus?>(null) }

    val currentFloor = floors.getOrNull(selectedFloorIndex)
    val filteredHouses = remember(currentFloor, selectedStatus) {
        currentFloor?.houses?.filter { selectedStatus == null || it.status == selectedStatus } ?: emptyList()
    }

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
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Floor Selector
            ScrollableTabRow(
                selectedTabIndex = selectedFloorIndex,
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                divider = {}
            ) {
                floors.forEachIndexed { index, floor ->
                    Tab(
                        selected = selectedFloorIndex == index,
                        onClick = { selectedFloorIndex = index },
                        text = { Text(floor.floorName) }
                    )
                }
            }

            // Status Filter
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedStatus == null,
                        onClick = { selectedStatus = null },
                        label = { Text("All Houses") }
                    )
                }
                items(HouseStatus.entries) { status ->
                    FilterChip(
                        selected = selectedStatus == status,
                        onClick = { selectedStatus = status },
                        label = { Text(status.name) }
                    )
                }
            }

            // Houses Grid
            if (filteredHouses.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No houses matching filters", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredHouses) { house ->
                        HouseRow(house = house, onClick = { onHouseClicked(house.houseId) })
                    }
                }
            }
        }
    }
}

@Composable
private fun HouseRow(house: TenantHouseUIModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(45.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(house.houseNumber, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text("House ${house.houseNumber}", fontWeight = FontWeight.Bold)
                Text(house.houseType.name.replace("_", " "), fontSize = 12.sp, color = Color.Gray)
            }
            
            StatusBadge(status = house.status)
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
fun ApartmentFloorsScreenPreview() {
    PropertyOSTheme {
        ApartmentFloorsScreen(
            apartmentName = "Green Valley",
            floors = listOf(
                ApartmentFloorUIModel(1, "Ground Floor", listOf(
                    TenantHouseUIModel(houseNumber = "G1", floorNumber = 1, houseType = HouseType.TWO_BEDROOM, status = HouseStatus.VACANT, condition = HouseCondition.EXCELLENT, monthlyRent = 15000.0, deposit = 15000.0),
                    TenantHouseUIModel(houseNumber = "G2", floorNumber = 1, houseType = HouseType.BEDSITTER, status = HouseStatus.OCCUPIED, condition = HouseCondition.GOOD, monthlyRent = 8000.0, deposit = 8000.0)
                )),
                ApartmentFloorUIModel(2, "1st Floor", emptyList())
            ),
            onBack = {}
        )
    }
}
