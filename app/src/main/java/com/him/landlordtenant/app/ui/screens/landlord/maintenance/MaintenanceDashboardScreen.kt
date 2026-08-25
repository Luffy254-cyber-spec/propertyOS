package com.him.landlordtenant.app.ui.screens.landlord.maintenance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PendingActions
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
fun MaintenanceDashboardScreen(
    onBack: () -> Unit,
    onActiveRequests: () -> Unit,
    onMaintenanceHistory: () -> Unit,
    onManageTechnicians: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Maintenance Center") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { ActiveSummaryCard(onActiveRequests) }
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    LargeActionCard(Modifier.weight(1f), "History", Icons.Default.History, onMaintenanceHistory)
                    LargeActionCard(Modifier.weight(1f), "Staff", Icons.Default.PendingActions, onManageTechnicians)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MaintenanceDashboardScreenPreview() {
    PropertyOSTheme {
        MaintenanceDashboardScreen(onBack = {}, onActiveRequests = {}, onMaintenanceHistory = {}, onManageTechnicians = {})
    }
}

@Composable
private fun ActiveSummaryCard(onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Build, null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Active Requests", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("8 pending issues found.", color = Color.Gray, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun LargeActionCard(modifier: Modifier, label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(modifier = modifier, onClick = onClick) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, fontWeight = FontWeight.Bold)
        }
    }
}
