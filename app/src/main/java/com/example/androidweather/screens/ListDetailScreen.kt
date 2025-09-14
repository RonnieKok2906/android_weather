package com.example.androidweather.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidweather.viewModels.ListDetailViewModel
import com.example.androidweather.views.AnimatedListDetailPane
import com.example.androidweather.views.DetailPane
import com.example.androidweather.views.ListPane
import com.example.androidweather.views.ListDetailPane
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ListDetailScreen(
    listDetailViewModel: ListDetailViewModel = viewModel()
) {
    val uiState by listDetailViewModel.uiState.collectAsState()
    val navigator = rememberListDetailPaneScaffoldNavigator<String?>()
    val scope = rememberCoroutineScope()

    var menuVisible by remember { mutableStateOf(true) }

    val animateSideMenu = true

    LaunchedEffect(uiState.selectedItemId, uiState.showDetailPaneFullScreen) {
        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, uiState.selectedItemId)
    }

    // TOP BAR
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopStart
            ) {
                TopAppBar(
                    title = { Text("Weather") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = if(menuVisible) uiState.selectedWeather?.condition?.gradientColors()?.first() ?: Color.White else Color.Transparent,
                    ),
                    modifier = Modifier.fillMaxWidth(0.33f),
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                menuVisible = !menuVisible
                            }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            if(animateSideMenu) {
                AnimatedListDetailPane(
                    showList = menuVisible,
                    list = {
                        ListPane(
                            items = uiState.items,
                            onItemSelected = { id -> listDetailViewModel.selectItem(id) },
                            selectedItemId = uiState.selectedItemId,
                            modifier = Modifier.fillMaxSize()
                                .background(
                                    Brush.verticalGradient(colors = uiState.selectedWeather?.condition?.gradientColors() ?: listOf(Color(0xFF2196F3), Color(0xFF0D47A1)))
                                )
                        )
                    },
                    detail = {
                        val currentSelectedId: String? =
                            navigator.currentDestination
                                ?.takeIf { it.pane == ListDetailPaneScaffoldRole.Detail }
                                ?.contentKey

                        val detailsToDisplay =
                            if (currentSelectedId == uiState.selectedItemId) {
                                uiState.selectedItemDetails
                            } else {
                                null
                            }

                        DetailPane(
                            itemDetails = detailsToDisplay,
                            modifier = Modifier.fillMaxWidth(),
                            onNavigateBack = null
                        )
                    }
                )
            } else {
                ListDetailPane(
                    listDetailViewModel = listDetailViewModel,
                    menuVisible = menuVisible
                )
            }
        }
    }
}