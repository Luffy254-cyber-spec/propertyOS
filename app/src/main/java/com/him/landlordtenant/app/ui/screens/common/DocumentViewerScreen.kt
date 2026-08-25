package com.him.landlordtenant.app.ui.screens.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentViewerScreen(
    title: String,
    url: String,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle share */ }) {
                        Icon(Icons.Default.Share, "Share")
                    }
                    IconButton(onClick = { /* Handle download */ }) {
                        Icon(Icons.Default.Download, "Download")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder for PDF/Image viewer
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Viewing document from:", color = Color.Gray)
                Text(url, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(24.dp))
                CircularProgressIndicator()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DocumentViewerScreenPreview() {
    PropertyOSTheme {
        DocumentViewerScreen(
            title = "Lease Agreement",
            url = "https://example.com/lease.pdf",
            onBack = {}
        )
    }
}
