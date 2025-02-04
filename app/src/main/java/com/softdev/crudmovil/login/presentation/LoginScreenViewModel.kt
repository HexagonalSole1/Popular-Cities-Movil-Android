package com.softdev.crudmovil.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softdev.crudmovil.login.infrastructure.AuthRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _loginError = MutableStateFlow("")
    val loginError = _loginError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun login(email: String, password: String, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val response = repository.login(email, password)
            _isLoading.value = false

            if (response.success) {
                _loginError.value = "si entre"
                response.accessToken?.let { onSuccess(it) }
            } else {
                _loginError.value = response.message ?: "Error desconocido"
            }
        }
    }
}
