package com.example.chatwebsocket.data.network

import com.example.chatwebsocket.data.model.entity.ChatModel
import retrofit2.http.GET

interface ChatApiClient {

    @GET("/api/v1/chats")
    suspend fun getAllChats(): List<ChatModel>
}