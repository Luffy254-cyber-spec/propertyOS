package com.him.landlordtenant.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

object AnimationUtils {
    
    @Composable
    fun fadeInAnimation(duration: Int = 300): Float {
        val infiniteTransition = rememberInfiniteTransition(label = "fadeIn")
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(duration),
                repeatMode = RepeatMode.Reverse
            ),
            label = "alpha"
        )
        return alpha
    }

    val standardEasing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
}
