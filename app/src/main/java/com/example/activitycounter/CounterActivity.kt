package com.example.activitycounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModelProvider
import com.example.activitycounter.ui.theme.ActivityCounterTheme

@ExperimentalMaterial3Api
class CounterActivity : ComponentActivity() {
    private lateinit var counterViewModel: CounterViewModel
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
}
