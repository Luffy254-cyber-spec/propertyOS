package com.him.landlordtenant.app.ui.screens.tenant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

enum class NotificationFilter { ALL, UNREAD, RENT, PAYMENTS, MAINTENANCE, ANNOUNCEMENTS }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantNotificationsScreen(
    notifications: List<TenantNotificationUIModel> = emptyList(),
    onBack: () -> Unit = {},
    onNotificationClick: (TenantNotificationUIModel) -> Unit = {},
    onMarkAsRead: (TenantNotificationUIModel) -> Unit = {},
    onMarkAllAsRead: () -> Unit = {},
    onDeleteNotification: (TenantNotificationUIModel) -> Unit = {},
    onClearAll: () -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf(NotificationFilter.ALL) }
    var showClearDialog by remember { mutableStateOf(false) }
    var selectedNotification by remember { mutableStateOf<TenantNotificationUIModel?>(null) }

    val filteredNotifications = notifications.filter {
        when (selectedFilter) {
            NotificationFilter.ALL -> true
            NotificationFilter.UNREAD -> !it.isRead
            NotificationFilter.RENT -> it.type == NotificationType.RENT
            NotificationFilter.PAYMENTS -> it.type == NotificationType.PAYMENT
            NotificationFilter.MAINTENANCE -> it.type == NotificationType.MAINTENANCE
            NotificationFilter.ANNOUNCEMENTS -> it.type == NotificationType.ANNOUNCEMENT
        }
    }

    val unreadCount = notifications.count { !it.isRead }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "Notifications", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        if (unreadCount > 0) Text(text = "$unreadCount unread", fontSize = 11.sp, color = Color.Gray)
                    }
                },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } },
                actions = {
                    IconButton(onClick = onMarkAllAsRead) { Icon(Icons.Default.MarkEmailRead, "Read All") }
                    IconButton(onClick = { showClearDialog = true }) { Icon(Icons.Default.Delete, "Clear All") }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            NotificationFilters(selectedFilter) { selectedFilter = it }
            if (filteredNotifications.isEmpty()) {
                EmptyNotificationsState()
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(16.dp)) {
                    itemsIndexed(filteredNotifications, key = { _, n -> n.id }) { _, notification ->
                        NotificationItem(
                            notification = notification,
                            onClick = { onMarkAsRead(notification); onNotificationClick(notification) },
                            onDelete = { onDeleteNotification(notification) }
                        )
                    }
                }
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear all notifications?") },
            confirmButton = { TextButton(onClick = { onClearAll(); showClearDialog = false }) { Text("Clear") } },
            dismissButton = { TextButton(onClick = { showClearDialog = false }) { Text("Cancel") } }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TenantNotificationsScreenPreview() {
    PropertyOSTheme {
        TenantNotificationsScreen()
    }
}

@Composable
private fun NotificationFilters(selected: NotificationFilter, onSelected: (NotificationFilter) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        NotificationFilter.values().forEach { filter ->
            FilterChip(selected = selected == filter, onClick = { onSelected(filter) }, label = { Text(filter.name, fontSize = 11.sp) })
        }
    }
}

@Composable
private fun NotificationItem(notification: TenantNotificationUIModel, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = if (notification.isRead) MaterialTheme.colorScheme.surface else Color(0xFFF1F8FF))
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(notificationColor(notification.type).copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                Icon(notificationIcon(notification.type), null, modifier = Modifier.size(20.dp), tint = notificationColor(notification.type))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(notification.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(notification.message, fontSize = 12.sp, color = Color.Gray, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(notification.time, fontSize = 10.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
            }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Close, null, modifier = Modifier.size(16.dp)) }
        }
    }
}

@Composable
private fun EmptyNotificationsState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.NotificationsNone, null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
            Text("No notifications", color = Color.Gray)
        }
    }
}

private fun notificationIcon(type: NotificationType) = when (type) {
    NotificationType.RENT -> Icons.Default.Home
    NotificationType.PAYMENT -> Icons.Default.Payment
    NotificationType.MAINTENANCE -> Icons.Default.Build
    else -> Icons.Default.Notifications
}

private fun notificationColor(type: NotificationType) = when (type) {
    NotificationType.RENT -> Color.Blue
    NotificationType.PAYMENT -> Color(0xFF2E7D32)
    NotificationType.MAINTENANCE -> Color(0xFFF57C00)
    else -> Color.Gray
}
