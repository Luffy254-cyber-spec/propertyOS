package com.him.landlordtenant.app.di

import android.content.Context
import androidx.room.Room
import com.him.landlordtenant.app.data.local.PropertyOSDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PropertyOSDatabase {
        return Room.databaseBuilder(
            context,
            PropertyOSDatabase::class.java,
            "property_os_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideUserDao(db: PropertyOSDatabase) = db.userDao()

    @Provides
    fun provideApartmentDao(db: PropertyOSDatabase) = db.apartmentDao()

    @Provides
    fun provideHouseDao(db: PropertyOSDatabase) = db.houseDao()

    @Provides
    fun provideTenantDao(db: PropertyOSDatabase) = db.tenantDao()

    @Provides
    fun provideBillDao(db: PropertyOSDatabase) = db.billDao()

    @Provides
    fun providePaymentDao(db: PropertyOSDatabase) = db.paymentDao()

    @Provides
    fun provideMaintenanceDao(db: PropertyOSDatabase) = db.maintenanceDao()

    @Provides
    fun provideNotificationDao(db: PropertyOSDatabase) = db.notificationDao()
}
