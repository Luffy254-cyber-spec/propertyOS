package com.him.landlordtenant.app.ui.screens.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    onBack: () -> Unit
) {
    var rentAlerts by remember { mutableStateOf(true) }
    var billAlerts by remember { mutableStateOf(true) }
    var messageAlerts by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notification Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            NotificationToggleItem("Rent Reminders", rentAlerts) { rentAlerts = it }
            NotificationToggleItem("New Bills", billAlerts) { billAlerts = it }
            NotificationToggleItem("Direct Messages", messageAlerts) { messageAlerts = it }
        }
    }
}

@Composable
private fun NotificationToggleItem(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationSettingsScreenPreview() {
    PropertyOSTheme {
        NotificationSettingsScreen({})
    }
}
