package com.him.landlordtenant.app.ui.screens.landlord.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun VerificationScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Landlord Verification") },
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
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Verification Status",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            
            Surface(
                modifier = Modifier.padding(top = 8.dp),
                color = Color(0xFFE8F5E9),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "PARTIALLY VERIFIED",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    color = Color(0xFF2E7D32),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            VerificationDocItem("Owner Identity", "National ID / Passport verified.", true)
            VerificationDocItem("Property Ownership", "Title deeds or leasehold docs.", false)
            VerificationDocItem("Business Registration", "For companies managing units.", false)
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Button(
                onClick = { /* Upload docs */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.UploadFile, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Upload Documents")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VerificationScreenPreview() {
    PropertyOSTheme {
        VerificationScreen(onBack = {})
    }
}

@Composable
private fun VerificationDocItem(title: String, subtitle: String, verified: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Description,
                contentDescription = null,
                tint = if (verified) Color(0xFF2E7D32) else Color.Gray
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold)
                Text(subtitle, fontSize = 12.sp, color = Color.Gray)
            }
            if (verified) {
                Icon(Icons.Default.Verified, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
            } else {
                Text("Pending", fontSize = 10.sp, color = Color.Gray)
            }
        }
    }
}
