package com.example.androidweather.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.example.androidweather.models.WeatherDetail

// --- Detail Pane Composable ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailPane(
    itemDetails: WeatherDetail?,
    onNavigateBack: (() -> Unit)?, // Nullable if back navigation is handled differently on large screens
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            // Show TopAppBar with back button only if onNavigateBack is provided (typical for compact screens)
            if (onNavigateBack != null) {
                TopAppBar(
                    title = { Text(itemDetails?.title ?: "Details") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        },
        modifier = modifier.fillMaxSize()) { paddingValues ->
        if (itemDetails == null) {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Select an item to see details.")
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text(itemDetails.title, style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(8.dp))
                // You might use Coil or Glide to load an image here if itemDetails.imageUrl is not null
                if (itemDetails.imageUrl != null) {
                    // Placeholder for image
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(bottom = 8.dp)) {
                        Text("Imagine an image here: ${itemDetails.imageUrl}", modifier = Modifier.align(Alignment.Center))
                    }
                }
                Text(itemDetails.fullDescription, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}