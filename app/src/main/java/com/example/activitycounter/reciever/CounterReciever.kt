package com.example.activitycounter.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.activitycounter.service.TapCounterService
import com.example.activitycounter.data.CounterRepository

class CounterReciever : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TapCounterService.ACTION_TAP) {
            val counterRepository = CounterRepository.getInstance()
            counterRepository.incrementCounter()
        }
    }
}