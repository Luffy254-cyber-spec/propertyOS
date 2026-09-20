package com.him.landlordtenant.app.ui.screens.landlord.apartment

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.TenantApartmentUIModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApartmentsScreen(
    apartments: List<TenantApartmentUIModel> = emptyList(),
    onBack: () -> Unit,
    onApartmentClick: (String) -> Unit,
    onApartmentPreview: (String) -> Unit = {},
    onAddApartment: () -> Unit,
    onRefresh: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        onRefresh()
    }
    
    MyApartmentsContent(
        apartments = apartments,
        onBack = onBack,
        onApartmentClick = onApartmentClick,
        onApartmentPreview = onApartmentPreview,
        onAddApartment = onAddApartment
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApartmentsContent(
    apartments: List<TenantApartmentUIModel>,
    onBack: () -> Unit,
    onApartmentClick: (String) -> Unit,
    onApartmentPreview: (String) -> Unit,
    onAddApartment: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    
    val filteredApartments = remember(apartments, searchQuery) {
        if (searchQuery.isBlank()) apartments
        else apartments.filter { it.name.contains(searchQuery, ignoreCase = true) || it.location.contains(searchQuery, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Properties", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddApartment,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, "Add Property")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                placeholder = { Text("Search your properties...", fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, null, modifier = Modifier.size(20.dp)) },
                trailingIcon = { 
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) { Icon(Icons.Default.Close, null, modifier = Modifier.size(20.dp)) }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                )
            )

            if (apartments.isEmpty()) {
                EmptyApartmentsState(onAddApartment)
            } else if (filteredApartments.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No properties match '$searchQuery'", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    itemsIndexed(filteredApartments, key = { _, it -> it.id }) { index, apartment ->
                        var isVisible by remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) {
                            kotlinx.coroutines.delay(index * 100L)
                            isVisible = true
                        }
                        AnimatedVisibility(
                            visible = isVisible,
                            enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn()
                        ) {
                            ApartmentItem(
                                apartment = apartment,
                                onClick = { onApartmentClick(apartment.id) },
                                onPreview = { onApartmentPreview(apartment.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ApartmentItem(apartment: TenantApartmentUIModel, onClick: () -> Unit, onPreview: () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }
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
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(160.dp).background(Color.LightGray)) {
                if (apartment.images.isNotEmpty()) {
                    coil.compose.AsyncImage(
                        model = apartment.images.first(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize().background(Brush.linearGradient(com.him.landlordtenant.app.ui.theme.PremiumGradient.map { it.copy(alpha = 0.8f) })), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Apartment, null, modifier = Modifier.size(60.dp), tint = Color.White.copy(alpha = 0.2f))
                    }
                }
                
                Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.TopEnd) {
                    Surface(color = Color.White.copy(alpha = 0.9f), shape = CircleShape, shadowElevation = 4.dp) {
                        IconButton(onClick = { showMenu = true }, modifier = Modifier.size(40.dp)) {
                            Icon(Icons.Default.MoreVert, null, tint = MaterialTheme.colorScheme.primary)
                        }
                        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                            DropdownMenuItem(text = { Text("Edit Details") }, onClick = { showMenu = false; onClick() }, leadingIcon = { Icon(Icons.Default.Edit, null) })
                            DropdownMenuItem(text = { Text("Public Preview") }, onClick = { showMenu = false; onPreview() }, leadingIcon = { Icon(Icons.Default.Visibility, null) })
                            Divider()
                            DropdownMenuItem(text = { Text("Delete Property") }, onClick = { showMenu = false }, colors = MenuDefaults.itemColors(textColor = Color.Red), leadingIcon = { Icon(Icons.Default.Delete, null, tint = Color.Red) })
                        }
                    }
                }
            }
            
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(), 
                    horizontalArrangement = Arrangement.SpaceBetween, 
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Text(apartment.name, fontSize = 20.sp, fontWeight = FontWeight.Black)
                        if (apartment.verified) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)) {
                                Icon(Icons.Default.Verified, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp).padding(2.dp))
                            }
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowForward, 
                        contentDescription = null, 
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(apartment.location, fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                    Column {
                        Text("OCCUPANCY", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Gray, letterSpacing = 1.sp)
                        Text("${apartment.totalUnits - apartment.availableUnits}/${apartment.totalUnits} Units", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("REVENUE", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Gray, letterSpacing = 1.sp)
                        Text(apartment.totalRevenue.ifEmpty { "KSh 0" }, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary, fontSize = 17.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyApartmentsState(onAdd: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Apartment, null, modifier = Modifier.size(80.dp), tint = Color.LightGray)
        Spacer(modifier = Modifier.height(16.dp))
        Text("No properties listed yet", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text("Start by adding your first apartment or building.", textAlign = TextAlign.Center, color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onAdd) {
            Text("Add My First Property")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyApartmentsScreenPreview() {
    PropertyOSTheme {
        MyApartmentsContent(
            apartments = listOf(
                TenantApartmentUIModel(id = "1", name = "Green Valley", location = "Kilimani", totalUnits = 24, totalRevenue = "KSh 840k", verified = true, county = "Nairobi", description = "", availableUnits = 5, startingRent = 15000.0, highestRent = 25000.0, rating = 4.5, distanceKm = 1.0, houseTypes = emptyList())
            ),
            onBack = {},
            onApartmentClick = {},
            onApartmentPreview = {},
            onAddApartment = {}
        )
    }
}
