package com.him.landlordtenant.app.ui.screens.tenant.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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

data class EmergencyContact(val name: String, val phone: String, val icon: ImageVector, val color: Color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyScreen(
    onBack: () -> Unit = {},
    onCallEmergency: (String) -> Unit = {},
) {
    val emergencyContacts = listOf(
        EmergencyContact("Police", "999", Icons.Default.Shield, Color(0xFFD32F2F)),
        EmergencyContact("Ambulance", "911", Icons.Default.MedicalServices, Color(0xFF2E7D32)),
        EmergencyContact("Fire Dept", "112", Icons.Default.FireTruck, Color(0xFFF57C00)),
        EmergencyContact("Red Cross", "0700 395 395", Icons.Default.HealthAndSafety, Color(0xFFC62828))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emergency Contacts") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Warning, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("In case of immediate danger, please call the numbers below.", fontSize = 14.sp, color = MaterialTheme.colorScheme.onErrorContainer, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(emergencyContacts) { contact ->
                    EmergencyContactItem(contact) { onCallEmergency(contact.phone) }
                }
            }
        }
    }
}

@Composable
private fun EmergencyContactItem(contact: EmergencyContact, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(50.dp).background(contact.color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(contact.icon, null, tint = contact.color)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(contact.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(contact.phone, color = Color.Gray)
            }
            
            IconButton(onClick = onClick) {
                Icon(Icons.Default.Call, null, tint = contact.color)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmergencyScreenPreview() {
    PropertyOSTheme {
        EmergencyScreen()
    }
}
