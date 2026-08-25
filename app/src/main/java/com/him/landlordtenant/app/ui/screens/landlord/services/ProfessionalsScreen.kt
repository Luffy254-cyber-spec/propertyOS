package com.him.landlordtenant.app.ui.screens.landlord.services

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Star
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
fun ProfessionalsScreen(
    onBack: () -> Unit,
    onProfessionalClick: (String) -> Unit
) {
    val professionals = listOf("Plumber John", "Electrician Sam", "Painter Mike")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Service Professionals") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(professionals) { prof ->
                ProfessionalCard(prof) { onProfessionalClick(prof) }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfessionalsScreenPreview() {
    PropertyOSTheme {
        ProfessionalsScreen(onBack = {}, onProfessionalClick = {})
    }
}

@Composable
private fun ProfessionalCard(name: String, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Build, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, fontWeight = FontWeight.Bold)
                Text(text = "Verified Pro", fontSize = 12.sp, color = Color.Gray)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                Text(text = "4.8", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}
