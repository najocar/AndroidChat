package com.example.chatwebsocket.data.model.dto.local

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.chatwebsocket.data.model.entity.MessageModel

@Entity(tableName = "messages")
data class MessageLocalDTO(
    @PrimaryKey var id: String,
    var content: String,
    var userId: Int,
    var userName: String,
    var chatId: Long,
    var createdAt: Int,
    var updatedAt: Int
) {
    fun toMessageModel(): MessageModel {
        return MessageModel(
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
