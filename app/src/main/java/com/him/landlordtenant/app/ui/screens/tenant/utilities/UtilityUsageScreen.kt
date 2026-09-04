package com.him.landlordtenant.app.ui.screens.tenant.utilities

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.screens.tenant.UtilityUsageUIModel
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.theme.SkyGradient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtilityUsageScreen(
    onBack: () -> Unit
) {
    val usageData = listOf(
        UtilityUsageUIModel("May", 12.0, 1200.0, 45.0, 2200.0),
        UtilityUsageUIModel("Jun", 15.0, 1500.0, 50.0, 2500.0),
        UtilityUsageUIModel("Jul", 10.0, 1000.0, 40.0, 2000.0),
        UtilityUsageUIModel("Aug", 18.0, 1800.0, 55.0, 2800.0)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Utility Tracker", fontWeight = FontWeight.Black) },
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
                .padding(24.dp)
        ) {
            Text("Consumption Analytics", fontSize = 20.sp, fontWeight = FontWeight.Black)
            Text("Track your monthly water and electricity usage over time.", color = Color.Gray, fontSize = 14.sp)
            
            Spacer(modifier = Modifier.height(32.dp))

            UsageChart(usageData)

            Spacer(modifier = Modifier.height(40.dp))

            UsageBreakdown(usageData.last())
        }
    }
}

@Composable
private fun UsageChart(data: List<UtilityUsageUIModel>) {
    Card(
        modifier = Modifier.fillMaxWidth().height(250.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Monthly Trend", fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).background(Color(0xFF2196F3), RoundedCornerShape(2.dp)))
                    Text(" Electricity", fontSize = 10.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.size(10.dp).background(Color(0xFF00BCD4), RoundedCornerShape(2.dp)))
                    Text(" Water", fontSize = 10.sp, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth().height(150.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                data.forEach { month ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(modifier = Modifier.width(12.dp).height((month.electricityUnits * 2).dp).background(Color(0xFF2196F3), RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)))
                            Box(modifier = Modifier.width(12.dp).height((month.waterUnits * 4).dp).background(Color(0xFF00BCD4), RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(month.month, fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
private fun UsageBreakdown(latest: UtilityUsageUIModel) {
    Column {
        Text("Current Month Details", fontWeight = FontWeight.Black, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            InfoCard(
                modifier = Modifier.weight(1f),
                title = "Electricity",
                value = "${latest.electricityUnits} kWh",
                cost = "KES ${latest.electricityCost}",
                icon = Icons.Default.ElectricBolt,
                color = Color(0xFF2196F3)
            )
            InfoCard(
                modifier = Modifier.weight(1f),
                title = "Water",
                value = "${latest.waterUnits} m³",
                cost = "KES ${latest.waterCost}",
                icon = Icons.Default.WaterDrop,
                color = Color(0xFF00BCD4)
            )
        }
    }
}

@Composable
private fun InfoCard(modifier: Modifier, title: String, value: String, cost: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = color.copy(alpha = 0.05f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = color)
            Text(cost, fontSize = 11.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UtilityUsageScreenPreview() {
    PropertyOSTheme {
        UtilityUsageScreen({})
    }
}
