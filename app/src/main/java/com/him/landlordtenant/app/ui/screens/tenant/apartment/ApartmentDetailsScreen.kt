package com.him.landlordtenant.app.ui.screens.tenant.apartment

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.him.landlordtenant.app.ui.theme.PremiumGradient
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.TenantApartmentUIModel
import com.him.landlordtenant.app.ui.viewmodel.tenant.ApartmentDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApartmentDetailsScreen(
    apartmentId: String,
    onBack: () -> Unit,
    onViewHouses: (String) -> Unit,
    onJoinApartment: (String) -> Unit,
    onCallLandlord: (String) -> Unit = {},
    onSmsLandlord: (String) -> Unit = {},
    onWhatsAppLandlord: (String) -> Unit = {},
    onDirections: (Double, Double) -> Unit = { _, _ -> },
    onShareApartment: (String) -> Unit = {},
    viewModel: ApartmentDetailsViewModel = hiltViewModel()
) {
    val apartment by viewModel.apartment.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(apartmentId) {
        viewModel.loadApartment(apartmentId)
    }

    if (isLoading) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Loading...", fontWeight = FontWeight.Black) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    } else if (apartment == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Not Found", fontWeight = FontWeight.Black) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Apartment not found")
            }
        }
    } else {
        ApartmentDetailsContent(
            apartment = apartment!!,
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApartmentDetailsContent(
    apartment: TenantApartmentUIModel,
    onBack: () -> Unit,
    onViewHouses: (String) -> Unit,
    onJoinApartment: (String) -> Unit,
    onCallLandlord: (String) -> Unit = {},
    onSmsLandlord: (String) -> Unit = {},
    onWhatsAppLandlord: (String) -> Unit = {},
    onDirections: (Double, Double) -> Unit = { _, _ -> },
    onShareApartment: (String) -> Unit = {},
    title: String = apartment.name
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onShareApartment(apartment.id) }) {
                        Icon(Icons.Default.Share, "Share")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            ApartmentImageGallery(apartment.images)
            
            Column(modifier = Modifier.padding(24.dp)) {
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
                
                Spacer(modifier = Modifier.height(32.dp))
                
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
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text("About Property", fontWeight = FontWeight.Black, fontSize = 18.sp)
                Text(
                    text = apartment.description,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 12.dp)
                )
                
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
                
                Spacer(modifier = Modifier.height(40.dp))
                
                Button(
                    onClick = { onViewHouses(apartment.id) },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    shape = RoundedCornerShape(18.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text("Explore Available Houses", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                
                OutlinedButton(
                    onClick = { onJoinApartment(apartment.id) },
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp).height(60.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Application Request", fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
            .height(280.dp)
            .background(Color.LightGray)
    ) {
        if (images.isNotEmpty()) {
            AsyncImage(
                model = images.first(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize().background(Brush.linearGradient(PremiumGradient)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Apartment, null, modifier = Modifier.size(80.dp), tint = Color.White.copy(alpha = 0.2f))
            }
        }
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.3f))))
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
        ApartmentDetailsScreen(
            apartmentId = "1",
            onBack = {},
            onViewHouses = {},
            onJoinApartment = {}
        )
    }
}
