package com.him.landlordtenant.app.ui.screens.landlord.reports

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsDashboardScreen(
    onBack: () -> Unit,
    onRevenueReport: () -> Unit,
    onPaymentReport: () -> Unit,
    onOccupancyReport: () -> Unit,
    onExpensesReport: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reports & Analytics") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { ReportCategoryCard("Revenue Analysis", "Income, arrears and projections.", Icons.Default.TrendingUp, Color(0xFF2E7D32), onRevenueReport) }
            item { ReportCategoryCard("Payment Records", "Detailed transaction history.", Icons.Default.Payments, Color(0xFF1976D2), onPaymentReport) }
            item { ReportCategoryCard("Occupancy Stats", "Vacancies and tenant turnover.", Icons.Default.PieChart, Color(0xFFF57C00), onOccupancyReport) }
            item { ReportCategoryCard("Expense Tracking", "Maintenance and utility costs.", Icons.Default.BarChart, Color.Red, onExpensesReport) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReportsDashboardScreenPreview() {
    PropertyOSTheme {
        ReportsDashboardScreen(onBack = {}, onRevenueReport = {}, onPaymentReport = {}, onOccupancyReport = {}, onExpensesReport = {})
    }
}

@Composable
private fun ReportCategoryCard(title: String, subtitle: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = color, modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}
