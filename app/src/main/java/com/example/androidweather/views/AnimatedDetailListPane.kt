package com.example.androidweather.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun AnimatedDetailListPane(
    showList: Boolean,
    list: @Composable () -> Unit,
    detail: @Composable () -> Unit
) {
    val listFraction by animateFloatAsState(
        targetValue = if (showList) 0.33f else 0f,
        label = "listWidthFraction"
    )

    Row(Modifier.fillMaxSize()) {
        if (listFraction > 0.001f) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .weight(listFraction)
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = showList,
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    list()
                }
            }
        }
        Box(
            Modifier
                .fillMaxHeight()
                .weight(1f - listFraction)
        ) {
            detail()
        }
    }
}