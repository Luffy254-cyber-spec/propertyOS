package com.him.landlordtenant.app.ui.screens.landlord.apartment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.viewmodel.landlord.PropertyManagementViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditApartmentScreen(
    apartmentId: String,
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: PropertyManagementViewModel = hiltViewModel()
) {
    val property by viewModel.property.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isNameAvailable by viewModel.isNameAvailable.collectAsState()
    val error by viewModel.error.collectAsState()

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    LaunchedEffect(apartmentId) {
        viewModel.loadProperty(apartmentId)
    }

    LaunchedEffect(property) {
        property?.let {
            name = it.name
            description = it.description ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Property") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp).padding(end = 16.dp))
                    TextButton(
                        onClick = { viewModel.updateBasicInfo(apartmentId, name, description, onSaveSuccess) },
                        enabled = !isLoading && name.isNotBlank() && isNameAvailable != false
                    ) {
                        Text("Save", fontWeight = FontWeight.Bold)
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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            error?.let {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(12.dp), fontSize = 13.sp)
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = { 
                    name = it
                    viewModel.validatePropertyName(it)
                },
                label = { Text("Property Name") },
                modifier = Modifier.fillMaxWidth(),
                isError = isNameAvailable == false,
                trailingIcon = {
                    if (isNameAvailable == true) {
                        Icon(Icons.Default.CheckCircle, "Available", tint = androidx.compose.ui.graphics.Color(0xFF2E7D32))
                    } else if (isNameAvailable == false) {
                        Icon(Icons.Default.Error, "Unavailable", tint = MaterialTheme.colorScheme.error)
                    }
                }
            )
            
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { viewModel.updateBasicInfo(apartmentId, name, description, onSaveSuccess) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !isLoading && name.isNotBlank() && isNameAvailable != false
            ) {
                Text("Update Property", fontWeight = FontWeight.Bold)
            }
        }
    }
}
