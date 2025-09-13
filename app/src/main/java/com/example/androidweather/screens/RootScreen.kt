package com.example.androidweather.screens

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
fun RootScreen() {
    val info = currentWindowAdaptiveInfo()
    val isRegular = info.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED

    if (isRegular) {
        ListDetailScreen()
    } else {
        CompactScreen()
    }
}