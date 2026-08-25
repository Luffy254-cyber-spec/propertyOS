package com.him.landlordtenant.app.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.him.landlordtenant.app.data.model.*

class DatabaseConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String = gson.toJson(value)

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromApartmentAmenityList(value: List<ApartmentAmenity>): String = gson.toJson(value)

    @TypeConverter
    fun toApartmentAmenityList(value: String): List<ApartmentAmenity> {
        val listType = object : TypeToken<List<ApartmentAmenity>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromSecurityFeatureList(value: List<SecurityFeature>): String = gson.toJson(value)

    @TypeConverter
    fun toSecurityFeatureList(value: String): List<SecurityFeature> {
        val listType = object : TypeToken<List<SecurityFeature>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromHouseTypeList(value: List<HouseType>): String = gson.toJson(value)

    @TypeConverter
    fun toHouseTypeList(value: String): List<HouseType> {
        val listType = object : TypeToken<List<HouseType>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromPaymentMethodList(value: List<com.him.landlordtenant.app.enums.PaymentMethod>): String = gson.toJson(value)

    @TypeConverter
    fun toPaymentMethodList(value: String): List<com.him.landlordtenant.app.enums.PaymentMethod> {
        val listType = object : TypeToken<List<com.him.landlordtenant.app.enums.PaymentMethod>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromHouseFacilityList(value: List<HouseFacility>): String = gson.toJson(value)

    @TypeConverter
    fun toHouseFacilityList(value: String): List<HouseFacility> {
        val listType = object : TypeToken<List<HouseFacility>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromMaintenanceAttachmentList(value: List<MaintenanceAttachment>): String = gson.toJson(value)

    @TypeConverter
    fun toMaintenanceAttachmentList(value: String): List<MaintenanceAttachment> {
        val listType = object : TypeToken<List<MaintenanceAttachment>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromPaymentAuditEventList(value: List<PaymentAuditEvent>): String = gson.toJson(value)

    @TypeConverter
    fun toPaymentAuditEventList(value: String): List<PaymentAuditEvent> {
        val listType = object : TypeToken<List<PaymentAuditEvent>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromTenantOccupantList(value: List<TenantOccupant>): String = gson.toJson(value)

    @TypeConverter
    fun toTenantOccupantList(value: String): List<TenantOccupant> {
        val listType = object : TypeToken<List<TenantOccupant>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromPreviousTenancyList(value: List<PreviousTenancy>): String = gson.toJson(value)

    @TypeConverter
    fun toPreviousTenancyList(value: String): List<PreviousTenancy> {
        val listType = object : TypeToken<List<PreviousTenancy>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromPropertyType(value: PropertyType): String = value.name
    @TypeConverter
    fun toPropertyType(value: String): PropertyType = PropertyType.valueOf(value)

    @TypeConverter
    fun fromApartmentCategory(value: ApartmentCategory): String = value.name
    @TypeConverter
    fun toApartmentCategory(value: String): ApartmentCategory = ApartmentCategory.valueOf(value)

    @TypeConverter
    fun fromVerificationStatus(value: VerificationStatus): String = value.name
    @TypeConverter
    fun toVerificationStatus(value: String): VerificationStatus = VerificationStatus.valueOf(value)

    @TypeConverter
    fun fromApartmentListingStatus(value: ApartmentListingStatus): String = value.name
    @TypeConverter
    fun toApartmentListingStatus(value: String): ApartmentListingStatus = ApartmentListingStatus.valueOf(value)

    @TypeConverter
    fun fromBillType(value: BillType): String = value.name
    @TypeConverter
    fun toBillType(value: String): BillType = BillType.valueOf(value)

    @TypeConverter
    fun fromBillStatus(value: BillStatus): String = value.name
    @TypeConverter
    fun toBillStatus(value: String): BillStatus = BillStatus.valueOf(value)

    @TypeConverter
    fun fromHouseType(value: HouseType): String = value.name
    @TypeConverter
    fun toHouseType(value: String): HouseType = HouseType.valueOf(value)

    @TypeConverter
    fun fromHouseStatus(value: HouseStatus): String = value.name
    @TypeConverter
    fun toHouseStatus(value: String): HouseStatus = HouseStatus.valueOf(value)

    @TypeConverter
    fun fromHouseCondition(value: HouseCondition): String = value.name
    @TypeConverter
    fun toHouseCondition(value: String): HouseCondition = HouseCondition.valueOf(value)

    @TypeConverter
    fun fromBillingMethod(value: BillingMethod): String = value.name
    @TypeConverter
    fun toBillingMethod(value: String): BillingMethod = BillingMethod.valueOf(value)

    @TypeConverter
    fun fromMaintenanceCategory(value: MaintenanceCategory): String = value.name
    @TypeConverter
    fun toMaintenanceCategory(value: String): MaintenanceCategory = MaintenanceCategory.valueOf(value)

    @TypeConverter
    fun fromMaintenancePriority(value: MaintenancePriority): String = value.name
    @TypeConverter
    fun toMaintenancePriority(value: String): MaintenancePriority = MaintenancePriority.valueOf(value)

    @TypeConverter
    fun fromMaintenanceStatus(value: MaintenanceStatus): String = value.name
    @TypeConverter
    fun toMaintenanceStatus(value: String): MaintenanceStatus = MaintenanceStatus.valueOf(value)

    @TypeConverter
    fun fromNotificationType(value: NotificationType): String = value.name
    @TypeConverter
    fun toNotificationType(value: String): NotificationType = NotificationType.valueOf(value)

    @TypeConverter
    fun fromNotificationIcon(value: NotificationIcon): String = value.name
    @TypeConverter
    fun toNotificationIcon(value: String): NotificationIcon = NotificationIcon.valueOf(value)

    @TypeConverter
    fun fromNotificationPriority(value: NotificationPriority): String = value.name
    @TypeConverter
    fun toNotificationPriority(value: String): NotificationPriority = NotificationPriority.valueOf(value)

    @TypeConverter
    fun fromNotificationStatus(value: NotificationStatus): String = value.name
    @TypeConverter
    fun toNotificationStatus(value: String): NotificationStatus = NotificationStatus.valueOf(value)

    @TypeConverter
    fun fromPaymentType(value: PaymentType): String = value.name
    @TypeConverter
    fun toPaymentType(value: String): PaymentType = PaymentType.valueOf(value)

    @TypeConverter
    fun fromPaymentMethod(value: com.him.landlordtenant.app.enums.PaymentMethod): String = value.name
    @TypeConverter
    fun toPaymentMethod(value: String): com.him.landlordtenant.app.enums.PaymentMethod = com.him.landlordtenant.app.enums.PaymentMethod.valueOf(value)

    @TypeConverter
    fun fromPaymentProvider(value: PaymentProvider): String = value.name
    @TypeConverter
    fun toPaymentProvider(value: String): PaymentProvider = PaymentProvider.valueOf(value)

    @TypeConverter
    fun fromPaymentStatus(value: PaymentStatus): String = value.name
    @TypeConverter
    fun toPaymentStatus(value: String): PaymentStatus = PaymentStatus.valueOf(value)

    @TypeConverter
    fun fromPaymentVerificationStatus(value: PaymentVerificationStatus): String = value.name
    @TypeConverter
    fun toPaymentVerificationStatus(value: String): PaymentVerificationStatus = PaymentVerificationStatus.valueOf(value)

    @TypeConverter
    fun fromTenantIdType(value: TenantIdType): String = value.name
    @TypeConverter
    fun toTenantIdType(value: String): TenantIdType = TenantIdType.valueOf(value)

    @TypeConverter
    fun fromIdentityVerificationStatus(value: IdentityVerificationStatus): String = value.name
    @TypeConverter
    fun toIdentityVerificationStatus(value: String): IdentityVerificationStatus = IdentityVerificationStatus.valueOf(value)

    @TypeConverter
    fun fromTenancyStatus(value: TenancyStatus): String = value.name
    @TypeConverter
    fun toTenancyStatus(value: String): TenancyStatus = TenancyStatus.valueOf(value)

    @TypeConverter
    fun fromUserRole(value: UserRole): String = value.name
    @TypeConverter
    fun toUserRole(value: String): UserRole = UserRole.valueOf(value)

    @TypeConverter
    fun fromAuthProvider(value: AuthProvider): String = value.name
    @TypeConverter
    fun toAuthProvider(value: String): AuthProvider = AuthProvider.valueOf(value)

    @TypeConverter
    fun fromAccountStatus(value: AccountStatus): String = value.name
    @TypeConverter
    fun toAccountStatus(value: String): AccountStatus = AccountStatus.valueOf(value)

    @TypeConverter
    fun fromUserRoleList(value: List<UserRole>): String = gson.toJson(value)

    @TypeConverter
    fun toUserRoleList(value: String): List<UserRole> {
        val listType = object : TypeToken<List<UserRole>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromIntList(value: List<Int>): String = gson.toJson(value)

    @TypeConverter
    fun toIntList(value: String): List<Int> {
        val listType = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromEmergencyPhoneNumberList(value: List<EmergencyPhoneNumber>): String = gson.toJson(value)

    @TypeConverter
    fun toEmergencyPhoneNumberList(value: String): List<EmergencyPhoneNumber> {
        val listType = object : TypeToken<List<EmergencyPhoneNumber>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromEmergencyOperatingHours(value: EmergencyOperatingHours): String = gson.toJson(value)

    @TypeConverter
    fun toEmergencyOperatingHours(value: String): EmergencyOperatingHours = gson.fromJson(value, EmergencyOperatingHours::class.java)

    @TypeConverter
    fun fromEmergencyServiceList(value: List<EmergencyService>): String = gson.toJson(value)

    @TypeConverter
    fun toEmergencyServiceList(value: String): List<EmergencyService> {
        val listType = object : TypeToken<List<EmergencyService>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromEmergencyContactVerification(value: EmergencyContactVerification): String = gson.toJson(value)

    @TypeConverter
    fun toEmergencyContactVerification(value: String): EmergencyContactVerification = gson.fromJson(value, EmergencyContactVerification::class.java)
}
