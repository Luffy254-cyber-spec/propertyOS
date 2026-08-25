package com.him.landlordtenant.app.ui.screens.tenant.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.TenantPaymentReceiptUIModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptScreen(
    receipt: TenantPaymentReceiptUIModel,
    onBack: () -> Unit,
    onDownload: () -> Unit = {},
    onShare: () -> Unit = {},
    onPaymentHistory: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment Receipt") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onShare) { Icon(Icons.Default.Share, "Share") }
                    IconButton(onClick = onDownload) { Icon(Icons.Default.Download, "Download") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.size(72.dp).clip(CircleShape).background(Color(0xFFE8F5E9)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(48.dp), tint = Color(0xFF2E7D32))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Payment Successful", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Your payment of KES ${receipt.amount} was successful.", fontSize = 14.sp, color = Color.Gray)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            ReceiptCard(receipt)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(onClick = onPaymentHistory, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Text("Go to Payment History")
            }
        }
    }
}

@Composable
private fun ReceiptCard(receipt: TenantPaymentReceiptUIModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            ReceiptRow("Receipt No", receipt.receiptNumber)
            ReceiptRow("Transaction ID", receipt.transactionId)
            ReceiptRow("Date", receipt.date)
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            ReceiptRow("Paid By", receipt.tenantName)
            ReceiptRow("Phone", receipt.phoneNumber)
            ReceiptRow("Payment Method", receipt.paymentMethod)
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            ReceiptRow("Apartment", receipt.apartmentName)
            ReceiptRow("House Number", receipt.houseNumber)
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            ReceiptRow("Amount Paid", "KES ${receipt.amount}", isBold = true)
        }
    }
}

@Composable
private fun ReceiptRow(label: String, value: String, isBold: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 14.sp, color = Color.Gray)
        Text(value, fontSize = 14.sp, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal)
    }
}

@Preview(showBackground = true)
@Composable
fun ReceiptScreenPreview() {
    PropertyOSTheme {
        ReceiptScreen(
            receipt = TenantPaymentReceiptUIModel(
                receiptNumber = "RCP-88291",
                transactionId = "TXN-123456789",
                amount = 15000.0,
                paymentMethod = "M-PESA",
                phoneNumber = "0712345678",
                date = "17 August 2026",
                apartmentName = "Green Valley Apartments",
                houseNumber = "G1",
                tenantName = "Jane Doe"
            ),
            onBack = {}
        )
    }
}
