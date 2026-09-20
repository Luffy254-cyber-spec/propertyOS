package com.him.landlordtenant.app.ui.screens.landlord.houses

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.viewmodel.landlord.HouseManagementViewModel

@Composable
fun HouseManagementScreen(
    apartmentId: String,
    floorId: String,
    houseId: String,
    onBack: () -> Unit,
    onEditDetails: (String, String, String) -> Unit,
    onManageMedia: (String, String, String) -> Unit,
    onUpdateStatus: (String, String, String) -> Unit,
    onManageTenant: (String) -> Unit,
    onMeterReading: (String) -> Unit = {},
    onCreateInvoice: (String, String, String) -> Unit = { _, _, _ -> },
    viewModel: HouseManagementViewModel = hiltViewModel()
) {
    val house by viewModel.house.collectAsState()

    LaunchedEffect(houseId) {
        viewModel.loadHouse(apartmentId, floorId, houseId)
    }

    HouseManagementContent(
        houseId = houseId,
        houseData = house,
        onBack = onBack,
        onEditDetails = { onEditDetails(apartmentId, floorId, it) },
        onManageMedia = { onManageMedia(apartmentId, floorId, it) },
        onUpdateStatus = { onUpdateStatus(apartmentId, floorId, it) },
        onManageTenant = onManageTenant,
        onMeterReading = onMeterReading,
        onCreateInvoice = { onInvoiceId -> onCreateInvoice(apartmentId, floorId, onInvoiceId) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseManagementContent(
    houseId: String,
    houseData: com.him.landlordtenant.app.data.model.House? = null,
    onBack: () -> Unit,
    onEditDetails: (String) -> Unit,
    onManageMedia: (String) -> Unit,
    onUpdateStatus: (String) -> Unit,
    onManageTenant: (String) -> Unit,
    onMeterReading: (String) -> Unit = {},
    onCreateInvoice: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Unit") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            HouseOverviewHeader(houseData)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            ManagementOption(Icons.Default.Edit, "Edit Details", "House number, rent and type.", { onEditDetails(houseId) })
            ManagementOption(Icons.Default.Speed, "Meter Reading", "Record water/electricity usage.", { onMeterReading(houseId) })
            ManagementOption(Icons.Default.Description, "Generate Invoice", "Calculate rent, water and others.", { onCreateInvoice(houseId) })
            ManagementOption(Icons.Default.Image, "Photos", "Gallery for this specific unit.", { onManageMedia(houseId) })
            ManagementOption(Icons.Default.Info, "Update Status", "Mark as occupied, vacant or repair.", { onUpdateStatus(houseId) })
            ManagementOption(Icons.Default.Person, "Tenant Management", "Assign or view current tenant.", { onManageTenant(houseId) })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HouseManagementScreenPreview() {
    PropertyOSTheme {
        HouseManagementContent("1", null, {}, {}, {}, {}, {})
    }
}

@Composable
private fun HouseOverviewHeader(house: com.him.landlordtenant.app.data.model.House?) {
    Card(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(50.dp), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Home, null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(house?.houseNumber?.let { "Unit $it" } ?: "Loading...", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(
                    text = house?.let { 
                        "${it.houseType.name.replace("_", " ")} • Floor ${it.floorId.replace("floor_", "")} • ${it.status.displayName}" 
                    } ?: "Please wait", 
                    fontSize = 12.sp, 
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun ManagementOption(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = onClick
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold)
                Text(subtitle, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
        }
    }
}
