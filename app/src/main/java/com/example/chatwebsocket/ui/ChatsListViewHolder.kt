package com.example.chatwebsocket.ui

import androidx.recyclerview.widget.RecyclerView
import com.example.chatwebsocket.data.model.entity.ChatModel
import com.example.chatwebsocket.databinding.ItemChatBinding

class ChatsListViewHolder (private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        chat: ChatModel,
        setChatClick: (ChatModel) -> Unit
    ) {
        binding.chatNameET.text = chat.name
        binding.itemChat.setOnClickListener {
            setChatClick.invoke(chat)
        }
    }
}