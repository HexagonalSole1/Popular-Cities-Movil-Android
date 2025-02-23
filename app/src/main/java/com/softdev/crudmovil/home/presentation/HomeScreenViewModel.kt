package com.softdev.crudmovil.home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softdev.crudmovil.home.domain.dtos.request.CityRequest
import com.softdev.crudmovil.home.domain.dtos.response.CitiesResponse
import com.softdev.crudmovil.home.infrastructure.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CityViewModel(private val repository: HomeRepository) : ViewModel() {

    private val _cities = MutableStateFlow<List<CitiesResponse>>(emptyList())
    val cities = _cities.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadCities() {
        _isLoading.value = true
        viewModelScope.launch {
            val cityList = repository.getCities()
            _cities.value = cityList
            _isLoading.value = false
        }
    }
    fun createCity(city: CityRequest) {
        viewModelScope.launch {
            try {
                repository.createCity(city)
                loadCities() // Recargar la lista después de agregar una ciudad
            } catch (e: Exception) {
                Log.e("CityViewModel", "Error al crear ciudad", e)
            }
        }
    }

}
