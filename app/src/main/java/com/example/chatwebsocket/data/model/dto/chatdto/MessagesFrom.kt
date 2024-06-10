package com.example.chatwebsocket.data.model.dto.chatdto

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.util.Date

data class MessagesFrom(
    @SerializedName("updated_at") var from: Int,
    @SerializedName("chat") var chat: Long,
    @SerializedName("page") var page: Int? = 1
)
