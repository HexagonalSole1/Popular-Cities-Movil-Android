package com.softdev.crudmovil.login.data.network
import AuthResponse
import com.softdev.crudmovil.core.network.RetrofitClient
import com.softdev.crudmovil.login.domain.Dtos.request.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("token") // Endpoint del login en tu API
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    companion object {
        fun create(): AuthService {
            return RetrofitClient.instance.create(AuthService::class.java)
        }
    }
}
