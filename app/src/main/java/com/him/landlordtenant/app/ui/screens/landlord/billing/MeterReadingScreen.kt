package com.him.landlordtenant.app.ui.screens.landlord.billing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.GasMeter
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeterReadingScreen(
    unitName: String,
    tenantName: String,
    meterType: String,
    previousReading: Double,
    ratePerUnit: Double,
    onBack: () -> Unit,
    onSave: (Double, Double, Double) -> Unit
) {
    var currentReadingStr by remember { mutableStateOf("") }
    val currentReading = currentReadingStr.toDoubleOrNull() ?: 0.0
    
    val consumption = if (currentReading >= previousReading) currentReading - previousReading else 0.0
    val totalAmount = consumption * ratePerUnit

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meter Reading") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    TextButton(
                        onClick = { onSave(currentReading, consumption, totalAmount) },
                        enabled = currentReading >= previousReading && currentReadingStr.isNotEmpty()
                    ) {
                        Text("Save", fontWeight = FontWeight.Bold)
                    }
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "$unitName • $tenantName", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Recording $meterType usage", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                }
            }

            // Calculation Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Previous Reading", fontSize = 12.sp, color = Color.Gray)
                            Text("$previousReading Units", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                        Icon(Icons.Default.Speed, null, tint = MaterialTheme.colorScheme.primary)
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    OutlinedTextField(
                        value = currentReadingStr,
                        onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) currentReadingStr = it },
                        label = { Text("Current Reading") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = currentReading < previousReading && currentReadingStr.isNotEmpty(),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                        ),
                        placeholder = { Text("Enter current units") }
                    )
                    
                    if (currentReading < previousReading && currentReadingStr.isNotEmpty()) {
                        Text(
                            "⚠️ Current reading cannot be lower than previous ($previousReading)",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // Automatic Tally (The Calculator part)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Automatic Tally", fontWeight = FontWeight.Black, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    TallyRow("Consumption", "$consumption Units")
                    TallyRow("Rate per Unit", "KES $ratePerUnit")
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Water Bill", fontWeight = FontWeight.Bold)
                        Text("KES $totalAmount", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary, fontSize = 18.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onSave(currentReading, consumption, totalAmount) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = currentReading >= previousReading && currentReadingStr.isNotEmpty(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Reading and Finish", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun TallyRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
}
