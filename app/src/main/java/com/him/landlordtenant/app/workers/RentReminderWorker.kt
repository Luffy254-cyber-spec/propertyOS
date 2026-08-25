package com.him.landlordtenant.app.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class RentReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val tenantName = inputData.getString("tenantName") ?: "Tenant"
        val amount = inputData.getDouble("amount", 0.0)

        sendNotification(tenantName, amount)

        return Result.success()
    }

    private fun sendNotification(tenantName: String, amount: Double) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "rent_reminders"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Rent Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders for rent payments"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Rent Due Reminder")
            .setContentText("Hello $tenantName, your rent of KSh $amount is due soon.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}
