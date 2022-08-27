package com.example.sample.repository

import androidx.annotation.VisibleForTesting
import com.example.sample.network.model.RestaurantsResponse
import com.example.sample.ui.main.model.Restaurant
import retrofit2.Response

object RestaurantResponseMapper {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun mapToRestaurant(response: Response<RestaurantsResponse?>?): List<Restaurant> {
        return response?.body()?.results?.map {
            Restaurant(
                name = it.name,
                iconUrl = it.iconUrl,
                priceLevel = it.priceLevel,
                businessStatus = it.businessStatus,
                rating = it.rating.toInt(),
                longitude = it.geometry.location.longitude,
                latitude = it.geometry.location.latitude,
            )
        }.orEmpty()
    }

    fun mapResponseToResult(restaurantResponse: Response<RestaurantsResponse?>?): RestaurantsResult {
        if (restaurantResponse == null || !restaurantResponse.isSuccessful) {
            return RestaurantsResult.Error("Error fetching restaurants")
        }

        if (restaurantResponse.body()?.results.isNullOrEmpty()) {
            return RestaurantsResult.Success(data = emptyList())
        }

        val restaurantList = mapToRestaurant(restaurantResponse)

        return RestaurantsResult.Success(restaurantList)
    }
}