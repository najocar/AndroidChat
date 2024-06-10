package com.example.chatwebsocket.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.chatwebsocket.data.model.entity.MessageModel

class ChatDiffUtil(
    private val oldList: List<MessageModel>,
    private val newList: List<MessageModel>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int { return oldList.size }

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}