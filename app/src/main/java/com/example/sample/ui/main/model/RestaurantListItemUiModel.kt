package com.example.sample.ui.main.model

data class RestaurantListItemUiModel(
    val name: String,
    val iconUrl: String,
    val priceLevel: Int,
    val businessStatus: String,
    val rating: Int,
    val latitude: Double,
    val longitude: Double,
)
