package com.example.sample.network.model

import com.google.gson.annotations.SerializedName

data class RestaurantsResponse(
    @SerializedName("html_attributions")
    val html_attributions: List<String>,
    @SerializedName("status")
    val status: String,
    @SerializedName("results")
    val results: List<RestaurantResponse>
)

data class RestaurantResponse(
    @SerializedName("business_status")
    val businessStatus: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price_level")
    val priceLevel: Int,
    @SerializedName("rating")
    val rating: Float,
    @SerializedName("icon")
    val iconUrl: String,
    @SerializedName("geometry")
    val geometry: GeometryResponse
)

data class GeometryResponse(
    @SerializedName("location")
    val location: LocationResponse,
)

data class LocationResponse(
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("lng")
    val longitude: Double,
)
