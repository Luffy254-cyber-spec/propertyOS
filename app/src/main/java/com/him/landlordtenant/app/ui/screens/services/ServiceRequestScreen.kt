package com.him.landlordtenant.app.ui.screens.services

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
fun ServiceRequestScreen(
    serviceType: String,
    onBack: () -> Unit,
    onSubmit: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Request $serviceType") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Service Location") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. House G2, Kitchen") }
            )
            
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Describe the issue") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth(),
                enabled = description.isNotBlank() && location.isNotBlank()
            ) {
                Text("Submit Request")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ServiceRequestScreenPreview() {
    PropertyOSTheme {
        ServiceRequestScreen("Plumbing", onBack = {}, onSubmit = {})
    }
}
