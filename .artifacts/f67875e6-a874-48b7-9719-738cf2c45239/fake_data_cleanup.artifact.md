# Fake Data Cleanup Tracking

This document tracks the progress of removing hardcoded sample data and strings from the UI screens, replacing them with dynamic parameters and preparing the app for Firebase integration.

## Progress Checklist

- [x] `GuestScreen.kt` - Refactored to accept `apartments` and `counties` as parameters.
- [x] `ApartmentFloorsScreen.kt` - Refactored to accept `floors` as a parameter.
- [x] `TenantHouseJoinAgreementScreen.kt` - Refactored to accept `agreement` as a parameter.
- [x] `HouseDetailsScreen.kt` - Refactored to accept `house` as a parameter.
- [x] `LandlordDashboardScreen.kt` - Refactored to accept `dashboardData`.
- [x] `MyApartmentsScreen.kt` - Refactored to use `apartment.totalRevenue`.
- [x] `ApartmentDetailsScreen.kt` - Refactored to accept `apartment`.
- [x] `ApartmentSearchScreen.kt` - Refactored to accept `apartments`.
- [x] `BillsScreen.kt` - Refactored to move sample data to preview.
- [x] `MaintenanceScreen.kt` - Verified, param-driven.
- [x] `TenantsScreen.kt` - Verified, param-driven.

## Firebase Integration Tasks

1.  **Auth Implementation**: Update `AuthRepositoryImpl` to use `FirebaseAuth`.
2.  **Firestore Implementation**: Implement `PropertyRepository`, `TenantRepository`, etc., using `FirebaseFirestore`.
3.  **Storage Integration**: Use `FirebaseStorage` for media (apartment photos, receipts).
4.  **FCM Setup**: Register device tokens and handle incoming notifications.
