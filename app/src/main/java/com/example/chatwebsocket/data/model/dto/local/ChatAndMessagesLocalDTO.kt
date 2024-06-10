package com.example.chatwebsocket.data.model.dto.local

import androidx.room.Embedded
import androidx.room.Relation

data class ChatAndMessagesLocalDTO(
    @Embedded val chat: ChatLocalDTO,
    @Relation(
        parentColumn = "id",
        entityColumn = "chatId"
    )
    val messages: List<MessageLocalDTO>
)
