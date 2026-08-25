package com.him.landlordtenant.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.him.landlordtenant.app.data.dao.*
import com.him.landlordtenant.app.data.entities.*

@Database(
    entities = [
        ApartmentEntity::class,
        BillEntity::class,
        HouseEntity::class,
        MaintenanceEntity::class,
        NotificationEntity::class,
        PaymentEntity::class,
        TenantEntity::class,
        UserEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DatabaseConverters::class)
abstract class PropertyOSDatabase : RoomDatabase() {
    abstract fun apartmentDao(): ApartmentDao
    abstract fun billDao(): BillDao
    abstract fun houseDao(): HouseDao
    abstract fun maintenanceDao(): MaintenanceDao
    abstract fun notificationDao(): NotificationDao
    abstract fun paymentDao(): PaymentDao
    abstract fun tenantDao(): TenantDao
    abstract fun userDao(): UserDao
}


