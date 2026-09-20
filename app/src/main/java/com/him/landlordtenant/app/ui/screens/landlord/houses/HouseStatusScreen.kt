package com.him.landlordtenant.app.ui.screens.landlord.houses

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.data.model.HouseStatus
import com.him.landlordtenant.app.ui.viewmodel.landlord.HouseManagementViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseStatusScreen(
    apartmentId: String,
    floorId: String,
    houseId: String,
    onBack: () -> Unit,
    viewModel: HouseManagementViewModel = hiltViewModel()
) {
    val house by viewModel.house.collectAsState()
    
    LaunchedEffect(houseId) {
        viewModel.loadHouse(apartmentId, floorId, houseId)
    }

    val currentStatus = house?.status ?: HouseStatus.VACANT

    HouseStatusContent(
        currentStatus = currentStatus,
        onBack = onBack,
        onStatusChange = { newStatus ->
            viewModel.updateStatus(apartmentId, floorId, houseId, newStatus)
            onBack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseStatusContent(
    currentStatus: HouseStatus,
    onBack: () -> Unit,
    onStatusChange: (HouseStatus) -> Unit
) {
    var selectedStatus by remember { mutableStateOf(currentStatus) }

    LaunchedEffect(currentStatus) {
        selectedStatus = currentStatus
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update House Status") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { onStatusChange(selectedStatus) }) {
                        Text("Save", fontWeight = FontWeight.Bold)
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
                .padding(24.dp)
        ) {
            Text("Current Status: ${currentStatus.displayName}", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Select New Status", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            
            HouseStatus.entries.forEach { status ->
                StatusOption(
                    status = status,
                    isSelected = selectedStatus == status,
                    onClick = { selectedStatus = status }
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = { onStatusChange(selectedStatus) },
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            ) {
                Text("Save and Finish", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HouseStatusScreenPreview() {
    PropertyOSTheme {
        HouseStatusContent(currentStatus = HouseStatus.VACANT, onBack = {}, onStatusChange = {})
    }
}

@Composable
private fun StatusOption(status: HouseStatus, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = isSelected, onClick = onClick)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = status.displayName, fontSize = 16.sp)
    }
}
