package com.him.landlordtenant.app.ui.screens.landlord.communication

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.communication.TenantChatScreen
import com.him.landlordtenant.app.ui.screens.tenant.ChatRoom

import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.viewmodel.communication.ChatViewModel

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import com.him.landlordtenant.app.ui.screens.communication.ChatDetailContent

@Composable
fun TenantGroupChatScreen(
    onBack: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val conversation by viewModel.currentConversation.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val isPartnerOnline by viewModel.isPartnerOnline.collectAsState()
    val partnerProfile by viewModel.partnerProfile.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.selectConversation("apartment_group_chat_placeholder")
    }

    ChatDetailContent(
        conversation = conversation,
        messages = messages,
        isPartnerOnline = isPartnerOnline,
        partnerProfile = partnerProfile,
        currentUserId = viewModel.currentUserId,
        onBack = onBack,
        onSendMessage = { viewModel.sendMessage(it) },
        onInitiateCall = { isVideo -> viewModel.initiateCall(conversation?.id ?: "", conversation?.name ?: "", isVideo) },
        onSetTyping = { viewModel.setTyping(it) },
        onSetRecording = { viewModel.setRecording(it) },
        onAddReaction = { msgId, emoji -> viewModel.addReaction(msgId, emoji) },
        onDeleteMessage = { msgId -> viewModel.deleteMessage(msgId, true) }
    )
}

@Preview(showBackground = true)
@Composable
fun TenantGroupChatScreenPreview() {
    PropertyOSTheme {
        ChatDetailContent(
            conversation = null,
            messages = emptyList(),
            isPartnerOnline = false,
            partnerProfile = null,
            currentUserId = "user123",
            onBack = {},
            onSendMessage = {},
            onInitiateCall = {},
            onSetTyping = {},
            onSetRecording = {},
            onAddReaction = { _, _ -> },
            onDeleteMessage = {}
        )
    }
}
