package com.him.landlordtenant.app.ui.screens.landlord.apartment

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.viewmodel.landlord.CreateApartmentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateApartmentScreen(
    onBack: () -> Unit,
    onCreated: (String) -> Unit,
    viewModel: CreateApartmentViewModel = hiltViewModel()
) {
    var step by remember { mutableStateOf(0) }
    
    // Step 0: Basic Info
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var county by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var totalUnits by remember { mutableStateOf("") }

    // Step 1: Agreement Rules & Proof
    var rules by remember { mutableStateOf("1. Rent is due on 1st of every month.\n2. One month notice is required before vacating.\n3. Keeping pets requires prior approval.\n4. No loud music after 10 PM.") }
    var agreementProofUri by remember { mutableStateOf<android.net.Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        agreementProofUri = uri
    }

    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (step == 0) "Property Information" else "Tenancy Agreement") },
                navigationIcon = {
                    IconButton(onClick = { if (step > 0) step-- else onBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (step == 0) {
                    Text(
                        "Basic Details",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    error?.let {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(12.dp), fontSize = 13.sp)
                        }
                    }
                    
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Property Name") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g. Green Valley Apartments") },
                        shape = RoundedCornerShape(12.dp)
                    )
                    
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location / Area") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g. Kilimani") },
                        shape = RoundedCornerShape(12.dp)
                    )
                    
                    OutlinedTextField(
                        value = county,
                        onValueChange = { county = it },
                        label = { Text("County") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g. Nairobi") },
                        shape = RoundedCornerShape(12.dp)
                    )
                    
                    OutlinedTextField(
                        value = totalUnits,
                        onValueChange = { if (it.all { char -> char.isDigit() }) totalUnits = it },
                        label = { Text("Total Units") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )
                    
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4,
                        shape = RoundedCornerShape(12.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = { step = 1 },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        enabled = name.isNotBlank() && location.isNotBlank() && county.isNotBlank() && totalUnits.isNotBlank(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Continue to Agreement", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text(
                        "Standard Tenancy Rules",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "These rules will be included in the default agreement for all units in this property.",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    OutlinedTextField(
                        value = rules,
                        onValueChange = { rules = it },
                        label = { Text("Property Rules (One per line)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 6,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Agreement Proof (Mandatory)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { imagePickerLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (agreementProofUri != null) {
                            AsyncImage(
                                model = agreementProofUri,
                                contentDescription = "Agreement Proof",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.AddPhotoAlternate, null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Upload Signed Agreement Proof", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Icon(Icons.Default.Gavel, null, tint = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Legal Disclaimer", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
                                Text(
                                    "This tenancy agreement is an official government document. Legal action can be taken against both parties if any terms of this agreement are violated. By creating this property, you acknowledge these terms.",
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { 
                            viewModel.createApartment(name, location, county, description, totalUnits, rules, agreementProofUri?.toString(), onCreated)
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        enabled = rules.isNotBlank() && agreementProofUri != null && !isLoading,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Finish and Create Property", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CreateApartmentScreenPreview() {
    PropertyOSTheme {
        CreateApartmentScreen(
            onBack = {},
            onCreated = {}
        )
    }
}
