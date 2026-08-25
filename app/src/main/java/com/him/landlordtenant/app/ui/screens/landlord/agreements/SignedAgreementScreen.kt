package com.him.landlordtenant.app.ui.screens.landlord.agreements

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.TenantAgreementUIModel
import com.him.landlordtenant.app.ui.screens.tenant.agreement.AgreementScreen

@Composable
fun SignedAgreementScreen(
    agreement: TenantAgreementUIModel,
    onBack: () -> Unit
) {
    AgreementScreen(
        agreement = agreement,
        onBack = onBack
    )
}

@Preview(showBackground = true)
@Composable
fun SignedAgreementScreenPreview() {
    PropertyOSTheme {
        SignedAgreementScreen(
            agreement = TenantAgreementUIModel(
                agreementId = "AGR-101",
                agreementVersion = "1.0",
                apartmentName = "Green Valley",
                houseNumber = "G2",
                floorNumber = "1",
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
