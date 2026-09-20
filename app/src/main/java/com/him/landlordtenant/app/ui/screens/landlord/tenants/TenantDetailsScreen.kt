package com.him.landlordtenant.app.ui.screens.landlord.tenants

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.him.landlordtenant.app.ui.theme.PremiumGradient
import com.him.landlordtenant.app.ui.viewmodel.landlord.LandlordTenantDetailsUIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantDetailsScreen(
    tenant: LandlordTenantDetailsUIState,
    onBack: () -> Unit,
    onChat: (String) -> Unit = {},
    onCall: (String) -> Unit = {},
    onRemoveTenant: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resident Profile", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onChat(tenant.id) }) {
                        Icon(Icons.AutoMirrored.Filled.Chat, "Chat", tint = MaterialTheme.colorScheme.primary)
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
                .padding(24.dp)
        ) {
            // Header Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.size(120.dp)) {
                    if (!tenant.profilePhotoUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = tenant.profilePhotoUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .border(4.dp, MaterialTheme.colorScheme.primaryContainer, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Brush.linearGradient(PremiumGradient)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tenant.name.take(1).uppercase(),
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                    
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF4CAF50))
                            .border(2.dp, Color.White, CircleShape)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = tenant.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )
                
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = tenant.tenancyStatus.uppercase(),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF2E7D32)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Financial Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Current Balance", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text(
                            text = "KES ${String.format(java.util.Locale.getDefault(), "%,.0f", tenant.rentBalance)}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = if (tenant.rentBalance > 0) Color(0xFFF44336) else Color(0xFF2E7D32)
                        )
                    }
                    if (tenant.rentBalance > 0) {
                        Button(
                            onClick = { /* Send Reminder */ },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                        ) {
                            Text("Remind", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Details Grid
            Text("Tenancy Details", fontWeight = FontWeight.Black, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            
            DetailItem(Icons.Default.Apartment, "Property", tenant.apartmentName)
            DetailItem(Icons.Default.MeetingRoom, "Unit Number", tenant.unitName)
            DetailItem(Icons.Default.Payments, "Monthly Rent", "KES ${String.format(java.util.Locale.getDefault(), "%,.0f", tenant.monthlyRent)}")
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text("Contact Information", fontWeight = FontWeight.Black, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            
            DetailItem(
                icon = Icons.Default.Phone, 
                label = "Phone Number", 
                value = tenant.phoneNumber,
                trailingAction = {
                    IconButton(onClick = { onCall(tenant.phoneNumber) }) {
                        Icon(Icons.Default.Call, null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )
            
            DetailItem(
                icon = Icons.Default.Email, 
                label = "Email Address", 
                value = tenant.email.ifEmpty { "Not shared" }
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Danger Zone
            Text("Management Actions", fontWeight = FontWeight.Black, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedButton(
                onClick = onRemoveTenant,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red.copy(alpha = 0.3f))
            ) {
                Icon(Icons.Default.PersonRemove, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("End Tenancy Agreement", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun DetailItem(
    icon: ImageVector,
    label: String,
    value: String,
    trailingAction: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, modifier = Modifier.size(20.dp), tint = Color.Gray)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        trailingAction?.invoke()
    }
}
