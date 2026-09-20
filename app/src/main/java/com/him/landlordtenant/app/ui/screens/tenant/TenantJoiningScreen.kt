package com.him.landlordtenant.app.ui.screens.tenant

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.him.landlordtenant.app.ui.theme.PremiumGradient
import com.him.landlordtenant.app.ui.theme.PropertyOSTheme

import androidx.hilt.navigation.compose.hiltViewModel
import com.him.landlordtenant.app.ui.viewmodel.tenant.TenantJoiningViewModel

import androidx.compose.animation.core.*
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantJoiningScreen(
    apartmentId: String,
    apartmentName: String,
    houseId: String? = null,
    houseNumber: String? = null,
    onBack: () -> Unit,
    onJoinComplete: () -> Unit,
    viewModel: TenantJoiningViewModel = hiltViewModel()
) {
    var currentStep by remember { mutableIntStateOf(0) }
    val totalSteps = 4
    
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val propertyAgreement by viewModel.propertyAgreement.collectAsState()
    
    LaunchedEffect(apartmentId) {
        viewModel.loadPropertyAgreement(apartmentId, houseNumber)
    }

    // Data
    var signature by remember { mutableStateOf("") }
    var idImageUri by remember { mutableStateOf<Uri?>(null) }
    var feedback by remember { mutableStateOf("") }
    var rating by remember { mutableIntStateOf(5) }
    
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        val houseLabel = if (!houseNumber.isNullOrEmpty() && houseNumber != "GENERAL") " Unit $houseNumber" else ""
                        Text("Joining $apartmentName$houseLabel", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("Step ${currentStep + 1} of $totalSteps", fontSize = 11.sp, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { if (currentStep > 0) currentStep-- else onBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            LinearProgressIndicator(
                progress = { (currentStep + 1).toFloat() / totalSteps },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            )

            Box(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.fillMaxSize()) {
                    error?.let {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                        ) {
                            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(12.dp), fontSize = 12.sp)
                        }
                    }

                    Box(modifier = Modifier.weight(1f)) {
                    when (currentStep) {
                        0 -> WelcomeStep(onNext = { currentStep = 1 })
                        1 -> AgreementStep(
                            agreement = propertyAgreement,
                            onNext = { currentStep = 2 }
                        )
                        2 -> IdentityStep(
                        signature = signature,
                        onSignatureChange = { signature = it },
                        idImageUri = idImageUri,
                        onImageSelected = { idImageUri = it },
                        onNext = { currentStep = 3 }
                    )
                    3 -> CompletionStep(
                        rating = rating,
                        onRatingChange = { rating = it },
                        feedback = feedback,
                        onFeedbackChange = { feedback = it },
                        onComplete = { 
                            viewModel.joinApartment(
                                apartmentId = apartmentId,
                                signature = signature,
                                idUri = idImageUri,
                                houseId = houseId,
                                houseNumber = houseNumber,
                                onSuccess = onJoinComplete
                            )
                        },
                        isLoading = isLoading
                    )
                }
                    }
                }
            }
        }
    }
}

@Composable
private fun WelcomeStep(onNext: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "welcome")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    var showItems by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(200)
        showItems = true
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            visible = showItems,
            enter = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn()
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .scale(scale)
                    .background(
                        Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)),
                        CircleShape
                    )
                    .shadow(12.dp, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Celebration, null, modifier = Modifier.size(80.dp), tint = Color.White)
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))
        
        AnimatedVisibility(
            visible = showItems,
            enter = slideInVertically(initialOffsetY = { 40 }) + fadeIn(animationSpec = tween(600, 200))
        ) {
            Text(
                "Welcome to propertyOS!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        AnimatedVisibility(
            visible = showItems,
            enter = slideInVertically(initialOffsetY = { 40 }) + fadeIn(animationSpec = tween(600, 400))
        ) {
            Text(
                "We're here to help you find your dream home. Our app is designed to make your move-in and stay as effortless as possible. You're just a few steps away from your new community.",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                lineHeight = 20.sp
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        AnimatedVisibility(
            visible = showItems,
            enter = slideInVertically(initialOffsetY = { 40 }) + fadeIn(animationSpec = tween(600, 600))
        ) {
            Text(
                "Please enjoy our app and rate it! Feel free to send us feedback at any time—we are constantly working to improve your experience. ✨",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        AnimatedVisibility(
            visible = showItems,
            enter = fadeIn(animationSpec = tween(800, 800))
        ) {
            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Text("Let's Get Started 🚀", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun AgreementStep(
    agreement: TenantAgreementUIModel?,
    onNext: () -> Unit
) {
    val scrollState = rememberScrollState()
    val reachedBottom = scrollState.value >= scrollState.maxValue - 20
    var accepted by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Tenancy Agreement", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        
        Box(
            modifier = Modifier
                .weight(1f)
                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(12.dp)
            ) {
                if (agreement != null) {
                    Text(
                        "RESIDENTIAL TENANCY AGREEMENT\n\n" +
                        "This agreement is made between ${agreement.landlordName} and the Tenant for the lease of ${agreement.apartmentName}.\n\n" +
                        agreement.agreementContent,
                        lineHeight = 22.sp,
                        fontSize = 14.sp
                    )
                    
                    if (!agreement.agreementProofUrl.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Agreement Proof / Certificate:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        AsyncImage(
                            model = agreement.agreementProofUrl,
                            contentDescription = "Agreement Proof",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Fit
                        )
                    }
                } else {
                    // Fallback to generic if loading or not found
                    Text(
                        "RESIDENTIAL TENANCY AGREEMENT\n\n" +
                        "This agreement is made between the Landlord and the Tenant for the lease of the premises. " +
                        "By proceeding, you agree to the following terms:\n\n" +
                        "1. RENT: Rent is due on the 1st of every month.\n" +
                        "2. DEPOSIT: A security deposit equal to one month's rent is required.\n" +
                        "3. DURATION: The initial lease term is 12 months.\n" +
                        "4. MAINTENANCE: Tenant is responsible for keeping the premises clean.\n" +
                        "5. TERMINATION: One month's written notice is required before vacating.\n\n" +
                        "6. COMMUNITY RULES: All residents must adhere to building noise regulations and common area protocols.",
                        lineHeight = 22.sp,
                        fontSize = 14.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                if (reachedBottom) {
                    Text("✓ Full agreement reviewed.", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                } else {
                    Text("↓ Please scroll to the bottom to continue.", color = Color.Gray, fontStyle = FontStyle.Italic)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = accepted, onCheckedChange = { accepted = it }, enabled = reachedBottom)
            Text("I confirm that I have read and understood the agreement.", fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = accepted,
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Confirm & Next", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun IdentityStep(
    signature: String,
    onSignatureChange: (String) -> Unit,
    idImageUri: Uri?,
    onImageSelected: (Uri) -> Unit,
    onNext: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { onImageSelected(it) }
    }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState())) {
        Text("Digital Verification", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        Text("Upload National ID Image", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { launcher.launch("image/*") }
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (idImageUri != null) {
                AsyncImage(model = idImageUri, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Badge, null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary)
                    Text("Tap to upload ID photo", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        LegalIdDisclaimer()

        Spacer(modifier = Modifier.height(32.dp))
        Text("Digital Signature", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text("Type your full legal name as a signature", fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = signature,
            onValueChange = onSignatureChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("John Doe") },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = idImageUri != null && signature.isNotBlank(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Continue", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun LegalIdDisclaimer() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Gavel, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "CRITICAL LEGAL NOTICE: Provision of a fake ID, an ID that isn't yours, or the identity of a deceased person is a serious criminal offense. Such fraudulent acts will be reported and used against you in a legal court of law.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold,
                lineHeight = 16.sp
            )
        }
    }
}


@Composable
private fun CompletionStep(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    feedback: String,
    onFeedbackChange: (String) -> Unit,
    onComplete: () -> Unit,
    isLoading: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(100.dp).background(Color(0xFFE8F5E9), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(60.dp), tint = Color(0xFF2E7D32))
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("Almost There!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
        Text("Your application is being finalized.", color = Color.Gray)
        
        Spacer(modifier = Modifier.height(32.dp))
        Text("Rate your experience", fontWeight = FontWeight.Bold)
        Row {
            repeat(5) { index ->
                val active = index < rating
                IconButton(onClick = { onRatingChange(index + 1) }) {
                    Icon(
                        if (active) Icons.Default.Star else Icons.Default.StarBorder,
                        null,
                        tint = if (active) Color(0xFFFFB300) else Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = feedback,
            onValueChange = onFeedbackChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Any feedback for us?") },
            placeholder = { Text("Tell us how we can improve...") },
            minLines = 3,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))
        Text(
            "After joining, please pick a vacant house from the available units and move in as soon as possible! We look forward to having you.",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onComplete,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text("Complete Joining", fontWeight = FontWeight.Bold)
            }
        }
    }
}
