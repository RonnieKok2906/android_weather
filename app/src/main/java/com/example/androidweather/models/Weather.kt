package com.example.androidweather.models

data class Weather(val id: String,
                   val city: String,
                   var time: String,
                   var currentTemp: String,
                   val minimumTemp: String,
                   val maximumTemp: String,
                   val weatherSummary: String)

// Sample Data (in a ViewModel or repository)
val sampleItems: List<Weather> = listOf(
    Weather(
        id = "item_1",
        city = "Haarlem",
        time = "23:29",
        minimumTemp = "20°C",
        maximumTemp = "30°C",
        currentTemp = "25°C",
        weatherSummary = "Regen verwacht in het komende uur."
    ),
    Weather(
        id = "item_2",
        city = "Amsterdam",
        time = "23:29",
        minimumTemp = "18°C",
        maximumTemp = "28°C",
        currentTemp = "22°C",
        weatherSummary = "Motregen."
    ),
    Weather(
        id = "item_3",
        city = "Osaka",
        time = "23:29",
        minimumTemp = "18°C",
        maximumTemp = "28°C",
        currentTemp = "22°C",
        weatherSummary = "Zwaarbewolkt."
    )
)