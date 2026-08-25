package com.him.landlordtenant.app.ui.screens.landlord.tenants

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
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
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

data class LandlordTenantUIModel(
    val id: String,
    val tenantName: String,
    val houseNumber: String,
    val apartmentName: String,
    val profileImageUrl: String? = null,
    val rentBalance: String = "KSh 0",
    val status: String = "Active"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantsScreen(
    tenants: List<LandlordTenantUIModel>,
    onBack: () -> Unit,
    onTenantClick: (String) -> Unit,
    onInviteTenant: () -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Tenants") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onInviteTenant) {
                        Icon(Icons.Default.PersonAdd, "Invite")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onInviteTenant) {
                Icon(Icons.Default.Add, "Add Tenant")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                placeholder = { Text("Search tenants or house number") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                shape = RoundedCornerShape(12.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(tenants.filter { it.tenantName.contains(searchQuery, true) || it.houseNumber.contains(searchQuery, true) }) { tenant ->
                    TenantItem(tenant = tenant, onClick = { onTenantClick(tenant.id) })
                }
            }
        }
    }
}

@Composable
private fun TenantItem(tenant: LandlordTenantUIModel, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(tenant.tenantName, fontWeight = FontWeight.Bold) },
        supportingContent = { Text("${tenant.houseNumber} • ${tenant.apartmentName}") },
        leadingContent = {
            Box(modifier = Modifier.size(45.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape), contentAlignment = Alignment.Center) {
                Text(tenant.tenantName.take(1).uppercase(), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(tenant.rentBalance, fontWeight = FontWeight.Bold, color = if (tenant.rentBalance != "KSh 0") Color.Red else Color.Black)
                    Text(tenant.status, fontSize = 10.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { /* Chat */ }) { Icon(Icons.AutoMirrored.Filled.Chat, null, tint = Color.Gray) }
            }
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Preview(showBackground = true)
@Composable
fun TenantsScreenPreview() {
    PropertyOSTheme {
        TenantsScreen(
            tenants = listOf(
                LandlordTenantUIModel("1", "John Doe", "G1", "Green Valley"),
                LandlordTenantUIModel("2", "Jane Smith", "A4", "Blue Sky", rentBalance = "KSh 12,000")
            ),
            onBack = {},
            onTenantClick = {},
            onInviteTenant = {}
        )
    }
}
