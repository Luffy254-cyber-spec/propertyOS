package com.him.landlordtenant.app.ui.screens.guest

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.*
import com.him.landlordtenant.app.ui.screens.tenant.StatusBadge
import kotlinx.coroutines.launch

import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.screens.tenant.TenantApartmentUIModel
import com.him.landlordtenant.app.ui.screens.tenant.StatusBadge
import com.him.landlordtenant.app.ui.viewmodel.guest.GuestViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestHomeScreen(
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onBack: () -> Unit = {},
    onApartmentSelected: (String) -> Unit = {},
    onCallProperty: (String) -> Unit = {},
    viewModel: GuestViewModel = hiltViewModel()
) {
    val apartments by viewModel.apartments.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val counties = listOf("All", "Nairobi", "Kiambu", "Mombasa", "Kisumu", "Nakuru")
    
    var searchQuery by remember { mutableStateOf("") }
    var selectedCounty by remember { mutableStateOf("All") }
    var previewApartment by remember { mutableStateOf<TenantApartmentUIModel?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    
    val listVisible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { 
        kotlinx.coroutines.delay(100L)
        listVisible.value = true 
    }

    val filteredApartments = apartments.filter { apartment ->
        val query = searchQuery.trim().lowercase()
        val matchesSearch = query.isBlank() || apartment.name.lowercase().contains(query) || apartment.location.lowercase().contains(query) || apartment.county.lowercase().contains(query)
        val matchesCounty = selectedCounty == "All" || apartment.county == selectedCounty
        matchesSearch && matchesCounty
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.HomeWork, null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "propertyOS", fontWeight = FontWeight.Black, fontSize = 20.sp, letterSpacing = (-0.5).sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
                },
                actions = {
                    Surface(
                        onClick = onLogin,
                        modifier = Modifier.padding(end = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ) {
                        Text("SIGN IN", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        },
        bottomBar = {
            GuestBottomBar(onLogin = onLogin, onRegister = onRegister)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            AnimatedVisibility(
                visible = listVisible.value,
                enter = fadeIn(animationSpec = tween(800)) + slideInVertically(animationSpec = tween(800), initialOffsetY = { 100 })
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                    // Header Area
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f), Color.Transparent)))
                            .padding(horizontal = 24.dp, vertical = 20.dp)
                    ) {
                        Column {
                            Text(
                                text = "Find Your Next Home",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onBackground,
                                letterSpacing = (-1.2).sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Discover premium apartments across Kenya.",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            
                            Spacer(modifier = Modifier.height(28.dp))
                            
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(22.dp),
                                color = MaterialTheme.colorScheme.surface,
                                tonalElevation = 3.dp,
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                            ) {
                                Row(modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
                                    Spacer(modifier = Modifier.width(14.dp))
                                    BasicTextField(
                                        value = searchQuery,
                                        onValueChange = { searchQuery = it },
                                        modifier = Modifier.weight(1f).padding(vertical = 14.dp),
                                        singleLine = true,
                                        textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                        decorationBox = { innerTextField ->
                                            Box {
                                                if (searchQuery.isEmpty()) Text(text = "Search location, apartment...", fontSize = 15.sp, color = Color.Gray)
                                                innerTextField()
                                            }
                                        }
                                    )
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { searchQuery = "" }, modifier = Modifier.size(24.dp)) {
                                            Icon(Icons.Default.Close, null, tint = Color.Gray)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        Text(
                            text = "BROWSE COUNTIES",
                            modifier = Modifier.padding(horizontal = 24.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp)
                        ) {
                            items(counties) { county ->
                                CountyChip(text = county, selected = selectedCounty == county, onClick = { selectedCounty = county })
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (filteredApartments.isEmpty()) {
                        EmptyGuestSearch { searchQuery = ""; selectedCounty = "All" }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 4.dp, bottom = 120.dp)
                        ) {
                            itemsIndexed(filteredApartments, key = { _, it -> it.id }) { index, apartment ->
                                val itemVisible = remember { mutableStateOf(false) }
                                LaunchedEffect(Unit) {
                                    kotlinx.coroutines.delay(index * 100L)
                                    itemVisible.value = true
                                }
                                
                                AnimatedVisibility(
                                    visible = itemVisible.value,
                                    enter = fadeIn(tween(600)) + slideInHorizontally(tween(600)) { if (index % 2 == 0) -50 else 50 }
                                ) {
                                    val gradient = if (index % 3 == 0) PremiumGradient else if (index % 3 == 1) SkyGradient else RoseGradient
                                    GuestApartmentCard(
                                        apartment = apartment,
                                        gradient = gradient,
                                        onClick = { previewApartment = apartment; onApartmentSelected(apartment.id) },
                                        onCall = { onCallProperty(apartment.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    if (previewApartment != null) {
        ModalBottomSheet(
            onDismissRequest = { previewApartment = null },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            GuestApartmentPreview(
                apartment = previewApartment!!,
                onClose = { scope.launch { sheetState.hide() }.invokeOnCompletion { previewApartment = null } },
                onLogin = { scope.launch { sheetState.hide() }.invokeOnCompletion { previewApartment = null; onLogin() } },
                onCall = { onCallProperty(previewApartment!!.id) }
            )
        }
    }
}

@Composable
private fun GuestApartmentCard(
    apartment: TenantApartmentUIModel,
    gradient: List<Color> = PremiumGradient,
    onClick: () -> Unit,
    onCall: () -> Unit
) {
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
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
        shadowElevation = 2.dp
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Brush.linearGradient(gradient.map { it.copy(alpha = 0.9f) })),
                contentAlignment = Alignment.Center
            ) {
                // Background animation effect
                val infiniteTransition = rememberInfiniteTransition(label = "guest_card")
                val pulseScale by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(3000),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "pulse"
                )
                
                Icon(
                    Icons.Default.Apartment, 
                    null, 
                    modifier = Modifier.size(80.dp).scale(pulseScale), 
                    tint = Color.White.copy(alpha = 0.25f)
                )
                
                if (apartment.verified) {
                    Box(modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)) {
                        Surface(
                            color = Color.White.copy(alpha = 0.95f),
                            shape = RoundedCornerShape(12.dp),
                            shadowElevation = 4.dp
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Verified, null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("VERIFIED", fontSize = 9.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
            Column(modifier = Modifier.padding(24.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = apartment.name, fontSize = 21.sp, fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 6.dp)) {
                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "${apartment.location}, ${apartment.county}", fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text(text = "STARTING FROM", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Gray, letterSpacing = 1.sp)
                        Text(
                            text = "KES ${apartment.startingRent.toInt()}", 
                            fontSize = 20.sp, 
                            fontWeight = FontWeight.Black, 
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Button(
                        onClick = onCall,
                        shape = RoundedCornerShape(18.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text("EXPLORE", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    if (apartment.totalUnits > 0) {
                        Text(
                            text = "${apartment.totalUnits} Total Units",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    Surface(
                        color = if (apartment.availableUnits > 0) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (apartment.availableUnits > 0) "${apartment.availableUnits} VACANT" else "FULL",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = if (apartment.availableUnits > 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CountyChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        tonalElevation = if (selected) 4.dp else 1.dp,
        border = if (!selected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)) else null
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun EmptyGuestSearch(onClear: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.SearchOff, null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "No properties found", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = "Try adjusting your filters or location.", color = Color.Gray, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))
        TextButton(onClick = onClear) { Text(text = "Reset All Filters", fontWeight = FontWeight.Black) }
    }
}

@Composable
private fun GuestBottomBar(onLogin: () -> Unit, onRegister: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
        tonalElevation = 10.dp,
        shadowElevation = 20.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(modifier = Modifier.padding(20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = onLogin,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Login", fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onRegister,
                modifier = Modifier.weight(1.2f).height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Create Account", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun GuestApartmentPreview(apartment: TenantApartmentUIModel, onClose: () -> Unit, onLogin: () -> Unit, onCall: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(24.dp).padding(bottom = 32.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(50.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Apartment, null, tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = apartment.name, fontSize = 22.sp, fontWeight = FontWeight.Black)
                Text(text = apartment.location, color = Color.Gray, fontSize = 14.sp)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = apartment.description,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onLogin,
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text("Login to Join Apartment", fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        OutlinedButton(
            onClick = onCall,
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text("Contact Landlord", fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GuestHomeScreenPreview() {
    PropertyOSTheme {
        GuestHomeScreen(
            onLogin = {},
            onRegister = {}
        )
    }
}
