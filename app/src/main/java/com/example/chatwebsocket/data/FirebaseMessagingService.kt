package com.example.chatwebsocket.data

import com.example.chatwebsocket.data.model.dto.userdto.UserToken
import com.example.chatwebsocket.data.provider.UserProvider
import com.example.chatwebsocket.data.repository.UserRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MyFirebaseMessagingService() : FirebaseMessagingService() {
    @Inject lateinit var userRepository: UserRepository
    @Inject lateinit var userProvider: UserProvider

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.getNotification() != null) {
            val title: String = remoteMessage.getNotification()!!.getTitle().toString()
            val body: String = remoteMessage.getNotification()!!.getBody().toString()
        }
    }

    override fun onNewToken(token: String) {
        if(userProvider.userId != 0) {
            CoroutineScope(Dispatchers.IO).launch {
                userRepository.updateFirebaseToken(UserToken(token, userProvider.userId))
            }
        }
        println(token)
    }
}