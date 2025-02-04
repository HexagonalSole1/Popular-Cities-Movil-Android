package com.softdev.crudmovil.login.domain.Dtos.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)