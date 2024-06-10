package com.example.chatwebsocket

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.chatwebsocket.data.WssWorkManager
import com.example.chatwebsocket.data.provider.UserProvider
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class ChatWebSocketApp: Application() {
    @Inject
    lateinit var userProvider: UserProvider

    override fun onCreate() {
        super.onCreate()
        configureWorkManager()
        CoroutineScope(Dispatchers.IO).launch {
            userProvider.initialize()
        }
    }

    private fun configureWorkManager() {
        val workRequest = PeriodicWorkRequestBuilder<WssWorkManager>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "WebSocketWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }
}