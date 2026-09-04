package com.him.landlordtenant.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@Composable
fun RoleSelectionScreen(
    onBack: () -> Unit,
    onTenantSelected: () -> Unit,
    onLandlordSelected: () -> Unit,
    onGuestSelected: () -> Unit,
    userName: String = "",
    userEmail: String = "",
    isLoading: Boolean = false,
) {
    var selectedRole by rememberSaveable { mutableStateOf<String?>(null) }
    val displayName = userName.trim().takeIf { it.isNotEmpty() } ?: "there"

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxSize().navigationBarsPadding().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "propertyOS", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(34.dp))

            Box(modifier = Modifier.size(78.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f), CircleShape), contentAlignment = Alignment.Center) {
                Icon(imageVector = Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Welcome, $displayName!", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(text = "How will you use propertyOS? Choose the role that best describes you.", fontSize = 15.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(26.dp))

            RoleCard(
                selected = selectedRole == "tenant",
                title = "I'm a Tenant",
                subtitle = "Find a house, join an apartment, pay rent and manage your tenancy.",
                icon = Icons.Default.Person,
                features = listOf("Search available apartments", "Pay rent and bills", "Chat with landlord"),
                onClick = { selectedRole = "tenant" }
            )

            Spacer(modifier = Modifier.height(16.dp))

            RoleCard(
                selected = selectedRole == "landlord",
                title = "I'm a Landlord",
                subtitle = "Manage apartments, tenants, houses, and property operations.",
                icon = Icons.Default.Business,
                features = listOf("Manage properties & units", "Track rent and bills", "Manage maintenance"),
                onClick = { selectedRole = "landlord" }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { if (selectedRole == "tenant") onTenantSelected() else onLandlordSelected() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = selectedRole != null && !isLoading,
                shape = RoundedCornerShape(16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text(text = if (selectedRole == null) "Select Your Role" else "Continue as ${selectedRole?.replaceFirstChar { it.uppercase() }}", fontWeight = FontWeight.Bold)
                    if (selectedRole != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(onClick = onGuestSelected, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp)) {
                Text(text = "Continue as Guest", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun RoleCard(
    selected: Boolean,
    title: String,
    subtitle: String,
    icon: ImageVector,
    features: List<String>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),
        border = if (selected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(50.dp).background(if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.LightGray.copy(alpha = 0.2f), CircleShape), contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
                }
                RadioButton(selected = selected, onClick = onClick)
            }
            Spacer(modifier = Modifier.height(12.dp))
            features.forEach { feature ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                    Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = feature, fontSize = 12.sp, color = Color.DarkGray)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoleSelectionScreenPreview() {
    PropertyOSTheme {
        RoleSelectionScreen({}, {}, {}, {}, "User", "user@example.com", false)
    }
}
