package com.him.landlordtenant.app.ui.screens.tenant.search

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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.screens.tenant.LiquidBackground
import coil.compose.AsyncImage
import com.him.landlordtenant.app.ui.theme.PremiumGradient
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.TenantApartmentUIModel
import com.him.landlordtenant.app.ui.screens.tenant.formatPaymentMoney
import com.him.landlordtenant.app.ui.viewmodel.tenant.SearchApartmentViewModel

@Composable
fun ApartmentSearchScreen(
    onBack: () -> Unit,
    onApartmentSelected: (String) -> Unit = {},
    onMapClicked: () -> Unit = {},
    viewModel: SearchApartmentViewModel = hiltViewModel()
) {
    val apartments by viewModel.apartments.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    ApartmentSearchContent(
        apartments = apartments,
        isLoading = isLoading,
        onBack = onBack,
        onApartmentSelected = onApartmentSelected,
        onMapClicked = onMapClicked,
        onSearch = { viewModel.searchApartments(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ApartmentSearchContent(
    apartments: List<TenantApartmentUIModel>,
    isLoading: Boolean,
    onBack: () -> Unit,
    onApartmentSelected: (String) -> Unit,
    onMapClicked: () -> Unit,
    onSearch: (String) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedCounty by remember { mutableStateOf("All counties") }
    var selectedHouseType by remember { mutableStateOf("All") }
    var onlyVacant by remember { mutableStateOf(false) }
    var showFilters by remember { mutableStateOf(false) }

    LaunchedEffect(searchText) {
        onSearch(searchText)
    }

    val filteredApartments = apartments.filter { apartment ->
        val matchesSearch = searchText.isBlank() || 
                apartment.name.contains(searchText, true) || 
                apartment.location.contains(searchText, true) ||
                apartment.county.contains(searchText, true)
        
        val matchesCounty = selectedCounty == "All counties" || 
                apartment.county.trim().equals(selectedCounty.trim(), ignoreCase = true)
        
        val matchesHouseType = selectedHouseType == "All" || 
                apartment.houseTypes.any { it.trim().equals(selectedHouseType.trim(), ignoreCase = true) }
        
        val matchesVacancy = !onlyVacant || apartment.availableUnits > 0
        
        matchesSearch && matchesCounty && matchesHouseType && matchesVacancy
    }

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Find Your Home", fontWeight = FontWeight.Black, fontSize = 20.sp, letterSpacing = (-0.5).sp)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) { 
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") 
                    }
                },
                actions = {
                    IconButton(onClick = onMapClicked) { 
                        Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Map, null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LiquidBackground(MaterialTheme.colorScheme.primary)
            
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                // Animated Search Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f), MaterialTheme.colorScheme.background)))
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Column {
                        SearchField(searchText, onValueChange = { searchText = it }) { searchText = "" }
                        Spacer(modifier = Modifier.height(16.dp))
                        CountySelector(selectedCounty) { selectedCounty = it }
                    }
                }

                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 32.dp)
                ) {
                    if (isLoading) {
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    strokeWidth = 4.dp,
                                    modifier = Modifier.size(48.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "Finding your perfect home...",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    item { 
                        FilterControls(selectedHouseType, onlyVacant, { selectedHouseType = it }, { onlyVacant = it }, { showFilters = true }) 
                    }
                    
                    item { 
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = if (filteredApartments.isEmpty()) "No matches found" else "${filteredApartments.size} Apartments Found",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Gray,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                    
                    if (filteredApartments.isEmpty()) {
                        item { EmptySearchResult { searchText = ""; selectedCounty = "All counties"; selectedHouseType = "All"; onlyVacant = true } }
                    } else {
                        itemsIndexed(filteredApartments, key = { _, it -> it.id }) { index, apartment ->
                            var isVisible by remember { mutableStateOf(false) }
                            LaunchedEffect(Unit) {
                                kotlinx.coroutines.delay(index * 120L)
                                isVisible = true
                            }
                            
                            AnimatedVisibility(
                                visible = isVisible,
                                enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn(animationSpec = tween(500))
                            ) {
                                ApartmentCard(apartment) { onApartmentSelected(apartment.id) }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showFilters) {
        ApartmentFilterSheet(selectedHouseType, onlyVacant, { showFilters = false }, { type, vac -> selectedHouseType = type; onlyVacant = vac; showFilters = false })
    }
}

@Composable
private fun SearchField(value: String, onValueChange: (String) -> Unit, onClear: () -> Unit) {
    var isFocused by remember { mutableStateOf(false) }
    val shadowElevation by animateDpAsState(if (isFocused) 8.dp else 2.dp, label = "elevation")
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(shadowElevation, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = if (isFocused) 0.3f else 0.05f))
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            leadingIcon = { 
                Icon(
                    Icons.Default.Search, 
                    null, 
                    tint = if (isFocused) MaterialTheme.colorScheme.primary else Color.Gray,
                    modifier = Modifier.size(24.dp)
                ) 
            },
            trailingIcon = { 
                if (value.isNotEmpty()) {
                    IconButton(onClick = onClear) { 
                        Icon(Icons.Default.Close, null, tint = Color.Gray, modifier = Modifier.size(20.dp)) 
                    } 
                } 
            },
            placeholder = { Text("Search by name, location or town...", fontSize = 15.sp, color = Color.Gray) },
            singleLine = true,
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
private fun CountySelector(selected: String, onSelected: (String) -> Unit) {
    val counties = listOf("All counties", "Nairobi", "Kiambu", "Mombasa", "Kisumu", "Nakuru")
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(counties) { county ->
            val isSelected = selected == county
            Surface(
                modifier = Modifier.clickable { onSelected(county) },
                shape = RoundedCornerShape(14.dp),
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                tonalElevation = if (isSelected) 4.dp else 1.dp,
                border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)) else null
            ) {
                Text(
                    text = county,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun FilterControls(houseType: String, vacantOnly: Boolean, onTypeChange: (String) -> Unit, onVacantChange: (Boolean) -> Unit, onMore: () -> Unit) {
    val types = listOf("All", "Bedsitter", "1 Bedroom", "2 Bedroom")
    Column {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Property Type", fontSize = 13.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
            IconButton(onClick = onMore) { Icon(Icons.Default.Tune, null, tint = MaterialTheme.colorScheme.primary) }
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(vertical = 4.dp)) {
            items(types) { type ->
                val isSelected = houseType == type
                Surface(
                    modifier = Modifier.clickable { onTypeChange(type) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ) {
                    Text(
                        text = type,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ApartmentCard(apartment: TenantApartmentUIModel, onClick: () -> Unit) {
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
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 1.dp
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                if (apartment.images.isNotEmpty()) {
                    AsyncImage(
                        model = apartment.images.first(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize().background(Brush.linearGradient(PremiumGradient.map { it.copy(alpha = 0.8f) })),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Apartment, null, modifier = Modifier.size(60.dp), tint = Color.White.copy(alpha = 0.2f))
                    }
                }
                
                // Animated "New" Badge
                val infiniteTransition = rememberInfiniteTransition(label = "badge")
                val badgeAlpha by infiniteTransition.animateFloat(
                    initialValue = 0.6f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(800),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "alpha"
                )

                if (apartment.verified) {
                    Box(modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)) {
                        Surface(
                            color = Color.White.copy(alpha = badgeAlpha),
                            shape = RoundedCornerShape(12.dp)
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
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(apartment.name, fontWeight = FontWeight.Black, fontSize = 18.sp, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface)
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f), RoundedCornerShape(8.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                        Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(apartment.rating.toString(), fontSize = 12.sp, fontWeight = FontWeight.Black)
                    }
                }
                Text("${apartment.location}, ${apartment.county}", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("MONTHLY", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Gray, letterSpacing = 1.sp)
                        Text(
                            text = "KES ${formatPaymentMoney(apartment.startingRent)}",
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
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
private fun EmptySearchResult(onClear: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.SearchOff, null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
        Spacer(modifier = Modifier.height(16.dp))
        Text("No results found", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text("Try adjusting your filters or location.", color = Color.Gray, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))
        TextButton(onClick = onClear) { Text("Clear All Filters", fontWeight = FontWeight.Black) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ApartmentFilterSheet(currentType: String, currentVac: Boolean, onDismiss: () -> Unit, onApply: (String, Boolean) -> Unit) {
    var type by remember { mutableStateOf(currentType) }
    var vac by remember { mutableStateOf(currentVac) }
    
    ModalBottomSheet(
        onDismissRequest = onDismiss, 
        shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(28.dp).padding(bottom = 32.dp).fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(48.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Tune, null, tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("Search Filters", fontWeight = FontWeight.Black, fontSize = 24.sp)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
            ) {
                Row(modifier = Modifier.padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Vacant Units Only", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        Text("Only show apartments with currently available houses.", fontSize = 12.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = vac, 
                        onCheckedChange = { vac = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Button(
                onClick = { onApply(type, vac) },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text("Show Results", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ApartmentSearchScreenPreview() {
    PropertyOSTheme {
        ApartmentSearchContent(
            apartments = emptyList(),
            isLoading = false,
            onBack = {},
            onApartmentSelected = {},
            onMapClicked = {},
            onSearch = {}
        )
    }
}
