package com.example.chatwebsocket.ui

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.chatwebsocket.R
import com.example.chatwebsocket.data.model.entity.MessageModel
import com.example.chatwebsocket.data.provider.UserProvider
import com.example.chatwebsocket.databinding.ItemLoaderBinding
import com.example.chatwebsocket.databinding.ItemMessageBinding
import com.example.chatwebsocket.ui.viewModel.MessageItem
import javax.inject.Inject


abstract class MyViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {}
class MessageViewHolder (private val binding: ItemMessageBinding, private val userProvider: UserProvider) : MyViewHolder(binding) {

    fun bind(
        item: MessageItem
    ) {
        val message = item.message
        binding.textET.text = message.content

        if (message.userId == userProvider.userId) {
            binding.autorET.text = "Yo"
            binding.materialToolbar.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.myChat))
            val layoutParams = binding.materialToolbar.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.horizontalBias = 0.9f
            binding.materialToolbar.layoutParams = layoutParams
        } else {
            binding.autorET.text = message.userName
            binding.materialToolbar.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.greenSecondary))
            val layoutParams = binding.materialToolbar.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.horizontalBias = 0.1f
        }
    }

    fun bindContent(
        item: MessageItem
    ) {
        val message = item.message
        binding.textET.text = message.content
    }
}

class LoaderViewHolder(val binding: ItemLoaderBinding) : MyViewHolder(binding) {
    fun bind() {
        binding.progressBar.isIndeterminate = true
    }
}