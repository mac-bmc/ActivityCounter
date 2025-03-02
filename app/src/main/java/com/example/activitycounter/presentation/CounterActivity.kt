package com.example.activitycounter.presentation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModelProvider
import com.example.activitycounter.service.TapCounterService
import com.example.activitycounter.ui.theme.ActivityCounterTheme

@ExperimentalMaterial3Api
class CounterActivity : ComponentActivity() {
    private lateinit var counterViewModel: CounterViewModel
    private lateinit var serviceIntent: Intent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        counterViewModel = ViewModelProvider(this)[CounterViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            ActivityCounterTheme {
                ActivityCounterScreen(counterViewModel)
            }
        }
    }

    override fun onResume() {
        if (::serviceIntent.isInitialized) {
            stopService(serviceIntent)
        }
        super.onResume()
    }

    override fun onStop() {
        if (counterViewModel.isTracking.value == true) {
            serviceIntent = Intent(this, TapCounterService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent)
            } else {
                startService(serviceIntent)
            }
        }

        super.onStop()
    }

    override fun onDestroy() {
        if (::serviceIntent.isInitialized) {
            stopService(serviceIntent)
        }
        super.onDestroy()
    }

}
