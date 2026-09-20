package com.him.landlordtenant.app.ui.screens.communication

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.him.landlordtenant.app.data.model.communication.ChatConversation
import com.him.landlordtenant.app.data.model.communication.MessageStatus
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.viewmodel.communication.ChatViewModel
import java.text.SimpleDateFormat
import java.util.*

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
            EmptyConversations(modifier = Modifier.padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(conversations, key = { it.id }) { conversation ->
                    ConversationItem(
                        conversation = conversation,
                        currentUserId = currentUserId,
                        onClick = { onConversationClick(conversation.id) },
                        onPin = { onPinConversation(conversation.id, it) },
                        onMute = { onMuteConversation(conversation.id, it) },
                        onDelete = { onDeleteConversation(conversation.id) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ConversationItem(
    conversation: ChatConversation,
    currentUserId: String,
    onClick: () -> Unit,
    onPin: (Boolean) -> Unit,
    onMute: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    var showOptions by remember { mutableStateOf(false) }
    val isPinned = conversation.pinnedBy.containsKey(currentUserId)

    ListItem(
        modifier = Modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = { showOptions = true }
            ),
        headlineContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = conversation.name,
                    fontWeight = if (conversation.unreadCount > 0) FontWeight.ExtraBold else FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                if (isPinned) {
                    Icon(Icons.Default.PushPin, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                }
            }
        },
        supportingContent = {
            val partner = conversation.participants.find { it.id != currentUserId }
            if (partner?.isTyping == true) {
                Text("typing...", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            } else {
                Text(
                    text = conversation.lastMessage?.text ?: "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (conversation.unreadCount > 0) MaterialTheme.colorScheme.onSurface else Color.Gray
                )
            }
        },
        leadingContent = {
            Box(modifier = Modifier.size(56.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Brush.linearGradient(com.him.landlordtenant.app.ui.theme.PremiumGradient)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = conversation.name.take(1).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp
                    )
                }
            }
        },
        trailingContent = {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formatTimestamp(conversation.lastMessage?.timestamp ?: 0L),
                    fontSize = 11.sp,
                    color = if (conversation.unreadCount > 0) MaterialTheme.colorScheme.primary else Color.Gray,
                    fontWeight = if (conversation.unreadCount > 0) FontWeight.Bold else FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(6.dp))
                if (conversation.unreadCount > 0) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = conversation.unreadCount.toString(),
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    )

    if (showOptions) {
        AlertDialog(
            onDismissRequest = { showOptions = false },
            title = { Text(conversation.name) },
            text = {
                Column {
                    ListItem(
                        headlineContent = { Text(if (isPinned) "Unpin Chat" else "Pin Chat") },
                        leadingContent = { Icon(Icons.Default.PushPin, null) },
                        modifier = Modifier.clickable { onPin(!isPinned); showOptions = false }
                    )
                    ListItem(
                        headlineContent = { Text("Delete Conversation") },
                        leadingContent = { Icon(Icons.Default.Delete, null, tint = Color.Red) },
                        modifier = Modifier.clickable { onDelete(); showOptions = false }
                    )
                }
            },
            confirmButton = {}
        )
    }
}

@Composable
private fun EmptyConversations(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.ChatBubbleOutline, null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
            Spacer(modifier = Modifier.height(16.dp))
            Text("No messages yet", color = Color.Gray, fontWeight = FontWeight.Bold)
            Text("Your property communications will appear here.", color = Color.LightGray, fontSize = 12.sp)
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    if (timestamp == 0L) return ""
    val date = Date(timestamp)
    val now = Calendar.getInstance()
    val msgDate = Calendar.getInstance().apply { time = date }
    
    return when {
        now.get(Calendar.DATE) == msgDate.get(Calendar.DATE) -> {
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
        }
        now.get(Calendar.DATE) - msgDate.get(Calendar.DATE) == 1 -> {
            "Yesterday"
        }
        else -> {
            SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(date)
        }
    }
}
