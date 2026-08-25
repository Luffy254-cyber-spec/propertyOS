package com.him.landlordtenant.app.ui.screens.landlord.maintenance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.MaintenanceRequestUIModel
import com.him.landlordtenant.app.ui.screens.tenant.MaintenancePriority
import com.him.landlordtenant.app.ui.screens.tenant.MaintenanceStatus
import com.him.landlordtenant.app.ui.screens.tenant.MaintenanceCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaintenanceRequestsScreen(
    requests: List<MaintenanceRequestUIModel>,
    onBack: () -> Unit,
    onRequestClick: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Maintenance Requests") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (requests.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No active maintenance requests", color = Color.Gray)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                items(requests) { request ->
                    MaintenanceItem(request = request, onClick = { onRequestClick(request.id) })
                }
            }
        }
    }
}

@Composable
private fun MaintenanceItem(request: MaintenanceRequestUIModel, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(request.description.take(30) + if (request.description.length > 30) "..." else "", fontWeight = FontWeight.Bold) },
        supportingContent = { Text("${request.category.name} • ${request.createdAt}", maxLines = 1, overflow = TextOverflow.Ellipsis) },
        leadingContent = {
            Box(modifier = Modifier.size(45.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Build, null, tint = MaterialTheme.colorScheme.primary)
            }
        },
        trailingContent = {
            Column(horizontalAlignment = Alignment.End) {
                StatusBadge(status = request.status)
                Spacer(modifier = Modifier.height(4.dp))
                PriorityBadge(priority = request.priority)
            }
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
private fun StatusBadge(status: MaintenanceStatus) {
    val color = when (status) {
        MaintenanceStatus.SUBMITTED -> Color.Gray
        MaintenanceStatus.REVIEWING -> Color(0xFF1976D2)
        MaintenanceStatus.APPROVED -> Color(0xFF2E7D32)
        MaintenanceStatus.ASSIGNED -> Color(0xFFF57C00)
        MaintenanceStatus.IN_PROGRESS -> Color(0xFF7B1FA2)
        MaintenanceStatus.WAITING_FOR_PARTS -> Color(0xFFC2185B)
        MaintenanceStatus.COMPLETED -> Color(0xFF2E7D32)
        MaintenanceStatus.CANCELLED -> Color.Red
    }
    Surface(shape = RoundedCornerShape(50), color = color.copy(alpha = 0.1f)) {
        Text(text = status.name, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
private fun PriorityBadge(priority: MaintenancePriority) {
    val color = when (priority) {
        MaintenancePriority.LOW -> Color(0xFF2E7D32)
        MaintenancePriority.MEDIUM -> Color(0xFF1976D2)
        MaintenancePriority.HIGH -> Color(0xFFF57C00)
        MaintenancePriority.EMERGENCY -> Color(0xFFD32F2F)
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (priority == MaintenancePriority.EMERGENCY) Icon(Icons.Default.PriorityHigh, null, modifier = Modifier.size(10.dp), tint = color)
        Text(text = priority.name, fontSize = 9.sp, color = color)
    }
}

@Preview(showBackground = true)
@Composable
fun MaintenanceRequestsScreenPreview() {
    PropertyOSTheme {
        MaintenanceRequestsScreen(
            requests = listOf(
                MaintenanceRequestUIModel(
                    id = "1",
                    category = MaintenanceCategory.PLUMBING,
                    priority = MaintenancePriority.HIGH,
                    location = "Unit G1",
                    description = "Broken Sink leaking heavily",
                    status = MaintenanceStatus.SUBMITTED,
                    createdAt = "1 hour ago",
                    assignedTo = null,
                    assignedPhone = null
                ),
                MaintenanceRequestUIModel(
                    id = "2",
                    category = MaintenanceCategory.ELECTRICAL,
                    priority = MaintenancePriority.EMERGENCY,
                    location = "Unit A4",
                    description = "Power Outage in kitchen",
                    status = MaintenanceStatus.IN_PROGRESS,
                    createdAt = "2 hours ago",
                    assignedTo = "John Electrician",
                    assignedPhone = "0700111222"
                )
            ),
            onBack = {},
            onRequestClick = {}
        )
    }
}
