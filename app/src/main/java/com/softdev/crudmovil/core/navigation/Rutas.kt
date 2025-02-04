package com.softdev.crudmovil.core.navigation

sealed class Rutas(val ruta: String) {
    object Login : Rutas("login")
    object Register : Rutas("register")
    object Home : Rutas("home")

}
