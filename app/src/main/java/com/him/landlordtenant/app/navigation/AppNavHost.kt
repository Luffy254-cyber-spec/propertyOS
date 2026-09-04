package com.him.landlordtenant.app.navigation

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.him.landlordtenant.app.data.model.UserRole
import com.him.landlordtenant.app.ui.screens.common.*
import com.him.landlordtenant.app.ui.viewmodel.auth.AuthViewModel
import com.him.landlordtenant.app.util.AlertManager
import com.him.landlordtenant.app.util.BannerType
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = "auth_graph",
    authViewModel: AuthViewModel = hiltViewModel(),
    alertManager: AlertManager = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var alertToShow by remember { mutableStateOf<com.him.landlordtenant.app.util.AlertMessage?>(null) }
    val currentBanner by alertManager.banners.collectAsState(initial = null)

    LaunchedEffect(Unit) {
        alertManager.snackbars.collectLatest { msg ->
            val result = snackbarHostState.showSnackbar(
                message = msg.message,
                actionLabel = msg.actionLabel,
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                msg.onAction?.invoke()
            }
        }
    }

    LaunchedEffect(Unit) {
        alertManager.alerts.collectLatest { alert ->
            alertToShow = alert
        }
    }

    LaunchedEffect(Unit) {
        alertManager.toasts.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    if (alertToShow != null) {
        AlertDialog(
            onDismissRequest = { alertToShow = null },
            title = { Text(alertToShow!!.title, fontWeight = FontWeight.Bold) },
            text = { Text(alertToShow!!.message) },
            confirmButton = {
                Button(onClick = {
                    alertToShow!!.onConfirm?.invoke()
                    alertToShow = null
                }) {
                    Text(alertToShow!!.positiveButton)
                }
            },
            dismissButton = {
                TextButton(onClick = { alertToShow = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // In-App Alert Banner
            AnimatedVisibility(
                visible = currentBanner != null,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                currentBanner?.let { banner ->
                    val backgroundColor = when (banner.type) {
                        BannerType.INFO -> MaterialTheme.colorScheme.primaryContainer
                        BannerType.SUCCESS -> Color(0xFFE8F5E9)
                        BannerType.WARNING -> Color(0xFFFFF3E0)
                        BannerType.ERROR -> MaterialTheme.colorScheme.errorContainer
                    }
                    val contentColor = when (banner.type) {
                        BannerType.INFO -> MaterialTheme.colorScheme.onPrimaryContainer
                        BannerType.SUCCESS -> Color(0xFF2E7D32)
                        BannerType.WARNING -> Color(0xFFE65100)
                        BannerType.ERROR -> MaterialTheme.colorScheme.onErrorContainer
                    }
                    val icon = when (banner.type) {
                        BannerType.INFO -> Icons.Default.Info
                        BannerType.SUCCESS -> Icons.Default.CheckCircle
                        BannerType.WARNING -> Icons.Default.Warning
                        BannerType.ERROR -> Icons.Default.Error
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = backgroundColor,
                        contentColor = contentColor,
                        tonalElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(icon, null, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = banner.message, modifier = Modifier.weight(1f), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            IconButton(onClick = { 
                                scope.launch { alertManager.hideBanner() } 
                            }) {
                                Icon(Icons.Default.Close, null, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }

            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.weight(1f),
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(400, easing = FastOutSlowInEasing)
                    ) + fadeIn(animationSpec = tween(400))
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(400, easing = FastOutSlowInEasing)
                    ) + fadeOut(animationSpec = tween(400))
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(400, easing = FastOutSlowInEasing)
                    ) + fadeIn(animationSpec = tween(400))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(400, easing = FastOutSlowInEasing)
                    ) + fadeOut(animationSpec = tween(400))
                }
            ) {
                // Authentication & Onboarding
                authNavGraph(
                    navController = navController,
                    authViewModel = authViewModel,
                    onAuthSuccess = { role ->
                        val targetGraph = if (role == UserRole.LANDLORD) "landlord_graph" else "tenant_graph"
                        navController.navigate(targetGraph) {
                            popUpTo("auth_graph") { inclusive = true }
                        }
                    }
                )

                // Tenant Section
                tenantNavGraph(
                    navController = navController,
                    onLogout = {
                        navController.clearBackStackAndNavigate("auth_graph")
                    },
                    onSwitchRole = {
                        authViewModel.prepareForRoleSwitch()
                        navController.navigate(Route.ChooseRole.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )

                // Landlord Section
                landlordNavGraph(
                    navController = navController,
                    onLogout = {
                        navController.clearBackStackAndNavigate("auth_graph")
                    },
                    onSwitchRole = {
                        authViewModel.prepareForRoleSwitch()
                        navController.navigate(Route.ChooseRole.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )

                // Guest Section
                guestNavGraph(
                    navController = navController,
                    onLogin = {
                        navController.navigate("auth_graph") {
                            popUpTo(0)
                        }
                    }
                )

                // Common Routes
                composable(Route.Settings.route) {
                    SettingsScreen(
                        onBack = { navController.popBackStack() },
                        onNavigateToAbout = { navController.navigate(Route.About.route) },
                        onNavigateToPrivacy = { navController.navigate(Route.PrivacyPolicy.route) },
                        onNavigateToTerms = { navController.navigate(Route.TermsOfService.route) },
                        onNavigateToHelp = { navController.navigate(Route.HelpCenter.route) },
                        onLogout = {
                            navController.clearBackStackAndNavigate("auth_graph")
                        },
                        onSwitchRole = {
                            authViewModel.prepareForRoleSwitch()
                            navController.navigate(Route.ChooseRole.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Route.About.route) {
                    AboutScreen(onBack = { navController.popBackStack() })
                }

                composable(Route.PrivacyPolicy.route) {
                    PrivacyPolicyScreen(onBack = { navController.popBackStack() })
                }

                composable(Route.TermsOfService.route) {
                    TermsOfServiceScreen(onBack = { navController.popBackStack() })
                }

                composable(Route.ContactSupport.route) {
                    ContactSupportScreen(onBack = { navController.popBackStack() })
                }

                composable(Route.HelpCenter.route) {
                    HelpCenterScreen(onBack = { navController.popBackStack() })
                }
            }
        }
    }
}
