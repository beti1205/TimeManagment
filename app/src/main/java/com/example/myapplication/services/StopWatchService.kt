package com.example.myapplication.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.data.StopWatch
import com.example.myapplication.data.StopWatchState
import com.example.myapplication.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class StopWatchService : LifecycleService() {

    @Inject
    lateinit var stopWatch: StopWatch

    private lateinit var notificationBuilder: NotificationCompat.Builder

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()

        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        notificationBuilder = NotificationCompat.Builder(this, "running_channel")
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.baseline_access_time_24)
            .setContentTitle("Time Management")
            .setContentText("00:00:00")
            .setContentIntent(pendingIntent)

        val notification = notificationBuilder.build()

        startForeground(1, notification)

        lifecycleScope.launchWhenStarted {
            stopWatch.state.collectLatest { state ->
                updateTime(state)
            }
        }
    }

    private fun updateTime(state: StopWatchState) {
        val notification = notificationBuilder.setContentText(state.timeAmount)
        val notificationManager = getSystemService<NotificationManager>()
        notificationManager?.notify(1, notification.build())
    }

    enum class Actions {
        START, STOP
    }
}