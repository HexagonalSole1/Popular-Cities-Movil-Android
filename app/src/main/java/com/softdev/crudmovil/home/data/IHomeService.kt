package com.softdev.crudmovil.home.data
import com.softdev.crudmovil.core.network.RetrofitClient
import com.softdev.crudmovil.home.domain.dtos.response.CitiesResponse
import retrofit2.Response
import retrofit2.http.GET


interface IHomeService {
    @GET("cities") // Reemplaza con el endpoint real de tu API
    suspend fun getCities(): Response<List<CitiesResponse>>

    companion object {
        fun create(): IHomeService {
            return RetrofitClient.instance.create(IHomeService::class.java)
        }
    }
}