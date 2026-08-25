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
import coil.compose.AsyncImage
import com.him.landlordtenant.app.ui.theme.PremiumGradient
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.TenantApartmentUIModel
import com.him.landlordtenant.app.ui.screens.tenant.formatPaymentMoney
import com.him.landlordtenant.app.ui.screens.tenant.StatusBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApartmentSearchScreen(
    onBack: () -> Unit = {},
    onApartmentSelected: (String) -> Unit = {},
    onMapClicked: () -> Unit = {},
    viewModel: com.him.landlordtenant.app.ui.viewmodel.tenant.SearchApartmentViewModel = hiltViewModel()
) {
    var searchText by remember { mutableStateOf("") }
    var selectedCounty by remember { mutableStateOf("All counties") }
    var selectedHouseType by remember { mutableStateOf("All") }
    var onlyVacant by remember { mutableStateOf(true) }
    var showFilters by remember { mutableStateOf(false) }

    val apartments by viewModel.apartments.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(searchText) {
        viewModel.searchApartments(searchText)
    }

    val filteredApartments = apartments.filter { apartment ->
        val matchesSearch = searchText.isBlank() || apartment.name.contains(searchText, true) || apartment.location.contains(searchText, true)
        val matchesCounty = selectedCounty == "All counties" || apartment.county == selectedCounty
        val matchesHouseType = selectedHouseType == "All" || apartment.houseTypes.contains(selectedHouseType)
        val matchesVacancy = !onlyVacant || apartment.availableUnits > 0
        matchesSearch && matchesCounty && matchesHouseType && matchesVacancy
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Find Your Home", fontWeight = FontWeight.Black, fontSize = 20.sp, letterSpacing = (-0.5).sp)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
                },
                actions = {
                    IconButton(onClick = onMapClicked) { 
                        Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Map, null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
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
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(strokeWidth = 3.dp, modifier = Modifier.size(32.dp))
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
                        val visible = remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) {
                            kotlinx.coroutines.delay(index * 100L)
                            visible.value = true
                        }
                        
                        AnimatedVisibility(
                            visible = visible.value,
                            enter = fadeIn(tween(600)) + slideInVertically(tween(600)) { 20 }
                        ) {
                            ApartmentCard(apartment) { onApartmentSelected(apartment.id) }
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
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            leadingIcon = { Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.primary) },
            trailingIcon = { if (value.isNotEmpty()) IconButton(onClick = onClear) { Icon(Icons.Default.Close, null, tint = Color.Gray) } },
            placeholder = { Text("Search location, town or name...", fontSize = 14.sp, color = Color.Gray) },
            singleLine = true
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
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 1.dp
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
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

                if (apartment.verified) {
                    Box(modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)) {
                        StatusBadge(text = "Verified", background = Color.White.copy(alpha = 0.9f), foreground = MaterialTheme.colorScheme.primary)
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
    ModalBottomSheet(onDismissRequest = onDismiss, shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)) {
        Column(modifier = Modifier.padding(24.dp).padding(bottom = 32.dp).fillMaxWidth()) {
            Text("Search Filters", fontWeight = FontWeight.Black, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Vacant Units Only", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Only show apartments with available houses", fontSize = 12.sp, color = Color.Gray)
                }
                Switch(checked = vac, onCheckedChange = { vac = it })
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = { onApply(type, vac) },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Show Results", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ApartmentSearchScreenPreview() {
    PropertyOSTheme {
        ApartmentSearchScreen(
            onBack = {},
            onApartmentSelected = {}
        )
    }
}
