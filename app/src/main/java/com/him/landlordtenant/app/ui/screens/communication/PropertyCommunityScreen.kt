package com.him.landlordtenant.app.ui.screens.communication

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.viewmodel.communication.CommunityViewModel
import com.him.landlordtenant.app.data.model.communication.ChatMessage
import com.him.landlordtenant.app.ui.theme.PremiumGradient
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyCommunityScreen(
    propertyId: String,
    onBack: () -> Unit,
    isAnnouncementsOnly: Boolean = false,
    onNavigateToChat: (String) -> Unit = {},
    viewModel: CommunityViewModel = hiltViewModel()
) {
    val announcements by viewModel.announcements.collectAsState()
    val propertyName by viewModel.propertyName.collectAsState()
    val isLandlord by viewModel.isLandlord.collectAsState()
    var showPostDialog by remember { mutableStateOf(false) }

    LaunchedEffect(propertyId) {
        viewModel.loadCommunityData(propertyId)
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { 
                    Column {
                        Text("Building Community", fontWeight = FontWeight.Black)
                        Text(propertyName, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    if (isLandlord) {
                        IconButton(onClick = { showPostDialog = true }) { 
                            Icon(Icons.Default.Add, "Post Announcement") 
                        }
                    }
                    IconButton(onClick = { /* Info */ }) { Icon(Icons.Default.Info, null) }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Announcements",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            itemsIndexed(announcements) { index, announcement ->
                var isVisible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(index * 150L)
                    isVisible = true
                }
                
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(initialOffsetY = { 30 }) + fadeIn()
                ) {
                    AnnouncementCard(
                        Announcement(
                            id = announcement.id,
                            title = "Official Update",
                            content = announcement.text,
                            time = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date(announcement.timestamp)),
                            type = "INFO"
                        )
                    )
                }
            }
            
            if (!isAnnouncementsOnly) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        "Tenant Discussion",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                        Card(
                        modifier = Modifier.fillMaxWidth().clickable { onNavigateToChat("tenant_$propertyId") },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Forum, null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Join Discussion", fontWeight = FontWeight.Bold)
                            Text("Chat with your neighbors directly from here.", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }

        if (showPostDialog) {
            PostAnnouncementDialog(
                onDismiss = { showPostDialog = false },
                onPost = { text ->
                    viewModel.postAnnouncement(propertyId, text)
                    showPostDialog = false
                }
            )
        }
    }
}

@Composable
fun PostAnnouncementDialog(
    onDismiss: () -> Unit,
    onPost: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Post Announcement") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter announcement details...") },
                minLines = 3
            )
        },
        confirmButton = {
            Button(onClick = { onPost(text) }, enabled = text.isNotBlank()) {
                Text("Post")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun AnnouncementCard(announcement: Announcement) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            when(announcement.type) {
                                "URGENT" -> Color.Red.copy(alpha = 0.1f)
                                "SECURITY" -> Color.Black.copy(alpha = 0.1f)
                                else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when(announcement.type) {
                            "URGENT" -> Icons.Default.Warning
                            "SECURITY" -> Icons.Default.Security
                            else -> Icons.Default.Campaign
                        },
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = when(announcement.type) {
                            "URGENT" -> Color.Red
                            "SECURITY" -> Color.Black
                            else -> MaterialTheme.colorScheme.primary
                        }
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = announcement.title,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = announcement.time,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = announcement.content,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

data class Announcement(
    val id: String,
    val title: String,
    val content: String,
    val time: String,
    val type: String
)
