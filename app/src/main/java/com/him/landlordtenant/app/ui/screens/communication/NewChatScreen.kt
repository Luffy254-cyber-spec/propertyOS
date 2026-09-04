package com.him.landlordtenant.app.ui.screens.communication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.theme.PremiumGradient
import com.him.landlordtenant.app.ui.viewmodel.communication.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewChatScreen(
    onBack: () -> Unit,
    onUserSelected: (String) -> Unit,
    onPropertyCommunity: (String, Boolean) -> Unit = { _, _ -> },
    viewModel: ChatViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val contacts by viewModel.contacts.collectAsState()
    val userProperties by viewModel.userProperties.collectAsState()

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("New Message", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                        }
                    }
                )
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = {},
                    active = false,
                    onActiveChange = {},
                    placeholder = { Text("Search by name or unit...") },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    trailingIcon = { Icon(Icons.Default.Search, null) }
                ) { }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (userProperties.isNotEmpty()) {
                item {
                    SectionHeader("Property Channels")
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                items(userProperties) { apartment ->
                    QuickActionItem(
                        icon = Icons.Default.Campaign, 
                        title = "${apartment.name} Announcements", 
                        subtitle = "Official updates"
                    ) { 
                        onPropertyCommunity(apartment.id, true) 
                    }
                    QuickActionItem(
                        icon = Icons.Default.Groups, 
                        title = "${apartment.name} Community", 
                        subtitle = "Resident discussion"
                    ) { 
                        onPropertyCommunity(apartment.id, false)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader("Contacts")
            }

            items(contacts.filter { it.fullName.contains(searchQuery, true) || (it.username ?: "").contains(searchQuery, true) }) { user ->
                ContactItem(
                    ContactUIModel(
                        id = user.id,
                        name = user.fullName.ifEmpty { user.username ?: "User" },
                        role = user.activeRole?.displayName ?: "User",
                        isOnline = false // Online status could be observed per item but for now keep false
                    )
                ) { onUserSelected(user.id) }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun QuickActionItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(subtitle, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun ContactItem(contact: ContactUIModel, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(PremiumGradient)),
            contentAlignment = Alignment.Center
        ) {
            Text(contact.name.take(1), color = Color.White, fontWeight = FontWeight.Bold)
            if (contact.isOnline) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(2.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize().clip(CircleShape).background(Color(0xFF4CAF50)))
                }
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(contact.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(contact.role, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

data class ContactUIModel(
    val id: String,
    val name: String,
    val role: String,
    val isOnline: Boolean
)
