package com.example.chatwebsocket.data.repository

import android.content.Context
import com.example.chatwebsocket.data.model.dto.local.UserLocalDTO
import com.example.chatwebsocket.data.model.dto.userdto.UserLogin
import com.example.chatwebsocket.data.model.dto.userdto.UserToken
import com.example.chatwebsocket.data.model.entity.UserModel
import com.example.chatwebsocket.data.network.UserApiClient
import com.example.chatwebsocket.data.provider.UserProvider
import com.example.chatwebsocket.utils.exceptions.ApiException
import com.example.chatwebsocket.utils.exceptions.ErrorLoginException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val api: UserApiClient) {
    @Inject lateinit var userProvider: UserProvider

    suspend fun login(context: Context, user: UserLogin): String? {
        return withContext(Dispatchers.IO) {
            var response: UserLocalDTO = UserLocalDTO( 0, "", "")
            try {
                val apiRespose = api.login(user)
                response.token = apiRespose.token
                response.id = apiRespose.userId.toLong()
                if (response.token != "") {
                    userProvider.login(context, response)
                } else {
                    throw ErrorLoginException()
                }
            } catch (e: HttpException) {
                e.printStackTrace()
                throw ApiException()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            response.token
        }
    }

    suspend fun register(context: Context, user: UserLogin): String? {
        return withContext(Dispatchers.IO) {
            var response: UserToken? = null
            try {
                response = api.register(user)
                if (response.token != "") {
//                    userProvider.login(context, response)
                } else {
                    throw ErrorLoginException()
                }
            } catch (e: HttpException) {
                e.printStackTrace()
                throw ApiException()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            response?.token
        }
    }

    suspend fun updateFirebaseToken(user: UserToken): UserModel? {
        return withContext(Dispatchers.IO) {
            var response: UserModel? = null
            try {
                response = api.updateFirebaseToken(user).body()
            } catch (e: HttpException) {
                e.printStackTrace()
                throw ApiException()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            response
        }
    }
}