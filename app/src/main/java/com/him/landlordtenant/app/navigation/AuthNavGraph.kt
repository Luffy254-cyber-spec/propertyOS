package com.him.landlordtenant.app.navigation

import android.app.Activity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.him.landlordtenant.app.data.model.UserRole
import com.him.landlordtenant.app.ui.screens.auth.*
import com.him.landlordtenant.app.ui.screens.onboarding.OnboardingScreen
import com.him.landlordtenant.app.ui.screens.onboarding.WelcomeScreen
import com.him.landlordtenant.app.ui.screens.splash.SplashScreen
import com.him.landlordtenant.app.ui.viewmodel.auth.AuthState
import com.him.landlordtenant.app.ui.viewmodel.auth.AuthViewModel

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    onAuthSuccess: (UserRole) -> Unit
) {
    navigation(
        startDestination = Route.Splash.route,
        route = "auth_graph"
    ) {
        composable(Route.Splash.route) {
            val currentUser by authViewModel.currentUser.collectAsState()
            val authState by authViewModel.authState.collectAsState()

            SplashScreen(
                onFinished = {
                    if (currentUser != null) {
                        // If they have an active role, go to the right graph directly
                        val role = currentUser?.activeRole
                        if (role != null) {
                            val targetGraph = if (role == UserRole.LANDLORD) "landlord_graph" else "tenant_graph"
                            navController.navigate(targetGraph) {
                                popUpTo(Route.Splash.route) { inclusive = true }
                            }
                        } else {
                            navController.navigate(Route.ChooseRole.route) {
                                popUpTo(Route.Splash.route) { inclusive = true }
                            }
                        }
                    } else {
                        navController.navigate(Route.Onboarding.route) {
                            popUpTo(Route.Splash.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Route.Onboarding.route) {
            OnboardingScreen(
                onFinished = {
                    navController.navigate(Route.Welcome.route) {
                        popUpTo(Route.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Route.ChooseRole.route) {
            val currentUser by authViewModel.currentUser.collectAsState()
            val authState by authViewModel.authState.collectAsState()

            LaunchedEffect(Unit) {
                authViewModel.prepareForRoleSwitch()
            }

            RoleSelectionScreen(
                userName = currentUser?.displayName ?: "",
                userEmail = currentUser?.email ?: "",
                isLoading = authState is AuthState.Loading,
                onTenantSelected = { 
                    authViewModel.updateUserRole(UserRole.TENANT)
                },
                onLandlordSelected = { 
                    authViewModel.updateUserRole(UserRole.LANDLORD)
                },
                onGuestSelected = { navController.navigate("guest_graph") },
                onBack = { navController.popBackStack() }
            )
            
            LaunchedEffect(currentUser, authState) {
                if (authState is AuthState.Success) {
                    currentUser?.activeRole?.let { role ->
                        onAuthSuccess(role)
                    }
                }
            }
        }

        composable(Route.Welcome.route) {
            WelcomeScreen(
                onLogin = { navController.navigate(Route.Login.route) },
                onRegister = { navController.navigate(Route.Register.route) },
                onGuest = { 
                    // For guests, we can just navigate to guest graph or choose a default role
                    navController.navigate("guest_graph")
                }
            )
        }

        composable(Route.AuthSuccess.route) {
            AuthSuccessScreen(
                onFinished = {
                    // Always navigate to Choose Role screen after Auth Success
                    navController.navigate(Route.ChooseRole.route) {
                        popUpTo(Route.AuthSuccess.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Route.Login.route) {
            val authState by authViewModel.authState.collectAsState()
            
            LaunchedEffect(authState) {
                val state = authState
                when (state) {
                    is AuthState.Success -> {
                        navController.navigate(Route.AuthSuccess.route) {
                            popUpTo(Route.Login.route) { inclusive = true }
                        }
                    }
                    is AuthState.RequiresEmailVerification -> {
                        navController.navigate(Route.EmailVerification.createRoute(state.email))
                    }
                    is AuthState.RequiresPhoneVerification -> {
                        navController.navigate(Route.OtpVerification.createRoute(state.phoneNumber))
                    }
                    else -> {}
                }
            }

            LoginScreen(
                onBack = { navController.popBackStack() },
                onLoginSuccess = { email, pass -> authViewModel.login(email, pass) },
                onGoogleSignIn = { navController.navigate(Route.GoogleAuth.route) },
                onForgotPassword = { navController.navigate(Route.ForgotPassword.route) },
                onRegister = { navController.navigate(Route.Register.route) },
                onGuest = { navController.navigate("guest_graph") },
                isLoading = authState is AuthState.Loading,
                errorMessage = (authState as? AuthState.Error)?.message
            )
        }

        composable(Route.ForgotPassword.route) {
            val authState by authViewModel.authState.collectAsState()
            ForgotPasswordScreen(
                onBack = { navController.popBackStack() },
                onCodeSent = { email -> authViewModel.forgotPassword(email) },
                isLoading = authState is AuthState.Loading,
                externalErrorMessage = (authState as? AuthState.Error)?.message
            )
        }

        composable(Route.Register.route) {
            val authState by authViewModel.authState.collectAsState()
            
            LaunchedEffect(authState) {
                val state = authState
                when (state) {
                    is AuthState.Success -> {
                        navController.navigate(Route.AuthSuccess.route) {
                            popUpTo(Route.Register.route) { inclusive = true }
                        }
                    }
                    is AuthState.RequiresEmailVerification -> {
                        navController.navigate(Route.EmailVerification.createRoute(state.email))
                    }
                    is AuthState.RequiresPhoneVerification -> {
                        navController.navigate(Route.OtpVerification.createRoute(state.phoneNumber))
                    }
                    else -> {}
                }
            }

            RegisterScreen(
                onBack = { navController.popBackStack() },
                onRegistrationSuccess = { name, email, pass -> 
                    authViewModel.register(name, email, pass) 
                },
                onGoogleSignUp = { navController.navigate(Route.GoogleAuth.route) },
                onLogin = { navController.navigate(Route.Login.route) },
                onGuest = { navController.navigate("guest_graph") },
                onTerms = { navController.navigate(Route.TermsOfService.route) },
                onPrivacyPolicy = { navController.navigate(Route.PrivacyPolicy.route) },
                isLoading = authState is AuthState.Loading,
                errorMessage = (authState as? AuthState.Error)?.message
            )
        }

        composable(Route.GoogleAuth.route) {
            val authState by authViewModel.authState.collectAsState()

            LaunchedEffect(authState) {
                val state = authState
                when (state) {
                    is AuthState.Success -> {
                        navController.navigate(Route.AuthSuccess.route) {
                            popUpTo(Route.GoogleAuth.route) { inclusive = true }
                        }
                    }
                    is AuthState.RequiresEmailVerification -> {
                        navController.navigate(Route.EmailVerification.createRoute(state.email))
                    }
                    is AuthState.RequiresPhoneVerification -> {
                        navController.navigate(Route.OtpVerification.createRoute(state.phoneNumber))
                    }
                    else -> {}
                }
            }

            GoogleAuthScreen(
                onTokenReceived = { token -> authViewModel.signInWithGoogle(token) },
                onAuthFailure = { /* TODO: Show snackbar */ }
            )
        }

        composable(
            Route.OtpVerification.route,
            arguments = listOf(navArgument("identifier") { type = NavType.StringType })
        ) { backStackEntry ->
            val identifier = backStackEntry.arguments?.getString("identifier") ?: ""
            val authState by authViewModel.authState.collectAsState()
            
            LaunchedEffect(authState) {
                if (authState is AuthState.Success) {
                    navController.navigate(Route.AuthSuccess.route) {
                        popUpTo(Route.OtpVerification.route) { inclusive = true }
                    }
                }
            }

            OtpVerificationScreen(
                phoneNumber = identifier,
                onVerify = { otp -> authViewModel.verifyOtp(otp) },
                onResendOtp = { /* TODO */ }
            )
        }

        composable(
            Route.EmailVerification.route,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val authState by authViewModel.authState.collectAsState()

            LaunchedEffect(authState) {
                if (authState is AuthState.Success) {
                    navController.navigate(Route.AuthSuccess.route) {
                        popUpTo(Route.EmailVerification.route) { inclusive = true }
                    }
                }
            }

            EmailVerificationScreen(
                email = email,
                onContinue = { 
                    authViewModel.checkEmailVerification()
                },
                onResendEmail = { authViewModel.sendEmailVerification() },
                isLoading = authState is AuthState.Loading
            )
        }

        composable(Route.PhoneVerification.route) {
            val context = LocalContext.current
            PhoneVerificationScreen(
                onBack = { navController.popBackStack() },
                onCodeSent = { phoneNumber ->
                    val activity = context as? Activity
                    if (activity != null) {
                        authViewModel.startPhoneAuth(phoneNumber, activity)
                        navController.navigate(Route.OtpVerification.createRoute(phoneNumber))
                    }
                }
            )
        }

        composable(Route.IdentityVerification.route) {
            IdentityVerificationScreen(
                onBack = { navController.popBackStack() },
                onComplete = { 
                    navController.navigate(Route.AuthSuccess.route) {
                        popUpTo(Route.IdentityVerification.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
