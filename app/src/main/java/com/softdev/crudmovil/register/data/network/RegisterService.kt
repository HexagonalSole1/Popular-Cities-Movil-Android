package com.softdev.crudmovil.register.data.network

import AuthResponse
import com.softdev.crudmovil.core.network.RetrofitClient
import com.softdev.crudmovil.register.domain.dtos.request.RegisterRequest
import com.softdev.crudmovil.register.domain.dtos.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    companion object {
        fun create(): RegisterService {
            return RetrofitClient.instance.create(RegisterService::class.java)
        }
    }
}
