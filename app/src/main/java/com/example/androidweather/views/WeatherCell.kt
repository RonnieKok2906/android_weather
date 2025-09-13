package com.example.androidweather.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidweather.models.Weather

@Composable
fun WeatherCard(weather: Weather, selected: Boolean, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = if (selected) 5.dp else 0.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(16.dp)
            )
            .height(120.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF333344), Color(0xFF222233))
                        )
                    )
            )

            Row(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(weather.city, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimary)
                    Text(weather.time, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimary)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(weather.weatherSummary, modifier = Modifier.widthIn(max = 130.dp), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimary)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(weather.currentTemp, style = MaterialTheme.typography.displayLarge, color = MaterialTheme.colorScheme.onPrimary)

                    Row {
                        Text("Max: ${weather.maximumTemp}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary)
                        Spacer(Modifier.width(8.dp))
                        Text("Min: ${weather.minimumTemp}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Weather cell",
    showBackground = true,
    showSystemUi = false
)
@Composable
fun WeatherCardPreview() {
    Box(Modifier.padding(16.dp)) {
        WeatherCard(weather = Weather(
            id = "item_1",
            city = "Haarlem",
            time = "23:29",
            minimumTemp = "20°C",
            maximumTemp = "30°C",
            currentTemp = "25°C",
            weatherSummary = "This is a short summary for item 1."
        ),
            selected = true)
    }
}