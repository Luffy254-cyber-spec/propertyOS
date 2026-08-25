package com.him.landlordtenant.app.ui.screens.landlord.houses

import androidx.compose.foundation.layout.*
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
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.HouseStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseStatusScreen(
    currentStatus: HouseStatus,
    onBack: () -> Unit,
    onStatusChange: (HouseStatus) -> Unit
) {
    var selectedStatus by remember { mutableStateOf(currentStatus) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update House Status") },
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
                .padding(24.dp)
        ) {
            Text("Current Status: ${currentStatus.name}", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Select New Status", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            
            HouseStatus.values().forEach { status ->
                StatusOption(
                    status = status,
                    isSelected = selectedStatus == status,
                    onClick = { selectedStatus = status }
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = { onStatusChange(selectedStatus) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update Status")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HouseStatusScreenPreview() {
    PropertyOSTheme {
        HouseStatusScreen(currentStatus = HouseStatus.VACANT, onBack = {}, onStatusChange = {})
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
        Text(text = status.name.replace("_", " "), fontSize = 16.sp)
    }
}
