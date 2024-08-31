package com.example.chatwebsocket.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.chatwebsocket.data.model.dao.ChatDAO
import com.example.chatwebsocket.data.model.dto.chatdto.MessageSendDTO
import com.example.chatwebsocket.data.model.dto.chatdto.MessagesFrom
import com.example.chatwebsocket.data.model.entity.MessageModel
import com.example.chatwebsocket.data.network.websocket.WebSocketListener
import com.example.chatwebsocket.data.repository.MessageRepository
import com.example.chatwebsocket.ui.MessagesPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

const val CHAT_ID = "chatId"
const val CHAT_NAME = "chatName"

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val webSocketListener: WebSocketListener,
    private val chatDAO : ChatDAO,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val chatId = savedStateHandle.get<Long>(CHAT_ID) ?: 0L
    val chatName = savedStateHandle.get<String>(CHAT_NAME)
    private val _messagesList: MutableStateFlow<MutableList<ListItemType>> = MutableStateFlow(
        mutableListOf()
    )
    val messagesList: StateFlow<MutableList<ListItemType>> = _messagesList.asStateFlow()
    var goDown = true
    var page = 1
    private var created = 0



    init {
        val flow = Pager(
            PagingConfig(pageSize = 20)
        ) {
            MessagesPaging(chatDAO, chatId.toInt())
        }.flow
            .cachedIn(viewModelScope)

        viewModelScope.launch {
            flow.collect() {
                println(it)
                it.map { println(it) }
            }
//            messageRepository.takeDB(chatId).collect() {
//                println(it)
//                it.forEach {
//                    if (!_messagesList.value.contains(MessageItem(it))) {
//                        goDown = true
//                    }
//                }
//                _messagesList.value = it.toMutableList().map { MessageItem(it) }.toMutableList()
//                goDown = false
//            }
        }
        viewModelScope.launch {
            val messagesFrom = MessagesFrom(created, chatId, page)
            try {
                messageRepository.getMessagesFromChat(messagesFrom)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
//        subscribeToMessages()
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            try {
                val result = messageRepository.sendMessage(MessageSendDTO(message), chatId)
                if(result != null){
                    val actualMessages = _messagesList.value.toMutableList()
                    actualMessages.add(MessageItem(result))
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
            goDown = true
            try {
                messagesFrom.page = 1
//                actualMessages.addAll(messageRepository.getMessagesFromChat(messagesFrom).toMutableList().map { MessageItem(it) })
            } catch (e : Exception) {
                e.printStackTrace()
            }
            _messagesList.emit(actualMessages)
            goDown = false
        }
    }

    fun subscribeToMessages() {
        viewModelScope.launch {
            webSocketListener.sharedFlow.collect { value ->
                println("entra en collect")
                val messagesList = _messagesList.value.filterIsInstance<MessageItem>()
                if (messagesList.isEmpty() || messagesList.last().toMessageModel().createdAt < value.message.date){
                    newMessage(MessagesFrom(value.message.date - 1, chatId))
                } else {
                    val oldMessage = _messagesList.value.find { it is MessageItem && it.toMessageModel().id == value.message.messageId }
                    if (oldMessage != null) {
                        if (value.message.type == "delete"){
                            _messagesList.emit((_messagesList.value - oldMessage).toMutableList())
//                            messageRepository.deleteMessageA(oldMessage.toMessageModel())
                        } else {
                            try {
                                val messageToUpdated = messageRepository.getMessage(value.message.messageId)
                                if (messageToUpdated != null) {
                                    updateMessage(oldMessage as MessageItem, MessageItem(messageToUpdated))
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        }
    }

    fun deleteMessage(message: MessageItem) {
        viewModelScope.launch {
            messageRepository.deleteMessage(message.toMessageModel())
            _messagesList.emit((_messagesList.value - message).toMutableList())
        }
    }

    fun updateMessage(message: MessageItem, messageUpdated: MessageItem) {
        viewModelScope.launch {
            val currentMessages = _messagesList.value.toMutableList()
            val index = currentMessages.indexOf(message)
            if (index != -1) {
                currentMessages[index] = messageUpdated
                _messagesList.value = currentMessages
            }
            _messagesList.emit(currentMessages)
            messageRepository.updateMessage(messageUpdated.toMessageModel())
        }
    }

    fun chargePage(){
        page = 2
        val messagesFrom = MessagesFrom(created, chatId, page)
        viewModelScope.launch {
            toggleLoader()
            delay(1000)
            val actualMessages = _messagesList.value.toMutableList()
            created = actualMessages.filterIsInstance<MessageItem>().first().toMessageModel().createdAt
            messagesFrom.from = created
            try {
                messageRepository.getMessagesFromChat(messagesFrom)
//                actualMessages.addAll(0, messageRepository.getMessagesFromChat(messagesFrom).map { MessageItem(it) })
            } catch (e : Exception) {
                e.printStackTrace()
            }
            _messagesList.emit(actualMessages)
            toggleLoader()
        }
    }

    suspend fun toggleLoader() {
        if (isLoader()) {
            _messagesList.emit((_messagesList.value - LoaderItem(true)).toMutableList())
        } else {
            val actualMessages = _messagesList.value.toMutableList()
            actualMessages.add(0, LoaderItem(true))
            _messagesList.emit(actualMessages)
        }
    }

    fun isLoader(): Boolean {
        return _messagesList.value.contains(LoaderItem(true))
    }
}

sealed class ListItemType
data class MessageItem(val message: MessageModel) : ListItemType() {
    fun toMessageModel(): MessageModel = message

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MessageItem) return false
        if (message.id == other.message.id && message.content == other.message.content) return true
        return false
    }
}
data class LoaderItem(val isLoading: Boolean) : ListItemType()