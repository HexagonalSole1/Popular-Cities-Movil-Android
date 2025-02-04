package com.softdev.crudmovil.login.infrastructure

import AuthResponse
import com.softdev.crudmovil.login.data.network.AuthService
import com.softdev.crudmovil.login.domain.Dtos.request.LoginRequest

class AuthRepository(private val authService: AuthService) {
    suspend fun login(username: String, password: String): AuthResponse {
        return try {
            val response = authService.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                response.body() ?: AuthResponse(success = false, accessToken = null, message = "Respuesta vacía")
            } else {
                AuthResponse(success = false,accessToken = "null", message = "Credenciales incorrectas")
            }
        } catch (e: Exception) {
            AuthResponse(success = false, accessToken = "null", message = "Error de conexión: ${e.localizedMessage}")
        }
    }
}
