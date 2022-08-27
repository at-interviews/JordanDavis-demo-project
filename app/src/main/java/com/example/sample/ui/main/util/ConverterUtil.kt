package com.example.sample.ui.main.util

object ConverterUtil {
    private const val MILES_PER_METER = 0.0006213712
    fun convertMetersToMiles(meters: Float) = (meters * MILES_PER_METER)
}