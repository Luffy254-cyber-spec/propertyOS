package com.him.landlordtenant.app.ui.screens.tenant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.formatPaymentMoney

enum class RentBillsTab { OVERVIEW, BILLS, HISTORY }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantRentAndBillsScreen(
    rent: TenantRentSummaryUIModel = TenantRentSummaryUIModel(),
    bills: List<TenantBillUIModel> = emptyList(),
    payments: List<TenantPaymentHistoryUIModel> = emptyList(),
    onBack: () -> Unit = {},
    onPayNow: (List<TenantBillUIModel>) -> Unit = {},
    onViewReceipt: (TenantPaymentHistoryUIModel) -> Unit = {},
    onRefresh: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(RentBillsTab.OVERVIEW) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rent & Bills") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Default.Refresh, "Refresh")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            TabRow(selectedTabIndex = selectedTab.ordinal) {
                RentBillsTab.entries.forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = { Text(tab.name.lowercase().replaceFirstChar { it.uppercase() }) }
                    )
                }
            }

            when (selectedTab) {
                RentBillsTab.OVERVIEW -> RentOverview(rent, bills, onPayNow)
                RentBillsTab.BILLS -> BillsList(bills, onPayNow)
                RentBillsTab.HISTORY -> PaymentsHistory(payments, onViewReceipt)
            }
        }
    }
}

@Composable
private fun RentOverview(rent: TenantRentSummaryUIModel, bills: List<TenantBillUIModel>, onPayNow: (List<TenantBillUIModel>) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Total Outstanding", fontSize = 12.sp, color = Color.Gray)
                    Text("KES ${formatPaymentMoney(rent.monthlyRent + rent.arrears)}", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Due Date: ${rent.dueDate}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        
        item {
            Button(onClick = { onPayNow(bills.filter { it.status != TenantBillStatus.PAID }) }, modifier = Modifier.fillMaxWidth()) {
                Text("Pay All Outstanding")
            }
        }
    }
}

@Composable
private fun BillsList(bills: List<TenantBillUIModel>, onPayNow: (List<TenantBillUIModel>) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(bills) { bill ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(bill.icon ?: Icons.Default.Receipt, null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(bill.title, fontWeight = FontWeight.Bold)
                        Text("Due: ${bill.dueDate}", fontSize = 11.sp, color = Color.Gray)
                    }
                    Text("KES ${formatPaymentMoney(bill.amount)}", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun PaymentsHistory(payments: List<TenantPaymentHistoryUIModel>, onViewReceipt: (TenantPaymentHistoryUIModel) -> Unit) {
    if (payments.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No payment history yet", color = Color.Gray)
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(payments) { payment ->
                Card(modifier = Modifier.fillMaxWidth().clickable { onViewReceipt(payment) }) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.History, null, tint = Color.Gray)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(payment.description, fontWeight = FontWeight.Bold)
                            Text(payment.date, fontSize = 11.sp, color = Color.Gray)
                        }
                        Text("KES ${payment.amount}", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TenantRentAndBillsScreenPreview() {
    PropertyOSTheme {
        TenantRentAndBillsScreen(
            rent = TenantRentSummaryUIModel(monthlyRent = 15000.0, dueDate = "1st Sept", arrears = 0.0),
            bills = listOf(TenantBillUIModel(title = "Water", amount = 650.0, dueDate = "28th Aug"))
        )
    }
}
