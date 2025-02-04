package com.softdev.crudmovil.home.infrastructure

import IHomeService
import com.softdev.crudmovil.home.domain.dtos.request.CityRequest
import com.softdev.crudmovil.home.domain.dtos.response.CitiesResponse
import retrofit2.Response


class HomeRepository(private val cityService: IHomeService) {

    suspend fun getCities(): List<CitiesResponse> {
        return try {
            val response = cityService.getCities()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Crea una nueva ciudad en la API.
     */
    suspend fun createCity(city: CityRequest): Boolean {
        return try {
            val response: Response<Unit> = cityService.createCity(city)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
