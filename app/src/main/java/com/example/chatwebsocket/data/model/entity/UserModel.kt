package com.example.chatwebsocket.data.model.entity

import com.example.chatwebsocket.data.model.dto.local.UserLocalDTO
import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("id") var id: Long,
    @SerializedName("username") var username: String,
) {
//    fun toUserLocalDTO(): UserLocalDTO {
//        return UserLocalDTO(
//            id = id,
//            username = username
//        )
//    }
}
