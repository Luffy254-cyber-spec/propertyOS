# Rewrite LandlordNavGraph.kt

Correct errors and mismatches between screen declarations and their usages in the navigation graph.

## User Review Required

> [!IMPORTANT]
> The rewrite involves changing parameters passed to several screen Composables to match their latest declarations. This includes removing redundant ViewModel logic from the NavGraph and letting the screens handle their own state.

## Proposed Changes

### Navigation

#### [MODIFY] [LandlordNavGraph.kt](file:///C:/Users/user/AndroidStudioProjects/propertyOS/app/src/main/java/com/him/landlordtenant/app/navigation/LandlordNavGraph.kt)

- Rewrite `LandlordNavGraph.kt` from scratch.
- Fix `LandlordDashboardScreen` parameters (remove `unreadMessages`).
- Fix `HouseManagementScreen` parameters (remove `houseData`, add `apartmentId`, `floorId`).
- Fix `HouseStatusScreen` parameters (pass `apartmentId`, `floorId`, `houseId` instead of `currentStatus`/`onStatusChange`).
- Fix `MyApartmentsScreen`, `TenantsScreen`, `AgreementsScreen`, `MaintenanceRequestsScreen` to remove manually passed state (they use internal ViewModels).
- Fix `MeterReadingScreen` parameter types (convert String arguments to Double where necessary).
- Clean up duplicate and wildcard imports.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` to verify compilation.

### Manual Verification
- Deploy to device/emulator.
- Navigate through all Landlord screens to ensure no crashes occur due to missing arguments or incorrect types.
