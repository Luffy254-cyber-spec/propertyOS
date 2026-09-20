package com.him.landlordtenant.app.ui.screens.tenant

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.him.landlordtenant.app.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun LiquidBackground(color: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "liquid")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val path = Path()
        
        path.moveTo(0f, height * 0.7f)
        
        for (x in 0..width.toInt() step 5) {
            val y = height * 0.75f + Math.sin(x * 0.01 + phase).toFloat() * 20f
            path.lineTo(x.toFloat(), y)
        }
        
        path.lineTo(width, height)
        path.lineTo(0f, height)
        path.close()
        
        drawPath(
            path = path,
            color = color.copy(alpha = 0.05f),
            style = Fill
        )
    }
}

@Composable
fun CinematicGreeting(tenantName: String, points: Int, onRewardsClick: () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }

    Box(modifier = Modifier.fillMaxWidth().height(160.dp)) {
        // Blurred background image for cinematic feel
        AsyncImage(
            model = "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?q=80&w=2070&auto=format&fit=crop",
            contentDescription = null,
            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(32.dp)).graphicsLayer(alpha = 0.4f),
            contentScale = ContentScale.Crop
        )
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))))
                .padding(24.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                Column {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(tween(1000)) + slideInHorizontally(tween(1000), initialOffsetX = { -50 })
                    ) {
                        Text(
                            text = "Good Evening,",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(tween(1200)) + slideInHorizontally(tween(1200), initialOffsetX = { -70 })
                    ) {
                        Text(text = tenantName, fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.White)
                    }
                }
                
                Surface(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.clickable(onClick = onRewardsClick),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                ) {
                    Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Stars, null, modifier = Modifier.size(16.dp), tint = Color(0xFFFFD700))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("$points pts", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantHomeScreen(
    tenant: TenantDashboardUIState,
    bills: List<TenantBillUIModel>,
    quickActions: List<TenantQuickActionUIModel>,
    unreadMessages: Int = 0,
    onBack: (() -> Unit)? = null,
    onSearchApartments: () -> Unit = {},
    onAvailableHouses: () -> Unit = {},
    onPayRent: () -> Unit = {},
    onPayBills: () -> Unit = {},
    onPaymentHistory: () -> Unit = {},
    onNotifications: () -> Unit = {},
    onMessages: () -> Unit = {},
    onMaintenance: () -> Unit = {},
    onAgreement: () -> Unit = {},
    onMyHouse: () -> Unit = {},
    onServices: () -> Unit = {},
    onRewards: () -> Unit = {},
    onAmenityBooking: () -> Unit = {},
    onMaintenanceHistory: () -> Unit = {},
    onUtilityUsage: () -> Unit = {},
    onDocumentVault: () -> Unit = {},
    onPropertyReviews: (String) -> Unit = {},
    onProfile: () -> Unit = {},
    onContactLandlord: () -> Unit = {},
    onSettings: () -> Unit = {},
    onLogout: () -> Unit = {},
    onDashboard: () -> Unit = {},
    onSwitchRole: () -> Unit = {},
    onRefresh: () -> Unit = {},
    onApplications: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedBottomItem by remember { mutableIntStateOf(0) }
    
    LaunchedEffect(Unit) {
        onRefresh()
    }
    
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
                    onApplications = { scope.launch { drawerState.close() }; onApplications() },
                    onAgreement = { scope.launch { drawerState.close() }; onAgreement() },
                    onRewards = { scope.launch { drawerState.close() }; onRewards() },
                    onServices = { scope.launch { drawerState.close() }; onServices() },
                    onVault = { scope.launch { drawerState.close() }; onDocumentVault() },
                    onUtilityUsage = { scope.launch { drawerState.close() }; onUtilityUsage() },
                    onPaymentHistory = { scope.launch { drawerState.close() }; onPaymentHistory() },
                    onNotifications = { scope.launch { drawerState.close() }; onNotifications() },
                    onSettings = { scope.launch { drawerState.close() }; onSettings() },
                    onSwitchRole = { scope.launch { drawerState.close() }; onSwitchRole() },
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
                    unreadMessages = unreadMessages,
                    onBack = onBack,
                    onMenu = { scope.launch { drawerState.open() } },
                    onNotifications = onNotifications,
                    onMessages = onMessages,
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
                    unreadMessages = unreadMessages
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                LiquidBackground(MaterialTheme.colorScheme.primary)
                
                AnimatedVisibility(
                    visible = visible.value,
                    enter = fadeIn(animationSpec = tween(800)) + slideInVertically(animationSpec = tween(800), initialOffsetY = { 100 })
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 24.dp)
                    ) {
                        item { 
                            CinematicGreeting(
                                tenantName = tenant.tenantName, 
                                points = tenant.loyaltyPoints,
                                onRewardsClick = onRewards
                            ) 
                        }

                        if (tenant.apartmentId.isNotEmpty() && tenant.houseNumber.isNotEmpty()) {
                            item {
                                StaggeredFadeIn(delay = 150) {
                                    PropertyNoticeBoard(
                                        apartmentName = tenant.apartmentName,
                                        onNoticeClick = onNotifications
                                    )
                                }
                            }
                        }
                        
                        if (tenant.apartmentId.isEmpty()) {
                            item { 
                                WelcomeIntroductionCard(
                                    onExplore = onSearchApartments
                                ) 
                            }
                            item {
                                SectionTitle(title = "Featured Apartments", actionText = "See All", onAction = onSearchApartments)
                            }
                            
                            if (tenant.featuredApartments.isEmpty()) {
                                item {
                                    ShimmerPropertyCard()
                                }
                            } else {
                                item {
                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                        contentPadding = PaddingValues(horizontal = 4.dp)
                                    ) {
                                        itemsIndexed(tenant.featuredApartments) { index, apartment ->
                                            var isRowVisible by remember { mutableStateOf(false) }
                                            LaunchedEffect(Unit) {
                                                kotlinx.coroutines.delay(index * 150L)
                                                isRowVisible = true
                                            }
                                            AnimatedVisibility(
                                                visible = isRowVisible,
                                                enter = slideInHorizontally(initialOffsetX = { 100 }) + fadeIn()
                                            ) {
                                                FeaturedApartmentCard(apartment) { onSearchApartments() }
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (tenant.houseNumber == "N/A" || tenant.houseNumber.isEmpty() || tenant.houseNumber == "GENERAL") {
                            item {
                                MoveInPromptCard(
                                    apartmentName = tenant.apartmentName,
                                    onMoveIn = onAvailableHouses
                                )
                            }
                            item {
                                SectionTitle(title = "Onboarding Checklist")
                            }
                            itemsIndexed(tenant.moveInChecklist) { index, item ->
                                var isItemVisible by remember { mutableStateOf(false) }
                                LaunchedEffect(Unit) {
                                    kotlinx.coroutines.delay(index * 100L)
                                    isItemVisible = true
                                }
                                AnimatedVisibility(
                                    visible = isItemVisible,
                                    enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn()
                                ) {
                                    ChecklistItemRow(
                                        item = item,
                                        onClick = {
                                            if (item.task.contains("house", ignoreCase = true)) {
                                                onAvailableHouses()
                                            }
                                        }
                                    )
                                }
                            }
                        } else {
                            item { 
                                StaggeredFadeIn(delay = 100) {
                                    CurrentHouseCard(tenant = tenant, onClick = onMyHouse) 
                                }
                            }
                            
                        item { 
                            StaggeredFadeIn(delay = 200) {
                                PaymentSummaryCard(
                                    tenant = tenant,
                                    onPayRent = onPayRent,
                                    onPayBills = onPayBills,
                                    onHistory = onPaymentHistory
                                )
                            }
                        }

                        item {
                            StaggeredFadeIn(delay = 250) {
                                UtilityConsumptionCard(tenant)
                            }
                        }
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
                                    Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.VerifiedUser, null, tint = Color(0xFF43A047), modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text("No pending bills. You're all clear!", color = Color.Gray, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                    }
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
                        
                        item { MaintenanceTrackerCard(tenant, onClick = onMaintenance, onHistoryClick = onMaintenanceHistory) }
                        
                        item {
                            LandlordContactCard(
                                landlordName = tenant.landlordName,
                                landlordPhone = tenant.landlordPhone,
                                onCall = onContactLandlord,
                                onMessage = onMessages
                            )
                        }
                        
                        item { AgreementCard(onClick = onAgreement) }
                        
                        item { DocumentVaultCard(onClick = onDocumentVault) }
                        
                        item { PropertyReviewCard(onClick = { onPropertyReviews(tenant.apartmentId) }) }
                        
                        item { CommunityInviteCard(tenant.apartmentName) }
                        
                        item { RateAppCard() }
                        
                        item { PropertyInsightsCard(tenant) }
                        
                        if (tenant.apartmentId.isNotEmpty()) {
                            item { DigitalKeyCard(tenant) }
                            item { StaggeredFadeIn(delay = 550) { GuestPassCard(onNotifications) } }
                            item { StaggeredFadeIn(delay = 600) { LoyaltyTierCard(tenant) } }
                            item { StaggeredFadeIn(delay = 650) { HouseholdSharingCard() } }
                            item { StaggeredFadeIn(delay = 700) { ReferAndEarnCard() } }
                        }
                        
                        item { LocalPerksCard() }

                        item { CommunityMarketplaceCard() }
                        
                        item { SearchApartmentCard(onClick = onSearchApartments) }
                        
                        item { Spacer(modifier = Modifier.height(20.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun PropertyNoticeBoard(apartmentName: String, onNoticeClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onNoticeClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(32.dp).background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.NotificationsActive, null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "LATEST UPDATES: $apartmentName", 
                    fontSize = 10.sp, 
                    fontWeight = FontWeight.Black, 
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(verticalAlignment = Alignment.Top) {
                Box(modifier = Modifier.size(6.dp).offset(y = 6.dp).background(MaterialTheme.colorScheme.primary, CircleShape))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Water service maintenance scheduled for tomorrow between 9 AM and 11 AM. Please plan accordingly.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 18.sp
                    )
                    Text(
                        text = "Posted 2 hours ago by Management",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FeaturedApartmentCard(apartment: TenantApartmentUIModel, onClick: () -> Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.width(280.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(140.dp).background(Color.LightGray)) {
                val imageUrl = apartment.images.firstOrNull()?.trim()
                if (!imageUrl.isNullOrEmpty()) {
                    val model = remember(imageUrl) {
                        coil.request.ImageRequest.Builder(context)
                            .data(if (imageUrl.startsWith("http")) imageUrl else "https://$imageUrl")
                            .crossfade(true)
                            .build()
                    }
                    
                    coil.compose.AsyncImage(
                        model = model,
                        contentDescription = apartment.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        error = rememberVectorPainter(Icons.Default.BrokenImage),
                        placeholder = ColorPainter(Color.LightGray.copy(alpha = 0.5f))
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize().background(Brush.linearGradient(PremiumGradient.map { it.copy(alpha = 0.3f) })), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Apartment, null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                    }
                }
                Box(modifier = Modifier.align(Alignment.TopEnd).padding(12.dp)) {
                    StatusBadge(text = "NEW", background = Color.White.copy(alpha = 0.9f), foreground = MaterialTheme.colorScheme.primary)
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(apartment.name, fontWeight = FontWeight.Black, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("${apartment.location} • KES ${formatPaymentMoney(apartment.startingRent)}", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun ChecklistItemRow(item: ChecklistItem, onClick: () -> Unit = {}) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(24.dp).clip(CircleShape).background(if (item.isCompleted) Color(0xFFE8F5E9) else Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (item.isCompleted) Icons.Default.Check else Icons.Default.Circle,
                    null,
                    modifier = Modifier.size(16.dp),
                    tint = if (item.isCompleted) Color(0xFF2E7D32) else Color.LightGray
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = item.task,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (item.isCompleted) FontWeight.Normal else FontWeight.Bold,
                color = if (item.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface,
                textDecoration = if (item.isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
            )
        }
    }
}

@Composable
private fun WelcomeIntroductionCard(onExplore: () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }
    
    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically() + fadeIn(animationSpec = tween(1000))
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
        ) {
            Column(modifier = Modifier.padding(28.dp)) {
                Text(
                    "Welcome to propertyOS",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "We're thrilled to have you here! Find your perfect home, manage your stay, and enjoy seamless living with our all-in-one property management tool.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(28.dp))
                Button(
                    onClick = onExplore,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text("Start Exploring Now", fontWeight = FontWeight.ExtraBold)
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(Icons.Default.Explore, null, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
private fun MoveInPromptCard(apartmentName: String, onMoveIn: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
        border = androidx.compose.foundation.BorderStroke(2.dp, Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)))
    ) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(64.dp).background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f), CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.VpnKey, null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.secondary)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Your Key is Waiting!", fontWeight = FontWeight.Black, fontSize = 22.sp)
            Text(
                "Congratulations! You've joined $apartmentName. The next step is to pick your favorite vacant house and start your new chapter.",
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onMoveIn, 
                modifier = Modifier.fillMaxWidth().height(56.dp), 
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Pick My New House 🏠", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun MaintenanceTrackerCard(tenant: TenantDashboardUIState, onClick: () -> Unit, onHistoryClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(48.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Build, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Maintenance Center", fontWeight = FontWeight.Black, fontSize = 16.sp)
                    Text("Report issues & track repair progress", fontSize = 11.sp, color = Color.Gray)
                }
            }
            
            if (tenant.maintenanceRequests > 0) {
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(20.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).background(Color(0xFF1E88E5), CircleShape))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "1 Active Request: Electrical Repair",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    StatusBadge(text = "IN PROGRESS", background = Color(0xFFE3F2FD), foreground = Color(0xFF1E88E5))
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Progress Bar
                LinearProgressIndicator(
                    progress = { 0.6f },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                    color = Color(0xFF1E88E5),
                    trackColor = Color.LightGray.copy(alpha = 0.2f)
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = onHistoryClick) {
                    Text("History", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onClick,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    Text("Report Issue", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ReferAndEarnCard() {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CardGiftcard, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Refer & Earn", fontWeight = FontWeight.Black, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Know a landlord? Refer them to propertyOS and get 500 loyalty points when they list their first property.", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    val sendIntent: android.content.Intent = android.content.Intent().apply {
                        action = android.content.Intent.ACTION_SEND
                        putExtra(android.content.Intent.EXTRA_TEXT, "Manage your properties like a pro with propertyOS. Sign up today: https://propertyos.app/landlord/referral")
                        type = "text/plain"
                    }
                    val shareIntent = android.content.Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Refer Landlord", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun RateAppCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Enjoying propertyOS?", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Please enjoy our app and rate it! Feel free to send us feedback—we'd love to hear from you.",
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                repeat(5) { Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(24.dp)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TenantTopBar(
    tenant: TenantDashboardUIState,
    unreadMessages: Int = 0,
    onBack: (() -> Unit)?,
    onMenu: () -> Unit,
    onNotifications: () -> Unit,
    onMessages: () -> Unit = {},
    onProfile: () -> Unit
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(36.dp).background(Brush.linearGradient(PremiumGradient), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.HomeWork,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "propertyOS",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = tenant.houseNumber,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                }
            } else {
                IconButton(onClick = onMenu) {
                    Icon(Icons.Default.Menu, "Menu")
                }
            }
        },
        actions = {
            IconButton(onClick = onMessages) {
                BadgedBox(
                    badge = {
                        if (unreadMessages > 0) {
                            Badge(containerColor = MaterialTheme.colorScheme.primary) {
                                Text(text = if (unreadMessages > 999) "999+" else unreadMessages.toString(), color = Color.White)
                            }
                        }
                    }
                ) {
                    Icon(Icons.AutoMirrored.Filled.Chat, "Messages", tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                }
            }

            IconButton(onClick = onNotifications) {
                BadgedBox(
                    badge = {
                        if (tenant.unreadNotifications > 0) {
                            Badge(containerColor = MaterialTheme.colorScheme.error) {
                                Text(text = tenant.unreadNotifications.toString(), color = Color.White)
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

@Composable
fun StaggeredFadeIn(
    delay: Int = 0,
    content: @Composable () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delay.toLong())
        isVisible = true
    }
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(600)) + slideInVertically(initialOffsetY = { 30 })
    ) {
        content()
    }
}

@Composable
private fun ShimmerPropertyCard() {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    Surface(
        modifier = Modifier.fillMaxWidth().height(120.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.LightGray.copy(alpha = alpha),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)).background(Color.Gray.copy(alpha = 0.2f)))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Box(modifier = Modifier.width(150.dp).height(16.dp).background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(4.dp)))
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.width(100.dp).height(12.dp).background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(4.dp)))
            }
        }
    }
}

@Composable
private fun CurrentHouseCard(tenant: TenantDashboardUIState, onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.98f else 1f, label = "scale")

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 12.dp
    ) {
        Box(
            modifier = Modifier
                .background(Brush.linearGradient(PremiumGradient))
                .fillMaxWidth()
        ) {
            // Background patterns/images for texture
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 30.dp, y = 30.dp)
                    .size(150.dp)
                    .background(Color.White.copy(alpha = 0.05f), CircleShape)
            )
            
            Column(modifier = Modifier.padding(28.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                            .shadow(4.dp, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ACTIVE RESIDENCE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White.copy(alpha = 0.7f),
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = tenant.apartmentName, 
                            fontSize = 20.sp, 
                            fontWeight = FontWeight.ExtraBold, 
                            color = Color.White
                        )
                        Text(
                            text = "${tenant.houseNumber} • ${tenant.floorNumber}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward, 
                        contentDescription = null, 
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(20.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tenant.location, 
                        fontSize = 12.sp, 
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.8f)
                    )
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
            PaymentDetail(label = "Monthly Rent", value = "KES ${formatPaymentMoney(tenant.totalRentDue)}", modifier = Modifier.weight(1f))
            PaymentDetail(label = "Utility Bills", value = "KES ${formatPaymentMoney(tenant.totalUtilitiesDue)}", modifier = Modifier.weight(1f))
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
private fun PropertyInsightsCard(tenant: TenantDashboardUIState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoGraph, null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Neighbor Insights", fontWeight = FontWeight.Black, fontSize = 16.sp, color = MaterialTheme.colorScheme.tertiary)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "85% of your neighbors in ${tenant.apartmentName} have already cleared their rent this month. Keep up the great streak!",
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
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
private fun CommunityInviteCard(apartmentName: String) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.GroupAdd, null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.tertiary)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Invite Your Neighbors", fontWeight = FontWeight.Black, fontSize = 18.sp)
            Text(
                "Help build a stronger community. Invite other tenants to join $apartmentName on propertyOS.",
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    val sendIntent: android.content.Intent = android.content.Intent().apply {
                        action = android.content.Intent.ACTION_SEND
                        putExtra(android.content.Intent.EXTRA_TEXT, "Hey! Join our community at $apartmentName using propertyOS. Download here: https://propertyos.app/invite")
                        type = "text/plain"
                    }
                    val shareIntent = android.content.Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Share Invite Link", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun GuestPassCard(onSchedule: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).background(Color(0xFFE8EAF6), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.QrCode, null, tint = Color(0xFF3F51B5), modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Visitor Pass", fontWeight = FontWeight.Black, fontSize = 16.sp)
                    Text("Generate secure access for guests", fontSize = 11.sp, color = Color.Gray)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { /* Simulated QR Generation */ },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Instant Pass", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = onSchedule,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Schedule", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun HouseholdSharingCard() {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Group, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Household Sharing", fontWeight = FontWeight.Black, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Invite family members or roommates to share digital keys and split bills.", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    val sendIntent: android.content.Intent = android.content.Intent().apply {
                        action = android.content.Intent.ACTION_SEND
                        putExtra(android.content.Intent.EXTRA_TEXT, "Join my household on propertyOS to share keys and manage our stay: https://propertyos.app/household/invite")
                        type = "text/plain"
                    }
                    val shareIntent = android.content.Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.PersonAdd, null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Invite Roommate", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun CommunityMarketplaceCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.1f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Storefront, null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Resident Market", fontWeight = FontWeight.Black, fontSize = 16.sp, color = MaterialTheme.colorScheme.tertiary)
                    Text("Buy & sell within your community", fontSize = 11.sp, color = Color.Gray)
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(3) { index ->
                    MarketplaceItem(
                        name = listOf("Office Chair", "Bicycle", "Coffee Table")[index],
                        price = listOf("KSh 4,500", "KSh 12,000", "KSh 3,000")[index]
                    )
                }
            }
        }
    }
}

@Composable
private fun MarketplaceItem(name: String, price: String) {
    var isInterested by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier.width(130.dp).clickable { isInterested = !isInterested },
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(80.dp).background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                if (isInterested) {
                    Icon(Icons.Default.Chat, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                } else {
                    Icon(Icons.Default.Image, null, tint = Color.LightGray)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(name, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(if (isInterested) "Contact Seller" else price, fontSize = 11.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun LoyaltyTierCard(tenant: TenantDashboardUIState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
    ) {
        Box(modifier = Modifier.background(Brush.linearGradient(PremiumGradient))) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.WorkspacePremium, null, tint = Color.White, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Elite Resident Status", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.White)
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("GOLD TIER", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("LEVEL 4", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f), modifier = Modifier.padding(bottom = 4.dp))
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                LinearProgressIndicator(
                    progress = { 0.75f },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                    color = Color.White,
                    trackColor = Color.White.copy(alpha = 0.2f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "250 more points until Platinum status.",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun LocalPerksCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).background(Color(0xFFE1F5FE), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.ConfirmationNumber, null, tint = Color(0xFF03A9F4), modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Neighborhood Perks", fontWeight = FontWeight.Black, fontSize = 16.sp)
                    Text("Exclusive offers for our residents", fontSize = 11.sp, color = Color.Gray)
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                item { PerkItem("Café Java", "15% OFF", Icons.Default.Coffee) }
                item { PerkItem("QuickMart", "Free Delivery", Icons.Default.LocalGroceryStore) }
                item { PerkItem("FitLife Gym", "Week Pass", Icons.Default.FitnessCenter) }
            }
        }
    }
}

@Composable
private fun PerkItem(brand: String, offer: String, icon: ImageVector) {
    Surface(
        modifier = Modifier.width(140.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.LightGray.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, tint = Color.Gray, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(brand, fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text(offer, fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF03A9F4))
        }
    }
}

@Composable
private fun DigitalKeyCard(tenant: TenantDashboardUIState) {
    var isUnlocked by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.VpnKey, null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Resident Smart Access", fontWeight = FontWeight.Black, fontSize = 16.sp, color = MaterialTheme.colorScheme.secondary)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            IconButton(
                onClick = { isUnlocked = !isUnlocked },
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        if (isUnlocked) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f), 
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = if (isUnlocked) Icons.Default.LockOpen else Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = if (isUnlocked) Color(0xFF43A047) else MaterialTheme.colorScheme.secondary
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = if (isUnlocked) "GATE ACCESS GRANTED" else "TAP TO UNLOCK GATE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = if (isUnlocked) Color(0xFF43A047) else Color.Gray,
                letterSpacing = 1.sp
            )
            
            Text(
                text = "Authorized for: ${tenant.apartmentName} Main Entrance",
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun DocumentVaultCard(onClick: () -> Unit) {
    PremiumCard(onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.FolderZip, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Document Vault", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.padding(top = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    VaultMiniBadge(Icons.Default.Badge, "ID")
                    VaultMiniBadge(Icons.Default.Description, "Lease")
                    VaultMiniBadge(Icons.Default.Receipt, "Bills")
                }
            }
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}

@Composable
private fun VaultMiniBadge(icon: ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.background(Color.LightGray.copy(alpha = 0.15f), RoundedCornerShape(6.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) {
        Icon(icon, null, modifier = Modifier.size(10.dp), tint = Color.Gray)
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
    }
}

@Composable
private fun UtilityConsumptionCard(tenant: TenantDashboardUIState) {
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
                    Icon(Icons.Default.WaterDrop, null, tint = Color(0xFF1E88E5), modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Utility Tracker", fontWeight = FontWeight.Black, fontSize = 16.sp)
                    Text("Monthly consumption summary", fontSize = 11.sp, color = Color.Gray)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                UtilityMetric(
                    label = "Water",
                    value = "12.5 units",
                    trend = "-5%",
                    isPositive = true,
                    modifier = Modifier.weight(1f)
                )
                UtilityMetric(
                    label = "Electricity",
                    value = "450 kWh",
                    trend = "+2%",
                    isPositive = false,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun UtilityMetric(label: String, value: String, trend: String, isPositive: Boolean, modifier: Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.Black)
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                Icon(
                    imageVector = if (isPositive) Icons.Default.TrendingDown else Icons.Default.TrendingUp,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = if (isPositive) Color(0xFF43A047) else Color(0xFFF44336)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = trend,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isPositive) Color(0xFF43A047) else Color(0xFFF44336)
                )
            }
        }
    }
}

@Composable
private fun PropertyReviewCard(onClick: () -> Unit) {
    PremiumCard(onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.RateReview, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Property Reviews", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(text = "Share your stay experience", fontSize = 11.sp, color = Color.Gray)
            }
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}

@Composable
private fun LifeActionCard(title: String, icon: ImageVector, gradient: List<Color>, onClick: () -> Unit) {
    Card(
        modifier = Modifier.width(160.dp).height(100.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    ) {
        Box(modifier = Modifier.fillMaxSize().background(Brush.linearGradient(gradient.map { it.copy(alpha = 0.05f) }))) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(icon, null, tint = gradient.first(), modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = gradient.first())
            }
        }
    }
}

@Composable
private fun TenantDrawer(
    tenant: TenantDashboardUIState,
    onDashboard: () -> Unit,
    onProfile: () -> Unit,
    onApplications: () -> Unit,
    onAgreement: () -> Unit,
    onRewards: () -> Unit,
    onServices: () -> Unit,
    onVault: () -> Unit = {},
    onUtilityUsage: () -> Unit = {},
    onPaymentHistory: () -> Unit,
    onNotifications: () -> Unit,
    onSettings: () -> Unit,
    onSwitchRole: () -> Unit,
    onLogout: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(top = 40.dp).verticalScroll(rememberScrollState())) {
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
        DrawerItem(icon = Icons.AutoMirrored.Filled.Assignment, title = "My Applications", onClick = onApplications)
        DrawerItem(icon = Icons.Default.Stars, title = "My Rewards", onClick = onRewards)
        DrawerItem(icon = Icons.Default.Storefront, title = "Local Services", onClick = onServices)
        DrawerItem(icon = Icons.Default.BarChart, title = "Usage Stats", onClick = onUtilityUsage)
        DrawerItem(icon = Icons.Default.FolderZip, title = "Document Vault", onClick = onVault)
        DrawerItem(icon = Icons.Default.Gavel, title = "Legal Agreements", onClick = onAgreement)
        DrawerItem(icon = Icons.AutoMirrored.Filled.ReceiptLong, title = "Payment History", onClick = onPaymentHistory)
        DrawerItem(icon = Icons.Default.Notifications, title = "Notification Center", onClick = onNotifications)
        DrawerItem(icon = Icons.Default.Settings, title = "System Settings", onClick = onSettings)
        
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().clickable(onClick = onSwitchRole),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
        ) {
            Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.SyncAlt, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text("Switch to Landlord", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
        }

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
