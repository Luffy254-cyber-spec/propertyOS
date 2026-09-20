package com.him.landlordtenant.app.ui.screens.tenant

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import com.him.landlordtenant.app.ui.screens.tenant.StaggeredFadeIn
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantDashboardScreen(
    dashboardData: TenantDashboardUIState,
    onBack: () -> Unit = {},
    onNotifications: () -> Unit = {},
    onProfile: () -> Unit = {},
    onPayRent: () -> Unit = {},
    onBills: () -> Unit = {},
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
    onApplications: () -> Unit = {},
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
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onNotifications) {
                        BadgedBox(
                            badge = {
                                if (dashboardData.unreadNotifications > 0) {
                                    Badge(containerColor = MaterialTheme.colorScheme.error) {
                                        Text(text = dashboardData.unreadNotifications.coerceAtMost(99).toString(), color = Color.White)
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.Notifications, "Notifications", tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                        }
                    }
                    IconButton(onClick = onProfile) {
                        Surface(
                            modifier = Modifier.size(32.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Person,
                                    "Profile",
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(24.dp)
        ) {
            item { 
                StaggeredFadeIn(delay = 100) {
                    TenantOccupancyBanner(status = dashboardData.occupancyStatus) 
                }
            }
            item {
                StaggeredFadeIn(delay = 250) {
                    DashboardCurrentHouseCard(
                        apartmentName = dashboardData.apartmentName,
                        houseNumber = dashboardData.houseNumber,
                        floor = dashboardData.floorNumber,
                        houseType = dashboardData.houseType,
                        onClick = onHouseDetails
                    )
                }
            }
            item {
                StaggeredFadeIn(delay = 400) {
                    DashboardRentStatusCard(
                        monthlyRent = dashboardData.monthlyRent,
                        outstanding = dashboardData.outstandingAmount,
                        paymentDate = dashboardData.dueDate,
                        status = dashboardData.rentStatus,
                        onPayRent = onPayRent
                    )
                }
            }
            item {
                StaggeredFadeIn(delay = 550) {
                    DashboardQuickActions(
                        onPayRent = onPayRent,
                        onBills = onBills,
                        onMaintenance = onMaintenance,
                        onChat = onLandlordChat,
                        onEmergency = onEmergency,
                        onAgreement = onAgreement,
                        onApplications = onApplications
                    )
                }
            }
            item {
                StaggeredFadeIn(delay = 700) {
                    Text(text = "House Details", fontSize = 16.sp, fontWeight = FontWeight.Black)
                }
            }
            item {
                StaggeredFadeIn(delay = 800) {
                    DashboardInfoCard(
                        items = listOf(
                            "Apartment" to dashboardData.apartmentName,
                            "Location" to dashboardData.location,
                            "Landlord" to dashboardData.landlordName,
                            "Status" to dashboardData.occupancyStatus.name
                        )
                    )
                }
            }
            item {
                StaggeredFadeIn(delay = 950) {
                    OutlinedButton(
                        onClick = onVacate,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp).height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f))
                    ) {
                        Text(text = "Notice to Vacate", fontWeight = FontWeight.Bold)
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
private fun TenantOccupancyBanner(status: TenantOccupancyStatus) {
    val title: String
    val description: String
    val background: Color
    val icon: ImageVector

    when (status) {
        TenantOccupancyStatus.ACTIVE -> {
            title = "Tenancy Active"
            description = "You are registered as a tenant."
            background = Color(0xFFE8F5E9)
            icon = Icons.Default.CheckCircle
        }
        TenantOccupancyStatus.PENDING_VERIFICATION -> {
            title = "Verification Pending"
            description = "Awaiting verification."
            background = Color(0xFFFFF8E1)
            icon = Icons.Default.HourglassEmpty
        }
        TenantOccupancyStatus.VACATING -> {
            title = "Vacating Process"
            description = "Request submitted."
            background = Color(0xFFFFF3E0)
            icon = Icons.AutoMirrored.Filled.DirectionsRun
        }
        TenantOccupancyStatus.NOTICE_PERIOD -> {
            title = "Notice Period"
            description = "Currently in notice period."
            background = Color(0xFFFFF3E0)
            icon = Icons.Default.AccessTime
        }
        TenantOccupancyStatus.INACTIVE -> {
            title = "Inactive"
            description = "No active tenancy found."
            background = Color(0xFFF5F5F5)
            icon = Icons.Default.Search
        }
    }

    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }

    AnimatedVisibility(visible = isVisible, enter = expandVertically() + fadeIn()) {
        Card(
            modifier = Modifier.fillMaxWidth(), 
            colors = CardDefaults.cardColors(containerColor = background),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = title, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                    Text(text = description, fontSize = 11.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
private fun DashboardCurrentHouseCard(apartmentName: String, houseNumber: String, floor: String, houseType: String, onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.98f else 1f, label = "scale")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Home, null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.onSecondaryContainer)
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "My Current House", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                Text(text = "House $houseNumber", fontSize = 20.sp, fontWeight = FontWeight.Black)
                Text(text = "$apartmentName • Floor $floor • $houseType", fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, color = Color.Gray)
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = MaterialTheme.colorScheme.primary)
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
    
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.98f else 1f, label = "scale")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(text = "MONTHLY RENT", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Gray, letterSpacing = 1.2.sp)
                    Text(text = "KES ${formatPaymentMoney(monthlyRent)}", fontSize = 24.sp, fontWeight = FontWeight.Black)
                }
                Surface(shape = RoundedCornerShape(12.dp), color = statusColor.copy(alpha = 0.12f)) {
                    Text(text = status.name, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Black)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = "BALANCE DUE", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Gray, letterSpacing = 1.sp)
                    Text(text = "KES ${formatPaymentMoney(outstanding)}", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = if (outstanding > 0) Color.Red else Color.Black)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "DUE ON", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Gray, letterSpacing = 1.sp)
                    Text(text = paymentDate, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                modifier = Modifier.fillMaxWidth().height(56.dp), 
                onClick = onPayRent,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Icon(Icons.Default.Payment, null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = if (outstanding > 0) "PAY BALANCE NOW" else "VIEW HISTORY", fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Composable
private fun DashboardQuickActions(onPayRent: () -> Unit, onBills: () -> Unit, onMaintenance: () -> Unit, onChat: () -> Unit, onEmergency: () -> Unit, onAgreement: () -> Unit, onApplications: () -> Unit) {
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
            QuickAction(Modifier.weight(1f), Icons.AutoMirrored.Filled.Assignment, "Requests", onApplications)
            QuickAction(Modifier.weight(1f), Icons.Default.Security, "Contract", onAgreement)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            QuickAction(Modifier.weight(1f), Icons.Default.Warning, "Emergency", onEmergency)
            Spacer(modifier = Modifier.weight(2f))
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
                apartmentName = "Sample Apartment",
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
                unreadMessages = 0,
                waterBill = 650.0,
                garbageFee = 300.0,
                serviceCharge = 900.0,
                outstandingAmount = 1850.0,
                nextPaymentDate = "1 Sept"
            )
        )
    }
}
