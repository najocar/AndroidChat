package com.example.chatwebsocket.core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
     fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://www.my-api-rails.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}