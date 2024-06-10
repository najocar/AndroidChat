package com.example.chatwebsocket.ui.view

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatwebsocket.R
import com.example.chatwebsocket.data.WssService
import com.example.chatwebsocket.data.model.dao.ChatDAO
import com.example.chatwebsocket.data.model.dto.userdto.UserToken
import com.example.chatwebsocket.data.model.entity.ChatModel
import com.example.chatwebsocket.data.provider.UserProvider
import com.example.chatwebsocket.data.repository.UserRepository
import com.example.chatwebsocket.databinding.FragmentChatListBinding
import com.example.chatwebsocket.ui.ChatsListAdapter
import com.example.chatwebsocket.ui.viewModel.ChatsListViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatListFragment : Fragment() {
    @Inject lateinit var chatDAO: ChatDAO
    @Inject lateinit var userProvider: UserProvider
    private val chatVM: ChatsListViewModel by viewModels()
    private lateinit var adapter: ChatsListAdapter
    private lateinit var binding: FragmentChatListBinding
    private lateinit var linearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!chatVM.isServiceRunning) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(
                        ContentValues.TAG,
                        "Fetching FCM registration token failed",
                        task.exception
                    )
                    return@OnCompleteListener
                }

                val token = task.result

                lifecycleScope.launch {
                    userRepository.updateFirebaseToken(UserToken(token, userProvider.userId))
                }

                userProvider.deviceToken = token
                println(token)

                val intent = Intent(requireContext(), WssService::class.java)
                requireContext().startService(intent)
                chatVM.isServiceRunning = true

            })
        }
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatListBinding.inflate(inflater, container, false)
        println(savedInstanceState?.getInt( "chatId"))


        linearLayoutManager = LinearLayoutManager(this.context)

        adapter = ChatsListAdapter(
            chatVM.chatList.value,
            setChatClick = { ChatModel -> onSetChatClick(ChatModel) }
        )

        binding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = this@ChatListFragment.adapter
            addItemDecoration(DividerItemDecoration(this.context, linearLayoutManager.orientation))
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                chatVM.chatList.collect { chats ->
                    if (chats.isNotEmpty()) {
                        adapter.chatsList = chats
                        adapter.notifyItemRangeChanged( 0, chats.size - 1)
                        binding.recyclerView.scrollToPosition(chats.size - 1)
                    }
                }
            }
        }

        binding.singOutBtn.setOnClickListener {
            lifecycleScope.launch {
                chatDAO.deleteAll()
            }
            userProvider.logout(requireContext())
            findNavController().navigate(R.id.action_chatListFragment_to_welcomeFragment)
        }
        return binding.root
    }

    private fun onSetChatClick(chat: ChatModel) {
        val action = ChatListFragmentDirections.actionChatListFragmentToChatFragment(chat.id, chat.name)
        findNavController().navigate(action)
    }
}