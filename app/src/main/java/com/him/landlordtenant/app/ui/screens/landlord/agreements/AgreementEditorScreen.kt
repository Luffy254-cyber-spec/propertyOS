package com.him.landlordtenant.app.ui.screens.landlord.agreements

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgreementEditorScreen(
    onBack: () -> Unit,
    onSave: (String) -> Unit
) {
    var content by remember { mutableStateOf("Agreement content here...") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Template") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { onSave(content) }) {
                        Text("Done")
                    }
                }
            )
        }
    ) { padding ->
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            label = { Text("Template Content") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AgreementEditorScreenPreview() {
    PropertyOSTheme {
        AgreementEditorScreen(onBack = {}, onSave = {})
    }
}
