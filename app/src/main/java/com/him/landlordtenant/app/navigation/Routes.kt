package com.him.landlordtenant.app.navigation

sealed class Route(val route: String) {
    // Auth Routes
    object Splash : Route("splash")
    object Welcome : Route("welcome/{role}") {
        fun createRoute(role: String) = "welcome/$role"
    }
    object Login : Route("login/{role}") {
        fun createRoute(role: String) = "login/$role"
    }
    object Register : Route("register/{role}") {
        fun createRoute(role: String) = "register/$role"
    }
    object ChooseRole : Route("choose_role")
    object Onboarding : Route("onboarding")
    object GoogleAuth : Route("google_auth/{role}") {
        fun createRoute(role: String) = "google_auth/$role"
    }
    object AuthSuccess : Route("auth_success")

    // Tenant Routes
    object TenantHome : Route("tenant_home")
    object TenantDashboard : Route("tenant_dashboard")
    object TenantSearch : Route("tenant_search")
    object ApartmentDetails : Route("apartment_details/{apartmentId}") {
        fun createRoute(apartmentId: String) = "apartment_details/$apartmentId"
    }
    object HouseSelection : Route("house_selection/{apartmentId}") {
        fun createRoute(apartmentId: String) = "house_selection/$apartmentId"
    }
    object HouseDetails : Route("house_details/{houseId}") {
        fun createRoute(houseId: String) = "house_details/$houseId"
    }
    object TenantPayments : Route("tenant_payments")
    object TenantBills : Route("tenant_bills")
    object TenantRentAndBills : Route("tenant_rent_and_bills")
    object TenantMaintenance : Route("tenant_maintenance")
    object Messages : Route("messages")
    object TenantChat : Route("tenant_chat/{partnerId}") {
        fun createRoute(partnerId: String) = "tenant_chat/$partnerId"
    }
    object TenantAgreement : Route("tenant_agreement")
    object TenantLease : Route("tenant_lease")
    object TenantEmergency : Route("tenant_emergency")
    object TenantVacateNotice : Route("tenant_vacate_notice")
    object TenantJoinAgreement : Route("tenant_join_agreement")

    // Landlord Routes
    object LandlordHome : Route("landlord_home")
    object LandlordDashboard : Route("landlord_dashboard")
    object PropertyManagement : Route("property_management")
    object MyApartments : Route("my_apartments")
    object CreateApartment : Route("create_apartment")
    object ApartmentManagement : Route("apartment_management/{apartmentId}") {
        fun createRoute(apartmentId: String) = "apartment_management/$apartmentId"
    }
    object ApartmentPreview : Route("apartment_preview/{apartmentId}") {
        fun createRoute(apartmentId: String) = "apartment_preview/$apartmentId"
    }
    object LandlordTenants : Route("landlord_tenants")
    object LandlordBilling : Route("landlord_billing")
    object LandlordMaintenance : Route("landlord_maintenance")
    object LandlordReports : Route("landlord_reports")
    object LandlordChat : Route("landlord_chat/{partnerId}") {
        fun createRoute(partnerId: String) = "landlord_chat/$partnerId"
    }
    
    // Landlord Apartment Management Sub-routes
    object ApartmentFloors : Route("apartment_floors/{apartmentId}") {
        fun createRoute(apartmentId: String) = "apartment_floors/$apartmentId"
    }
    object ApartmentMedia : Route("apartment_media/{apartmentId}") {
        fun createRoute(apartmentId: String) = "apartment_media/$apartmentId"
    }
    object ApartmentLocation : Route("apartment_location/{apartmentId}") {
        fun createRoute(apartmentId: String) = "apartment_location/$apartmentId"
    }
    object AddFloor : Route("add_floor/{apartmentId}") {
        fun createRoute(apartmentId: String) = "add_floor/$apartmentId"
    }
    object EditApartment : Route("edit_apartment/{apartmentId}") {
        fun createRoute(apartmentId: String) = "edit_apartment/$apartmentId"
    }
    
    // Common
    object Profile : Route("profile")
    object EditProfile : Route("edit_profile")
    object Settings : Route("settings")
    object Notifications : Route("notifications")
    object About : Route("about")
    object PrivacyPolicy : Route("privacy_policy")
    object TermsOfService : Route("terms_of_service")
    object ContactSupport : Route("contact_support")
    object HelpCenter : Route("help_center")

    // Auth Verifications
    object OtpVerification : Route("otp_verification/{identifier}") {
        fun createRoute(identifier: String) = "otp_verification/$identifier"
    }
    object EmailVerification : Route("email_verification/{email}") {
        fun createRoute(email: String) = "email_verification/$email"
    }
    object PhoneVerification : Route("phone_verification")
    object IdentityVerification : Route("identity_verification")
}
