package com.example.chatwebsocket.data.repository

import com.example.chatwebsocket.data.model.dao.ChatDAO
import com.example.chatwebsocket.data.model.dto.chatdto.MessageSendDTO
import com.example.chatwebsocket.data.model.dto.chatdto.MessagesFrom
import com.example.chatwebsocket.data.model.dto.local.MessageLocalDTO
import com.example.chatwebsocket.data.model.entity.MessageModel
import com.example.chatwebsocket.data.network.MessageApiClient
import com.example.chatwebsocket.data.provider.UserProvider
import com.example.chatwebsocket.utils.exceptions.ApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.util.UUID
import javax.inject.Inject

class MessageRepository @Inject constructor(private val api: MessageApiClient) {
    @Inject lateinit var chatDAO : ChatDAO
    @Inject lateinit var userProvider: UserProvider

    suspend fun getMessagesFromChat(from: MessagesFrom): List<MessageModel> {
        return withContext(Dispatchers.IO) {
            var response: List<MessageModel> = emptyList()
            try {
                var page = from.page ?: 1
                response = api.getMessagesFromChat(page, from.from, from.chat)
                response.map { chatDAO.upsertMessage(it.toMessageLocalDTO())}
            } catch (e: HttpException) {
                e.printStackTrace()
                throw ApiException()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            response
        }
    }

    suspend fun takeDB(id: Long) : List<MessageModel> {
//        val response = chatDAO.getByIdWithMessages(id).messages.map { it.toMessageModel() }.toMutableList()
        val response = chatDAO.getMessagesByChatOrdered(id).map { it.toMessageModel() }.toMutableList()
        println("response de takeDB: ${response.size}")
        return response
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

    suspend fun deleteMessage(message: MessageModel) {
        withContext(Dispatchers.IO) {
            api.deleteMessage(message.id)
            chatDAO.deleteMessage(message.toMessageLocalDTO())
        }
    }
}