package com.example.androidweather.models

data class Weather(val id: String, val title: String, val summary: String)

// Sample Data (in a ViewModel or repository)
val sampleItems = List<Weather>(20) { index ->
    Weather(
        id = "item_$index",
        title = "Item ${index + 1}",
        summary = "This is a short summary for item ${index + 1}."
    )
}