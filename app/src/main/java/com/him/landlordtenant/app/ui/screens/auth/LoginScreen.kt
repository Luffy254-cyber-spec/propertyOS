package com.him.landlordtenant.app.ui.screens.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PremiumGradient
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onLoginSuccess: (String, String) -> Unit,
    onGoogleSignIn: () -> Unit,
    onForgotPassword: () -> Unit,
    onRegister: () -> Unit,
    onGuest: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    var identifier by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var rememberMe by rememberSaveable { mutableStateOf(true) }

    var identifierError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }
    var successMessage by rememberSaveable { mutableStateOf<String?>(null) }
    
    // Animation
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    fun validateAndLogin() {
        identifierError = null
        passwordError = null
        successMessage = null

        var valid = true
        if (identifier.isBlank()) {
            identifierError = "Enter your email or phone number"
            valid = false
        } else if (identifier.contains("@") && !identifier.contains(".")) {
            identifierError = "Enter a valid email address"
            valid = false
        } else if (!identifier.contains("@") && identifier.length < 9) {
            identifierError = "Enter a valid phone number"
            valid = false
        }

        if (password.isBlank()) {
            passwordError = "Enter your password"
            valid = false
        } else if (password.length < 6) {
            passwordError = "Password must contain at least 6 characters"
            valid = false
        }

        if (valid) {
            onLoginSuccess(identifier, password)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f * alpha), MaterialTheme.colorScheme.background)))
    ) {
        // Decorative circles
        Box(modifier = Modifier.offset(x = (-50).dp, y = (-50).dp).size(200.dp).graphicsLayer(alpha = alpha * 0.1f).background(MaterialTheme.colorScheme.primary, CircleShape))
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 20.dp)
        ) {
            /* TOP BAR */
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, enabled = !isLoading) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }
                Spacer(modifier = Modifier.weight(1f))
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.HomeWork, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("propertyOS", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            /* TITLE */
            Text(
                text = "Welcome Back",
                fontSize = 34.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-1).sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Login to manage your premium spaces.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(40.dp))

            /* MESSAGES */
            AnimatedVisibility(visible = !errorMessage.isNullOrBlank()) {
                LoginMessageCard(message = errorMessage ?: "", isError = true)
            }

            /* INPUTS */
            OutlinedTextField(
                value = identifier,
                onValueChange = { identifier = it; identifierError = null },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                label = { Text("Email or Phone") },
                shape = RoundedCornerShape(16.dp),
                leadingIcon = { Icon(if (identifier.contains("@")) Icons.Default.Email else Icons.Default.Phone, null) },
                isError = identifierError != null,
                supportingText = identifierError?.let { { Text(it) } },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it; passwordError = null },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                label = { Text("Password") },
                shape = RoundedCornerShape(16.dp),
                leadingIcon = { Icon(Icons.Default.Lock, null) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null)
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = passwordError != null,
                supportingText = passwordError?.let { { Text(it) } },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it }, enabled = !isLoading)
                    Text("Remember me", fontSize = 13.sp)
                }
                TextButton(onClick = onForgotPassword, enabled = !isLoading) {
                    Text("Forgot Password?", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { validateAndLogin() },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(18.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Login Account", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = onGoogleSignIn,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(Icons.Default.AccountCircle, null)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Continue with Google", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("New here?", color = Color.Gray)
                Text(" Create Account", modifier = Modifier.clickable(onClick = onRegister), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun LoginMessageCard(message: String, isError: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        shape = RoundedCornerShape(14.dp),
        color = if (isError) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (isError) Icons.Default.Error else Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = if (isError) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = message, modifier = Modifier.weight(1f), fontSize = 13.sp, lineHeight = 19.sp, color = if (isError) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    PropertyOSTheme {
        LoginScreen({}, { _, _ -> }, {}, {}, {}, {})
    }
}
