package com.him.landlordtenant.app.ui.screens.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PremiumGradient
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

private data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val secondaryIcons: List<androidx.compose.ui.graphics.vector.ImageVector>,
)

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit
) {
    var currentPage by remember { mutableIntStateOf(0) }
    val pages = remember {
        listOf(
            OnboardingPage(
                title = "Find Your Next Home",
                description = "Discover luxury apartments and rental spaces with ease. Search by location and property type.",
                icon = Icons.Default.Apartment,
                secondaryIcons = listOf(Icons.Default.LocationOn, Icons.Default.HomeWork)
            ),
            OnboardingPage(
                title = "Smart Management",
                description = "Manage floors, units, tenants, and rent collections from a unified dashboard designed for efficiency.",
                icon = Icons.Default.ManageAccounts,
                secondaryIcons = listOf(Icons.Default.Apartment, Icons.Default.HomeWork)
            ),
            OnboardingPage(
                title = "Digital Ecosystem",
                description = "Handle lease agreements and maintenance requests digitally. Stay organized with premium automation.",
                icon = Icons.Default.Description,
                secondaryIcons = listOf(Icons.Default.Security, Icons.Default.VerifiedUser)
            ),
            OnboardingPage(
                title = "Seamless Payments",
                description = "Track deposits, rent, and utility bills with real-time reminders and instant STK push integrations.",
                icon = Icons.Default.AccountBalanceWallet,
                secondaryIcons = listOf(Icons.Default.Payment, Icons.Default.CheckCircle)
            )
        )
    }

    var dragAmount by remember { mutableFloatStateOf(0f) }
    val infiniteTransition = rememberInfiniteTransition(label = "Onboarding Animation")
    val floatingScale by infiniteTransition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Floating Scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(currentPage) {
                detectHorizontalDragGestures(
                    onDragStart = { dragAmount = 0f },
                    onHorizontalDrag = { _, amount -> dragAmount += amount },
                    onDragEnd = {
                        if (dragAmount < -100f) {
                            if (currentPage < pages.lastIndex) currentPage++ else onFinished()
                        } else if (dragAmount > 100f) {
                            if (currentPage > 0) currentPage--
                        }
                        dragAmount = 0f
                    }
                )
            }
    ) {
        // Decorative BG
        Box(modifier = Modifier.offset(x = (-100).dp, y = (-100).dp).size(400.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.03f), CircleShape))

        Column(
            modifier = Modifier.fillMaxSize().navigationBarsPadding().padding(horizontal = 28.dp, vertical = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (currentPage > 0) {
                    IconButton(onClick = { currentPage-- }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = MaterialTheme.colorScheme.primary)
                    }
                } else {
                    Spacer(modifier = Modifier.size(48.dp))
                }

                Text(text = "propertyOS", fontWeight = FontWeight.Black, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)

                TextButton(onClick = onFinished) {
                    Text("Skip", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            AnimatedContent(
                targetState = currentPage,
                modifier = Modifier.weight(1f),
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
                    } else {
                        slideInHorizontally { -it } + fadeIn() togetherWith slideOutHorizontally { it } + fadeOut()
                    }.using(SizeTransform(clip = false))
                },
                label = "PageTransition"
            ) { index ->
                val page = pages[index]
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier.size(280.dp).scale(floatingScale),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier.size(240.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f), CircleShape))
                        Box(modifier = Modifier.size(190.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f), CircleShape))
                        
                        Surface(
                            modifier = Modifier.size(130.dp),
                            shape = RoundedCornerShape(42.dp),
                            color = MaterialTheme.colorScheme.primary,
                            shadowElevation = 12.dp
                        ) {
                            Box(modifier = Modifier.background(Brush.linearGradient(PremiumGradient)), contentAlignment = Alignment.Center) {
                                Icon(page.icon, null, modifier = Modifier.size(60.dp), tint = Color.White)
                            }
                        }

                        if (page.secondaryIcons.isNotEmpty()) {
                            SmallFloatingIcon(page.secondaryIcons[0], Modifier.align(Alignment.TopEnd).offset(y = 20.dp))
                        }
                        if (page.secondaryIcons.size > 1) {
                            SmallFloatingIcon(page.secondaryIcons[1], Modifier.align(Alignment.BottomStart).offset(y = (-20).dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    Text(
                        text = page.title,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        lineHeight = 38.sp,
                        letterSpacing = (-1).sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = page.description,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }

            // Indicators
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                pages.indices.forEach { index ->
                    val width by animateDpAsState(if (index == currentPage) 28.dp else 8.dp, label = "indicator")
                    val color by animateColorAsState(if (index == currentPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), label = "color")
                    Box(modifier = Modifier.padding(horizontal = 4.dp).width(width).height(8.dp).background(color, CircleShape))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { if (currentPage < pages.lastIndex) currentPage++ else onFinished() },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(18.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(if (currentPage < pages.lastIndex) "Continue" else "Get Started", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun SmallFloatingIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier) {
    Surface(
        modifier = modifier.size(56.dp),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 6.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(icon, null, modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    PropertyOSTheme {
        OnboardingScreen {}
    }
}
