package com.example.sample.network

import com.example.sample.network.model.RestaurantsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface Api {
    @GET("maps/api/place/nearbysearch/json?type=restaurant&radius=5000&key=AIzaSyCqWHKkgLxJiSwS63bxfWpQ-XhSQs65H5c")
    suspend fun fetchNearbyRestaurantsByKeyword(
        @Query("location") latLongString: String,
        @Query("keyword") searchText: String
    ): Response<RestaurantsResponse?>?

    @GET("maps/api/place/nearbysearch/json?type=restaurant&radius=5000&key=AIzaSyCqWHKkgLxJiSwS63bxfWpQ-XhSQs65H5c")
    suspend fun fetchNearbyRestaurants(@Query("location") latLongString: String): Response<RestaurantsResponse?>?
}