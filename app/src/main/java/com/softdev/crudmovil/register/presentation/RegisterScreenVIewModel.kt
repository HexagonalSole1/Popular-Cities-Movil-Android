package com.softdev.crudmovil.register.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.softdev.crudmovil.login.data.network.AuthService
import com.softdev.crudmovil.login.infrastructure.AuthRepository
import com.softdev.crudmovil.register.infrastructure.RegisterRepository

class RegisterViewModel(private val repository: RegisterRepository) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow("")
    val successMessage = _successMessage.asStateFlow()

    fun register(username: String, email: String, password: String, confirmPassword: String) {
        if (password != confirmPassword) {
            _errorMessage.value = "Las contraseñas no coinciden"
            return
        }

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            _errorMessage.value = "Todos los campos son obligatorios"
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.register(username, email, password)
                if (response.success) {
                    _successMessage.value = "Registro exitoso"
                    _errorMessage.value = ""
                } else {
                    _errorMessage.value = response.message ?: "Error desconocido"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
