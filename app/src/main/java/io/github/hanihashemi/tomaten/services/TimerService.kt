package io.github.hanihashemi.tomaten.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.github.hanihashemi.tomaten.MainActivity
import io.github.hanihashemi.tomaten.R
import io.github.hanihashemi.tomaten.extensions.formatTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerService : Service() {
    companion object {
        const val TIME_PARAM = "startTime"
        const val REMAINING_TIME = "remainingTime"
        const val TIME_UPDATE_ACTION = "TIMER_UPDATED"
        private const val CHANNEL = "TIMER_SERVICE_CHANNEL"
    }

    private var remainingTime: Long = 0
    private var job: Job? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        job = null
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        startForeground()
        val timeInMillis = intent?.getLongExtra(TIME_PARAM, 0) ?: 0
        startTimer(timeInMillis)
        return START_STICKY
    }

    private fun startForeground() {
        val notificationContent = "Timer Started"
        val notification = createNotification(notificationContent, 0, 0)
        val notificationType =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE
            } else {
                0
            }

        ServiceCompat.startForeground(
            this@TimerService,
            1,
            notification,
            notificationType,
        )
    }

    private fun startTimer(timeInMillis: Long) {
        println("==> startTimer: $timeInMillis")
        remainingTime = timeInMillis
        job =
            CoroutineScope(Dispatchers.IO).launch {
                while (remainingTime > 0) {
                    val progress = timeInMillis - remainingTime
                    val content = "Remaining Time: ${remainingTime.formatTime()}"

                    sendTimeUpdate()
                    updateNotification(
                        content,
                        progress.toInt(),
                        timeInMillis.toInt(),
                    )
                    delay(1000) // delay for 1 second
                    remainingTime -= 1
                }
                sendTimeUpdate()
                showCompletionNotification()
            }
    }

    private fun sendTimeUpdate() {
        val intent = Intent(TIME_UPDATE_ACTION)
        intent.putExtra(REMAINING_TIME, remainingTime)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun updateNotification(
        content: String,
        progress: Int = 0,
        maxProgress: Int = 0,
    ) {
        val notification = createNotification(content, progress, maxProgress)
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

    private fun createNotificationChannel() {
        val serviceChannel =
            NotificationChannel(
                CHANNEL,
                "Timer Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }

    private fun createNotification(
        content: String,
        progress: Int,
        maxProgress: Int,
    ): Notification {
        val title = "Timer Service"
        return NotificationCompat.Builder(this, CHANNEL)
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                ),
            )
            .setSmallIcon(R.drawable.tomato)
            .setProgress(maxProgress, progress, false)
            .setSilent(true) // Don't play a sound
            .build()
    }

    private fun showCompletionNotification() {
        val title = "Timer Service"
        val content = "Timer Completed"
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )

        val notification =
            NotificationCompat.Builder(this, CHANNEL)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.tomato)
                .setContentIntent(pendingIntent)
                .build()

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(2, notification)
    }
}
