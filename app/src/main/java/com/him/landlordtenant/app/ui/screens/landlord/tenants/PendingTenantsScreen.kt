package com.him.landlordtenant.app.ui.screens.landlord.tenants

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.him.landlordtenant.app.interfaces.ApartmentApplicationData
import com.him.landlordtenant.app.ui.viewmodel.landlord.LandlordApplicationsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingTenantsScreen(
    onBack: () -> Unit,
    viewModel: LandlordApplicationsViewModel = hiltViewModel()
) {
    val applications by viewModel.applications.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    var selectedApplication by remember { mutableStateOf<ApartmentApplicationData?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadApplications()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Joining Requests", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp).padding(end = 16.dp), strokeWidth = 2.dp)
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            error?.let {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(12.dp), fontSize = 12.sp)
                }
            }

            if (applications.isEmpty() && !isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.PeopleOutline, null, modifier = Modifier.size(80.dp), tint = Color.LightGray)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("No pending requests", fontWeight = FontWeight.Bold, color = Color.Gray)
                        Text("When tenants apply to your apartment, they appear here.", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(applications) { app ->
                        DetailedApplicationItem(
                            app = app,
                            onAccept = { viewModel.processApplication(app.id, true) },
                            onDecline = { selectedApplication = app }
                        )
                    }
                }
            }
        }
    }

    if (selectedApplication != null) {
        var reason by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { selectedApplication = null },
            title = { Text("Decline Application") },
            text = {
                Column {
                    Text("Decline ${selectedApplication?.tenantName} for ${selectedApplication?.apartmentName}?", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = reason,
                        onValueChange = { reason = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Reason (e.g., Background check failed)") },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { 
                        viewModel.processApplication(selectedApplication!!.id, false, reason)
                        selectedApplication = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Decline Request")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedApplication = null }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun DetailedApplicationItem(
    app: ApartmentApplicationData,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header: Tenant Profile
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(56.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(app.tenantName, fontWeight = FontWeight.Black, fontSize = 18.sp)
                    Text(app.tenantEmail, fontSize = 12.sp, color = Color.Gray)
                    Text(app.tenantPhone, fontSize = 12.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.weight(1f))
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "NEW", 
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 10.sp, 
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(16.dp))

            // Application Details
            Text("Applying For:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Gray)
            Text(app.apartmentName, fontWeight = FontWeight.Black, fontSize = 16.sp)
            
            if (app.houseNumber.isNotEmpty() && app.houseNumber != "GENERAL") {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Home, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Unit Number: ", fontSize = 14.sp)
                    Text(app.houseNumber, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Apartment, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("General Property Access", fontSize = 14.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            
            // Documents Section
            if (app.nationalIdUrl.isNotEmpty()) {
                Text("Tenant Identity (National ID)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                AsyncImage(
                    model = app.nationalIdUrl,
                    contentDescription = "National ID",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Text("Digital Signature", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Gray)
            Surface(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    app.signature, 
                    modifier = Modifier.padding(12.dp),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, 
                    color = MaterialTheme.colorScheme.primary, 
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Action Buttons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onDecline, 
                    modifier = Modifier.weight(1f).height(48.dp), 
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                ) {
                    Text("Decline")
                }
                Button(
                    onClick = onAccept, 
                    modifier = Modifier.weight(1.2f).height(48.dp), 
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Approve & Join")
                }
            }
        }
    }
}
