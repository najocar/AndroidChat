package com.example.chatwebsocket.data.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.chatwebsocket.data.model.dto.local.MessageLocalDTO

@Dao
interface MessageDAO {

    @Query("SELECT * FROM messages")
    suspend fun getAll(): List<MessageLocalDTO>

    @Query("SELECT * FROM messages WHERE id = :id")
    suspend fun getById(id: Long): MessageLocalDTO

    @Upsert
    suspend fun upsertMessage(vararg message: MessageLocalDTO)

    @Insert
    suspend fun insertAll(vararg messages: MessageLocalDTO)

    @Query("DELETE FROM messages WHERE id >= 0")
    suspend fun deleteAll()
}