package com.him.landlordtenant.app.ui.screens.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsOfServiceScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Terms of Service") },
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
                .padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Gavel,
                contentDescription = null,
                modifier = Modifier.size(64.dp).align(Alignment.CenterHorizontally),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Terms & Conditions",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            TermsSection("1. Acceptance of Terms", "By using propertyOS, you agree to comply with these terms. If you do not agree, please refrain from using the application.")
            TermsSection("2. User Accounts", "You are responsible for maintaining the confidentiality of your account credentials and all activities under your account.")
            TermsSection("3. Property Listings", "Landlords must provide accurate property details. Fraudulent or misleading listings will be removed, and accounts may be suspended.")
            TermsSection("4. Payments", "Payments made through the app are processed securely. propertyOS is not responsible for disputes arising from external payment channels.")
            TermsSection("5. Maintenance Requests", "Tenants should report issues promptly. Landlords are expected to address reasonable maintenance requests within a timely manner.")
            TermsSection("6. Limitation of Liability", "propertyOS is a platform facilitator and is not liable for direct or indirect damages resulting from property disputes or usage.")
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "By continuing, you agree to these terms.",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TermsOfServiceScreenPreview() {
    PropertyOSTheme {
        TermsOfServiceScreen(onBack = {})
    }
}

@Composable
private fun TermsSection(title: String, content: String) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = content,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}
