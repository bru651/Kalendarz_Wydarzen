package com.example.kalendarzwydarze.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
//import com.example.kalendarzwydarze.notifications.NotificationWorker


class DailyNotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        // Replace this with logic to query your database for today's/tomorrow's/upcoming events or goals.
        showNotification("Kalendarz Wydarzeń", "Sprawdź wydarzenia i cele na dziś!")

        return Result.success()
    }

    private fun showNotification(title: String, content: String) {
        val channelId = "event_notifications"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create channel (only once)
        val channel = NotificationChannel(
            channelId,
            "Event Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        // Build notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // Show it
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}

// Delay until 8:00 AM
fun calculateDelayTo8AM(): Long {
    val now = LocalDateTime.now()
    val next8AM = now.withHour(8).withMinute(0).withSecond(0).withNano(0)
        .let { if (now >= it) it.plusDays(1) else it }

    return Duration.between(now, next8AM).toMillis()
}

// schedule notifications
fun scheduleDailyNotificationWorker(context: Context, testMode: Boolean = false) {
    val workManager = WorkManager.getInstance(context)

    /*if (testMode) {
        // One-time immediate test run
        val testRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .build()
        workManager.enqueue(testRequest)
    }*/

    val request = if (testMode) {
        PeriodicWorkRequestBuilder<DailyNotificationWorker>(2, TimeUnit.MINUTES)
            .setInitialDelay(0, TimeUnit.MINUTES)
            .build()
    } else {
        PeriodicWorkRequestBuilder<DailyNotificationWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(calculateDelayTo8AM(), TimeUnit.MILLISECONDS)
            .build()
    }

    workManager.enqueueUniquePeriodicWork(
        "DailyEventNotification",
        ExistingPeriodicWorkPolicy.UPDATE,
        request
    )
}
