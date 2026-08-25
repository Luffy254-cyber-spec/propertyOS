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
            SplashScreen(
                onFinished = {
                    navController.navigate(Route.Onboarding.route) {
                        popUpTo(Route.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Route.Onboarding.route) {
            OnboardingScreen(
                onFinished = {
                    navController.navigate(Route.ChooseRole.route) {
                        popUpTo(Route.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Route.ChooseRole.route) {
            RoleSelectionScreen(
                onTenantSelected = { navController.navigate(Route.Welcome.createRoute(UserRole.TENANT.name)) },
                onLandlordSelected = { navController.navigate(Route.Welcome.createRoute(UserRole.LANDLORD.name)) },
                onGuestSelected = { navController.navigate("guest_graph") },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            Route.Welcome.route,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: UserRole.TENANT.name
            WelcomeScreen(
                onLogin = { navController.navigate(Route.Login.createRoute(role)) },
                onRegister = { navController.navigate(Route.Register.createRoute(role)) },
                onGuest = { onAuthSuccess(UserRole.valueOf(role)) }
            )
        }

        composable(Route.AuthSuccess.route) {
            val currentUser by authViewModel.currentUser.collectAsState()
            AuthSuccessScreen(
                onFinished = {
                    onAuthSuccess(currentUser?.activeRole ?: UserRole.TENANT)
                }
            )
        }

        composable(
            Route.Login.route,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: UserRole.TENANT.name
            val authState by authViewModel.authState.collectAsState()
            
            LaunchedEffect(authState) {
                if (authState is AuthState.Success) {
                    navController.navigate(Route.AuthSuccess.route) {
                        popUpTo(Route.Login.route) { inclusive = true }
                    }
                }
            }

            LoginScreen(
                onBack = { navController.popBackStack() },
                onLoginSuccess = { email, pass -> authViewModel.login(email, pass) },
                onGoogleSignIn = { navController.navigate(Route.GoogleAuth.createRoute(role)) },
                onForgotPassword = { /* TODO */ },
                onRegister = { navController.navigate(Route.Register.createRoute(role)) },
                onGuest = { onAuthSuccess(UserRole.valueOf(role)) },
                isLoading = authState is AuthState.Loading,
                errorMessage = (authState as? AuthState.Error)?.message
            )
        }

        composable(
            Route.Register.route,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val roleStr = backStackEntry.arguments?.getString("role") ?: UserRole.TENANT.name
            val role = UserRole.valueOf(roleStr)
            val authState by authViewModel.authState.collectAsState()
            
            LaunchedEffect(authState) {
                if (authState is AuthState.Success) {
                    navController.navigate(Route.AuthSuccess.route) {
                        popUpTo(Route.Register.route) { inclusive = true }
                    }
                }
            }

            RegisterScreen(
                onBack = { navController.popBackStack() },
                onRegistrationSuccess = { name, email, pass -> 
                    authViewModel.register(name, email, pass, role) 
                },
                onGoogleSignUp = { navController.navigate(Route.GoogleAuth.createRoute(roleStr)) },
                onLogin = { navController.navigate(Route.Login.createRoute(roleStr)) },
                onGuest = { onAuthSuccess(role) },
                onTerms = { navController.navigate(Route.TermsOfService.route) },
                onPrivacyPolicy = { navController.navigate(Route.PrivacyPolicy.route) },
                isLoading = authState is AuthState.Loading,
                errorMessage = (authState as? AuthState.Error)?.message
            )
        }

        composable(
            Route.GoogleAuth.route,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: UserRole.TENANT.name
            val authState by authViewModel.authState.collectAsState()

            LaunchedEffect(authState) {
                if (authState is AuthState.Success) {
                    navController.navigate(Route.AuthSuccess.route) {
                        popUpTo(Route.GoogleAuth.route) { inclusive = true }
                    }
                }
            }

            GoogleAuthScreen(
                onTokenReceived = { token -> authViewModel.signInWithGoogle(token, UserRole.valueOf(role)) },
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
                onResendEmail = { authViewModel.sendEmailVerification() }
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
