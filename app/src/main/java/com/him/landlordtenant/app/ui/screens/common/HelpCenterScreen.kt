package com.him.landlordtenant.app.ui.screens.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpCenterScreen(
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    
    val faqs = remember {
        listOf(
            "How do I pay rent?",
            "What if I lose my house keys?",
            "How to report maintenance issues?",
            "Can I cancel a vacate notice?",
            "How to update my profile information?",
            "What documents are required to join?"
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help Center") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Text(
                    text = "How can we help you?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search help topics...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    shape = MaterialTheme.shapes.medium
                )
            }
            
            item {
                Text(
                    text = "Frequently Asked Questions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            
            items(faqs.filter { it.contains(searchQuery, ignoreCase = true) }) { faq ->
                FaqItem(faq)
            }
            
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Still need help?",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Our support team is available 24/7.",
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { /* Navigate to contact */ }) {
                            Text("Contact Support")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HelpCenterScreenPreview() {
    PropertyOSTheme {
        HelpCenterScreen(onBack = {})
    }
}

@Composable
private fun FaqItem(question: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle FAQ click */ },
        shape = MaterialTheme.shapes.small,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = question,
                modifier = Modifier.weight(1f),
                fontSize = 14.sp
            )
            Icon(Icons.Default.ChevronRight, null, modifier = Modifier.size(20.dp))
        }
    }
}
