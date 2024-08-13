package com.example.myapplication.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.services.TimeTrackerService
import com.example.myapplication.timetracker.domain.timetracker.TimeTracker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class  MainActivity : ComponentActivity() {

    @Inject
    lateinit var timeTracker: TimeTracker

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { updateLocale(it, Locale.ENGLISH) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }

        setContent {
            App()
        }

        lifecycleScope.launch {
            timeTracker.state.map { it.isActive }.distinctUntilChanged().collect { isActive ->
                Intent(this@MainActivity, TimeTrackerService::class.java)
                    .also { intent ->
                        if (isActive) {
                            intent.action = TimeTrackerService.Actions.START.toString()
                        } else {
                            intent.action = TimeTrackerService.Actions.STOP.toString()
                        }
                        startService(intent)
                    }
            }
        }
    }

    private fun updateLocale(context: Context, locale: Locale): Context {
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)

        return context.createConfigurationContext(configuration)
    }

}
