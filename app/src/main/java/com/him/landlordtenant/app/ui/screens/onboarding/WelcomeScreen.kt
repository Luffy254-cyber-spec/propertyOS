package com.him.landlordtenant.app.ui.screens.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onGuest: () -> Unit
) {
    val visible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible.value = true }

    // Floating animation for icon
    val infiniteTransition = rememberInfiniteTransition(label = "icon_float")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -20f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Vibrant Decorative background elements
        Box(
            modifier = Modifier
                .offset(x = 150.dp, y = (-100).dp)
                .size(400.dp)
                .background(Brush.radialGradient(listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), Color.Transparent)), CircleShape)
        )
        
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-100).dp, y = 150.dp)
                .size(350.dp)
                .background(Brush.radialGradient(listOf(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f), Color.Transparent)), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = visible.value,
                enter = fadeIn(tween(1000)) + scaleIn(tween(1000, easing = OvershootInterpolator().toEasing()))
            ) {
                Box(
                    modifier = Modifier
                        .offset(y = offsetY.dp)
                        .size(140.dp)
                        .clip(RoundedCornerShape(36.dp))
                        .background(Brush.linearGradient(PremiumGradient))
                        .shadow(20.dp, RoundedCornerShape(36.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.HomeWork,
                        contentDescription = null,
                        modifier = Modifier.size(70.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            AnimatedVisibility(
                visible = visible.value,
                enter = fadeIn(tween(1000, 300)) + slideInVertically(tween(1000, 300)) { 30 }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Experience Premium Living",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        lineHeight = 44.sp,
                        letterSpacing = (-1.5).sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "The ultimate operating system for modern property management and luxury living.",
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp),
                        lineHeight = 26.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            AnimatedVisibility(
                visible = visible.value,
                enter = fadeIn(tween(1000, 600)) + slideInVertically(tween(1000, 600)) { 50 }
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = onLogin,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(68.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                    ) {
                        Text(text = "Sign In to Account", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.size(20.dp))
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedButton(
                        onClick = onRegister,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(68.dp),
                        shape = RoundedCornerShape(24.dp),
                        border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                    ) {
                        Text(text = "Join propertyOS", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            TextButton(
                onClick = onGuest,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Explore as Guest",
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}


// Helper for OvershootInterpolator
private fun android.view.animation.Interpolator.toEasing() = androidx.compose.animation.core.Easing { x ->
    getInterpolation(x)
}

private class OvershootInterpolator : android.view.animation.Interpolator {
    override fun getInterpolation(input: Float): Float {
        val t = input - 1.0f
        return t * t * ((2.0f + 1.0f) * t + 2.0f) + 1.0f
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    PropertyOSTheme {
        WelcomeScreen(onLogin = {}, onRegister = {}, onGuest = {})
    }
}
