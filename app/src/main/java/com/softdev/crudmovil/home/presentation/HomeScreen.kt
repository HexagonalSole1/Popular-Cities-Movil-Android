package com.softdev.crudmovil.home.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.softdev.crudmovil.home.domain.dtos.response.CitiesResponse
import com.softdev.crudmovil.home.domain.dtos.request.CityRequest
import com.softdev.crudmovil.home.infrastructure.HomeRepository

@Composable
fun HomeScreen(navController: NavHostController) {
    val cityService = IHomeService.create()
    val repository = HomeRepository(cityService)
    val viewModel: CityViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CityViewModel(repository) as T
        }
    })

    val cities by viewModel.cities.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCities()
    }
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF0D47A1)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(cities) { city ->
                    CityCard(city)
                }
            }
        }

        // Floating Action Button
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

        // Mostrar modal para agregar una ciudad
        if (showDialog) {
            AddCityDialog(
                onDismiss = { showDialog = false },
                onSave = { city ->
                    viewModel.createCity(city) // Enviar a la API
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun CityCard(city: CitiesResponse) {
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
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = city.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Población: ${city.population}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun AddCityDialog(onDismiss: () -> Unit, onSave: (CityRequest) -> Unit) {
    var name by remember { mutableStateOf("") }
    var population by remember { mutableStateOf("") }
    var urlPhoto by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Ciudad") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") }
                )
                OutlinedTextField(
                    value = population,
                    onValueChange = { population = it },
                    label = { Text("Población") }
                )
                OutlinedTextField(
                    value = urlPhoto,
                    onValueChange = { urlPhoto = it },
                    label = { Text("URL de la Foto") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotEmpty() && population.isNotEmpty() && urlPhoto.isNotEmpty()) {
                        val city = CityRequest(1,name, population.toInt(), urlPhoto)
                        onSave(city)
                    }
                }
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
