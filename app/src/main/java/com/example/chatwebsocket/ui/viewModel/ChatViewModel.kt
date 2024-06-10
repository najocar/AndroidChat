package com.example.chatwebsocket.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatwebsocket.data.model.dto.chatdto.MessageSendDTO
import com.example.chatwebsocket.data.model.dto.chatdto.MessagesFrom
import com.example.chatwebsocket.data.model.entity.MessageModel
import com.example.chatwebsocket.data.network.websocket.WebSocketListener
import com.example.chatwebsocket.data.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val CHAT_ID = "chatId"
const val CHAT_NAME = "chatName"

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val webSocketListener: WebSocketListener,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val chatId = savedStateHandle.get<Long>(CHAT_ID) ?: 0L
    val chatName = savedStateHandle.get<String>(CHAT_NAME)
    private val _messagesList: MutableStateFlow<MutableList<MessageModel>> = MutableStateFlow(
        mutableListOf()
    )
    val messagesList: StateFlow<MutableList<MessageModel>> = _messagesList.asStateFlow()
    var isLoading = false
    var goDown = true

    var page = 1

    init {
        val messagesFrom = MessagesFrom(1713781937, chatId, page)
        viewModelScope.launch {
            _messagesList.emit(messageRepository.takeDB(chatId).toMutableList())
            goDown = false
            try {
                val repositoryMessages = messageRepository.getMessagesFromChat(messagesFrom).toMutableList()
                _messagesList.value = repositoryMessages
//                chargePage()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        subscribeToMessages()
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            try {
                val result = messageRepository.sendMessage(MessageSendDTO(message), chatId)
                if(result != null){
                    val actualMessages = _messagesList.value.toMutableList()
                    actualMessages.add(result)
                    goDown = true
                    _messagesList.emit(actualMessages)
                    goDown = false
                }
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }

    fun newMessage(messagesFrom: MessagesFrom) {
        viewModelScope.launch {
            val actualMessages = _messagesList.value.toMutableList()
            actualMessages.addAll(messageRepository.getMessagesFromChat(messagesFrom).toMutableList())
            _messagesList.emit(actualMessages)
        }
    }

    fun subscribeToMessages() {
        viewModelScope.launch {
            webSocketListener.sharedFlow.collect { value ->
                newMessage(MessagesFrom(value.message.date - 1, chatId))
            }
        }
    }

    fun deleteMessage(message: MessageModel) {
        viewModelScope.launch {
            messageRepository.deleteMessage(message)
            _messagesList.emit((_messagesList.value - message).toMutableList())
//            newMessage(MessagesFrom(message.createdAt - 1, chatId))
        }
    }

    fun chargePage(){
        isLoading = true
        page++
        val messagesFrom = MessagesFrom(1713781937, chatId, page)
        viewModelScope.launch {
            val actualMessages = _messagesList.value.toMutableList()
            actualMessages.addAll(0, messageRepository.getMessagesFromChat(messagesFrom).toMutableList())
            _messagesList.emit(actualMessages)
            isLoading = false
        }
    }
}

sealed class ListItemType
data class MessageItem(val message: MessageModel) : ListItemType()
data class LoaderItem(val isLoading: Boolean) : ListItemType()