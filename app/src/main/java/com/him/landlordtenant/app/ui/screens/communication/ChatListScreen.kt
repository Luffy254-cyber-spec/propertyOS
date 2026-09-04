package com.him.landlordtenant.app.ui.screens.communication

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.data.model.communication.ChatConversation
import com.him.landlordtenant.app.data.model.communication.MessageType
import com.him.landlordtenant.app.ui.viewmodel.communication.ChatViewModel
import androidx.compose.ui.tooling.preview.Preview
import com.him.landlordtenant.app.navigation.Route
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    onConversationClick: (String) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNewChat: () -> Unit = {},
    viewModel: ChatViewModel = hiltViewModel()
) {
    val conversations by viewModel.conversations.collectAsState()
    ChatListContent(
        conversations = conversations,
        onConversationClick = onConversationClick,
        onNavigateToSettings = onNavigateToSettings,
        onNewChat = onNewChat,
        onMarkAllAsRead = { viewModel.markAllAsRead() },
        onPinConversation = { id, pinned -> viewModel.pinConversation(id, pinned) },
        onMuteConversation = { id, muted -> viewModel.muteConversation(id, muted) },
        onDeleteConversation = { id -> viewModel.deleteConversation(id) },
        currentUserId = viewModel.currentUserId
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListContent(
    conversations: List<ChatConversation>,
    onConversationClick: (String) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNewChat: () -> Unit,
    onMarkAllAsRead: () -> Unit = {},
    onPinConversation: (String, Boolean) -> Unit = { _, _ -> },
    onMuteConversation: (String, Boolean) -> Unit = { _, _ -> },
    onDeleteConversation: (String) -> Unit = {},
    currentUserId: String = ""
) {
    var showMenu by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { 
                    Column {
                        Text("Messages", fontWeight = FontWeight.Black, fontSize = 28.sp)
                        Text("Property Communications", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Search */ }) { Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.onSurface) }
                    Box {
                        IconButton(onClick = { showMenu = true }) { Icon(Icons.Default.MoreVert, null, tint = MaterialTheme.colorScheme.onSurface) }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Mark all as read") },
                                onClick = { onMarkAllAsRead(); showMenu = false },
                                leadingIcon = { Icon(Icons.Default.DoneAll, null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Settings") },
                                onClick = { onNavigateToSettings(); showMenu = false },
                                leadingIcon = { Icon(Icons.Default.Settings, null) }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewChat,
                containerColor = Color.Transparent,
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                modifier = Modifier
                    .size(60.dp)
                    .shadow(8.dp, CircleShape)
                    .background(Brush.linearGradient(com.him.landlordtenant.app.ui.theme.PremiumGradient), CircleShape)
            ) {
                Icon(Icons.Default.Chat, null, tint = Color.White)
            }
        }
    ) { padding ->
        if (conversations.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Forum, null, modifier = Modifier.size(64.dp), tint = Color.Gray.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No messages yet", color = Color.Gray)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                itemsIndexed(conversations) { index, conversation ->
                    var itemVisible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        kotlinx.coroutines.delay(index * 50L)
                        itemVisible = true
                    }
                    
                    var showContextMenu by remember { mutableStateOf(false) }

                    AnimatedVisibility(
                        visible = itemVisible,
                        enter = slideInVertically { it / 2 } + fadeIn(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box {
                            ConversationItem(
                                conversation = conversation,
                                onClick = { onConversationClick(conversation.id) },
                                onLongClick = { showContextMenu = true }
                            )
                            
                            DropdownMenu(
                                expanded = showContextMenu,
                                onDismissRequest = { showContextMenu = false }
                            ) {
                                val isPinned = conversation.pinnedBy[currentUserId] == true
                                DropdownMenuItem(
                                    text = { Text(if (isPinned) "Unpin chat" else "Pin chat") },
                                    onClick = { onPinConversation(conversation.id, !isPinned); showContextMenu = false },
                                    leadingIcon = { Icon(Icons.Default.PushPin, null) }
                                )
                                val isMuted = conversation.mutedBy[currentUserId] == true
                                DropdownMenuItem(
                                    text = { Text(if (isMuted) "Unmute notifications" else "Mute notifications") },
                                    onClick = { onMuteConversation(conversation.id, !isMuted); showContextMenu = false },
                                    leadingIcon = { Icon(if (isMuted) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Delete chat") },
                                    onClick = { onDeleteConversation(conversation.id); showContextMenu = false },
                                    leadingIcon = { Icon(Icons.Default.Delete, null, tint = Color.Red) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatListScreenPreview() {
    PropertyOSTheme {
        ChatListContent(
            conversations = emptyList(),
            onConversationClick = {},
            onNavigateToSettings = {},
            onNewChat = {}
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConversationItem(
    conversation: ChatConversation,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon/Avatar with Gradient
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(com.him.landlordtenant.app.ui.theme.PremiumGradient)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (conversation.isGroup) Icons.Default.Groups else Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color.White
                )
                
                // Online indicator
                if (!conversation.isGroup) {
                    val isOnline = conversation.participants.any { it.isOnline } 
                    
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(if (isOnline) Color(0xFF4CAF50) else Color.Gray)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(18.dp))

            // Content
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    conversation.lastMessage?.let {
                        Text(
                            text = formatTimestamp(it.timestamp),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (conversation.unreadCount > 0) MaterialTheme.colorScheme.primary else Color.Gray,
                            fontWeight = if (conversation.unreadCount > 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    conversation.lastMessage?.let { lastMsg ->
                        val previewText = when (lastMsg.type) {
                            MessageType.IMAGE -> "📷 Photo"
                            MessageType.VIDEO -> "📹 Video"
                            MessageType.AUDIO -> "🎵 Voice Message"
                            MessageType.DOCUMENT -> "📄 Document"
                            MessageType.LOCATION -> "📍 Location"
                            else -> lastMsg.text
                        }

                        Text(
                            text = previewText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (conversation.unreadCount > 0) MaterialTheme.colorScheme.onSurface else Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f),
                            fontWeight = if (conversation.unreadCount > 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }

                    if (conversation.unreadCount > 0) {
                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                                .shadow(2.dp, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = conversation.unreadCount.toString(),
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val now = Calendar.getInstance()
    val msgDate = Calendar.getInstance().apply { time = date }

    return when {
        now.get(Calendar.DATE) == msgDate.get(Calendar.DATE) -> SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
        now.get(Calendar.DATE) - msgDate.get(Calendar.DATE) == 1 -> "Yesterday"
        else -> SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(date)
    }
}
