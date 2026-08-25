package com.him.landlordtenant.app.ui.screens.intent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.components.intent.IntentHeader
import com.him.landlordtenant.app.ui.components.intent.QuickIntentItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntentSelectionScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("What would you like to do?") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item { IntentHeader(title = "Rent & Bills") }
            item {
                QuickIntentItem(
                    title = "Pay Rent",
                    subtitle = "Settle your monthly rent via M-Pesa",
                    icon = Icons.Default.Payment,
                    onClick = { /* TODO */ }
                )
            }
            item {
                QuickIntentItem(
                    title = "View Bills",
                    subtitle = "See outstanding utility and service bills",
                    icon = Icons.Default.ReceiptLong,
                    onClick = { /* TODO */ }
                )
            }
            
            item { Spacer(modifier = Modifier.height(16.dp)) }
            
            item { IntentHeader(title = "Maintenance") }
            item {
                QuickIntentItem(
                    title = "Report Issue",
                    subtitle = "Request repairs for your house unit",
                    icon = Icons.Default.Build,
                    onClick = { /* TODO */ }
                )
            }
            
            item { Spacer(modifier = Modifier.height(16.dp)) }
            
            item { IntentHeader(title = "Communication") }
            item {
                QuickIntentItem(
                    title = "Call Landlord",
                    subtitle = "Get in touch with property management",
                    icon = Icons.Default.Call,
                    onClick = { /* TODO */ }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IntentSelectionScreenPreview() {
    PropertyOSTheme {
        IntentSelectionScreen({})
    }
}
