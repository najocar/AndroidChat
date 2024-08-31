package com.example.chatwebsocket.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.chatwebsocket.data.model.entity.ChatModel
import com.example.chatwebsocket.data.model.entity.MessageModel
import com.example.chatwebsocket.data.provider.UserProvider
import com.example.chatwebsocket.databinding.ItemChatBinding
import com.example.chatwebsocket.databinding.ItemLoaderBinding
import com.example.chatwebsocket.databinding.ItemMessageBinding
import com.example.chatwebsocket.ui.viewModel.ListItemType
import com.example.chatwebsocket.ui.viewModel.LoaderItem
import com.example.chatwebsocket.ui.viewModel.MessageItem
import dagger.hilt.android.AndroidEntryPoint

class MessageListAdapter (
    var messagesList: MutableList<ListItemType>,
    private val userProvider: UserProvider
) : PagingDataAdapter<User, UserViewHolder>(diffCallback) {
    private val ITEM_TYPE_LOADER = 0
    private val ITEM_TYPE_MESSAGE = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_TYPE_LOADER -> {
                val binding = ItemLoaderBinding.inflate(inflater, parent, false)
                LoaderViewHolder(binding)
            }
            ITEM_TYPE_MESSAGE -> {
                val binding = ItemMessageBinding.inflate(inflater, parent, false)
                MessageViewHolder(binding, userProvider)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        when (val itemViewType = getItemViewType(position)) {
            ITEM_TYPE_LOADER -> (holder as LoaderViewHolder).bind()
            ITEM_TYPE_MESSAGE -> {
                val message = messagesList[position] as MessageItem
                (holder as MessageViewHolder).bind(message)
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            payloads.forEach { payload ->
                if (payload is ChatDiffUtil.ContentPayback) {
                    val message = messagesList[position] as MessageItem
                    (holder as MessageViewHolder).bindContent(message)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (messagesList[position]) {
            is LoaderItem -> ITEM_TYPE_LOADER
            is MessageItem -> ITEM_TYPE_MESSAGE
        }
    }

    fun updateList(newList: List<ListItemType>) {
        val diffUtil = ChatDiffUtil(messagesList, newList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        messagesList = newList.toMutableList()
        diffResults.dispatchUpdatesTo(this)
    }
}