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
                Spacer(modifier = Modifier.height(24.dp))
                if (reachedBottom) {
                    Text("✓ You have read the full agreement.", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                } else {
                    Text("↓ Scroll to bottom to sign.", color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            Surface(shadowElevation = 8.dp) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = accepted, onCheckedChange = { accepted = it }, enabled = reachedBottom)
                        Text("I have read and agree to the terms.", fontSize = 13.sp)
                    }
                    Button(
                        onClick = { onAccepted(agreement) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = reachedBottom && accepted,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Accept & Proceed to Payment")
                    }
                }
            }
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
                apartmentName = "Green Valley",
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
