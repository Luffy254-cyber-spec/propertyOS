package com.him.landlordtenant.app.ui.screens.landlord.communication

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.screens.communication.ChatDetailScreen
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.viewmodel.communication.ChatViewModel

@Composable
fun TenantGroupChatScreen(
    onBack: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    ChatDetailScreen(
        conversationId = "apartment_group_chat_placeholder",
        onBack = onBack,
        viewModel = viewModel
    )
}

@Preview(showBackground = true)
@Composable
fun TenantGroupChatScreenPreview() {
    PropertyOSTheme {
        TenantGroupChatScreen(onBack = {})
    }
}
