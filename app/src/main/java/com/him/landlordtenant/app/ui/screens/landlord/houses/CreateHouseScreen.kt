package com.him.landlordtenant.app.ui.screens.landlord.houses

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.HouseType
import com.him.landlordtenant.app.ui.viewmodel.landlord.FloorsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHouseScreen(
    apartmentId: String,
    floorId: String,
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: FloorsViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()

    CreateHouseContent(
        apartmentId = apartmentId,
        floorId = floorId,
        isLoading = isLoading,
        onBack = onBack,
        onSave = { houseNumber, selectedType, rent, deposit, initialReading, description, mediaUris ->
            viewModel.addUnitWithMedia(
                apartmentId = apartmentId,
                floorId = floorId,
                houseNumber = houseNumber,
                houseType = selectedType,
                rent = rent,
                deposit = deposit,
                description = description,
                initialWaterReading = initialReading,
                mediaUris = mediaUris,
                onSuccess = onSaveSuccess
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHouseContent(
    apartmentId: String,
    floorId: String,
    isLoading: Boolean,
    onBack: () -> Unit,
    onSave: (String, HouseType, Double, Double, Double, String, List<String>) -> Unit
) {
    var houseNumber by remember { mutableStateOf("") }
    var rent by remember { mutableStateOf("") }
    var deposit by remember { mutableStateOf("") }
    var initialReading by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(HouseType.ONE_BEDROOM) }
    
    val selectedMediaUris = remember { mutableStateListOf<android.net.Uri>() }
    var showMediaDialog by remember { mutableStateOf(false) }

    val galleryPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        selectedMediaUris.addAll(uris)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        // Note: Real implementation should save bitmap to a file and get URI
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add House to $floorId") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
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
            Text("House Information", fontWeight = FontWeight.Black, fontSize = 18.sp)
            
            OutlinedTextField(
                value = houseNumber,
                onValueChange = { houseNumber = it },
                label = { Text("House / Unit Number (e.g. A1)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            
            HouseTypeSelector(selectedType) { selectedType = it }
            
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("Describe the house features, balcony, view, etc.") }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            Text("Financials", fontWeight = FontWeight.Black, fontSize = 18.sp)
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = rent,
                    onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) rent = it },
                    label = { Text("Rent (KES)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(12.dp)
                )
                
                OutlinedTextField(
                    value = deposit,
                    onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) deposit = it },
                    label = { Text("Deposit (KES)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            OutlinedTextField(
                value = initialReading,
                onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) initialReading = it },
                label = { Text("Initial Water Meter Reading") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("Used for first bill calculation") }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            Text("House Media", fontWeight = FontWeight.Black, fontSize = 18.sp)
            
            if (selectedMediaUris.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { showMediaDialog = true }
                        .border(1.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AddAPhoto, null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
                        Text("Add Photos & Videos", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            } else {
                androidx.compose.foundation.lazy.LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(selectedMediaUris.size) { index ->
                        val uri = selectedMediaUris[index]
                        Box(modifier = Modifier.size(120.dp).clip(RoundedCornerShape(12.dp))) {
                            AsyncImage(model = uri, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                            IconButton(
                                onClick = { selectedMediaUris.removeAt(index) },
                                modifier = Modifier.align(Alignment.TopEnd).size(24.dp).background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            ) {
                                Icon(Icons.Default.Close, null, tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                    item {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { showMediaDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, null, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = { 
                    onSave(
                        houseNumber,
                        selectedType,
                        rent.toDoubleOrNull() ?: 0.0,
                        deposit.toDoubleOrNull() ?: 0.0,
                        initialReading.toDoubleOrNull() ?: 0.0,
                        description,
                        selectedMediaUris.map { it.toString() }
                    )
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = houseNumber.isNotBlank() && rent.isNotBlank() && !isLoading
            ) {
                if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("Create & Save Unit", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    if (showMediaDialog) {
        AlertDialog(
            onDismissRequest = { showMediaDialog = false },
            title = { Text("Add Media") },
            text = {
                Column {
                    ListItem(
                        headlineContent = { Text("Camera") },
                        leadingContent = { Icon(Icons.Default.PhotoCamera, null) },
                        modifier = Modifier.clickable { 
                            showMediaDialog = false
                            cameraLauncher.launch(null)
                        }
                    )
                    ListItem(
                        headlineContent = { Text("Gallery (Photos & Videos)") },
                        leadingContent = { Icon(Icons.Default.Collections, null) },
                        modifier = Modifier.clickable { 
                            showMediaDialog = false
                            galleryPicker.launch("*/*") 
                        }
                    )
                }
            },
            confirmButton = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreateHouseScreenPreview() {
    PropertyOSTheme {
        CreateHouseContent(
            apartmentId = "1",
            floorId = "1",
            isLoading = false,
            onBack = {},
            onSave = { _, _, _, _, _, _, _ -> }
        )
    }
}

@Composable
private fun HouseTypeSelector(selected: HouseType, onSelected: (HouseType) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedTextField(
            value = selected.name.replace("_", " "),
            onValueChange = {},
            readOnly = true,
            label = { Text("House Type") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, null)
                }
            }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            HouseType.values().forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.name.replace("_", " ")) },
                    onClick = { onSelected(type); expanded = false }
                )
            }
        }
    }
}
