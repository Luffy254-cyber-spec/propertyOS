package com.him.landlordtenant.app.ui.screens.tenant.apartment

import androidx.compose.animation.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.him.landlordtenant.app.ui.theme.PremiumGradient
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.TenantApartmentUIModel
import com.him.landlordtenant.app.ui.screens.tenant.StaggeredFadeIn
import com.him.landlordtenant.app.ui.viewmodel.tenant.ApartmentDetailsViewModel

@Composable
fun ApartmentDetailsScreen(
    apartmentId: String,
    onBack: () -> Unit,
    onViewHouses: (String) -> Unit,
    onJoinApartment: (String, String, String, String) -> Unit,
    onCallLandlord: (String) -> Unit = {},
    onSmsLandlord: (String) -> Unit = {},
    onWhatsAppLandlord: (String) -> Unit = {},
    onDirections: (Double, Double) -> Unit = { _, _ -> },
    onShareApartment: (String) -> Unit = {},
    viewModel: ApartmentDetailsViewModel = hiltViewModel()
) {
    val apartment by viewModel.apartment.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isAlreadyJoined by viewModel.isAlreadyJoined.collectAsState()

    LaunchedEffect(apartmentId) {
        viewModel.loadApartment(apartmentId)
    }

    ApartmentDetailsContent(
        apartment = apartment,
        isLoading = isLoading,
        isAlreadyJoined = isAlreadyJoined,
        onBack = onBack,
        onViewHouses = onViewHouses,
        onJoinApartment = onJoinApartment,
        onCallLandlord = onCallLandlord,
        onSmsLandlord = onSmsLandlord,
        onWhatsAppLandlord = onWhatsAppLandlord,
        onDirections = onDirections,
        onShareApartment = onShareApartment
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ApartmentDetailsContent(
    apartment: TenantApartmentUIModel?,
    isLoading: Boolean,
    isAlreadyJoined: Boolean = false,
    onBack: () -> Unit,
    onViewHouses: (String) -> Unit,
    onJoinApartment: (String, String, String, String) -> Unit,
    onCallLandlord: (String) -> Unit = {},
    onSmsLandlord: (String) -> Unit = {},
    onWhatsAppLandlord: (String) -> Unit = {},
    onDirections: (Double, Double) -> Unit = { _, _ -> },
    onShareApartment: (String) -> Unit = {},
    title: String = apartment?.name ?: "Apartment"
) {
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (apartment != null) {
                        IconButton(onClick = { onShareApartment(apartment.id) }) {
                            Icon(Icons.Default.Share, "Share")
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (apartment == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Apartment not found")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                ApartmentImageGallery(apartment.images)
                
                Column(modifier = Modifier.padding(24.dp)) {
                    StaggeredFadeIn(delay = 100) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(apartment.name, fontSize = 26.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onBackground)
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("${apartment.location}, ${apartment.county}", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
                                }
                            }
                            if (apartment.verified) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Verified, "Verified", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    StaggeredFadeIn(delay = 200) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ) {
                            Row(
                                modifier = Modifier.padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                StatItem(Icons.Default.Home, apartment.totalUnits.toString(), "Units")
                                VerticalDivider(modifier = Modifier.height(40.dp))
                                StatItem(Icons.Default.MeetingRoom, apartment.availableUnits.toString(), "Vacant")
                                VerticalDivider(modifier = Modifier.height(40.dp))
                                StatItem(Icons.Default.Star, apartment.rating.toString(), "Rating")
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    StaggeredFadeIn(delay = 300) {
                        Text("About Property", fontWeight = FontWeight.Black, fontSize = 18.sp)
                        Text(
                            text = apartment.description,
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                    
                    if (apartment.amenities.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(32.dp))
                        StaggeredFadeIn(delay = 400) {
                            Column {
                                Text("Amenities", fontWeight = FontWeight.Black, fontSize = 18.sp)
                                Spacer(modifier = Modifier.height(12.dp))
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    apartment.amenities.forEach { amenity ->
                                        Surface(
                                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                            shape = RoundedCornerShape(12.dp),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                                        ) {
                                            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    when(amenity.uppercase()) {
                                                        "WIFI" -> Icons.Default.Wifi
                                                        "PARKING" -> Icons.Default.LocalParking
                                                        "SECURITY" -> Icons.Default.Security
                                                        "CCTV" -> Icons.Default.Visibility
                                                        "GYM" -> Icons.Default.FitnessCenter
                                                        "POOL" -> Icons.Default.Pool
                                                        else -> Icons.Default.Done
                                                    },
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp),
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(amenity, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("Rental Pricing", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                            Text(
                                text = "KES ${apartment.startingRent} - KES ${apartment.highestRent} / month",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Text("Location", fontWeight = FontWeight.Black, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        val propertyLocation = LatLng(apartment.latitude, apartment.longitude)
                        val cameraPositionState = rememberCameraPositionState {
                            position = CameraPosition.fromLatLngZoom(propertyLocation, 15f)
                        }
                        
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState,
                            uiSettings = MapUiSettings(zoomControlsEnabled = false, scrollGesturesEnabled = false, zoomGesturesEnabled = false, tiltGesturesEnabled = false, rotationGesturesEnabled = false)
                        ) {
                            Marker(
                                state = MarkerState(position = propertyLocation),
                                title = apartment.name,
                                snippet = apartment.location
                            )
                        }
                    }
                    
                    Button(
                        onClick = { onDirections(apartment.latitude, apartment.longitude) },
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Icon(Icons.Default.Navigation, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Get Directions", fontWeight = FontWeight.Bold)
                    }
                    
                    Spacer(modifier = Modifier.height(40.dp))
                    
                    StaggeredFadeIn(delay = 600) {
                        Button(
                            onClick = { onViewHouses(apartment.id) },
                            modifier = Modifier.fillMaxWidth().height(60.dp),
                            shape = RoundedCornerShape(20.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                        ) {
                            Text("Explore Available Houses 🏘️", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                    
                    StaggeredFadeIn(delay = 700) {
                        Column {
                            if (isAlreadyJoined) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f)),
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                                ) {
                                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("You are already joined to another apartment. Please vacate your current residence before applying elsewhere.", fontSize = 11.sp, color = MaterialTheme.colorScheme.error, lineHeight = 16.sp)
                                    }
                                }
                            }
                            
                            OutlinedButton(
                                onClick = { onJoinApartment(apartment.id, apartment.name, "GENERAL", "GENERAL") },
                                modifier = Modifier.fillMaxWidth().height(60.dp),
                                shape = RoundedCornerShape(20.dp),
                                enabled = !isAlreadyJoined,
                                border = androidx.compose.foundation.BorderStroke(2.dp, if (isAlreadyJoined) Color.LightGray else MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Submit Joining Request 📄", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}


@Composable
private fun ApartmentImageGallery(images: List<String>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .background(Color.LightGray)
    ) {
        if (images.isNotEmpty()) {
            AsyncImage(
                model = images.first(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                // Premium effects
                alignment = Alignment.Center
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize().background(Brush.linearGradient(PremiumGradient)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Apartment, null, modifier = Modifier.size(80.dp), tint = Color.White.copy(alpha = 0.2f))
            }
        }
        
        // Gradient overlay for better text legibility and premium feel
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))))
        )
    }
}

@Composable
private fun StatItem(icon: ImageVector, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontWeight = FontWeight.Black, fontSize = 17.sp)
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun ApartmentDetailsScreenPreview() {
    PropertyOSTheme {
        ApartmentDetailsContent(
            apartment = null,
            isLoading = false,
            onBack = {},
            onViewHouses = { _ -> },
            onJoinApartment = { _, _, _, _ -> }
        )
    }
}
