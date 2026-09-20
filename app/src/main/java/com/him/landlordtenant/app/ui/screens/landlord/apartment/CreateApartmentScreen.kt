package com.him.landlordtenant.app.ui.screens.landlord.apartment

import android.Manifest
import android.location.Geocoder
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.viewmodel.landlord.CreateApartmentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@Composable
fun CreateApartmentScreen(
    onBack: () -> Unit,
    onCreated: (String) -> Unit,
    viewModel: CreateApartmentViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isNameAvailable by viewModel.isNameAvailable.collectAsState()
    val isCheckingName by viewModel.isCheckingName.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    var createdPropertyId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(isNameAvailable) {
        if (isNameAvailable == false) {
            snackbarHostState.showSnackbar("Property name is already taken. Please choose another.")
        }
    }

    if (createdPropertyId != null) {
        PropertyCreatedSuccessContent(
            onSave = { onCreated(createdPropertyId!!) },
            onBackToDashboard = onBack
        )
    } else {
        CreateApartmentContent(
            onBack = onBack,
            isLoading = isLoading,
            error = error,
            isNameAvailable = isNameAvailable,
            isCheckingName = isCheckingName,
            snackbarHostState = snackbarHostState,
            onNameChange = { viewModel.validatePropertyName(it) },
            onCreateApartment = { name, type, rent, location, county, description, amenities, images, rules, proofUri, latitude, longitude ->
                viewModel.createApartment(
                    name = name,
                    propertyType = type,
                    startingRent = rent,
                    location = location,
                    county = county,
                    description = description,
                    amenities = amenities,
                    propertyImages = images,
                    rules = rules,
                    proofUri = proofUri,
                    latitude = latitude,
                    longitude = longitude,
                    onSuccess = { id -> createdPropertyId = id }
                )
            },
            setError = { viewModel.setError(it) }
        )
    }
}

@Composable
fun PropertyCreatedSuccessContent(
    onSave: () -> Unit,
    onBackToDashboard: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFFE8F5E9), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CheckCircle, 
                    null, 
                    modifier = Modifier.size(64.dp), 
                    tint = Color(0xFF2E7D32)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Success!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Your property has been created. Click Save to finalize and view it in your dashboard.",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                lineHeight = 20.sp
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Icon(Icons.Default.Save, null)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Save & Manage Property", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TextButton(
                onClick = onBackToDashboard,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Go to Dashboard", color = Color.Gray)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreateApartmentContent(
    onBack: () -> Unit,
    isLoading: Boolean,
    error: String?,
    isNameAvailable: Boolean?,
    isCheckingName: Boolean,
    snackbarHostState: SnackbarHostState,
    onNameChange: (String) -> Unit,
    onCreateApartment: (String, String, Double, String, String, String, List<String>, List<String>, String, String?, Double, Double) -> Unit,
    setError: (String) -> Unit
) {
    var step by remember { mutableStateOf(0) }
    
    // Form Data
    var name by remember { mutableStateOf("") }
    var propertyType by remember { mutableStateOf("Apartment") }
    var startingRent by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var county by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val selectedAmenities = remember { mutableStateListOf<String>() }
    val propertyImageUris = remember { mutableStateListOf<android.net.Uri>() }
    var rules by remember { mutableStateOf("1. Rent is due on 1st of every month.\n2. One month notice is required before vacating.\n3. Keeping pets requires prior approval.\n4. No loud music after 10 PM.") }
    var agreementProofUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var pinnedLocation by remember { mutableStateOf<LatLng?>(null) }

    // State
    var isFullscreenMap by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-1.286389, 36.817223), 12f)
    }

    // Handle back button
    BackHandler(enabled = isFullscreenMap) { isFullscreenMap = false }

    var hasLocationPermission by remember {
        mutableStateOf(
            context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions.values.all { it }
        if (hasLocationPermission) {
            scope.launch {
                try {
                    LocationServices.getFusedLocationProviderClient(context)
                        .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                        .addOnSuccessListener { loc ->
                            loc?.let {
                                val currentLatLng = LatLng(it.latitude, it.longitude)
                                scope.launch { cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f)) }
                            }
                        }
                } catch (e: SecurityException) { setError("Location permission error.") }
            }
        }
    }

    val uiSettings = remember(hasLocationPermission) {
        MapUiSettings(myLocationButtonEnabled = hasLocationPermission, zoomControlsEnabled = true)
    }
    val mapProperties = remember(hasLocationPermission) {
        MapProperties(isMyLocationEnabled = hasLocationPermission)
    }

    val multipleImagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { propertyImageUris.addAll(it) }
    val proofPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { agreementProofUri = it }

    Box(modifier = Modifier.fillMaxSize()) {
        if (!isFullscreenMap) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(listOf("Property Details", "Amenities", "Photos", "Agreement", "Location")[step]) },
                        navigationIcon = {
                            IconButton(onClick = { if (step > 0) step-- else onBack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                            }
                        },
                        actions = {
                            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp).padding(end = 16.dp), strokeWidth = 2.dp)
                            Text("${step + 1}/5", modifier = Modifier.padding(end = 16.dp), style = MaterialTheme.typography.bodySmall)
                        }
                    )
                },
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ) { padding ->
                Column(
                    modifier = Modifier.fillMaxSize().padding(padding).imePadding().verticalScroll(rememberScrollState()).padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    error?.let {
                        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                            Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(12.dp), fontSize = 13.sp)
                        }
                    }

                    when(step) {
                        0 -> {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { 
                                    name = it
                                    onNameChange(it)
                                },
                                label = { Text("Property Name") },
                                modifier = Modifier.fillMaxWidth(),
                                isError = isNameAvailable == false,
                                supportingText = {
                                    if (isNameAvailable == false) {
                                        Text("This property name already exists. Please choose a unique name.", color = MaterialTheme.colorScheme.error)
                                    }
                                },
                                trailingIcon = {
                                    if (isCheckingName) {
                                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                    } else if (isNameAvailable == true) {
                                        Icon(Icons.Default.CheckCircle, "Available", tint = Color(0xFF2E7D32))
                                    } else if (isNameAvailable == false) {
                                        Icon(Icons.Default.Error, "Already Taken", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            )
                            OutlinedTextField(value = startingRent, onValueChange = { startingRent = it }, label = { Text("Starting Rent (KSh)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number))
                            OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Area/Town") }, modifier = Modifier.fillMaxWidth())
                            OutlinedTextField(value = county, onValueChange = { county = it }, label = { Text("County") }, modifier = Modifier.fillMaxWidth())
                            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), minLines = 4)
                            Button(
                                onClick = { step = 1 },
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                enabled = name.isNotBlank() && startingRent.isNotBlank() && isNameAvailable != false
                            ) { Text("Next: Amenities") }
                        }
                        1 -> {
                            Text("Amenities", fontWeight = FontWeight.Bold)
                            FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("WIFI", "PARKING", "SECURITY", "CCTV", "WATER", "ELECTRICITY", "GYM", "SWIMMING_POOL").forEach { amenity ->
                                    FilterChip(
                                        selected = selectedAmenities.contains(amenity),
                                        onClick = { if (selectedAmenities.contains(amenity)) selectedAmenities.remove(amenity) else selectedAmenities.add(amenity) },
                                        label = { Text(amenity) }
                                    )
                                }
                            }
                            Button(onClick = { step = 2 }, modifier = Modifier.fillMaxWidth().height(56.dp)) { Text("Next: Photos") }
                        }
                        2 -> {
                            Text("Property Photos", fontWeight = FontWeight.Bold)
                            FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                propertyImageUris.forEach { uri ->
                                    Box(modifier = Modifier.size(100.dp).clip(RoundedCornerShape(8.dp))) {
                                        AsyncImage(model = uri, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                                        IconButton(onClick = { propertyImageUris.remove(uri) }, modifier = Modifier.align(Alignment.TopEnd).size(24.dp).background(Color.Black.copy(alpha = 0.5f), CircleShape)) {
                                            Icon(Icons.Default.Close, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                                Box(modifier = Modifier.size(100.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.surfaceVariant).clickable { multipleImagePicker.launch("image/*") }.border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.AddAPhoto, null, tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                            Button(onClick = { step = 3 }, modifier = Modifier.fillMaxWidth().height(56.dp), enabled = propertyImageUris.isNotEmpty()) { Text("Next: Agreement") }
                        }
                        3 -> {
                            OutlinedTextField(value = rules, onValueChange = { rules = it }, label = { Text("Property Rules") }, modifier = Modifier.fillMaxWidth(), minLines = 6)
                            Text("Agreement Proof (Required)", fontWeight = FontWeight.Bold)
                            Box(modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surfaceVariant).clickable { proofPicker.launch("image/*") }, contentAlignment = Alignment.Center) {
                                if (agreementProofUri != null) AsyncImage(model = agreementProofUri, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                                else Icon(Icons.Default.AddPhotoAlternate, null, modifier = Modifier.size(48.dp))
                            }
                            Button(onClick = { if (agreementProofUri == null) setError("Please upload proof") else step = 4 }, modifier = Modifier.fillMaxWidth().height(56.dp)) { Text("Next: Location") }
                        }
                        4 -> {
                            OutlinedTextField(
                                value = searchQuery, onValueChange = { searchQuery = it }, modifier = Modifier.fillMaxWidth(), placeholder = { Text("Search location...") },
                                trailingIcon = {
                                    IconButton(onClick = {
                                        scope.launch {
                                            withContext(Dispatchers.IO) {
                                                try {
                                                    val addrs = Geocoder(context, Locale.getDefault()).getFromLocationName(searchQuery, 1)
                                                    if (!addrs.isNullOrEmpty()) {
                                                        val latLng = LatLng(addrs[0].latitude, addrs[0].longitude)
                                                        withContext(Dispatchers.Main) { cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 15f)) }
                                                    }
                                                } catch (e: Exception) {}
                                            }
                                        }
                                    }) { Icon(Icons.Default.Search, null) }
                                }
                            )

                            Box(modifier = Modifier.fillMaxWidth().height(300.dp).clip(RoundedCornerShape(16.dp)).background(Color.LightGray).border(1.dp, Color.Gray, RoundedCornerShape(16.dp))) {
                                GoogleMap(
                                    modifier = Modifier.fillMaxSize(),
                                    cameraPositionState = cameraPositionState,
                                    properties = mapProperties,
                                    uiSettings = uiSettings,
                                    onMapClick = { pinnedLocation = it }
                                ) {
                                    pinnedLocation?.let { Marker(state = MarkerState(position = it)) }
                                }
                                SmallFloatingActionButton(
                                    onClick = { isFullscreenMap = true },
                                    modifier = Modifier.align(Alignment.TopEnd).padding(12.dp),
                                    containerColor = Color.White.copy(alpha = 0.9f)
                                ) { Icon(Icons.Default.OpenInFull, "Expand") }
                            }

                            if (pinnedLocation != null) Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)), modifier = Modifier.fillMaxWidth()) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocationOn, null, tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Location Pinned", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }

                            Button(
                                onClick = { onCreateApartment(name, propertyType, startingRent.toDoubleOrNull() ?: 0.0, location, county, description, selectedAmenities.toList(), propertyImageUris.map { it.toString() }, rules, agreementProofUri?.toString(), pinnedLocation?.latitude ?: 0.0, pinnedLocation?.longitude ?: 0.0) },
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                enabled = pinnedLocation != null && !isLoading
                            ) {
                                if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                                else Text("Create Property", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Fullscreen Overlay (Completely separate branch to avoid crashes)
        if (isFullscreenMap) {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                Column {
                    TopAppBar(
                        title = { Text("Pin Property Location") },
                        navigationIcon = { IconButton(onClick = { isFullscreenMap = false }) { Icon(Icons.Default.Close, null) } },
                        actions = { TextButton(onClick = { isFullscreenMap = false }) { Text("DONE", fontWeight = FontWeight.Bold) } }
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState,
                            properties = mapProperties,
                            uiSettings = uiSettings,
                            onMapClick = { pinnedLocation = it }
                        ) {
                            pinnedLocation?.let { Marker(state = MarkerState(position = it)) }
                        }
                        FloatingActionButton(
                            onClick = { permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)) },
                            modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp),
                            containerColor = MaterialTheme.colorScheme.primary
                        ) { Icon(Icons.Default.MyLocation, null) }
                    }
                }
            }
        }
    }
}
