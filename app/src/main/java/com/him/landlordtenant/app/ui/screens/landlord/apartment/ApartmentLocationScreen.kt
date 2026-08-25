package com.him.landlordtenant.app.ui.screens.landlord.apartment

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApartmentLocationScreen(
    apartmentId: String,
    onBack: () -> Unit,
    onSave: (Double, Double, String) -> Unit
) {
    var address by remember { mutableStateOf("Kilimani, Nairobi") }
    var selectedLocation by remember { mutableStateOf(LatLng(-1.2921, 36.8219)) } // Nairobi coordinates

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selectedLocation, 15f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Location") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Property Address", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. 123 Street Name, Town") }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(modifier = Modifier.fillMaxWidth().height(350.dp)) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    onMapClick = { latLng ->
                        selectedLocation = latLng
                    }
                ) {
                    Marker(
                        state = MarkerState(position = selectedLocation),
                        title = "Property Location",
                        draggable = true
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Tap on the map to select the property location precisely.",
                fontSize = 12.sp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = { onSave(selectedLocation.latitude, selectedLocation.longitude, address) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirm Location")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ApartmentLocationScreenPreview() {
    PropertyOSTheme {
        ApartmentLocationScreen("1", {}, { _, _, _ -> })
    }
}
