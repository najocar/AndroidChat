package com.example.chatwebsocket.data.model.dto.chatdto

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class WssMessage(
    @SerializedName("message") var message: Message
)

data class Message(
    @SerializedName("from") var from: String,
    @SerializedName("chat_id") var chatId: Long,
    @SerializedName("date") var date: Int
)
