package com.example.activitycounter.presentation


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.activitycounter.data.CounterRepository
import com.example.activitycounter.domain.ActivityStatus

class CounterViewModel(application: Application) : AndroidViewModel(application) {
    private val counterRepository = CounterRepository.getInstance()
    val tapCount: LiveData<Int> = counterRepository.tapCount
    val activityStatus: LiveData<ActivityStatus> = counterRepository.activityStatus
    val isTracking: LiveData<Boolean> = counterRepository.isTracking

    fun updateTracking() = counterRepository.updateTracking()
    fun incrementCounter() = counterRepository.incrementCounter()
    fun updateCurrentValues(tapCount: Int, isTracking: Boolean, status: String) =
        counterRepository.updateCurrentValues(tapCount, isTracking, status)
}
