package com.him.landlordtenant.app.di

import android.content.Context
import com.him.landlordtenant.app.data.preferences.PreferencesManager
import com.him.landlordtenant.app.util.AlertManager
import com.him.landlordtenant.app.util.EmailService
import com.him.landlordtenant.app.util.MockEmailService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManager(context)
    }

    @Provides
    @Singleton
    fun provideAlertManager(): AlertManager {
        return AlertManager()
    }

    @Provides
    @Singleton
    fun provideEmailService(): EmailService {
        return MockEmailService()
    }
}
