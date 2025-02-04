package com.softdev.crudmovil.home.domain.dtos.response

data class CitiesResponse(
    val id: Int,
    val name: String,
    val population: Int,
    val urlPhoto: String
)
