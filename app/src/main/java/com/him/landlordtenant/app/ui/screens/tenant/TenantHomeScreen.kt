package com.him.landlordtenant.app.ui.screens.tenant

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantHomeScreen(
    tenant: TenantDashboardUIState,
    bills: List<TenantBillUIModel>,
    quickActions: List<TenantQuickActionUIModel>,
    onSearchApartments: () -> Unit = {},
    onAvailableHouses: () -> Unit = {},
    onPayRent: () -> Unit = {},
    onPayBills: () -> Unit = {},
    onPaymentHistory: () -> Unit = {},
    onNotifications: () -> Unit = {},
    onMessages: () -> Unit = {},
    onMaintenance: () -> Unit = {},
    onAgreement: () -> Unit = {},
    onProfile: () -> Unit = {},
    onContactLandlord: () -> Unit = {},
    onSettings: () -> Unit = {},
    onLogout: () -> Unit = {},
    onDashboard: () -> Unit = {},
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedBottomItem by remember { mutableIntStateOf(0) }
    
    // Animation state
    val visible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { 
        kotlinx.coroutines.delay(100.milliseconds)
        visible.value = true 
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerShape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp)
            ) {
                TenantDrawer(
                    tenant = tenant,
                    onDashboard = { scope.launch { drawerState.close() }; onDashboard() },
                    onProfile = { scope.launch { drawerState.close() }; onProfile() },
                    onAgreement = { scope.launch { drawerState.close() }; onAgreement() },
                    onPaymentHistory = { scope.launch { drawerState.close() }; onPaymentHistory() },
                    onNotifications = { scope.launch { drawerState.close() }; onNotifications() },
                    onSettings = { scope.launch { drawerState.close() }; onSettings() },
                    onLogout = { 
                        scope.launch { drawerState.close() }
                        onLogout() 
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TenantTopBar(
                    tenant = tenant,
                    onMenu = { scope.launch { drawerState.open() } },
                    onNotifications = onNotifications,
                    onProfile = onProfile
                )
            },
            bottomBar = {
                TenantBottomNavigation(
                    selectedItem = selectedBottomItem,
                    onItemSelected = { index ->
                        selectedBottomItem = index
                        when (index) {
                            0 -> { /* Already on Home */ }
                            1 -> onSearchApartments()
                            2 -> onPayBills()
                            3 -> onMessages()
                            4 -> onProfile()
                        }
                    },
                    unreadMessages = tenant.unreadMessages
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                AnimatedVisibility(
                    visible = visible.value,
                    enter = fadeIn(animationSpec = tween(800)) + slideInVertically(animationSpec = tween(800), initialOffsetY = { 100 })
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 24.dp)
                    ) {
                        item { TenantGreeting(tenantName = tenant.tenantName) }
                        
                        item { 
                            CurrentHouseCard(tenant = tenant, onClick = onAvailableHouses) 
                        }
                        
                        item {
                            PaymentSummaryCard(
                                tenant = tenant,
                                onPayRent = onPayRent,
                                onPayBills = onPayBills,
                                onHistory = onPaymentHistory
                            )
                        }
                        
                        item {
                            SectionTitle(title = "Quick Actions", actionText = "Explore", onAction = onSearchApartments)
                        }
                        
                        item {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(horizontal = 2.dp)
                            ) {
                                items(quickActions) { action ->
                                    val gradient = when(action.title) {
                                        "Pay Rent" -> PremiumGradient
                                        "Pay Bills" -> SkyGradient
                                        "Find House" -> GoldGradient
                                        else -> RoseGradient
                                    }
                                    QuickActionCard(
                                        action = action,
                                        gradient = gradient,
                                        onClick = {
                                            when (action.title) {
                                                "Pay Rent" -> onPayRent()
                                                "Pay Bills" -> onPayBills()
                                                "Find House" -> onSearchApartments()
                                                "Maintenance" -> onMaintenance()
                                            }
                                        }
                                    )
                                }
                            }
                        }
                        
                        item {
                            SectionTitle(title = "Pending Bills", actionText = "View All", onAction = onPayBills)
                        }
                        
                        if (bills.isEmpty()) {
                            item { 
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(20.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                                ) {
                                    Text("No pending bills. You're all clear!", modifier = Modifier.padding(24.dp), color = Color.Gray, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        } else {
                            itemsIndexed(bills) { index, bill ->
                                val itemVisible = remember { mutableStateOf(false) }
                                LaunchedEffect(Unit) {
                                    kotlinx.coroutines.delay((index * 100L).milliseconds)
                                    itemVisible.value = true
                                }
                                AnimatedVisibility(visible = itemVisible.value, enter = fadeIn() + expandHorizontally()) {
                                    BillRow(
                                        icon = bill.icon ?: Icons.Default.Receipt,
                                        title = bill.title,
                                        amount = bill.amount,
                                        onClick = onPayBills
                                    )
                                }
                            }
                        }
                        
                        item { MaintenanceCard(onClick = onMaintenance) }
                        
                        item {
                            LandlordContactCard(
                                landlordName = tenant.landlordName,
                                landlordPhone = tenant.landlordPhone,
                                onCall = onContactLandlord,
                                onMessage = onMessages
                            )
                        }
                        
                        item { AgreementCard(onClick = onAgreement) }
                        
                        item { SearchApartmentCard(onClick = onSearchApartments) }
                        
                        item { Spacer(modifier = Modifier.height(20.dp)) }
                    }
                }
            }
        }
    }
}


@Composable
private fun TenantTopBar(
    tenant: TenantDashboardUIState,
    onMenu: () -> Unit,
    onNotifications: () -> Unit,
    onProfile: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxWidth(), shadowElevation = 1.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onMenu) { Icon(Icons.Default.Menu, "Open menu") }
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).background(Brush.linearGradient(PremiumGradient), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.HomeWork,
                        contentDescription = null,
                        modifier = Modifier.size(21.dp),
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = "propertyOS", fontSize = 16.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                    Text(
                        text = tenant.houseNumber,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
            }
            BadgedBox(
                badge = {
                    if (tenant.unreadNotifications > 0) {
                        Badge { Text(text = tenant.unreadNotifications.toString()) }
                    }
                }
            ) {
                IconButton(onClick = onNotifications) {
                    Icon(Icons.Default.Notifications, "Notifications")
                }
            }
            IconButton(onClick = onProfile) { Icon(Icons.Default.Person, "Profile") }
        }
    }
}

@Composable
private fun TenantGreeting(tenantName: String) {
    Column {
        Text(
            text = "Welcome Home,",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Text(text = tenantName, fontSize = 28.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
private fun CurrentHouseCard(tenant: TenantDashboardUIState, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 8.dp
    ) {
        Box(modifier = Modifier.background(Brush.linearGradient(PremiumGradient))) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(54.dp).background(Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ACTIVE RESIDENCE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White.copy(alpha = 0.6f),
                            letterSpacing = 1.sp
                        )
                        Text(text = tenant.apartmentName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(
                            text = "${tenant.houseNumber} • ${tenant.floorNumber}",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.White)
                }
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = tenant.location, fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                }
            }
        }
    }
}

@Composable
private fun PaymentSummaryCard(
    tenant: TenantDashboardUIState,
    onPayRent: () -> Unit,
    onPayBills: () -> Unit,
    onHistory: () -> Unit
) {
    PremiumCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(46.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.AccountBalanceWallet, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "TOTAL BALANCE", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Gray, letterSpacing = 1.sp)
                Text(text = "KES ${formatPaymentMoney(tenant.totalDue)}", fontSize = 24.sp, fontWeight = FontWeight.Black)
            }
            StatusBadge(
                text = tenant.rentStatus.name.replace("_", " "),
                background = MaterialTheme.colorScheme.errorContainer,
                foreground = MaterialTheme.colorScheme.error
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            PaymentDetail(label = "Monthly Rent", value = "KES ${formatPaymentMoney(tenant.monthlyRent)}", modifier = Modifier.weight(1f))
            PaymentDetail(label = "Utility Bills", value = "KES ${formatPaymentMoney(tenant.outstandingAmount)}", modifier = Modifier.weight(1f))
            PaymentDetail(label = "Due Date", value = tenant.dueDate, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = onPayRent,
                modifier = Modifier.weight(1.2f).height(50.dp),
                shape = RoundedCornerShape(14.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(text = "Pay Rent", fontWeight = FontWeight.Bold)
            }
            OutlinedButton(
                onClick = onPayBills,
                modifier = Modifier.weight(1f).height(50.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(text = "Bills", fontWeight = FontWeight.Bold)
            }
        }
        TextButton(onClick = onHistory, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Text(text = "Statement History", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp))
        }
    }
}

@Composable
private fun PaymentDetail(label: String, value: String, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun QuickActionCard(action: TenantQuickActionUIModel, gradient: List<Color>, onClick: () -> Unit) {
    QuickAction(icon = action.icon, title = action.title, onClick = onClick, gradient = gradient)
}

@Composable
private fun AgreementCard(onClick: () -> Unit) {
    PremiumCard(onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Gavel, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Lease Agreement", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(text = "View legal terms & conditions", fontSize = 11.sp, color = Color.Gray)
            }
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}

@Composable
private fun SearchApartmentCard(onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(48.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Find Your Next Home", fontSize = 16.sp, fontWeight = FontWeight.Black)
                Text(text = "Explore luxury apartments nearby", fontSize = 12.sp, color = Color.Gray)
            }
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun TenantDrawer(
    tenant: TenantDashboardUIState,
    onDashboard: () -> Unit,
    onProfile: () -> Unit,
    onAgreement: () -> Unit,
    onPaymentHistory: () -> Unit,
    onNotifications: () -> Unit,
    onSettings: () -> Unit,
    onLogout: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(top = 40.dp)) {
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Box(
                modifier = Modifier.size(64.dp).clip(CircleShape).background(Brush.linearGradient(PremiumGradient)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Person, contentDescription = null, modifier = Modifier.size(32.dp), tint = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = tenant.tenantName, fontSize = 20.sp, fontWeight = FontWeight.Black)
            Text(text = tenant.houseNumber, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp), color = Color.LightGray.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(16.dp))
        DrawerItem(icon = Icons.Default.Home, title = "Dashboard", onClick = onDashboard)
        DrawerItem(icon = Icons.Default.Person, title = "Personal Profile", onClick = onProfile)
        DrawerItem(icon = Icons.Default.Gavel, title = "Legal Agreements", onClick = onAgreement)
        DrawerItem(icon = Icons.AutoMirrored.Filled.ReceiptLong, title = "Payment History", onClick = onPaymentHistory)
        DrawerItem(icon = Icons.Default.Notifications, title = "Notification Center", onClick = onNotifications)
        DrawerItem(icon = Icons.Default.Settings, title = "System Settings", onClick = onSettings)
        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp), color = Color.LightGray.copy(alpha = 0.2f))
        DrawerItem(icon = Icons.AutoMirrored.Filled.Logout, title = "Secure Logout", onClick = onLogout, destructive = true)
        Spacer(modifier = Modifier.height(20.dp).navigationBarsPadding())
    }
}

@Composable
private fun DrawerItem(icon: ImageVector, title: String, onClick: () -> Unit, destructive: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 24.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = if (destructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = if (destructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun TenantBottomNavigation(selectedItem: Int, onItemSelected: (Int) -> Unit, unreadMessages: Int) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = { onItemSelected(0) },
            icon = { Icon(Icons.Default.Home, "Home") },
            label = { Text("Home", fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, unselectedIconColor = Color.Gray)
        )
        NavigationBarItem(
            selected = selectedItem == 1,
            onClick = { onItemSelected(1) },
            icon = { Icon(Icons.Default.Search, "Search") },
            label = { Text("Search", fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, unselectedIconColor = Color.Gray)
        )
        NavigationBarItem(
            selected = selectedItem == 2,
            onClick = { onItemSelected(2) },
            icon = { Icon(Icons.Default.Payment, "Payments") },
            label = { Text("Payments", fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, unselectedIconColor = Color.Gray)
        )
        NavigationBarItem(
            selected = selectedItem == 3,
            onClick = { onItemSelected(3) },
            icon = {
                BadgedBox(badge = { if (unreadMessages > 0) Badge(containerColor = MaterialTheme.colorScheme.primary) { Text(unreadMessages.toString(), color = Color.White) } }) {
                    Icon(Icons.AutoMirrored.Filled.Chat, "Chat")
                }
            },
            label = { Text("Chat", fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, unselectedIconColor = Color.Gray)
        )
        NavigationBarItem(
            selected = selectedItem == 4,
            onClick = { onItemSelected(4) },
            icon = { Icon(Icons.Default.Person, "Profile") },
            label = { Text("Profile", fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, unselectedIconColor = Color.Gray)
        )
    }
}
