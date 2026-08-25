package com.him.landlordtenant.app.utils

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.him.landlordtenant.app.workers.DataSyncWorker
import com.him.landlordtenant.app.workers.NotificationSyncWorker
import java.util.concurrent.TimeUnit

object WorkerUtils {

    fun schedulePeriodicDataSync(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<DataSyncWorker>(
            1, TimeUnit.HOURS,
            15, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "DataSyncWork",
            androidx.work.ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }

    fun scheduleNotificationSync(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val notificationRequest = PeriodicWorkRequestBuilder<NotificationSyncWorker>(
            30, TimeUnit.MINUTES,
            5, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "NotificationSyncWork",
            androidx.work.ExistingPeriodicWorkPolicy.KEEP,
            notificationRequest
        )
    }
}
