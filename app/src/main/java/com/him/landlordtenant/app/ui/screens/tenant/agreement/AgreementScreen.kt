package com.him.landlordtenant.app.ui.screens.tenant.agreement

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import com.him.landlordtenant.app.ui.screens.tenant.StaggeredFadeIn
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.TenantAgreementUIModel
import com.him.landlordtenant.app.ui.screens.tenant.formatPaymentMoney

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgreementScreen(
    agreement: TenantAgreementUIModel,
    onBack: () -> Unit = {},
    onAgreementAccepted: (TenantAgreementUIModel) -> Unit = {},
    onAgreementRejected: () -> Unit = {},
    onContactLandlord: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    var agreeChecked by remember { mutableStateOf(agreement.isAlreadyAccepted) }
    var disagreeChecked by remember { mutableStateOf(false) }
    var showAcceptDialog by remember { mutableStateOf(false) }
    var showRejectDialog by remember { mutableStateOf(false) }

    val reachedBottom = scrollState.value >= scrollState.maxValue - 20

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "Tenancy Agreement", fontSize = 18.sp, fontWeight = FontWeight.Black)
                        Text(text = "Version ${agreement.agreementVersion}", fontSize = 11.sp, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            StaggeredFadeIn(delay = 100) {
                AgreementHeader(agreement)
            }
            Column(modifier = Modifier.weight(1f).verticalScroll(scrollState).padding(horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Spacer(modifier = Modifier.height(8.dp))
                StaggeredFadeIn(delay = 200) { AgreementTitle(agreement) }
                StaggeredFadeIn(delay = 300) { AgreementPartiesSection(agreement) }
                StaggeredFadeIn(delay = 400) { AgreementFinancialsSection(agreement) }
                StaggeredFadeIn(delay = 500) { AgreementTextContent(agreement.agreementContent) }
                if (!agreement.agreementProofUrl.isNullOrEmpty()) {
                    StaggeredFadeIn(delay = 550) { AgreementProofSection(agreement.agreementProofUrl) }
                }
                StaggeredFadeIn(delay = 600) { LegalDisclaimerBox() }
                StaggeredFadeIn(delay = 700) { SignatureStatusCard(agreement) }
                Spacer(modifier = Modifier.height(32.dp))
            }
            
            val actionsVisible = remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { 
                kotlinx.coroutines.delay(800)
                actionsVisible.value = true 
            }
            
            AnimatedVisibility(visible = actionsVisible.value, enter = slideInVertically(initialOffsetY = { it })) {
                AgreementBottomActions(
                    isAccepted = agreement.isAlreadyAccepted,
                    reachedBottom = reachedBottom,
                    agreeChecked = agreeChecked,
                    disagreeChecked = disagreeChecked,
                    onAgreeChanged = { agreeChecked = it; if (it) disagreeChecked = false },
                    onDisagreeChanged = { disagreeChecked = it; if (it) agreeChecked = false },
                    onAgreeClick = { showAcceptDialog = true },
                    onDisagreeClick = { showRejectDialog = true }
                )
            }
        }
    }

    if (showAcceptDialog) {
        AcceptAgreementDialog(
            id = agreement.agreementId,
            version = agreement.agreementVersion,
            onConfirm = { showAcceptDialog = false; onAgreementAccepted(agreement) },
            onDismiss = { showAcceptDialog = false }
        )
    }

    if (showRejectDialog) {
        RejectAgreementDialog(
            onConfirm = { showRejectDialog = false; onAgreementRejected() },
            onDismiss = { showRejectDialog = false }
        )
    }
}

@Composable
private fun AgreementHeader(agreement: TenantAgreementUIModel) {
    val background = if (agreement.isAlreadyAccepted) Color(0xFFE8F5E9) else Color(0xFFE3F2FD)
    Card(modifier = Modifier.fillMaxWidth().padding(16.dp), colors = CardDefaults.cardColors(containerColor = background)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(if (agreement.isAlreadyAccepted) Icons.Default.CheckCircle else Icons.Default.Description, null)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(if (agreement.isAlreadyAccepted) "Agreement Accepted" else "Review Required", fontWeight = FontWeight.Bold)
                Text(if (agreement.isAlreadyAccepted) "Accepted on ${agreement.acceptedDate}" else "Please read carefully before signing.", fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun AgreementTitle(agreement: TenantAgreementUIModel) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("TENANCY AGREEMENT", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        Text("ID: ${agreement.agreementId}", fontSize = 10.sp, color = Color.Gray)
    }
}

@Composable
private fun AgreementPartiesSection(agreement: TenantAgreementUIModel) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("1. PARTIES", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            DetailRow("Landlord", agreement.landlordName)
            DetailRow("Tenant", agreement.tenantName)
            DetailRow("Property", agreement.apartmentName)
            DetailRow("House", agreement.houseNumber)
        }
    }
}

@Composable
private fun AgreementFinancialsSection(agreement: TenantAgreementUIModel) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("2. FINANCIALS", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            DetailRow("Monthly Rent", "KSh ${formatPaymentMoney(agreement.monthlyRent)}")
            DetailRow("Security Deposit", "KSh ${formatPaymentMoney(agreement.deposit)}")
            DetailRow("Notice Period", "${agreement.noticePeriodDays} Days")
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun AgreementTextContent(content: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("3. TERMS & CONDITIONS", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(content, fontSize = 13.sp, lineHeight = 20.sp)
        }
    }
}


@Composable
private fun AgreementProofSection(imageUrl: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("3.5 AGREEMENT PROOF", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            AsyncImage(
                model = imageUrl,
                contentDescription = "Agreement Proof",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Landlord provided this image as proof of the physical agreement.", fontSize = 11.sp, color = Color.Gray)
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
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Medium,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun SignatureStatusCard(agreement: TenantAgreementUIModel) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("4. ACCEPTANCE", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Landlord: ${agreement.landlordName}", fontSize = 12.sp)
            Text("Tenant: ${agreement.tenantName}", fontSize = 12.sp)
            if (agreement.isAlreadyAccepted) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Accepted on ${agreement.acceptedDate}", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun AgreementBottomActions(
    isAccepted: Boolean,
    reachedBottom: Boolean,
    agreeChecked: Boolean,
    disagreeChecked: Boolean,
    onAgreeChanged: (Boolean) -> Unit,
    onDisagreeChanged: (Boolean) -> Unit,
    onAgreeClick: () -> Unit,
    onDisagreeClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (isAccepted) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Lock, null)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Agreement is locked and active.", fontWeight = FontWeight.Bold)
                }
            } else {
                if (!reachedBottom) {
                    Text("Please scroll to the end to enable signing.", fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = agreeChecked, onCheckedChange = onAgreeChanged, enabled = reachedBottom)
                    Text("I agree to the terms.", fontSize = 13.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = disagreeChecked, onCheckedChange = onDisagreeChanged, enabled = reachedBottom)
                    Text("I do not agree.", fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = onDisagreeClick, enabled = reachedBottom && disagreeChecked, modifier = Modifier.weight(1f)) { Text("Reject") }
                    Button(onClick = onAgreeClick, enabled = reachedBottom && agreeChecked, modifier = Modifier.weight(1f)) { Text("Accept") }
                }
            }
        }
    }
}

@Composable
private fun AcceptAgreementDialog(id: String, version: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Accept Agreement?") },
        text = { Text("Confirm acceptance of agreement $id (v$version).") },
        confirmButton = { Button(onClick = onConfirm) { Text("Accept") } },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun RejectAgreementDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reject Agreement?") },
        text = { Text("If you reject, you cannot proceed with the tenancy. Notify landlord?") },
        confirmButton = { Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) { Text("Reject") } },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Back") } }
    )
}

@Preview(showBackground = true)
@Composable
fun AgreementScreenPreview() {
    PropertyOSTheme {
        AgreementScreen(
            agreement = TenantAgreementUIModel(
                agreementId = "AGR-1",
                agreementVersion = "1.0",
                apartmentName = "Green Valley",
                houseNumber = "G2",
                floorNumber = "1",
                landlordName = "John Doe",
                tenantName = "Jane Doe",
                createdDate = "21 Aug",
                effectiveDate = "1 Sept",
                monthlyRent = 15000.0,
                deposit = 15000.0,
                noticePeriodDays = 30,
                agreementContent = "Agreement details..."
            )
        )
    }
}
