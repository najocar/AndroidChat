package com.example.chatwebsocket.data.model.entity

import com.example.chatwebsocket.data.model.dto.local.ChatLocalDTO
import com.google.gson.annotations.SerializedName
import java.util.Date

data class ChatModel(
    @SerializedName("id") var id: Long,
    @SerializedName("user_name") var name: String,
    @SerializedName("created_at") var createdAt: Date?,
    @SerializedName("updated_at") var updatedAt: Date?
) {
    fun toChatLocalDTO(): ChatLocalDTO {
        return ChatLocalDTO(
            id = id,
            name = name
        )
    }
}
