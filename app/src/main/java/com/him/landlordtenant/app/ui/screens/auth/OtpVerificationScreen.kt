package com.him.landlordtenant.app.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@Composable
fun OtpVerificationScreen(
    phoneNumber: String,
    onVerify: (String) -> Unit,
    onResendOtp: () -> Unit,
) {
    var otpCode by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Enter Verification Code",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "We've sent a 6-digit code to $phoneNumber",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = otpCode,
            onValueChange = { if (it.length <= 6) otpCode = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Verification Code") },
            placeholder = { Text("000000") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center,
                letterSpacing = 8.sp,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            shape = MaterialTheme.shapes.medium
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = { onVerify(otpCode) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = otpCode.length == 6,
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Verify & Continue")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextButton(onClick = onResendOtp) {
            Text("Resend Code")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OtpVerificationScreenPreview() {
    PropertyOSTheme {
        OtpVerificationScreen(
            phoneNumber = "+254 700 000 000",
            onVerify = {},
            onResendOtp = {}
        )
    }
}
