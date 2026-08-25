package com.him.landlordtenant.app.ui.screens.landlord.agreements

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@Composable
fun AgreementLockScreen(
    onUnlock: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Lock, null, modifier = Modifier.size(64.dp), tint = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))
        Text("Agreement is Locked")
        Text("It has already been signed by both parties.", color = Color.Gray)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onUnlock) { Text("Request Unlock") }
    }
}

@Preview(showBackground = true)
@Composable
fun AgreementLockScreenPreview() {
    PropertyOSTheme {
        AgreementLockScreen(onUnlock = {})
    }
}
