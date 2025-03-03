package com.example.activitycounter.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.activitycounter.R
import com.example.activitycounter.service.TapCounterService
import com.example.activitycounter.ui.theme.ActivityCounterTheme

@ExperimentalMaterial3Api
class CounterActivity : ComponentActivity() {
    private lateinit var counterViewModel: CounterViewModel
    private lateinit var serviceIntent: Intent
    private val notificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, getString(R.string.notification_granted), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, getString(R.string.notification_denied), Toast.LENGTH_SHORT)
                    .show()
            }
        }


    @SuppressLint("InlinedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        counterViewModel = ViewModelProvider(this)[CounterViewModel::class.java]
        notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        if (savedInstanceState != null) {
            counterViewModel.updateCurrentValues(
                tapCount = savedInstanceState.getInt("tap_count"),
                status = savedInstanceState.getString("activity_status") ?: "",
                isTracking = savedInstanceState.getBoolean("is_tracking")
            )
        }
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
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (counterViewModel.isTracking.value == true) {
                serviceIntent = Intent(this, TapCounterService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent)
                } else {
                    startService(serviceIntent)
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.notification_denied), Toast.LENGTH_SHORT).show()
        }
        super.onStop()
    }

    override fun onDestroy() {
        if (::serviceIntent.isInitialized) {
            stopService(serviceIntent)
        }
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (counterViewModel.isTracking.value == true) {//persist data only if activity is tracking is going on
            outState.putInt("tap_count", counterViewModel.tapCount.value ?: 0)
            outState.putBoolean("is_tracking", counterViewModel.isTracking.value ?: false)
            outState.putString("activity_status", counterViewModel.activityStatus.value.toString())
        }
        super.onSaveInstanceState(outState)
    }
}
