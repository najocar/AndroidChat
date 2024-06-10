package com.example.chatwebsocket.ui

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.chatwebsocket.R
import com.example.chatwebsocket.data.model.entity.MessageModel
import com.example.chatwebsocket.data.provider.UserProvider
import com.example.chatwebsocket.databinding.ItemMessageBinding
import javax.inject.Inject

class MessageListViewHolder (private val binding: ItemMessageBinding, private val userProvider: UserProvider) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        message: MessageModel
    ) {
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
}