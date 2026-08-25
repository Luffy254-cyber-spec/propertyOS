package com.him.landlordtenant.app.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdentityVerificationScreen(
    onBack: () -> Unit,
    onComplete: () -> Unit,
) {
    var idNumber by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Identity Verification") },
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Badge,
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Verify your identity",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "To ensure security and trust within propertyOS, we need to verify your official identification.",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            OutlinedTextField(
                value = idNumber,
                onValueChange = { idNumber = it },
                label = { Text("National ID / Passport Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { /* Open Camera */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(Icons.Default.CameraAlt, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Scan Document")
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Button(
                onClick = onComplete,
                modifier = Modifier.fillMaxWidth(),
                enabled = idNumber.isNotBlank(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Submit for Review")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IdentityVerificationScreenPreview() {
    PropertyOSTheme {
        IdentityVerificationScreen(
            onBack = {},
            onComplete = {}
        )
    }
}
