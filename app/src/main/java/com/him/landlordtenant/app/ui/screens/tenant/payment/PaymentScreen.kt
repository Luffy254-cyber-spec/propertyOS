package com.him.landlordtenant.app.ui.screens.tenant.payment

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
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

import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.viewmodel.tenant.TenantPaymentViewModel
import com.him.landlordtenant.app.ui.viewmodel.tenant.PaymentProcessState
import com.stripe.android.paymentsheet.rememberPaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    paymentData: TenantPaymentUIState,
    onBack: () -> Unit = {},
    onPaymentSuccessful: (TenantPaymentReceiptUIModel) -> Unit = {},
    onPaymentFailed: () -> Unit = {},
    viewModel: TenantPaymentViewModel = hiltViewModel()
) {
    val paymentState by viewModel.paymentState.collectAsState()
    val paymentConfig by viewModel.paymentConfig.collectAsState()

    LaunchedEffect(paymentData.apartmentId) {
        viewModel.loadPaymentConfig(paymentData.apartmentId)
    }

    val paymentSheet = rememberPaymentSheet { result ->
        when (result) {
            is PaymentSheetResult.Completed -> {
                viewModel.completePayment(paymentData, "Card Payment")
            }
            is PaymentSheetResult.Failed -> {
                // Error handling
            }
            is PaymentSheetResult.Canceled -> {
                // Canceled
            }
        }
    }

    LaunchedEffect(paymentState) {
        if (paymentState is PaymentProcessState.CardIntentCreated) {
            val data = (paymentState as PaymentProcessState.CardIntentCreated).data
            paymentSheet.presentWithPaymentIntent(
                data.clientSecret,
                com.stripe.android.paymentsheet.PaymentSheet.Configuration("propertyOS")
            )
        }
    }

    PaymentContent(
        paymentData = paymentData,
        paymentState = paymentState,
        paymentConfig = paymentConfig,
        onBack = onBack,
        onPayMpesa = { phone -> viewModel.payWithMpesaStkPush(paymentData, phone) },
        onPayCard = { viewModel.payWithCard(paymentData) },
        onConfirmPin = { phone -> viewModel.completePayment(paymentData, phone) },
        onPaymentSuccessful = onPaymentSuccessful
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentContent(
    paymentData: TenantPaymentUIState,
    paymentState: PaymentProcessState,
    paymentConfig: com.him.landlordtenant.app.data.model.billing.PaymentChannelConfig? = null,
    onBack: () -> Unit,
    onPayMpesa: (String) -> Unit,
    onPayCard: () -> Unit,
    onConfirmPin: (String) -> Unit,
    onPaymentSuccessful: (TenantPaymentReceiptUIModel) -> Unit
) {
    var selectedMethod by remember { mutableStateOf(TenantPaymentMethod.MPESA) }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var cardholderName by remember { mutableStateOf("") }
    var saveCardInfo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = if (selectedMethod == TenantPaymentMethod.CARD) "Pay with card" else "Settlement", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        if (selectedMethod != TenantPaymentMethod.CARD) {
                            Text(text = "Choose a payment method", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                },
                navigationIcon = {
                    if (paymentState is PaymentProcessState.Idle) {
                        IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                    }
                }
            )
        }
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        
        when (val state = paymentState) {
            is PaymentProcessState.Idle -> {
                if (selectedMethod == TenantPaymentMethod.CARD) {
                    StripeCardPaymentForm(
                        modifier = modifier,
                        paymentData = paymentData,
                        email = email,
                        onEmailChanged = { email = it },
                        cardholderName = cardholderName,
                        onCardholderNameChanged = { cardholderName = it },
                        saveCardInfo = saveCardInfo,
                        onSaveCardInfoChanged = { saveCardInfo = it },
                        onBackToMethods = { selectedMethod = TenantPaymentMethod.MPESA },
                        onPay = onPayCard
                    )
                } else {
                    PaymentReviewContent(
                        modifier = modifier,
                        paymentData = paymentData,
                        paymentConfig = paymentConfig,
                        selectedMethod = selectedMethod,
                        onMethodSelected = { selectedMethod = it },
                        phoneNumber = phoneNumber,
                        onPhoneChanged = { phoneNumber = it },
                        errorMessage = null,
                        onPay = {
                            if (selectedMethod == TenantPaymentMethod.MPESA) {
                                if (ValidationUtils.isValidPhone(phoneNumber)) {
                                    onPayMpesa(phoneNumber)
                                }
                            } else if (selectedMethod == TenantPaymentMethod.CARD) {
                                onPayCard()
                            }
                        }
                    )
                }
            }
            is PaymentProcessState.Processing, is PaymentProcessState.CardIntentCreated -> {
                PaymentProcessingContent(modifier, selectedMethod, paymentData.total)
            }
            is PaymentProcessState.StkSent -> {
                Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(60.dp), color = MaterialTheme.colorScheme.primary, strokeWidth = 6.dp)
                    Spacer(modifier = Modifier.height(32.dp))
                    Text("Enter M-Pesa PIN", fontSize = 22.sp, fontWeight = FontWeight.Black)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "An M-Pesa prompt has been sent to your phone ($phoneNumber). Please enter your PIN to authorize the payment of KSh ${formatPaymentMoney(paymentData.total)}.",
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    TextButton(onClick = { onConfirmPin(phoneNumber) }) {
                        Text("I have entered my PIN", fontWeight = FontWeight.Bold)
                    }
                    Text("The app will automatically detect your payment status.", fontSize = 11.sp, color = Color.LightGray)
                }
            }
            is PaymentProcessState.Success -> {
                PaymentSuccessContent(modifier, paymentData, state.receipt, onContinue = {
                    onPaymentSuccessful(state.receipt)
                })
            }
            is PaymentProcessState.Error -> {
                PaymentFailedContent(modifier, state.message, onRetry = { /* Reset state logic handled by caller */ })
            }
        }
    }
}

@Composable
private fun PaymentReviewContent(
    modifier: Modifier,
    paymentData: TenantPaymentUIState,
    paymentConfig: com.him.landlordtenant.app.data.model.billing.PaymentChannelConfig? = null,
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
        
        // M-Pesa Shortcode Card (Config or Placeholder)
        if (selectedMethod == TenantPaymentMethod.MPESA) {
            val shortCode = paymentConfig?.mpesaShortCode?.takeIf { it.isNotEmpty() } 
                ?: com.him.landlordtenant.app.util.PaymentKeys.MPESA_BUSINESS_SHORTCODE
            val typeName = paymentConfig?.mpesaType?.name ?: "PAYBILL"

            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, null, tint = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Paying to $typeName: $shortCode", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "Account: ${paymentData.houseNumber}", fontSize = 12.sp)
                    }
                }
            }
        }

        PaymentSummaryInfoCard(paymentData)
        Spacer(modifier = Modifier.height(16.dp))
        if (selectedMethod == TenantPaymentMethod.MPESA) {
            Text(text = "Payment details", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = onPhoneChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("M-Pesa Number") },
                placeholder = { Text("0712345678") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                leadingIcon = { Icon(Icons.Default.Phone, null) },
                isError = errorMessage != null
            )
            if (errorMessage != null) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error, fontSize = 11.sp, modifier = Modifier.padding(start = 4.dp, top = 4.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        Text(text = "Select payment method", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        PaymentMethodItem(TenantPaymentMethod.MPESA, "M-Pesa", selectedMethod == TenantPaymentMethod.MPESA) { onMethodSelected(TenantPaymentMethod.MPESA) }
        PaymentMethodItem(TenantPaymentMethod.CARD, "Visa / Mastercard", selectedMethod == TenantPaymentMethod.CARD) { onMethodSelected(TenantPaymentMethod.CARD) }
        Spacer(modifier = Modifier.height(24.dp))
        
        if (selectedMethod != TenantPaymentMethod.CARD) {
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
}

@Composable
fun StripeCardPaymentForm(
    modifier: Modifier,
    paymentData: TenantPaymentUIState,
    email: String,
    onEmailChanged: (String) -> Unit,
    cardholderName: String,
    onCardholderNameChanged: (String) -> Unit,
    saveCardInfo: Boolean,
    onSaveCardInfoChanged: (Boolean) -> Unit,
    onBackToMethods: () -> Unit,
    onPay: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Amount (Read-only as requested)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Amount to Pay", fontWeight = FontWeight.Medium)
                Text("KSh ${formatPaymentMoney(paymentData.total)}", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary, fontSize = 18.sp)
            }
        }

        Text("Email", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChanged,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("email@example.com") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Text("Payment method", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        
        // Mimic the "Card" box from the image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CreditCard, null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Card", fontWeight = FontWeight.Bold)
                }

                Text("Card information", color = Color.Gray, fontSize = 12.sp)
                
                // Placeholder for Stripe CardInputWidget (In real app, this would be an AndroidView)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                        .padding(12.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("1234 1234 1234 1234", color = Color.LightGray)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                             // Mock icons for Visa/Mastercard
                             Icon(Icons.Default.CreditCard, null, modifier = Modifier.size(16.dp), tint = Color.Blue)
                             Icon(Icons.Default.CreditCard, null, modifier = Modifier.size(16.dp), tint = Color.Red)
                        }
                    }
                }
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                    Box(modifier = Modifier.weight(1f).border(1.dp, Color.LightGray).padding(12.dp)) {
                        Text("MM / YY", color = Color.LightGray)
                    }
                    Box(modifier = Modifier.weight(1f).border(1.dp, Color.LightGray).padding(12.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("CVC", color = Color.LightGray)
                            Icon(Icons.Default.CreditCard, null, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Text("Cardholder name", color = Color.Gray, fontSize = 12.sp)
                OutlinedTextField(
                    value = cardholderName,
                    onValueChange = onCardholderNameChanged,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Full name on card") }
                )

                Text("Country or region", color = Color.Gray, fontSize = 12.sp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                        .padding(12.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("Kenya")
                        Icon(Icons.Default.ArrowDropDown, null)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = saveCardInfo, onCheckedChange = onSaveCardInfoChanged)
                    Text("Save my payment information for future purchases", fontSize = 12.sp)
                }
            }
        }

        // Legal text from image
        Text(
            text = "I agree that I am purchasing a limited license to access the product governed by the Terms of License. By submitting payment information you agree to propertyOS Terms and Privacy Policy including the arbitration clause and revocation policy. If you save your payment information, this will be your default purchase method for all future propertyOS purchases. You can delete your saved payment information anytime from the Billing Settings page. Your payment information will be stored by Stripe, Inc. You agree to Stripe's Terms of Use and Privacy Policy. EU, UK and EEA residents: you consent to the immediate performance of the contract and acknowledge that you thereby lose your right of withdrawal.",
            fontSize = 10.sp,
            color = Color.Gray,
            lineHeight = 14.sp
        )

        Button(
            onClick = onPay,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B67FF)) // Matching the blue from image
        ) {
            Text("Pay", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Powered by ", fontSize = 12.sp, color = Color.Gray)
            Text("stripe", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.Gray)
            Text("  |  Legal  |  Contact", fontSize = 12.sp, color = Color.Gray)
        }
        
        TextButton(onClick = onBackToMethods, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Change Payment Method")
        }

        Spacer(modifier = Modifier.height(24.dp))
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
        PaymentContent(
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
            ),
            paymentState = PaymentProcessState.Idle,
            onBack = {},
            onPayMpesa = {},
            onPayCard = {},
            onConfirmPin = {},
            onPaymentSuccessful = {}
        )
    }
}
