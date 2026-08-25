package com.him.landlordtenant.app.ui.screens.tenant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantMyHouseScreen(
    house: TenantHouseUIModel,
    onBack: () -> Unit = {},
    onHouseDetails: () -> Unit = {},
    onAgreement: () -> Unit = {},
    onPayments: () -> Unit = {},
    onMaintenance: () -> Unit = {},
    onMessages: () -> Unit = {},
    onCallLandlord: () -> Unit = {},
    onVacate: () -> Unit = {},
    onEmergency: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "My House", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HouseHeaderCard(house)
            HouseMoneySummaryCard(house)
            TenancyInfoCard(house)
            Text("House Management", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
            ManagementActionItem(Icons.Default.Description, "Agreement", "View tenancy agreement", onAgreement)
            ManagementActionItem(Icons.Default.Payment, "Payments", "Rent & utility bills", onPayments)
            ManagementActionItem(Icons.Default.Build, "Maintenance", "Request repairs", onMaintenance)
            LandlordContactCard(
                landlordName = house.landlordName,
                landlordPhone = house.landlordPhone,
                onCall = onCallLandlord,
                onMessage = onMessages
            )
            EmergencyCard(onOpen = onEmergency)
            OutlinedButton(onClick = onVacate, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Request to Vacate")
            }
        }
    }
}

@Composable
private fun HouseHeaderCard(house: TenantHouseUIModel) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(52.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Home, null, modifier = Modifier.size(28.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(house.houseNumber, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("${house.houseType.name.replace("_", " ")} • Floor ${house.floorNumber}", fontSize = 12.sp, color = Color.Gray)
                Text(house.apartmentName, fontSize = 11.sp, color = Color.Gray)
            }
            StatusBadge(text = house.status.name, background = Color(0xFFE8F5E9), foreground = Color(0xFF2E7D32))
        }
    }
}

@Composable
private fun HouseMoneySummaryCard(house: TenantHouseUIModel) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Monthly Summary", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            HouseMoneyItem("Monthly Rent", house.monthlyRent)
            HouseMoneyItem("Utilities", house.waterBill + house.garbageBill)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Estimated Total", fontWeight = FontWeight.Bold)
                Text("KSh ${formatPaymentMoney(house.monthlyTotal)}", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun TenancyInfoCard(house: TenantHouseUIModel) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Tenancy Information", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            HouseInfoItem("Move-in Date", house.moveInDate)
            HouseInfoItem("Lease Status", house.leaseStatus)
            HouseInfoItem("Security Deposit", "KSh ${formatPaymentMoney(house.deposit)}")
        }
    }
}

@Composable
private fun ManagementActionItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
                Icon(icon, null, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(subtitle, fontSize = 12.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null)
        }
    }
}

@Composable
private fun HouseMoneyItem(label: String, amount: Double) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Text("KSh ${formatPaymentMoney(amount)}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun HouseInfoItem(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun TenantMyHouseScreenPreview() {
    PropertyOSTheme {
        TenantMyHouseScreen(
            house = TenantHouseUIModel(
                houseNumber = "G2",
                floorNumber = 1,
                houseType = HouseType.TWO_BEDROOM,
                status = HouseStatus.OCCUPIED,
                condition = HouseCondition.GOOD,
                monthlyRent = 15000.0,
                deposit = 15000.0,
                apartmentName = "Green Valley",
                landlordName = "John Doe",
                landlordPhone = "0700000000"
            )
        )
    }
}
