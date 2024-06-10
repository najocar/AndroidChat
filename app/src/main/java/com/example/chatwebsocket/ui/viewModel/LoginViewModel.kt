package com.example.chatwebsocket.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatwebsocket.data.model.dto.userdto.UserLogin
import com.example.chatwebsocket.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    private var _logginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Initial)
    var logginState: StateFlow<LoginState> = _logginState.asStateFlow()

    fun login(context: Context, user: UserLogin) {
        viewModelScope.launch {
            val response = userRepository.login(context, user)
            _logginState.emit(LoginState.Loading)
            _logginState.emit(
                if (response != null){
                    LoginState.Success
                } else {
                    LoginState.Error
                }
            )
        }
    }

    suspend fun register(context: Context, user: UserLogin) {
        viewModelScope.launch {
            val response = userRepository.register(context, user)
            _logginState.emit(LoginState.Loading)
            _logginState.emit(
                if (response != null){
                    LoginState.Success
                } else {
                    LoginState.Error
                }
            )
        }
    }

    sealed class LoginState {
        object Initial : LoginState()
        object Loading : LoginState()
        object Success : LoginState()
        object Error : LoginState()
    }
}