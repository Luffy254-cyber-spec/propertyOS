package com.him.landlordtenant.app.di

import com.him.landlordtenant.app.interfaces.*
import com.him.landlordtenant.app.interfaces.repository.impl.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindPropertyRepository(impl: PropertyRepositoryImpl): PropertyRepository

    @Binds
    @Singleton
    abstract fun bindTenantRepository(impl: TenantRepositoryImpl): TenantRepository

    @Binds
    @Singleton
    abstract fun bindAgreementRepository(impl: AgreementRepositoryImpl): AgreementRepository

    @Binds
    @Singleton
    abstract fun bindPropertyBillingRepository(impl: BillingRepositoryImpl): PropertyBillingRepository

    @Binds
    @Singleton
    abstract fun bindMessageRepository(impl: ChatRepositoryImpl): MessageRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository

    @Binds
    @Singleton
    abstract fun bindSupportRepository(impl: EmergencyRepositoryImpl): SupportRepository

    @Binds
    @Singleton
    abstract fun bindLandlordRepository(impl: LandlordRepositoryImpl): LandlordRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository

    @Binds
    @Singleton
    abstract fun bindMaintenanceRepository(impl: MaintenanceRepositoryImpl): MaintenanceRepository

    @Binds
    @Singleton
    abstract fun bindDocumentRepository(impl: MediaRepositoryImpl): DocumentRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(impl: NotificationRepositoryImpl): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindPaymentRepository(impl: PaymentRepositoryImpl): PaymentRepository

    @Binds
    @Singleton
    abstract fun bindProfessionalRepository(impl: ProfessionalRepositoryImpl): ProfessionalRepository

    @Binds
    @Singleton
    abstract fun bindHouseRepository(impl: HouseRepositoryImpl): HouseRepository

    @Binds
    @Singleton
    abstract fun bindApartmentRepository(impl: ApartmentRepositoryImpl): ApartmentRepository

    @Binds
    @Singleton
    abstract fun bindPropertyListingRepository(impl: PropertyListingRepositoryImpl): PropertyListingRepository

    @Binds
    @Singleton
    abstract fun bindExpenseRepository(impl: ExpenseRepositoryImpl): ExpenseRepository

    @Binds
    @Singleton
    abstract fun bindViewingRepository(impl: ViewingRepositoryImpl): ViewingRepository

    @Binds
    @Singleton
    abstract fun bindStaffRepository(impl: StaffRepositoryImpl): StaffRepository

    @Binds
    @Singleton
    abstract fun bindActivityRepository(impl: ActivityRepositoryImpl): ActivityRepository
}
