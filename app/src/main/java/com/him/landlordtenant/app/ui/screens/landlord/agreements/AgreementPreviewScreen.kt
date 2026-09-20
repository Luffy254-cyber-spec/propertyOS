package com.him.landlordtenant.app.ui.screens.landlord.agreements

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.TenantAgreementUIModel
import com.him.landlordtenant.app.ui.screens.tenant.agreement.AgreementScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgreementPreviewScreen(
    agreement: TenantAgreementUIModel,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agreement Preview") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        // Reuse tenant agreement screen for consistent preview
        AgreementScreen(
            agreement = agreement,
            onBack = onBack
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AgreementPreviewScreenPreview() {
    PropertyOSTheme {
        AgreementPreviewScreen(
            agreement = TenantAgreementUIModel(
                agreementId = "AGR-101",
                agreementVersion = "1.0",
                apartmentName = "Green Valley",
                houseNumber = "G2",
                floorNumber = "1",
                landlordId = "LL001",
                landlordName = "John Landlord",
                tenantName = "Jane Tenant",
                createdDate = "22 Aug",
                effectiveDate = "1 Sept",
                monthlyRent = 15000.0,
                deposit = 15000.0,
                noticePeriodDays = 30,
                agreementContent = "Agreement details..."
            ),
            onBack = {}
        )
    }
}
