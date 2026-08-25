package com.him.landlordtenant.app.ui.screens.communication

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.data.model.communication.*
import com.him.landlordtenant.app.ui.viewmodel.communication.ChatViewModel
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
    val partnerProfile by viewModel.partnerProfile.collectAsState()

    LaunchedEffect(conversationId) {
        viewModel.selectConversation(conversationId)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailContent(
    conversation: ChatConversation?,
    messages: List<ChatMessage>,
    isPartnerOnline: Boolean,
    partnerProfile: com.him.landlordtenant.app.data.model.User?,
    currentUserId: String,
    onBack: () -> Unit,
    onSendMessage: (String) -> Unit,
    onInitiateCall: (Boolean) -> Unit,
    onSetTyping: (Boolean) -> Unit,
    onSetRecording: (Boolean) -> Unit,
    onAddReaction: (String, String) -> Unit,
    onDeleteMessage: (String) -> Unit
) {
    var messageText by remember { mutableStateOf("") }
    var showAttachmentDrawer by remember { mutableStateOf(false) }
    var selectedMessage by remember { mutableStateOf<ChatMessage?>(null) }
    var showMessageMenu by remember { mutableStateOf(false) }
    var showReactionPicker by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }
    var showEmojiPicker by remember { mutableStateOf(false) }
    var isSelfRecording by remember { mutableStateOf(false) }
    
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
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
                        Box(
                            modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Icon(
                                if (conversation?.isGroup == true) Icons.Default.Groups else Icons.Default.Person, 
                                null, 
                                modifier = Modifier.align(Alignment.Center),
                                tint = MaterialTheme.colorScheme.primary
                            )
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
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            val partner = conversation?.participants?.find { it.id != currentUserId }
                            if (partner?.isTyping == true) {
                                Text("typing...", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                            } else if (partner?.isRecording == true) {
                                Text("recording audio...", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                            } else if (isPartnerOnline) {
                                Text("Online", fontSize = 11.sp, color = Color(0xFF4CAF50))
                            } else {
                                Text("Available", fontSize = 11.sp, color = Color.Gray)
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
                    IconButton(onClick = { onInitiateCall(false) }) { 
                        Icon(Icons.Default.Call, null) 
                    }
                    IconButton(onClick = { onInitiateCall(true) }) { 
                        Icon(Icons.Default.VideoCall, null) 
                    }
                    Box {
                        IconButton(onClick = { showMenu = true }) { 
                            Icon(Icons.Default.MoreVert, null) 
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Search") },
                                onClick = { showMenu = false },
                                leadingIcon = { Icon(Icons.Default.Search, null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Mute notifications") },
                                onClick = { showMenu = false },
                                leadingIcon = { Icon(Icons.Default.NotificationsOff, null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Clear chat") },
                                onClick = { 
                                    showMenu = false
                                },
                                leadingIcon = { Icon(Icons.Default.DeleteSweep, null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Block") },
                                onClick = { showMenu = false },
                                leadingIcon = { Icon(Icons.Default.Block, null) }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            ChatInputBar(
                text = messageText,
                onTextChange = { 
                    messageText = it
                    onSetTyping(it.isNotEmpty())
                },
                onSend = {
                    if (messageText.isNotBlank() || isSelfRecording) {
                        onSendMessage(messageText)
                        messageText = ""
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
                isRecording = isSelfRecording
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.surface) 
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                reverseLayout = true
            ) {
                items(messages, key = { it.id }) { message ->
                    WhatsAppBubble(
                        message = message,
                        isCurrentUser = message.senderId == currentUserId,
                        onLongClick = {
                            selectedMessage = message
                            showMessageMenu = true
                        }
                    )
                }
            }

            if (showAttachmentDrawer) {
                AttachmentDrawer(
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp),
                    onAction = { type ->
                        showAttachmentDrawer = false
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
                    onReply = {
                        showMessageMenu = false
                    },
                    onReact = {
                        showReactionPicker = true
                        showMessageMenu = false
                    },
                    onForward = {
                        showMessageMenu = false
                    },
                    onDelete = {
                        onDeleteMessage(selectedMessage!!.id)
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

            if (showProfileDialog && partnerProfile != null) {
                UserProfileDialog(
                    user = partnerProfile,
                    onDismiss = { showProfileDialog = false }
                )
            }
        }
    }
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
                Box(
                    modifier = Modifier.size(100.dp).clip(CircleShape).background(Color.LightGray)
                ) {
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
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Close")
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WhatsAppBubble(
    message: ChatMessage, 
    isCurrentUser: Boolean,
    onLongClick: () -> Unit
) {
    val bubbleColor = if (isCurrentUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val contentColor = if (isCurrentUser) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
    val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    val shape = if (isCurrentUser) {
        RoundedCornerShape(12.dp, 0.dp, 12.dp, 12.dp)
    } else {
        RoundedCornerShape(0.dp, 12.dp, 12.dp, 12.dp)
    }

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = alignment) {
        Surface(
            color = bubbleColor,
            contentColor = contentColor,
            shape = shape,
            shadowElevation = 1.dp,
            modifier = Modifier.combinedClickable(
                onClick = {},
                onLongClick = onLongClick
            )
        ) {
            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp).widthIn(max = 280.dp)) {
                if (message.replyTo != null) {
                    ReplyPreview(message.replyTo!!)
                    Spacer(modifier = Modifier.height(4.dp))
                }
                
                if (message.type != MessageType.TEXT) {
                    AttachmentPreview(message)
                }

                if (message.text.isNotEmpty()) {
                    Text(text = message.text, fontSize = 15.sp)
                }

                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.timestamp)),
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    if (isCurrentUser) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = when(message.status) {
                                MessageStatus.READ -> Icons.Default.DoneAll
                                MessageStatus.DELIVERED -> Icons.Default.DoneAll
                                else -> Icons.Default.Check
                            },
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = if (message.status == MessageStatus.READ) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                }
            }
        }
        
        // Reactions
        if (message.reactions.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .align(if (isCurrentUser) Alignment.BottomEnd else Alignment.BottomStart)
                    .offset(y = 12.dp)
                    .background(Color.White, CircleShape)
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                message.reactions.take(3).forEach { reaction ->
                    Text(reaction.emoji, fontSize = 12.sp)
                }
                if (message.reactions.size > 3) {
                    Text("+${message.reactions.size - 3}", fontSize = 10.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun MessageOptionsMenu(
    onDismiss: () -> Unit,
    onReply: () -> Unit,
    onReact: () -> Unit,
    onForward: () -> Unit,
    onDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        text = {
            Column {
                ListItem(
                    headlineContent = { Text("Reply") },
                    leadingContent = { Icon(Icons.AutoMirrored.Filled.Reply, null) },
                    modifier = Modifier.clickable { onReply() }
                )
                ListItem(
                    headlineContent = { Text("React") },
                    leadingContent = { Icon(Icons.Default.AddReaction, null) },
                    modifier = Modifier.clickable { onReact() }
                )
                ListItem(
                    headlineContent = { Text("Forward") },
                    leadingContent = { Icon(Icons.AutoMirrored.Filled.ArrowForward, null) },
                    modifier = Modifier.clickable { onForward() }
                )
                ListItem(
                    headlineContent = { Text("Delete", color = Color.Red) },
                    leadingContent = { Icon(Icons.Default.Delete, null, tint = Color.Red) },
                    modifier = Modifier.clickable { onDelete() }
                )
            }
        }
    )
}

@Composable
fun ReactionPicker(
    onDismiss: () -> Unit,
    onReact: (String) -> Unit
) {
    val reactions = listOf("❤️", "😂", "👍", "😮", "😢", "🙏")
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                reactions.forEach { emoji ->
                    Text(
                        text = emoji,
                        fontSize = 28.sp,
                        modifier = Modifier
                            .clickable { onReact(emoji) }
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EmojiPickerDialog(
    onDismiss: () -> Unit,
    onEmojiSelected: (String) -> Unit
) {
    val commonEmojis = listOf("❤️", "😂", "👍", "😮", "😢", "🙏", "🔥", "✨", "🙌", "😊", "🤔", "👀")
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Pick an Emoji", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))
                androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                    columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(200.dp)
                ) {
                    items(commonEmojis.size) { index ->
                        val emoji = commonEmojis[index]
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onEmojiSelected(emoji) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(emoji, fontSize = 24.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReplyPreview(reply: ChatReplyReference) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.3f))
            .padding(8.dp)
    ) {
        Text(reply.senderName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
        Text(reply.textPreview, maxLines = 1, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun AttachmentPreview(message: ChatMessage) {
    // Simplified attachment preview
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            when(message.type) {
                MessageType.IMAGE -> Icons.Default.Image
                MessageType.VIDEO -> Icons.Default.VideoFile
                MessageType.AUDIO -> Icons.Default.AudioFile
                MessageType.DOCUMENT -> Icons.Default.Description
                MessageType.LOCATION -> Icons.Default.LocationOn
                MessageType.CONTACT -> Icons.Default.ContactPage
                else -> Icons.Default.FilePresent
            },
            null,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
fun ChatInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    onAttachmentClick: () -> Unit,
    onMicClick: () -> Unit,
    onEmojiClick: () -> Unit,
    isRecording: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .navigationBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isRecording) {
            // Recording UI
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(28.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Mic, null, tint = Color.Red, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Recording...", modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurfaceVariant)
                TextButton(onClick = { /* Cancel recording logic */ }) {
                    Text("Cancel", color = Color.Red)
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(28.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onEmojiClick) { 
                    Icon(Icons.Default.SentimentSatisfiedAlt, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) 
                }
                
                TextField(
                    value = text,
                    onValueChange = onTextChange,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Message", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
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
                
                IconButton(onClick = onAttachmentClick) { 
                    Icon(Icons.Default.AttachFile, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) 
                }
                
                if (text.isEmpty()) {
                    IconButton(onClick = { /* Camera */ }) { 
                        Icon(Icons.Default.PhotoCamera, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) 
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.width(8.dp))

        FloatingActionButton(
            onClick = { if (text.isNotEmpty()) onSend() else onMicClick() },
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(48.dp),
            elevation = FloatingActionButtonDefaults.elevation(2.dp)
        ) {
            Icon(if (text.isNotEmpty() || isRecording) Icons.AutoMirrored.Filled.Send else Icons.Default.Mic, null)
        }
    }
}

@Composable
fun AttachmentDrawer(
    modifier: Modifier = Modifier,
    onAction: (MessageType) -> Unit
) {
    Card(
        modifier = modifier.padding(horizontal = 16.dp).fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
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
        Box(
            modifier = Modifier.size(52.dp).clip(CircleShape).background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = color)
        }
        Text(label, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp), color = MaterialTheme.colorScheme.onSurface)
    }
}
