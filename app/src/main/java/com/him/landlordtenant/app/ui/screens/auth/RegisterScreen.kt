package com.him.landlordtenant.app.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegistrationSuccess: (String, String, String) -> Unit,
    onGoogleSignUp: () -> Unit,
    onLogin: () -> Unit,
    onGuest: () -> Unit,
    onTerms: () -> Unit,
    onPrivacyPolicy: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    var fullName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var agreeToTerms by rememberSaveable { mutableStateOf(false) }

    var fullNameError by rememberSaveable { mutableStateOf<String?>(null) }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var phoneError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }
    var confirmPasswordError by rememberSaveable { mutableStateOf<String?>(null) }

    fun validateAndRegister() {
        fullNameError = null
        emailError = null
        phoneError = null
        passwordError = null
        confirmPasswordError = null

        var valid = true
        if (fullName.isBlank()) { fullNameError = "Enter your full name"; valid = false }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) { emailError = "Enter a valid email"; valid = false }
        if (phone.length < 10) { phoneError = "Enter a valid phone number"; valid = false }
        if (password.length < 8) { passwordError = "Password must be at least 8 characters"; valid = false }
        if (password != confirmPassword) { confirmPasswordError = "Passwords do not match"; valid = false }
        if (!agreeToTerms) { /* Show snackbar or message */ valid = false }

        if (valid) {
            onRegistrationSuccess(fullName, email, password)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.07f),
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                        .clickable(enabled = !isLoading, onClick = onBack),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "propertyOS", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(text = "Create Account", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(text = "Join propertyOS and start managing your rentals easily.", color = Color.Gray, fontSize = 15.sp)

            Spacer(modifier = Modifier.height(24.dp))

            if (!errorMessage.isNullOrBlank()) {
                Surface(color = MaterialTheme.colorScheme.errorContainer, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.onErrorContainer, modifier = Modifier.padding(16.dp), fontSize = 14.sp)
                }
            }

            OutlinedTextField(
                value = fullName, onValueChange = { fullName = it; fullNameError = null },
                label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth(),
                isError = fullNameError != null, supportingText = fullNameError?.let { { Text(it) } },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email, onValueChange = { email = it; emailError = null },
                label = { Text("Email Address") }, modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = emailError != null, supportingText = emailError?.let { { Text(it) } },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phone, onValueChange = { phone = it; phoneError = null },
                label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = phoneError != null, supportingText = phoneError?.let { { Text(it) } },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password, onValueChange = { password = it; passwordError = null },
                label = { Text("Password") }, modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null)
                    }
                },
                isError = passwordError != null, supportingText = passwordError?.let { { Text(it) } },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword, onValueChange = { confirmPassword = it; confirmPasswordError = null },
                label = { Text("Confirm Password") }, modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null)
                    }
                },
                isError = confirmPasswordError != null, supportingText = confirmPasswordError?.let { { Text(it) } },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = agreeToTerms, onCheckedChange = { agreeToTerms = it })
                Text(text = "I agree to the ", fontSize = 12.sp)
                Text(text = "Terms", color = MaterialTheme.colorScheme.primary, modifier = Modifier.clickable(onClick = onTerms), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(text = " and ", fontSize = 12.sp)
                Text(text = "Privacy Policy", color = MaterialTheme.colorScheme.primary, modifier = Modifier.clickable(onClick = onPrivacyPolicy), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { validateAndRegister() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !isLoading && agreeToTerms,
                shape = RoundedCornerShape(16.dp)
            ) {
                if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("Create Account", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text("Already have an account?", fontSize = 13.sp)
                TextButton(onClick = onLogin) { Text("Sign In", fontWeight = FontWeight.Bold) }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    PropertyOSTheme {
        RegisterScreen({}, { _, _, _ -> }, {}, {}, {}, {}, {})
    }
}
