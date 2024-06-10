package com.example.chatwebsocket.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.chatwebsocket.data.model.entity.ChatModel
import com.example.chatwebsocket.data.model.entity.MessageModel
import com.example.chatwebsocket.data.provider.UserProvider
import com.example.chatwebsocket.databinding.ItemChatBinding
import com.example.chatwebsocket.databinding.ItemMessageBinding
import dagger.hilt.android.AndroidEntryPoint

class MessageListAdapter (
    var messagesList: MutableList<MessageModel>,
    private val userProvider: UserProvider
) : RecyclerView.Adapter<MessageListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMessageBinding.inflate(inflater, parent, false)
        return MessageListViewHolder(binding, userProvider)
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    override fun onBindViewHolder(holder: MessageListViewHolder, position: Int) {
        holder.bind(messagesList[position])
    }

    fun updateList(newList: List<MessageModel>) {
        println(messagesList.size)
        println(newList.size)
        val diffUtil = ChatDiffUtil(messagesList, newList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        messagesList = newList.toMutableList()
        diffResults.dispatchUpdatesTo(this)
    }
}