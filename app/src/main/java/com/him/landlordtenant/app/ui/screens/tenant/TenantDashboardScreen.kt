package com.him.landlordtenant.app.ui.screens.tenant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantDashboardScreen(
    dashboardData: TenantDashboardUIState,
    onNotifications: () -> Unit = {},
    onProfile: () -> Unit = {},
    onPayRent: () -> Unit = {},
    onBills: () -> Unit = {},
    onPaymentHistory: () -> Unit = {},
    onApartmentDetails: () -> Unit = {},
    onHouseDetails: () -> Unit = {},
    onLandlordChat: () -> Unit = {},
    onTenantGroupChat: () -> Unit = {},
    onCallLandlord: () -> Unit = {},
    onMaintenance: () -> Unit = {},
    onEmergency: () -> Unit = {},
    onAgreement: () -> Unit = {},
    onFindApartment: () -> Unit = {},
    onVacate: () -> Unit = {},
    onSettings: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                TenantDashboardDrawerHeader(
                    tenantName = dashboardData.tenantName,
                    apartmentName = dashboardData.apartmentName,
                    houseNumber = dashboardData.houseNumber
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Dashboard") },
                    selected = true,
                    onClick = { scope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Default.Dashboard, null) }
                )
                NavigationDrawerItem(
                    label = { Text("My House") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; onHouseDetails() },
                    icon = { Icon(Icons.Default.Home, null) }
                )
                NavigationDrawerItem(
                    label = { Text("Payments") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; onPaymentHistory() },
                    icon = { Icon(Icons.Default.Payment, null) }
                )
                NavigationDrawerItem(
                    label = { Text("Agreement") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; onAgreement() },
                    icon = { Icon(Icons.Default.Security, null) }
                )
                NavigationDrawerItem(
                    label = { Text("Maintenance") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; onMaintenance() },
                    icon = { Icon(Icons.Default.Build, null) }
                )
                NavigationDrawerItem(
                    label = { Text("Settings") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; onSettings() },
                    icon = { Icon(Icons.Default.Settings, null) }
                )
                Spacer(modifier = Modifier.weight(1f))
                NavigationDrawerItem(
                    label = { Text("Logout") },
                    selected = false,
                    onClick = { 
                        scope.launch { drawerState.close() }
                        onLogout() 
                    },
                    icon = { Icon(Icons.AutoMirrored.Filled.Logout, null) }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(text = "Hello, ${dashboardData.tenantName}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Tenant Dashboard", fontSize = 10.sp, color = Color.Gray)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = onNotifications) {
                            BadgedBox(
                                badge = {
                                    if (dashboardData.unreadNotifications > 0) {
                                        Badge { Text(dashboardData.unreadNotifications.coerceAtMost(99).toString()) }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Notifications, "Notifications")
                            }
                        }
                        IconButton(onClick = onProfile) {
                            Icon(Icons.Default.Person, "Profile")
                        }
                    }
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                item { TenantOccupancyBanner(status = dashboardData.occupancyStatus) }
                item {
                    DashboardCurrentHouseCard(
                        apartmentName = dashboardData.apartmentName,
                        houseNumber = dashboardData.houseNumber,
                        floor = dashboardData.floorNumber,
                        houseType = dashboardData.houseType,
                        onClick = onHouseDetails
                    )
                }
                item {
                    DashboardRentStatusCard(
                        monthlyRent = dashboardData.monthlyRent,
                        outstanding = dashboardData.outstandingAmount,
                        paymentDate = dashboardData.dueDate,
                        status = dashboardData.rentStatus,
                        onPayRent = onPayRent
                    )
                }
                item {
                    DashboardQuickActions(
                        onPayRent = onPayRent,
                        onBills = onBills,
                        onMaintenance = onMaintenance,
                        onChat = onLandlordChat,
                        onEmergency = onEmergency,
                        onAgreement = onAgreement
                    )
                }
                item {
                    Text(text = "Billing History", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                item {
                    BillingHistoryCard(
                        records = listOf(
                            BillingHistoryRecord("Aug 2026", 16850.0, "PAID"),
                            BillingHistoryRecord("July 2026", 17500.0, "PAID"),
                            BillingHistoryRecord("June 2026", 16200.0, "PAID")
                        )
                    )
                }
                item {
                    Text(text = "House Details", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                item {
                    DashboardInfoCard(
                        items = listOf(
                            "Apartment" to dashboardData.apartmentName,
                            "Location" to dashboardData.location,
                            "Landlord" to dashboardData.landlordName,
                            "Status" to dashboardData.occupancyStatus.name
                        )
                    )
                }
                item {
                    Button(
                        onClick = onVacate,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer, contentColor = MaterialTheme.colorScheme.onErrorContainer)
                    ) {
                        Text(text = "Notice to Vacate")
                    }
                }
            }
        }
    }
}

@Composable
private fun BillingHistoryCard(records: List<BillingHistoryRecord>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            records.forEach { record ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = record.month, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "Total: KSh ${formatPaymentMoney(record.amount)}", fontSize = 11.sp, color = Color.Gray)
                    }
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = (if (record.status == "PAID") Color(0xFF4CAF50) else Color(0xFFFF9800)).copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = record.status,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = if (record.status == "PAID") Color(0xFF2E7D32) else Color(0xFFE65100),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                if (record != records.last()) HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            }
        }
    }
}

data class BillingHistoryRecord(val month: String, val amount: Double, val status: String)

@Composable
private fun TenantDashboardDrawerHeader(tenantName: String, apartmentName: String, houseNumber: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Box(modifier = Modifier.size(55.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Person, null, modifier = Modifier.size(30.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = tenantName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = "$apartmentName • House $houseNumber", fontSize = 11.sp, color = Color.Gray)
    }
}

@Composable
private fun TenantOccupancyBanner(status: TenantOccupancyStatus) {
    val (title, description, background) = when (status) {
        TenantOccupancyStatus.ACTIVE -> Triple("Tenancy Active", "You are registered as a tenant.", Color(0xFFE8F5E9))
        TenantOccupancyStatus.PENDING_VERIFICATION -> Triple("Verification Pending", "Awaiting verification.", Color(0xFFFFF8E1))
        TenantOccupancyStatus.VACATING -> Triple("Vacating Process", "Request submitted.", Color(0xFFFFF3E0))
        TenantOccupancyStatus.NOTICE_PERIOD -> Triple("Notice Period", "Currently in notice period.", Color(0xFFFFF3E0))
        TenantOccupancyStatus.INACTIVE -> Triple("Inactive", "No active tenancy found.", Color(0xFFF5F5F5))
    }
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = background)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(if (status == TenantOccupancyStatus.ACTIVE) Icons.Default.CheckCircle else Icons.Default.Warning, null)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(text = description, fontSize = 10.sp)
            }
        }
    }
}

@Composable
private fun DashboardCurrentHouseCard(apartmentName: String, houseNumber: String, floor: String, houseType: String, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(16.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(50.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.secondaryContainer), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Home, null, modifier = Modifier.size(28.dp), tint = MaterialTheme.colorScheme.onSecondaryContainer)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "My Current House", fontSize = 10.sp, color = Color.Gray)
                Text(text = "House $houseNumber", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = "$apartmentName • Floor $floor • $houseType", fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
        }
    }
}

@Composable
private fun DashboardRentStatusCard(monthlyRent: Double, outstanding: Double, paymentDate: String, status: TenantRentStatus, onPayRent: () -> Unit) {
    val statusColor = when (status) {
        TenantRentStatus.PAID -> Color(0xFF2E7D32)
        TenantRentStatus.DUE_SOON, TenantRentStatus.PARTIALLY_PAID -> Color(0xFFF57C00)
        TenantRentStatus.OVERDUE -> Color(0xFFC62828)
    }
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(text = "Rent", fontSize = 11.sp, color = Color.Gray)
                    Text(text = "KSh ${formatPaymentMoney(monthlyRent)}", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
                Surface(shape = RoundedCornerShape(50), color = statusColor.copy(alpha = 0.12f)) {
                    Text(text = status.name, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = "Outstanding", fontSize = 9.sp, color = Color.Gray)
                    Text(text = "KSh ${formatPaymentMoney(outstanding)}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Next payment", fontSize = 9.sp, color = Color.Gray)
                    Text(text = paymentDate, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(modifier = Modifier.fillMaxWidth(), onClick = onPayRent) {
                Icon(Icons.Default.Payment, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = if (outstanding > 0) "Pay Now" else "View Payments")
            }
        }
    }
}

@Composable
private fun DashboardQuickActions(onPayRent: () -> Unit, onBills: () -> Unit, onMaintenance: () -> Unit, onChat: () -> Unit, onEmergency: () -> Unit, onAgreement: () -> Unit) {
    Column {
        Text(text = "Quick Actions", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            QuickAction(Modifier.weight(1f), Icons.Default.Payment, "Pay", onPayRent)
            QuickAction(Modifier.weight(1f), Icons.AutoMirrored.Filled.ReceiptLong, "Bills", onBills)
            QuickAction(Modifier.weight(1f), Icons.Default.Build, "Repair", onMaintenance)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            QuickAction(Modifier.weight(1f), Icons.AutoMirrored.Filled.Chat, "Chat", onChat)
            QuickAction(Modifier.weight(1f), Icons.Default.Warning, "Emergency", onEmergency)
            QuickAction(Modifier.weight(1f), Icons.Default.Security, "Agreement", onAgreement)
        }
    }
}

@Composable
private fun DashboardInfoCard(items: List<Pair<String, String>>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            items.forEach { (label, value) ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = label, fontSize = 12.sp, color = Color.Gray)
                    Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TenantDashboardScreenPreview() {
    PropertyOSTheme {
        TenantDashboardScreen(
            dashboardData = TenantDashboardUIState(
                tenantName = "Jane Doe",
                apartmentName = "Green Valley Apartments",
                apartmentId = "1",
                houseNumber = "G2",
                floorNumber = "Floor 1",
                houseType = "2BR",
                location = "Kilimani, Nairobi",
                landlordId = "l1",
                landlordName = "John Landlord",
                landlordPhone = "0712345678",
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
