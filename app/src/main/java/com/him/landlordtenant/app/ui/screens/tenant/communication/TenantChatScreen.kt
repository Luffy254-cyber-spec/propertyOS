package com.him.landlordtenant.app.ui.screens.tenant.communication

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.screens.communication.ChatDetailScreen
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.viewmodel.communication.ChatViewModel

@Composable
fun TenantChatScreen(
    currentUserId: String, // Kept for compatibility if needed, but viewModel has it
    partnerId: String? = null,
    onBack: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    ChatDetailScreen(
        conversationId = "",
        partnerId = partnerId,
        onBack = onBack,
        viewModel = viewModel
    )
}

@Preview(showBackground = true)
@Composable
fun TenantChatScreenPreview() {
    PropertyOSTheme {
        TenantChatScreen(currentUserId = "user123", onBack = {})
    }
}
