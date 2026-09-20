package com.him.landlordtenant.app.ui.screens.landlord.viewings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Visibility
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
import com.him.landlordtenant.app.ui.viewmodel.landlord.ViewingViewModel
import com.him.landlordtenant.app.interfaces.ViewingSummaryData
import com.him.landlordtenant.app.interfaces.ViewingStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewingRequestsScreen(
    onBack: () -> Unit,
    viewModel: ViewingViewModel = hiltViewModel()
) {
    val viewings by viewModel.viewings.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    ViewingRequestsContent(
        viewings = viewings,
        isLoading = isLoading,
        onBack = onBack,
        onApprove = { viewModel.approveViewing(it) },
        onReject = { viewModel.rejectViewing(it, "Declined by landlord") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewingRequestsContent(
    viewings: List<ViewingSummaryData>,
    isLoading: Boolean,
    onBack: () -> Unit,
    onApprove: (String) -> Unit,
    onReject: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Viewing Requests", fontWeight = FontWeight.Black) },
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
                "Manage appointments for potential tenants to view your properties.",
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                color = Color.Gray,
                fontSize = 14.sp
            )

            if (viewings.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (isLoading) CircularProgressIndicator()
                    else Text("No viewing requests yet", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(viewings) { request ->
                        ViewingCard(
                            request = request,
                            onApprove = { onApprove(request.id) },
                            onReject = { onReject(request.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ViewingCard(
    request: ViewingSummaryData,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(44.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Visibility, null, tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(request.requesterName ?: "Unknown Applicant", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(request.propertyName ?: "Property", fontSize = 12.sp, color = Color.Gray)
                }
                StatusBadge(request.status.name)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarMonth, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text("${request.scheduledDate ?: "TBD"} at ${request.scheduledTime ?: "TBD"}", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (request.status == ViewingStatus.REQUESTED) {
                    Button(onClick = onApprove, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                        Text("Approve")
                    }
                    OutlinedButton(onClick = onReject, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                        Text("Decline")
                    }
                } else {
                    OutlinedButton(onClick = { /* Contact */ }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                        Icon(Icons.Default.Call, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Contact Requester")
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    val color = when (status) {
        "REQUESTED" -> Color(0xFFF57C00)
        "APPROVED" -> Color(0xFF1976D2)
        "COMPLETED" -> Color(0xFF2E7D32)
        "CANCELLED", "REJECTED" -> Color(0xFFD32F2F)
        else -> Color.Gray
    }
    Surface(shape = RoundedCornerShape(8.dp), color = color.copy(alpha = 0.1f)) {
        Text(text = status, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Preview(showBackground = true)
@Composable
fun ViewingRequestsScreenPreview() {
    PropertyOSTheme {
        ViewingRequestsScreen({})
    }
}
