package com.example.chatwebsocket.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.chatwebsocket.data.model.entity.MessageModel
import com.example.chatwebsocket.ui.viewModel.ListItemType
import com.example.chatwebsocket.ui.viewModel.LoaderItem
import com.example.chatwebsocket.ui.viewModel.MessageItem

class ChatDiffUtil(
    private val oldList: List<ListItemType>,
    private val newList: List<ListItemType>
): DiffUtil.Callback() {

    object ContentPayback
    override fun getOldListSize(): Int { return oldList.size }

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition] is LoaderItem && newList[newItemPosition] is LoaderItem -> true
            oldList[oldItemPosition] is MessageItem && newList[newItemPosition] is MessageItem -> {
                (oldList[oldItemPosition] as MessageItem).message.id == (newList[newItemPosition] as MessageItem).message.id
            }
            else -> false
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition] is LoaderItem && newList[newItemPosition] is LoaderItem -> true
            oldList[oldItemPosition] is MessageItem && newList[newItemPosition] is MessageItem -> {
                (oldList[oldItemPosition] as MessageItem).message == (newList[newItemPosition] as MessageItem).message
            }
            else -> false
        }
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        if((oldList[oldItemPosition] as MessageItem).message.content != (newList[newItemPosition] as MessageItem).message.content){
            return ContentPayback
        } else {
            return super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }
}