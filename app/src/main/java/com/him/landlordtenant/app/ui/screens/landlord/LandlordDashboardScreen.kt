package com.him.landlordtenant.app.ui.screens.landlord

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
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.TrendingUp
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.*
import com.him.landlordtenant.app.ui.screens.tenant.LandlordDashboardUIState
import com.him.landlordtenant.app.ui.screens.tenant.LandlordActivityUIModel
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandlordDashboardScreen(
    dashboardData: LandlordDashboardUIState,
    onNotifications: () -> Unit = {},
    onProfile: () -> Unit = {},
    onAddProperty: () -> Unit = {},
    onManageProperties: () -> Unit = {},
    onTenants: () -> Unit = {},
    onPayments: () -> Unit = {},
    onMaintenance: () -> Unit = {},
    onReports: () -> Unit = {},
    onSettings: () -> Unit = {},
    onChat: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    // Animation for entries
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
                LandlordDrawerHeader(dashboardData.businessName, dashboardData.totalProperties, dashboardData.totalUnits)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(12.dp))
                NavigationDrawerItem(
                    label = { Text("Dashboard", fontWeight = FontWeight.Bold) },
                    selected = true,
                    onClick = { scope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Default.Dashboard, null) },
                    modifier = Modifier.padding(horizontal = 12.dp),
                    shape = RoundedCornerShape(16.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Properties", fontWeight = FontWeight.Medium) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; onManageProperties() },
                    icon = { Icon(Icons.Default.Apartment, null) },
                    modifier = Modifier.padding(horizontal = 12.dp),
                    shape = RoundedCornerShape(16.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Tenants", fontWeight = FontWeight.Medium) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; onTenants() },
                    icon = { Icon(Icons.Default.People, null) },
                    modifier = Modifier.padding(horizontal = 12.dp),
                    shape = RoundedCornerShape(16.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Payments", fontWeight = FontWeight.Medium) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; onPayments() },
                    icon = { Icon(Icons.Default.Payment, null) },
                    modifier = Modifier.padding(horizontal = 12.dp),
                    shape = RoundedCornerShape(16.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Messages", fontWeight = FontWeight.Medium) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; onChat() },
                    icon = { Icon(Icons.AutoMirrored.Filled.Chat, null) },
                    modifier = Modifier.padding(horizontal = 12.dp),
                    shape = RoundedCornerShape(16.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Maintenance", fontWeight = FontWeight.Medium) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; onMaintenance() },
                    icon = { Icon(Icons.Default.Build, null) },
                    modifier = Modifier.padding(horizontal = 12.dp),
                    shape = RoundedCornerShape(16.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Reports", fontWeight = FontWeight.Medium) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; onReports() },
                    icon = { Icon(Icons.Default.BarChart, null) },
                    modifier = Modifier.padding(horizontal = 12.dp),
                    shape = RoundedCornerShape(16.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Settings", fontWeight = FontWeight.Medium) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; onSettings() },
                    icon = { Icon(Icons.Default.Settings, null) },
                    modifier = Modifier.padding(horizontal = 12.dp),
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                NavigationDrawerItem(
                    label = { Text("Logout", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error) },
                    selected = false,
                    onClick = { 
                        scope.launch { drawerState.close() }
                        onLogout() 
                    },
                    icon = { Icon(Icons.AutoMirrored.Filled.Logout, null, tint = MaterialTheme.colorScheme.error) },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(text = "Hello, Landlord", fontSize = 18.sp, fontWeight = FontWeight.Black, letterSpacing = (-0.5).sp)
                            Text(text = dashboardData.businessName, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                    },
                    navigationIcon = { IconButton(onClick = { scope.launch { drawerState.open() } }) { Icon(Icons.Default.Menu, "Menu") } },
                    actions = {
                        IconButton(onClick = onNotifications) { Icon(Icons.Default.Notifications, "Notifications") }
                        IconButton(onClick = onProfile) { 
                            Surface(modifier = Modifier.size(32.dp), shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Person, null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAddProperty,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(16.dp)
                ) { Icon(Icons.Default.Add, "Add Property") }
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
                        item { OverviewStatsRow(dashboardData.totalRevenue, dashboardData.occupancyRate) }
                        item { RentCollectionChartCard() }
                        item { SectionTitle(title = "Quick Management", actionText = "View All", onAction = onManageProperties) }
                        item {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                ManagementCard(Modifier.weight(1f), Icons.Default.AddHome, "Add Unit", onAddProperty, PremiumGradient)
                                ManagementCard(Modifier.weight(1f), Icons.Default.PersonAdd, "Invite", onTenants, SkyGradient)
                                ManagementCard(Modifier.weight(1f), Icons.Default.Build, "Fixes", onMaintenance, RoseGradient)
                            }
                        }
                        item { SectionTitle(title = "Recent Activities") }
                        if (dashboardData.recentActivities.isEmpty()) {
                            item {
                                EmptyActivityPlaceholder()
                            }
                        } else {
                            itemsIndexed(dashboardData.recentActivities) { index, activity ->
                                val itemVisible = remember { mutableStateOf(false) }
                                LaunchedEffect(Unit) {
                                    kotlinx.coroutines.delay(index * 100L)
                                    itemVisible.value = true
                                }
                                AnimatedVisibility(visible = itemVisible.value, enter = fadeIn() + expandVertically()) {
                                    ActivityItem(activity)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun LandlordDrawerHeader(name: String, properties: Int, units: Int) {
    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(PremiumGradient)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Business, null, modifier = Modifier.size(32.dp), tint = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = "$properties Properties • $units Units", fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
private fun OverviewStatsRow(revenue: String, occupancy: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        StatCard(
            modifier = Modifier.weight(1f),
            label = "Total Revenue",
            value = revenue,
            icon = Icons.AutoMirrored.Filled.TrendingUp,
            color = Color(0xFF43A047),
            containerColor = Color(0xFFE8F5E9)
        )
        StatCard(
            modifier = Modifier.weight(1f),
            label = "Occupancy",
            value = occupancy,
            icon = Icons.Default.PieChart,
            color = Color(0xFF1E88E5),
            containerColor = Color(0xFFE3F2FD)
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier,
    label: String,
    value: String,
    icon: ImageVector,
    color: Color,
    containerColor: Color
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = containerColor.copy(alpha = 0.6f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onBackground)
            Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        }
    }
}

@Composable
private fun RentCollectionChartCard() {
    Surface(
        modifier = Modifier.fillMaxWidth().height(180.dp),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Collection Insights", fontWeight = FontWeight.Black, fontSize = 16.sp)
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.BarChart, null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                    Text("Analytics visualized here", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun ManagementCard(
    modifier: Modifier,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    gradient: List<Color>
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Brush.linearGradient(gradient), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, modifier = Modifier.size(20.dp), tint = Color.White)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun EmptyActivityPlaceholder() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.History,
            null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("All caught up!", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text("No new activities at the moment", fontSize = 11.sp, color = Color.Gray)
    }
}


@Composable
private fun ActivityItem(activity: LandlordActivityUIModel) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Notifications, null, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = activity.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = "${activity.subtitle} • ${activity.time}", fontSize = 11.sp, color = Color.Gray)
        }
        activity.amount?.let {
            Text(text = it, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}

@Composable
private fun SectionTitle(title: String, actionText: String? = null, onAction: () -> Unit = {}) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        if (actionText != null) {
            TextButton(onClick = onAction) { Text(text = actionText, fontSize = 12.sp) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LandlordDashboardScreenPreview() {
    PropertyOSTheme {
        LandlordDashboardScreen(
            dashboardData = LandlordDashboardUIState(
                businessName = "Premium Properties Ltd",
                email = "landlord@example.com",
                totalProperties = 12,
                totalUnits = 84,
                totalRevenue = "KSh 1.2M",
                occupancyRate = "92%",
                recentActivities = listOf(
                    LandlordActivityUIModel("1", "Rent Paid - House G2", "Tenant: Jane Doe", "+KSh 15,000", "2 mins ago", "PAYMENT")
                )
            )
        )
    }
}
