package com.softdev.crudmovil.home.infrastructure.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import androidx.health.connect.client.records.ExerciseRoute
import java.time.Instant

@SuppressLint("MissingPermission")
fun getCurrentLocation(context: Context, onLocationReceived: (ExerciseRoute.Location?) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation
        .addOnSuccessListener { location: Location? ->
            val exerciseRouteLocation = location?.toExerciseRouteLocation()
            onLocationReceived(exerciseRouteLocation)
        }
        .addOnFailureListener {
            Log.e("Location", "Error obteniendo ubicación", it)
            onLocationReceived(null) // Si hay error, enviamos `null`
        }
}

/**
 * Convierte `android.location.Location` en `ExerciseRoute.Location`
 */
fun Location.toExerciseRouteLocation(): ExerciseRoute.Location {
    return ExerciseRoute.Location(
        latitude = this.latitude,
        longitude = this.longitude,
        time = Instant.ofEpochMilli(this.time) // Convierte Long a Instant
    )
}
