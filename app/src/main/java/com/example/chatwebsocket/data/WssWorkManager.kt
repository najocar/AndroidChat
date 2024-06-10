package com.example.chatwebsocket.data

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters

class WssWorkManager (val context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
//        val serviceIntent = Intent(applicationContext, WssService::class.java)
//        try {
//            context.startService(serviceIntent)
//            return Result.success()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return Result.failure()
        return Result.success()
    }
}