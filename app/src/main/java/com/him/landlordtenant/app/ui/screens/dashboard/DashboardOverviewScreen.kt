package com.him.landlordtenant.app.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.components.dashboard.StatCard
import com.him.landlordtenant.app.ui.components.dashboard.ActionCard
import com.him.landlordtenant.app.ui.components.dashboard.RecentActivityItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardOverviewScreen(
    onBack: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Overview") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onNotificationClick) {
                        Icon(Icons.Default.Notifications, "Notifications")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(
                        title = "Active Units",
                        value = "24",
                        icon = Icons.Default.Home,
                        iconColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Pending Bills",
                        value = "5",
                        icon = Icons.Default.Receipt,
                        iconColor = Color.Red,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            item {
                Text(
                    text = "Quick Actions",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ActionCard(
                        title = "Add Unit",
                        icon = Icons.Default.AddHome,
                        onClick = { /* TODO */ },
                        modifier = Modifier.weight(1f)
                    )
                    ActionCard(
                        title = "New Bill",
                        icon = Icons.Default.Payment,
                        onClick = { /* TODO */ },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            item {
                Text(
                    text = "Recent Activity",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            items(5) {
                RecentActivityItem(
                    title = "Rent Payment Received",
                    time = "2 hours ago",
                    icon = Icons.Default.CheckCircle,
                    iconColor = Color(0xFF4CAF50L) // Added L suffix for proper Long literal
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardOverviewScreenPreview() {
    PropertyOSTheme {
        DashboardOverviewScreen({}, {})
    }
}
