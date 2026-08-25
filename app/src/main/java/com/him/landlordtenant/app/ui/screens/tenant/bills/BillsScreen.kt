package com.him.landlordtenant.app.ui.screens.tenant.bills

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.him.landlordtenant.app.ui.screens.tenant.TenantBillUIModel
import com.him.landlordtenant.app.ui.screens.tenant.TenantBillStatus

enum class BillFilter {
    ALL, PENDING, PAID, OVERDUE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillsScreen(
    apartmentName: String,
    houseNumber: String,
    bills: List<TenantBillUIModel>,
    onBack: () -> Unit,
    onPayBill: (TenantBillUIModel) -> Unit = {},
    onViewReceipt: (String) -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    var selectedFilter by remember { mutableStateOf(BillFilter.ALL) }

    val filteredBills = remember(selectedFilter, bills) {
        when (selectedFilter) {
            BillFilter.ALL -> bills
            BillFilter.PENDING -> bills.filter { it.status == TenantBillStatus.PENDING }
            BillFilter.PAID -> bills.filter { it.status == TenantBillStatus.PAID }
            BillFilter.OVERDUE -> bills.filter { it.status == TenantBillStatus.OVERDUE }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("My Bills", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("$apartmentName • $houseNumber", fontSize = 11.sp, color = Color.Gray)
                    }
                },
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
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                BillSummaryHeader(bills)
            }
            
            item {
                BillFilters(selected = selectedFilter, onSelected = { selectedFilter = it })
            }
            
            if (filteredBills.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                        Text("No bills found", color = Color.Gray)
                    }
                }
            } else {
                items(filteredBills) { bill ->
                    TenantBillItemCard(
                        bill = bill,
                        onPay = { onPayBill(bill) },
                        onDetails = { if (bill.status == TenantBillStatus.PAID) bill.receiptNumber?.let { onViewReceipt(it) } }
                    )
                }
            }
        }
    }
}

@Composable
private fun BillSummaryHeader(bills: List<TenantBillUIModel>) {
    val totalDue = bills.filter { it.status != TenantBillStatus.PAID }.sumOf { it.amount }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Total Outstanding", fontSize = 12.sp)
            Text("KES $totalDue", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun BillFilters(selected: BillFilter, onSelected: (BillFilter) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(BillFilter.entries) { filter ->
            FilterChip(
                selected = selected == filter,
                onClick = { onSelected(filter) },
                label = { Text(filter.name) }
            )
        }
    }
}

@Composable
private fun TenantBillItemCard(bill: TenantBillUIModel, onPay: () -> Unit, onDetails: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onDetails)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(42.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(bill.icon ?: Icons.Default.Receipt, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(bill.title, fontWeight = FontWeight.Bold)
                Text("Due: ${bill.dueDate}", fontSize = 11.sp, color = Color.Gray)
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text("KES ${bill.amount}", fontWeight = FontWeight.Bold)
                StatusBadge(status = bill.status)
                if (bill.status != TenantBillStatus.PAID) {
                    TextButton(onClick = onPay, contentPadding = PaddingValues(0.dp)) {
                        Text("Pay Now", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(status: TenantBillStatus) {
    val color = when (status) {
        TenantBillStatus.PAID -> Color(0xFF2E7D32)
        TenantBillStatus.PENDING -> Color(0xFFF57C00)
        TenantBillStatus.OVERDUE -> Color(0xFFD32F2F)
        TenantBillStatus.PARTIALLY_PAID -> Color(0xFF1976D2)
    }
    Surface(shape = RoundedCornerShape(50), color = color.copy(alpha = 0.1f)) {
        Text(text = status.name, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Preview(showBackground = true)
@Composable
fun BillsScreenPreview() {
    PropertyOSTheme {
        BillsScreen(
            apartmentName = "Green Valley",
            houseNumber = "G1",
            bills = listOf(
                TenantBillUIModel(title = "Rent", amount = 15000.0, dueDate = "1st Sept", status = TenantBillStatus.PENDING),
                TenantBillUIModel(title = "Water", amount = 650.0, dueDate = "28th Aug", status = TenantBillStatus.OVERDUE),
                TenantBillUIModel(title = "Garbage", amount = 300.0, dueDate = "28th Aug", status = TenantBillStatus.PAID)
            ),
            onBack = {}
        )
    }
}
