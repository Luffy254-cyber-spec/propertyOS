package com.him.landlordtenant.app.ui.screens.landlord.billing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.TenantBillType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBillScreen(
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(TenantBillType.RENT) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Generate Bill") },
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
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Bill Title") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount (KSh)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            
            OutlinedTextField(
                value = dueDate,
                onValueChange = { dueDate = it },
                label = { Text("Due Date") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. 1st Sept 2026") }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank() && amount.isNotBlank()
            ) {
                Text("Generate Bill")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateBillScreenPreview() {
    PropertyOSTheme {
        CreateBillScreen(onBack = {}, onSave = {})
    }
}
