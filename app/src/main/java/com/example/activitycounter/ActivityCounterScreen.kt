package com.example.activitycounter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@ExperimentalMaterial3Api
@Composable
fun ActivityCounterScreen(viewModel: CounterViewModel) {
    val tapCount = viewModel.tapCount.observeAsState(0)
    val activityStatus = viewModel.activityStatus.observeAsState(ActivityStatus.Idle)
    val isTracking = viewModel.isTracking.observeAsState(false)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Activity Counter") },
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tap Count: ${tapCount.value}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Status: ${activityStatus.value}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                if (isTracking.value) {
                    Button(
                        onClick = { viewModel.incrementCounter() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Blue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Tap Me")
                    }
                }
                Button(
                    onClick = { viewModel.updateTracking() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = if (isTracking.value) "Stop" else "Start")
                }
            }
        }

    }
}