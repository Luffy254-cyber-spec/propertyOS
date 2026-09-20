package com.him.landlordtenant.app.ui.screens.tenant

import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.interfaces.ApartmentApplicationData
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.viewmodel.tenant.TenantApplicationsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantApplicationsScreen(
    onBack: () -> Unit,
    onProceed: () -> Unit = {},
    viewModel: TenantApplicationsViewModel = hiltViewModel()
) {
    val applications by viewModel.applications.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadApplications()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Applications", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp).padding(end = 16.dp), strokeWidth = 2.dp)
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            error?.let {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer), modifier = Modifier.padding(16.dp)) {
                    Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(12.dp), fontSize = 12.sp)
                }
            }

            if (applications.isEmpty() && !isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                        Icon(Icons.Default.Description, null, modifier = Modifier.size(80.dp), tint = Color.LightGray)
                        Spacer(modifier = Modifier.height(20.dp))
                        Text("No applications yet", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Search for apartments and apply to see them here.", color = Color.Gray, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(applications) { app ->
                        ApplicationItem(
                            app = app,
                            onCancel = { viewModel.cancelApplication(app.id) },
                            onProceed = onProceed
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ApplicationItem(
    app: ApartmentApplicationData,
    onCancel: () -> Unit,
    onProceed: () -> Unit
) {
    val statusColor = when (app.status) {
        "PENDING" -> Color(0xFFE65100)
        "APPROVED" -> Color(0xFF2E7D32)
        "DECLINED" -> Color(0xFFD32F2F)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(50.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Apartment, null, tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    val houseLabel = if (!app.houseNumber.isNullOrEmpty() && app.houseNumber != "GENERAL") " - Unit ${app.houseNumber}" else ""
                    Text("${app.apartmentName}$houseLabel", fontWeight = FontWeight.Black, fontSize = 18.sp)
                    Text("Applied on ${formatDate(app.appliedAt)}", fontSize = 12.sp, color = Color.Gray)
                }
                Surface(color = statusColor.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)) {
                    Text(app.status, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), fontSize = 10.sp, fontWeight = FontWeight.Black, color = statusColor)
                }
            }
            
            if (app.status == "DECLINED" && !app.rejectionReason.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))) {
                    Text("Reason: ${app.rejectionReason}", modifier = Modifier.padding(12.dp), fontSize = 12.sp, color = Color(0xFFC62828))
                }
            }

            if (app.status == "APPROVED") {
                Spacer(modifier = Modifier.height(20.dp))
                Text("Congratulations! Your application was approved. You have been automatically joined to the property community.", fontSize = 13.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Medium)
                
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onProceed,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Text("Proceed to Apartment Dashboard")
                }
            }
            
            if (app.status == "PENDING") {
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                ) {
                    Text("Cancel Application")
                }
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    return java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date(timestamp))
}

@Preview(showBackground = true)
@Composable
fun TenantApplicationsScreenPreview() {
    PropertyOSTheme {
        TenantApplicationsScreen(onBack = {})
    }
}
