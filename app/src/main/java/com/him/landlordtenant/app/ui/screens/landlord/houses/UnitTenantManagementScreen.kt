package com.him.landlordtenant.app.ui.screens.landlord.houses

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.theme.PremiumGradient
import com.him.landlordtenant.app.ui.viewmodel.landlord.UnitTenantManagementViewModel
import com.him.landlordtenant.app.interfaces.TenantSummaryData
import com.him.landlordtenant.app.interfaces.UserProfileData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitTenantManagementScreen(
    apartmentId: String,
    floorId: String,
    houseId: String,
    onBack: () -> Unit,
    onViewTenantDetails: (String) -> Unit,
    viewModel: UnitTenantManagementViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAssignDialog by remember { mutableStateOf(false) }

    LaunchedEffect(houseId) {
        viewModel.startObserving(apartmentId, floorId, houseId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Resident Management", fontSize = 18.sp, fontWeight = FontWeight.Black)
                        Text(uiState.house?.name?.let { "Unit $it" } ?: "Loading...", fontSize = 12.sp, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                
                // Current Resident Section
                Text("Current Resident", fontWeight = FontWeight.Black, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.currentTenant != null) {
                    CurrentResidentCard(
                        tenant = uiState.currentTenant!!,
                        onClick = { onViewTenantDetails(uiState.currentTenant!!.id) },
                        onRemove = { viewModel.evictTenant(apartmentId, floorId, houseId) }
                    )
                } else if (!uiState.isLoading) {
                    VacantCard(onAssign = { showAssignDialog = true })
                }

                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }

            if (showAssignDialog) {
                AssignResidentDialog(
                    availableTenants = uiState.availableTenants,
                    onDismiss = { showAssignDialog = false },
                    onAssign = { tenantId ->
                        viewModel.assignTenant(tenantId, apartmentId, floorId, houseId)
                        showAssignDialog = false
                    }
                )
            }
        }
    }
    
    // Error Handling
    uiState.error?.let { err ->
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Error") },
            text = { Text(err) },
            confirmButton = { TextButton(onClick = { viewModel.clearError() }) { Text("OK") } }
        )
    }
}

@Composable
private fun CurrentResidentCard(
    tenant: UserProfileData,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(60.dp).clip(CircleShape).background(Brush.linearGradient(PremiumGradient)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(tenant.fullName.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Black, fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(tenant.fullName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(tenant.phoneNumber ?: "No phone", fontSize = 14.sp, color = Color.Gray)
                }
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.PersonRemove, null, tint = Color.Red)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = onClick) {
                    Text("View Full Profile", fontWeight = FontWeight.Bold)
                }
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "ACTIVE",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF2E7D32)
                    )
                }
            }
        }
    }
}

@Composable
private fun VacantCard(onAssign: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onAssign),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.PersonAdd, null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Unit is Vacant", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Assign a resident to this unit", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onAssign, shape = RoundedCornerShape(12.dp)) {
                Text("Assign Resident", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AssignResidentDialog(
    availableTenants: List<TenantSummaryData>,
    onDismiss: () -> Unit,
    onAssign: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choose Resident", fontWeight = FontWeight.Black) },
        text = {
            if (availableTenants.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                    Text("No residents available to assign.", color = Color.Gray, fontSize = 14.sp)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp)) {
                    items(availableTenants) { tenant ->
                        ListItem(
                            modifier = Modifier.clickable { onAssign(tenant.id) },
                            headlineContent = { Text(tenant.name, fontWeight = FontWeight.Bold) },
                            supportingContent = { Text(tenant.phoneNumber ?: "No phone") },
                            leadingContent = {
                                Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.LightGray), contentAlignment = Alignment.Center) {
                                    Text(tenant.name.take(1).uppercase(), fontWeight = FontWeight.Bold)
                                }
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
