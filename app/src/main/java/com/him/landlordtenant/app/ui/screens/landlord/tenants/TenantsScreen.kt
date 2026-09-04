package com.him.landlordtenant.app.ui.screens.landlord.tenants

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import kotlinx.coroutines.delay

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
    onChatClick: (String) -> Unit = {},
    onPendingTenants: () -> Unit = {},
    onFormerTenants: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Tenants", fontWeight = FontWeight.Black) },
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
            FloatingActionButton(
                onClick = onInviteTenant,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, "Add Tenant")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CategoryChip("Pending", onPendingTenants, Color(0xFFF57C00))
                CategoryChip("Former", onFormerTenants, Color.Gray)
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                placeholder = { Text("Search by name or house number") },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.primary) },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                itemsIndexed(tenants.filter { it.tenantName.contains(searchQuery, true) || it.houseNumber.contains(searchQuery, true) }) { index, tenant ->
                    var isVisible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        delay(index * 80L)
                        isVisible = true
                    }
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = slideInHorizontally(initialOffsetX = { -50 }) + fadeIn()
                    ) {
                        TenantItem(
                            tenant = tenant, 
                            onClick = { onTenantClick(tenant.id) },
                            onChat = { onChatClick(tenant.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryChip(label: String, onClick: () -> Unit, color: Color) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
private fun TenantItem(tenant: LandlordTenantUIModel, onClick: () -> Unit, onChat: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.98f else 1f, label = "scale")

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surface
    ) {
        ListItem(
            headlineContent = { Text(tenant.tenantName, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            supportingContent = { Text("${tenant.houseNumber} • ${tenant.apartmentName}", fontSize = 13.sp, color = Color.Gray) },
            leadingContent = {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape), 
                    contentAlignment = Alignment.Center
                ) {
                    Text(tenant.tenantName.take(1).uppercase(), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Black, fontSize = 18.sp)
                }
            },
            trailingContent = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(tenant.rentBalance, fontWeight = FontWeight.Black, color = if (tenant.rentBalance != "KSh 0") Color(0xFFD32F2F) else Color.Black)
                        Surface(
                            color = if (tenant.status == "Active") Color(0xFFE8F5E9) else Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(tenant.status, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (tenant.status == "Active") Color(0xFF2E7D32) else Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    IconButton(onClick = onChat) { 
                        Icon(Icons.AutoMirrored.Filled.Chat, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp)) 
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TenantsScreenPreview() {
    PropertyOSTheme {
        TenantsScreen(
            tenants = listOf(
                LandlordTenantUIModel("1", "John Doe", "G1", "Sample Apartment"),
                LandlordTenantUIModel("2", "Jane Smith", "A4", "Blue Sky", rentBalance = "KSh 12,000")
            ),
            onBack = {},
            onTenantClick = {},
            onInviteTenant = {}
        )
    }
}
