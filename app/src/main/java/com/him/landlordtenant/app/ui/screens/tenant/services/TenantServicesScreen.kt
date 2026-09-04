package com.him.landlordtenant.app.ui.screens.tenant.services

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

data class ServiceCategory(
    val title: String,
    val icon: ImageVector,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantServicesScreen(
    onBack: () -> Unit,
    onCategoryClick: (String) -> Unit
) {
    val categories = listOf(
        ServiceCategory("Laundry", Icons.Default.LocalLaundryService, Color(0xFF2196F3)),
        ServiceCategory("Grocery", Icons.Default.LocalGroceryStore, Color(0xFF4CAF50)),
        ServiceCategory("Water", Icons.Default.WaterDrop, Color(0xFF03A9F4)),
        ServiceCategory("Gas", Icons.Default.LocalGasStation, Color(0xFFFF9800)),
        ServiceCategory("Hospital", Icons.Default.LocalHospital, Color(0xFFF44336)),
        ServiceCategory("Pharmacy", Icons.Default.LocalPharmacy, Color(0xFFE91E63)),
        ServiceCategory("Catering", Icons.Default.Restaurant, Color(0xFF795548)),
        ServiceCategory("Handyman", Icons.Default.Handyman, Color(0xFF607D8B))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Local Services", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text(
                "Find verified services near your apartment",
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                color = Color.Gray,
                fontSize = 14.sp
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(categories) { category ->
                    CategoryCard(category) { onCategoryClick(category.title) }
                }
            }
        }
    }
}

@Composable
private fun CategoryCard(category: ServiceCategory, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(category.color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(category.icon, null, tint = category.color, modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(category.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TenantServicesScreenPreview() {
    PropertyOSTheme {
        TenantServicesScreen(onBack = {}, onCategoryClick = {})
    }
}
