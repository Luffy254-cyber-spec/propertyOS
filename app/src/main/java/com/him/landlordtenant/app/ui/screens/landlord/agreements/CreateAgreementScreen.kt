package com.him.landlordtenant.app.ui.screens.landlord.agreements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAgreementScreen(
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    var tenantName by remember { mutableStateOf("") }
    var houseNumber by remember { mutableStateOf("") }
    var agreementContent by remember { mutableStateOf("STANDARD TENANCY AGREEMENT\n\nThis agreement is made between...") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Agreement") },
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
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = tenantName,
                onValueChange = { tenantName = it },
                label = { Text("Tenant Name") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = houseNumber,
                onValueChange = { houseNumber = it },
                label = { Text("House Number") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Text("Agreement Terms", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            
            OutlinedTextField(
                value = agreementContent,
                onValueChange = { agreementContent = it },
                modifier = Modifier.fillMaxWidth(),
                minLines = 10,
                maxLines = 20
            )

            LegalLandlordDisclaimer()
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Generate Agreement")
            }
        }
    }
}

@Composable
private fun LegalLandlordDisclaimer() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Gavel, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "LANDLORD LEGAL COMPLIANCE: Provision of a false agreement, violating the signed terms, or engaging in fraudulent leasing practices may result in severe financial penalties, license revocation, and legal prosecution in a court of law.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold,
                lineHeight = 16.sp
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun CreateAgreementScreenPreview() {
    PropertyOSTheme {
        CreateAgreementScreen(onBack = {}, onSave = {})
    }
}
