package com.him.landlordtenant.app.ui.screens.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.viewmodel.common.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onLogout: () -> Unit,
    onSwitchRole: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val userPreferences by viewModel.userPreferences.collectAsState()

    SettingsContent(
        onBack = onBack,
        onNavigateToAbout = onNavigateToAbout,
        onNavigateToPrivacy = onNavigateToPrivacy,
        onNavigateToTerms = onNavigateToTerms,
        onNavigateToHelp = onNavigateToHelp,
        onLogout = onLogout,
        onSwitchRole = onSwitchRole,
        useDarkMode = userPreferences.useDarkMode,
        pushNotifications = userPreferences.pushNotifications,
        onToggleDarkMode = { viewModel.toggleDarkMode(it) },
        onTogglePushNotifications = { viewModel.togglePushNotifications(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    onBack: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onLogout: () -> Unit,
    onSwitchRole: () -> Unit,
    useDarkMode: Boolean,
    pushNotifications: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    onTogglePushNotifications: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            SettingsSectionTitle("Account & Security")
            SettingsClickItem(Icons.Default.SyncAlt, "Switch Dashboard Role") { onSwitchRole() }
            SettingsToggleItem(
                icon = Icons.Default.Notifications,
                title = "Push Notifications",
                checked = pushNotifications
            ) { onTogglePushNotifications(it) }
            
            SettingsClickItem(Icons.Default.Lock, "Change Password") { /* Navigate */ }
            SettingsClickItem(Icons.Default.Fingerprint, "Biometric Auth") { /* Navigate */ }
            
            SettingsSectionTitle("Appearance")
            SettingsToggleItem(
                icon = Icons.Default.DarkMode,
                title = "Dark Mode",
                checked = useDarkMode
            ) { onToggleDarkMode(it) }
            
            SettingsSectionTitle("Information")
            SettingsClickItem(Icons.Default.Help, "Help Center", onNavigateToHelp)
            SettingsClickItem(Icons.Default.PrivacyTip, "Privacy Policy", onNavigateToPrivacy)
            SettingsClickItem(Icons.Default.Gavel, "Terms of Service", onNavigateToTerms)
            SettingsClickItem(Icons.Default.Info, "About", onNavigateToAbout)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.Default.Logout, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout")
            }
            
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    PropertyOSTheme {
        SettingsContent(
            onBack = {},
            onNavigateToAbout = {},
            onNavigateToPrivacy = {},
            onNavigateToTerms = {},
            onNavigateToHelp = {},
            onLogout = {},
            onSwitchRole = {},
            useDarkMode = false,
            pushNotifications = true,
            onToggleDarkMode = {},
            onTogglePushNotifications = {}
        )
    }
}


@Composable
private fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun SettingsToggleItem(icon: ImageVector, title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, modifier = Modifier.weight(1f), fontSize = 16.sp)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}


@Composable
private fun SettingsClickItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, modifier = Modifier.weight(1f), fontSize = 16.sp)
        Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
    }
}
