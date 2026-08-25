package com.him.landlordtenant.app.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RatingBar(
    rating: Double,
    maxStars: Int = 5,
    color: Color = Color(0xFFFFB300),
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        val fullStars = rating.toInt()
        val hasHalfStar = (rating - fullStars) >= 0.5
        
        repeat(maxStars) { index ->
            val icon = when {
                index < fullStars -> Icons.Default.Star
                index == fullStars && hasHalfStar -> Icons.AutoMirrored.Filled.StarHalf
                else -> Icons.Default.StarOutline
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
