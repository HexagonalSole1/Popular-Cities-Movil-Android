package com.softdev.crudmovil.home.domain.dtos.response

data class CitiesResponse(
    val id: Int,
    val name: String,
    val population: Int,
    val urlPhoto: String,
    val latitude: Double,  // Agregar estos campos
    val longitude: Double  // Agregar estos campos
)

