package com.him.landlordtenant.app.navigation

sealed class Route(val route: String) {
    // Auth Routes
    object Splash : Route("splash")
    object Welcome : Route("welcome")
    object Login : Route("login")
    object Register : Route("register")
    object ChooseRole : Route("choose_role")
    object Onboarding : Route("onboarding")
    object GoogleAuth : Route("google_auth")
    object AuthSuccess : Route("auth_success")
    object ForgotPassword : Route("forgot_password")

    // Tenant Routes
    object TenantHome : Route("tenant_home")
    object TenantDashboard : Route("tenant_dashboard")
    object MyHouse : Route("my_house")
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
    object TenantJoinAgreement : Route("tenant_join_agreement/{apartmentId}/{apartmentName}/{houseId}/{houseNumber}") {
        fun createRoute(apartmentId: String, apartmentName: String, houseId: String = "GENERAL", houseNumber: String = "GENERAL") = 
            "tenant_join_agreement/$apartmentId/$apartmentName/$houseId/$houseNumber"
    }
    object NewChat : Route("new_chat")
    object ChatDetail : Route("chat_detail/{conversationId}") {
        fun createRoute(conversationId: String) = "chat_detail/$conversationId"
    }
    object PropertyCommunity : Route("property_community/{propertyId}?isAnnouncementsOnly={isAnnouncementsOnly}") {
        fun createRoute(propertyId: String, isAnnouncementsOnly: Boolean = false) = "property_community/$propertyId?isAnnouncementsOnly=$isAnnouncementsOnly"
    }
    object TenantServices : Route("tenant_services")
    object AmenityBooking : Route("amenity_booking/{propertyId}") {
        fun createRoute(propertyId: String) = "amenity_booking/$propertyId"
    }
    object MaintenanceHistory : Route("maintenance_history")
    object TenantRewards : Route("tenant_rewards")
    object UtilityUsage : Route("utility_usage")
    object DocumentVault : Route("document_vault")
    object TenantReceipt : Route("tenant_receipt/{receiptId}") {
        fun createRoute(receiptId: String) = "tenant_receipt/$receiptId"
    }
    object PropertyReviews : Route("property_reviews/{propertyId}") {
        fun createRoute(propertyId: String) = "property_reviews/$propertyId"
    }
    object TenantApplications : Route("tenant_applications")

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
    object LandlordAgreements : Route("landlord_agreements")
    object CreateAgreement : Route("create_agreement")
    object AgreementPreview : Route("agreement_preview/{agreementId}") {
        fun createRoute(agreementId: String) = "agreement_preview/$agreementId"
    }
    object LandlordStaff : Route("landlord_staff")
    object PropertyAnalytics : Route("property_analytics/{propertyId}") {
        fun createRoute(propertyId: String) = "property_analytics/$propertyId"
    }
    object ViewingRequests : Route("viewing_requests")
    object MaintenanceDashboard : Route("maintenance_dashboard")
    object LandlordExpenses : Route("landlord_expenses")
    object LandlordPendingTenants : Route("pending_tenants")
    object LandlordFormerTenants : Route("former_tenants")
    object TenantVerification : Route("tenant_verification/{tenantId}") {
        fun createRoute(tenantId: String) = "tenant_verification/$tenantId"
    }
    object MarketingTools : Route("marketing_tools")
    object LandlordDocuments : Route("landlord_documents")
    object TenantDetails : Route("tenant_details/{tenantId}") {
        fun createRoute(tenantId: String) = "tenant_details/$tenantId"
    }
    object PaymentHistory : Route("payment_history")
    object Receipts : Route("receipts")
    object FinancialSettings : Route("financial_settings/{propertyId}") {
        fun createRoute(propertyId: String) = "financial_settings/$propertyId"
    }
    object MeterReading : Route("meter_reading/{unitName}/{tenantName}/{meterType}/{previousReading}/{ratePerUnit}") {
        fun createRoute(unitName: String, tenantName: String, meterType: String, previousReading: Double, ratePerUnit: Double) = 
            "meter_reading/$unitName/$tenantName/$meterType/$previousReading/$ratePerUnit"
    }
    object MaintenanceDetails : Route("maintenance_details/{requestId}") {
        fun createRoute(requestId: String) = "maintenance_details/$requestId"
    }
    object RevenueReport : Route("revenue_report")
    object PaymentReport : Route("payment_report")
    object OccupancyReport : Route("occupancy_report")
    object ExpensesReport : Route("expenses_report")
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
    object CreateHouse : Route("create_house/{apartmentId}/{floorId}") {
        fun createRoute(apartmentId: String, floorId: String) = "create_house/$apartmentId/$floorId"
    }
    object FloorUnits : Route("floor_units/{apartmentId}/{floorId}") {
        fun createRoute(apartmentId: String, floorId: String) = "floor_units/$apartmentId/$floorId"
    }
    object EditApartment : Route("edit_apartment/{apartmentId}") {
        fun createRoute(apartmentId: String) = "edit_apartment/$apartmentId"
    }
    object HouseManagement : Route("house_management/{apartmentId}/{floorId}/{houseId}") {
        fun createRoute(apartmentId: String, floorId: String, houseId: String) = "house_management/$apartmentId/$floorId/$houseId"
    }
    object EditHouse : Route("edit_house/{apartmentId}/{floorId}/{houseId}") {
        fun createRoute(apartmentId: String, floorId: String, houseId: String) = "edit_house/$apartmentId/$floorId/$houseId"
    }
    object HouseMedia : Route("house_media/{apartmentId}/{floorId}/{houseId}") {
        fun createRoute(apartmentId: String, floorId: String, houseId: String) = "house_media/$apartmentId/$floorId/$houseId"
    }
    object HouseStatusUpdate : Route("house_status/{apartmentId}/{floorId}/{houseId}") {
        fun createRoute(apartmentId: String, floorId: String, houseId: String) = "house_status/$apartmentId/$floorId/$houseId"
    }
    object CreateInvoice : Route("create_invoice/{apartmentId}/{floorId}/{houseId}") {
        fun createRoute(apartmentId: String, floorId: String, houseId: String) = "create_invoice/$apartmentId/$floorId/$houseId"
    }
    object UnitTenantManagement : Route("unit_tenant_management/{apartmentId}/{floorId}/{houseId}") {
        fun createRoute(apartmentId: String, floorId: String, houseId: String) = "unit_tenant_management/$apartmentId/$floorId/$houseId"
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
