package com.him.landlordtenant.app.ui.viewmodel.communication

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import com.him.landlordtenant.app.data.model.User
import com.him.landlordtenant.app.data.model.communication.*
import com.him.landlordtenant.app.interfaces.*
import com.him.landlordtenant.app.ui.screens.communication.CallActivity
import com.him.landlordtenant.app.ui.screens.tenant.TenantApartmentUIModel
import com.him.landlordtenant.app.util.AlertManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val propertyRepository: PropertyRepository,
    private val documentRepository: DocumentRepository,
    private val alertManager: AlertManager,
    private val landlordRepository: LandlordRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _conversations = MutableStateFlow<List<ChatConversation>>(emptyList())
    val conversations: StateFlow<List<ChatConversation>> = _conversations.asStateFlow()

    private val _currentConversation = MutableStateFlow<ChatConversation?>(null)
    val currentConversation: StateFlow<ChatConversation?> = _currentConversation.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _activeCall = MutableStateFlow<CallSession?>(null)
    val activeCall: StateFlow<CallSession?> = _activeCall.asStateFlow()

    private val _isPartnerOnline = MutableStateFlow(false)
    val isPartnerOnline: StateFlow<Boolean> = _isPartnerOnline.asStateFlow()

    private val _partnerLastSeen = MutableStateFlow(0L)
    val partnerLastSeen: StateFlow<Long> = _partnerLastSeen.asStateFlow()

    private val _isUserBlocked = MutableStateFlow(false)
    val isUserBlocked: StateFlow<Boolean> = _isUserBlocked.asStateFlow()

    private val _contacts = MutableStateFlow<List<User>>(emptyList())
    val contacts: StateFlow<List<User>> = _contacts.asStateFlow()

    private val _userProperties = MutableStateFlow<List<TenantApartmentUIModel>>(emptyList())
    val userProperties: StateFlow<List<TenantApartmentUIModel>> = _userProperties.asStateFlow()

    private val _tenants = MutableStateFlow<List<TenantSummaryData>>(emptyList())
    val tenants: StateFlow<List<TenantSummaryData>> = _tenants.asStateFlow()

    private val _partnerProfile = MutableStateFlow<User?>(null)
    val partnerProfile: StateFlow<User?> = _partnerProfile.asStateFlow()

    private val _currentUserId = MutableStateFlow(authRepository.getCurrentUserId() ?: "")
    val currentUserId: String get() = _currentUserId.value

    private val _currentUserName = MutableStateFlow(authRepository.getCurrentUserName() ?: "User")
    val currentUserName: String get() = _currentUserName.value

    private var messagesJob: Job? = null
    private var conversationJob: Job? = null
    private var partnerStatusJob: Job? = null
    private var activeCallJob: Job? = null

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.observeAuthenticationState().collectLatest { isAuthenticated ->
                if (isAuthenticated) {
                    val uid = authRepository.getCurrentUserId() ?: ""
                    _currentUserId.value = uid
                    _currentUserName.value = authRepository.getCurrentUserName() ?: "User"
                    
                    if (uid.isNotEmpty()) {
                        loadInitialData()
                        chatRepository.setOnlineStatus(uid, true)
                        observeUserGroupsAndCalls()
                    }
                } else {
                    clearAllState()
                }
            }
        }
    }

    private fun loadInitialData() {
        loadConversations()
        loadContacts()
        loadUserProperties()
        loadTenants()
    }

    private fun clearAllState() {
        _currentUserId.value = ""
        _currentUserName.value = "User"
        _conversations.value = emptyList()
        _messages.value = emptyList()
        _currentConversation.value = null
        _activeCall.value = null
        _tenants.value = emptyList()
        messagesJob?.cancel()
        conversationJob?.cancel()
        partnerStatusJob?.cancel()
        activeCallJob?.cancel()
    }

    fun loadTenants() {
        viewModelScope.launch {
            val user = userRepository.getUserById(currentUserId)
            if (user?.isLandlord == true) {
                landlordRepository.getTenants(currentUserId).onSuccess { _tenants.value = it }
            }
        }
    }

    private fun loadUserProperties() {
        viewModelScope.launch {
            val user = userRepository.getUserById(currentUserId)
            if (user?.isLandlord == true) {
                propertyRepository.getPropertiesByOwner(currentUserId).onSuccess { props ->
                    _userProperties.value = props.map { it.toUIModel() }
                }
            } else {
                try {
                    val ref = FirebaseDatabase.getInstance().getReference("memberships/$currentUserId")
                    val snapshot = ref.get().await()
                    val apartmentId = snapshot.child("apartmentId").getValue(String::class.java)
                    if (apartmentId != null) {
                        propertyRepository.getProperty(apartmentId).onSuccess { prop ->
                            _userProperties.value = listOf(prop.toUIModel())
                        }
                    }
                } catch (e: Exception) { Log.e("ChatVM", "Error loading tenant prop", e) }
            }
        }
    }

    private fun PropertyDetailsData.toUIModel() = TenantApartmentUIModel(
        id = id, name = name, county = county ?: "", location = town ?: "", description = description ?: "",
        availableUnits = availableUnits, totalUnits = totalUnits, startingRent = startingRent,
        highestRent = startingRent, rating = 4.5, verified = verified, distanceKm = 0.0, houseTypes = emptyList()
    )

    private fun loadContacts() {
        userRepository.getUsers()
            .onEach { users -> _contacts.value = users.filter { it.id != currentUserId } }
            .catch { Log.e("ChatVM", "Error loading contacts", it) }
            .launchIn(viewModelScope)
    }

    private fun loadConversations() {
        chatRepository.getConversations(currentUserId)
            .map { list ->
                list.map { conv ->
                    if (!conv.isGroup) {
                        val partner = conv.participants.find { it.id != currentUserId }
                        conv.copy(name = partner?.name ?: conv.name)
                    } else conv
                }
            }
            .onEach { _conversations.value = it }
            .catch { Log.e("ChatVM", "Conversations error", it) }
            .launchIn(viewModelScope)
    }

    private fun observeUserGroupsAndCalls() {
        viewModelScope.launch {
            _userProperties.collectLatest { props ->
                val groupIds = props.flatMap { listOf("comm_${it.id}", "tenant_${it.id}") }
                activeCallJob?.cancel()
                activeCallJob = chatRepository.getActiveCall(currentUserId, groupIds)
                    .onEach { _activeCall.value = it }
                    .catch { Log.e("ChatVM", "Active call error", it) }
                    .launchIn(this)
            }
        }
    }

    fun selectConversation(conversationId: String, partnerId: String? = null) {
        messagesJob?.cancel()
        conversationJob?.cancel()
        partnerStatusJob?.cancel()
        
        _messages.value = emptyList()
        _currentConversation.value = null
        _partnerProfile.value = null

        if (conversationId.isEmpty()) return

        viewModelScope.launch {
            var actualId = conversationId
            if (partnerId != null && !conversationId.startsWith("direct_") && !conversationId.startsWith("comm_") && !conversationId.startsWith("tenant_")) {
                chatRepository.getOrCreateDirectConversation(currentUserId, partnerId).onSuccess { actualId = it }
            }

            conversationJob = chatRepository.observeChatConversation(actualId)
                .onEach { conv ->
                    _currentConversation.value = conv
                    val pid = partnerId ?: conv.participants.find { it.id != currentUserId }?.id
                    pid?.let { fetchPartnerProfile(it) }
                }
                .catch { Log.e("ChatVM", "Observe conv error", it) }
                .launchIn(viewModelScope)

            messagesJob = chatRepository.getChatMessages(actualId)
                .onEach { msgs ->
                    val filteredMsgs = msgs.filter { it.deletedForUsers[currentUserId] != true }
                    val currentOptimistic = _messages.value.filter { it.status == com.him.landlordtenant.app.data.model.communication.MessageStatus.SENDING || it.status == com.him.landlordtenant.app.data.model.communication.MessageStatus.FAILED }
                    val unsyncedOptimistic = currentOptimistic.filter { opt -> filteredMsgs.none { it.text == opt.text && Math.abs(it.timestamp - opt.timestamp) < 5000 } }
                    _messages.value = (unsyncedOptimistic + filteredMsgs).sortedByDescending { it.timestamp }
                    
                    if (filteredMsgs.any { it.senderId != currentUserId && !it.readBy.containsKey(currentUserId) }) {
                        chatRepository.markAsRead(actualId, currentUserId)
                    }
                }
                .catch { Log.e("ChatVM", "Messages error", it) }
                .launchIn(viewModelScope)

            val targetPid = partnerId ?: _currentConversation.value?.participants?.find { it.id != currentUserId }?.id
            targetPid?.let { tid ->
                partnerStatusJob = combine(
                    chatRepository.getOnlineStatus(tid),
                    chatRepository.getLastSeen(tid),
                    chatRepository.isUserBlocked(currentUserId, tid)
                ) { online, lastSeen, blocked ->
                    _isPartnerOnline.value = online
                    _partnerLastSeen.value = lastSeen
                    _isUserBlocked.value = blocked
                }.launchIn(viewModelScope)
            }
            chatRepository.markAsRead(actualId, currentUserId)
        }
    }

    private fun fetchPartnerProfile(userId: String) {
        viewModelScope.launch { userRepository.getUserById(userId)?.let { _partnerProfile.value = it } }
    }

    fun sendMessage(text: String, type: com.him.landlordtenant.app.data.model.communication.MessageType = com.him.landlordtenant.app.data.model.communication.MessageType.TEXT, attachments: List<ChatAttachment> = emptyList(), replyTo: ChatReplyReference? = null) {
        val cid = _currentConversation.value?.id ?: return
        viewModelScope.launch {
            val uploaded = attachments.map { 
                if (it.url.startsWith("content://") || it.url.startsWith("file://")) {
                    documentRepository.uploadImage(it.url).getOrNull()?.let { remote -> it.copy(url = remote) } ?: it
                } else it
            }
            val tempId = "msg_${System.currentTimeMillis()}"
            val msg = ChatMessage(id = tempId, conversationId = cid, senderId = currentUserId, senderName = currentUserName, text = text, type = type, attachments = uploaded, replyTo = replyTo, status = com.him.landlordtenant.app.data.model.communication.MessageStatus.SENDING, timestamp = System.currentTimeMillis())
            _messages.value = listOf(msg) + _messages.value
            chatRepository.sendMessage(msg).onFailure {
                _messages.value = _messages.value.map { if (it.id == tempId) it.copy(status = com.him.landlordtenant.app.data.model.communication.MessageStatus.FAILED) else it }
                alertManager.showToast("Failed to send message")
            }
        }
    }

    fun initiateCall(receiverId: String, receiverName: String, isVideo: Boolean) {
        viewModelScope.launch {
            try {
                val connected = FirebaseDatabase.getInstance().getReference(".info/connected").get().await().getValue(Boolean::class.java) ?: false
                if (!connected) { alertManager.showToast("Check your connection"); return@launch }

                val isGroup = receiverId.startsWith("comm_") || receiverId.startsWith("tenant_")
                val isOnline = isGroup || (FirebaseDatabase.getInstance().getReference("presence/$receiverId/online").get().await().getValue(Boolean::class.java) ?: false)
                val status = if (isOnline) com.him.landlordtenant.app.data.model.communication.CallStatus.RINGING else com.him.landlordtenant.app.data.model.communication.CallStatus.DIALING

                val call = CallSession(callerId = currentUserId, callerName = currentUserName, receiverId = receiverId, receiverName = receiverName, isVideo = isVideo, status = status, startTime = System.currentTimeMillis())
                chatRepository.initiateCall(call).onSuccess { callId ->
                    val intent = Intent(context, CallActivity::class.java).apply {
                        putExtra("CALL_ID", callId); putExtra("IS_VIDEO_CALL", isVideo); putExtra("IS_CALLER", true); flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                }
            } catch (e: Exception) { alertManager.showToast("Call failed") }
        }
    }

    fun endCall() {
        _activeCall.value?.id?.let { viewModelScope.launch { chatRepository.endCall(it) } }
        _activeCall.value = null
    }

    fun setPresence(isOnline: Boolean) {
        if (currentUserId.isNotEmpty()) viewModelScope.launch { chatRepository.setOnlineStatus(currentUserId, isOnline) }
    }

    fun setTyping(typing: Boolean) { _currentConversation.value?.id?.let { cid -> viewModelScope.launch { chatRepository.setTypingStatus(cid, currentUserId, typing) } } }
    fun setRecording(rec: Boolean) { _currentConversation.value?.id?.let { cid -> viewModelScope.launch { chatRepository.setRecordingStatus(cid, currentUserId, rec) } } }
    fun addReaction(mid: String, emoji: String) { _currentConversation.value?.id?.let { cid -> viewModelScope.launch { chatRepository.addReaction(cid, mid, emoji, currentUserId) } } }
    fun pinMessage(mid: String, pinned: Boolean) { _currentConversation.value?.id?.let { cid -> viewModelScope.launch { chatRepository.pinMessage(cid, mid, pinned) } } }
    fun deleteMessage(mid: String, everyone: Boolean) { _currentConversation.value?.id?.let { cid -> viewModelScope.launch { chatRepository.deleteMessage(cid, mid, everyone) } } }
    fun clearChat() { _currentConversation.value?.id?.let { cid -> viewModelScope.launch { chatRepository.clearChat(cid, currentUserId) } } }
    fun editMessage(mid: String, text: String) { _currentConversation.value?.id?.let { cid -> viewModelScope.launch { chatRepository.editMessage(cid, mid, text) } } }
    fun toggleStarMessage(mid: String) { _currentConversation.value?.id?.let { cid -> viewModelScope.launch { chatRepository.toggleStarMessage(cid, mid, currentUserId) } } }
    fun deleteChat() { _currentConversation.value?.id?.let { cid -> viewModelScope.launch { chatRepository.deleteChat(cid, currentUserId) } } }
    fun deleteMessageForMe(mid: String) { _currentConversation.value?.id?.let { cid -> viewModelScope.launch { chatRepository.deleteMessageForUser(cid, mid, currentUserId) } } }
    fun blockUser() { _partnerProfile.value?.id?.let { pid -> viewModelScope.launch { chatRepository.blockUser(currentUserId, pid) } } }
    fun unblockUser() { _partnerProfile.value?.id?.let { pid -> viewModelScope.launch { chatRepository.unblockUser(currentUserId, pid) } } }
    fun reportUser(reason: String) { _partnerProfile.value?.id?.let { pid -> viewModelScope.launch { chatRepository.reportUser(currentUserId, pid, reason) } } }
    fun setChatTheme(tid: String) { _currentConversation.value?.id?.let { cid -> viewModelScope.launch { chatRepository.setChatTheme(cid, currentUserId, tid) } } }
    fun setChatWallpaper(url: String) { _currentConversation.value?.id?.let { cid -> viewModelScope.launch { chatRepository.setChatWallpaper(cid, currentUserId, url) } } }
    fun muteNotifications(mute: Boolean) { _currentConversation.value?.id?.let { cid -> viewModelScope.launch { chatRepository.muteNotifications(cid, currentUserId, mute) } } }
    fun setDisappearingMessages(dur: Long) { _currentConversation.value?.id?.let { cid -> viewModelScope.launch { chatRepository.setDisappearingMessages(cid, dur) } } }
    fun setNickname(nick: String) { val cid = _currentConversation.value?.id ?: return; val pid = _currentConversation.value?.participants?.find { it.id != currentUserId }?.id ?: return; viewModelScope.launch { chatRepository.setNickname(cid, pid, nick) } }

    fun forwardMessage(msg: ChatMessage, targets: List<String>) {
        viewModelScope.launch { targets.forEach { tid -> chatRepository.sendMessage(msg.copy(id = "msg_${System.currentTimeMillis()}_$tid", conversationId = tid, senderId = currentUserId, senderName = currentUserName, timestamp = System.currentTimeMillis(), forwarded = true, status = com.him.landlordtenant.app.data.model.communication.MessageStatus.SENT, reactions = emptyMap(), replyTo = null)) } }
    }

    fun markAllAsRead() { viewModelScope.launch { chatRepository.markAllAsRead(currentUserId) } }
    fun pinConversation(cid: String, p: Boolean) { viewModelScope.launch { chatRepository.pinConversation(cid, currentUserId, p) } }
    fun muteConversation(cid: String, m: Boolean) { viewModelScope.launch { chatRepository.muteNotifications(cid, currentUserId, m) } }
    fun deleteConversation(cid: String) { viewModelScope.launch { chatRepository.deleteChat(cid, currentUserId) } }

    override fun onCleared() { super.onCleared(); setPresence(false) }
}
