package com.him.landlordtenant.app.ui.screens.landlord.houses

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.HouseType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHouseScreen(
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    var houseNumber by remember { mutableStateOf("") }
    var rent by remember { mutableStateOf("") }
    var deposit by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(HouseType.BEDSITTER) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New House") },
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
            Text("House Information", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            
            OutlinedTextField(
                value = houseNumber,
                onValueChange = { houseNumber = it },
                label = { Text("House / Unit Number") },
                modifier = Modifier.fillMaxWidth()
            )
            
            HouseTypeSelector(selectedType) { selectedType = it }
            
            Spacer(modifier = Modifier.height(8.dp))
            Text("Financials", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            
            OutlinedTextField(
                value = rent,
                onValueChange = { rent = it },
                label = { Text("Monthly Rent (KSh)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            
            OutlinedTextField(
                value = deposit,
                onValueChange = { deposit = it },
                label = { Text("Security Deposit (KSh)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                enabled = houseNumber.isNotBlank() && rent.isNotBlank()
            ) {
                Text("Create Unit")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateHouseScreenPreview() {
    PropertyOSTheme {
        CreateHouseScreen(onBack = {}, onSave = {})
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
