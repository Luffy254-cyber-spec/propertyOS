package com.him.landlordtenant.app.ui.screens.notifications

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.TenantNotificationUIModel

@Composable
fun NotificationCenterScreen(
    notifications: List<TenantNotificationUIModel> = emptyList(),
    onBack: () -> Unit,
    onNotificationClick: (TenantNotificationUIModel) -> Unit,
    onClearAll: () -> Unit
) {
    // Reuse general notifications list
    NotificationsScreen(
        notifications = notifications,
        onBack = onBack,
        onNotificationClick = onNotificationClick,
        onClearAll = onClearAll
    )
}

@Preview(showBackground = true)
@Composable
fun NotificationCenterScreenPreview() {
    PropertyOSTheme {
        NotificationCenterScreen(onBack = {}, onNotificationClick = {}, onClearAll = {})
    }
}
