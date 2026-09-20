package com.him.landlordtenant.app.ui.screens.landlord.houses

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.data.model.HouseStatus
import com.him.landlordtenant.app.ui.screens.tenant.HouseType
import com.him.landlordtenant.app.ui.viewmodel.landlord.EditHouseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHouseScreen(
    apartmentId: String,
    floorId: String,
    houseId: String,
    onBack: () -> Unit,
    onSave: () -> Unit,
    viewModel: EditHouseViewModel = hiltViewModel()
) {
    val house by viewModel.house.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var houseNumber by remember { mutableStateOf("") }
    var rent by remember { mutableStateOf("") }
    var deposit by remember { mutableStateOf("") }
    var initialReading by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(HouseStatus.VACANT) }

    LaunchedEffect(houseId) {
        viewModel.loadHouse(apartmentId, floorId, houseId)
    }

    LaunchedEffect(house) {
        house?.let {
            houseNumber = it.houseNumber
            rent = it.monthlyRent.toString()
            deposit = it.securityDeposit.toString()
            initialReading = it.initialWaterReading.toString()
            status = it.status
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Unit Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.updateHouse(
                                apartmentId = apartmentId,
                                floorId = floorId,
                                houseId = houseId,
                                houseNumber = houseNumber,
                                rent = rent.toDoubleOrNull() ?: 0.0,
                                deposit = deposit.toDoubleOrNull() ?: 0.0,
                                initialWaterReading = initialReading.toDoubleOrNull() ?: 0.0,
                                status = status,
                                onSuccess = onSave
                            )
                        },
                        enabled = !isLoading && houseNumber.isNotBlank()
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
            OutlinedTextField(
                value = houseNumber,
                onValueChange = { houseNumber = it },
                label = { Text("House Number") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = rent,
                onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) rent = it },
                label = { Text("Monthly Rent (KSh)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = deposit,
                onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) deposit = it },
                label = { Text("Security Deposit (KSh)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = initialReading,
                onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) initialReading = it },
                label = { Text("Initial Water Meter Reading") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text("Used for first bill calculation") }
            )

            Text("Current Occupancy Status", fontWeight = FontWeight.Black, fontSize = 16.sp)
            
            HouseStatus.entries.forEach { statusOption ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = status == statusOption, onClick = { status = statusOption })
                    Text(statusOption.displayName)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    viewModel.updateHouse(
                        apartmentId = apartmentId,
                        floorId = floorId,
                        houseId = houseId,
                        houseNumber = houseNumber,
                        rent = rent.toDoubleOrNull() ?: 0.0,
                        deposit = deposit.toDoubleOrNull() ?: 0.0,
                        initialWaterReading = initialReading.toDoubleOrNull() ?: 0.0,
                        status = status,
                        onSuccess = onSave
                    )
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading && houseNumber.isNotBlank()
            ) {
                if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                else Text("Save and Finish", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditHouseScreenPreview() {
    PropertyOSTheme {
        EditHouseScreen(apartmentId = "1", floorId = "1", houseId = "1", onBack = {}, onSave = {})
    }
}
