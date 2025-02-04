package com.softdev.crudmovil.register.infrastructure


import com.softdev.crudmovil.register.data.network.RegisterService
import com.softdev.crudmovil.register.domain.dtos.request.RegisterRequest
import com.softdev.crudmovil.register.domain.dtos.response.RegisterResponse

class RegisterRepository(private val authService: RegisterService) {

    suspend fun register(username: String, email: String, password: String): RegisterResponse {
        return try {
            val response = authService.register(RegisterRequest(username, email, password))
            if (response.isSuccessful) {
                // Devuelve la respuesta del body o una respuesta predeterminada si está vacía
                response.body() ?: RegisterResponse(success = false, message = "Respuesta vacía")
            } else {
                // Maneja el caso de un error HTTP
                RegisterResponse(success = false, message = "Error al registrarse: ${response.message()}")
            }
        } catch (e: Exception) {
            // Maneja errores relacionados con la conexión o la ejecución
            RegisterResponse(success = false, message = "Error de conexión: ${e.localizedMessage}")
        }
    }
}
