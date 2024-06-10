package com.example.chatwebsocket.core

import android.content.Context
import com.example.chatwebsocket.BuildConfig
import com.example.chatwebsocket.data.network.ChatApiClient
import com.example.chatwebsocket.data.network.MessageApiClient
import com.example.chatwebsocket.data.network.UserApiClient
import com.example.chatwebsocket.data.network.websocket.WebSocketListener
import com.example.chatwebsocket.data.provider.UserProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        println("retrofit network")

        return Retrofit.Builder()
//            .baseUrl("http://www.my-api-rails.com/api/v1/")
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideUserApiClient(retrofit: Retrofit): UserApiClient {
        return retrofit.create(UserApiClient::class.java)
    }

    @Singleton
    @Provides
    fun provideChatApiClient(retrofit: Retrofit): ChatApiClient {
        return retrofit.create(ChatApiClient::class.java)
    }

    @Singleton
    @Provides
    fun provideMessageApiClient(retrofit: Retrofit): MessageApiClient {
        return retrofit.create(MessageApiClient::class.java)
    }

    @Singleton
    @Provides
    fun provideWebSocketListener(): WebSocketListener {
        return WebSocketListener()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(@ApplicationContext appContext: Context, userProvider: UserProvider):OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor(appContext, userProvider))
            .build()
    }
}