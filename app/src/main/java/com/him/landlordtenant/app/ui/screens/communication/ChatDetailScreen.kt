package com.him.landlordtenant.app.ui.screens.communication

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.data.model.communication.*
import com.him.landlordtenant.app.ui.viewmodel.communication.ChatViewModel
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatDetailScreen(
    conversationId: String,
    onBack: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val conversation by viewModel.currentConversation.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val isPartnerOnline by viewModel.isPartnerOnline.collectAsState()
    val partnerLastSeen by viewModel.partnerLastSeen.collectAsState()
    val partnerProfile by viewModel.partnerProfile.collectAsState()
    val allConversations by viewModel.conversations.collectAsState()

    LaunchedEffect(conversationId) {
        viewModel.selectConversation(conversationId)
    }

    ChatDetailContent(
        conversation = conversation,
        messages = messages,
        allConversations = allConversations,
        isPartnerOnline = isPartnerOnline,
        partnerLastSeen = partnerLastSeen,
        partnerProfile = partnerProfile,
        currentUserId = viewModel.currentUserId,
        onBack = onBack,
        onSendMessage = { text, replyTo -> viewModel.sendMessage(text, replyTo = replyTo) },
        onSendMedia = { uri, type -> 
            viewModel.sendMessage(
                text = "Shared ${type.name.lowercase().replaceFirstChar { it.uppercase() }}",
                type = type,
                attachments = listOf(ChatAttachment(url = uri.toString()))
            )
        },
        onSendLocation = { lat, lng -> viewModel.sendLocation(lat, lng) },
        onSendContact = { name, phone -> 
            viewModel.sendMessage(
                text = "Contact: $name",
                type = MessageType.CONTACT,
                attachments = listOf(ChatAttachment(contactName = name, contactPhone = phone))
            )
        },
        onInitiateCall = { isVideo -> viewModel.initiateCall(conversation?.id ?: "", conversation?.name ?: "", isVideo) },
        onSetTyping = { viewModel.setTyping(it) },
        onSetRecording = { viewModel.setRecording(it) },
        onAddReaction = { msgId, emoji -> viewModel.addReaction(msgId, emoji) },
        onPinMessage = { msgId, pinned -> viewModel.pinMessage(msgId, pinned) },
        onForwardMessage = { msg, targets -> viewModel.forwardMessage(msg, targets) },
        onEditMessage = { msgId, newText -> viewModel.editMessage(msgId, newText) },
        onToggleStar = { msgId -> viewModel.toggleStarMessage(msgId) },
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
        onSetNickname = { viewModel.setNickname(it) },
        onSetWallpaper = { viewModel.setChatWallpaper(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailContent(
    conversation: ChatConversation?,
    messages: List<ChatMessage>,
    allConversations: List<ChatConversation> = emptyList(),
    isPartnerOnline: Boolean,
    partnerLastSeen: Long,
    partnerProfile: com.him.landlordtenant.app.data.model.User?,
    currentUserId: String,
    onBack: () -> Unit,
    onSendMessage: (String, ChatReplyReference?) -> Unit,
    onSendMedia: (android.net.Uri, MessageType) -> Unit = { _, _ -> },
    onSendLocation: (Double, Double) -> Unit = { _, _ -> },
    onSendContact: (String, String) -> Unit = { _, _ -> },
    onInitiateCall: (Boolean) -> Unit,
    onSetTyping: (Boolean) -> Unit,
    onSetRecording: (Boolean) -> Unit,
    onAddReaction: (String, String) -> Unit,
    onPinMessage: (String, Boolean) -> Unit = { _, _ -> },
    onForwardMessage: (ChatMessage, List<String>) -> Unit = { _, _ -> },
    onEditMessage: (String, String) -> Unit = { _, _ -> },
    onToggleStar: (String) -> Unit = {},
    onDeleteMessage: (String, Boolean) -> Unit,
    onClearChat: () -> Unit = {},
    onMuteNotifications: (Boolean) -> Unit = {},
    onBlockUser: () -> Unit = {},
    onReportUser: (String) -> Unit = {},
    onDeleteChat: () -> Unit = {},
    onSetDisappearingMessages: (Long) -> Unit = {},
    onSetNickname: (String) -> Unit = {},
    onSetWallpaper: (String) -> Unit = {}
) {
    var messageText by remember { mutableStateOf("") }
    var replyingTo by remember { mutableStateOf<ChatMessage?>(null) }
    var editingMessage by remember { mutableStateOf<ChatMessage?>(null) }
    var showAttachmentDrawer by remember { mutableStateOf(false) }
    var selectedMessage by remember { mutableStateOf<ChatMessage?>(null) }
    var showMessageMenu by remember { mutableStateOf(false) }
    var showReactionPicker by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }
    var showEmojiPicker by remember { mutableStateOf(false) }
    var isSelfRecording by remember { mutableStateOf(false) }
    var showSharedMedia by remember { mutableStateOf(false) }
    var showStarredMessages by remember { mutableStateOf(false) }
    var showDisappearingSettings by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    var showChatSettings by remember { mutableStateOf(false) }
    var showSearch by remember { mutableStateOf(false) }
    var showForwardPicker by remember { mutableStateOf(false) }
    var showContactPicker by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val imagePicker = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { onSendMedia(it, MessageType.IMAGE) }
    }

    Scaffold(
        topBar = {
            if (showSearch) {
                SearchTopBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onClose = { showSearch = false; searchQuery = "" }
                )
            } else {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { 
                                if (conversation?.isGroup == true) {
                                    // showGroupInfoDialog = true
                                } else {
                                    showProfileDialog = true
                                }
                            }
                        ) {
                            Box(modifier = Modifier.size(40.dp)) {
                                Box(
                                    modifier = Modifier.fillMaxSize().clip(CircleShape).background(
                                        Brush.linearGradient(com.him.landlordtenant.app.ui.theme.PremiumGradient)
                                    )
                                ) {
                                    Icon(
                                        if (conversation?.isGroup == true) Icons.Default.Groups else Icons.Default.Person, 
                                        null, 
                                        modifier = Modifier.align(Alignment.Center),
                                        tint = Color.White
                                    )
                                }
                                
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .padding(1.5.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                            .background(if (isPartnerOnline) Color(0xFF4CAF50) else Color.Gray)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                val headerTitle = if (conversation?.isGroup == true) {
                                    conversation.name
                                } else {
                                    partnerProfile?.username?.let { "@$it" } ?: partnerProfile?.fullName ?: conversation?.name ?: "Chat"
                                }
                                
                                Text(
                                    text = headerTitle, 
                                    fontSize = 16.sp, 
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                val partner = conversation?.participants?.find { it.id != currentUserId }
                                AnimatedContent(
                                    targetState = when {
                                        partner?.isTyping == true -> "typing..."
                                        partner?.isRecording == true -> "recording audio..."
                                        isPartnerOnline -> "Online"
                                        partnerLastSeen > 0 -> "Last seen ${formatLastSeen(partnerLastSeen)}"
                                        else -> "Offline"
                                    },
                                    transitionSpec = {
                                        fadeIn(animationSpec = tween(150, 150)) + slideInVertically { it / 2 } togetherWith
                                        fadeOut(animationSpec = tween(150)) + slideOutVertically { -it / 2 }
                                    },
                                    label = "StatusTransition"
                                ) { status ->
                                    Text(
                                        text = status, 
                                        fontSize = 11.sp, 
                                        color = if (status == "Online" || status == "typing..." || status == "recording audio...") Color(0xFF4CAF50) else Color.Gray,
                                        fontWeight = if (status == "Online" || status == "typing...") FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                        }
                    },
                    actions = {
                        IconButton(onClick = { onInitiateCall(false) }) { Icon(Icons.Default.Call, null) }
                        IconButton(onClick = { onInitiateCall(true) }) { Icon(Icons.Default.VideoCall, null) }
                        Box {
                            IconButton(onClick = { showMenu = true }) { Icon(Icons.Default.MoreVert, null) }
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("View contact") },
                                    onClick = { showProfileDialog = true; showMenu = false },
                                    leadingIcon = { Icon(Icons.Default.Person, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Search") },
                                    onClick = { showSearch = true; showMenu = false },
                                    leadingIcon = { Icon(Icons.Default.Search, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Chat settings") },
                                    onClick = { showChatSettings = true; showMenu = false },
                                    leadingIcon = { Icon(Icons.Default.Settings, null) }
                                )
                                val isMuted = conversation?.mutedBy?.get(currentUserId) == true
                                DropdownMenuItem(
                                    text = { Text(if (isMuted) "Unmute notifications" else "Mute notifications") },
                                    onClick = { onMuteNotifications(!isMuted); showMenu = false },
                                    leadingIcon = { Icon(if (isMuted) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Shared media") },
                                    onClick = { showSharedMedia = true; showMenu = false },
                                    leadingIcon = { Icon(Icons.Default.PermMedia, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Starred messages") },
                                    onClick = { showStarredMessages = true; showMenu = false },
                                    leadingIcon = { Icon(Icons.Default.Star, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Disappearing messages") },
                                    onClick = { showDisappearingSettings = true; showMenu = false },
                                    leadingIcon = { Icon(Icons.Default.Timelapse, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Clear chat") },
                                    onClick = { onClearChat(); showMenu = false },
                                    leadingIcon = { Icon(Icons.Default.DeleteSweep, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Delete chat") },
                                    onClick = { onDeleteChat(); showMenu = false },
                                    leadingIcon = { Icon(Icons.Default.DeleteForever, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Export chat") },
                                    onClick = { showMenu = false },
                                    leadingIcon = { Icon(Icons.Default.ImportExport, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Add shortcut") },
                                    onClick = { showMenu = false },
                                    leadingIcon = { Icon(Icons.AutoMirrored.Filled.OpenInNew, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Block") },
                                    onClick = { onBlockUser(); showMenu = false },
                                    leadingIcon = { Icon(Icons.Default.Block, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Report") },
                                    onClick = { showReportDialog = true; showMenu = false },
                                    leadingIcon = { Icon(Icons.Default.Report, null) }
                                )
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            Column {
                if (replyingTo != null) {
                    ReplyPreview(
                        reply = ChatReplyReference(
                            messageId = replyingTo!!.id,
                            senderName = replyingTo!!.senderName,
                            textPreview = replyingTo!!.text,
                            type = replyingTo!!.type
                        ),
                        onCancel = { replyingTo = null }
                    )
                }
                if (editingMessage != null) {
                    EditPreview(
                        text = editingMessage!!.text,
                        onCancel = { 
                            editingMessage = null
                            messageText = ""
                        }
                    )
                }
                ChatInputBar(
                    text = messageText,
                    onTextChange = { 
                        messageText = it
                        onSetTyping(it.isNotEmpty())
                    },
                    onSend = {
                        if (messageText.isNotBlank() || isSelfRecording) {
                            if (editingMessage != null) {
                                onEditMessage(editingMessage!!.id, messageText)
                                editingMessage = null
                            } else {
                                val replyRef = replyingTo?.let {
                                    ChatReplyReference(
                                        messageId = it.id,
                                        senderName = it.senderName,
                                        textPreview = it.text,
                                        type = it.type
                                    )
                                }
                                onSendMessage(messageText, replyRef)
                            }
                            messageText = ""
                            replyingTo = null
                            onSetTyping(false)
                            isSelfRecording = false
                            onSetRecording(false)
                        }
                    },
                    onAttachmentClick = { showAttachmentDrawer = !showAttachmentDrawer },
                    onMicClick = { 
                        isSelfRecording = !isSelfRecording
                        onSetRecording(isSelfRecording)
                    },
                    onEmojiClick = { showEmojiPicker = !showEmojiPicker },
                    isRecording = isSelfRecording,
                    isEditing = editingMessage != null
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Background Wallpaper
            val wallpaper = conversation?.wallpaperUrls?.get(currentUserId)
            if (wallpaper != null) {
                AsyncImage(
                    model = wallpaper,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alpha = 0.2f
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.surface,
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.05f)
                                )
                            )
                        )
                )
            }

            Column(modifier = Modifier.fillMaxSize()) {
                // Pinned Messages Banner
                val pinnedMessages = messages.filter { it.pinned }
                if (pinnedMessages.isNotEmpty()) {
                    PinnedMessagesBanner(
                        message = pinnedMessages.first(),
                        count = pinnedMessages.size,
                        onClick = { 
                            val index = messages.indexOfFirst { it.id == pinnedMessages.first().id }
                            if (index != -1) {
                                scope.launch {
                                    listState.animateScrollToItem(index)
                                }
                            }
                        }
                    )
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    reverseLayout = true
                ) {
                    val filteredMessages = if (searchQuery.isBlank()) messages 
                    else messages.filter { it.text.contains(searchQuery, ignoreCase = true) }
                    
                    items(filteredMessages, key = { it.id.ifEmpty { java.util.UUID.randomUUID().toString() } }) { message ->
                        if (message.type == MessageType.SYSTEM) {
                            SystemMessageItem(message.text)
                        } else {
                            val isCurrentUser = message.senderId == currentUserId
                            
                            var isVisible by remember { mutableStateOf(false) }
                            LaunchedEffect(Unit) { isVisible = true }
                            
                            AnimatedVisibility(
                                visible = isVisible,
                                enter = slideInHorizontally(
                                    initialOffsetX = { if (isCurrentUser) it else -it },
                                    animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
                                ) + fadeIn()
                            ) {
                                PremiumChatBubble(
                                message = message,
                                isCurrentUser = isCurrentUser,
                                currentUserId = currentUserId,
                                onLongClick = {
                                    selectedMessage = message
                                    showMessageMenu = true
                                }
                            )
                            }
                        }
                    }
                }
            }
            
            // Jump to Bottom FAB
            val showJumpToBottom by remember {
                derivedStateOf { listState.firstVisibleItemIndex > 2 }
            }
            if (showJumpToBottom) {
                SmallFloatingActionButton(
                    onClick = { scope.launch { listState.animateScrollToItem(0) } },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 16.dp, end = 16.dp),
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.KeyboardArrowDown, null)
                }
            }

            if (showAttachmentDrawer) {
                AttachmentDrawer(
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp),
                    onAction = { type -> 
                        showAttachmentDrawer = false
                        when(type) {
                            MessageType.IMAGE -> imagePicker.launch("image/*")
                            MessageType.LOCATION -> onSendLocation(-1.286389, 36.817223) // Nairobi center mock
                            MessageType.CONTACT -> showContactPicker = true
                            else -> { /* Other types */ }
                        }
                    }
                )
            }
            
            if (showContactPicker) {
                // For simplicity, using a list of known users as contacts
                ContactPickerDialog(
                    contacts = allConversations.flatMap { it.participants }.distinctBy { it.id }.filter { it.id != currentUserId },
                    onDismiss = { showContactPicker = false },
                    onContactSelected = { name, phone ->
                        onSendContact(name, phone)
                        showContactPicker = false
                    }
                )
            }
            
            if (showEmojiPicker) {
                EmojiPickerDialog(
                    onDismiss = { showEmojiPicker = false },
                    onEmojiSelected = { emoji ->
                        messageText += emoji
                        showEmojiPicker = false
                    }
                )
            }
            
            if (showMessageMenu && selectedMessage != null) {
                MessageOptionsMenu(
                    onDismiss = { showMessageMenu = false },
                    isCurrentUser = selectedMessage!!.senderId == currentUserId,
                    isPinned = selectedMessage!!.pinned,
                    isStarred = selectedMessage!!.starredBy.containsKey(currentUserId),
                    onReply = {
                        replyingTo = selectedMessage
                        showMessageMenu = false
                    },
                    onPin = {
                        onPinMessage(selectedMessage!!.id, !selectedMessage!!.pinned)
                        showMessageMenu = false
                    },
                    onStar = {
                        onToggleStar(selectedMessage!!.id)
                        showMessageMenu = false
                    },
                    onEdit = {
                        editingMessage = selectedMessage
                        messageText = selectedMessage!!.text
                        showMessageMenu = false
                    },
                    onReact = {
                        showReactionPicker = true
                        showMessageMenu = false
                    },
                    onForward = {
                        showForwardPicker = true
                        showMessageMenu = false
                    },
                    onDeleteForMe = {
                        onDeleteMessage(selectedMessage!!.id, false)
                        showMessageMenu = false
                    },
                    onDeleteForEveryone = {
                        onDeleteMessage(selectedMessage!!.id, true)
                        showMessageMenu = false
                    }
                )
            }
            
            if (showReactionPicker && selectedMessage != null) {
                ReactionPicker(
                    onDismiss = { showReactionPicker = false },
                    onReact = { emoji ->
                        onAddReaction(selectedMessage!!.id, emoji)
                        showReactionPicker = false
                        selectedMessage = null
                    }
                )
            }

            if (showForwardPicker && selectedMessage != null) {
                ForwardPickerDialog(
                    conversations = allConversations,
                    onDismiss = { showForwardPicker = false },
                    onForward = { targetIds: List<String> ->
                        onForwardMessage(selectedMessage!!, targetIds)
                        showForwardPicker = false
                        selectedMessage = null
                    }
                )
            }

            if (showProfileDialog && partnerProfile != null) {
                UserProfileDialog(
                    user = partnerProfile as com.him.landlordtenant.app.data.model.User,
                    onDismiss = { showProfileDialog = false }
                )
            }

            if (showSharedMedia) {
                SharedMediaDialog(
                    messages = messages.filter { it.type != MessageType.TEXT && it.attachments.isNotEmpty() },
                    onDismiss = { showSharedMedia = false }
                )
            }

            if (showStarredMessages) {
                StarredMessagesDialog(
                    messages = messages.filter { it.starredBy.containsKey(currentUserId) },
                    onDismiss = { showStarredMessages = false }
                )
            }

            if (showDisappearingSettings) {
                DisappearingMessagesDialog(
                    currentDurationMs = conversation?.disappearingMessagesDuration ?: 0L,
                    onDismiss = { showDisappearingSettings = false },
                    onSelect = { durationMs ->
                        onSetDisappearingMessages(durationMs)
                        showDisappearingSettings = false
                    }
                )
            }

            if (showReportDialog) {
                ReportUserDialog(
                    onDismiss = { showReportDialog = false },
                    onReport = { reason ->
                        onReportUser(reason)
                        showReportDialog = false
                    }
                )
            }

            if (showChatSettings) {
                ChatSettingsDialog(
                    currentNickname = conversation?.nicknames?.get(partnerProfile?.id) ?: "",
                    onDismiss = { showChatSettings = false },
                    onSaveNickname = { onSetNickname(it) },
                    onSetWallpaper = { onSetWallpaper(it) }
                )
            }
        }
    }
}

@Composable
fun PinnedMessagesBanner(
    message: ChatMessage,
    count: Int,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        tonalElevation = 4.dp,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.PushPin, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (count > 1) "Pinned Messages ($count)" else "Pinned Message",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = message.text,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun ForwardPickerDialog(
    conversations: List<ChatConversation>,
    onDismiss: () -> Unit,
    onForward: (List<String>) -> Unit
) {
    val selectedIds = remember { mutableStateListOf<String>() }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Forward to...") },
        text = {
            LazyColumn(modifier = Modifier.height(300.dp)) {
                items(conversations) { conv ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { 
                                if (selectedIds.contains(conv.id)) selectedIds.remove(conv.id)
                                else selectedIds.add(conv.id)
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = selectedIds.contains(conv.id), onCheckedChange = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(conv.name, modifier = Modifier.weight(1f))
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onForward(selectedIds.toList()) },
                enabled = selectedIds.isNotEmpty()
            ) {
                Text("Forward")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun EditPreview(text: String, onCancel: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.width(4.dp).height(40.dp).background(Color(0xFF4CAF50), RoundedCornerShape(2.dp)))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("Edit message", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF4CAF50))
            Text(text, maxLines = 1, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, overflow = TextOverflow.Ellipsis)
        }
        IconButton(onClick = onCancel) { Icon(Icons.Default.Close, null, modifier = Modifier.size(20.dp), tint = Color.Gray) }
    }
}

@Composable
fun ChatSettingsDialog(
    currentNickname: String,
    onDismiss: () -> Unit,
    onSaveNickname: (String) -> Unit,
    onSetWallpaper: (String) -> Unit
) {
    var nickname by remember { mutableStateOf(currentNickname) }
    
    val wallpapers = listOf(
        "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?q=80&w=2070",
        "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?q=80&w=2070",
        "https://images.unsplash.com/photo-1560518883-ce09059eeffa?q=80&w=2073"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Chat Settings") },
        text = {
            Column {
                Text("Nickname", fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Set a nickname for this user") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Chat Wallpaper", fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    wallpapers.forEach { url ->
                        Box(
                            modifier = Modifier
                                .size(60.dp, 80.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray)
                                .clickable { onSetWallpaper(url) }
                        ) {
                            AsyncImage(model = url, contentDescription = null, contentScale = ContentScale.Crop)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onSaveNickname(nickname); onDismiss() }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit
) {
    TopAppBar(
        title = {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search messages...") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
        },
        navigationIcon = {
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, null)
            }
        }
    )
}

@Composable
private fun SystemMessageItem(text: String) {
    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun ContactPickerDialog(
    contacts: List<ChatParticipant>,
    onDismiss: () -> Unit,
    onContactSelected: (String, String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Contact") },
        text = {
            LazyColumn(modifier = Modifier.height(300.dp)) {
                items(contacts) { contact ->
                    ListItem(
                        headlineContent = { Text(contact.name) },
                        leadingContent = { 
                            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                                Text(contact.name.take(1), fontWeight = FontWeight.Bold)
                            }
                        },
                        modifier = Modifier.clickable { onContactSelected(contact.name, "+254 700 000000") } // Mock phone
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun StarredMessagesDialog(
    messages: List<ChatMessage>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().height(500.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Starred Messages", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
                if (messages.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No starred messages", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(messages) { message ->
                            StarredMessageItem(message)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StarredMessageItem(message: ChatMessage) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(message.senderName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                Text(
                    SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date(message.timestamp)),
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(message.text, fontSize = 14.sp)
        }
    }
}

@Composable
fun SharedMediaDialog(
    messages: List<ChatMessage>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().height(500.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Shared Media", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
                if (messages.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No media shared yet", color = Color.Gray)
                    }
                } else {
                    androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                        columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(messages.size) { index ->
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.LightGray)
                            ) {
                                Icon(Icons.Default.Image, null, modifier = Modifier.align(Alignment.Center))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DisappearingMessagesDialog(
    currentDurationMs: Long,
    onDismiss: () -> Unit,
    onSelect: (Long) -> Unit
) {
    val options = listOf(
        "Off" to 0L,
        "24 Hours" to 86400000L,
        "7 Days" to 604800000L,
        "90 Days" to 7776000000L
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Disappearing Messages") },
        text = {
            Column {
                Text("Messages in this chat will disappear after the selected time.")
                Spacer(modifier = Modifier.height(16.dp))
                options.forEach { (label, duration) ->
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { onSelect(duration) }.padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = currentDurationMs == duration, onClick = { onSelect(duration) })
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(label)
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Close") } }
    )
}

@Composable
fun ReportUserDialog(
    onDismiss: () -> Unit,
    onReport: (String) -> Unit
) {
    var reason by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Report User") },
        text = {
            Column {
                Text("Why are you reporting this user?")
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Spam, harassment, etc.") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onReport(reason) }, enabled = reason.isNotBlank()) {
                Text("Report")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun UserProfileDialog(
    user: com.him.landlordtenant.app.data.model.User,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.size(100.dp).clip(CircleShape).background(Color.LightGray)) {
                    Icon(Icons.Default.Person, null, modifier = Modifier.size(64.dp).align(Alignment.Center))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(user.fullName, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("@${user.username ?: "user"}", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("About", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(user.bio ?: "Hey there! I am using propertyOS.", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Phone Number", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(user.phoneNumber ?: "Not shared", fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Close") }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PremiumChatBubble(
    message: ChatMessage, 
    isCurrentUser: Boolean,
    currentUserId: String,
    onLongClick: () -> Unit
) {
    val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    val shape = if (isCurrentUser) {
        RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)
    } else {
        RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp)
    }
    val bubbleBrush = if (isCurrentUser) {
        Brush.linearGradient(com.him.landlordtenant.app.ui.theme.PremiumGradient)
    } else {
        Brush.linearGradient(listOf(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.surfaceVariant))
    }
    val contentColor = if (isCurrentUser) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = alignment) {
        Column(
            horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Surface(
                color = Color.Transparent,
                shape = shape,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .background(bubbleBrush, shape)
                    .combinedClickable(onClick = {}, onLongClick = onLongClick)
            ) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
                    if (message.replyTo != null) {
                        ReplyPreview(message.replyTo!!)
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    if (message.type != MessageType.TEXT) {
                        AttachmentPreview(message)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    if (message.text.isNotEmpty()) {
                        MarkdownText(
                            text = message.text, 
                            fontSize = 15.sp, 
                            color = contentColor, 
                            lineHeight = 20.sp
                        )
                    }
                    Row(
                        modifier = Modifier.align(Alignment.End).padding(top = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (message.isEdited) {
                            Text(
                                text = "edited",
                                fontSize = 9.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                color = if (isCurrentUser) Color.White.copy(alpha = 0.6f) else Color.Gray,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }
                        if (message.starredBy.containsKey(currentUserId)) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(10.dp).padding(end = 4.dp),
                                tint = if (isCurrentUser) Color.White.copy(alpha = 0.8f) else Color(0xFFFFB300)
                            )
                        }
                        Text(
                            text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.timestamp)),
                            fontSize = 10.sp,
                            color = if (isCurrentUser) Color.White.copy(alpha = 0.7f) else Color.Gray
                        )
                        if (isCurrentUser) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = when(message.status) {
                                    MessageStatus.READ -> Icons.Default.DoneAll
                                    MessageStatus.DELIVERED -> Icons.Default.DoneAll
                                    MessageStatus.FAILED -> Icons.Default.Error
                                    MessageStatus.SENDING -> Icons.Default.Schedule
                                    else -> Icons.Default.Check
                                },
                                contentDescription = null,
                                modifier = Modifier.size(13.dp),
                                tint = if (message.status == MessageStatus.READ) Color(0xFF00B0FF) else Color.White.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
            if (message.reactions.isNotEmpty()) {
                Row(
                    modifier = Modifier.offset(y = (-8).dp, x = if (isCurrentUser) (-12).dp else 12.dp).background(MaterialTheme.colorScheme.surface, CircleShape).padding(horizontal = 6.dp, vertical = 2.dp).shadow(1.dp, CircleShape),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    message.reactions.take(3).forEach { reaction -> Text(reaction.emoji, fontSize = 12.sp) }
                }
            }
        }
    }
}

@Composable
fun MarkdownText(text: String, fontSize: androidx.compose.ui.unit.TextUnit, color: Color, lineHeight: androidx.compose.ui.unit.TextUnit) {
    val annotatedString = remember(text) {
        androidx.compose.ui.text.buildAnnotatedString {
            var currentIndex = 0
            val boldPattern = "\\*(.*?)\\*".toRegex()
            val italicPattern = "_(.*?)_".toRegex()
            
            // This is a very simplified parser
            val allMatches = (boldPattern.findAll(text).map { it to "BOLD" } + 
                             italicPattern.findAll(text).map { it to "ITALIC" })
                             .sortedBy { it.first.range.first }

            for (match in allMatches) {
                val (result, type) = match
                append(text.substring(currentIndex, result.range.first))
                pushStyle(if (type == "BOLD") androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.Bold) 
                          else androidx.compose.ui.text.SpanStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic))
                append(result.groupValues[1])
                pop()
                currentIndex = result.range.last + 1
            }
            append(text.substring(currentIndex))
        }
    }
    Text(text = annotatedString, fontSize = fontSize, color = color, lineHeight = lineHeight)
}

@Composable
fun MessageOptionsMenu(
    onDismiss: () -> Unit,
    isCurrentUser: Boolean,
    isPinned: Boolean,
    isStarred: Boolean,
    onReply: () -> Unit,
    onPin: () -> Unit,
    onStar: () -> Unit,
    onEdit: () -> Unit,
    onReact: () -> Unit,
    onForward: () -> Unit,
    onDeleteForMe: () -> Unit,
    onDeleteForEveryone: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        text = {
            Column {
                ListItem(headlineContent = { Text("Reply") }, leadingContent = { Icon(Icons.AutoMirrored.Filled.Reply, null) }, modifier = Modifier.clickable { onReply() })
                ListItem(headlineContent = { Text(if (isPinned) "Unpin" else "Pin") }, leadingContent = { Icon(Icons.Default.PushPin, null) }, modifier = Modifier.clickable { onPin() })
                ListItem(headlineContent = { Text(if (isStarred) "Unstar" else "Star") }, leadingContent = { Icon(if (isStarred) Icons.Default.Star else Icons.Default.StarBorder, null) }, modifier = Modifier.clickable { onStar() })
                if (isCurrentUser) {
                    ListItem(headlineContent = { Text("Edit") }, leadingContent = { Icon(Icons.Default.Edit, null) }, modifier = Modifier.clickable { onEdit() })
                }
                ListItem(headlineContent = { Text("React") }, leadingContent = { Icon(Icons.Default.AddReaction, null) }, modifier = Modifier.clickable { onReact() })
                ListItem(headlineContent = { Text("Forward") }, leadingContent = { Icon(Icons.AutoMirrored.Filled.ArrowForward, null) }, modifier = Modifier.clickable { onForward() })
                ListItem(headlineContent = { Text("Delete for me") }, leadingContent = { Icon(Icons.Default.Delete, null) }, modifier = Modifier.clickable { onDeleteForMe() })
                if (isCurrentUser) {
                    ListItem(headlineContent = { Text("Delete for everyone", color = Color.Red) }, leadingContent = { Icon(Icons.Default.DeleteForever, null, tint = Color.Red) }, modifier = Modifier.clickable { onDeleteForEveryone() })
                }
            }
        }
    )
}

@Composable
fun ReactionPicker(onDismiss: () -> Unit, onReact: (String) -> Unit) {
    val reactions = listOf("❤️", "😂", "👍", "😮", "😢", "🙏")
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.surface, shadowElevation = 8.dp) {
            Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                reactions.forEach { emoji -> Text(text = emoji, fontSize = 28.sp, modifier = Modifier.clickable { onReact(emoji) }.padding(4.dp)) }
            }
        }
    }
}

@Composable
fun EmojiPickerDialog(onDismiss: () -> Unit, onEmojiSelected: (String) -> Unit) {
    val commonEmojis = listOf("❤️", "😂", "👍", "😮", "😢", "🙏", "🔥", "✨", "🙌", "😊", "🤔", "👀")
    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(24.dp), modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Pick an Emoji", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))
                androidx.compose.foundation.lazy.grid.LazyVerticalGrid(columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(4), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.height(200.dp)) {
                    items(commonEmojis.size) { index ->
                        val emoji = commonEmojis[index]
                        Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).clickable { onEmojiSelected(emoji) }, contentAlignment = Alignment.Center) { Text(emoji, fontSize = 24.sp) }
                    }
                }
            }
        }
    }
}

@Composable
fun ReplyPreview(reply: ChatReplyReference, onCancel: (() -> Unit)? = null) {
    Row(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.width(4.dp).height(40.dp).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(2.dp)))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(reply.senderName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
            Text(reply.textPreview, maxLines = 1, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, overflow = TextOverflow.Ellipsis)
        }
        if (onCancel != null) { IconButton(onClick = onCancel) { Icon(Icons.Default.Close, null, modifier = Modifier.size(20.dp), tint = Color.Gray) } }
    }
}

@Composable
fun AttachmentPreview(message: ChatMessage) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray), 
        contentAlignment = Alignment.Center
    ) {
        val attachment = message.attachments.firstOrNull()
        when(message.type) { 
            MessageType.IMAGE -> {
                AsyncImage(
                    model = attachment?.url,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            MessageType.LOCATION -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary)
                    Text("Lat: ${attachment?.latitude}, Lng: ${attachment?.longitude}", fontSize = 10.sp, color = Color.Gray)
                }
            }
            MessageType.VIDEO -> Icon(Icons.Default.VideoFile, null, modifier = Modifier.size(48.dp))
            MessageType.AUDIO -> Icon(Icons.Default.AudioFile, null, modifier = Modifier.size(48.dp))
            MessageType.DOCUMENT -> Icon(Icons.Default.Description, null, modifier = Modifier.size(48.dp))
            MessageType.CONTACT -> Icon(Icons.Default.ContactPage, null, modifier = Modifier.size(48.dp))
            else -> Icon(Icons.Default.FilePresent, null, modifier = Modifier.size(48.dp)) 
        }
    }
}

@Composable
fun ChatInputBar(text: String, onTextChange: (String) -> Unit, onSend: () -> Unit, onAttachmentClick: () -> Unit, onMicClick: () -> Unit, onEmojiClick: () -> Unit, isRecording: Boolean = false, isEditing: Boolean = false) {
    var isSelfRecording by remember { mutableStateOf(isRecording) }
    
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp).navigationBarsPadding(), verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier = Modifier
                .weight(1f)
                .shadow(4.dp, RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(28.dp))
                .padding(horizontal = 4.dp), 
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onEmojiClick) { 
                Icon(Icons.Default.SentimentSatisfiedAlt, null, tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)) 
            }
            
            Box(modifier = Modifier.weight(1f).animateContentSize()) {
                if (isRecording) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                        val infiniteTransition = rememberInfiniteTransition(label = "recording")
                        val dotAlpha by infiniteTransition.animateFloat(
                            initialValue = 0.2f,
                            targetValue = 1f,
                            animationSpec = infiniteRepeatable(animation = tween(500), repeatMode = RepeatMode.Reverse),
                            label = "alpha"
                        )
                        Box(modifier = Modifier.size(10.dp).background(Color.Red.copy(alpha = dotAlpha), CircleShape))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Recording...", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                } else {
                    TextField(
                        value = text, 
                        onValueChange = onTextChange, 
                        modifier = Modifier.fillMaxWidth(), 
                        placeholder = { Text(if (isEditing) "Edit your message..." else "Type a message...", color = Color.Gray) }, 
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent, 
                            unfocusedContainerColor = Color.Transparent, 
                            disabledContainerColor = Color.Transparent, 
                            focusedIndicatorColor = Color.Transparent, 
                            unfocusedIndicatorColor = Color.Transparent, 
                            focusedTextColor = MaterialTheme.colorScheme.onSurface, 
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        ), 
                        maxLines = 6
                    )
                }
            }
            
            IconButton(onClick = onAttachmentClick) { Icon(Icons.Default.AttachFile, null, tint = Color.Gray) }
        }
        Spacer(modifier = Modifier.width(10.dp))
        
        val buttonScale by animateFloatAsState(if (text.isNotEmpty() || isRecording) 1.1f else 1f, label = "send_scale")
        
        Box(
            modifier = Modifier
                .size(52.dp)
                .scale(buttonScale)
                .shadow(6.dp, CircleShape)
                .background(Brush.linearGradient(com.him.landlordtenant.app.ui.theme.PremiumGradient), CircleShape)
                .clickable { if (text.isNotEmpty()) onSend() else onMicClick() }, 
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isEditing) Icons.Default.Check else if (text.isNotEmpty() || isRecording) Icons.AutoMirrored.Filled.Send else Icons.Default.Mic, 
                contentDescription = null, 
                tint = Color.White, 
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun AttachmentDrawer(modifier: Modifier = Modifier, onAction: (MessageType) -> Unit) {
    Card(modifier = modifier.padding(horizontal = 16.dp).fillMaxWidth(), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(8.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                AttachmentItem(Icons.Default.Description, "Document", MaterialTheme.colorScheme.primary) { onAction(MessageType.DOCUMENT) }
                AttachmentItem(Icons.Default.PhotoCamera, "Camera", MaterialTheme.colorScheme.secondary) { onAction(MessageType.IMAGE) }
                AttachmentItem(Icons.Default.Image, "Gallery", MaterialTheme.colorScheme.tertiary) { onAction(MessageType.IMAGE) }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                AttachmentItem(Icons.Default.Headphones, "Audio", MaterialTheme.colorScheme.error) { onAction(MessageType.AUDIO) }
                AttachmentItem(Icons.Default.LocationOn, "Location", MaterialTheme.colorScheme.primary) { onAction(MessageType.LOCATION) }
                AttachmentItem(Icons.Default.Person, "Contact", MaterialTheme.colorScheme.secondary) { onAction(MessageType.CONTACT) }
            }
        }
    }
}

@Composable
fun AttachmentItem(icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable(onClick = onClick)) {
        Box(modifier = Modifier.size(52.dp).clip(CircleShape).background(color.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) { Icon(icon, null, tint = color) }
        Text(label, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp), color = MaterialTheme.colorScheme.onSurface)
    }
}

private fun formatLastSeen(timestamp: Long): String {
    if (timestamp == 0L) return "Never"
    val date = Date(timestamp)
    val now = Calendar.getInstance()
    val lastSeenDate = Calendar.getInstance().apply { time = date }
    return when {
        now.get(Calendar.DATE) == lastSeenDate.get(Calendar.DATE) -> {
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            "today at ${timeFormat.format(date)}"
        }
        now.get(Calendar.DATE) - lastSeenDate.get(Calendar.DATE) == 1 -> {
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            "yesterday at ${timeFormat.format(date)}"
        }
        else -> {
            val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            dateFormat.format(date)
        }
    }
}
