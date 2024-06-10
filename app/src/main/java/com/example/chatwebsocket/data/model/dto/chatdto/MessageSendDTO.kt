package com.example.chatwebsocket.data.model.dto.chatdto

import com.google.gson.annotations.SerializedName

data class MessageSendDTO(
    @SerializedName("content") var content: String,
    @SerializedName("message_id") var messageId: String = ""
)
