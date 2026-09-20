package com.him.landlordtenant.app.ui.screens.tenant.house

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.TenantHouseUIModel
import com.him.landlordtenant.app.ui.screens.tenant.HouseType
import com.him.landlordtenant.app.data.model.HouseStatus
import com.him.landlordtenant.app.ui.screens.tenant.HouseCondition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseDetailsScreen(
    house: TenantHouseUIModel,
    onBack: () -> Unit,
    onJoinHouse: (String, String, String, String) -> Unit,
    onCallLandlord: (String) -> Unit = {},
    onMessageLandlord: (String) -> Unit = {},
    onOpenMap: (Double, Double) -> Unit = { _, _ -> },
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("House ${house.houseNumber}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share */ }) {
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
            HouseImageGallery(house.imageUrls)
            
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("House ${house.houseNumber}", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    StatusBadge(status = house.status)
                }
                
                Text("${house.houseType.name.replace("_", " ")} • Floor ${house.floorNumber}", fontSize = 15.sp, color = Color.Gray)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    StatItem(Icons.Default.KingBed, "${house.bedrooms} BR", "Bedrooms")
                    StatItem(Icons.Default.Bathtub, "${house.bathrooms} BA", "Bathrooms")
                    StatItem(Icons.Default.Straighten, "650 sqft", "Area")
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text("Description", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(house.description.ifEmpty { "No description provided." }, fontSize = 15.sp, color = Color.DarkGray, modifier = Modifier.padding(top = 8.dp))
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text("Features", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Column(modifier = Modifier.padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    FeatureItem(Icons.Default.FlashOn, "Electricity", house.hasElectricity)
                    FeatureItem(Icons.Default.WaterDrop, "Water Supply", house.hasWater)
                    FeatureItem(Icons.Default.LocalParking, "Parking", house.hasParking)
                    FeatureItem(Icons.Default.Wifi, "Internet", house.hasInternet)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text("Pricing", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                PriceRow("Monthly Rent", "KES ${house.monthlyRent}")
                PriceRow("Security Deposit", "KES ${house.deposit}")
                PriceRow("Water Bill (Avg)", "KES ${house.waterBill}")
                PriceRow("Total Move-in", "KES ${house.initialPayment}", isTotal = true)
                
                Spacer(modifier = Modifier.height(32.dp))
                
                if (house.status == HouseStatus.VACANT) {
                    Button(onClick = { onJoinHouse(house.houseId, house.houseNumber, house.houseId, house.houseNumber) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                        Text("Apply to Join")
                    }
                } else {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { onCallLandlord(house.landlordPhone) }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                            Icon(Icons.Default.Call, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Call")
                        }
                        OutlinedButton(onClick = { onMessageLandlord(house.landlordPhone) }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                            Icon(Icons.AutoMirrored.Filled.Chat, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Message")
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text("Location", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                HouseMapPreview(house.latitude, house.longitude, house.apartmentAddress) { onOpenMap(house.latitude, house.longitude) }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun HouseImageGallery(images: List<String>) {
    // Standardize images for preview
    val hasImages = images.isNotEmpty()
    Box(modifier = Modifier.fillMaxWidth().height(230.dp).background(Color.LightGray), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Image, null, modifier = Modifier.size(48.dp), tint = Color.Gray)
            if (!hasImages) Text("No photos available", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
private fun StatItem(icon: ImageVector, value: String, label: String) {
    Card(modifier = Modifier.width(90.dp)) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(label, fontSize = 10.sp, color = Color.Gray)
        }
    }
}

@Composable
private fun FeatureItem(icon: ImageVector, label: String, available: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, modifier = Modifier.size(18.dp), tint = if (available) MaterialTheme.colorScheme.primary else Color.Gray)
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, fontSize = 14.sp, color = if (available) Color.Black else Color.Gray)
        Spacer(modifier = Modifier.weight(1f))
        if (available) {
            Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp), tint = Color(0xFF2E7D32))
        } else {
            Icon(Icons.Default.Close, null, modifier = Modifier.size(16.dp), tint = Color(0xFFD32F2F))
        }
    }
}

@Composable
private fun PriceRow(label: String, value: String, isTotal: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal, fontSize = if (isTotal) 16.sp else 14.sp)
        Text(value, fontWeight = if (isTotal) FontWeight.Bold else FontWeight.SemiBold, fontSize = if (isTotal) 16.sp else 14.sp, color = if (isTotal) MaterialTheme.colorScheme.primary else Color.Black)
    }
}

@Composable
private fun HouseMapPreview(lat: Double, lng: Double, address: String, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(top = 8.dp).clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(120.dp).background(Color.LightGray.copy(alpha = 0.5f)), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Map, null, tint = Color.Gray)
                    Text("Tap to open in Maps (Lat: $lat, Lng: $lng)", fontSize = 8.sp, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(address, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
private fun StatusBadge(status: HouseStatus) {
    val color = when (status) {
        HouseStatus.VACANT -> Color(0xFF2E7D32)
        HouseStatus.OCCUPIED -> Color(0xFFD32F2F)
        HouseStatus.NOT_READY -> Color(0xFFF57C00)
        HouseStatus.UNDER_MAINTENANCE -> Color(0xFF757575)
        HouseStatus.RESERVED -> Color(0xFF1976D2)
        HouseStatus.PENDING_MOVE_IN -> Color(0xFF0097A7)
        HouseStatus.BLOCKED -> Color(0xFF616161)
        HouseStatus.ARCHIVED -> Color(0xFF424242)
    }
    Surface(shape = RoundedCornerShape(50), color = color.copy(alpha = 0.1f)) {
        Text(text = status.name, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Preview(showBackground = true)
@Composable
fun HouseDetailsScreenPreview() {
    PropertyOSTheme {
        HouseDetailsScreen(
            house = TenantHouseUIModel(
                houseId = "1",
                houseNumber = "G1",
                floorNumber = 0,
                houseType = HouseType.TWO_BEDROOM,
                status = HouseStatus.VACANT,
                condition = HouseCondition.EXCELLENT,
                monthlyRent = 15000.0,
                deposit = 15000.0,
                waterBill = 650.0,
                description = "Modern 2 bedroom house with master ensuite, solar water heating and high speed internet.",
                apartmentAddress = "Kilimani, Nairobi",
                hasParking = true,
                hasInternet = true,
                latitude = -1.286389,
                longitude = 36.817223
            ),
            onBack = {},
            onJoinHouse = { _, _, _, _ -> }
        )
    }
}
