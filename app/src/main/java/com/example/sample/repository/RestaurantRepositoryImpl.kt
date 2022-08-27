package com.example.sample.repository

import com.example.sample.network.Api
import com.example.sample.ui.main.model.Restaurant
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val api: Api
): RestaurantRepository {

    override suspend fun getNearbyRestaurantsByKeyword(
        searchText: String,
        latitude: Double,
        longitude: Double
    ): RestaurantsResult {
        val locationString = "$latitude,$longitude"

        return try {
            val restaurantResult = api.fetchNearbyRestaurantsByKeyword(searchText, locationString)
            return RestaurantResponseMapper.mapResponseToResult(restaurantResult)
        } catch (ex: Exception) {
            RestaurantsResult.Error("Network Exception")
        }
    }

    override suspend fun getNearbyRestaurants(
        latitude: Double,
        longitude: Double
    ): RestaurantsResult {
        val locationString = "$latitude,$longitude"

        return try {
            val restaurantResult = api.fetchNearbyRestaurants(locationString)
            RestaurantResponseMapper.mapResponseToResult(restaurantResult)
        } catch (ex: Exception) {
            RestaurantsResult.Error("Network Exception")
        }
    }
}

sealed class RestaurantsResult {
    data class Error(val message: String): RestaurantsResult()
    data class Success(val data: List<Restaurant>): RestaurantsResult()
}