package com.him.landlordtenant.app

import android.app.Application
import com.cloudinary.android.MediaManager
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.him.landlordtenant.app.utils.WorkerUtils
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class PropertyOSApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        
        // Initialize App Check with Play Integrity (Default for Android)
        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )

        // Remote Config
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)

        // Initialize Cloudinary
        try {
            // Check if already initialized to avoid IllegalStateException
            try {
                MediaManager.get()
                println("Cloudinary already initialized")
            } catch (e: Exception) {
                val config = mapOf(
                    "cloud_name" to "yauqylbp",
                    "api_key" to "564414674728954",
                    "api_secret" to "M_secret_placeholder"
                )
                MediaManager.init(this, config)
                println("Cloudinary initialized successfully with yauqylbp")
            }
        } catch (e: Exception) {
            println("Cloudinary initialization failed: ${e.message}")
        }

        // Initialize periodic background tasks
        try {
            WorkerUtils.schedulePeriodicDataSync(this)
            WorkerUtils.scheduleNotificationSync(this)
        } catch (e: Exception) {
            // Log to Crashlytics if available
        }
    }
}
