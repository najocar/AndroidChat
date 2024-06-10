package com.example.chatwebsocket.data.model.dto.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.chatwebsocket.data.model.dto.userdto.UserToken

@Entity(tableName = "user")
data class UserLocalDTO(
    @PrimaryKey var id: Long,
    var username: String,
    var token: String
)
