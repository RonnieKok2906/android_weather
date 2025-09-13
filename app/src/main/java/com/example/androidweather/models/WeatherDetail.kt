package com.example.androidweather.models

data class WeatherDetail(val id: String, val city: String, val fullDescription: String, val imageUrl: String? = null)

val sampleItemDetails = sampleItems.associate { item ->
    item.id to WeatherDetail(
        id = item.id,
        city = item.city,
        fullDescription = "This is the full, detailed description for ${item.city}. It contains much more information than the summary shown in the list. Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
        imageUrl = if (item.id.hashCode() % 3 == 0) "https://example.com/image${item.id.hashCode() % 5}.jpg" else null // Placeholder
    )
}