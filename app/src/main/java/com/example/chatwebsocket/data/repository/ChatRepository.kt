package com.example.chatwebsocket.data.repository

import com.example.chatwebsocket.data.model.dao.ChatDAO
import com.example.chatwebsocket.data.model.entity.ChatModel
import com.example.chatwebsocket.data.network.ChatApiClient
import com.example.chatwebsocket.utils.exceptions.ApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class ChatRepository @Inject constructor(private val api: ChatApiClient) {
    @Inject lateinit var chatDAO : ChatDAO

    /**
     * Obtiene una lista de todos los chats desde una fuente remota y los inserta o actualiza en la base de datos local.
     *
     * @return Una lista de objetos [ChatModel] que representan todos los chats obtenidos de la fuente remota.
     * @throws HttpException Si ocurre un error HTTP durante la obtención de los chats.
     * @throws Exception Si ocurre cualquier otro tipo de excepción durante la obtención o procesamiento de los chats.
     */
    suspend fun getAllChats(): List<ChatModel> {
        return withContext(Dispatchers.IO){
            var response: List<ChatModel> = emptyList()
            try {
                response = api.getAllChats()
                if (response.isNotEmpty()) {
                    response.map { chatDAO.upsert(it.toChatLocalDTO()) }
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

    suspend fun takeDB(): List<ChatModel> {
        val response = chatDAO.getAll().map { it.toChatModel() }
        return response
    }
}