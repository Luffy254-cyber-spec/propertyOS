package com.him.landlordtenant.app.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.withTimeout
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@Composable
fun GoogleAuthScreen(
    onTokenReceived: (String) -> Unit,
    onAuthFailure: (String) -> Unit,
) {
    val context = LocalContext.current
    val credentialManager = CredentialManager.create(context)

    LaunchedEffect(Unit) {
        try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("491772819372-m0sapiosd6jace0rfbfq66k8qdsdv6f1.apps.googleusercontent.com")
                .setAutoSelectEnabled(true)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = withTimeout(30000) { 
                credentialManager.getCredential(context, request) 
            }
            val credential = result.credential

            if (credential is GoogleIdTokenCredential) {
                onTokenReceived(credential.idToken)
            } else {
                onAuthFailure("Unexpected credential type")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val errorMessage = if (e is androidx.credentials.exceptions.GetCredentialException) {
                "Google Sign-In Error (Type: ${e.type}): ${e.message}"
            } else {
                "Error: ${e.javaClass.simpleName} - ${e.message ?: "Unknown"}"
            }
            onAuthFailure(errorMessage)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Connecting to Google...",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GoogleAuthScreenPreview() {
    PropertyOSTheme {
        GoogleAuthScreen(
            onTokenReceived = {},
            onAuthFailure = {}
        )
    }
}
