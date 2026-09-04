package com.him.landlordtenant.app.ui.screens.landlord.staff

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.viewmodel.landlord.LandlordStaffViewModel
import com.him.landlordtenant.app.interfaces.StaffData
import com.him.landlordtenant.app.interfaces.StaffStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandlordStaffScreen(
    onBack: () -> Unit,
    onAddStaff: () -> Unit = {},
    viewModel: LandlordStaffViewModel = hiltViewModel()
) {
    val staffList by viewModel.staffList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Staff & Professionals", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onAddStaff) {
                        Icon(Icons.Default.PersonAdd, "Add Staff")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddStaff, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Default.Add, "Add Staff", tint = Color.White)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text(
                "Manage your property management team and service providers.",
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                color = Color.Gray,
                fontSize = 14.sp
            )

            if (staffList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (isLoading) CircularProgressIndicator()
                    else Text("No staff members found", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(staffList) { staff ->
                        StaffCard(staff)
                    }
                }
            }
        }
    }
}

@Composable
private fun StaffCard(staff: StaffData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(staff.fullName.take(1), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(staff.fullName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    if (staff.status == StaffStatus.ACTIVE) {
                        Icon(Icons.Default.Verified, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp).padding(start = 4.dp))
                    }
                }
                Text("${staff.role.name} • ${staff.assignedPropertyCount} Properties", fontSize = 12.sp, color = Color.Gray)
            }
            IconButton(onClick = { /* Call */ }) {
                Icon(Icons.Default.Call, null, tint = Color(0xFF2E7D32))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LandlordStaffScreenPreview() {
    PropertyOSTheme {
        LandlordStaffScreen({})
    }
}
