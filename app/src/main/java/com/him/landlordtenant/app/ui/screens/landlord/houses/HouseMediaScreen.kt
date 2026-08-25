package com.him.landlordtenant.app.ui.screens.landlord.houses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseMediaScreen(
    onBack: () -> Unit,
    onAddMedia: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("House Photos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddMedia) {
                Icon(Icons.Default.Add, "Add Photos")
            }
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize().padding(padding).padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(6) { index ->
                MediaItemBox()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HouseMediaScreenPreview() {
    PropertyOSTheme {
        HouseMediaScreen({}, {})
    }
}

@Composable
private fun MediaItemBox() {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.Image, null, tint = Color.Gray)
    }
}
