package com.example.androidweather.models

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

enum class WeatherCondition {
    SUNNY,
    LIGHTCLOUDY,

    DARKCLOUDY,
    RAINY,
    SNOWY;

    fun gradientColors(): List<Color> = when (this) {
        SUNNY -> listOf(Color(0xFF2196F3), Color(0xFF0D47A1))
        LIGHTCLOUDY -> listOf(Color(0xFF90A4AE), Color(0xFF607D8B))
        DARKCLOUDY -> listOf(Color(0xFF333344), Color(0xFF222233))
        RAINY -> listOf(Color(0xFFB3E5FC), Color(0xFF0288D1))
        SNOWY -> listOf(Color(0xFFFFFFFF), Color(0xFFBBDEFB), Color(0xFF1976D2))
    }
}