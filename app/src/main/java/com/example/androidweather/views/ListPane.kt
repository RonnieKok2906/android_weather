package com.example.androidweather.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidweather.models.Weather

@Composable
fun ListPane(
    items: List<Weather>,
    selectedItemId: String?,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()) {
        items(items, key = { it.id }) { item ->
            WeatherCard(
                weather = item,
                selected = item.id == selectedItemId,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemSelected(item.id) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}