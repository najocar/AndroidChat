package com.example.chatwebsocket.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.chatwebsocket.R
import com.example.chatwebsocket.data.provider.UserProvider
import com.example.chatwebsocket.databinding.FragmentWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeFragment : Fragment() {
    @Inject lateinit var userProvider: UserProvider
    private lateinit var binding: FragmentWelcomeBinding
    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        if (userProvider.isLoggedIn()){
            findNavController().navigate(R.id.action_welcomeFragment_to_chatListFragment)
        }

        binding.loginBtn.setOnClickListener {
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToLoginAndRegisterFragment(LoginAndRegisterFragment.Type.LOGIN.ordinal)
            findNavController().navigate(action)
        }

        binding.registerBtn.setOnClickListener {
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToLoginAndRegisterFragment(LoginAndRegisterFragment.Type.REGISTER.ordinal)
            findNavController().navigate(action)
        }

        return binding.root
    }
}