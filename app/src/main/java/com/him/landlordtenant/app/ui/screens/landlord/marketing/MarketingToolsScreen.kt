package com.him.landlordtenant.app.ui.screens.landlord.marketing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.GoldGradient
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketingToolsScreen(
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Marketing Hub", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text("Grow Your Presence", fontSize = 20.sp, fontWeight = FontWeight.Black)
            Text("Boost your property visibility and attract high-quality tenants faster.", color = Color.Gray, fontSize = 14.sp)
            
            MarketingActionCard(
                title = "Promote Listing",
                description = "Get featured on the home page and top search results.",
                icon = Icons.Default.RocketLaunch,
                gradient = GoldGradient
            )

            MarketingActionCard(
                title = "Virtual Tours",
                description = "Enable 360° tours for your premium apartments.",
                icon = Icons.Default.Vrpano,
                gradient = listOf(Color(0xFF2196F3), Color(0xFF00BCD4))
            )

            MarketingActionCard(
                title = "Tenant Insights",
                description = "View detailed reports on tenant search patterns.",
                icon = Icons.Default.Analytics,
                gradient = listOf(Color(0xFF9C27B0), Color(0xFFE91E63))
            )
        }
    }
}

@Composable
private fun MarketingActionCard(title: String, description: String, icon: androidx.compose.ui.graphics.vector.ImageVector, gradient: List<Color>) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { /* TODO */ },
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Brush.linearGradient(gradient), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = Color.White, modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(title, fontWeight = FontWeight.Black, fontSize = 17.sp)
                Text(description, fontSize = 12.sp, color = Color.Gray, lineHeight = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MarketingToolsScreenPreview() {
    PropertyOSTheme {
        MarketingToolsScreen({})
    }
}
