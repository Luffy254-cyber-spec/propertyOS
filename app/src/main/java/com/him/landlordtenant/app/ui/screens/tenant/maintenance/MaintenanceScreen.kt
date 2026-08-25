package com.him.landlordtenant.app.ui.screens.tenant.maintenance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.MaintenanceCategory
import com.him.landlordtenant.app.ui.screens.tenant.MaintenancePriority
import com.him.landlordtenant.app.ui.screens.tenant.MaintenanceRequestUIModel
import com.him.landlordtenant.app.ui.screens.tenant.MaintenanceStatus
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaintenanceScreen(
    requests: List<MaintenanceRequestUIModel> = emptyList(),
    onBack: () -> Unit = {},
    onCreateRequest: (MaintenanceRequestUIModel) -> Unit = {},
    onCancelRequest: (MaintenanceRequestUIModel) -> Unit = {},
    onCallTechnician: (MaintenanceRequestUIModel) -> Unit = {},
    onEmergency: () -> Unit = {},
    onViewHistory: () -> Unit = {}
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var showEmergencyDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "Maintenance", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Repairs & property issues", fontSize = 11.sp, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                },
                actions = {
                    IconButton(onClick = onViewHistory) { Icon(Icons.Default.History, "History") }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { MaintenanceHeader() }
            item { EmergencyMaintenanceCard(onClick = { showEmergencyDialog = true }) }
            item {
                Button(
                    onClick = { showCreateDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Report a Problem")
                }
            }
            item { SectionHeader("Active Requests") }
            val activeRequests = requests.filter { it.status != MaintenanceStatus.COMPLETED && it.status != MaintenanceStatus.CANCELLED }
            if (activeRequests.isEmpty()) {
                item { EmptyMaintenanceState() }
            } else {
                items(activeRequests, key = { it.id }) { request ->
                    MaintenanceRequestCard(
                        request = request,
                        onCancel = { onCancelRequest(request) },
                        onCall = { onCallTechnician(request) }
                    )
                }
            }
            item { SectionHeader("Recent History") }
            val historyRequests = requests.filter { it.status == MaintenanceStatus.COMPLETED || it.status == MaintenanceStatus.CANCELLED }
            items(historyRequests.take(5), key = { "hist_${it.id}" }) { request ->
                MaintenanceHistoryCard(request)
            }
        }
    }

    if (showCreateDialog) {
        CreateMaintenanceDialog(
            onDismiss = { showCreateDialog = false },
            onSubmit = { request -> showCreateDialog = false; onCreateRequest(request) }
        )
    }

    if (showEmergencyDialog) {
        EmergencyDialog(
            onDismiss = { showEmergencyDialog = false },
            onConfirm = { showEmergencyDialog = false; onEmergency() }
        )
    }
}

@Composable
private fun MaintenanceHeader() {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(50.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.HomeRepairService, null, modifier = Modifier.size(28.dp), tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Need something fixed?", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Report property issues and track repairs.", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun EmergencyMaintenanceCard(onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.PriorityHigh, null, modifier = Modifier.size(28.dp), tint = Color(0xFFC62828))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Emergency Maintenance", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFC62828))
                Text("Fire, flooding, dangerous electrical faults.", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun CreateMaintenanceDialog(onDismiss: () -> Unit, onSubmit: (MaintenanceRequestUIModel) -> Unit) {
    var category by remember { mutableStateOf(MaintenanceCategory.OTHER) }
    var priority by remember { mutableStateOf(MaintenancePriority.MEDIUM) }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Report Problem") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Category", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                MaintenanceCategorySelector(category) { category = it }
                Text("Priority", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MaintenancePriority.values().forEach {
                        FilterChip(selected = priority == it, onClick = { priority = it }, label = { Text(it.name, fontSize = 10.sp) })
                    }
                }
                OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location (e.g. Kitchen)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
            }
        },
        confirmButton = { Button(enabled = description.isNotBlank(), onClick = {
            onSubmit(MaintenanceRequestUIModel(
                id = "MNT-${System.currentTimeMillis()}",
                category = category,
                priority = priority,
                location = location,
                description = description,
                status = MaintenanceStatus.SUBMITTED,
                createdAt = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date()),
                assignedTo = null,
                assignedPhone = null
            ))
        }) { Text("Submit") } },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun MaintenanceCategorySelector(selected: MaintenanceCategory, onSelected: (MaintenanceCategory) -> Unit) {
    Column {
        val categories = MaintenanceCategory.values().toList()
        categories.chunked(3).forEach { categoryRow ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                categoryRow.forEach { cat ->
                    FilterChip(
                        selected = selected == cat,
                        onClick = { onSelected(cat) },
                        label = { Text(cat.name, fontSize = 10.sp) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun MaintenanceRequestCard(request: MaintenanceRequestUIModel, onCancel: () -> Unit, onCall: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
                    Icon(categoryIcon(request.category), null, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(request.category.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(request.description, fontSize = 12.sp, color = Color.Gray, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
                IconButton(onClick = onCancel) { Icon(Icons.Default.Close, "Cancel") }
            }
            Spacer(modifier = Modifier.height(12.dp))
            StatusBadge(request.status)
            if (request.assignedTo != null) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Assigned: ${request.assignedTo}", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = onCall) { Icon(Icons.Default.Call, null) }
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(status: MaintenanceStatus) {
    val color = when (status) {
        MaintenanceStatus.SUBMITTED -> Color.Gray
        MaintenanceStatus.REVIEWING, MaintenanceStatus.APPROVED -> Color.Blue
        MaintenanceStatus.ASSIGNED, MaintenanceStatus.IN_PROGRESS -> Color(0xFFF57C00)
        MaintenanceStatus.COMPLETED -> Color(0xFF2E7D32)
        MaintenanceStatus.CANCELLED -> Color.Red
        else -> Color.Gray
    }
    Surface(color = color.copy(alpha = 0.1f), shape = RoundedCornerShape(4.dp)) {
        Text(status.name, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun MaintenanceHistoryCard(request: MaintenanceRequestUIModel) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(categoryIcon(request.category), null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(request.category.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(request.createdAt, fontSize = 11.sp, color = Color.Gray)
            }
            Text(request.status.name, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = if (request.status == MaintenanceStatus.COMPLETED) Color(0xFF2E7D32) else Color.Gray)
        }
    }
}

@Composable
private fun EmptyMaintenanceState() {
    Column(modifier = Modifier.fillMaxWidth().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(48.dp), tint = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Text("No active requests", fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, modifier = Modifier.padding(top = 8.dp))
}

@Composable
private fun EmergencyDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.LocalFireDepartment, null, modifier = Modifier.size(36.dp), tint = Color.Red) },
        title = { Text("Emergency Assistance") },
        text = { Text("Use this only for life-threatening or serious property emergencies.") },
        confirmButton = { Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) { Text("Emergency Call") } },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

private fun categoryIcon(category: MaintenanceCategory) = when (category) {
    MaintenanceCategory.PLUMBING -> Icons.Default.Plumbing
    MaintenanceCategory.ELECTRICAL -> Icons.Default.ElectricalServices
    MaintenanceCategory.WATER -> Icons.Default.WaterDrop
    else -> Icons.Default.Build
}

@Preview(showBackground = true)
@Composable
fun MaintenanceScreenPreview() {
    PropertyOSTheme {
        MaintenanceScreen()
    }
}
