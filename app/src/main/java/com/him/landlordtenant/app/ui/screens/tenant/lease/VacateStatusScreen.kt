package com.him.landlordtenant.app.ui.screens.tenant.lease

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.LandlordContactCard
import com.him.landlordtenant.app.ui.screens.tenant.TenantVacateNoticeStatus
import com.him.landlordtenant.app.ui.screens.tenant.TenantVacateNoticeStatusUIModel
import com.him.landlordtenant.app.ui.screens.tenant.formatPaymentMoney

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacateStatusScreen(
    notice: TenantVacateNoticeStatusUIModel,
    onBack: () -> Unit = {},
    onCancelNotice: () -> Unit = {},
    onContactLandlord: () -> Unit = {},
    onRefresh: () -> Unit = {}
) {
    var showCancelDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "Notice Status", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = notice.noticeId, fontSize = 10.sp, color = Color.Gray)
                    }
                },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                actions = { IconButton(onClick = onRefresh) { Icon(Icons.Default.Refresh, "Refresh") } }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item { NoticeStatusHeader(notice.status) }
            item { NoticeTimelineSection(notice) }
            item { NoticeDetailsCard(notice) }
            item {
                LandlordContactCard(
                    landlordName = notice.landlordName,
                    landlordPhone = notice.landlordPhone,
                    onCall = {},
                    onMessage = onContactLandlord
                )
            }
            if (notice.status == TenantVacateNoticeStatus.SUBMITTED) {
                item {
                    OutlinedButton(onClick = { showCancelDialog = true }, modifier = Modifier.fillMaxWidth()) {
                        Text("Cancel Notice", color = Color.Red)
                    }
                }
            }
        }
    }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("Cancel Notice?") },
            text = { Text("Are you sure you want to withdraw your notice to vacate?") },
            confirmButton = { TextButton(onClick = { onCancelNotice(); showCancelDialog = false }) { Text("Yes, Cancel") } },
            dismissButton = { TextButton(onClick = { showCancelDialog = false }) { Text("No, Keep It") } }
        )
    }
}

@Composable
private fun NoticeStatusHeader(status: TenantVacateNoticeStatus) {
    val color = when (status) {
        TenantVacateNoticeStatus.APPROVED -> Color(0xFF2E7D32)
        TenantVacateNoticeStatus.REJECTED -> Color.Red
        else -> Color(0xFFF57C00)
    }
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Info, null, tint = color, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(status.name.replace("_", " "), fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
private fun NoticeTimelineSection(notice: TenantVacateNoticeStatusUIModel) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Progress", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            TimelineItem("Submitted", notice.submittedAt, true)
            TimelineItem("Acknowledged", notice.acknowledgedAt ?: "Pending", notice.acknowledgedAt != null)
            TimelineItem("Inspection", notice.inspectionDate ?: "Not scheduled", notice.inspectionDate != null)
            TimelineItem("Final Approval", notice.approvedAt ?: "Pending", notice.approvedAt != null)
        }
    }
}

@Composable
private fun TimelineItem(title: String, subtitle: String, completed: Boolean) {
    Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(if (completed) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked, null, tint = if (completed) Color(0xFF2E7D32) else Color.Gray, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, fontSize = 11.sp, color = Color.Gray)
        }
    }
}

@Composable
private fun NoticeDetailsCard(notice: TenantVacateNoticeStatusUIModel) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Details", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            NoticeRow("Intended Date", notice.intendedVacateDate)
            NoticeRow("Reason", notice.reason)
            if (notice.outstandingBalance > 0) {
                NoticeRow("Outstanding", "KSh ${formatPaymentMoney(notice.outstandingBalance)}")
            }
        }
    }
}

@Composable
private fun NoticeRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Preview(showBackground = true)
@Composable
fun VacateStatusScreenPreview() {
    PropertyOSTheme {
        VacateStatusScreen(
            notice = TenantVacateNoticeStatusUIModel(
                noticeId = "1",
                apartmentName = "Green Valley",
                houseNumber = "G2",
                floorNumber = "1",
                noticeDate = "21 Aug",
                intendedVacateDate = "30 Sept",
                reason = "Moving closer to work",
                status = TenantVacateNoticeStatus.SUBMITTED,
                landlordName = "John Doe",
                landlordPhone = "0700000000",
                submittedAt = "21 Aug 2026"
            )
        )
    }
}
