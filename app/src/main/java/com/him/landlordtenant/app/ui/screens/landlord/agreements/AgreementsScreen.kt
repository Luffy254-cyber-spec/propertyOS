package com.him.landlordtenant.app.ui.screens.landlord.agreements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.TenantAgreementUIModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgreementsScreen(
    agreements: List<TenantAgreementUIModel> = emptyList(),
    onBack: () -> Unit,
    onAgreementClick: (String) -> Unit,
    onCreateAgreement: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rental Agreements") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onCreateAgreement) {
                        Icon(Icons.Default.Add, "New")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateAgreement) {
                Icon(Icons.Default.Add, "Create Agreement")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                placeholder = { Text("Search by tenant or house...") },
                leadingIcon = { Icon(Icons.Default.Search, null) }
            )

            if (agreements.isEmpty()) {
                EmptyAgreements(onCreateAgreement)
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(agreements.filter { it.tenantName.contains(searchQuery, true) || it.houseNumber.contains(searchQuery, true) }) { agreement ->
                        AgreementItem(agreement) { onAgreementClick(agreement.agreementId) }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AgreementsScreenPreview() {
    PropertyOSTheme {
        AgreementsScreen(onBack = {}, onAgreementClick = {}, onCreateAgreement = {})
    }
}

@Composable
private fun AgreementItem(agreement: TenantAgreementUIModel, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Description, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Tenant: ${agreement.tenantName}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = "House ${agreement.houseNumber} • ${agreement.apartmentName}", fontSize = 12.sp, color = Color.Gray)
            }
            if (agreement.isAlreadyAccepted) {
                Surface(color = Color(0xFFE8F5E9), shape = MaterialTheme.shapes.small) {
                    Text("Active", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = Color(0xFF2E7D32), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun EmptyAgreements(onCreate: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("No agreements yet", color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onCreate) { Text("Create First Agreement") }
        }
    }
}
