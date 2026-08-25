package com.him.landlordtenant.app.ui.screens.tenant.lease

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.TenantVacateNoticeStatus
import com.him.landlordtenant.app.ui.screens.tenant.TenantVacateNoticeUIModel
import com.him.landlordtenant.app.ui.screens.tenant.TenantVacateReason
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacateRequestScreen(
    apartmentName: String = "Green Valley",
    houseNumber: String = "G1",
    floorNumber: String = "1",
    tenantName: String = "Tenant",
    tenantPhone: String = "0700000000",
    landlordName: String = "Landlord",
    landlordPhone: String = "0799999999",
    onBack: () -> Unit = {},
    onSubmit: (TenantVacateNoticeUIModel) -> Unit = {},
    onSubmitted: () -> Unit = {},
) {
    var selectedReason by remember { mutableStateOf<TenantVacateReason?>(null) }
    var selectedDate by remember { mutableStateOf(getMinVacateDate()) }
    var message by remember { mutableStateOf("") }
    var accepted by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notice to Vacate", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item { NoticeWarningBox() }
            item { PropertyInfoCard(apartmentName, houseNumber, floorNumber) }
            item { VacateDatePicker(selectedDate) { selectedDate = it } }
            item { ReasonPicker(selectedReason) { selectedReason = it } }
            item {
                OutlinedTextField(
                    value = message,
                    onValueChange = { if (it.length <= 500) message = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Additional Message (Optional)") },
                    minLines = 3
                )
            }
            item { DeclarationSection(accepted) { accepted = it } }
            item {
                Button(
                    onClick = { showConfirm = true },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedReason != null && accepted
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submit Notice")
                }
            }
        }
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Confirm Submission") },
            text = { Text("You are submitting a notice to vacate Unit $houseNumber on $selectedDate.") },
            confirmButton = { Button(onClick = {
                val notice = TenantVacateNoticeUIModel(
                    noticeId = "NOTICE-${System.currentTimeMillis()}",
                    apartmentName = apartmentName,
                    houseNumber = houseNumber,
                    floorNumber = floorNumber,
                    tenantName = tenantName,
                    tenantPhone = tenantPhone,
                    landlordName = landlordName,
                    landlordPhone = landlordPhone,
                    noticeDate = getToday(),
                    intendedVacateDate = selectedDate,
                    reason = selectedReason ?: TenantVacateReason.OTHER,
                    additionalMessage = message,
                    status = TenantVacateNoticeStatus.SUBMITTED
                )
                onSubmit(notice)
                onSubmitted()
                showConfirm = false
            }) { Text("Confirm") } },
            dismissButton = { TextButton(onClick = { showConfirm = false }) { Text("Cancel") } }
        )
    }
}

@Composable
private fun NoticeWarningBox() {
    Surface(color = Color(0xFFFFF3E0), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Warning, null, tint = Color(0xFFF57C00))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Submitting this notice is a formal request to terminate your tenancy.", fontSize = 12.sp)
        }
    }
}

@Composable
private fun PropertyInfoCard(apt: String, house: String, floor: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Home, null)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(apt, fontWeight = FontWeight.Bold)
                Text("Unit $house • Floor $floor", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun VacateDatePicker(date: String, onDateChange: (String) -> Unit) {
    Column {
        Text("Vacate Date", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        OutlinedTextField(value = date, onValueChange = onDateChange, modifier = Modifier.fillMaxWidth(), leadingIcon = { Icon(Icons.Default.CalendarMonth, null) })
    }
}

@Composable
private fun ReasonPicker(selected: TenantVacateReason?, onSelected: (TenantVacateReason) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Text("Reason", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Box(modifier = Modifier.fillMaxWidth().clickable { expanded = true }.background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)).padding(16.dp)) {
            Text(selected?.name?.replace("_", " ") ?: "Select Reason")
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                TenantVacateReason.entries.forEach { reason ->
                    DropdownMenuItem(text = { Text(reason.name.replace("_", " ")) }, onClick = { onSelected(reason); expanded = false })
                }
            }
        }
    }
}

@Composable
private fun DeclarationSection(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.Top, modifier = Modifier.clickable { onCheckedChange(!checked) }) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text("I understand that this notice is binding and subject to the tenancy agreement.", fontSize = 12.sp, modifier = Modifier.padding(top = 12.dp))
    }
}

private fun getMinVacateDate(): String {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_MONTH, 3)
    return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(cal.time)
}

private fun getToday() = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())

@Preview(showBackground = true)
@Composable
fun VacateRequestScreenPreview() {
    PropertyOSTheme {
        VacateRequestScreen()
    }
}
