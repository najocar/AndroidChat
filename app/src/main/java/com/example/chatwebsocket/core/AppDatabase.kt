package com.example.chatwebsocket.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.chatwebsocket.data.model.dao.ChatDAO
import com.example.chatwebsocket.data.model.dao.MessageDAO
import com.example.chatwebsocket.data.model.dao.UserDAO
import com.example.chatwebsocket.data.model.dto.local.ChatLocalDTO
import com.example.chatwebsocket.data.model.dto.local.MessageLocalDTO
import com.example.chatwebsocket.data.model.dto.local.UserLocalDTO

@Database(entities = [ChatLocalDTO::class, MessageLocalDTO::class, UserLocalDTO::class], version = 4)
abstract class AppDatabase: RoomDatabase() {

    abstract fun chatDAO(): ChatDAO
    abstract fun messageDAO(): MessageDAO
    abstract fun userDAO(): UserDAO
}