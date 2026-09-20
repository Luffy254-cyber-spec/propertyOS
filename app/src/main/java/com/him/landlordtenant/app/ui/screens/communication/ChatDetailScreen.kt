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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.him.landlordtenant.app.data.model.User
import com.him.landlordtenant.app.data.model.communication.*
import com.him.landlordtenant.app.ui.theme.PremiumGradient
import com.him.landlordtenant.app.ui.viewmodel.communication.ChatViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatDetailScreen(
    conversationId: String,
    partnerId: String? = null,
    onBack: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val conversation by viewModel.currentConversation.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val isPartnerOnline by viewModel.isPartnerOnline.collectAsState()
    val partnerLastSeen by viewModel.partnerLastSeen.collectAsState()
    val partnerProfile by viewModel.partnerProfile.collectAsState()
    val allConversations by viewModel.conversations.collectAsState()

    LaunchedEffect(conversationId, partnerId) {
        viewModel.selectConversation(conversationId, partnerId)
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
        onInitiateCall = { isVideo -> 
            val pid = partnerId ?: conversation?.participants?.find { it.id != viewModel.currentUserId }?.id
            val pname = partnerProfile?.fullName ?: conversation?.name ?: "User"
            if (conversation?.isGroup == true) viewModel.initiateCall(conversation!!.id, conversation!!.name, isVideo)
            else if (pid != null) viewModel.initiateCall(pid, pname, isVideo)
        },
        onSetTyping = { viewModel.setTyping(it) },
        onSetRecording = { viewModel.setRecording(it) },
        onAddReaction = { mid, emo -> viewModel.addReaction(mid, emo) },
        onPinMessage = { mid, p -> viewModel.pinMessage(mid, p) },
        onForwardMessage = { msg, t -> viewModel.forwardMessage(msg, t) },
        onEditMessage = { mid, txt -> viewModel.editMessage(mid, txt) },
        onToggleStar = { mid -> viewModel.toggleStarMessage(mid) },
        onDeleteMessage = { mid, ev -> if (ev) viewModel.deleteMessage(mid, true) else viewModel.deleteMessageForMe(mid) },
        onClearChat = { viewModel.clearChat() },
        onMuteNotifications = { viewModel.muteNotifications(it) },
        onBlockUser = { viewModel.blockUser() },
        onDeleteChat = { viewModel.deleteChat(); onBack() },
        onSetDisappearingMessages = { viewModel.setDisappearingMessages(it) },
        onSetNickname = { viewModel.setNickname(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailContent(
    conversation: ChatConversation?,
    messages: List<ChatMessage>,
    allConversations: List<ChatConversation>,
    isPartnerOnline: Boolean,
    partnerLastSeen: Long,
    partnerProfile: User?,
    currentUserId: String,
    onBack: () -> Unit,
    onSendMessage: (String, ChatReplyReference?) -> Unit,
    onSendMedia: (android.net.Uri, MessageType) -> Unit,
    onInitiateCall: (Boolean) -> Unit,
    onSetTyping: (Boolean) -> Unit,
    onSetRecording: (Boolean) -> Unit,
    onAddReaction: (String, String) -> Unit,
    onPinMessage: (String, Boolean) -> Unit,
    onForwardMessage: (ChatMessage, List<String>) -> Unit,
    onEditMessage: (String, String) -> Unit,
    onToggleStar: (String) -> Unit,
    onDeleteMessage: (String, Boolean) -> Unit,
    onClearChat: () -> Unit,
    onMuteNotifications: (Boolean) -> Unit,
    onBlockUser: () -> Unit,
    onDeleteChat: () -> Unit,
    onSetDisappearingMessages: (Long) -> Unit,
    onSetNickname: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var replyingTo by remember { mutableStateOf<ChatMessage?>(null) }
    var editingMsg by remember { mutableStateOf<ChatMessage?>(null) }
    var selectedMsg by remember { mutableStateOf<ChatMessage?>(null) }
    var showMsgMenu by remember { mutableStateOf(false) }
    var showReactPicker by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var showProfile by remember { mutableStateOf(false) }
    var showSearch by remember { mutableStateOf(false) }
    var showForward by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showAttachment by remember { mutableStateOf(false) }
    var isSelfRec by remember { mutableStateOf(false) }
    
    val listState = rememberLazyListState()
    val imagePicker = androidx.activity.compose.rememberLauncherForActivityResult(androidx.activity.result.contract.ActivityResultContracts.GetContent()) { it?.let { onSendMedia(it, MessageType.IMAGE) } }

    Scaffold(
        topBar = {
            if (showSearch) {
                SearchTopBar(query = searchQuery, onQueryChange = { searchQuery = it }, onClose = { showSearch = false; searchQuery = "" })
            } else {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { if (conversation?.isGroup == true) { /* Group info */ } else showProfile = true }) {
                            Box(modifier = Modifier.size(40.dp)) {
                                Box(modifier = Modifier.fillMaxSize().clip(CircleShape).background(Brush.linearGradient(PremiumGradient))) {
                                    Icon(if (conversation?.isGroup == true) Icons.Default.Groups else Icons.Default.Person, null, modifier = Modifier.align(Alignment.Center), tint = Color.White)
                                }
                                Box(modifier = Modifier.align(Alignment.BottomEnd).size(12.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surface).padding(1.5.dp)) {
                                    Box(modifier = Modifier.fillMaxSize().clip(CircleShape).background(if (isPartnerOnline) Color(0xFF4CAF50) else Color.Gray))
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(if (conversation?.isGroup == true) conversation.name else partnerProfile?.fullName ?: conversation?.name ?: "Chat", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                val partner = conversation?.participants?.find { it.id != currentUserId }
                                val st = when {
                                    partner?.isTyping == true -> "typing..."
                                    partner?.isRecording == true -> "recording audio..."
                                    isPartnerOnline -> "Online"
                                    partnerLastSeen > 0 -> "Last seen ${formatLastSeen(partnerLastSeen)}"
                                    else -> "Offline"
                                }
                                Text(st, fontSize = 11.sp, color = if (st == "Online" || st.endsWith("...")) Color(0xFF4CAF50) else Color.Gray, fontWeight = if (st == "Online" || st.endsWith("...")) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    },
                    navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                    actions = {
                        CallActionButtons(isGroup = conversation?.isGroup == true, onCall = { onInitiateCall(false) }, onVideoCall = { onInitiateCall(true) })
                        Box {
                            IconButton(onClick = { showMenu = true }) { Icon(Icons.Default.MoreVert, null) }
                            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                                DropdownMenuItem(text = { Text("View contact") }, onClick = { showProfile = true; showMenu = false }, leadingIcon = { Icon(Icons.Default.Person, null) })
                                DropdownMenuItem(text = { Text("Search") }, onClick = { showSearch = true; showMenu = false }, leadingIcon = { Icon(Icons.Default.Search, null) })
                                val isMuted = conversation?.mutedBy?.get(currentUserId) == true
                                DropdownMenuItem(text = { Text(if (isMuted) "Unmute" else "Mute") }, onClick = { onMuteNotifications(!isMuted); showMenu = false }, leadingIcon = { Icon(if (isMuted) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff, null) })
                                DropdownMenuItem(text = { Text("Clear chat") }, onClick = { onClearChat(); showMenu = false }, leadingIcon = { Icon(Icons.Default.DeleteSweep, null) })
                                DropdownMenuItem(text = { Text("Delete chat") }, onClick = { onDeleteChat(); showMenu = false }, leadingIcon = { Icon(Icons.Default.DeleteForever, null) })
                                DropdownMenuItem(text = { Text("Block") }, onClick = { onBlockUser(); showMenu = false }, leadingIcon = { Icon(Icons.Default.Block, null) })
                                DropdownMenuItem(text = { Text("Report") }, onClick = { /* showReport */ showMenu = false }, leadingIcon = { Icon(Icons.Default.Report, null) })
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            Column {
                if (replyingTo != null) ReplyPreview(reply = ChatReplyReference(messageId = replyingTo!!.id, senderName = replyingTo!!.senderName, textPreview = replyingTo!!.text, type = replyingTo!!.type), onCancel = { replyingTo = null })
                if (editingMsg != null) EditPreview(text = editingMsg!!.text, onCancel = { editingMsg = null; text = "" })
                ChatInputBar(
                    text = text,
                    onTextChange = { text = it; onSetTyping(it.isNotEmpty()) },
                    onSend = {
                        if (text.isNotBlank() || isSelfRec) {
                            if (editingMsg != null) onEditMessage(editingMsg!!.id, text)
                            else onSendMessage(text, replyingTo?.let { ChatReplyReference(it.id, it.senderName, it.text, it.type) })
                            text = ""; replyingTo = null; editingMsg = null; onSetTyping(false); isSelfRec = false; onSetRecording(false)
                        }
                    },
                    onAttachmentClick = { showAttachment = !showAttachment },
                    onMicClick = { isSelfRec = !isSelfRec; onSetRecording(isSelfRec) },
                    onEmojiClick = { /* showEmoji */ },
                    isRecording = isSelfRec,
                    isEditing = editingMsg != null
                )
            }
        }
    ) { p ->
        Box(modifier = Modifier.fillMaxSize().padding(p)) {
            LazyColumn(state = listState, modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp), reverseLayout = true) {
                val filtered = if (searchQuery.isBlank()) messages else messages.filter { it.text.contains(searchQuery, true) }
                items(filtered, key = { it.id.ifEmpty { "${it.timestamp}_${it.senderId}" } }) { msg ->
                    if (msg.type == MessageType.SYSTEM) SystemMessageItem(msg.text)
                    else PremiumChatBubble(message = msg, isCurrentUser = msg.senderId == currentUserId, currentUserId = currentUserId, onLongClick = { selectedMsg = msg; showMsgMenu = true })
                }
            }
            if (showAttachment) AttachmentDrawer(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp), onAction = { t -> showAttachment = false; if (t == MessageType.IMAGE) imagePicker.launch("image/*") })
            if (showMsgMenu && selectedMsg != null) {
                MessageOptionsMenu(
                    onDismiss = { showMsgMenu = false }, isCurrentUser = selectedMsg!!.senderId == currentUserId, isPinned = selectedMsg!!.pinned, isStarred = selectedMsg!!.starredBy.containsKey(currentUserId),
                    onReply = { replyingTo = selectedMsg; showMsgMenu = false },
                    onPin = { onPinMessage(selectedMsg!!.id, !selectedMsg!!.pinned); showMsgMenu = false },
                    onStar = { onToggleStar(selectedMsg!!.id); showMsgMenu = false },
                    onEdit = { editingMsg = selectedMsg; text = selectedMsg!!.text; showMsgMenu = false },
                    onReact = { showReactPicker = true; showMsgMenu = false },
                    onForward = { showForward = true; showMsgMenu = false },
                    onDeleteForMe = { onDeleteMessage(selectedMsg!!.id, false); showMsgMenu = false },
                    onDeleteForEveryone = { onDeleteMessage(selectedMsg!!.id, true); showMsgMenu = false }
                )
            }
            if (showReactPicker && selectedMsg != null) ReactionPicker(onDismiss = { showReactPicker = false }, onReact = { onAddReaction(selectedMsg!!.id, it); showReactPicker = false; selectedMsg = null })
            if (showForward && selectedMsg != null) ForwardPickerDialog(conversations = allConversations, onDismiss = { showForward = false }, onForward = { onForwardMessage(selectedMsg!!, it); showForward = false; selectedMsg = null })
            if (showProfile && partnerProfile != null) UserProfileDialog(user = partnerProfile, onDismiss = { showProfile = false })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(query: String, onQueryChange: (String) -> Unit, onClose: () -> Unit) {
    TopAppBar(title = { TextField(value = query, onValueChange = onQueryChange, modifier = Modifier.fillMaxWidth(), placeholder = { Text("Search messages...") }, colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent), singleLine = true) }, navigationIcon = { IconButton(onClick = onClose) { Icon(Icons.Default.Close, null) } })
}

@Composable
private fun SystemMessageItem(text: String) {
    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
        Surface(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), shape = RoundedCornerShape(12.dp)) {
            Text(text, modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp), fontSize = 11.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PremiumChatBubble(message: ChatMessage, isCurrentUser: Boolean, currentUserId: String, onLongClick: () -> Unit) {
    val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    val shape = if (isCurrentUser) RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp) else RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp)
    val bubbleBrush = if (isCurrentUser) Brush.linearGradient(PremiumGradient) else Brush.linearGradient(listOf(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.surfaceVariant))
    val contentColor = if (isCurrentUser) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = alignment) {
        Column(horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start, modifier = Modifier.widthIn(max = 300.dp)) {
            Surface(color = Color.Transparent, shape = shape, shadowElevation = 2.dp, modifier = Modifier.background(bubbleBrush, shape).combinedClickable(onClick = {}, onLongClick = onLongClick)) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
                    if (message.replyTo != null) { ReplyPreview(message.replyTo!!); Spacer(modifier = Modifier.height(6.dp)) }
                    if (message.type != MessageType.TEXT) { AttachmentPreview(message); Spacer(modifier = Modifier.height(4.dp)) }
                    if (message.text.isNotEmpty()) Text(message.text, fontSize = 15.sp, color = contentColor, lineHeight = 20.sp)
                    Row(modifier = Modifier.align(Alignment.End).padding(top = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                        if (message.isEdited) Text("edited", fontSize = 9.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, color = if (isCurrentUser) Color.White.copy(0.6f) else Color.Gray, modifier = Modifier.padding(end = 4.dp))
                        if (message.starredBy.containsKey(currentUserId)) Icon(Icons.Default.Star, null, modifier = Modifier.size(10.dp).padding(end = 4.dp), tint = if (isCurrentUser) Color.White.copy(0.8f) else Color(0xFFFFB300))
                        Text(SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.timestamp)), fontSize = 10.sp, color = if (isCurrentUser) Color.White.copy(0.7f) else Color.Gray)
                        if (isCurrentUser) {
                            Spacer(modifier = Modifier.width(4.dp))
                            val (tick, color) = when(message.status) {
                                MessageStatus.READ -> Icons.Default.DoneAll to Color(0xFF00B0FF)
                                MessageStatus.DELIVERED -> Icons.Default.DoneAll to Color.White
                                MessageStatus.SENT -> Icons.Default.Check to Color.White.copy(0.8f)
                                MessageStatus.SENDING -> Icons.Default.Schedule to Color.White.copy(0.5f)
                                else -> Icons.Default.Error to Color.White.copy(0.5f)
                            }
                            Icon(tick, null, modifier = Modifier.size(13.dp), tint = color)
                        }
                    }
                }
            }
            if (message.reactions.isNotEmpty()) {
                Row(modifier = Modifier.offset(y = (-8).dp, x = if (isCurrentUser) (-12).dp else 12.dp).background(MaterialTheme.colorScheme.surface, CircleShape).padding(horizontal = 6.dp, vertical = 2.dp).shadow(1.dp, CircleShape), horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    message.reactions.values.take(3).forEach { reaction -> Text(reaction.emoji, fontSize = 12.sp) }
                }
            }
        }
    }
}

@Composable
fun MessageOptionsMenu(onDismiss: () -> Unit, isCurrentUser: Boolean, isPinned: Boolean, isStarred: Boolean, onReply: () -> Unit, onPin: () -> Unit, onStar: () -> Unit, onEdit: () -> Unit, onReact: () -> Unit, onForward: () -> Unit, onDeleteForMe: () -> Unit, onDeleteForEveryone: () -> Unit) {
    AlertDialog(onDismissRequest = onDismiss, confirmButton = {}, text = {
        Column {
            ListItem(headlineContent = { Text("Reply") }, leadingContent = { Icon(Icons.AutoMirrored.Filled.Reply, null) }, modifier = Modifier.clickable { onReply() })
            ListItem(headlineContent = { Text(if (isPinned) "Unpin" else "Pin") }, leadingContent = { Icon(Icons.Default.PushPin, null) }, modifier = Modifier.clickable { onPin() })
            ListItem(headlineContent = { Text(if (isStarred) "Unstar" else "Star") }, leadingContent = { Icon(if (isStarred) Icons.Default.Star else Icons.Default.StarBorder, null) }, modifier = Modifier.clickable { onStar() })
            if (isCurrentUser) ListItem(headlineContent = { Text("Edit") }, leadingContent = { Icon(Icons.Default.Edit, null) }, modifier = Modifier.clickable { onEdit() })
            ListItem(headlineContent = { Text("React") }, leadingContent = { Icon(Icons.Default.AddReaction, null) }, modifier = Modifier.clickable { onReact() })
            ListItem(headlineContent = { Text("Forward") }, leadingContent = { Icon(Icons.AutoMirrored.Filled.ArrowForward, null) }, modifier = Modifier.clickable { onForward() })
            ListItem(headlineContent = { Text("Delete for me") }, leadingContent = { Icon(Icons.Default.Delete, null) }, modifier = Modifier.clickable { onDeleteForMe() })
            if (isCurrentUser) ListItem(headlineContent = { Text("Delete for everyone", color = Color.Red) }, leadingContent = { Icon(Icons.Default.DeleteForever, null, tint = Color.Red) }, modifier = Modifier.clickable { onDeleteForEveryone() })
        }
    })
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
fun ReplyPreview(reply: ChatReplyReference, onCancel: (() -> Unit)? = null) {
    Row(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.width(4.dp).height(40.dp).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(2.dp)))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(reply.senderName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
            Text(reply.textPreview, maxLines = 1, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, overflow = TextOverflow.Ellipsis)
        }
        if (onCancel != null) IconButton(onClick = onCancel) { Icon(Icons.Default.Close, null, modifier = Modifier.size(20.dp), tint = Color.Gray) }
    }
}

@Composable
fun AttachmentPreview(message: ChatMessage) {
    Box(modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(8.dp)).background(Color.LightGray), contentAlignment = Alignment.Center) {
        val attachment = message.attachments.firstOrNull()
        when(message.type) { 
            MessageType.IMAGE -> AsyncImage(model = attachment?.url, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            MessageType.LOCATION -> Column(horizontalAlignment = Alignment.CenterHorizontally) { Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary); Text("Lat: ${attachment?.latitude}, Lng: ${attachment?.longitude}", fontSize = 10.sp, color = Color.Gray) }
            else -> Icon(Icons.Default.FilePresent, null, modifier = Modifier.size(48.dp)) 
        }
    }
}

@Composable
fun ChatInputBar(text: String, onTextChange: (String) -> Unit, onSend: () -> Unit, onAttachmentClick: () -> Unit, onMicClick: () -> Unit, onEmojiClick: () -> Unit, isRecording: Boolean = false, isEditing: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp).navigationBarsPadding(), verticalAlignment = Alignment.CenterVertically) {
        Row(modifier = Modifier.weight(1f).shadow(4.dp, RoundedCornerShape(28.dp)).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(28.dp)).padding(horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onEmojiClick) { Icon(Icons.Default.SentimentSatisfiedAlt, null, tint = MaterialTheme.colorScheme.primary.copy(0.8f)) }
            Box(modifier = Modifier.weight(1f).animateContentSize()) {
                if (isRecording) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                        val infiniteTransition = rememberInfiniteTransition(label = "recording")
                        val dotAlpha by infiniteTransition.animateFloat(initialValue = 0.2f, targetValue = 1f, animationSpec = infiniteRepeatable(animation = tween(500), repeatMode = RepeatMode.Reverse), label = "alpha")
                        Box(modifier = Modifier.size(10.dp).background(Color.Red.copy(alpha = dotAlpha), CircleShape))
                        Spacer(modifier = Modifier.width(8.dp)); Text("Recording...", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                } else TextField(value = text, onValueChange = onTextChange, modifier = Modifier.fillMaxWidth(), placeholder = { Text(if (isEditing) "Edit message..." else "Type a message...", color = Color.Gray) }, colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, disabledContainerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent), maxLines = 6)
            }
            IconButton(onClick = onAttachmentClick) { Icon(Icons.Default.AttachFile, null, tint = Color.Gray) }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Box(modifier = Modifier.size(52.dp).shadow(6.dp, CircleShape).background(Brush.linearGradient(PremiumGradient), CircleShape).clickable { if (text.isNotEmpty()) onSend() else onMicClick() }, contentAlignment = Alignment.Center) {
            Icon(imageVector = if (isEditing) Icons.Default.Check else if (text.isNotEmpty() || isRecording) Icons.AutoMirrored.Filled.Send else Icons.Default.Mic, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
fun AttachmentDrawer(modifier: Modifier = Modifier, onAction: (MessageType) -> Unit) {
    Card(modifier = modifier.padding(horizontal = 16.dp).fillMaxWidth(), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(8.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                AttachmentItem(Icons.Default.PhotoCamera, "Camera", MaterialTheme.colorScheme.secondary) { onAction(MessageType.IMAGE) }
                AttachmentItem(Icons.Default.Image, "Gallery", MaterialTheme.colorScheme.tertiary) { onAction(MessageType.IMAGE) }
                AttachmentItem(Icons.Default.LocationOn, "Location", MaterialTheme.colorScheme.primary) { onAction(MessageType.LOCATION) }
                AttachmentItem(Icons.Default.Person, "Contact", MaterialTheme.colorScheme.secondary) { onAction(MessageType.CONTACT) }
            }
        }
    }
}

@Composable
fun AttachmentItem(icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable(onClick = onClick)) {
        Box(modifier = Modifier.size(52.dp).clip(CircleShape).background(color.copy(0.1f)), contentAlignment = Alignment.Center) { Icon(icon, null, tint = color) }
        Text(label, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp), color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun CallActionButtons(isGroup: Boolean, onCall: () -> Unit, onVideoCall: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Surface(onClick = onCall, modifier = Modifier.size(38.dp), shape = CircleShape, color = Color.Transparent) { Box(contentAlignment = Alignment.Center) { Icon(if (isGroup) Icons.Default.GroupAdd else Icons.Default.Call, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp)) } }
        Surface(onClick = onVideoCall, modifier = Modifier.size(38.dp), shape = CircleShape, color = Color.Transparent) { Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.VideoCall, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp)) } }
    }
}

@Composable
fun EditPreview(text: String, onCancel: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.width(4.dp).height(40.dp).background(Color(0xFF4CAF50), RoundedCornerShape(2.dp)))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) { Text("Edit message", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF4CAF50)); Text(text, maxLines = 1, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, overflow = TextOverflow.Ellipsis) }
        IconButton(onClick = onCancel) { Icon(Icons.Default.Close, null, modifier = Modifier.size(20.dp), tint = Color.Gray) }
    }
}

@Composable
fun ForwardPickerDialog(conversations: List<ChatConversation>, onDismiss: () -> Unit, onForward: (List<String>) -> Unit) {
    val selectedIds = remember { mutableStateListOf<String>() }
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Forward to...") }, text = { LazyColumn(modifier = Modifier.height(300.dp)) { items(conversations) { conv -> Row(modifier = Modifier.fillMaxWidth().clickable { if (selectedIds.contains(conv.id)) selectedIds.remove(conv.id) else selectedIds.add(conv.id) }.padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) { Checkbox(checked = selectedIds.contains(conv.id), onCheckedChange = null); Spacer(modifier = Modifier.width(12.dp)); Text(conv.name, modifier = Modifier.weight(1f)) } } } }, confirmButton = { Button(onClick = { onForward(selectedIds.toList()) }, enabled = selectedIds.isNotEmpty()) { Text("Forward") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } })
}

@Composable
fun UserProfileDialog(user: User, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) { Card(modifier = Modifier.fillMaxWidth().padding(16.dp), shape = RoundedCornerShape(16.dp)) { Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) { Box(modifier = Modifier.size(100.dp).clip(CircleShape).background(Color.LightGray)) { Icon(Icons.Default.Person, null, modifier = Modifier.size(64.dp).align(Alignment.Center)) }; Spacer(modifier = Modifier.height(16.dp)); Text(user.fullName, fontWeight = FontWeight.Bold, fontSize = 20.sp); Text("@${user.username ?: "user"}", color = Color.Gray, fontSize = 14.sp); Spacer(modifier = Modifier.height(16.dp)); HorizontalDivider(); Spacer(modifier = Modifier.height(16.dp)); Column(modifier = Modifier.fillMaxWidth()) { Text("About", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary); Text(user.bio ?: "Hey there! I am using propertyOS.", fontSize = 14.sp); Spacer(modifier = Modifier.height(16.dp)); Text("Phone Number", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary); Text(user.phoneNumber ?: "Not shared", fontSize = 14.sp) }; Spacer(modifier = Modifier.height(24.dp)); Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Close") } } } }
}

private fun formatLastSeen(timestamp: Long): String {
    if (timestamp == 0L) return "Never"
    val date = Date(timestamp)
    val now = Calendar.getInstance()
    val lastSeenDate = Calendar.getInstance().apply { time = date }
    return when {
        now.get(Calendar.DATE) == lastSeenDate.get(Calendar.DATE) -> "today at ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)}"
        now.get(Calendar.DATE) - lastSeenDate.get(Calendar.DATE) == 1 -> "yesterday at ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)}"
        else -> SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(date)
    }
}
