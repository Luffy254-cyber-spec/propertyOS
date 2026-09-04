package com.him.landlordtenant.app.ui.screens.tenant.communication

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.screens.communication.ChatDetailContent
import com.him.landlordtenant.app.ui.viewmodel.communication.ChatViewModel
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@Composable
fun TenantChatScreen(
    currentUserId: String,
    partnerId: String? = null,
    onBack: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val conversation by viewModel.currentConversation.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val isPartnerOnline by viewModel.isPartnerOnline.collectAsState()
    val partnerLastSeen by viewModel.partnerLastSeen.collectAsState()
    val partnerProfile by viewModel.partnerProfile.collectAsState()

    LaunchedEffect(partnerId) {
        val conversationId = if (partnerId != null) {
            val isDirectId = partnerId.contains("_") || partnerId.startsWith("prop_")
            if (isDirectId) {
                partnerId
            } else {
                if (currentUserId < partnerId) "${currentUserId}_${partnerId}" else "${partnerId}_${currentUserId}"
            }
        } else {
            "tenant_landlord_chat_placeholder"
        }
        viewModel.selectConversation(conversationId, partnerId)
    }

    ChatDetailContent(
        conversation = conversation,
        messages = messages,
        isPartnerOnline = isPartnerOnline,
        partnerLastSeen = partnerLastSeen,
        partnerProfile = partnerProfile,
        currentUserId = currentUserId,
        onBack = onBack,
        onSendMessage = { text, replyTo -> viewModel.sendMessage(text, replyTo = replyTo) },
        onInitiateCall = { isVideo -> viewModel.initiateCall(conversation?.id ?: "", conversation?.name ?: "", isVideo) },
        onSetTyping = { viewModel.setTyping(it) },
        onSetRecording = { viewModel.setRecording(it) },
        onAddReaction = { msgId, emoji -> viewModel.addReaction(msgId, emoji) },
        onDeleteMessage = { msgId, forEveryone -> 
            if (forEveryone) viewModel.deleteMessage(msgId, true)
            else viewModel.deleteMessageForMe(msgId)
        },
        onClearChat = { viewModel.clearChat() },
        onMuteNotifications = { viewModel.muteNotifications(it) },
        onBlockUser = { viewModel.blockUser() },
        onReportUser = { viewModel.reportUser(it) },
        onDeleteChat = { viewModel.deleteChat(); onBack() },
        onSetDisappearingMessages = { viewModel.setDisappearingMessages(it) },
        onSetNickname = { viewModel.setNickname(it) }
    )
}

@Preview(showBackground = true)
@Composable
fun TenantChatScreenPreview() {
    PropertyOSTheme {
        ChatDetailContent(
            conversation = null,
            messages = emptyList(),
            isPartnerOnline = true,
            partnerLastSeen = 0L,
            partnerProfile = null,
            currentUserId = "user123",
            onBack = {},
            onSendMessage = { _, _ -> },
            onInitiateCall = {},
            onSetTyping = {},
            onSetRecording = {},
            onAddReaction = { _, _ -> },
            onDeleteMessage = { _, _ -> }
        )
    }
}
