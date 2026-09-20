package com.him.landlordtenant.app.ui.screens.landlord.billing

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.data.model.billing.MpesaType
import com.him.landlordtenant.app.data.model.billing.PaymentChannelConfig
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.viewmodel.landlord.FinancialSettingsViewModel
import com.him.landlordtenant.app.ui.screens.tenant.payment.StripeCardPaymentForm
import com.him.landlordtenant.app.ui.screens.tenant.TenantPaymentUIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialSettingsScreen(
    propertyId: String,
    onBack: () -> Unit,
    viewModel: FinancialSettingsViewModel = hiltViewModel()
) {
    val config by viewModel.config.collectAsState()
    val waterRate by viewModel.waterRate.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val saveSuccess by viewModel.saveSuccess.collectAsState()

    LaunchedEffect(propertyId) {
        viewModel.loadConfig(propertyId)
    }

    if (saveSuccess) {
        LaunchedEffect(Unit) {
            viewModel.resetSuccess()
            onBack()
        }
    }

    FinancialSettingsContent(
        propertyId = propertyId,
        config = config,
        waterRate = waterRate,
        isLoading = isLoading,
        onBack = onBack,
        onSave = { config, rate -> viewModel.saveConfig(config, rate) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialSettingsContent(
    propertyId: String,
    config: PaymentChannelConfig?,
    waterRate: Double,
    isLoading: Boolean,
    onBack: () -> Unit,
    onSave: (PaymentChannelConfig, Double) -> Unit
) {
    var mpesaShortCode by remember { mutableStateOf("") }
    var mpesaType by remember { mutableStateOf(MpesaType.PAYBILL) }
    
    var cardEnabled by remember { mutableStateOf(false) }
    var stripePayoutCardNumber by remember { mutableStateOf("") }
    var cashEnabled by remember { mutableStateOf(true) }
    
    var localWaterRate by remember { mutableStateOf(waterRate.toString()) }

    LaunchedEffect(config, waterRate) {
        localWaterRate = waterRate.toString()
        
        config?.let {
            mpesaShortCode = it.mpesaShortCode ?: ""
            mpesaType = it.mpesaType
            cardEnabled = it.cardEnabled
            stripePayoutCardNumber = it.stripePayoutCardNumber ?: ""
            cashEnabled = it.cashEnabled
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp).padding(end = 16.dp))
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text("Configure your payment receiving channels and utility rates. Secure keys are managed centrally by the platform.", color = Color.Gray, fontSize = 14.sp)

            // Utility Pricing
            PaymentChannelSection(title = "Utility Pricing", icon = Icons.Default.WaterDrop) {
                OutlinedTextField(
                    value = localWaterRate,
                    onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) localWaterRate = it },
                    label = { Text("Water Rate (KES per Unit)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
                )
            }

            // M-Pesa Configuration
            PaymentChannelSection(title = "M-Pesa (Safaricom)", icon = Icons.Default.PhoneAndroid) {
                MpesaTypeSelector(mpesaType) { mpesaType = it }
                
                OutlinedTextField(
                    value = mpesaShortCode,
                    onValueChange = { mpesaShortCode = it },
                    label = { Text("M-Pesa Shortcode") },
                    placeholder = { Text("174379") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
                )
                
                Text(
                    "Note: Secure API credentials (Consumer Key/Secret) are configured in the propertyOS master backend.",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    lineHeight = 14.sp
                )
            }

            // Cards
            PaymentChannelSection(title = "Visa / Mastercard", icon = Icons.Default.CreditCard) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = cardEnabled, onCheckedChange = { cardEnabled = it })
                    Text("Enable Card Payments (via Stripe)")
                }
                
                if (cardEnabled) {
                    OutlinedTextField(
                        value = stripePayoutCardNumber,
                        onValueChange = { stripePayoutCardNumber = it },
                        label = { Text("Payout Card Number") },
                        placeholder = { Text("1234 5678 9012 3456") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
                    )

                    Text(
                        "Money received via Stripe will be settled to the bank account linked to this card.",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        lineHeight = 14.sp
                    )
                }
            }

            // Cash
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Switch(checked = cashEnabled, onCheckedChange = { cashEnabled = it })
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Allow Cash/Manual Payments", fontWeight = FontWeight.Bold)
                    Text("Tenants can record manual transactions.", fontSize = 12.sp, color = Color.Gray)
                }
            }

            HorizontalDivider()

            Text("Form Preview (Tenant's View)", fontWeight = FontWeight.Black, fontSize = 16.sp)
            
            // Preview of the form
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)),
                color = MaterialTheme.colorScheme.surface
            ) {
                StripeCardPaymentForm(
                    modifier = Modifier,
                    paymentData = TenantPaymentUIState(
                        transactionId = "PREVIEW",
                        apartmentId = propertyId,
                        houseId = "PREVIEW",
                        houseNumber = "G2-Preview",
                        landlordName = "Property Owner",
                        rent = 25000.0,
                        deposit = 0.0,
                        water = 0.0,
                        garbage = 0.0,
                        serviceCharge = 0.0
                    ),
                    email = "tenant@example.com",
                    onEmailChanged = {},
                    cardholderName = "John Doe",
                    onCardholderNameChanged = {},
                    saveCardInfo = false,
                    onSaveCardInfoChanged = {},
                    onBackToMethods = {},
                    onPay = {}
                )
            }

            Button(
                onClick = {
                    val newConfig = PaymentChannelConfig(
                        propertyId = propertyId,
                        mpesaShortCode = mpesaShortCode,
                        mpesaType = mpesaType,
                        cardEnabled = cardEnabled,
                        stripePayoutCardNumber = stripePayoutCardNumber,
                        cashEnabled = cashEnabled
                    )
                    onSave(newConfig, localWaterRate.toDoubleOrNull() ?: waterRate)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Financial Credentials", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun PaymentChannelSection(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, content: @Composable ColumnScope.() -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(12.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            content()
        }
    }
}

@Composable
private fun MpesaTypeSelector(selected: MpesaType, onSelected: (MpesaType) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        MpesaType.entries.forEach { type ->
            FilterChip(
                selected = selected == type,
                onClick = { onSelected(type) },
                label = { Text(type.name.replace("_", " ")) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FinancialSettingsScreenPreview() {
    PropertyOSTheme {
        FinancialSettingsContent(
            propertyId = "1",
            config = null,
            waterRate = 100.0,
            isLoading = false,
            onBack = {},
            onSave = { _, _ -> }
        )
    }
}
