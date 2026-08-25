package com.him.landlordtenant.app.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import kotlinx.coroutines.delay

/**
 * PropertyOS Welcome Screen.
 *
 * This is the first authentication entry point.
 *
 * Flow:
 *
 * Onboarding
 *      ↓
 * Welcome
 *      │
 *      ├── Login
 *      │
 *      ├── Create Account
 *      │
 *      └── Continue as Guest
 */
@Composable
fun WelcomeScreen(
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onGuest: () -> Unit
) {

    var contentVisible by remember {
        mutableStateOf(false)
    }

    /*
     * Start entrance animation.
     */
    LaunchedEffect(Unit) {

        delay(150)

        contentVisible = true
    }

    /*
     * Subtle floating animation for the main
     * property illustration.
     */
    val infiniteTransition = rememberInfiniteTransition(
        label = "Welcome Animation"
    )

    val floatingScale by infiniteTransition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1800,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Welcome Floating Scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.10f
                        ),
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {

        /*
         * Decorative background circles.
         */
        Box(
            modifier = Modifier
                .size(220.dp)
                .alpha(0.08f)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
                .align(Alignment.TopEnd)
        )

        Box(
            modifier = Modifier
                .size(170.dp)
                .alpha(0.06f)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
                .align(Alignment.BottomStart)
        )

        AnimatedVisibility(
            visible = contentVisible,
            enter = fadeIn(
                animationSpec = tween(700)
            ) + scaleIn(
                initialScale = 0.92f,
                animationSpec = tween(700)
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .padding(
                        horizontal = 26.dp,
                        vertical = 24.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                /*
                 * TOP BRANDING
                 */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.HomeWork,
                        contentDescription = "PropertyOS",
                        modifier = Modifier.size(28.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(
                        modifier = Modifier.size(8.dp)
                    )

                    Text(
                        text = "propertyOS",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(
                    modifier = Modifier.height(35.dp)
                )

                /*
                 * MAIN PROPERTY ILLUSTRATION
                 */
                Box(
                    modifier = Modifier
                        .size(235.dp)
                        .scale(floatingScale),
                    contentAlignment = Alignment.Center
                ) {

                    /*
                     * Outer circle.
                     */
                    Box(
                        modifier = Modifier
                            .size(225.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(
                                    alpha = 0.06f
                                ),
                                shape = CircleShape
                            )
                    )

                    /*
                     * Middle circle.
                     */
                    Box(
                        modifier = Modifier
                            .size(175.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(
                                    alpha = 0.09f
                                ),
                                shape = CircleShape
                            )
                    )

                    /*
                     * Main building.
                     */
                    Box(
                        modifier = Modifier
                            .size(125.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(35.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Apartment,
                            contentDescription = "Apartments",
                            modifier = Modifier.size(65.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    /*
                     * Floating key icon.
                     */
                    WelcomeFloatingIcon(
                        icon = Icons.Default.Key,
                        modifier = Modifier.align(
                            Alignment.TopEnd
                        )
                    )

                    /*
                     * Floating security icon.
                     */
                    WelcomeFloatingIcon(
                        icon = Icons.Default.VerifiedUser,
                        modifier = Modifier.align(
                            Alignment.BottomStart
                        )
                    )
                }

                Spacer(
                    modifier = Modifier.height(30.dp)
                )

                /*
                 * TITLE
                 */
                Text(
                    text = "Your property, simplified.",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 29.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                /*
                 * DESCRIPTION
                 */
                Text(
                    text =
                        "Find a home, manage your property, " +
                                "pay rent and bills, and stay connected " +
                                "with your landlord or tenants.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    lineHeight = 23.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(
                        alpha = 0.65f
                    )
                )

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                /*
                 * LOGIN BUTTON
                 */
                Button(
                    onClick = onLogin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            MaterialTheme.colorScheme.primary
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Login,
                        contentDescription = "Login"
                    )

                    Spacer(
                        modifier = Modifier.size(10.dp)
                    )

                    Text(
                        text = "Login",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                /*
                 * CREATE ACCOUNT BUTTON
                 */
                OutlinedButton(
                    onClick = onRegister,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = "Create account"
                    )

                    Spacer(
                        modifier = Modifier.size(10.dp)
                    )

                    Text(
                        text = "Create an Account",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                /*
                 * GUEST BUTTON
                 */
                Row(
                    modifier = Modifier
                        .clickable(
                            onClick = onGuest
                        )
                        .padding(
                            horizontal = 12.dp,
                            vertical = 10.dp
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Key,
                        contentDescription = "Guest",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(
                        modifier = Modifier.size(7.dp)
                    )

                    Text(
                        text = "Continue as Guest",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(
                        modifier = Modifier.size(4.dp)
                    )

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                /*
                 * SECURITY MESSAGE
                 */
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Secure",
                        modifier = Modifier.size(15.dp),
                        tint = MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.45f
                        )
                    )

                    Spacer(
                        modifier = Modifier.size(5.dp)
                    )

                    Text(
                        text = "Secure property management",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.45f
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    PropertyOSTheme {
        WelcomeScreen({}, {}, {})
    }
}

/**
 * Floating icon used by the welcome illustration.
 */
@Composable
private fun WelcomeFloatingIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier
) {

    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(
            durationMillis = 700
        ),
        label = "Floating Icon Scale"
    )

    Box(
        modifier = modifier
            .size(55.dp)
            .scale(scale)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(18.dp)
            ),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(26.dp),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}