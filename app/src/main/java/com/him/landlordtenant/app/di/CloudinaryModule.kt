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
        return try {
            MediaManager.get()
        } catch (e: Exception) {
            val config = mapOf(
                "cloud_name" to "yauqylbp",
                "api_key" to "188633318352559",
                "api_secret" to "M_secret_placeholder"
            )
            MediaManager.init(context, config)
            MediaManager.get()
        }
    }
}
