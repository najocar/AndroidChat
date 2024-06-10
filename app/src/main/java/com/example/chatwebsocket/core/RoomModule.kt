package com.example.chatwebsocket.core

import android.content.Context
import androidx.room.Room
import com.example.chatwebsocket.data.model.dao.ChatDAO
import com.example.chatwebsocket.data.model.dao.MessageDAO
import com.example.chatwebsocket.data.model.dao.UserDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
                context,
                AppDatabase::class.java, "database-chatwss"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideChatDAO(appDatabase: AppDatabase): ChatDAO {
        return appDatabase.chatDAO()
    }

    @Singleton
    @Provides
    fun provideUserDAO(appDatabase: AppDatabase): UserDAO {
        return appDatabase.userDAO()
    }

    @Singleton
    @Provides
    fun provideMessageDAO(appDatabase: AppDatabase): MessageDAO {
        return appDatabase.messageDAO()
    }
}