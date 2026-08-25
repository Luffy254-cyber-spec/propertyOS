package com.him.landlordtenant.app.ui.screens.landlord.billing

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.TenantBillStatus
import com.him.landlordtenant.app.ui.screens.tenant.TenantBillUIModel
import com.him.landlordtenant.app.ui.screens.tenant.formatPaymentMoney

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillDetailsScreen(
    bill: TenantBillUIModel,
    onBack: () -> Unit,
    onMarkAsPaid: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bill Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp)) {
            Text(text = bill.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = "Due: ${bill.dueDate}", color = Color.Gray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(32.dp))

            DetailItem("Amount", "KSh ${formatPaymentMoney(bill.amount)}")
            DetailItem("Status", bill.status.name)
            DetailItem("Billing Month", bill.billingMonth)
            DetailItem("Description", bill.description)

            Spacer(modifier = Modifier.weight(1f))

            if (bill.status != TenantBillStatus.PAID) {
                Button(
                    onClick = onMarkAsPaid,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Mark as Manually Paid")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BillDetailsScreenPreview() {
    PropertyOSTheme {
        BillDetailsScreen(
            bill = TenantBillUIModel(
                id = "1",
                title = "Water Bill",
                description = "Usage for July 2026",
                amount = 850.0,
                dueDate = "15 Aug 2026",
                status = TenantBillStatus.PENDING,
                billingMonth = "July 2026"
            ),
            onBack = {},
            onMarkAsPaid = {}
        )
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}
