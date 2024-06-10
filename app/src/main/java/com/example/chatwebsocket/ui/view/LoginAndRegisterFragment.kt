package com.example.chatwebsocket.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.chatwebsocket.R
import com.example.chatwebsocket.data.model.dto.userdto.UserLogin
import com.example.chatwebsocket.databinding.FragmentLoginAndRegisterBinding
import com.example.chatwebsocket.ui.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginAndRegisterFragment : Fragment() {
    private lateinit var binding: FragmentLoginAndRegisterBinding
    private val loginVM: LoginViewModel by viewModels()
    private lateinit var type: Type
    enum class Type {
        LOGIN,
        REGISTER
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type = arguments?.getInt("type")
        val enumType = Type.entries.find { it.ordinal == type }
        if(enumType == null){
            throw Exception("Error, tipo inválido")
        } else {
            this.type = enumType
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginAndRegisterBinding.inflate(inflater, container, false)
        listenChanges()
        initForm()
        return binding.root
    }

    private fun listenChanges() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginVM.logginState.collect { state ->
                    binding.progressBar.visibility = View.GONE
                    when (state) {
                        LoginViewModel.LoginState.Initial -> binding.progressBar.visibility = View.GONE
                        LoginViewModel.LoginState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            delay(1000)
                        }
                        LoginViewModel.LoginState.Success -> navigateToChatListFragment()
                        LoginViewModel.LoginState.Error -> showErrorToast()
                    }
                }
            }
        }
    }

    private fun initForm() {
        val buttonText = if (type == Type.LOGIN) getString(R.string.login) else getString(R.string.register)
        binding.loginBtn.text = buttonText
        binding.loginBtn.setOnClickListener {
            val name = binding.nameET.text.toString()
            val password = binding.passwordET.text.toString()

            if (name.isNotEmpty() && password.isNotEmpty()) {
                when (type) {
                    Type.LOGIN -> loginVM.login(requireContext(), UserLogin(username = name, password = password))
                    Type.REGISTER -> {
                        lifecycleScope.launch {
                            repeatOnLifecycle(Lifecycle.State.STARTED) {
                                loginVM.register(requireContext(), UserLogin(username = name, password = password))
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), R.string.error_fill_all_fields, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToChatListFragment() {
        findNavController().navigate(R.id.action_loginAndRegisterFragment_to_chatListFragment)
    }

    private fun showErrorToast() {
        val errorMessage = if (type == Type.LOGIN) R.string.error_user_not_exist else R.string.error_user_exist
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }
}