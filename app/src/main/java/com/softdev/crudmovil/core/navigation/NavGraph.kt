package com.softdev.crudmovil.core.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.softdev.crudmovil.home.presentation.HomeScreen
import com.softdev.crudmovil.login.presentation.LoginScreen
import com.softdev.crudmovil.register.presentation.RegisterScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.Login.ruta) {
        composable(Rutas.Login.ruta) {
            LoginScreen(navController = navController)
        }
        composable(Rutas.Register.ruta) {
            RegisterScreen(navController = navController)
        }
        composable(Rutas.Home.ruta) {
            HomeScreen(navController = navController)
        }
    }
}
