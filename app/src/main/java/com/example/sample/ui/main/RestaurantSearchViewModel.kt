package com.example.sample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sample.repository.RestaurantRepository
import com.example.sample.repository.RestaurantsResult
import com.example.sample.ui.main.model.Restaurant
import com.example.sample.ui.main.model.RestaurantListItemUiModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantSearchViewModel @Inject constructor(
    private val restaurantRepository: RestaurantRepository
): ViewModel() {
    private var _employeesListUiState = MutableLiveData<RestaurantListUiState>()
    val employeesListUiState: LiveData<RestaurantListUiState> = _employeesListUiState
    private var lastLatLong: LatLng? = null

    private fun updateUiState(response: RestaurantsResult) {
        if (response is RestaurantsResult.Success) {
            if (response.data.isEmpty()) {
                _employeesListUiState.value = RestaurantListUiState.Empty
            } else {
                val uiModels = mapRestaurantsToUiModel(response.data)
                _employeesListUiState.value = RestaurantListUiState.Data(uiModels)
            }
        } else {
            val errorMessage = (response as RestaurantsResult.Error).message
            _employeesListUiState.value = RestaurantListUiState.Error(errorMessage)
        }
    }

    private fun mapRestaurantsToUiModel(response: List<Restaurant>): List<RestaurantListItemUiModel> {
        return response.map {
            RestaurantListItemUiModel(
                name = it.name,
                iconUrl = it.iconUrl,
                priceLevel = it.priceLevel,
                businessStatus = it.businessStatus,
                rating = it.rating,
                longitude = it.longitude,
                latitude = it.latitude,
            )
        }
    }

    fun searchRestaurantsByKeyword(searchText: String) {
        if (lastLatLong != null) {
            _employeesListUiState.value = RestaurantListUiState.Loading

            viewModelScope.launch {
                val response = restaurantRepository.getNearbyRestaurantsByKeyword(
                    searchText = searchText,
                    longitude = lastLatLong!!.longitude,
                    latitude = lastLatLong!!.latitude
                )
                updateUiState(response)
            }
        } else {
            onLocationNotAcquired()
        }
    }

    fun onLocationUpdated(latitude: Double, longitude: Double) {
        _employeesListUiState.value = RestaurantListUiState.Loading

        lastLatLong = LatLng(latitude, longitude)

        viewModelScope.launch {
            val response = restaurantRepository.getNearbyRestaurants(latitude, longitude)
            updateUiState(response)
        }
    }

    fun onPermissionsNotGranted() {
        _employeesListUiState.value = RestaurantListUiState.Error("Location Permissions Not Granted")
    }

    fun onLocationNotAcquired() {
        _employeesListUiState.value = RestaurantListUiState.Error("Device location not available")
    }
}

sealed class RestaurantListUiState {
    object Loading: RestaurantListUiState()
    object Empty: RestaurantListUiState()
    data class Data(val restaurants: List<RestaurantListItemUiModel>): RestaurantListUiState()
    data class Error(val errorMessage: String): RestaurantListUiState()
}
