package com.example.activitycounter.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.activitycounter.domain.ActivityStatus
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class CounterRepository private constructor() {

    private val _tapCount = MutableLiveData(0)
    val tapCount: LiveData<Int> = _tapCount

    private val _activityStatus = MutableLiveData(ActivityStatus.Idle)
    val activityStatus: LiveData<ActivityStatus> = _activityStatus

    private val _isTracking = MutableLiveData(false)
    val isTracking: LiveData<Boolean> = _isTracking

    private val _countList = mutableListOf<Long>() // Stores taps in 5s
    private var executor: ScheduledExecutorService? = null // Background task executor

    companion object {
        @Volatile
        private var INSTANCE: CounterRepository? = null

        fun getInstance(): CounterRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CounterRepository().also { INSTANCE = it }
            }
        }
    }

    fun updateCurrentValues(tapCount: Int, isTracking: Boolean, status: String) {
        _tapCount.value = tapCount
        _isTracking.value = isTracking
        _activityStatus.value = ActivityStatus.valueOf(status)
    }

    fun updateTracking() {
        _isTracking.value = _isTracking.value?.not() ?: false
        if (!_isTracking.value!!) {
            resetCounter()
            stopExecutorService()
        } else {
            startExecutorService()
        }
    }

    private fun resetCounter() {
        _tapCount.value = 0
        _activityStatus.value = ActivityStatus.Idle
    }

    fun incrementCounter() {
        val now = System.currentTimeMillis()
        _tapCount.value = (_tapCount.value ?: 0) + 1
        _countList.add(now)
        _countList.removeAll { now - it > 5000 } // Remove old taps (outside 5s)
    }

    private fun startExecutorService() {
        if (executor == null || executor!!.isShutdown || executor!!.isTerminated) {
            executor = Executors.newSingleThreadScheduledExecutor()
        }

        executor!!.scheduleWithFixedDelay({
            updateActivityStatus()
        }, 0, 1, TimeUnit.SECONDS)
    }

    private fun stopExecutorService() {
        executor?.shutdown()
        executor = null
    }


    private fun updateActivityStatus() {
        try {
            val now = System.currentTimeMillis()
            _countList.removeAll { now - it > 5000 } // Remove old taps (outside 5s)
            _activityStatus.postValue(if (_countList.size >= 5) ActivityStatus.Active else ActivityStatus.Idle)
        } catch (e: Exception) {
            Log.d("executorError", e.message.toString())
        }
    }
}
