package com.him.landlordtenant.app.ui.screens.tenant.reviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.screens.tenant.PropertyReviewUIModel
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyReviewsScreen(
    onBack: () -> Unit
) {
    val reviews = listOf(
        PropertyReviewUIModel("1", "Alice Kamau", 5, "Amazing experience! The landlord is very responsive and the facilities are top-notch.", "24 Aug 2026"),
        PropertyReviewUIModel("2", "John Mutua", 4, "Great place to stay, quiet and secure. Only downside is the water pressure sometimes.", "15 Aug 2026"),
        PropertyReviewUIModel("3", "Sarah Wanjiku", 5, "I love it here! The community is very friendly.", "02 Aug 2026")
    )

    var showAddReview by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Property Reviews", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddReview = true }, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Default.RateReview, "Add Review", tint = Color.White)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ReviewSummary(reviews)
            }

            item {
                Text("Tenant Stories", fontWeight = FontWeight.Black, fontSize = 16.sp, modifier = Modifier.padding(top = 16.dp))
            }

            items(reviews) { review ->
                ReviewCard(review)
            }
        }
    }

    if (showAddReview) {
        AddReviewDialog(onDismiss = { showAddReview = false })
    }
}

@Composable
private fun ReviewSummary(reviews: List<PropertyReviewUIModel>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Community Rating", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("4.8", fontSize = 48.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row {
                        repeat(5) { Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp)) }
                    }
                    Text("Based on ${reviews.size} reviews", fontSize = 11.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
private fun ReviewCard(review: PropertyReviewUIModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(36.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                    Text(review.tenantName.take(1), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(review.tenantName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(review.date, fontSize = 10.sp, color = Color.Gray)
                }
                Row {
                    repeat(review.rating) { Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(14.dp)) }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(review.comment, fontSize = 13.sp, color = Color.DarkGray, lineHeight = 18.sp)
        }
    }
}

@Composable
private fun AddReviewDialog(onDismiss: () -> Unit) {
    var rating by remember { mutableIntStateOf(5) }
    var comment by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Write a Review") },
        text = {
            Column {
                Text("How has your stay been so far?")
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    repeat(5) { index ->
                        IconButton(onClick = { rating = index + 1 }) {
                            Icon(
                                if (index < rating) Icons.Default.Star else Icons.Default.StarBorder,
                                null,
                                tint = if (index < rating) Color(0xFFFFB300) else Color.Gray
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Your thoughts...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, enabled = comment.isNotBlank()) { Text("Submit Story") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PropertyReviewsScreenPreview() {
    PropertyOSTheme {
        PropertyReviewsScreen({})
    }
}
