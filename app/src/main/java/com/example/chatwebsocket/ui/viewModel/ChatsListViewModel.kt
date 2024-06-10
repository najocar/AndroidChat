package com.example.chatwebsocket.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatwebsocket.data.model.entity.ChatModel
import com.example.chatwebsocket.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsListViewModel @Inject constructor(private val chatRepository: ChatRepository) : ViewModel() {
    private val _chatList: MutableStateFlow<MutableList<ChatModel>> = MutableStateFlow(
        mutableListOf()
    )
    val chatList: StateFlow<MutableList<ChatModel>> = _chatList.asStateFlow()
    var isServiceRunning: Boolean = false

    init {
        viewModelScope.launch {
            _chatList.emit(chatRepository.takeDB().toMutableList())
            try {
                val result = chatRepository.getAllChats()
                _chatList.emit(result.toMutableList())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
