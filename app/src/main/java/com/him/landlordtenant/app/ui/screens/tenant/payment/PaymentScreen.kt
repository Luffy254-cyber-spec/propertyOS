package com.him.landlordtenant.app.ui.screens.tenant.payment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.screens.tenant.PaymentScreenStatus
import com.him.landlordtenant.app.ui.screens.tenant.TenantPaymentMethod
import com.him.landlordtenant.app.ui.screens.tenant.TenantPaymentReceiptUIModel
import com.him.landlordtenant.app.ui.screens.tenant.TenantPaymentUIState
import com.him.landlordtenant.app.ui.screens.tenant.formatPaymentMoney
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.utils.ValidationUtils
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    paymentData: TenantPaymentUIState,
    onBack: () -> Unit = {},
    onPaymentSuccessful: (TenantPaymentReceiptUIModel) -> Unit = {},
    onPaymentFailed: () -> Unit = {},
) {
    var selectedMethod by remember { mutableStateOf(TenantPaymentMethod.MPESA) }
    var phoneNumber by remember { mutableStateOf("") }
    var paymentStatus by remember { mutableStateOf(PaymentScreenStatus.REVIEW) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var receipt by remember { mutableStateOf<TenantPaymentReceiptUIModel?>(null) }

    LaunchedEffect(paymentStatus) {
        if (paymentStatus == PaymentScreenStatus.PROCESSING) {
            delay(2000)
            val generatedReceipt = TenantPaymentReceiptUIModel(
                receiptNumber = "RCP-${System.currentTimeMillis()}",
                transactionId = paymentData.transactionId,
                amount = paymentData.total,
                paymentMethod = selectedMethod.name,
                phoneNumber = phoneNumber,
                date = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date()),
                apartmentName = "Green Valley Apartments",
                houseNumber = paymentData.houseNumber
            )
            receipt = generatedReceipt
            paymentStatus = PaymentScreenStatus.SUCCESS
            onPaymentSuccessful(generatedReceipt)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "Payment", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Review and confirm payment", fontSize = 11.sp, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    if (paymentStatus == PaymentScreenStatus.REVIEW) {
                        IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                    }
                }
            )
        }
    ) { paddingValues ->
        when (paymentStatus) {
            PaymentScreenStatus.REVIEW -> PaymentReviewContent(
                modifier = Modifier.padding(paddingValues),
                paymentData = paymentData,
                selectedMethod = selectedMethod,
                onMethodSelected = { selectedMethod = it; errorMessage = null },
                phoneNumber = phoneNumber,
                onPhoneChanged = { phoneNumber = it; errorMessage = null },
                errorMessage = errorMessage,
                onPay = {
                    if (!ValidationUtils.isValidPhone(phoneNumber)) {
                        errorMessage = "Enter a valid Kenyan phone number."
                    } else {
                        paymentStatus = PaymentScreenStatus.PROCESSING
                    }
                }
            )
            PaymentScreenStatus.PROCESSING -> PaymentProcessingContent(Modifier.padding(paddingValues), selectedMethod, paymentData.total)
            PaymentScreenStatus.SUCCESS -> PaymentSuccessContent(Modifier.padding(paddingValues), paymentData, receipt, onContinue = {
                receipt?.let { onPaymentSuccessful(it) }
            })
            PaymentScreenStatus.FAILED -> PaymentFailedContent(Modifier.padding(paddingValues), errorMessage ?: "Payment failed", onRetry = { paymentStatus = PaymentScreenStatus.REVIEW })
        }
    }
}

@Composable
private fun PaymentReviewContent(
    modifier: Modifier,
    paymentData: TenantPaymentUIState,
    selectedMethod: TenantPaymentMethod,
    onMethodSelected: (TenantPaymentMethod) -> Unit,
    phoneNumber: String,
    onPhoneChanged: (String) -> Unit,
    errorMessage: String?,
    onPay: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        PaymentSecurityBanner()
        Spacer(modifier = Modifier.height(16.dp))
        PaymentSummaryInfoCard(paymentData)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Payment details", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = onPhoneChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("M-Pesa / Phone Number") },
            placeholder = { Text("0712345678") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            leadingIcon = { Icon(Icons.Default.Phone, null) },
            isError = errorMessage != null
        )
        if (errorMessage != null) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error, fontSize = 11.sp, modifier = Modifier.padding(start = 4.dp, top = 4.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Select payment method", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        PaymentMethodItem(TenantPaymentMethod.MPESA, "M-Pesa", selectedMethod == TenantPaymentMethod.MPESA) { onMethodSelected(TenantPaymentMethod.MPESA) }
        PaymentMethodItem(TenantPaymentMethod.AIRTEL_MONEY, "Airtel Money", selectedMethod == TenantPaymentMethod.AIRTEL_MONEY) { onMethodSelected(TenantPaymentMethod.AIRTEL_MONEY) }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onPay,
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = phoneNumber.isNotBlank()
        ) {
            Icon(Icons.Default.Lock, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Pay KSh ${formatPaymentMoney(paymentData.total)}", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PaymentMethodItem(method: TenantPaymentMethod, title: String, selected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = selected, onClick = onClick)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = title, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun PaymentSummaryInfoCard(paymentData: TenantPaymentUIState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Amount Breakdown", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            PaymentRow("Rent", paymentData.rent)
            PaymentRow("Deposit", paymentData.deposit)
            PaymentRow("Utilities", paymentData.water + paymentData.garbage)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Total", fontWeight = FontWeight.Bold)
                Text(text = "KSh ${formatPaymentMoney(paymentData.total)}", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun PaymentRow(label: String, amount: Double) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, fontSize = 13.sp, color = Color.Gray)
        Text(text = "KSh ${formatPaymentMoney(amount)}", fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun PaymentSecurityBanner() {
    Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(8.dp)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Security, null, tint = Color(0xFF2E7D32))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Your payment is secured and encrypted.", fontSize = 12.sp, color = Color(0xFF2E7D32))
        }
    }
}

@Composable
private fun PaymentProcessingContent(modifier: Modifier, method: TenantPaymentMethod, amount: Double) {
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        CircularProgressIndicator(modifier = Modifier.size(50.dp))
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Processing ${method.name} Payment", fontWeight = FontWeight.Bold)
        Text(text = "KSh ${formatPaymentMoney(amount)}", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Please do not close the app...", fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
private fun PaymentSuccessContent(
    modifier: Modifier,
    paymentData: TenantPaymentUIState,
    receipt: TenantPaymentReceiptUIModel?,
    onContinue: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(80.dp), tint = Color(0xFF2E7D32))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Payment Successful!", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Your payment has been received.", textAlign = TextAlign.Center, color = Color.Gray)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onContinue, modifier = Modifier.fillMaxWidth()) { Text("Finish") }
    }
}

@Composable
private fun PaymentFailedContent(modifier: Modifier, error: String, onRetry: () -> Unit) {
    Column(modifier = modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.Error, null, modifier = Modifier.size(80.dp), tint = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Payment Failed", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(text = error, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentScreenPreview() {
    PropertyOSTheme {
        PaymentScreen(
            paymentData = TenantPaymentUIState(
                transactionId = "TX-1",
                apartmentId = "1",
                houseId = "1",
                houseNumber = "G2",
                landlordName = "John Doe",
                rent = 15000.0,
                deposit = 0.0,
                water = 500.0,
                garbage = 200.0,
                serviceCharge = 1000.0
            )
        )
    }
}
