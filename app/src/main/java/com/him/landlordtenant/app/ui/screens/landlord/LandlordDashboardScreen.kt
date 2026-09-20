package com.him.landlordtenant.app.ui.screens.landlord

import androidx.compose.foundation.lazy.items
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.graphicsLayer
import coil.compose.AsyncImage
import com.him.landlordtenant.app.ui.screens.tenant.LiquidBackground
import com.him.landlordtenant.app.ui.theme.*
import com.him.landlordtenant.app.ui.screens.tenant.LandlordDashboardUIState
import com.him.landlordtenant.app.ui.screens.tenant.LandlordActivityUIModel
import com.him.landlordtenant.app.ui.screens.tenant.SectionTitle
import com.him.landlordtenant.app.ui.screens.tenant.StatusBadge
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun CinematicBusinessHeader(name: String) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }

    Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
        AsyncImage(
            model = "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?q=80&w=2070&auto=format&fit=crop",
            contentDescription = null,
            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(32.dp)).graphicsLayer(alpha = 0.5f),
            contentScale = ContentScale.Crop
        )
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))))
                .padding(24.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Column {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(1000)) + slideInHorizontally(tween(1000), initialOffsetX = { -50 })
                ) {
                    Text(
                        text = "Real Estate Portfolio",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.5.sp
                    )
                }
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(1200)) + slideInHorizontally(tween(1200), initialOffsetX = { -70 })
                ) {
                    Text(text = name, fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.White)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LandlordDashboardScreen(
    dashboardData: LandlordDashboardUIState,
    unreadMessages: Int = 0,
    onNotifications: () -> Unit = {},
    onProfile: () -> Unit = {},
    onAddProperty: () -> Unit = {},
    onManageProperties: () -> Unit = {},
    onMarketplace: () -> Unit = {},
    onEditProperty: (String) -> Unit = {},
    onTenants: () -> Unit = {},
    onPayments: () -> Unit = {},
    onMaintenance: () -> Unit = {},
    onReports: () -> Unit = {},
    onStaff: () -> Unit = {},
    onExpenses: () -> Unit = {},
    onMarketing: () -> Unit = {},
    onDocuments: () -> Unit = {},
    onViewings: () -> Unit = {},
    onMessages: () -> Unit = {},
    onChat: () -> Unit = {},
    onAgreements: () -> Unit = {},
    onSettings: () -> Unit = {},
    onLogout: () -> Unit = {},
    onSwitchRole: () -> Unit = {},
    onRefresh: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        onRefresh()
    }
    
    LandlordDashboardContent(
        dashboardData = dashboardData,
        unreadMessages = unreadMessages,
        drawerState = drawerState,
        onMenu = { scope.launch { drawerState.open() } },
        onNotifications = onNotifications,
        onProfile = onProfile,
        onAddProperty = onAddProperty,
        onManageProperties = onManageProperties,
        onMarketplace = onMarketplace,
        onEditProperty = onEditProperty,
        onTenants = onTenants,
        onPayments = onPayments,
        onMaintenance = onMaintenance,
        onReports = onReports,
        onStaff = onStaff,
        onExpenses = onExpenses,
        onMarketing = onMarketing,
        onDocuments = onDocuments,
        onViewings = onViewings,
        onMessages = onMessages,
        onChat = onChat,
        onAgreements = onAgreements,
        onSettings = onSettings,
        onLogout = { scope.launch { drawerState.close() }; onLogout() },
        onSwitchRole = { scope.launch { drawerState.close() }; onSwitchRole() },
        onCloseDrawer = { scope.launch { drawerState.close() } }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LandlordDashboardContent(
    dashboardData: LandlordDashboardUIState,
    unreadMessages: Int,
    drawerState: DrawerState,
    onMenu: () -> Unit,
    onNotifications: () -> Unit,
    onProfile: () -> Unit,
    onAddProperty: () -> Unit,
    onManageProperties: () -> Unit,
    onMarketplace: () -> Unit,
    onEditProperty: (String) -> Unit,
    onTenants: () -> Unit,
    onPayments: () -> Unit,
    onMaintenance: () -> Unit,
    onReports: () -> Unit,
    onStaff: () -> Unit,
    onExpenses: () -> Unit,
    onMarketing: () -> Unit,
    onDocuments: () -> Unit,
    onViewings: () -> Unit,
    onMessages: () -> Unit,
    onChat: () -> Unit,
    onAgreements: () -> Unit,
    onSettings: () -> Unit,
    onLogout: () -> Unit,
    onSwitchRole: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerShape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp)
            ) {
                LandlordDrawer(
                    name = dashboardData.businessName,
                    onManageProperties = { onCloseDrawer(); onManageProperties() },
                    onMarketplace = { onCloseDrawer(); onMarketplace() },
                    onTenants = { onCloseDrawer(); onTenants() },
                    onPayments = { onCloseDrawer(); onPayments() },
                    onStaff = { onCloseDrawer(); onStaff() },
                    onExpenses = { onCloseDrawer(); onExpenses() },
                    onMarketing = { onCloseDrawer(); onMarketing() },
                    onDocuments = { onCloseDrawer(); onDocuments() },
                    onViewings = { onCloseDrawer(); onViewings() },
                    onMessages = { onCloseDrawer(); onMessages() },
                    onAgreements = { onCloseDrawer(); onAgreements() },
                    onSettings = { onCloseDrawer(); onSettings() },
                    onSwitchRole = { onCloseDrawer(); onSwitchRole() },
                    onLogout = { onCloseDrawer(); onLogout() }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                LandlordTopBar(
                    unreadMessages = unreadMessages,
                    onMenu = onMenu,
                    onNotifications = onNotifications,
                    onMessages = onMessages,
                    onProfile = onProfile
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAddProperty,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, "Add Property")
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                LiquidBackground(MaterialTheme.colorScheme.secondary)
                
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 24.dp)
                    ) {
                        item { 
                            StaggeredFadeIn(delay = 100) {
                                CinematicBusinessHeader(dashboardData.businessName) 
                            }
                        }

                        item {
                            StaggeredFadeIn(delay = 150) {
                                PortfolioActionHub(
                                    onCollectRent = onPayments,
                                    onPostAnnouncement = onMarketing,
                                    onEmergencyAlert = { onReports() }
                                )
                            }
                        }
                        
                        item { 
                            AnimatedStatRow(
                                propertyCount = dashboardData.totalProperties,
                                revenue = dashboardData.totalRevenue,
                                occupancy = dashboardData.occupancyRate
                            )
                        }

                        item { StaggeredFadeIn(delay = 300) { RevenueInsightsCard(dashboardData) } }

                        item {
                            StaggeredFadeIn(delay = 320) {
                                FinancialHealthGauge(score = dashboardData.healthScore)
                            }
                        }

                        item {
                            StaggeredFadeIn(delay = 350) {
                                TenancyLifecycleCard(
                                    renewals = dashboardData.upcomingRenewals,
                                    notices = dashboardData.vacateNotices,
                                    applications = dashboardData.pendingApplications,
                                    onViewDetails = onTenants
                                )
                            }
                        }
                        
                        item { StaggeredFadeIn(delay = 400) { SectionTitle(title = "My Properties", actionText = "View All", onAction = onManageProperties) } }
                        
                        item { StaggeredFadeIn(delay = 450) { OccupancyHeatmapCard(dashboardData) } }

                        item { StaggeredFadeIn(delay = 500) { MaintenancePredictorCard(dashboardData, onMaintenance) } }

                        item { StaggeredFadeIn(delay = 550) { TenantSentimentCard(dashboardData.tenantSentiment) } }

                        item { StaggeredFadeIn(delay = 600) { BuildingComplianceCard(dashboardData, onDocuments) } }
                        
                        if (dashboardData.properties.isEmpty()) {
                            item {
                                StaggeredFadeIn(delay = 500) {
                                    EmptyPropertiesCard(onAddProperty)
                                }
                            }
                        } else {
                            item {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    contentPadding = PaddingValues(horizontal = 4.dp)
                                ) {
                                    itemsIndexed(dashboardData.properties) { index, property ->
                                        var isVisible by remember { mutableStateOf(false) }
                                        LaunchedEffect(Unit) {
                                            kotlinx.coroutines.delay(index * 150L)
                                            isVisible = true
                                        }
                                        AnimatedVisibility(
                                            visible = isVisible,
                                            enter = slideInHorizontally(initialOffsetX = { 100 }) + fadeIn()
                                        ) {
                                            PropertyCompactCard(property) { onEditProperty(property.id) }
                                        }
                                    }
                                }
                            }
                        }
                        
                        item { 
                            StaggeredFadeIn(delay = 600) {
                                SectionTitle(title = "Recent Activities") 
                            }
                        }
                        
                        itemsIndexed(dashboardData.recentActivities) { index, activity ->
                            var isVisible by remember { mutableStateOf(false) }
                            LaunchedEffect(Unit) {
                                kotlinx.coroutines.delay(index * 100L)
                                isVisible = true
                            }
                            AnimatedVisibility(visible = isVisible, enter = fadeIn() + slideInVertically { 20 }) {
                                ActivityItem(activity)
                            }
                        }
                        
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedStatRow(propertyCount: Int, revenue: String, occupancy: String) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }
    
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AnimatedVisibility(
                visible = isVisible,
                modifier = Modifier.weight(1f),
                enter = slideInHorizontally(initialOffsetX = { -100 }) + fadeIn()
            ) {
                StatCard(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Properties",
                    value = propertyCount.toString(),
                    icon = Icons.Default.Apartment,
                    color = Color(0xFF673AB7),
                    containerColor = Color(0xFFEDE7F6)
                )
            }
            AnimatedVisibility(
                visible = isVisible,
                modifier = Modifier.weight(1f),
                enter = slideInHorizontally(initialOffsetX = { 100 }) + fadeIn()
            ) {
                StatCard(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Occupancy",
                    value = occupancy,
                    icon = Icons.Default.PieChart,
                    color = Color(0xFF1E88E5),
                    containerColor = Color(0xFFE3F2FD)
                )
            }
        }
        
        AnimatedVisibility(
            visible = isVisible,
            modifier = Modifier.fillMaxWidth(),
            enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn()
        ) {
            StatCard(
                modifier = Modifier.fillMaxWidth(),
                label = "Total Portfolio Revenue",
                value = revenue,
                icon = Icons.AutoMirrored.Filled.TrendingUp,
                color = Color(0xFF43A047),
                containerColor = Color(0xFFE8F5E9)
            )
        }
    }
}

@Composable
private fun EmptyPropertiesCard(onAdd: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onAdd),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.AddHomeWork, null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(12.dp))
            Text("No properties found", fontWeight = FontWeight.Bold)
            Text("Tap to add your first property", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun StaggeredFadeIn(delay: Int, content: @Composable () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delay.toLong())
        isVisible = true
    }
    AnimatedVisibility(visible = isVisible, enter = fadeIn() + slideInVertically { 20 }) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LandlordTopBar(unreadMessages: Int = 0, onMenu: () -> Unit, onNotifications: () -> Unit, onMessages: () -> Unit = {}, onProfile: () -> Unit) {
    TopAppBar(
        title = { Text("propertyOS", fontWeight = FontWeight.Black, fontSize = 20.sp) },
        navigationIcon = { IconButton(onClick = onMenu) { Icon(Icons.Default.Menu, null) } },
        actions = {
            IconButton(onClick = onMessages) {
                BadgedBox(
                    badge = {
                        if (unreadMessages > 0) {
                            Badge(containerColor = MaterialTheme.colorScheme.primary) {
                                Text(if (unreadMessages > 999) "999+" else unreadMessages.toString(), color = Color.White)
                            }
                        }
                    }
                ) {
                    Icon(Icons.AutoMirrored.Filled.Chat, "Messages")
                }
            }
            IconButton(onClick = onNotifications) { Icon(Icons.Default.Notifications, null) }
            IconButton(onClick = onProfile) { Icon(Icons.Default.AccountCircle, null) }
        }
    )
}

@Composable
private fun StatCard(modifier: Modifier, label: String, value: String, icon: ImageVector, color: Color, containerColor: Color) {
    Card(modifier = modifier, shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = containerColor)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(label, fontSize = 12.sp, color = color.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Black, color = color)
        }
    }
}

@Composable
private fun PropertyCompactCard(property: com.him.landlordtenant.app.ui.screens.tenant.TenantApartmentUIModel, onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.98f else 1f, label = "scale")

    Card(
        modifier = Modifier
            .width(280.dp)
            .scale(scale)
            .clickable(
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(140.dp).background(Color.LightGray)) {
                if (property.images.isNotEmpty()) {
                    AsyncImage(
                        model = property.images.first(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize().background(Brush.linearGradient(PremiumGradient.map { it.copy(alpha = 0.85f) })), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Apartment, null, modifier = Modifier.size(50.dp), tint = Color.White.copy(alpha = 0.3f))
                    }
                }
                
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .background(Color.White.copy(alpha = 0.9f), CircleShape)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "ACTIVE", 
                        fontSize = 9.sp, 
                        fontWeight = FontWeight.Black, 
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = property.name, 
                    fontWeight = FontWeight.Black, 
                    fontSize = 18.sp, 
                    maxLines = 1, 
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${property.location} • ${property.totalUnits} Units", 
                    fontSize = 12.sp, 
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Edit, null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Manage Property", 
                        fontSize = 13.sp, 
                        fontWeight = FontWeight.Bold, 
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardQuickAction(title: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier.width(100.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(40.dp).background(color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PortfolioActionHub(
    onCollectRent: () -> Unit,
    onPostAnnouncement: () -> Unit,
    onEmergencyAlert: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HubActionItem(Icons.Default.Payments, "Collect Rent", PremiumGradient, onCollectRent)
            HubActionItem(Icons.Default.Campaign, "Announce", listOf(Color(0xFFFF9800), Color(0xFFFF5722)), onPostAnnouncement)
            HubActionItem(Icons.Default.Warning, "Emergency", listOf(Color(0xFFF44336), Color(0xFFD32F2F)), onEmergencyAlert)
        }
    }
}

@Composable
private fun HubActionItem(icon: ImageVector, label: String, gradient: List<Color>, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .background(Brush.linearGradient(gradient), CircleShape)
                .shadow(4.dp, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Color.White, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
    }
}

@Composable
private fun RevenueInsightsCard(data: LandlordDashboardUIState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).background(Color(0xFFE8F5E9), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AutoGraph, null, tint = Color(0xFF43A047), modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Revenue Stream", fontWeight = FontWeight.Black, fontSize = 16.sp)
                    Text("Live collection trend across all properties", fontSize = 11.sp, color = Color.Gray)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Custom Mini-Chart
            Row(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                val max = data.revenueTrend.maxOrNull()?.coerceAtLeast(1f) ?: 1f
                data.revenueTrend.forEachIndexed { index, amount ->
                    val weight = (amount / max).coerceAtMost(1f)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(weight.coerceAtLeast(0.1f))
                            .background(
                                Brush.verticalGradient(
                                    if (index == data.revenueTrend.size - 1) PremiumGradient 
                                    else listOf(Color.LightGray.copy(alpha = 0.3f), Color.LightGray.copy(alpha = 0.1f))
                                ),
                                RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                            )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                InsightTag("Current Revenue", data.totalRevenue, Color(0xFF43A047))
            }
        }
    }
}

@Composable
private fun FinancialHealthGauge(score: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.HealthAndSafety, null, tint = Color(0xFF673AB7), modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Portfolio Health Score", fontWeight = FontWeight.Black, fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                // Background Circle
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.size(140.dp),
                    color = Color.LightGray.copy(alpha = 0.2f),
                    strokeWidth = 12.dp,
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                )
                
                // Score Circle
                CircularProgressIndicator(
                    progress = { score / 100f },
                    modifier = Modifier.size(140.dp),
                    color = when {
                        score > 80 -> Color(0xFF43A047)
                        score > 50 -> Color(0xFFFFB300)
                        else -> Color(0xFFF44336)
                    },
                    strokeWidth = 12.dp,
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                )
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "$score", fontSize = 36.sp, fontWeight = FontWeight.Black)
                    Text(text = "EXCELLENT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF43A047), letterSpacing = 1.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Based on on-time rent collection, occupancy stability, and maintenance efficiency.",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun OccupancyHeatmapCard(data: LandlordDashboardUIState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).background(Color(0xFFE3F2FD), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Layers, null, tint = Color(0xFF1E88E5), modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Occupancy Heatmap", fontWeight = FontWeight.Black, fontSize = 16.sp)
                    Text("Unit availability distribution", fontSize = 11.sp, color = Color.Gray)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Real Unit Heatmap
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 8,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val totalDisplayUnits = data.totalUnits.coerceAtMost(48)
                val occupiedUnits = data.totalUnits - data.totalProperties * (data.properties.firstOrNull()?.availableUnits ?: 0) // Simplified
                
                repeat(totalDisplayUnits) { index ->
                    val isOccupied = index < (data.totalUnits - data.properties.sumOf { it.availableUnits })
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isOccupied) Color(0xFFE8F5E9) else Color(0xFFFFEBEE))
                            .border(1.dp, if (isOccupied) Color(0xFF43A047).copy(alpha = 0.2f) else Color(0xFFF44336).copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}", 
                            fontSize = 10.sp, 
                            fontWeight = FontWeight.Bold, 
                            color = if (isOccupied) Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LegendItem("Occupied", Color(0xFF43A047))
                    Spacer(modifier = Modifier.width(16.dp))
                    LegendItem("Vacant", Color(0xFFF44336))
                }
                Text("${data.totalUnits - data.properties.sumOf { it.availableUnits }} / ${data.totalUnits}", fontSize = 12.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).background(color, RoundedCornerShape(2.dp)))
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, fontSize = 10.sp, color = Color.Gray)
    }
}

@Composable
private fun MaintenancePredictorCard(data: LandlordDashboardUIState, onMaintenance: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).background(Color(0xFFFFF3E0), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Psychology, null, tint = Color(0xFFFF9800), modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("AI Maintenance Insights", fontWeight = FontWeight.Black, fontSize = 16.sp)
                    Text("Predicted infrastructure needs", fontSize = 11.sp, color = Color.Gray)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (data.maintenancePredictions.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("Analyzing historical patterns...", fontSize = 12.sp, color = Color.Gray)
                }
            } else {
                data.maintenancePredictions.forEach { prediction ->
                    PredictionItem(
                        title = prediction.title,
                        property = prediction.property,
                        risk = prediction.risk,
                        riskColor = Color(android.graphics.Color.parseColor(prediction.riskColor)),
                        description = prediction.description
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = onMaintenance,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("View Maintenance Dashboard", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun PredictionItem(title: String, property: String, risk: String, riskColor: Color, description: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = property, fontSize = 11.sp, color = Color.Gray)
                }
                StatusBadge(text = risk, background = riskColor.copy(alpha = 0.1f), foreground = riskColor)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 18.sp)
        }
    }
}

@Composable
private fun TenantSentimentCard(score: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).background(Color(0xFFFFEBEE), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Favorite, null, tint = Color(0xFFE91E63), modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Resident Satisfaction", fontWeight = FontWeight.Black, fontSize = 16.sp)
                    Text("Overall sentiment and feedback trends", fontSize = 11.sp, color = Color.Gray)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(text = String.format("%.1f", score), fontSize = 48.sp, fontWeight = FontWeight.Black, color = Color(0xFFE91E63))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Row {
                        repeat(score.toInt()) { Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp)) }
                        if (score % 1.0 > 0.4) Icon(Icons.Default.StarHalf, null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                    }
                    Text(if (score > 4.0) "Excellent (Positive)" else if (score > 3.0) "Good" else "Needs Attention", fontSize = 12.sp, color = if (score > 3.5) Color(0xFF43A047) else Color(0xFFF44336), fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            SentimentBar("Communication", 0.9f)
            Spacer(modifier = Modifier.height(12.dp))
            SentimentBar("Maintenance", 0.75f)
            Spacer(modifier = Modifier.height(12.dp))
            SentimentBar("Amenities", 0.85f)
        }
    }
}

@Composable
private fun SentimentBar(label: String, progress: Float) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("${(progress * 100).toInt()}%", fontSize = 11.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
            color = if (progress > 0.8f) Color(0xFF43A047) else Color(0xFFFFB300),
            trackColor = Color.LightGray.copy(alpha = 0.2f)
        )
    }
}

@Composable
private fun BuildingComplianceCard(data: LandlordDashboardUIState, onUpload: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.05f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Gavel, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Legal & Compliance", fontWeight = FontWeight.Black, fontSize = 16.sp, color = MaterialTheme.colorScheme.error)
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            if (data.complianceStatus.isEmpty()) {
                ComplianceItem("Fire Safety Certificate", "Scanning documents...", isCritical = true)
                Spacer(modifier = Modifier.height(12.dp))
                ComplianceItem("Property Insurance", "Valid", isCritical = false)
            } else {
                data.complianceStatus.forEach { item ->
                    ComplianceItem(item.title, item.status, item.isCritical)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Button(
                onClick = onUpload,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("View Property Vault", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ComplianceItem(title: String, status: String, isCritical: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.size(8.dp).background(if (isCritical) Color(0xFFF44336) else Color(0xFF43A047), CircleShape))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(status, fontSize = 11.sp, color = if (isCritical) Color(0xFFF44336) else Color.Gray)
        }
    }
}

@Composable
private fun InsightTag(label: String, value: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).background(color, CircleShape))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = "$label: ", fontSize = 10.sp, color = Color.Gray)
        Text(text = value, fontSize = 10.sp, fontWeight = FontWeight.Black, color = color)
    }
}

@Composable
private fun RentCollectionChartCard(onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().height(200.dp).clickable(onClick = onClick), shape = RoundedCornerShape(24.dp)) {
        Box(modifier = Modifier.fillMaxSize().background(Brush.linearGradient(PremiumGradient.map { it.copy(alpha = 0.05f) })), contentAlignment = Alignment.Center) {
            Text("Collection Analytics (Coming Soon)", color = Color.Gray, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun TenancyLifecycleCard(renewals: Int, notices: Int, applications: Int, onViewDetails: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onViewDetails),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).background(Color(0xFFE8F5E9), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.HourglassEmpty, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Tenancy Pulse", fontWeight = FontWeight.Black, fontSize = 16.sp)
                    Text("Upcoming lease & occupancy events", fontSize = 11.sp, color = Color.Gray)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                LifecycleEventBox(
                    count = applications,
                    label = "New Applications",
                    color = Color(0xFFF57C00),
                    modifier = Modifier.weight(1f)
                )
                LifecycleEventBox(
                    count = renewals,
                    label = "Lease Renewals",
                    color = Color(0xFF1E88E5),
                    modifier = Modifier.weight(1f)
                )
                LifecycleEventBox(
                    count = notices,
                    label = "Move-out Notices",
                    color = Color(0xFFF44336),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun LifecycleEventBox(count: Int, label: String, color: Color, modifier: Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.05f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = count.toString(), fontSize = 24.sp, fontWeight = FontWeight.Black, color = color)
            Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun ActivityItem(activity: LandlordActivityUIModel) {
    val iconColor = if (activity.status == "FAILURE") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    val containerColor = iconColor.copy(alpha = 0.1f)
    
    ListItem(
        headlineContent = { 
            Text(
                text = activity.title, 
                fontWeight = FontWeight.Bold,
                color = if (activity.status == "FAILURE") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            ) 
        },
        supportingContent = { Text(activity.subtitle) },
        overlineContent = { Text(activity.time, fontSize = 10.sp, color = Color.Gray) },
        leadingContent = {
            Box(modifier = Modifier.size(40.dp).background(containerColor, CircleShape), contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = if (activity.status == "FAILURE") Icons.Default.ErrorOutline else Icons.Default.Notifications, 
                    null, 
                    tint = iconColor, 
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        trailingContent = { activity.amount?.let { Text(it, fontWeight = FontWeight.Black, color = Color(0xFF43A047)) } }
    )
}

@Composable
private fun LandlordDrawer(
    name: String,
    onManageProperties: () -> Unit,
    onMarketplace: () -> Unit = {},
    onTenants: () -> Unit,
    onPayments: () -> Unit,
    onStaff: () -> Unit,
    onExpenses: () -> Unit,
    onMarketing: () -> Unit,
    onDocuments: () -> Unit,
    onViewings: () -> Unit = {},
    onMessages: () -> Unit,
    onAgreements: () -> Unit,
    onSettings: () -> Unit,
    onSwitchRole: () -> Unit,
    onLogout: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState())) {
        Text(name, fontWeight = FontWeight.Black, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(32.dp))
        DrawerItem(Icons.Default.Apartment, "Manage Properties", onManageProperties)
        DrawerItem(Icons.Default.Explore, "Public Marketplace", onMarketplace)
        DrawerItem(Icons.Default.People, "Tenants", onTenants)
        DrawerItem(Icons.Default.Engineering, "Staff & Professionals", onStaff)
        DrawerItem(Icons.Default.Payments, "Payments", onPayments)
        DrawerItem(Icons.Default.ReceiptLong, "Expense Tracker", onExpenses)
        DrawerItem(Icons.Default.Visibility, "Viewing Requests", onViewings)
        DrawerItem(Icons.Default.Campaign, "Marketing Hub", onMarketing)
        DrawerItem(Icons.Default.FolderZip, "Property Vault", onDocuments)
        DrawerItem(Icons.AutoMirrored.Filled.Chat, "Messages", onMessages)
        DrawerItem(Icons.Default.Description, "Agreements", onAgreements)
        DrawerItem(Icons.Default.Settings, "Settings", onSettings)
        Spacer(modifier = Modifier.weight(1f))
        DrawerItem(Icons.Default.SyncAlt, "Switch to Tenant", onSwitchRole)
        DrawerItem(Icons.AutoMirrored.Filled.Logout, "Logout", onLogout)
    }
}

@Composable
private fun DrawerItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Color.Gray)
        Spacer(modifier = Modifier.width(16.dp))
        Text(label, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun LandlordDashboardScreenPreview() {
    PropertyOSTheme {
        LandlordDashboardContent(
            dashboardData = LandlordDashboardUIState(
                businessName = "Sample Properties Ltd",
                email = "landlord@example.com",
                totalProperties = 1,
                totalUnits = 10,
                totalRevenue = "KSh 150k",
                occupancyRate = "100%",
                recentActivities = listOf(
                    LandlordActivityUIModel("1", "Application Received", "New Tenant", null, "2 mins ago", "TENANT")
                )
            ),
            unreadMessages = 0,
            drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
            onMenu = {},
            onNotifications = {},
            onProfile = {},
            onAddProperty = {},
            onManageProperties = {},
            onMarketplace = {},
            onEditProperty = {},
            onTenants = {},
            onPayments = {},
            onMaintenance = {},
            onReports = {},
            onStaff = {},
            onExpenses = {},
            onMarketing = {},
            onDocuments = {},
            onViewings = {},
            onMessages = {},
            onChat = {},
            onAgreements = {},
            onSettings = {},
            onLogout = {},
            onSwitchRole = {},
            onCloseDrawer = {}
        )
    }
}
