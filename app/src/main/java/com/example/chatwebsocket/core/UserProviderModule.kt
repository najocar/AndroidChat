package com.example.chatwebsocket.core

import com.example.chatwebsocket.data.model.dao.UserDAO
import com.example.chatwebsocket.data.provider.UserProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserProviderModule {
    @Singleton
    @Provides
    fun provideUserProvider(userDAO: UserDAO): UserProvider {
    return UserProvider(userDAO)
    }
}