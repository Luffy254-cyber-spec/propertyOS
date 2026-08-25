package com.him.landlordtenant.app.ui.screens.communication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    onConversationClick: (String) -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val conversations by viewModel.conversations.collectAsState()
    ChatListContent(
        conversations = conversations,
        onConversationClick = onConversationClick,
        onNavigateToSettings = onNavigateToSettings
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListContent(
    conversations: List<ChatConversation>,
    onConversationClick: (String) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Messages", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { /* Search */ }) { Icon(Icons.Default.Search, null) }
                    IconButton(onClick = onNavigateToSettings) { Icon(Icons.Default.Settings, null) }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* New Chat */ }) {
                Icon(Icons.Default.Chat, null)
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
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                items(conversations) { conversation ->
                    ConversationItem(
                        conversation = conversation,
                        onClick = { onConversationClick(conversation.id) }
                    )
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
            onNavigateToSettings = {}
        )
    }
}

@Composable
fun ConversationItem(
    conversation: ChatConversation,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon/Avatar
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                if (conversation.isGroup) Icons.Default.Groups else Icons.Default.Person,
                null,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

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
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                conversation.lastMessage?.let {
                    Text(
                        text = formatTimestamp(it.timestamp),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                conversation.lastMessage?.let { lastMsg ->
                    if (lastMsg.senderId == "current_user_id_placeholder") { // Should get from VM
                        Icon(
                            Icons.Default.DoneAll,
                            null,
                            modifier = Modifier.size(14.dp),
                            tint = if (lastMsg.status == com.him.landlordtenant.app.data.model.communication.MessageStatus.READ) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    val previewText = when (lastMsg.type) {
                        MessageType.IMAGE -> "📷 Photo"
                        MessageType.VIDEO -> "📹 Video"
                        MessageType.AUDIO -> "🎵 Audio"
                        MessageType.DOCUMENT -> "📄 Document"
                        MessageType.LOCATION -> "📍 Location"
                        MessageType.CONTACT -> "👤 Contact"
                        else -> lastMsg.text
                    }

                    Text(
                        text = previewText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (conversation.unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = conversation.unreadCount.toString(),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
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
