package com.softdev.crudmovil.home.domain.dtos.request

data class CityRequest(
    val id : Int,
    val name: String,
    val population: Int,
    val urlPhoto: String
)