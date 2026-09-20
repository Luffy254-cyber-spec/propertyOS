package com.him.landlordtenant.app.ui.screens.landlord.billing

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.data.model.billing.InvoiceItem
import com.him.landlordtenant.app.data.model.billing.InvoiceItemType
import com.him.landlordtenant.app.ui.viewmodel.landlord.CreateInvoiceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateInvoiceScreen(
    apartmentId: String,
    floorId: String,
    houseId: String,
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: CreateInvoiceViewModel = hiltViewModel()
) {
    val house by viewModel.house.collectAsState()
    val waterRate by viewModel.waterRate.collectAsState()
    val lastReading by viewModel.lastWaterReading.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(houseId) {
        viewModel.loadData(apartmentId, floorId, houseId)
    }

    CreateInvoiceContent(
        house = house,
        waterRate = waterRate,
        isLoading = isLoading,
        onBack = onBack,
        onSaveWaterRate = { viewModel.saveWaterRate(apartmentId, it) },
        onGenerateInvoice = { waterUnits, otherCharges -> 
            viewModel.generateInvoice(house!!, waterUnits, otherCharges, onSuccess)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateInvoiceContent(
    house: com.him.landlordtenant.app.data.model.House?,
    waterRate: Double,
    isLoading: Boolean,
    onBack: () -> Unit,
    onSaveWaterRate: (Double) -> Unit,
    onGenerateInvoice: (Double, List<InvoiceItem>) -> Unit
) {
    var waterUnitsStr by remember { mutableStateOf("") }
    var showRateDialog by remember { mutableStateOf(false) }
    var newRateStr by remember { mutableStateOf(waterRate.toString()) }
    
    val otherCharges = remember { mutableStateListOf<InvoiceItem>() }
    var showAddOtherDialog by remember { mutableStateOf(false) }
    var otherName by remember { mutableStateOf("") }
    var otherAmount by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Generate Invoice") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    TextButton(
                        onClick = { 
                            onGenerateInvoice(
                                waterUnitsStr.toDoubleOrNull() ?: 0.0,
                                otherCharges.toList()
                            )
                        },
                        enabled = !isLoading && house?.tenantId != null
                    ) {
                        Text("Send", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { padding ->
        if (house == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // House Summary
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Home, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Unit ${house.houseNumber}", fontWeight = FontWeight.Bold)
                            Text(house.tenantName ?: "No Tenant Assigned", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }

                // Section: Rent (Read-only)
                BillingSection(title = "Monthly Rent", icon = Icons.Default.Payments) {
                    ReadOnlyField(label = "Amount", value = "KES ${house.monthlyRent}")
                }

                // Section: Water Bill
                BillingSection(
                    title = "Water Consumption", 
                    icon = Icons.Default.WaterDrop,
                    action = {
                        TextButton(onClick = { showRateDialog = true }) {
                            Text("Edit Rate (KES $waterRate)")
                        }
                    }
                ) {
                    OutlinedTextField(
                        value = waterUnitsStr,
                        onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) waterUnitsStr = it },
                        label = { Text("Units Consumed") },
                        modifier = Modifier.fillMaxWidth(),
                        suffix = { Text("Units") }
                    )
                    Text(
                        text = "Cost: KES ${(waterUnitsStr.toDoubleOrNull() ?: 0.0) * waterRate}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Section: Others
                BillingSection(
                    title = "Other Charges", 
                    icon = Icons.Default.AddCircleOutline,
                    action = {
                        IconButton(onClick = { showAddOtherDialog = true }) {
                            Icon(Icons.Default.Add, null)
                        }
                    }
                ) {
                    if (otherCharges.isEmpty()) {
                        Text("No additional charges added.", fontSize = 12.sp, color = Color.Gray)
                    } else {
                        otherCharges.forEach { item ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(item.description, modifier = Modifier.weight(1f))
                                Text("KES ${item.amount}", fontWeight = FontWeight.Bold)
                                IconButton(onClick = { otherCharges.remove(item) }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Delete, null, tint = Color.Red, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                val total = house.monthlyRent + (waterUnitsStr.toDoubleOrNull() ?: 0.0) * waterRate + otherCharges.sumOf { it.amount }
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(modifier = Modifier.padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Grand Total", fontWeight = FontWeight.Bold)
                        Text("KES $total", fontWeight = FontWeight.Black, fontSize = 20.sp)
                    }
                }

                Button(
                    onClick = { 
                        onGenerateInvoice(
                            waterUnitsStr.toDoubleOrNull() ?: 0.0,
                            otherCharges.toList()
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = !isLoading && house.tenantId != null,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isLoading) CircularProgressIndicator(color = Color.White)
                    else Text("Save and Finish", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Dialogs
    if (showRateDialog) {
        AlertDialog(
            onDismissRequest = { showRateDialog = false },
            title = { Text("Water Rate Configuration") },
            text = {
                OutlinedTextField(value = newRateStr, onValueChange = { newRateStr = it }, label = { Text("Rate per Unit (KES)") })
            },
            confirmButton = {
                Button(onClick = { 
                    onSaveWaterRate(newRateStr.toDoubleOrNull() ?: waterRate)
                    showRateDialog = false
                }) { Text("Save") }
            }
        )
    }

    if (showAddOtherDialog) {
        AlertDialog(
            onDismissRequest = { showAddOtherDialog = false },
            title = { Text("Add Charge") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = otherName, onValueChange = { otherName = it }, label = { Text("Description") })
                    OutlinedTextField(value = otherAmount, onValueChange = { otherAmount = it }, label = { Text("Amount (KES)") })
                }
            },
            confirmButton = {
                Button(onClick = { 
                    otherCharges.add(InvoiceItem(type = InvoiceItemType.OTHER, description = otherName, amount = otherAmount.toDoubleOrNull() ?: 0.0))
                    showAddOtherDialog = false
                    otherName = ""
                    otherAmount = ""
                }) { Text("Add") }
            }
        )
    }
}

@Composable
private fun BillingSection(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, action: @Composable () -> Unit = {}, content: @Composable () -> Unit) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, modifier = Modifier.size(18.dp), tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            action()
        }
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun ReadOnlyField(label: String, value: String) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.LightGray.copy(alpha = 0.1f)
        )
    )
}
