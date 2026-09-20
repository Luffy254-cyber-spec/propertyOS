package com.him.landlordtenant.app.ui.screens.communication

import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.theme.PremiumGradient
import com.him.landlordtenant.app.ui.viewmodel.communication.ChatViewModel
import com.him.landlordtenant.app.interfaces.TenantSummaryData
import com.him.landlordtenant.app.data.model.User

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
    val tenants by viewModel.tenants.collectAsState()
    val userProperties by viewModel.userProperties.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.selectConversation("", null) 
        viewModel.loadTenants()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Message", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Enhanced Search
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
                placeholder = { Text("Search residents, units or contacts...") },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                )
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Property Channels
                if (userProperties.isNotEmpty() && searchQuery.isEmpty()) {
                    item { SectionHeader("Community Groups") }
                    items(userProperties) { property ->
                        PropertyChannelItem(
                            title = "${property.name} Announcements",
                            subtitle = "Broadcast to all residents",
                            icon = Icons.Default.Campaign,
                            onClick = { onPropertyCommunity(property.id, true) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        PropertyChannelItem(
                            title = "${property.name} Residents",
                            subtitle = "Community discussion board",
                            icon = Icons.Default.Groups,
                            onClick = { onPropertyCommunity(property.id, false) }
                        )
                    }
                }

                // 2. My Residents Section (For Landlords)
                val filteredTenants = tenants.filter { 
                    it.name.contains(searchQuery, true) || it.unitName.contains(searchQuery, true) 
                }
                if (filteredTenants.isNotEmpty()) {
                    item { SectionHeader("My Residents") }
                    items(filteredTenants) { tenant ->
                        TenantContactItem(tenant) { onUserSelected(tenant.id) }
                    }
                }

                // 3. General Contacts Section
                val filteredContacts = contacts.filter { 
                    it.fullName.contains(searchQuery, true) || (it.username ?: "").contains(searchQuery, true) 
                }.filter { c -> filteredTenants.none { it.id == c.id } } // Avoid duplicates
                
                if (filteredContacts.isNotEmpty()) {
                    item { SectionHeader("Other Contacts") }
                    items(filteredContacts) { user ->
                        GeneralContactItem(user) { onUserSelected(user.id) }
                    }
                }

                if (filteredTenants.isEmpty() && filteredContacts.isEmpty() && searchQuery.isNotEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                            Text("No results found for \"$searchQuery\"", color = Color.Gray)
                        }
                    }
                }
                
                item { Spacer(modifier = Modifier.height(40.dp)) }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        fontSize = 11.sp,
        fontWeight = FontWeight.Black,
        color = MaterialTheme.colorScheme.primary,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun PropertyChannelItem(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(44.dp).background(MaterialTheme.colorScheme.primary.copy(0.1f), CircleShape), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(subtitle, fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun TenantContactItem(tenant: TenantSummaryData, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(52.dp).clip(CircleShape).background(Brush.linearGradient(PremiumGradient)),
            contentAlignment = Alignment.Center
        ) {
            Text(tenant.name.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Black)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(tenant.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("Unit ${tenant.unitName} • ${tenant.propertyName}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun GeneralContactItem(user: User, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(52.dp).clip(CircleShape).background(Color.LightGray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Text(user.fullName.take(1).uppercase(), fontWeight = FontWeight.Bold, color = Color.Gray)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(user.fullName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(user.activeRole?.displayName ?: "User", fontSize = 12.sp, color = Color.Gray)
        }
    }
}
