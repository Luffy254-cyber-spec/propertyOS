package com.him.landlordtenant.app.ui.screens.tenant

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantHouseJoinAgreementScreen(
    agreement: TenantAgreementUIModel,
    onBack: () -> Unit = {},
    onAccepted: (TenantAgreementUIModel) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var accepted by remember { mutableStateOf(false) }
    val reachedBottom = scrollState.value >= scrollState.maxValue - 20

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tenancy Agreement", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Description, null)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(agreement.apartmentName, fontWeight = FontWeight.Bold)
                        Text("Unit ${agreement.houseNumber}", fontSize = 12.sp)
                    }
                }
            }

            Column(modifier = Modifier.weight(1f).verticalScroll(scrollState).padding(horizontal = 16.dp)) {
                Text("RESIDENTIAL TENANCY AGREEMENT", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                Text(agreement.agreementContent, lineHeight = 22.sp)
                Spacer(modifier = Modifier.height(16.dp))
                LegalDisclaimerBox()
                Spacer(modifier = Modifier.height(24.dp))
            }

            Surface(shadowElevation = 8.dp) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = accepted, onCheckedChange = { accepted = it })
                        Text("I have read and agree to the terms.", fontSize = 13.sp)
                    }
                    Button(
                        onClick = { onAccepted(agreement) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = accepted,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Accept & Proceed to Payment")
                    }
                }
            }
        }
    }
}

@Composable
private fun LegalDisclaimerBox() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.05f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Gavel, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "This is a legally binding electronic agreement. Providing false information or engaging in fraudulent activity can be used against you in a court of law.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Medium,
                lineHeight = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TenantHouseJoinAgreementScreenPreview() {
    PropertyOSTheme {
        TenantHouseJoinAgreementScreen(
            agreement = TenantAgreementUIModel(
                agreementId = "AGR-101",
                agreementVersion = "1.0",
                apartmentName = "Sample Apartment",
                houseNumber = "G2",
                floorNumber = "1",
                landlordName = "John Landlord",
                tenantName = "Jane Tenant",
                createdDate = "17 Aug",
                effectiveDate = "1 Sept",
                monthlyRent = 15000.0,
                deposit = 15000.0,
                noticePeriodDays = 30,
                agreementContent = "Full agreement text goes here..."
            )
        )
    }
}
