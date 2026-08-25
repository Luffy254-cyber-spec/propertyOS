package com.him.landlordtenant.app.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@Composable
fun EmailVerificationScreen(
    email: String,
    onContinue: () -> Unit,
    onResendEmail: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Email,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Verify your email",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "We've sent a verification link to $email. Please check your inbox and follow the instructions.",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(40.dp))
        
        Button(
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("I've Verified My Email")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextButton(onClick = onResendEmail) {
            Text("Resend Verification Email")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmailVerificationScreenPreview() {
    PropertyOSTheme {
        EmailVerificationScreen(
            email = "user@example.com",
            onContinue = {},
            onResendEmail = {}
        )
    }
}
