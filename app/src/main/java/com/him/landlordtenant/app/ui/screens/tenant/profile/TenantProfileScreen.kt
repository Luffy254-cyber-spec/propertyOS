package com.him.landlordtenant.app.ui.screens.tenant.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.TenantDashboardUIState
import com.him.landlordtenant.app.ui.screens.tenant.TenantRentStatus
import com.him.landlordtenant.app.ui.screens.tenant.TenantOccupancyStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantProfileScreen(
    tenant: TenantDashboardUIState,
    onBack: () -> Unit,
    onEditProfile: () -> Unit = {},
    onPaymentMethods: () -> Unit = {},
    onSettings: () -> Unit = {},
    onHelp: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile") },
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
                .verticalScroll(rememberScrollState())
        ) {
            ProfileHeader(tenant)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            ProfileMenuItem(Icons.Default.Person, "Edit Profile", onEditProfile)
            ProfileMenuItem(Icons.Default.Payment, "Payment Methods", onPaymentMethods)
            ProfileMenuItem(Icons.Default.Settings, "Account Settings", onSettings)
            ProfileMenuItem(Icons.AutoMirrored.Filled.Help, "Help & FAQ", onHelp)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout Account")
            }
            
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun ProfileHeader(tenant: TenantDashboardUIState) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(100.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(tenant.tenantName.take(1).uppercase(), fontSize = 40.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Text(tenant.tenantName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(tenant.location, color = Color.Gray)
    }
}

@Composable
private fun ProfileMenuItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(title) },
        leadingContent = { Icon(icon, null, tint = MaterialTheme.colorScheme.primary) },
        trailingContent = { Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Preview(showBackground = true)
@Composable
fun TenantProfileScreenPreview() {
    PropertyOSTheme {
        TenantProfileScreen(
            tenant = TenantDashboardUIState(
                tenantName = "Jane Doe",
                apartmentName = "Green Valley",
                apartmentId = "1",
                houseNumber = "G1",
                floorNumber = "0",
                houseType = "2BR",
                location = "Nairobi",
                landlordId = "l1",
                landlordName = "L",
                landlordPhone = "P",
                monthlyRent = 0.0,
                waterBill = 0.0,
                garbageFee = 0.0,
                serviceCharge = 0.0,
                outstandingAmount = 0.0,
                totalDue = 0.0,
                dueDate = "D",
                nextPaymentDate = "N",
                rentStatus = TenantRentStatus.PAID,
                occupancyStatus = TenantOccupancyStatus.ACTIVE
            ),
            onBack = {}
        )
    }
}
