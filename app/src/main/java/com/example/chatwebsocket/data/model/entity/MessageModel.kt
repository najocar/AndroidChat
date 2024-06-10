package com.example.chatwebsocket.data.model.entity

import com.example.chatwebsocket.data.model.dto.local.MessageLocalDTO
import com.google.gson.annotations.SerializedName

data class MessageModel(
    @SerializedName("id")var id: String,
    @SerializedName("content") var content: String,
    @SerializedName("user_id") var userId: Int,
    @SerializedName("user_name") var userName: String,
    @SerializedName("chat_id") var chatId: Long,
    @SerializedName("created_at") var createdAt: Int,
    @SerializedName("updated_at") var updatedAt: Int
) {
    fun toMessageLocalDTO(): MessageLocalDTO {
        return MessageLocalDTO(
            id = id,
            content = content,
            userId = userId,
            userName = userName,
            chatId = chatId,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
