package com.example.androidweather.screens

import androidx.compose.foundation.layout.Box
import kotlinx.coroutines.launch
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import com.example.androidweather.viewModels.ListDetailViewModel
import com.example.androidweather.views.ItemDetailPane
import com.example.androidweather.views.ItemListPane
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ListDetailScreen(
    listDetailViewModel: ListDetailViewModel = viewModel()
) {
    val uiState by listDetailViewModel.uiState.collectAsState()
    val navigator = rememberListDetailPaneScaffoldNavigator<String?>()
    val scope = rememberCoroutineScope()

    val info = currentWindowAdaptiveInfo()
    val isRegular = info.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED

    var menuVisible by remember { mutableStateOf(true) }

    LaunchedEffect(uiState.selectedItemId, uiState.showDetailPaneFullScreen) {
        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, uiState.selectedItemId)
    }

    // TOP BAR
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather") },
                navigationIcon = if (isRegular) { {
                        IconButton(onClick = {
                            scope.launch {
                                menuVisible = !menuVisible
                            }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                } else {
                    {}
                }
            )
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            ListDetailPaneScaffold(
                directive = navigator.scaffoldDirective,
                value = navigator.scaffoldValue,

                // LIST
                listPane = {
                    if (menuVisible) {
                        AnimatedPane {
                            ItemListPane(
                                items = uiState.items,
                                onItemSelected = { id ->
                                    listDetailViewModel.selectItem(id)

                                    scope.launch {
                                        navigator.navigateTo(
                                            ListDetailPaneScaffoldRole.Detail,
                                            contentKey = id
                                        )
                                    }
                                },
                                modifier = Modifier
                            )
                        }
                    }
                },
                // DETAIL
                detailPane = {
                    AnimatedPane {
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

                        ItemDetailPane(
                            itemDetails = detailsToDisplay,
                            onNavigateBack = null
                        )
                    }
                }
            )
        }
    }
}