package com.him.landlordtenant.app.ui.screens.tenant.amenities

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

data class Amenity(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val status: String, // "Available", "Full", "Closed"
    val bookingRequired: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmenityBookingScreen(
    propertyId: String,
    onBack: () -> Unit
) {
    val amenities = listOf(
        Amenity("1", "Gym", Icons.Default.FitnessCenter, "Available"),
        Amenity("2", "Swimming Pool", Icons.Default.Pool, "Available"),
        Amenity("3", "Common Room", Icons.Default.MeetingRoom, "Available"),
        Amenity("4", "Roof Top", Icons.Default.VerticalAlignTop, "Available"),
        Amenity("5", "Laundry Room", Icons.Default.LocalLaundryService, "Closed")
    )

    var showBookingDialog by remember { mutableStateOf<Amenity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Amenities", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Schedule time for apartment facilities",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            items(amenities) { amenity ->
                AmenityCard(amenity) {
                    if (amenity.status == "Available") {
                        showBookingDialog = amenity
                    }
                }
            }
        }
    }

    if (showBookingDialog != null) {
        BookingDialog(
            amenity = showBookingDialog!!,
            onDismiss = { showBookingDialog = null },
            onConfirm = { /* TODO: Process booking */ showBookingDialog = null }
        )
    }
}

@Composable
private fun AmenityCard(amenity: Amenity, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(amenity.icon, null, tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(amenity.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(amenity.status, fontSize = 12.sp, color = if (amenity.status == "Available") Color(0xFF2E7D32) else Color.Red)
            }
            Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
        }
    }
}

@Composable
private fun BookingDialog(amenity: Amenity, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Book ${amenity.name}") },
        text = {
            Column {
                Text("Select preferred time slot for today.")
                Spacer(modifier = Modifier.height(16.dp))
                // Simple slot selection mock
                listOf("08:00 - 09:00", "10:00 - 11:00", "14:00 - 15:00", "17:00 - 18:00").forEach { slot ->
                    OutlinedButton(
                        onClick = { /* Select slot */ },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Text(slot)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) { Text("Confirm Booking") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AmenityBookingScreenPreview() {
    PropertyOSTheme {
        AmenityBookingScreen("1", {})
    }
}
