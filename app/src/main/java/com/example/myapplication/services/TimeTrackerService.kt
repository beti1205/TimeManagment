package com.example.myapplication.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.R
import com.example.myapplication.timetracker.domain.timetracker.TimeTracker
import com.example.myapplication.timetracker.domain.timetracker.TimeTrackerState
import com.example.myapplication.timetracker.domain.usecases.GetLastWorkingSubjectsUseCase
import com.example.myapplication.ui.MainActivity
import com.example.myapplication.utils.convertSecondsToTimeString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimeTrackerService : LifecycleService() {

    @Inject
    lateinit var timeTracker: TimeTracker

    @Inject
    lateinit var getLastWorkingSubjectsUseCase: GetLastWorkingSubjectsUseCase

    private lateinit var notificationBuilder: NotificationCompat.Builder

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("TimeTracker", "action: ${intent?.action}")
        when (intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
            Actions.RESET.toString() -> timeTracker.reset()
            Actions.PAUSE.toString() -> timeTracker.toggleTimer()

        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val onClickPendingIntent = createOnClickPendingIntent()

        notificationBuilder = NotificationCompat.Builder(this, "running_channel")
            .setAutoCancel(false)
            .setOngoing(true)
            .setSilent(true)
            .setSmallIcon(R.drawable.baseline_access_time_24)
            .setContentTitle("Time Management")
            .setContentText("00:00:00")
            .setContentIntent(onClickPendingIntent)

        val notification = notificationBuilder.build()

        startForeground(1, notification)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                timeTracker.state.collectLatest { state ->
                    updateState(state)
                }
            }
        }
    }

    private fun updateState(state: TimeTrackerState) {
        val notificationActionText = if (state.isActive) "Pause" else "Start"
        val pendingIntent = createPausePendingIntent()
        val resetPendingIntent = createResetPendingIntent()

        val notification = notificationBuilder
            .setContentText(state.timeElapsed.convertSecondsToTimeString())
            .setContentTitle(state.workingSubject)
            .clearActions()
            .addAction(
                R.drawable.ic_pause,
                notificationActionText,
                pendingIntent
            )
            .addAction(R.drawable.ic_restart_alt, "Reset", resetPendingIntent)

        val notificationManager = getSystemService<NotificationManager>()
        notificationManager?.notify(1, notification.build())
    }

    private fun createOnClickPendingIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        return PendingIntent.getActivity(
            this,
            0,
            intent,
            FLAG_IMMUTABLE
        )
    }

    private fun createResetPendingIntent(): PendingIntent {
        val resetIntent = Intent(this, TimeTrackerService::class.java).apply {
            action = Actions.RESET.toString()
        }
        return PendingIntent.getService(this, 0, resetIntent, FLAG_IMMUTABLE)
    }

    private fun createPausePendingIntent(): PendingIntent {
        val pauseIntent = Intent(this, TimeTrackerService::class.java).apply {
            action = Actions.PAUSE.toString()
        }
        return PendingIntent.getService(this, 1, pauseIntent, FLAG_IMMUTABLE)
    }

    enum class Actions {
        START, STOP, RESET, PAUSE
    }
}