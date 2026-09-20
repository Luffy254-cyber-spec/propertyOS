package com.him.landlordtenant.app.ui.screens.landlord.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.viewmodel.landlord.PropertyAnalyticsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyAnalyticsScreen(
    propertyId: String,
    onBack: () -> Unit,
    viewModel: PropertyAnalyticsViewModel = hiltViewModel()
) {
    val analytics by viewModel.analytics.collectAsState()
    val property by viewModel.property.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(propertyId) {
        viewModel.loadData(propertyId)
    }

    PropertyAnalyticsContent(
        property = property,
        analytics = analytics,
        isLoading = isLoading,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyAnalyticsContent(
    property: com.him.landlordtenant.app.interfaces.PropertyDetailsData?,
    analytics: com.him.landlordtenant.app.interfaces.PropertyAnalyticsData?,
    isLoading: Boolean,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Property Insights", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading && property == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(scrollState)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                AnalyticsHeader(property?.name ?: "Property")
                
                RevenueChart()
                
                OccupancyStats(property?.totalUnits ?: 0, property?.occupiedUnits ?: 0)
                
                SectionTitle("Marketplace Performance")
                
                MarketplaceStats(
                    views = analytics?.totalViews ?: 0,
                    requests = analytics?.totalViewingRequests ?: 0,
                    conversion = analytics?.conversionRate ?: 0.0
                )
            }
        }
    }
}

@Composable
private fun AnalyticsHeader(name: String) {
    Column {
        Text("Performance Overview", fontSize = 20.sp, fontWeight = FontWeight.Black)
        Text("Detailed data for $name", color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
private fun RevenueChart() {
    Card(
        modifier = Modifier.fillMaxWidth().height(250.dp),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Monthly Revenue", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth().height(150.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                val values = listOf(0.4f, 0.6f, 0.8f, 0.7f, 0.9f, 1.0f)
                values.forEach { scale ->
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .fillMaxHeight(scale)
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("Mar", "Apr", "May", "Jun", "Jul", "Aug").forEach {
                    Text(it, fontSize = 10.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
private fun OccupancyStats(total: Int, occupied: Int) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        StatCard(Modifier.weight(1f), "Total Units", total.toString(), Icons.Default.Apartment, Color(0xFF2196F3))
        StatCard(Modifier.weight(1f), "Occupied", occupied.toString(), Icons.Default.Person, Color(0xFF4CAF50))
    }
}

@Composable
private fun MarketplaceStats(views: Long, requests: Long, conversion: Double) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp)) {
        Column(modifier = Modifier.padding(20.dp)) {
            MarketplaceRow("Total Listing Views", String.format("%,d", views), Icons.Default.Visibility)
            Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.2f))
            MarketplaceRow("Viewing Requests", String.format("%,d", requests), Icons.Default.Event)
            Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.2f))
            MarketplaceRow("Conversion Rate", String.format("%.1f%%", conversion), Icons.Default.TrendingUp)
        }
    }
}

@Composable
private fun MarketplaceRow(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, modifier = Modifier.weight(1f), fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Black, fontSize = 16.sp)
    }
}

@Composable
private fun StatCard(modifier: Modifier, label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = color.copy(alpha = 0.05f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(label, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Black, color = color)
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(title, fontWeight = FontWeight.Black, fontSize = 18.sp)
}

@Preview(showBackground = true)
@Composable
fun PropertyAnalyticsScreenPreview() {
    PropertyOSTheme {
        PropertyAnalyticsContent(
            property = com.him.landlordtenant.app.interfaces.PropertyDetailsData(
                id = "1",
                name = "Sample Property",
                totalUnits = 20,
                occupiedUnits = 15
            ),
            analytics = com.him.landlordtenant.app.interfaces.PropertyAnalyticsData(
                totalViews = 1200,
                uniqueViews = 800,
                totalShares = 50,
                totalViewingRequests = 10,
                favoriteCount = 30,
                conversionRate = 5.5
            ),
            isLoading = false,
            onBack = {}
        )
    }
}
