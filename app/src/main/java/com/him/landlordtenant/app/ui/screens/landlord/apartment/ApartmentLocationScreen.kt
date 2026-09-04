package com.him.landlordtenant.app.ui.screens.landlord.apartment

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.him.landlordtenant.app.ui.viewmodel.landlord.PropertyManagementViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApartmentLocationScreen(
    apartmentId: String,
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: PropertyManagementViewModel = hiltViewModel()
) {
    var address by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf(LatLng(-1.286389, 36.817223)) }
    val isLoading by viewModel.isLoading.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selectedLocation, 12f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Location") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp).padding(end = 16.dp))
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp)) {
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Physical Address") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(modifier = Modifier.fillMaxWidth().weight(1f)) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    onMapClick = { selectedLocation = it }
                ) {
                    Marker(state = MarkerState(position = selectedLocation))
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { viewModel.updateLocation(apartmentId, selectedLocation.latitude, selectedLocation.longitude, address, onSaveSuccess) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !isLoading
            ) {
                Text("Save Changes", fontWeight = FontWeight.Bold)
            }
        }
    }
}
