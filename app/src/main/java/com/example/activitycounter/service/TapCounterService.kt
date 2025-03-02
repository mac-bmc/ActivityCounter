package com.example.activitycounter.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.activitycounter.R
import com.example.activitycounter.data.CounterRepository
import com.example.activitycounter.reciever.CounterReciever


class TapCounterService : Service() {


    private val channelId = "activity_tracking_channel"
    private val notificationId = 1
    private val counterRepository = CounterRepository.getInstance()

    companion object {
        const val ACTION_TAP = "com.example.activitycounter.ACTION_TAP"
    }


    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(notificationId, createNotification(""))
        counterRepository.activityStatus.observeForever {
            updateNotification(it.toString())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_TAP) {
            sendBroadcast(Intent(ACTION_TAP))
            //updateNotification()
        }
        return START_STICKY
    }

    private fun updateNotification(status: String) {
        val notification = createNotification(status)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(notificationId, notification)
    }

    private fun createNotification(status: String): Notification {
        val tapIntent = Intent(this, CounterReciever::class.java).apply {
            action = ACTION_TAP
        }
        val tapPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Activity Counter")
            .setContentText("Status: $status")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .addAction(R.drawable.ic_click, "Tap", tapPendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Activity Counter", NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
