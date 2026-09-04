package com.him.landlordtenant.app.ui.screens.tenant.vault

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.screens.tenant.TenantDocumentUIModel
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentVaultScreen(
    onBack: () -> Unit
) {
    val documents = listOf(
        TenantDocumentUIModel("1", "National ID Copy", "ID", "url1", "24 May 2026"),
        TenantDocumentUIModel("2", "Lease Agreement", "LEASE", "url2", "01 Jun 2026"),
        TenantDocumentUIModel("3", "August Rent Receipt", "RECEIPT", "url3", "05 Aug 2026"),
        TenantDocumentUIModel("4", "Security Deposit Receipt", "RECEIPT", "url4", "30 May 2026")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Document Vault", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Upload */ }) {
                        Icon(Icons.Default.UploadFile, "Upload")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Upload */ }, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Default.Add, "Add Document", tint = Color.White)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Store and manage your essential tenancy documents securely in one place.",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            items(documents) { doc ->
                DocumentItem(doc)
            }
        }
    }
}

@Composable
private fun DocumentItem(doc: TenantDocumentUIModel) {
    val icon = when (doc.type) {
        "ID" -> Icons.Default.Badge
        "LEASE" -> Icons.Default.Description
        "RECEIPT" -> Icons.Default.ReceiptLong
        else -> Icons.Default.InsertDriveFile
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { /* TODO: View */ },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(doc.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Added on ${doc.date}", fontSize = 12.sp, color = Color.Gray)
            }
            IconButton(onClick = { /* TODO: Download */ }) {
                Icon(Icons.Default.Download, null, tint = Color.LightGray)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DocumentVaultScreenPreview() {
    PropertyOSTheme {
        DocumentVaultScreen({})
    }
}
