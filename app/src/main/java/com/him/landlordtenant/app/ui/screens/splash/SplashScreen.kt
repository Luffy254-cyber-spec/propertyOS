package com.him.landlordtenant.app.ui.screens.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import kotlinx.coroutines.delay

/**
 * PropertyOS Splash Screen
 *
 * First screen displayed when the application starts.
 *
 * Flow:
 *
 * Splash
 *   ↓
 * Onboarding
 *
 * The actual navigation is handled by the caller through
 * the onFinished callback.
 */
@Composable
fun SplashScreen(
    onFinished: () -> Unit,
) {

    var showLogo by remember {
        mutableStateOf(false)
    }

    var showText by remember {
        mutableStateOf(false)
    }

    /*
     * Logo scale animation.
     */
    val logoScale by animateFloatAsState(
        targetValue = if (showLogo) 1f else 0.65f,
        animationSpec = tween(
            durationMillis = 700,
            easing = FastOutSlowInEasing
        ),
        label = "Splash Logo Scale"
    )

    /*
     * Start splash animations and automatically
     * continue to onboarding.
     */
    LaunchedEffect(Unit) {

        // Show logo
        showLogo = true

        // Small delay before displaying text
        delay(350)

        showText = true

        // Keep splash visible for a moment
        delay(1800)

        // Navigate to next screen
        onFinished()
    }

    /*
     * Splash background.
     *
     * You can later move these colors into Theme.kt.
     */
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F172A),
            Color(0xFF172554),
            Color(0xFF1E3A8A)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            /*
             * PropertyOS logo.
             */
            AnimatedVisibility(
                visible = showLogo,
                enter = fadeIn(
                    animationSpec = tween(600)
                ) + scaleIn(
                    initialScale = 0.65f,
                    animationSpec = tween(
                        durationMillis = 700,
                        easing = FastOutSlowInEasing
                    )
                )
            ) {

                Box(
                    modifier = Modifier
                        .size(105.dp)
                        .scale(logoScale)
                        .background(
                            color = Color.White.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(28.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.HomeWork,
                        contentDescription = "PropertyOS",
                        modifier = Modifier.size(58.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            /*
             * Application name and tagline.
             */
            AnimatedVisibility(
                visible = showText,
                enter = fadeIn(
                    animationSpec = tween(700)
                )
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "propertyOS",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Smart Property Management",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        /*
         * Bottom branding.
         */
        Text(
            text = "Manage • Connect • Live",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .alpha(0.65f),
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    PropertyOSTheme {
        SplashScreen {}
    }
}
