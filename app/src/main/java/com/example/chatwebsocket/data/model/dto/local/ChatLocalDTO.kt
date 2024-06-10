package com.example.chatwebsocket.data.model.dto.local

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.chatwebsocket.data.model.entity.ChatModel
import java.util.Date

@Entity(tableName = "chats")
data class ChatLocalDTO(
    @PrimaryKey var id: Long,
    var name: String
) {
    fun toChatModel(): ChatModel {
        return ChatModel(
            id = id,
            name = name,
            createdAt = null,
            updatedAt = null
        )
    }
}
