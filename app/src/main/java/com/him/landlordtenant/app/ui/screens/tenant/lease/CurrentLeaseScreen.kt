package com.him.landlordtenant.app.ui.screens.tenant.lease

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
fun CurrentLeaseScreen(
    tenant: TenantDashboardUIState,
    onBack: () -> Unit = {},
    onViewAgreement: () -> Unit = {},
    onVacateRequest: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Current Lease", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LeaseSummaryCard(tenant)
            LeaseTimeline(tenant)
            LeaseActionsCard(onViewAgreement, onVacateRequest)
            LegalNoticeCard()
        }
    }
}

@Composable
private fun LeaseSummaryCard(tenant: TenantDashboardUIState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Lease Summary", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            LeaseRow("Status", tenant.occupancyStatus.name)
            LeaseRow("Apartment", tenant.apartmentName)
            LeaseRow("House", tenant.houseNumber)
            LeaseRow("Rent", "KSh ${tenant.monthlyRent}")
        }
    }
}

@Composable
private fun LeaseTimeline(tenant: TenantDashboardUIState) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Event, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Dates & Timeline", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            LeaseRow("Effective Date", tenant.dueDate) // Using dueDate as placeholder
            LeaseRow("Next Renewal", tenant.nextPaymentDate)
        }
    }
}

@Composable
private fun LeaseActionsCard(onView: () -> Unit, onVacate: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Actions", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onView, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                Icon(Icons.Default.Description, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("View Digital Agreement")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = onVacate, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)) {
                Text("Notice to Vacate")
            }
        }
    }
}

@Composable
private fun LegalNoticeCard() {
    Surface(color = Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Your lease is governed by the signed digital agreement and local housing laws.",
            modifier = Modifier.padding(12.dp),
            fontSize = 11.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun LeaseRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 13.sp, color = Color.Gray)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun CurrentLeaseScreenPreview() {
    PropertyOSTheme {
        CurrentLeaseScreen(
            tenant = TenantDashboardUIState(
                tenantName = "Jane Doe",
                apartmentName = "Green Valley Apartments",
                apartmentId = "1",
                houseNumber = "G2",
                floorNumber = "Floor 1",
                houseType = "2BR",
                location = "Kilimani, Nairobi",
                landlordId = "l1",
                landlordName = "John Landlord",
                landlordPhone = "0700000000",
                monthlyRent = 15000.0,
                totalDue = 16850.0,
                dueDate = "28 Aug",
                rentStatus = TenantRentStatus.PAID,
                occupancyStatus = TenantOccupancyStatus.ACTIVE,
                unreadNotifications = 3,
                unreadMessages = 5,
                waterBill = 650.0,
                garbageFee = 300.0,
                serviceCharge = 900.0,
                outstandingAmount = 1850.0,
                nextPaymentDate = "1 Sept"
            )
        )
    }
}
