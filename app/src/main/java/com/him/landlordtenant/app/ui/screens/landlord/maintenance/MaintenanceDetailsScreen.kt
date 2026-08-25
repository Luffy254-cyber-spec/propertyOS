package com.him.landlordtenant.app.ui.screens.landlord.maintenance

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaintenanceDetailsScreen(
    request: MaintenanceRequestUIModel,
    onBack: () -> Unit,
    onAssign: () -> Unit,
    onUpdateStatus: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Issue Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp)) {
            Text(text = request.category.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = "Location: ${request.location}", color = Color.Gray, fontSize = 14.sp)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Description", fontWeight = FontWeight.Bold)
            Text(request.description, modifier = Modifier.padding(top = 8.dp))
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onAssign, modifier = Modifier.weight(1f)) {
                    Text("Assign Pro")
                }
                OutlinedButton(onClick = onUpdateStatus, modifier = Modifier.weight(1f)) {
                    Text("Update Status")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MaintenanceDetailsScreenPreview() {
    PropertyOSTheme {
        MaintenanceDetailsScreen(
            request = MaintenanceRequestUIModel(
                id = "1",
                category = MaintenanceCategory.PLUMBING,
                priority = MaintenancePriority.HIGH,
                location = "Kitchen",
                description = "Sink is leaking",
                status = MaintenanceStatus.SUBMITTED,
                createdAt = "22 Aug",
                assignedTo = null,
                assignedPhone = null
            ),
            onBack = {},
            onAssign = {},
            onUpdateStatus = {}
        )
    }
}
