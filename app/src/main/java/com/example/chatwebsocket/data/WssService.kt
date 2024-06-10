package com.example.chatwebsocket.data

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.chatwebsocket.BuildConfig
import com.example.chatwebsocket.data.network.websocket.WebSocketListener
import com.example.chatwebsocket.data.provider.UserProvider
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import javax.inject.Inject

@AndroidEntryPoint
class WssService : Service() {
    @Inject lateinit var webSocketListener: WebSocketListener
    @Inject lateinit var okHttpClient: OkHttpClient
    @Inject lateinit var userProvider: UserProvider
    private var webSocket: WebSocket? = null

    override fun onCreate() {
        super.onCreate()
        Log.d("WssService", "WebSocketListener injected: $webSocketListener")
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        webSocket = okHttpClient.newWebSocket(createRequest(), webSocketListener)
        val subscribeMessage = """
                    {
                      "command": "subscribe",
                      "identifier": "{\"channel\":\"ChatChannel\", \"cable_id\":\"${userProvider.userId}\", \"device_token\":\"${userProvider.deviceToken}\"}"
                    }
                """.trimIndent()
        webSocket?.send(subscribeMessage)
        webSocket?.request()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocket?.close(1000, "Service destroyed")
        val broadcastIntent = Intent("ACTION_RESTART_WEBSOCKET_SERVICE")
        sendBroadcast(broadcastIntent)
    }

    fun createRequest(): Request {
        val webSocketURL = BuildConfig.WSS_URL
        return Request.Builder()
            .url(webSocketURL)
            .build()
    }
}