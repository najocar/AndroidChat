package com.example.chatwebsocket.data.network

import com.example.chatwebsocket.data.model.dto.chatdto.MessageSendDTO
import com.example.chatwebsocket.data.model.dto.local.MessageLocalDTO
import com.example.chatwebsocket.data.model.entity.MessageModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MessageApiClient {
    @GET("/api/v1/messages/{page}/last")
    suspend fun getMessagesFromChat(@Path("page") page: Int, @Query("from") from: Int, @Query("chat") chat: Long): List<MessageModel>

    @GET("/api/v1/messages/{id}")
    suspend fun getMessage(@Path("id") messageId: String): MessageModel

    @POST("/api/v1/messages")
    suspend fun sendMessage(@Body message: MessageSendDTO, @Query("chat") chat: Long): MessageModel

    @PUT("/api/v1/messages/{id}")
    suspend fun updateMessage(@Body message: MessageSendDTO, @Path("id") messageId: String): MessageModel

    @DELETE("/api/v1/messages/{id}")
    suspend fun deleteMessage(@Path("id") messageId: String): Response<MessageModel>
//    suspend fun deleteMessage(@Query("id") messageId: String)
}