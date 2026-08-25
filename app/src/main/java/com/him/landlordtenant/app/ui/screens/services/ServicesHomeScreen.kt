package com.him.landlordtenant.app.ui.screens.services

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesHomeScreen(
    onBack: () -> Unit,
    onServiceClick: (String) -> Unit
) {
    val services = listOf(
        ServiceItem("Plumbing", Icons.Default.Plumbing),
        ServiceItem("Electrical", Icons.Default.Bolt),
        ServiceItem("Cleaning", Icons.Default.CleaningServices),
        ServiceItem("Painting", Icons.Default.FormatPaint),
        ServiceItem("Security", Icons.Default.Security),
        ServiceItem("Internet", Icons.Default.Wifi),
        ServiceItem("Moving", Icons.Default.LocalShipping),
        ServiceItem("Repairs", Icons.Default.Build)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Professional Services") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text(
                text = "Find verified professionals for your needs.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(services) { service ->
                    ServiceGridCard(service) { onServiceClick(service.name) }
                }
            }
        }
    }
}

data class ServiceItem(val name: String, val icon: ImageVector)

@Composable
private fun ServiceGridCard(service: ServiceItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = service.icon,
                contentDescription = service.name,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = service.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ServicesHomeScreenPreview() {
    PropertyOSTheme {
        ServicesHomeScreen({}, {})
    }
}
