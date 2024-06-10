package com.example.chatwebsocket.data.network

import com.example.chatwebsocket.data.model.dto.userdto.UserLogin
import com.example.chatwebsocket.data.model.dto.userdto.UserToken
import com.example.chatwebsocket.data.model.entity.UserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserApiClient {

    @POST("/api/v1/users/login")
    suspend fun login(@Body user: UserLogin): UserToken

    @POST("/api/v1/users")
    suspend fun register(@Body user: UserLogin): UserToken

    @PUT("/api/v1/users/token")
    suspend fun updateFirebaseToken(@Body user: UserToken): Response<UserModel>
}