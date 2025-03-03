package com.example.activitycounter.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.activitycounter.domain.ActivityStatus
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class CounterRepository private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("counter_prefs", Context.MODE_PRIVATE)

    private val _tapCount = MutableLiveData(sharedPreferences.getInt("tap_count", 0))
    val tapCount: LiveData<Int> = _tapCount

    private val _activityStatus = MutableLiveData(
        ActivityStatus.valueOf(
            sharedPreferences.getString("activity_status", "Idle") ?: "Idle"
        )
    )
    val activityStatus: LiveData<ActivityStatus> = _activityStatus

    private val _isTracking = MutableLiveData(sharedPreferences.getBoolean("is_tracking", false))
    val isTracking: LiveData<Boolean> = _isTracking

    private val _countList = mutableListOf<Long>() // Stores taps in 5s
    private var executor: ScheduledExecutorService? = null // Background task executor

    companion object {
        @Volatile
        private var INSTANCE: CounterRepository? = null

        fun getInstance(context: Context): CounterRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CounterRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }


    fun updateTracking() {
        _isTracking.value = _isTracking.value?.not() ?: false
        saveToSharedPreferences()
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
        _countList.clear()
        saveToSharedPreferences()
    }

    fun incrementCounter() {
        val now = System.currentTimeMillis()
        _tapCount.value = (_tapCount.value ?: 0) + 1
        _countList.add(now)
        _countList.removeAll { now - it > 5000 } // Remove old taps (outside 5s)
        saveToSharedPreferences()
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
            saveToSharedPreferences()
        } catch (e: Exception) {
            Log.d("executorError", e.message.toString())
        }
    }

    private fun saveToSharedPreferences() {
        sharedPreferences.edit().apply {
            putInt("tap_count", _tapCount.value ?: 0)
            putBoolean("is_tracking", _isTracking.value ?: false)
            putString("activity_status", _activityStatus.value.toString())
            apply()
        }
    }
}