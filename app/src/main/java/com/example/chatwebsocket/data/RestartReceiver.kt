package com.example.chatwebsocket.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class RestartReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "ACTION_RESTART_WEBSOCKET_SERVICE") {
            val serviceIntent = Intent(context, WssService::class.java)
            ContextCompat.startForegroundService(context, serviceIntent)
        }
    }
}