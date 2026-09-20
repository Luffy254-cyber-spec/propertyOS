package com.him.landlordtenant.app.ui.screens.landlord.billing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import com.him.landlordtenant.app.ui.screens.tenant.LandlordBillingUIState

@Composable
fun BillingDashboardScreen(
    onBack: () -> Unit,
    onPaymentHistory: () -> Unit,
    onReceipts: () -> Unit,
    onFinancialSettings: () -> Unit,
    onMeterReading: () -> Unit = {},
    viewModel: com.him.landlordtenant.app.ui.viewmodel.landlord.BillingDashboardViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val billingData by viewModel.uiState.collectAsState()
    
    BillingDashboardContent(
        billingData = billingData,
        onBack = onBack,
        onPaymentHistory = onPaymentHistory,
        onReceipts = onReceipts,
        onFinancialSettings = onFinancialSettings,
        onMeterReading = onMeterReading
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingDashboardContent(
    billingData: LandlordBillingUIState,
    onBack: () -> Unit,
    onPaymentHistory: () -> Unit,
    onReceipts: () -> Unit,
    onFinancialSettings: () -> Unit,
    onMeterReading: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Billing & Financials") },
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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { FinancialOverviewCard(billingData) }
            
            item { SectionHeader("Quick Actions") }
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BillingActionCard(Modifier.weight(1f), Icons.Default.History, "History", onPaymentHistory)
                    BillingActionCard(Modifier.weight(1f), Icons.Default.Receipt, "Receipts", onReceipts)
                    BillingActionCard(Modifier.weight(1f), Icons.Default.Speed, "Meters", onMeterReading)
                    BillingActionCard(Modifier.weight(1f), Icons.Default.Settings, "Settings", onFinancialSettings)
                }
            }
            
            item { SectionHeader("Payment Collection") }
            item { CollectionSummaryCard(billingData) }
        }
    }
}

@Composable
private fun FinancialOverviewCard(data: LandlordBillingUIState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Total Outstanding", fontSize = 12.sp, color = Color.Gray)
            Text(data.totalOutstanding, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
            
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("This Month", fontSize = 10.sp, color = Color.Gray)
                    Text(data.thisMonthCollection, fontWeight = FontWeight.Bold)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Arrears", fontSize = 10.sp, color = Color.Gray)
                    Text(data.arrears, fontWeight = FontWeight.Bold, color = Color.Red)
                }
            }
        }
    }
}

@Composable
private fun BillingActionCard(modifier: Modifier, icon: ImageVector, label: String, onClick: () -> Unit) {
    Card(modifier = modifier.clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CollectionSummaryCard(data: LandlordBillingUIState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryRow("Collected", data.collectedAmount, Color(0xFF2E7D32))
            SummaryRow("Pending", data.pendingAmount, Color(0xFFF57C00))
            SummaryRow("Overdue", data.overdueAmount, Color(0xFFD32F2F))
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(8.dp).background(color, CircleShape))
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, fontSize = 14.sp)
        }
        Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
}

@Preview(showBackground = true)
@Composable
fun BillingDashboardScreenPreview() {
    PropertyOSTheme {
        BillingDashboardContent(
            billingData = LandlordBillingUIState(
                totalOutstanding = "KSh 1.2M",
                thisMonthCollection = "KSh 450k",
                arrears = "KSh 85k",
                collectedAmount = "KSh 320k",
                pendingAmount = "KSh 130k",
                overdueAmount = "KSh 50k"
            ),
            onBack = {},
            onPaymentHistory = {},
            onReceipts = {},
            onFinancialSettings = {}
        )
    }
}
