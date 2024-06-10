package com.example.chatwebsocket.data.network.websocket

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.fragment.app.viewModels
import com.example.chatwebsocket.data.model.dto.chatdto.Message
import com.example.chatwebsocket.data.model.dto.chatdto.WssMessage
import com.example.chatwebsocket.ui.viewModel.ChatsListViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

class WebSocketListener(): WebSocketListener() {

    private val TAG = "Test"


    private val _sharedFlow = MutableSharedFlow<WssMessage>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val sharedFlow = _sharedFlow.asSharedFlow()

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        println(this.hashCode())
        Log.d(TAG, "onOpen:")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        if (!JSONObject(text).has("type")) {
            val messageAux = JSONObject(text).getJSONObject("message")
            val response = WssMessage(
                Message(
                    messageAux.getString("from"),
                    messageAux.getLong("chat_id"),
                    messageAux.getInt("date")
                )
            )
            println("entra en la emisión")
            println(_sharedFlow.tryEmit(response))
        }
//        println("onMessage: $text")
        Log.d(TAG, "onMessage: $text")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        Log.d(TAG, "onClosing: $code $reason")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        Log.d(TAG, "onClosed: $code $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d(TAG, "onFailure: ${t.message} $response")
        super.onFailure(webSocket, t, response)
    }
}