package com.him.landlordtenant.app.ui.screens.landlord.billing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.him.landlordtenant.app.ui.screens.tenant.TenantPaymentHistoryUIModel
import com.him.landlordtenant.app.ui.screens.tenant.formatPaymentMoney

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentHistoryScreen(
    payments: List<TenantPaymentHistoryUIModel> = emptyList(),
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment History") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (payments.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("No payments recorded", color = Color.Gray)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                items(payments) { payment ->
                    LandlordPaymentItem(payment)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentHistoryScreenPreview() {
    PropertyOSTheme {
        PaymentHistoryScreen(onBack = {})
    }
}

@Composable
private fun LandlordPaymentItem(payment: TenantPaymentHistoryUIModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Ref: ${payment.reference}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = payment.date, fontSize = 11.sp, color = Color.Gray)
        }
        Text(
            text = "KSh ${formatPaymentMoney(payment.amount)}",
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF2E7D32)
        )
    }
}
