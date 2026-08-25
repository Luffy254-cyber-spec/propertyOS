package com.him.landlordtenant.app.ui.screens.landlord.billing

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialSettingsScreen(
    onBack: () -> Unit
) {
    var paybillNumber by remember { mutableStateOf("123456") }
    var accountPrefix by remember { mutableStateOf("GV") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Financial Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Payment Channel Configuration", style = MaterialTheme.typography.titleMedium)
            
            OutlinedTextField(
                value = paybillNumber,
                onValueChange = { paybillNumber = it },
                label = { Text("M-Pesa Paybill Number") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = accountPrefix,
                onValueChange = { accountPrefix = it },
                label = { Text("House Account Prefix") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { /* Save */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Settings")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FinancialSettingsScreenPreview() {
    PropertyOSTheme {
        FinancialSettingsScreen(onBack = {})
    }
}
