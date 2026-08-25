package com.him.landlordtenant.app.ui.screens.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.TenantNotificationUIModel
import com.him.landlordtenant.app.ui.screens.tenant.NotificationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationDetailsScreen(
    notification: TenantNotificationUIModel,
    onBack: () -> Unit,
    onDelete: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notification") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, "Delete")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text(
                text = notification.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = notification.time,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
            
            Text(
                text = notification.message,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            if (notification.action != com.him.landlordtenant.app.ui.screens.tenant.NotificationAction.NONE) {
                Button(
                    onClick = { /* Handle action */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View Details")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationDetailsScreenPreview() {
    PropertyOSTheme {
        NotificationDetailsScreen(
            notification = TenantNotificationUIModel(
                id = "1",
                title = "Rent Reminder",
                message = "Your rent for August is due in 3 days. Please settle it via the app.",
                type = NotificationType.RENT,
                time = "2 hours ago"
            ),
            onBack = {},
            onDelete = {}
        )
    }
}
