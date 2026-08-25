package com.him.landlordtenant.app.ui.viewmodel.communication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.data.model.communication.*
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.ChatRepository
import com.him.landlordtenant.app.interfaces.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _conversations = MutableStateFlow<List<ChatConversation>>(emptyList())
    val conversations: StateFlow<List<ChatConversation>> = _conversations.asStateFlow()

    private val _currentConversation = MutableStateFlow<ChatConversation?>(null)
    val currentConversation: StateFlow<ChatConversation?> = _currentConversation.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isPartnerOnline = MutableStateFlow(false)
    val isPartnerOnline: StateFlow<Boolean> = _isPartnerOnline.asStateFlow()

    private val _activeCall = MutableStateFlow<CallSession?>(null)
    val activeCall: StateFlow<CallSession?> = _activeCall.asStateFlow()

    val currentUserId = authRepository.getCurrentUserId() ?: ""
    val currentUserName = authRepository.getCurrentUserName() ?: "User"

    init {
        loadConversations()
        observeActiveCalls()
    }

    private fun loadConversations() {
        chatRepository.getConversations(currentUserId)
            .onEach { _conversations.value = it }
            .launchIn(viewModelScope)
    }

    private fun observeActiveCalls() {
        chatRepository.getActiveCall(currentUserId)
            .onEach { _activeCall.value = it }
            .launchIn(viewModelScope)
    }

    private val _partnerProfile = MutableStateFlow<com.him.landlordtenant.app.data.model.User?>(null)
    val partnerProfile: StateFlow<com.him.landlordtenant.app.data.model.User?> = _partnerProfile.asStateFlow()

    fun selectConversation(conversationId: String, partnerId: String? = null) {
        viewModelScope.launch {
            var actualId = conversationId
            
            // If it's a direct chat between tenant and landlord, ensure it exists
            if (partnerId != null && conversationId.contains("placeholder")) {
                chatRepository.getOrCreateDirectConversation(currentUserId, partnerId).onSuccess {
                    actualId = it
                }
            }

            chatRepository.observeChatConversation(actualId)
                .onEach { conv -> 
                    _currentConversation.value = conv
                    val pid = partnerId ?: conv.participants.find { it.id != currentUserId }?.id
                    pid?.let { fetchPartnerProfile(it) }
                }
                .launchIn(viewModelScope)

            chatRepository.getChatMessages(actualId)
                .onEach { _messages.value = it }
                .launchIn(viewModelScope)

            val pid = partnerId ?: _currentConversation.value?.participants?.find { it.id != currentUserId }?.id
            pid?.let { id ->
                chatRepository.getOnlineStatus(id)
                    .onEach { _isPartnerOnline.value = it }
                    .launchIn(viewModelScope)
            }

            chatRepository.setOnlineStatus(currentUserId, true)
            chatRepository.markAsRead(actualId, currentUserId)
        }
    }

    private fun fetchPartnerProfile(userId: String) {
        viewModelScope.launch {
            val user = userRepository.getUserById(userId)
            _partnerProfile.value = user
        }
    }

    fun sendMessage(
        text: String,
        type: MessageType = MessageType.TEXT,
        attachments: List<ChatAttachment> = emptyList(),
        replyTo: ChatReplyReference? = null
    ) {
        val conversationId = _currentConversation.value?.id ?: return
        
        val tempId = "temp_${System.currentTimeMillis()}"
        val chatMessage = ChatMessage(
            id = tempId,
            conversationId = conversationId,
            senderId = currentUserId,
            senderName = currentUserName,
            text = text,
            type = type,
            attachments = attachments,
            replyTo = replyTo,
            status = MessageStatus.SENDING,
            timestamp = System.currentTimeMillis()
        )
        
        // Optimistic update: add to local list immediately
        _messages.value = listOf(chatMessage) + _messages.value
        
        viewModelScope.launch {
            chatRepository.sendMessage(chatMessage).onFailure {
                // Update status to FAILED in local list
                _messages.value = _messages.value.map { 
                    if (it.id == tempId) it.copy(status = MessageStatus.FAILED) else it 
                }
            }
        }
    }

    fun setTyping(isTyping: Boolean) {
        val conversationId = _currentConversation.value?.id ?: return
        viewModelScope.launch {
            chatRepository.setTypingStatus(conversationId, currentUserId, isTyping)
        }
    }

    fun setRecording(isRecording: Boolean) {
        val conversationId = _currentConversation.value?.id ?: return
        viewModelScope.launch {
            chatRepository.setRecordingStatus(conversationId, currentUserId, isRecording)
        }
    }

    fun addReaction(messageId: String, emoji: String) {
        viewModelScope.launch {
            chatRepository.addReaction(messageId, emoji, currentUserId)
        }
    }

    fun deleteMessage(messageId: String, forEveryone: Boolean) {
        viewModelScope.launch {
            chatRepository.deleteMessage(messageId, forEveryone)
        }
    }

    fun pinMessage(messageId: String, isPinned: Boolean) {
        viewModelScope.launch {
            chatRepository.pinMessage(messageId, isPinned)
        }
    }

    fun initiateCall(receiverId: String, receiverName: String, isVideo: Boolean) {
        val call = CallSession(
            callerId = currentUserId,
            callerName = currentUserName,
            receiverId = receiverId,
            receiverName = receiverName,
            isVideo = isVideo
        )
        viewModelScope.launch {
            chatRepository.initiateCall(call)
        }
    }

    fun endCall() {
        _activeCall.value?.id?.let { callId ->
            viewModelScope.launch {
                chatRepository.endCall(callId)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            chatRepository.setOnlineStatus(currentUserId, false)
        }
    }
}
