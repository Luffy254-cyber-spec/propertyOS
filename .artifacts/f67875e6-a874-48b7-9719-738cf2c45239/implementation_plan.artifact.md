# Implementation Plan - Complete PropertyOS App

The goal is to finish the PropertyOS application by implementing missing data models, connecting screens via navigation, setting up the data layer (ViewModels, Repositories, DAOs), and completing the UI for all shell screens.

## User Review Required

> [!IMPORTANT]
> This plan involves significant changes across the entire project. I will be adding many new files and modifying existing ones to establish a functional architecture.

## Proposed Changes

### 1. Data Models & Enums
Populate the `models` and `enums` directories with proper definitions to replace empty placeholders.

#### [MODIFY] [Apartment.kt](file:///C:/Users/user/AndroidStudioProjects/propertyOS/app/src/main/java/com/him/landlordtenant/app/models/property/Apartment.kt)
#### [MODIFY] [User.kt](file:///C:/Users/user/AndroidStudioProjects/propertyOS/app/src/main/java/com/him/landlordtenant/app/models/User.kt)
#### [NEW] [Enums](file:///C:/Users/user/AndroidStudioProjects/propertyOS/app/src/main/java/com/him/landlordtenant/app/enums/) - (Status, Priority, Role, etc.)

### 2. Navigation
Implement a robust navigation system using `NavHost` and `sealed class Route`.

#### [MODIFY] [Routes.kt](file:///C:/Users/user/AndroidStudioProjects/propertyOS/app/src/main/java/com/him/landlordtenant/app/navigation/Routes.kt)
#### [MODIFY] [AppNavHost.kt](file:///C:/Users/user/AndroidStudioProjects/propertyOS/app/src/main/java/com/him/landlordtenant/app/navigation/AppNavHost.kt)
#### [MODIFY] [MainActivity.kt](file:///C:/Users/user/AndroidStudioProjects/propertyOS/app/src/main/java/com/him/landlordtenant/app/MainActivity.kt)

### 3. Data Layer (DI, Repository, ViewModel)
Set up Hilt and implement basic data flow.

#### [NEW] [AuthRepository.kt](file:///C:/Users/user/AndroidStudioProjects/propertyOS/app/src/main/java/com/him/landlordtenant/app/data/remote/AuthRepository.kt)
#### [MODIFY] [AuthViewModel.kt](file:///C:/Users/user/AndroidStudioProjects/propertyOS/app/src/main/java/com/him/landlordtenant/app/data/AuthViewModel.kt)
#### [NEW] [AppModule.kt](file:///C:/Users/user/AndroidStudioProjects/propertyOS/app/src/main/java/com/him/landlordtenant/app/di/AppModule.kt)

### 4. UI Implementation
Complete the shell screens and fix errors in existing ones (like `WelcomeScreen.kt`).

#### [MODIFY] [WelcomeScreen.kt](file:///C:/Users/user/AndroidStudioProjects/propertyOS/app/src/main/java/com/him/landlordtenant/app/ui/screens/onboarding/WelcomeScreen.kt)
#### [MODIFY] [LandlordDashboardScreen.kt](file:///C:/Users/user/AndroidStudioProjects/propertyOS/app/src/main/java/com/him/landlordtenant/app/ui/screens/landlord/LandlordDashboardScreen.kt)
#### [MODIFY] [TenantHomeScreen.kt](file:///C:/Users/user/AndroidStudioProjects/propertyOS/app/src/main/java/com/him/landlordtenant/app/ui/screens/tenant/TenantHomeScreen.kt)

## Verification Plan

### Automated Tests
- Build the project using `./gradlew assembleDebug`.
- Run unit tests for ViewModels.

### Manual Verification
- Deploy the app to a device/emulator.
- Navigate through the onboarding flow.
- Verify Login and Registration UI logic.
- Verify Dashboard navigation.
