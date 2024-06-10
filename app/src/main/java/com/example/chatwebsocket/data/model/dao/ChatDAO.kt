package com.example.chatwebsocket.data.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.chatwebsocket.data.model.dto.local.ChatAndMessagesLocalDTO
import com.example.chatwebsocket.data.model.dto.local.ChatLocalDTO
import com.example.chatwebsocket.data.model.dto.local.MessageLocalDTO

@Dao
interface ChatDAO {
    @Query("SELECT * FROM chats")
    suspend fun getAll(): List<ChatLocalDTO>

    @Query("SELECT * FROM chats WHERE id = :id")
    suspend fun getById(id: Long): ChatLocalDTO

    @Transaction
    @Query("SELECT * FROM chats WHERE id = :id ORDER BY id")
    suspend fun getByIdWithMessages(id: Long): ChatAndMessagesLocalDTO

    @Query("""
    SELECT * FROM messages 
    WHERE chatId = :chatId 
    ORDER BY createdAt DESC
    LIMIT 10
    """)
    suspend fun getMessagesByChatIdLimited(chatId: Long): List<MessageLocalDTO>

    suspend fun getMessagesByChatOrdered(chatId: Long): List<MessageLocalDTO> {
        return getMessagesByChatIdLimited(chatId).reversed()
    }

    @Transaction
    @Upsert
    suspend fun upsertMessage(vararg message: MessageLocalDTO)

    @Insert
    suspend fun insertAll(vararg chats: ChatLocalDTO)

    @Upsert
    suspend fun upsert(chat: ChatLocalDTO)

    @Delete
    suspend fun delete(chat: ChatLocalDTO)

    @Delete
    suspend fun deleteMessage(message: MessageLocalDTO)

    @Query("DELETE FROM chats WHERE id >= 0")
    suspend fun deleteAll() {
        deleteAllMessages()
    }

    @Query("DELETE FROM messages WHERE id >= 0")
    suspend fun deleteAllMessages()
}