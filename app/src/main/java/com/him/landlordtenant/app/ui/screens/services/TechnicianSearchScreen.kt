package com.him.landlordtenant.app.ui.screens.services

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechnicianSearchScreen(
    onBack: () -> Unit,
    onTechnicianClick: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Find Technicians") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                placeholder = { Text("Search by name or skill...") },
                leadingIcon = { Icon(Icons.Default.Search, null) }
            )
            
            // Implement technician list results
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("Search results appear here", color = androidx.compose.ui.graphics.Color.Gray)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TechnicianSearchScreenPreview() {
    PropertyOSTheme {
        TechnicianSearchScreen({}, {})
    }
}
