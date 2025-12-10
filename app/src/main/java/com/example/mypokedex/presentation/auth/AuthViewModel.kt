package com.example.mypokedex.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypokedex.domain.usecase.IsLoggedInUseCase
import com.example.mypokedex.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError = _loginError.asStateFlow()

    init {
        viewModelScope.launch {
            isLoggedInUseCase().collect {
                _isLoggedIn.value = it
            }
        }
    }

    fun login(username: String, password: String) {
        if (username == "admin" && password == "123") {
            viewModelScope.launch {
                loginUseCase(username)
                _loginError.value = null
            }
        } else {
            _loginError.value = "Invalid credentials. Please try again."
        }
    }
}
