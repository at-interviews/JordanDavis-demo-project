package com.example.sample.repository


interface RestaurantRepository {
    suspend fun getNearbyRestaurantsByKeyword(
        searchText: String,
        latitude: Double,
        longitude: Double
    ): RestaurantsResult

    suspend fun getNearbyRestaurants(latitude: Double, longitude: Double): RestaurantsResult
}