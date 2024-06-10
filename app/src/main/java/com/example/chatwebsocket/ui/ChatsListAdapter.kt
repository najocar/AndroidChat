package com.example.chatwebsocket.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatwebsocket.data.model.entity.ChatModel
import com.example.chatwebsocket.databinding.ItemChatBinding

class ChatsListAdapter (
    var chatsList: MutableList<ChatModel>,
    private val setChatClick: (ChatModel) -> Unit
) : RecyclerView.Adapter<ChatsListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChatBinding.inflate(inflater, parent, false)
        return ChatsListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return chatsList.size
    }

    override fun onBindViewHolder(holder: ChatsListViewHolder, position: Int) {
        holder.bind(chatsList[position], setChatClick)
    }
}