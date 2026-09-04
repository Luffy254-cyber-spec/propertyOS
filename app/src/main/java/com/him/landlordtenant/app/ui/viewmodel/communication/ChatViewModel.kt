package com.him.landlordtenant.app.ui.viewmodel.communication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.him.landlordtenant.app.data.model.communication.*
import com.him.landlordtenant.app.interfaces.AuthRepository
import com.him.landlordtenant.app.interfaces.ChatRepository
import com.him.landlordtenant.app.interfaces.DocumentRepository
import com.him.landlordtenant.app.interfaces.PropertyRepository
import com.him.landlordtenant.app.interfaces.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val propertyRepository: PropertyRepository,
    private val documentRepository: DocumentRepository
) : ViewModel() {

    private val _conversations = MutableStateFlow<List<ChatConversation>>(emptyList())
    val conversations: StateFlow<List<ChatConversation>> = _conversations.asStateFlow()

    private val _currentConversation = MutableStateFlow<ChatConversation?>(null)
    val currentConversation: StateFlow<ChatConversation?> = _currentConversation.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isUserBlocked = MutableStateFlow(false)
    val isUserBlocked: StateFlow<Boolean> = _isUserBlocked.asStateFlow()

    private val _isPartnerOnline = MutableStateFlow(false)
    val isPartnerOnline: StateFlow<Boolean> = _isPartnerOnline.asStateFlow()

    private val _partnerLastSeen = MutableStateFlow(0L)
    val partnerLastSeen: StateFlow<Long> = _partnerLastSeen.asStateFlow()

    private val _activeCall = MutableStateFlow<CallSession?>(null)
    val activeCall: StateFlow<CallSession?> = _activeCall.asStateFlow()

    private val _contacts = MutableStateFlow<List<com.him.landlordtenant.app.data.model.User>>(emptyList())
    val contacts: StateFlow<List<com.him.landlordtenant.app.data.model.User>> = _contacts.asStateFlow()

    private val _userProperties = MutableStateFlow<List<com.him.landlordtenant.app.ui.screens.tenant.TenantApartmentUIModel>>(emptyList())
    val userProperties: StateFlow<List<com.him.landlordtenant.app.ui.screens.tenant.TenantApartmentUIModel>> = _userProperties.asStateFlow()

    val currentUserId = authRepository.getCurrentUserId() ?: ""
    val currentUserName = authRepository.getCurrentUserName() ?: "User"

    init {
        loadConversations()
        observeActiveCalls()
        loadContacts()
        loadUserProperties()
    }

    private fun loadUserProperties() {
        viewModelScope.launch {
            val user = userRepository.getUserById(currentUserId)
            if (user?.isLandlord == true) {
                propertyRepository.getPropertiesByOwner(currentUserId).onSuccess { props ->
                    _userProperties.value = props.map { prop ->
                        com.him.landlordtenant.app.ui.screens.tenant.TenantApartmentUIModel(
                            id = prop.id,
                            name = prop.name,
                            county = prop.county ?: "",
                            location = prop.town ?: "",
                            description = prop.description ?: "",
                            availableUnits = prop.availableUnits,
                            totalUnits = prop.totalUnits,
                            startingRent = prop.startingRent,
                            highestRent = prop.startingRent,
                            rating = 4.5,
                            verified = prop.verified,
                            distanceKm = 0.0,
                            houseTypes = emptyList()
                        )
                    }
                }
            } else {
                // For tenants, we can get their apartment from their dashboard/tenancy info
                // For now, let's look at RTDB as a fallback if repo doesn't have a direct "getMyApartment"
                try {
                    val ref = com.google.firebase.database.FirebaseDatabase.getInstance().getReference("tenants/$currentUserId")
                    val snapshot = ref.get().await()
                    val apartmentId = snapshot.child("propertyId").getValue(String::class.java)
                    if (apartmentId != null) {
                        propertyRepository.getProperty(apartmentId).onSuccess { prop ->
                            _userProperties.value = listOf(
                                com.him.landlordtenant.app.ui.screens.tenant.TenantApartmentUIModel(
                                    id = prop.id,
                                    name = prop.name,
                                    county = prop.county ?: "",
                                    location = prop.town ?: "",
                                    description = prop.description ?: "",
                                    availableUnits = prop.availableUnits,
                                    totalUnits = prop.totalUnits,
                                    startingRent = prop.startingRent,
                                    highestRent = prop.startingRent,
                                    rating = 4.5,
                                    verified = prop.verified,
                                    distanceKm = 0.0,
                                    houseTypes = emptyList()
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.e("ChatViewModel", "Error loading tenant property", e)
                }
            }
        }
    }

    private fun loadContacts() {
        userRepository.getUsers()
            .onEach { allUsers ->
                _contacts.value = allUsers.filter { it.id != currentUserId }
            }
            .catch { e -> Log.e("ChatViewModel", "Error loading contacts", e) }
            .launchIn(viewModelScope)
    }

    private fun loadConversations() {
        chatRepository.getConversations(currentUserId)
            .map { list ->
                list.map { conv ->
                    if (!conv.isGroup) {
                        val partner = conv.participants.find { it.id != currentUserId }
                        conv.copy(name = partner?.name ?: conv.name)
                    } else {
                        conv
                    }
                }
            }
            .onEach { _conversations.value = it }
            .catch { e -> 
                Log.e("ChatViewModel", "Chat list error: ${e.message}")
            }
            .launchIn(viewModelScope)
    }

    private fun observeActiveCalls() {
        chatRepository.getActiveCall(currentUserId)
            .onEach { _activeCall.value = it }
            .catch { e -> Log.e("ChatViewModel", "Error observing active calls", e) }
            .launchIn(viewModelScope)
    }

    private val _partnerProfile = MutableStateFlow<com.him.landlordtenant.app.data.model.User?>(null)
    val partnerProfile: StateFlow<com.him.landlordtenant.app.data.model.User?> = _partnerProfile.asStateFlow()

    fun selectConversation(conversationId: String, partnerId: String? = null) {
        viewModelScope.launch {
            var actualId = conversationId
            
            // If it's a direct chat between tenant and landlord, ensure it exists
            if (partnerId != null && (conversationId.contains("placeholder") || !conversationId.contains("_"))) {
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
                .catch { e -> Log.e("ChatViewModel", "Error observing conversation", e) }
                .launchIn(viewModelScope)

            chatRepository.getChatMessages(actualId)
                .onEach { msgs -> 
                    // Filter out messages deleted for the current user
                    val filteredMsgs = msgs.filter { it.deletedForUsers[currentUserId] != true }
                    _messages.value = filteredMsgs
                    
                    // Mark as read if screen is active and we have new messages from partner
                    if (filteredMsgs.any { it.senderId != currentUserId && !it.readBy.containsKey(currentUserId) }) {
                        chatRepository.markAsRead(actualId, currentUserId)
                    }
                }
                .catch { e -> Log.e("ChatViewModel", "Error loading messages", e) }
                .launchIn(viewModelScope)

            val pid = partnerId ?: _currentConversation.value?.participants?.find { it.id != currentUserId }?.id
            pid?.let { id ->
                chatRepository.getOnlineStatus(id)
                    .onEach { _isPartnerOnline.value = it }
                    .catch { e -> Log.e("ChatViewModel", "Error observing online status", e) }
                    .launchIn(viewModelScope)
                
                chatRepository.getLastSeen(id)
                    .onEach { _partnerLastSeen.value = it }
                    .catch { e -> Log.e("ChatViewModel", "Error observing last seen", e) }
                    .launchIn(viewModelScope)

                chatRepository.isUserBlocked(currentUserId, id)
                    .onEach { _isUserBlocked.value = it }
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
        
        viewModelScope.launch {
            val uploadedAttachments = attachments.map { attachment ->
                if (attachment.url.startsWith("content://") || attachment.url.startsWith("file://")) {
                    documentRepository.uploadImage(attachment.url).getOrNull()?.let { remoteUrl ->
                        attachment.copy(url = remoteUrl)
                    } ?: attachment
                } else {
                    attachment
                }
            }

            val tempId = "msg_${System.currentTimeMillis()}"
            val chatMessage = ChatMessage(
                id = tempId,
                conversationId = conversationId,
                senderId = currentUserId,
                senderName = currentUserName,
                text = text,
                type = type,
                attachments = uploadedAttachments,
                replyTo = replyTo,
                status = MessageStatus.SENDING,
                timestamp = System.currentTimeMillis()
            )
            
            // Optimistic update
            _messages.value = listOf(chatMessage) + _messages.value
            
            chatRepository.sendMessage(chatMessage).onFailure {
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
        val conversationId = _currentConversation.value?.id ?: return
        viewModelScope.launch {
            chatRepository.addReaction(conversationId, messageId, emoji, currentUserId)
        }
    }

    fun pinMessage(messageId: String, pinned: Boolean) {
        val conversationId = _currentConversation.value?.id ?: return
        viewModelScope.launch {
            chatRepository.pinMessage(conversationId, messageId, pinned)
        }
    }

    fun deleteMessage(messageId: String, forEveryone: Boolean) {
        val conversationId = _currentConversation.value?.id ?: return
        viewModelScope.launch {
            chatRepository.deleteMessage(conversationId, messageId, forEveryone)
        }
    }

    fun clearChat() {
        val conversationId = _currentConversation.value?.id ?: return
        viewModelScope.launch {
            chatRepository.clearChat(conversationId, currentUserId)
        }
    }

    fun editMessage(messageId: String, newText: String) {
        val conversationId = _currentConversation.value?.id ?: return
        viewModelScope.launch {
            chatRepository.editMessage(conversationId, messageId, newText)
        }
    }

    fun toggleStarMessage(messageId: String) {
        val conversationId = _currentConversation.value?.id ?: return
        viewModelScope.launch {
            chatRepository.toggleStarMessage(conversationId, messageId, currentUserId)
        }
    }

    fun deleteChat() {
        val conversationId = _currentConversation.value?.id ?: return
        viewModelScope.launch {
            chatRepository.deleteChat(conversationId, currentUserId)
        }
    }

    fun deleteMessageForMe(messageId: String) {
        val conversationId = _currentConversation.value?.id ?: return
        viewModelScope.launch {
            chatRepository.deleteMessageForUser(conversationId, messageId, currentUserId)
        }
    }

    fun forwardMessage(message: ChatMessage, targetConversationIds: List<String>) {
        viewModelScope.launch {
            targetConversationIds.forEach { targetId ->
                val forwardedMsg = message.copy(
                    id = "msg_${System.currentTimeMillis()}_${targetId}",
                    conversationId = targetId,
                    senderId = currentUserId,
                    senderName = currentUserName,
                    timestamp = System.currentTimeMillis(),
                    forwarded = true,
                    status = MessageStatus.SENT,
                    reactions = emptyList(),
                    replyTo = null
                )
                chatRepository.sendMessage(forwardedMsg)
            }
        }
    }

    fun sendLocation(lat: Double, lng: Double) {
        sendMessage(
            text = "Shared Location",
            type = MessageType.LOCATION,
            attachments = listOf(ChatAttachment(latitude = lat, longitude = lng))
        )
    }

    fun blockUser() {
        val partnerId = _currentConversation.value?.participants?.find { it.id != currentUserId }?.id ?: return
        viewModelScope.launch {
            chatRepository.blockUser(currentUserId, partnerId)
        }
    }

    fun unblockUser() {
        val partnerId = _currentConversation.value?.participants?.find { it.id != currentUserId }?.id ?: return
        viewModelScope.launch {
            chatRepository.unblockUser(currentUserId, partnerId)
        }
    }

    fun reportUser(reason: String) {
        val partnerId = _currentConversation.value?.participants?.find { it.id != currentUserId }?.id ?: return
        viewModelScope.launch {
            chatRepository.reportUser(currentUserId, partnerId, reason)
        }
    }

    fun setChatTheme(themeId: String) {
        val conversationId = _currentConversation.value?.id ?: return
        viewModelScope.launch {
            chatRepository.setChatTheme(conversationId, currentUserId, themeId)
        }
    }

    fun setChatWallpaper(url: String) {
        val conversationId = _currentConversation.value?.id ?: return
        viewModelScope.launch {
            chatRepository.setChatWallpaper(conversationId, currentUserId, url)
        }
    }

    fun muteNotifications(muted: Boolean) {
        val conversationId = _currentConversation.value?.id ?: return
        viewModelScope.launch {
            chatRepository.muteNotifications(conversationId, currentUserId, muted)
        }
    }

    fun setDisappearingMessages(durationMs: Long) {
        val conversationId = _currentConversation.value?.id ?: return
        viewModelScope.launch {
            chatRepository.setDisappearingMessages(conversationId, durationMs)
        }
    }

    fun setNickname(nickname: String) {
        val conversationId = _currentConversation.value?.id ?: return
        // Usually you set partner's nickname for you to see
        val partnerId = _currentConversation.value?.participants?.find { it.id != currentUserId }?.id ?: return
        viewModelScope.launch {
            chatRepository.setNickname(conversationId, partnerId, nickname)
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            chatRepository.markAllAsRead(currentUserId)
        }
    }

    fun pinConversation(conversationId: String, pinned: Boolean) {
        viewModelScope.launch {
            chatRepository.pinConversation(conversationId, currentUserId, pinned)
        }
    }

    fun muteConversation(conversationId: String, muted: Boolean) {
        viewModelScope.launch {
            chatRepository.muteNotifications(conversationId, currentUserId, muted)
        }
    }

    fun deleteConversation(conversationId: String) {
        viewModelScope.launch {
            chatRepository.deleteChat(conversationId, currentUserId)
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
