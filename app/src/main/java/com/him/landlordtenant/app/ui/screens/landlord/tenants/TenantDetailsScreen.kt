package com.him.landlordtenant.app.ui.screens.landlord.tenants

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
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
import com.him.landlordtenant.app.ui.screens.tenant.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantDetailsScreen(
    tenant: TenantDashboardUIState,
    onBack: () -> Unit,
    onRemoveTenant: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tenant Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(modifier = Modifier.size(64.dp), shape = MaterialTheme.shapes.medium, color = MaterialTheme.colorScheme.primaryContainer) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = tenant.tenantName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Active Tenant", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            DetailRow("Contact", tenant.landlordPhone) // tenant phone
            DetailRow("Apartment", tenant.apartmentName)
            DetailRow("House", tenant.houseNumber)
            DetailRow("Rent", "KSh ${tenant.monthlyRent}")
            
            Spacer(modifier = Modifier.weight(1f))
            
            OutlinedButton(
                onClick = onRemoveTenant,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text("End Tenancy")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TenantDetailsScreenPreview() {
    PropertyOSTheme {
        TenantDetailsScreen(
            tenant = TenantDashboardUIState(
                tenantName = "Jane Doe",
                apartmentName = "Green Valley Apartments",
                apartmentId = "1",
                houseNumber = "G2",
                floorNumber = "1",
                houseType = "2BR",
                location = "Kilimani",
                landlordId = "l1",
                landlordName = "John Doe",
                landlordPhone = "0700111222",
                monthlyRent = 15000.0,
                waterBill = 500.0,
                garbageFee = 200.0,
                serviceCharge = 1000.0,
                outstandingAmount = 0.0,
                totalDue = 16700.0,
                dueDate = "1 Sept",
                nextPaymentDate = "1 Sept",
                rentStatus = TenantRentStatus.PAID,
                occupancyStatus = TenantOccupancyStatus.ACTIVE
            ),
            onBack = {},
            onRemoveTenant = {}
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}
