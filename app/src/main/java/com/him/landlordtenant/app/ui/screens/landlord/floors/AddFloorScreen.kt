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

import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.viewmodel.landlord.FloorsViewModel

import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFloorScreen(
    apartmentId: String,
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: FloorsViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()

    AddFloorContent(
        apartmentId = apartmentId,
        isLoading = isLoading,
        onBack = onBack,
        onSave = { floorNumber, floorName ->
            viewModel.addFloor(
                apartmentId = apartmentId,
                floorNumber = floorNumber,
                floorName = floorName,
                onSuccess = onSaveSuccess
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFloorContent(
    apartmentId: String,
    isLoading: Boolean,
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
                onClick = { 
                    onSave(floorNumber.toIntOrNull() ?: 0, floorName)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = floorNumber.isNotBlank() && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Add Floor", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddFloorScreenPreview() {
    PropertyOSTheme {
        AddFloorContent(
            apartmentId = "1",
            isLoading = false,
            onBack = {},
            onSave = { _, _ -> }
        )
    }
}

