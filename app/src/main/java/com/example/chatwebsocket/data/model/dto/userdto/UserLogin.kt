package com.example.chatwebsocket.data.model.dto.userdto

import com.example.chatwebsocket.data.model.dto.local.UserLocalDTO
import com.google.gson.annotations.SerializedName

data class UserLogin(
    @SerializedName("id") var id: Number = 0,
    @SerializedName("username") var username: String,
    @SerializedName("password_digest") var password: String
) {
    fun toUserLocalDTO(): UserLocalDTO {
        return UserLocalDTO(
            id = id.toLong(),
            username = username,
            token = ""
        )
    }
}