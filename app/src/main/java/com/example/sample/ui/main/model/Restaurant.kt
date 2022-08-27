package com.example.sample.ui.main.model

data class Restaurant(
    val name: String,
    val iconUrl: String,
    val priceLevel: Int,
    val businessStatus: String,
    val rating: Int,
    val longitude: Double,
    val latitude: Double,
)
