package com.softdev.crudmovil.home.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.accompanist.permissions.*
import com.google.android.gms.location.LocationServices
import com.softdev.crudmovil.home.domain.dtos.response.CitiesResponse
import com.softdev.crudmovil.home.domain.dtos.request.CityRequest
import com.softdev.crudmovil.home.infrastructure.HomeRepository
import kotlinx.coroutines.tasks.await


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    var userLocation by remember { mutableStateOf<Location?>(null) }

    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            userLocation = getCurrentLocation(context)
        }
    }

    // ViewModel con Factory corregida
    val cityService = IHomeService.create()
    val repository = HomeRepository(cityService)
    val viewModel: CityViewModel = viewModel(factory = CityViewModelFactory(repository))

    // Corregimos la recolección de datos
    val cities by viewModel.cities.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState(initial = false)

    // Cargar ciudades en el primer render
    LaunchedEffect(Unit) {
        viewModel.loadCities()
    }

    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF0D47A1)
            )
            cities.isEmpty() -> Text(
                "No hay ciudades disponibles",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 18.sp
            )
            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(cities, key = { it.id }) { city ->
                    CityCard(city, userLocation)
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            containerColor = Color(0xFF0D47A1),
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("+", fontSize = 24.sp)
        }

        if (showDialog) {
            AddCityDialog(
                onDismiss = { showDialog = false },
                onSave = { city ->
                    viewModel.createCity(city)
                    showDialog = false
                }
            )
        }
    }
}

@SuppressLint("MissingPermission")
suspend fun getCurrentLocation(context: Context): Location? {
    return try {
        LocationServices.getFusedLocationProviderClient(context).lastLocation.await()
    } catch (e: Exception) {
        Log.e("Location", "Error obteniendo ubicación", e)
        null
    }
}

@Composable
fun CityCard(city: CitiesResponse, userLocation: Location?) {
    val distance = remember(userLocation, city.latitude, city.longitude) {
        userLocation?.let {
            city.latitude?.let { lat ->
                city.longitude?.let { lon ->
                    distanceBetween(it.latitude, it.longitude, lat, lon)
                }
            }
        }
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = city.urlPhoto,
                contentDescription = city.name,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = city.name, style = MaterialTheme.typography.titleLarge, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Población: ${city.population}", style = MaterialTheme.typography.bodyMedium, fontSize = 14.sp)
                distance?.let {
                    Text(text = "Distancia: %.2f km".format(it), style = MaterialTheme.typography.bodyMedium, fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
    }
}

fun distanceBetween(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
    val results = FloatArray(1)
    android.location.Location.distanceBetween(lat1, lon1, lat2, lon2, results)
    return results[0] / 1000
}

@Composable
fun AddCityDialog(onDismiss: () -> Unit, onSave: (CityRequest) -> Unit) {
    var name by remember { mutableStateOf("") }
    var population by remember { mutableStateOf("") }
    var urlPhoto by remember { mutableStateOf("") }
    val isValid by remember { derivedStateOf { name.isNotBlank() && population.toIntOrNull() != null && urlPhoto.isNotBlank() } }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Ciudad") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                OutlinedTextField(value = population, onValueChange = { population = it }, label = { Text("Población") }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))
                OutlinedTextField(value = urlPhoto, onValueChange = { urlPhoto = it }, label = { Text("URL de la Foto") })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(CityRequest(1, name, population.toInt(), urlPhoto))
                },
                enabled = isValid
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

class CityViewModelFactory(private val repository: HomeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CityViewModel(repository) as T
    }
}
