package com.softdev.crudmovil.register.domain.dtos.response

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse (
    val success:Boolean,
    val message:String
)