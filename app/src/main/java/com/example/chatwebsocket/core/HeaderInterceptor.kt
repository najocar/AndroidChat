package com.example.chatwebsocket.core

import android.content.Context
import com.example.chatwebsocket.data.provider.UserProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userProvider: UserProvider
) : Interceptor {
//    @Inject lateinit var userProvider: UserProvider

    override fun intercept(chain: Interceptor.Chain): Response {
        if (userProvider.getToken(context).isEmpty()) return chain.proceed(chain.request())
        val request = chain.request().newBuilder()
            .addHeader("token", userProvider.getToken(context))
            .build()
        return chain.proceed(request)
    }
}