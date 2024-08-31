package com.example.chatwebsocket.data.repository

import com.example.chatwebsocket.data.model.dao.ChatDAO
import com.example.chatwebsocket.data.model.dao.MessageDAO
import com.example.chatwebsocket.data.model.dto.chatdto.MessageSendDTO
import com.example.chatwebsocket.data.model.dto.chatdto.MessagesFrom
import com.example.chatwebsocket.data.model.dto.local.MessageLocalDTO
import com.example.chatwebsocket.data.model.entity.MessageModel
import com.example.chatwebsocket.data.network.MessageApiClient
import com.example.chatwebsocket.data.provider.UserProvider
import com.example.chatwebsocket.utils.exceptions.ApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessageRepository @Inject constructor(private val api: MessageApiClient) {
    @Inject lateinit var chatDAO : ChatDAO
    @Inject lateinit var messageDAO : MessageDAO
    @Inject lateinit var userProvider: UserProvider

    suspend fun getMessagesFromChat(from: MessagesFrom){
        withContext(Dispatchers.IO) {
            var response: List<MessageModel> = emptyList()
            try {
                println("pide mensajes")
                var page = from.page ?: 1
                response = api.getMessagesFromChat(page, from.from, from.chat)
                response.map { chatDAO.upsertMessage(it.toMessageLocalDTO())
//                    response.map { chatDAO.upsertMessage(it.toMessageLocalDTO()).collect(){
//                    println(it)
//                }

                }
            } catch (e: HttpException) {
                e.printStackTrace()
                throw ApiException()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            response
        }
    }

    suspend fun getMessage(id: String): MessageModel? {
        return withContext(Dispatchers.IO) {
            var response: MessageModel? = null
            try {
                response = api.getMessage(id)
                chatDAO.upsertMessage(response.toMessageLocalDTO())
            } catch (e: HttpException) {
                e.printStackTrace()
                throw ApiException()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            response
        }
    }

//    suspend fun takeDB(id: Long) : List<MessageModel> {
//        val response = chatDAO.getMessagesByChatOrdered(id).map { it.toMessageModel() }.toMutableList()
//        println("response de takeDB: ${response.size}")
//        return response
//    }

    suspend fun takeDB(id: Long): Flow<List<MessageModel>> {
        return chatDAO.getMessagesByChatOrdered(id)
    }
    suspend fun sendMessage(message: MessageSendDTO, chat: Long): MessageModel? {
        return withContext(Dispatchers.IO) {
            var response: MessageModel? = null
            try {
                val generateId = UUID.randomUUID()
                val messageLocal = MessageLocalDTO(generateId.toString(), message.content, userProvider.userId, "user", chat, 0, 0)
                chatDAO.upsertMessage(messageLocal)
                println(chatDAO.getMessagesByChatOrdered(chat))
                message.messageId = generateId.toString()
                response = api.sendMessage(message, chat)
            } catch (e: HttpException) {
                e.printStackTrace()
                throw ApiException()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            response
        }
    }

    suspend fun updateMessage(message: MessageModel) {
        withContext(Dispatchers.IO) {
            try {
                val messageSend = MessageSendDTO(message.content, message.id)
                api.updateMessage(messageSend, messageSend.messageId)
                messageDAO.upsertMessage(message.toMessageLocalDTO())
//                chatDAO.upsertMessage(message.toMessageLocalDTO())
            } catch (e: Exception) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    suspend fun deleteMessage(message: MessageModel) {
        withContext(Dispatchers.IO) {
            api.deleteMessage(message.id)
            chatDAO.deleteMessage(message.toMessageLocalDTO())
        }
    }

    suspend fun deleteMessageA(message: MessageModel) {
        withContext(Dispatchers.IO) {
            chatDAO.deleteMessage(message.toMessageLocalDTO())
        }
    }
}