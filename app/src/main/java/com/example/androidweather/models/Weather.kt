package com.example.androidweather.models

data class Weather(val id: String,
                   val city: String,
                   var time: String,
                   var currentTemp: String,
                   val minimumTemp: String,
                   val maximumTemp: String,
                   val weatherSummary: String,
                   var condition: WeatherCondition)

// Sample Data (in a ViewModel or repository)
val sampleItems: List<Weather> = listOf(
    Weather(
        id = "item_1",
        city = "Osaka",
        time = "23:29",
        minimumTemp = "28°C",
        maximumTemp = "35°C",
        currentTemp = "35°C",
        weatherSummary = "Veel zon in de komende uur.",
        condition = WeatherCondition.SUNNY
    ),
    Weather(
        id = "item_2",
        city = "Amsterdam",
        time = "23:29",
        minimumTemp = "18°C",
        maximumTemp = "22°C",
        currentTemp = "22°C",
        weatherSummary = "Regen tot 17 uur verwacht.",
        condition = WeatherCondition.RAINY
    ),
    Weather(
        id = "item_4",
        city = "Düsseldorf",
        time = "23:29",
        minimumTemp = "18°C",
        maximumTemp = "23°C",
        currentTemp = "22°C",
        weatherSummary = "Lichtbewolkt.",
        condition = WeatherCondition.LIGHTCLOUDY
    ),
    Weather(
        id = "item_5",
        city = "Tokyo",
        time = "23:29",
        minimumTemp = "18°C",
        maximumTemp = "28°C",
        currentTemp = "22°C",
        weatherSummary = "Zwaarbewolkt.",
        condition = WeatherCondition.DARKCLOUDY
    ),
    Weather(
        id = "item_6",
        city = "London",
        time = "23:29",
        minimumTemp = "-3°C",
        maximumTemp = "2°C",
        currentTemp = "-1°C",
        weatherSummary = "Sneeuw verwacht tot 11 uur.",
        condition = WeatherCondition.SNOWY
    )
)