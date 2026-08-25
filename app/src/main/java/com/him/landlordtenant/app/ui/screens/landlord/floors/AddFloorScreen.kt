package com.him.landlordtenant.app.ui.screens.landlord.floors

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
fun AddFloorScreen(
    onBack: () -> Unit,
    onSave: (Int, String) -> Unit
) {
    var floorNumber by remember { mutableStateOf("") }
    var floorName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Floor") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                value = floorNumber,
                onValueChange = { floorNumber = it },
                label = { Text("Floor Number") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = floorName,
                onValueChange = { floorName = it },
                label = { Text("Floor Name (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. Ground Floor, Penthouse") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onSave(floorNumber.toIntOrNull() ?: 0, floorName) },
                modifier = Modifier.fillMaxWidth(),
                enabled = floorNumber.isNotBlank()
            ) {
                Text("Add Floor")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddFloorScreenPreview() {
    PropertyOSTheme {
        AddFloorScreen(onBack = {}, onSave = { _, _ -> })
    }
}
