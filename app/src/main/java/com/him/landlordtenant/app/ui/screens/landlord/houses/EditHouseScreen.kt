package com.him.landlordtenant.app.ui.screens.landlord.houses

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import com.him.landlordtenant.app.ui.screens.tenant.HouseStatus
import com.him.landlordtenant.app.ui.screens.tenant.HouseType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHouseScreen(
    houseId: String,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    var houseNumber by remember { mutableStateOf("G2") }
    var rent by remember { mutableStateOf("15000") }
    var selectedStatus by remember { mutableStateOf(HouseStatus.VACANT) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Unit Details") },
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
            OutlinedTextField(
                value = houseNumber,
                onValueChange = { houseNumber = it },
                label = { Text("House Number") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = rent,
                onValueChange = { rent = it },
                label = { Text("Monthly Rent (KSh)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update Unit")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditHouseScreenPreview() {
    PropertyOSTheme {
        EditHouseScreen(houseId = "1", onBack = {}, onSave = {})
    }
}
