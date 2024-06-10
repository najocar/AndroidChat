package com.example.chatwebsocket.ui.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatwebsocket.data.provider.UserProvider
import com.example.chatwebsocket.databinding.FragmentChatBinding
import com.example.chatwebsocket.ui.MessageListAdapter
import com.example.chatwebsocket.ui.viewModel.ChatViewModel
import com.example.chatwebsocket.utils.RecyclerItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment() {
    @Inject lateinit var userProvider: UserProvider
    private lateinit var binding: FragmentChatBinding
    private val chatVM: ChatViewModel by viewModels()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: MessageListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        linearLayoutManager = LinearLayoutManager(this.context)
        setObserver()

        setChatContent()
        binding.returnBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.sendBtn.setOnClickListener {
            chatVM.sendMessage(binding.contentET.text.toString())
            binding.contentET.text.clear()
            binding.recyclerView.scrollToPosition(chatVM.messagesList.value.size - 1)
        }

        binding.recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(requireContext(), binding.recyclerView,
            object : RecyclerItemClickListener.OnItemClickListener {
                override fun onItemClick(view: View?, position: Int) { }

                override fun onLongItemClick(view: View?, position: Int) {
                    println("long click")
                    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                    builder
                        .setTitle("¿Eliminar el mensaje?")
                        .setPositiveButton("Aceptar") { dialog, which ->
                            chatVM.deleteMessage(chatVM.messagesList.value[position])
                        }
                        .setNegativeButton("Candelar") { dialog, which ->
                            // Do something else.
                        }

                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
            })
        )

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
                println("lastVisibleItemPosition: $lastVisibleItemPosition")
                if (lastVisibleItemPosition == 0 && !chatVM.isLoading) {
                    loadMoreItems()
                }
            }
        })

        return binding.root
    }


    private fun setChatContent() {
        binding.nameTV.text = chatVM.chatName
    }

    private fun setObserver() {
        adapter = MessageListAdapter(
            chatVM.messagesList.value,
            userProvider
        )

        binding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = this@ChatFragment.adapter
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                chatVM.messagesList.collect { messages ->
                    if (messages.isNotEmpty()) {
                        adapter.updateList(messages)
                        if (chatVM.goDown) {
                            binding.recyclerView.scrollToPosition(chatVM.messagesList.value.size - 1)
                        }
                    }
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun loadMoreItems() {
        chatVM.chargePage()
    }
}