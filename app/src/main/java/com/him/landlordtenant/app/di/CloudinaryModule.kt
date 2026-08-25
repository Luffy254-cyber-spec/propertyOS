package com.him.landlordtenant.app.di

import android.content.Context
import com.cloudinary.android.MediaManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CloudinaryModule {

    @Provides
    @Singleton
    fun provideMediaManager(@ApplicationContext context: Context): MediaManager {
        // Cloudinary MediaManager is usually a singleton after initialization
        return try {
            MediaManager.get()
        } catch (e: Exception) {
            // Updated with user dashboard details
            val config = mapOf(
                "cloud_name" to "yauqylbp",
                "api_key" to "564414674728954", // Standard Cloudinary API key length is 15 digits
                "api_secret" to "M_secret_placeholder"
            )
            MediaManager.init(context, config)
            MediaManager.get()
        }
    }
}
