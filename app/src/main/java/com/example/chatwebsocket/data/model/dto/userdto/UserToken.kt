package com.example.chatwebsocket.data.model.dto.userdto

import com.google.gson.annotations.SerializedName

data class UserToken(
    @SerializedName("token") var token: String,
    @SerializedName("user_id") var userId: Int
)
