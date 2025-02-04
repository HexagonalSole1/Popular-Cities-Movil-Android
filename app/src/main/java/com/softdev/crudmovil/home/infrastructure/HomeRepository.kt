package com.softdev.crudmovil.home.infrastructure

import com.softdev.crudmovil.home.data.IHomeService
import com.softdev.crudmovil.home.domain.dtos.response.CitiesResponse


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
}
