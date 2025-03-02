package com.example.activitycounter.presentation

/*
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class CounterViewModel(application: Application) : AndroidViewModel(application) {
    private val _tapCount = MutableLiveData(0)
    val tapCount: LiveData<Int> = _tapCount

    private val _activityStatus = MutableLiveData(ActivityStatus.Idle)
    val activityStatus: LiveData<ActivityStatus> = _activityStatus

    private val _isTracking = MutableLiveData(false)
    val isTracking: LiveData<Boolean> = _isTracking

    private val _countList = mutableListOf<Long>()//a list for storing the count in 5s
    private var executor: ScheduledExecutorService? =
        null //an executor on the background thread to check for activity status every 1s

    fun updateTracking() {
        Log.d("executor", "update tracking")
        _isTracking.value = _isTracking.value?.not() ?: false
        if (!_isTracking.value!!) {
            resetCounter()
            stopExecutorService()
        } else {
            startExecutorService()
            _activityStatus.value = ActivityStatus.Active
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

    }

    private fun startExecutorService() {
        if (executor == null || executor!!.isShutdown || executor!!.isTerminated) {
            executor =
                Executors.newSingleThreadScheduledExecutor() // Reinitialize executor if it's shut down
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
            _countList.removeAll { now - it > 5000 } //removing elements not in the 5s gap
            if (_countList.size >= 5) {//checking for time condition and changing status
                _activityStatus.postValue(ActivityStatus.Active)
            } else {
                _activityStatus.postValue(ActivityStatus.Idle)
            }
        } catch (e: Exception) {
            Log.d("executorError", e.message.toString())
        }
    }

    override fun onCleared() {
        super.onCleared()
        executor?.shutdown()
    }


*/
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.activitycounter.domain.ActivityStatus
import com.example.activitycounter.data.CounterRepository

class CounterViewModel(application: Application) : AndroidViewModel(application) {
    private val counterRepository = CounterRepository.getInstance()
    val tapCount: LiveData<Int> = counterRepository.tapCount
    val activityStatus: LiveData<ActivityStatus> = counterRepository.activityStatus
    val isTracking: LiveData<Boolean> = counterRepository.isTracking

    fun updateTracking() = counterRepository.updateTracking()
    fun incrementCounter() = counterRepository.incrementCounter()
}
