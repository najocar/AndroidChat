package com.example.chatwebsocket.data.provider

import android.content.Context
import com.example.chatwebsocket.data.model.dao.UserDAO
import com.example.chatwebsocket.data.model.dto.local.UserLocalDTO
import com.example.chatwebsocket.data.model.dto.userdto.UserToken
import com.example.chatwebsocket.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class UserProvider @Inject constructor(private val userDAO: UserDAO) {

    var userId = 0
    var deviceToken = ""
    private var userToken = ""

    fun getToken(context: Context): String {
        if (userToken != "") return userToken
        return userToken
    }

    fun login(context: Context, userToken: UserLocalDTO){
        CoroutineScope(Dispatchers.IO).launch {
            userDAO.upsert(userToken)
        }
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", userToken.token)
        editor.putInt("user_id", userToken.id.toInt())
        editor.apply()
        this.userToken = userToken.token
        userId = userToken.id.toInt()
    }

    suspend fun logout(context: Context){
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        val user = userDAO.selectFirst()
        if (user != null) userDAO.delete(user)
        userToken = ""
        userId = 0
    }

    fun isLoggedIn(): Boolean {
        return userToken != ""
    }

    suspend fun initialize() {
        try {
            fetchToken()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun fetchToken() {
        userToken = userDAO.getUser().token
        userId = userDAO.getUser().id.toInt()
    }
}