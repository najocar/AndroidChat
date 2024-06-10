package com.example.chatwebsocket.data.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.chatwebsocket.data.model.dto.local.UserLocalDTO

@Dao
interface UserDAO {

    @Query("SELECT * FROM user ORDER BY id LIMIT 1")
    fun getUser(): UserLocalDTO

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getById(id: Long): UserLocalDTO

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun selectFirst(): UserLocalDTO?

    @Upsert
    suspend fun upsert(vararg users: UserLocalDTO)

    @Update
    suspend fun update(user: UserLocalDTO)
}